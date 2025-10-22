package com.contraomnese.weather.presentation.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.getOrElse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.contraomnese.weather.presentation.utils.retryIfError
import com.contraomnese.weather.presentation.utils.supervisorLaunch

internal class MviViewModel<Action : MviAction, Effect : MviEffect, Event : MviEvent, State : MviState>(
    private val tag: String,
    private val defaultState: State,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val reducer: (Effect, State) -> State = { _, state -> state },
    private val bootstrap: suspend () -> Unit = {},
    private val actor: suspend (Action) -> Unit = {},
) : Mvi<Action, Effect, Event, State> {

    private val logger = MviLogger<Action, Effect, Event, State>(tag)

    private val eventChannel: Channel<Event> = Channel(UNLIMITED)
    private val effectChannel: Channel<Effect> = Channel(UNLIMITED)
    private val actionChannel: Channel<Action> = Channel(UNLIMITED)

    private val bufferStateFlow: MutableSharedFlow<State> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val actionJobs = mutableMapOf<Class<out Action>, Job?>()

    override val eventFlow: Flow<Event> by lazy {
        eventChannel
            .receiveAsFlow()
            .onEach(logger::log)
            .flowOn(dispatcher)
    }

    override val stateFlow: StateFlow<State> by lazy {
        bufferStateFlow
            .onStart { logger.log(defaultState) }
            .onStart {
                scope.launch {
                    actionChannel
                        .receiveAsFlow()
                        .onEach(logger::log)
                        .onEach { action ->
                            actionJobs[action::class.java]?.cancel()
                            actionJobs[action::class.java] = launchActor(action, actor)
                        }
                        .retryIfError()
                        .launchIn(this)

                    effectChannel
                        .receiveAsFlow()
                        .onEach(logger::log)
                        .map { reducer(it, stateFlow.value) }
                        .onEach(bufferStateFlow::emit)
                        .onEach(logger::log)
                        .retryIfError()
                        .launchIn(this)

                    launchBootstrap(bootstrap)
                }
            }
            .distinctUntilChanged()
            .flowOn(dispatcher)
            .retryIfError()
            .stateIn(scope, SharingStarted.Lazily, defaultState)
    }

    override fun push(action: Action) = actionChannel
        .trySend(action)
        .getOrElse { logger.log(action, it) }

    override fun push(effect: Effect) = effectChannel
        .trySend(effect)
        .getOrElse { logger.log(effect, it) }

    override fun push(event: Event) = eventChannel
        .trySend(event)
        .getOrElse { logger.log(event, it) }

    private suspend fun launchActor(
        action: Action,
        actor: suspend (Action) -> Unit,
    ) = supervisorLaunch(
        onError = logger::logActor,
        block = { actor(action) }
    )

    private suspend fun launchBootstrap(
        bootstrap: suspend () -> Unit,
    ) = supervisorLaunch(
        onError = logger::logBootstrap,
        block = { bootstrap(); logger.logBootstrap() }
    )
}
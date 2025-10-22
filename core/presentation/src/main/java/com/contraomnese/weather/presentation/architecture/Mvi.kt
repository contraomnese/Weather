package com.contraomnese.weather.presentation.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviAction

interface MviEvent

interface MviEffect

interface MviState {
    val isLoading: Boolean
    val log get() = this.toString()
}

interface Mvi
<Action : MviAction, Effect : MviEffect, Event : MviEvent, State : MviState> {
    val stateFlow: StateFlow<State>
    val eventFlow: Flow<Event>
    fun push(action: Action)
    fun push(effect: Effect)
    fun push(event: Event)
}

fun <Action : MviAction, Effect : MviEffect, Event : MviEvent, State : MviState> createMvi(
    tag: String,
    defaultState: State,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    reducer: (Effect, State) -> State = { _, state -> state },
    bootstrap: suspend () -> Unit = {},
    actor: suspend (Action) -> Unit = {},
): Mvi<Action, Effect, Event, State> = MviViewModel(
    tag = tag,
    defaultState = defaultState,
    scope = scope,
    dispatcher = dispatcher,
    reducer = reducer,
    bootstrap = bootstrap,
    actor = actor,
)

@Composable
fun <Event : MviEvent> Flow<Event>.collectEvent(
    onEvent: suspend (Event) -> Unit,
) = LaunchedEffect(Unit) { collect { onEvent(it) } }

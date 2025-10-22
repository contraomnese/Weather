package com.contraomnese.weather.presentation.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviDestination

abstract class MviModel<Action : MviAction, Effect : MviEffect, Event : MviEvent, State : MviState>(
    defaultState: State,
    tag: String,
) : Mvi<Action, Effect, Event, State>, ViewModel() {

    private val mvi: Mvi<Action, Effect, Event, State> by lazy {
        createMvi(
            tag = tag,
            defaultState = defaultState,
            scope = viewModelScope,
            dispatcher = Dispatchers.Default,
            reducer = ::reducer,
            actor = ::actor,
            bootstrap = ::bootstrap,
        )
    }

    final override val eventFlow: Flow<Event> get() = mvi.eventFlow
    final override val stateFlow: StateFlow<State> get() = mvi.stateFlow
    final override fun push(action: Action) = mvi.push(action)
    final override fun push(event: Event) = mvi.push(event)
    final override fun push(effect: Effect) = mvi.push(effect)
    protected open suspend fun bootstrap() = run { }
    protected open suspend fun actor(action: Action) = run { }
    protected open fun reducer(effect: Effect, previousState: State): State = previousState
}
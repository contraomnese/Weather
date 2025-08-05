package com.contraomnese.weather.presentation.architecture

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCase
import com.contraomnese.weather.domain.cleanarchitecture.usecase.UseCase
import com.contraomnese.weather.domain.cleanarchitecture.usecase.UseCaseWithRequest
import com.contraomnese.weather.presentation.R
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<ViewState : UiState, Event : Any>(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
) : ViewModel() {

    protected abstract fun initialState(): ViewState

    private val _uiState = MutableStateFlow(this.initialState())
    val uiState: StateFlow<ViewState> = _uiState.asStateFlow()

    private val useCaseExecutor by lazy {
        useCaseExecutorProvider(viewModelScope)
    }

    protected fun <OUTPUT> execute(
        useCase: UseCase<OUTPUT>,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {},
    ) {
        useCaseExecutor.execute(useCase, onSuccess, onException)
    }

    protected fun <INPUT, OUTPUT> execute(
        useCaseWithRequest: UseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        useCaseExecutor.execute(useCaseWithRequest, value, onSuccess, onException)
    }

    protected fun <OUTPUT> observe(
        useCase: StreamingUseCase<OUTPUT>,
        onEach: (OUTPUT) -> Unit,
        onException: (DomainException) -> Unit = {},
    ) {
        useCaseExecutor.observe(useCase, onEach, onException)
    }

    abstract fun onEvent(event: Event)

    private fun updateViewState(newViewState: ViewState) {
        _uiState.update {
            newViewState
        }
    }

    protected fun updateViewState(
        updatedState: ViewState.() -> ViewState,
    ) = updateViewState(_uiState.value.updatedState())

    protected fun observeNotificationEvents(): Flow<Int> = notificationMonitor.notifications

    protected fun showNotification(@StringRes notification: Int) {
        viewModelScope.launch {
            notificationMonitor.emit(notification)
        }
    }

    protected fun provideException(exception: DomainException) {
        when (exception) {
            is UnknownDomainException -> showNotification(R.string.unknown_domain_exception)
            else -> Log.d("BaseViewModel", "${exception.message}")
        }
    }
}
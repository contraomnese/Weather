package com.contraomnese.weather.presentation.architecture

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.cleanarchitecture.exception.AuthorizationDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.LocationNotFoundDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.RequestDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.WeatherApiUnavailableDomainException
import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCase
import com.contraomnese.weather.domain.cleanarchitecture.usecase.StreamingUseCaseWithRequest
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
        onException: (DomainException) -> Unit = {},
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

    protected fun <INPUT, OUTPUT> observe(
        useCaseWithRequest: StreamingUseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onEach: (OUTPUT) -> Unit,
        onException: (DomainException) -> Unit = {},
    ) {
        useCaseExecutor.observe(useCaseWithRequest, value, onEach, onException)
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

    private fun showNotification(@StringRes notification: Int) {
        viewModelScope.launch {
            notificationMonitor.emit(notification)
        }
    }

    protected fun provideException(exception: DomainException) {
        when (exception) {
            is AuthorizationDomainException -> showNotification(R.string.authorization_domain_exception)
            is LocationNotFoundDomainException -> showNotification(R.string.weather_for_location_not_found_domain_exception)
            is RequestDomainException -> showNotification(R.string.wrong_request_domain_exception)
            is WeatherApiUnavailableDomainException -> showNotification(R.string.weather_api_domain_exception)
            is UnknownDomainException -> showNotification(R.string.unknown_domain_exception)
            else -> showNotification(R.string.unforeseen_internal_exception)
        }
    }
}
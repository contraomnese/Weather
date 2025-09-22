package com.contraomnese.weather

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Immutable
internal sealed class MainActivityEvent

internal data class MainActivityUiState(
    override val isLoading: Boolean = false,
) : UiState {

    override fun loading(): UiState = copy(isLoading = true)
}

internal class MainActivityViewModel(
    notificationMonitor: NotificationMonitor,
    useCaseExecutorProvider: UseCaseExecutorProvider,
) : BaseViewModel<MainActivityUiState, MainActivityEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        viewModelScope.launch {
            delay(2000)
            updateViewState { copy(isLoading = false) }
        }
    }

    override fun initialState(): MainActivityUiState = MainActivityUiState(isLoading = true)

    override fun onEvent(event: MainActivityEvent) = Unit

    val notificationEvents: Flow<Int> = observeNotificationEvents()
}
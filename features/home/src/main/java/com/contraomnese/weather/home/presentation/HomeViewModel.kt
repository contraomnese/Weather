package com.contraomnese.weather.home.presentation

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider


@Immutable
internal data class HomeUiState(
    override val isLoading: Boolean = false,
    val city: String = "",
) : UiState {
    override fun loading(): UiState = copy(isLoading = true)
}

@Immutable
internal sealed interface HomeEvent {
    data class CityChanged(val newCity: String) : HomeEvent
}

internal class HomeViewModel(
    private val useCaseExecutorProvider: UseCaseExecutorProvider,
    private val notificationMonitor: NotificationMonitor,
) : BaseViewModel<HomeUiState, HomeEvent>(useCaseExecutorProvider, notificationMonitor) {


    override fun initialState(): HomeUiState = HomeUiState()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CityChanged -> TODO()
        }
    }
}
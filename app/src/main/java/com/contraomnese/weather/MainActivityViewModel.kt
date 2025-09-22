package com.contraomnese.weather

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.weatherByLocation.model.LocationInfoDomainModel
import com.contraomnese.weather.presentation.architecture.BaseViewModel
import com.contraomnese.weather.presentation.architecture.UiState
import com.contraomnese.weather.presentation.notification.NotificationMonitor
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow

@Immutable
internal sealed class MainActivityEvent

internal data class MainActivityUiState(
    override val isLoading: Boolean = true,
    val favorites: ImmutableList<LocationInfoDomainModel> = persistentListOf(),
) : UiState {

    override fun loading(): UiState = copy(isLoading = true)
}

internal class MainActivityViewModel(
    notificationMonitor: NotificationMonitor,
    useCaseExecutorProvider: UseCaseExecutorProvider,
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : BaseViewModel<MainActivityUiState, MainActivityEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        execute(getFavoritesUseCase, ::onFavoritesUpdate, ::provideException)
    }

    override fun initialState(): MainActivityUiState = MainActivityUiState(isLoading = true)

    override fun onEvent(event: MainActivityEvent) = Unit

    val notificationEvents: Flow<Int> = observeNotificationEvents()

    private fun onFavoritesUpdate(newFavorites: List<LocationInfoDomainModel>) {
        updateViewState { copy(favorites = newFavorites.toPersistentList(), isLoading = false) }
    }
}
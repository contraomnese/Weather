package com.contraomnese.weather

import androidx.compose.runtime.Immutable
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
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
internal sealed interface MainActivityEvent {
    data class AddFavorite(val location: LocationInfoDomainModel) : MainActivityEvent
}

internal data class MainActivityUiState(
    override val isLoading: Boolean = true,
    val favorites: ImmutableList<LocationInfoDomainModel> = persistentListOf(),
) : UiState {

    override fun loading(): UiState = copy(isLoading = true)
}

internal class MainActivityViewModel(
    notificationMonitor: NotificationMonitor,
    useCaseExecutorProvider: UseCaseExecutorProvider,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
) : BaseViewModel<MainActivityUiState, MainActivityEvent>(useCaseExecutorProvider, notificationMonitor) {

    init {
        observe(observeFavoritesUseCase, ::onFavoritesUpdate, ::provideException)
    }

    override fun initialState(): MainActivityUiState = MainActivityUiState(isLoading = true)

    override fun onEvent(event: MainActivityEvent) {
        when (event) {
            is MainActivityEvent.AddFavorite -> onFavoriteAdded(event.location)
        }
    }

    val notificationEvents: Flow<Int> = observeNotificationEvents()

    private fun onFavoritesUpdate(newFavorites: List<LocationInfoDomainModel>) {
        updateViewState { copy(favorites = newFavorites.toPersistentList(), isLoading = false) }
    }

    private fun onFavoriteAdded(location: LocationInfoDomainModel) {
        execute(
            addFavoriteUseCase,
            location,
            onException = ::provideException
        )
    }
}
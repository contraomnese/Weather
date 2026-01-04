package com.contraomnese.weather

import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.home.navigation.HomeDestination
import com.contraomnese.weather.presentation.architecture.MviDestination
import com.contraomnese.weather.presentation.architecture.MviState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

internal data class MainActivityState(
    override val isLoading: Boolean = true,
    val favorites: ImmutableList<Location> = persistentListOf(),
    val startDestination: MviDestination,
) : MviState {

    fun setFavorites(newFavorites: List<Location>): MainActivityState = copy(favorites = newFavorites.toPersistentList())

    companion object {
        val DEFAULT = MainActivityState(isLoading = true, startDestination = HomeDestination)
    }
}
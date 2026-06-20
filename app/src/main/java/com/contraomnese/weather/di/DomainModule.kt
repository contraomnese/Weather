package com.contraomnese.weather.di

import com.contraomnese.weather.domain.app.usecase.DisableForecastAutoSyncOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.DisableForecastAutoSyncOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.DisablePushNotificationUseCase
import com.contraomnese.weather.domain.app.usecase.DisablePushNotificationUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.EnableForecastAutoSyncOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.EnableForecastAutoSyncOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.EnablePushNotificationUseCase
import com.contraomnese.weather.domain.app.usecase.EnablePushNotificationUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.ObserveAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.SetPrecipitationUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetPrecipitationUnitOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.SetPressureUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetPressureUnitOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.SetTemperatureUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetTemperatureUnitOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.SetWindSpeedUnitOnAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.SetWindSpeedUnitOnAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCase
import com.contraomnese.weather.domain.app.usecase.UpdateAppSettingsUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.AddFavoriteUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.GetFavoritesUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetFirstFavoriteIdUseCase
import com.contraomnese.weather.domain.home.usecase.GetFirstFavoriteIdUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCase
import com.contraomnese.weather.domain.home.usecase.GetLocationsUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCase
import com.contraomnese.weather.domain.home.usecase.ObserveFavoritesUseCaseImpl
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCase
import com.contraomnese.weather.domain.home.usecase.RemoveFavoriteUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveFavoriteForecastsUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveFavoriteForecastsUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveSingleForecastUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.ObserveSingleForecastUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateFavoritesForecastsUseCaseImpl
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastUseCase
import com.contraomnese.weather.domain.weatherByLocation.usecase.UpdateForecastUseCaseImpl
import org.koin.dsl.module

val domainModule = module {

    factory<GetLocationsUseCase> {
        GetLocationsUseCaseImpl(
            repository = get()
        )
    }
    factory<GetLocationUseCase> {
        GetLocationUseCaseImpl(
            repository = get()
        )
    }
    factory<AddFavoriteUseCase> {
        AddFavoriteUseCaseImpl(
            repository = get()
        )
    }
    factory<RemoveFavoriteUseCase> {
        RemoveFavoriteUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveFavoritesUseCase> {
        ObserveFavoritesUseCaseImpl(
            repository = get()
        )
    }
    factory<GetFirstFavoriteIdUseCase> {
        GetFirstFavoriteIdUseCaseImpl(
            repository = get()
        )
    }
    factory<GetFavoritesUseCase> {
        GetFavoritesUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveSingleForecastUseCase> {
        ObserveSingleForecastUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveFavoriteForecastsUseCase> {
        ObserveFavoriteForecastsUseCaseImpl(
            repository = get()
        )
    }
    factory<UpdateForecastUseCase> {
        UpdateForecastUseCaseImpl(
            repository = get()
        )
    }
    factory<ObserveAppSettingsUseCase> {
        ObserveAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<UpdateAppSettingsUseCase> {
        UpdateAppSettingsUseCaseImpl(
            repository = get()
        )
    }

    factory<UpdateFavoritesForecastsUseCase> {
        UpdateFavoritesForecastsUseCaseImpl(
            repository = get()
        )
    }
    factory<SetTemperatureUnitOnAppSettingsUseCase> {
        SetTemperatureUnitOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<SetPressureUnitOnAppSettingsUseCase> {
        SetPressureUnitOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<SetPrecipitationUnitOnAppSettingsUseCase> {
        SetPrecipitationUnitOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<SetWindSpeedUnitOnAppSettingsUseCase> {
        SetWindSpeedUnitOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<EnablePushNotificationUseCase> {
        EnablePushNotificationUseCaseImpl(
            repository = get()
        )
    }
    factory<DisablePushNotificationUseCase> {
        DisablePushNotificationUseCaseImpl(
            repository = get()
        )
    }
    factory<EnableForecastAutoSyncOnAppSettingsUseCase> {
        EnableForecastAutoSyncOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
    factory<DisableForecastAutoSyncOnAppSettingsUseCase> {
        DisableForecastAutoSyncOnAppSettingsUseCaseImpl(
            repository = get()
        )
    }
}
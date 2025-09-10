package com.contraomnese.weather.appsettings.di

import com.contraomnese.weather.appsettings.presentation.AppSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appSettingsModule = module {

    viewModelOf(::AppSettingsViewModel)
}
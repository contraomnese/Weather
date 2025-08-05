package com.contraomnese.weather.home.di

import com.contraomnese.weather.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val homeModule = module {

    viewModelOf(::HomeViewModel)
}
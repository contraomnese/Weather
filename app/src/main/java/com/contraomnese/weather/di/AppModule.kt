package com.contraomnese.weather.di

import com.contraomnese.weather.MainActivityViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    viewModelOf(::MainActivityViewModel)
}
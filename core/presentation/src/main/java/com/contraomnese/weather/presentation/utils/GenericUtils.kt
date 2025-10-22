package com.contraomnese.weather.presentation.utils

import kotlin.reflect.KClass

val <T : KClass<*>> T.simpleNameOrThrow: String
    get() = simpleName ?: throw Throwable(
        message = "Implementation class does not have simpleName"
    )

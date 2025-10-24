package com.contraomnese.weather.presentation.utils

import android.content.Context
import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.presentation.R

fun provideException(exception: DomainException): Int {
    return when (exception) {
        is DomainException.Unknown -> R.string.unknown_domain_exception
        is DomainException.ApiUnavailable -> R.string.weather_api_domain_exception
        is DomainException.Unauthorized -> R.string.authorization_domain_exception
        is DomainException.BadRequest -> R.string.wrong_request_domain_exception
        is DomainException.NotFound -> R.string.weather_for_location_not_found_domain_exception
        is DomainException.RateLimitExceeded -> R.string.rate_limit_exception
        is DomainException.DatabaseError, is DomainException.OperationFailed -> R.string.operation_failed
        is DomainException.InitializeError -> R.string.not_initialize
    }
}

fun Throwable.handleError(context: Context) =
    context.getString(provideException(this as? DomainException ?: DomainException.Unknown("Unknown error")))
package com.contraomnese.weather.presentation.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.domain.cleanarchitecture.usecase.withRequest.UseCaseWithRequest
import com.contraomnese.weather.presentation.usecase.UseCaseExecutorProvider

abstract class BaseViewModel<VIEW_STATE : Any, NOTIFICATION : Any>(
    useCaseExecutorProvider: UseCaseExecutorProvider
) : ViewModel() {

    private val useCaseExecutor by lazy {
        useCaseExecutorProvider(viewModelScope)
    }

    protected fun <OUTPUT> execute(
        useCaseWithRequest: UseCaseWithRequest<Unit, OUTPUT>,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        execute(useCaseWithRequest, Unit, onSuccess, onException)
    }

    protected fun <INPUT, OUTPUT> execute(
        useCaseWithRequest: UseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        useCaseExecutor.execute(useCaseWithRequest, value, onSuccess, onException)
    }
}
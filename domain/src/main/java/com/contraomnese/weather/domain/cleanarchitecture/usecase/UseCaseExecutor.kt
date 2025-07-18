package com.contraomnese.weather.domain.cleanarchitecture.usecase

import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import com.contraomnese.weather.domain.cleanarchitecture.usecase.withRequest.UseCaseWithRequest
import com.contraomnese.weather.domain.cleanarchitecture.usecase.withoutRequest.UseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UseCaseExecutor(
    private val coroutineScope: CoroutineScope
) {
    fun <OUTPUT> execute(
        useCase: UseCase<OUTPUT>,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                useCase(onSuccess)
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    (throwable as? DomainException)
                        ?: UnknownDomainException(throwable)
                )
            }
        }
    }

    fun <INPUT, OUTPUT> execute(
        useCaseWithRequest: UseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {}
    ) {
        coroutineScope.launch {
            try {
                useCaseWithRequest(value, onSuccess)
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    (throwable as? DomainException)
                        ?: UnknownDomainException(throwable)
                )
            }
        }
    }
}
package com.contraomnese.weather.domain.cleanarchitecture.usecase

import android.util.Log
import com.contraomnese.weather.domain.cleanarchitecture.exception.DomainException
import com.contraomnese.weather.domain.cleanarchitecture.exception.UnknownDomainException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UseCaseExecutor(
    private val coroutineScope: CoroutineScope,
) {
    fun <OUTPUT> execute(
        useCase: UseCase<OUTPUT>,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {},
    ) {
        coroutineScope.launch {
            try {
                val result = useCase()
                onSuccess(result)
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    handleException(throwable)
                )
            }
        }
    }

    fun <INPUT, OUTPUT> execute(
        useCaseWithRequest: UseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onSuccess: (OUTPUT) -> Unit = {},
        onException: (DomainException) -> Unit = {},
    ) {
        coroutineScope.launch {
            try {
                val result = useCaseWithRequest(value)
                onSuccess(result)
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    handleException(throwable)
                )
            }
        }
    }

    fun <OUTPUT> observe(
        useCase: StreamingUseCase<OUTPUT>,
        onEach: (OUTPUT) -> Unit,
        onException: (DomainException) -> Unit = {},
    ) {
        coroutineScope.launch {
            try {
                useCase()
                    .collect {
                        onEach(it)
                    }

            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    handleException(throwable)
                )
            }
        }
    }

    fun <INPUT, OUTPUT> observe(
        useCase: StreamingUseCaseWithRequest<INPUT, OUTPUT>,
        value: INPUT,
        onEach: (OUTPUT) -> Unit,
        onException: (DomainException) -> Unit = {},
    ) {
        coroutineScope.launch {
            try {
                useCase(value)
                    .collect {
                        onEach(it)
                    }
            } catch (ignore: CancellationException) {
            } catch (throwable: Throwable) {
                onException(
                    handleException(throwable)
                )
            }
        }
    }

    private fun handleException(throwable: Throwable): DomainException {
        Log.e("UseCaseExecutor", "${throwable.message}")
        return (throwable as? DomainException) ?: UnknownDomainException(throwable)
    }

}
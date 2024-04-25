package sk.minv.base.base.interactor

import timber.log.Timber

sealed class Result<out T : Any> {
    fun isSuccess() = this is Success

    fun isFailure() = this is Failure<*>

    fun getOrThrow(): T {
        when (this) {
            is Success -> return this.value
            is Failure<*> -> throw this.exception
        }
    }

    fun exceptionOrNull(): Throwable? {
        return when (this) {
            is Success -> null
            is Failure<*> -> this.exception
        }
    }

    fun log() {
        Timber.i("Success: ${isSuccess()}")
        this.onSuccess {
            Timber.i("Result: $it")
        }.onFailure {
            Timber.w(exceptionOrNull())
        }
    }
}

data class Success<out T : Any>(val value: T) : Result<T>()

data class Failure<out E : Exception>(val exception: E) : Result<Nothing>()

inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> = also {
    if (this is Success) {
        action(value)
    }
}

inline fun <T : Any> Result<T>.onFailure(action: (Exception) -> Unit): Result<T> = also {
    if (this is Failure<Exception>) {
        action(exception)
    }
}

inline fun <T : Any> Result<T>.onFailed(action: (Failure<Exception>) -> Nothing): T = when (this) {
    is Success -> value
    is Failure<Exception> -> action(this)
}
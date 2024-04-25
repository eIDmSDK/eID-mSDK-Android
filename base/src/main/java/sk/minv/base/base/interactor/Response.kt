package sk.minv.base.base.interactor

import retrofit2.Response
import sk.minv.base.exceptions.ServerException
import sk.minv.base.exceptions.UnexpectedException

inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (isSuccessful) { body()?.let(action) }
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (ServerException) -> Unit) {
    if (isSuccessful) {
        if (body() == null) {
            action(ServerException("Response body is empty"))
        }
    } else {
        action(ServerException(code(), message()))
    }
}

fun <T : Any> Response<T>.getResult(): Result<T> {
    onSuccess { return Success(it) }
    onFailure { return Failure(it) }
    return Failure(UnexpectedException("Response parsing error"))
}
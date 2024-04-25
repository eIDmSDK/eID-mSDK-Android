package sk.minv.base.base.interactor

sealed class DataLoadState<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataLoadState<T>()
    data class Error<out T : Any>(val error: Throwable) : DataLoadState<T>()
    class Loading<out T : Any> : DataLoadState<T>()
}
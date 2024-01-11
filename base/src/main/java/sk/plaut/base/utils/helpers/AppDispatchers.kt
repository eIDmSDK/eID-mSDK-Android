package sk.plaut.base.utils.helpers

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class AppDispatchers {
    open val MAIN: CoroutineContext by lazy { Dispatchers.Main }
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
    open val DEFAULT: CoroutineContext by lazy { Dispatchers.Default }
}

open class TestDispatchers : AppDispatchers() {
    override val MAIN: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val IO: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val DEFAULT: CoroutineContext by lazy { Dispatchers.Unconfined }
}
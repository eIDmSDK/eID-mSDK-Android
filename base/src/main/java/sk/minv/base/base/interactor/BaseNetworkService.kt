package sk.minv.base.base.interactor

import kotlinx.coroutines.withContext
import retrofit2.Response
import sk.minv.base.exceptions.UnexpectedException
import sk.minv.base.utils.helpers.AppDispatchers

abstract class BaseNetworkService(private val appDispatchers: AppDispatchers) {

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    protected suspend fun <T : Any> callApi(apiFunction: suspend () -> Response<T>): Result<T> =
            withContext(appDispatchers.IO) {
                try {
                    apiFunction().getResult()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Failure(UnexpectedException(ex))
                }
            }
}
package sk.plaut.sample_app.interactors.usecase.userData

import android.os.Build
import sk.plaut.base.base.interactor.Result
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import sk.plaut.base.base.interactor.Failure
import sk.plaut.base.base.interactor.Success
import sk.plaut.base.exceptions.ApplicationException
import sk.plaut.base.utils.helpers.AppDispatchers
import sk.plaut.sample_app.data.GetUserDataParams
import sk.plaut.sample_app.data.UserData
import timber.log.Timber
import java.util.*

class GetUserDataUseCaseImpl(
    private val appDispatchers: AppDispatchers
) : GetUserDataUseCase {

    override suspend fun invoke(params: GetUserDataParams): Result<UserData> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return Failure(ApplicationException("Requires SDK 26"))
        }

        val parts = params.idToken.split(".")

        return withContext(appDispatchers.IO) {
            try {
                val charset = charset("UTF-8")
                // JWT header
                val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
                // JWT payload
                val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
                // Parse user data from JWT payload
                val gson = Gson()
                val userData = gson.fromJson(payload, UserData::class.java)
                // Return parsed user data
                Success(userData)
            } catch (ex: Exception) {
                Failure(ex)
            }
        }
    }
}
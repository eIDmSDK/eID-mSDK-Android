package sk.minv.sample_app.interactors.usecase.userData

import sk.minv.base.base.interactor.Result
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Failure
import sk.minv.base.base.interactor.Success
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.sample_app.data.GetUserDataParams
import sk.minv.sample_app.data.UserData
import java.util.*

class GetUserDataUseCaseImpl(
    private val appDispatchers: AppDispatchers
) : GetUserDataUseCase {

    override suspend fun invoke(params: GetUserDataParams): Result<UserData> {
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
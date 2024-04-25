package sk.minv.sample_app.interactors.usecase.generateHash

import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.sample_app.data.GenerateHashParams
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class GenerateHashUseCaseImpl(
    private val appDispatchers: AppDispatchers
) : GenerateHashUseCase {

    override suspend fun invoke(params: GenerateHashParams): Result<ByteArray> {
        return withContext(appDispatchers.IO) {
            // Generate SHA-256 HASH from entered string
            val digest256: MessageDigest = MessageDigest.getInstance("SHA-256")
            val hash256: ByteArray = digest256.digest(params.text.toByteArray(StandardCharsets.UTF_8))
            Success(hash256)
        }
    }
}
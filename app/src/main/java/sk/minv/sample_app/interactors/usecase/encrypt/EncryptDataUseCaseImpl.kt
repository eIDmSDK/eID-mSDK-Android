package sk.minv.sample_app.interactors.usecase.encrypt

import kotlinx.coroutines.withContext
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.sample_app.data.EncryptDataParams
import javax.crypto.Cipher

class EncryptDataUseCaseImpl(
    private val appDispatchers: AppDispatchers
) :  EncryptDataUseCase {

    override suspend fun invoke(params: EncryptDataParams): Result<ByteArray> {
        return withContext(appDispatchers.IO) {
            // Encrypt entered text with public key from Encryption (ENC) certificate
            val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, params.publicKey)
            val data: ByteArray = cipher.doFinal(params.text.toByteArray())
            Success(data)
        }
    }
}
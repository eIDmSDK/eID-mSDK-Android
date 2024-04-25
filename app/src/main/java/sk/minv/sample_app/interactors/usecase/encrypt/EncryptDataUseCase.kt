package sk.minv.sample_app.interactors.usecase.encrypt

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.EncryptDataParams

interface EncryptDataUseCase {

    suspend operator fun invoke(params: EncryptDataParams): Result<ByteArray>
}
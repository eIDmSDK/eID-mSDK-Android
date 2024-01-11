package sk.plaut.sample_app.interactors.usecase.encrypt

import sk.plaut.base.base.interactor.Result
import sk.plaut.sample_app.data.EncryptDataParams

interface EncryptDataUseCase {

    suspend operator fun invoke(params: EncryptDataParams): Result<ByteArray>
}
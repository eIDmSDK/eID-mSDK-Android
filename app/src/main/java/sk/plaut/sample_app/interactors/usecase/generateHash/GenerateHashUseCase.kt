package sk.plaut.sample_app.interactors.usecase.generateHash

import sk.plaut.base.base.interactor.Result
import sk.plaut.sample_app.data.GenerateHashParams

interface GenerateHashUseCase {

    suspend operator fun invoke(params: GenerateHashParams): Result<ByteArray>
}
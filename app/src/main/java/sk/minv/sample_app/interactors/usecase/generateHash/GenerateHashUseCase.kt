package sk.minv.sample_app.interactors.usecase.generateHash

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.GenerateHashParams

interface GenerateHashUseCase {

    suspend operator fun invoke(params: GenerateHashParams): Result<ByteArray>
}
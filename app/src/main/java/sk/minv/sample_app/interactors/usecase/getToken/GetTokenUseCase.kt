package sk.minv.sample_app.interactors.usecase.getToken

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetTokenResponse

interface GetTokenUseCase {

    suspend operator fun invoke(params: GetTokenParams): Result<GetTokenResponse>
}
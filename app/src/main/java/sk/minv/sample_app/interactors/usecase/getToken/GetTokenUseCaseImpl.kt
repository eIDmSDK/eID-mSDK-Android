package sk.minv.sample_app.interactors.usecase.getToken

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetTokenResponse
import sk.minv.sample_app.interactors.repository.AuthenticationRepository

class GetTokenUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : GetTokenUseCase {

    override suspend fun invoke(params: GetTokenParams): Result<GetTokenResponse> {
        return authenticationRepository.getToken(params)
    }
}
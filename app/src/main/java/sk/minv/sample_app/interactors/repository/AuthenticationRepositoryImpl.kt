package sk.minv.sample_app.interactors.repository

import sk.minv.sample_app.interactors.network.service.AuthenticationService
import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.base.base.interactor.onFailed
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetTokenResponse

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService
) : AuthenticationRepository {

    override suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse> {
        return authenticationService.getToken(params).onFailed {
            return it
        }.let {
            Success(it)
        }
    }
}
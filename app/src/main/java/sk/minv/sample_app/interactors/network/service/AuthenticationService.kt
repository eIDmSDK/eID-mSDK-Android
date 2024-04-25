package sk.minv.sample_app.interactors.network.service

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetTokenResponse

interface AuthenticationService {

    suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse>
}
package sk.minv.sample_app.interactors.network.service

import sk.minv.sample_app.interactors.network.api.AuthenticationApi
import sk.minv.base.base.interactor.BaseNetworkService
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetTokenResponse
import sk.minv.base.base.interactor.Result

class AuthenticationServiceImpl(
    appDispatchers: AppDispatchers,
    private val api: AuthenticationApi
) : BaseNetworkService(appDispatchers), AuthenticationService {

    override suspend fun getToken(params: GetTokenParams): Result<GetTokenResponse> = callApi {
        api.getToken(
            params.url,
            params.contentType,
            params.grantType,
            params.clientId,
            params.clientSecret,
            params.code,
            params.scope,
            params.redirectUri
        )
    }
}
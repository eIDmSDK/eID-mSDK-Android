package sk.minv.sample_app.interactors.network.api

import retrofit2.Response
import retrofit2.http.*
import sk.minv.sample_app.data.GetTokenResponse

interface AuthenticationApi {

    @FormUrlEncoded
    @POST
    suspend fun getToken(@Url url: String,
                         @Header("Content-type") contentType: String,
                         @Field("grant_type") grantType: String,
                         @Field("client_id") clientId: String,
                         @Field("client_secret") clientSecret: String,
                         @Field("code") code: String,
                         @Field("scope") scope: String,
                         @Field("redirect_uri") redirectUri: String
    ): Response<GetTokenResponse>
}
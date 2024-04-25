package sk.minv.sample_app.data

import com.google.gson.annotations.SerializedName

class GetTokenResponse {
    @SerializedName("access_token") lateinit var accessToken: String
    @SerializedName("id_token") lateinit var idToken: String
    @SerializedName("token_type") lateinit var tokenType: String
    @SerializedName("expires_in") lateinit var expiresIn: String
}
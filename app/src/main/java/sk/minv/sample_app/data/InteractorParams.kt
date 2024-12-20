package sk.minv.sample_app.data

import sk.minv.sample_app.utils.common.AppConstants
import java.security.PublicKey

data class GetUserDataParams(val idToken: String)
data class ParseCertificatesParams(val certificatesJson: String)
data class VerifyCertificateParams(val certificateDataEncoded: String)
data class GenerateHashParams(val text: String)
data class EncryptDataParams(val text: String, val publicKey: PublicKey)
data class GetTokenParams(
    val url: String,
    val contentType: String = AppConstants.CONTENT_TYPE,
    val grantType: String = AppConstants.GRANT_TYPE,
    val clientId: String,
    val clientSecret: String,
    val code: String,
    val scope: String = AppConstants.SCOPE,
    val redirectUri: String
)

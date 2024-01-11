package sk.plaut.sample_app.data

import java.security.PublicKey

data class GetUserDataParams(val idToken: String)
data class ParseCertificatesParams(val certificatesJson: String)
data class VerifyCertificateParams(val certificateDataEncoded: String)
data class GenerateHashParams(val text: String)
data class EncryptDataParams(val text: String, val publicKey: PublicKey)

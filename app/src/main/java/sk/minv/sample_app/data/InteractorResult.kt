package sk.minv.sample_app.data

import java.security.PublicKey

/*-------------------------*/
/*          ENUMS          */
/*-------------------------*/

enum class StartupResult {
    TUTORIAL, MAIN_SCREEN
}

enum class AppLanguage(val value: String?) {
    SYSTEM(null),
    SLOVAK("sk"),
    ENGLISH("en")
}

enum class AuthenticationFlow(val value: String) {
    AUTH_CODE("auth_code"),
    IMPLICIT("implicit"),
}

/*-------------------------*/
/*      DATA CLASSES       */
/*-------------------------*/

data class UserData(val given_name: String, val family_name: String)

data class CertificatesJsonObject(
    val cardType: String,
    val QSCD: Boolean,
    val certificates: List<CertificateJsonObject>
)

data class CertificateJsonObject(
    val slot: String,
    val certIndex: Int,
    val certData: String,
    val isQualified: Boolean,
    val supportedSchemes: List<String>
)

data class Certificate(
    val slot: String,
    val certIndex: Int,
    val serialNumber: String,
    val fullName: String,
    val supportedSchemesFormatted: String,
    val publicKey: PublicKey,
    val certificateDataEncoded: String
)

data class SignData(
    val certIndex: Int,
    val signatureScheme: String,
    val dataToSignEncoded: String
)

data class DecryptData(
    val dataToDecryptEncoded: String
)
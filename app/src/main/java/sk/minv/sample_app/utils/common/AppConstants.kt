package sk.minv.sample_app.utils.common

object AppConstants {
    // Rest API values
    const val AUTH_BASE_URL_MINV_TEST = "https://teidas.minv.sk/idp/profile/oidc/token"
    const val AUTH_BASE_URL_MINV_PROD = "https://eidas.minv.sk/idp/profile/oidc/token"
    const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    const val GRANT_TYPE = "authorization_code"
    const val SCOPE = "openid"
    const val REDIRECT_URI_MINV_TEST = "sk.minv.eid-test://authResult?success=true"
    const val REDIRECT_URI_MINV_PROD = "sk.minv.eid://authResult?success=true"
    const val CONNECTION_TIMEOUT = 60L

    // Auth EID SDK - ClientID and ClientSecret
    const val CLIENT_ID_MINV_TEST = "CLIENT_ID_MINV_TEST"
    const val CLIENT_SECRET_MINV_TEST = "CLIENT_SECRET_MINV_TEST"
    const val CLIENT_ID_MINV_PROD = "CLIENT_ID_MINV_PROD"
    const val CLIENT_SECRET_MINV_PROD = "CLIENT_SECRET_MINV_PROD"

    // Dialog tags
    const val DIALOG_TAG_PIN_MANAGEMENT = "PIN_MANAGEMENT"
}
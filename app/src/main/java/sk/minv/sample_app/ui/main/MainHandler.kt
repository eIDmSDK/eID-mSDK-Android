package sk.minv.sample_app.ui.main

import sk.minv.base.base.activity.BaseHandler

interface MainHandler : BaseHandler {

    fun openAuthenticationScreen(language: String?)

    fun openQrCodeReader()

    fun handleQrCode(qrCodeData: String?, language: String?)

    fun openCertificatesScreen(language: String?)

    fun openSigningScreen()

    fun openDecryptScreen()

    fun openPINManagementScreen(language: String?)

    fun openTutorialScreen(language: String?)

    fun openSettings()
}
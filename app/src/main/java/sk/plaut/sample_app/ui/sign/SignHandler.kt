package sk.plaut.sample_app.ui.sign

import sk.eid.eidhandlerpublic.EIDCertificateType
import sk.plaut.base.base.activity.BaseHandler
import sk.plaut.sample_app.data.SignData

interface SignHandler : BaseHandler {

    fun openGetCertificatesScreen(certificateType: EIDCertificateType, language: String?)

    fun openSignScreen(signDataParameters: SignData, language: String?)

    fun openPINManagementScreen(language: String?)
}
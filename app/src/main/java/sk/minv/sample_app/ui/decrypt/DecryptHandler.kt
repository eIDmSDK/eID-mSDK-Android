package sk.minv.sample_app.ui.decrypt

import sk.eid.eidhandlerpublic.EIDCertificateType
import sk.minv.base.base.activity.BaseHandler
import sk.minv.sample_app.data.DecryptData

interface DecryptHandler : BaseHandler {

    fun openGetCertificatesScreen(certificateType: EIDCertificateType, language: String?)

    fun openDecryptScreen(decryptDataParameters: DecryptData, language: String?)

    fun openPINManagementScreen(language: String?)
}
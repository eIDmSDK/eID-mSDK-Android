package sk.plaut.sample_app.ui.sign

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sk.eid.eidexceptions.CertificateNotFoundException
import sk.eid.eidexceptions.DeviceRootedException
import sk.eid.eidexceptions.PINBlockedException
import sk.eid.eidexceptions.PINNotActivatedException
import sk.eid.eidhandlerpublic.EIDCertificateType
import sk.plaut.base.base.fragment.BaseFragment
import sk.plaut.base.base.interactor.DataLoadState
import sk.plaut.base.ui.ConfirmDialogFragment
import sk.plaut.base.ui.MessageDialogFragment
import sk.plaut.base.utils.common.ByteUtils
import sk.plaut.base.utils.common.DialogUtils
import sk.plaut.base.utils.helpers.onClick
import sk.plaut.base.utils.helpers.scrollToBottom
import sk.plaut.base.utils.helpers.subscribe
import sk.plaut.sample_app.R
import sk.plaut.sample_app.data.Certificate
import sk.plaut.sample_app.data.SignData
import sk.plaut.sample_app.data.VerifyCertificateResult
import sk.plaut.sample_app.databinding.FragmentSignBinding
import sk.plaut.sample_app.utils.common.AppConstants
import sk.plaut.sample_app.utils.common.AppUtils
import sk.plaut.sample_app.utils.managers.Preferences

class SignFragment : BaseFragment<FragmentSignBinding, SignHandler>(), KoinComponent,
    ConfirmDialogFragment.ConfirmDialogListener, MessageDialogFragment.MessageDialogListener {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: SignViewModel by viewModel()
    private val preferences: Preferences by inject()

    private var language: String? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): SignFragment = SignFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSignBinding {
        return FragmentSignBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.getCertificatesLoadState.subscribe(this, ::handleGetCertificatesLoadState)
        viewModel.parseCertificatesLoadState.subscribe(this, ::handleParseCertificatesLoadState)
        viewModel.verifyCertificatesLoadState.subscribe(this, ::verifyCertificateLoadState)
        viewModel.generateHashLoadState.subscribe(this, ::handleGenerateHashLoadState)
        viewModel.signDataLoadState.subscribe(this, ::handleSignDataLoadState)
    }

    override fun onViewReady() {
        // Get language - Selected by user or system language
        language = context?.let {
            val configuration = it.resources.configuration
            AppUtils.getLocale(preferences, configuration)?.language
        }

        clearForm()

        binding.btnSign.onClick {
            clearForm()
            // Get text from form
            val formText = binding.editFormText.text.toString()
            // Generate HASH
            viewModel.signData(formText)
        }

        binding.btnContinue.onClick {
            viewModel.signGeneratedHash()
        }

        binding.btnVerify.onClick {
            viewModel.verifyCertificate()
        }

        binding.btnShowSignedHash.onClick {
            binding.txtSignedHash.visibility = View.VISIBLE
        }
    }

    override fun onConfirmDialogAction(action: ConfirmDialogFragment.ConfirmDialogAction?, tag: String?) {
        if (action == ConfirmDialogFragment.ConfirmDialogAction.POSITIVE_BUTTON_CLICK) {
            when (tag) {
                AppConstants.DIALOG_TAG_PIN_MANAGEMENT -> handler.openPINManagementScreen(language)
            }
        }
    }

    override fun onMessageDialogAction(tag: String?) { }

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    /**
     * Parse data from retrieved json
     */
    fun processReceivedCertificatesJson(certificatesJson: String?) {
        viewModel.parseCertificates(certificatesJson)
    }

    /**
     * Show signed HASH
     */
    fun processReceivedSignedData(signedDataEncoded: String?) {
        val signedData = Base64.decode(signedDataEncoded, Base64.NO_WRAP)
        binding.txtSignedHash.text = ByteUtils.bytesToHexString(signedData)
        binding.signedFormLayout.visibility = View.VISIBLE
        scrollToBottom()
    }

    /**
     * Show error
     */
    fun onEidExceptionReceived(exception: Throwable?) {
        exception?.let {
            when (it) {
                is DeviceRootedException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__device_rooted))
                is CertificateNotFoundException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__certificate_not_found))
                is PINNotActivatedException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__pin_not_activated))
                is PINBlockedException -> {
                    showConfirmDialog(
                        getString(R.string.general__error),
                        getString(R.string.error__message__pin_blocked),
                        getString(R.string.error__action__pin_blocked),
                        AppConstants.DIALOG_TAG_PIN_MANAGEMENT
                    )
                }
                is SecurityException -> showMessageDialog(getString(R.string.general__error), "SecurityException")
            }
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun handleGetCertificatesLoadState(dataLoadState: DataLoadState<Boolean>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                var certificateType = EIDCertificateType.QES
                if (binding.btnEs.isChecked) {
                    certificateType = EIDCertificateType.ES
                }
                handler.openGetCertificatesScreen(certificateType, language)
            }
            is DataLoadState.Error -> { }
        }
    }

    /**
     * Show loaded certificate
     */
    private fun handleParseCertificatesLoadState(dataLoadState: DataLoadState<Certificate>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                val certificate = dataLoadState.data
                binding.txtCertificateSlot.text = certificate.slot
                binding.txtCertificateIndex.text = certificate.certIndex.toString()
                binding.txtCertificateSerialNumber.text = certificate.serialNumber
                binding.txtCertificateName.text = certificate.fullName
                binding.txtCertificateSupportedScemes.text = certificate.supportedSchemesFormatted
                binding.certificatesLayout.visibility = View.VISIBLE
            }
            is DataLoadState.Error -> showErrorDialog(dataLoadState.error)
        }
    }

    /**
     * Show certificate verification result
     */
    private fun verifyCertificateLoadState(dataLoadState: DataLoadState<VerifyCertificateResult>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> {
                binding.btnVerify.visibility = View.GONE
                binding.progressVerify.visibility = View.VISIBLE
            }
            is DataLoadState.Success -> {
                binding.txtCertificateExpiration.text = dataLoadState.data.result.expiration
                binding.txtCertificateVerification.text = dataLoadState.data.result.verification
                binding.btnVerify.visibility = View.VISIBLE
                binding.progressVerify.visibility = View.GONE
            }
            is DataLoadState.Error -> showErrorDialog(dataLoadState.error)
        }
    }

    /**
     * Show generated HASH
     */
    private fun handleGenerateHashLoadState(dataLoadState: DataLoadState<ByteArray>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                binding.txtHash.text = ByteUtils.bytesToHexString(dataLoadState.data)
                binding.hashLayout.visibility = View.VISIBLE
                binding.btnContinue.visibility = View.VISIBLE
                scrollToBottom()
            }
            is DataLoadState.Error -> showErrorDialog(dataLoadState.error)
        }
    }

    /**
     * Open Signing screen from eID SDK
     */
    private fun handleSignDataLoadState(dataLoadState: DataLoadState<SignData>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> handler.openSignScreen(dataLoadState.data, language)
            is DataLoadState.Error -> showErrorDialog(dataLoadState.error)
        }
    }

    private fun clearForm() {
        // Clear - previously generated HASH and loaded certificates
        viewModel.clearData()

        // Certificates layout
        binding.txtCertificateSlot.text = null
        binding.txtCertificateIndex.text = null
        binding.txtCertificateSerialNumber.text = null
        binding.txtCertificateName.text = null
        binding.txtCertificateSupportedScemes.text = null
        binding.txtCertificateExpiration.text = null
        binding.txtCertificateVerification.text = null
        binding.certificatesLayout.visibility = View.GONE

        // HASH layout
        binding.txtHash.text = null
        binding.hashLayout.visibility = View.GONE
        binding.btnContinue.visibility = View.GONE

        // Signed HASH layout
        binding.txtSignedHash.text = null
        binding.signedFormLayout.visibility = View.GONE
    }

    private fun scrollToBottom() {
        binding.scrollView.scrollToBottom()
    }

    private fun showConfirmDialog(title: String, message: String, positiveButton: String, tag: String) {
        DialogUtils.showConfirmDialog(title, message, positiveButton, getString(R.string.general__cancel), childFragmentManager, tag)
    }

    private fun showMessageDialog(title: String, message: String) {
        DialogUtils.showMessageDialog(title, message,getString(R.string.general__ok), childFragmentManager)
    }
}
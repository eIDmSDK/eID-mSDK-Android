package sk.minv.sample_app.ui.decrypt

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
import sk.minv.base.base.fragment.BaseFragment
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.ui.ConfirmDialogFragment
import sk.minv.base.ui.MessageDialogFragment
import sk.minv.base.utils.common.ByteUtils
import sk.minv.base.utils.common.DialogUtils
import sk.minv.base.utils.helpers.onClick
import sk.minv.base.utils.helpers.scrollToBottom
import sk.minv.base.utils.helpers.subscribe
import sk.minv.sample_app.R
import sk.minv.sample_app.data.Certificate
import sk.minv.sample_app.data.DecryptData
import sk.minv.sample_app.data.VerifyCertificateResult
import sk.minv.sample_app.databinding.FragmentDecryptBinding
import sk.minv.sample_app.utils.common.AppConstants
import sk.minv.sample_app.utils.common.AppUtils
import sk.minv.sample_app.utils.managers.Preferences

class DecryptFragment : BaseFragment<FragmentDecryptBinding, DecryptHandler>(), KoinComponent,
    ConfirmDialogFragment.ConfirmDialogListener, MessageDialogFragment.MessageDialogListener {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: DecryptViewModel by viewModel()
    private val preferences: Preferences by inject()

    private var language: String? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): DecryptFragment = DecryptFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDecryptBinding {
        return FragmentDecryptBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.getCertificatesLoadState.subscribe(this, ::handleGetCertificatesLoadState)
        viewModel.parseCertificatesLoadState.subscribe(this, ::handleParseCertificatesLoadState)
        viewModel.verifyCertificatesLoadState.subscribe(this, ::verifyCertificateLoadState)
        viewModel.encryptDataLoadState.subscribe(this, ::handleEncryptDataLoadState)
        viewModel.decryptDataLoadState.subscribe(this, ::handleDecryptDataLoadState)
    }

    override fun onViewReady() {
        binding.btnEncryptForm.onClick {
            clearForm()
            // Get text from form
            val formText = binding.editFormText.text.toString()
            // Encrypt text from form
            viewModel.encryptData(formText)
        }

        binding.btnDecryptData.onClick {
            viewModel.decryptData()
        }

        binding.btnVerify.onClick {
            viewModel.verifyCertificate()
        }
    }

    override fun onResume() {
        super.onResume()
        // Get language - Selected by user or system language
        language = context?.let {
            val configuration = it.resources.configuration
            AppUtils.getLocale(preferences, configuration)?.language
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
    fun processReceivedDecryptedData(decryptedDataEncoded: String?) {
        val decryptedData = String(Base64.decode(decryptedDataEncoded, Base64.NO_WRAP))
        binding.txtDecryptedData.text = decryptedData
        binding.decryptedDataLayout.visibility = View.VISIBLE
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
            is DataLoadState.Success -> handler.openGetCertificatesScreen(EIDCertificateType.ENC, language)
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
     * Show encrypted data
     */
    private fun handleEncryptDataLoadState(dataLoadState: DataLoadState<ByteArray>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                binding.txtEncryptedData.text = ByteUtils.bytesToHexString(dataLoadState.data)
                binding.encryptedDataLayout.visibility = View.VISIBLE
                binding.btnDecryptData.visibility = View.VISIBLE
                scrollToBottom()
            }
            is DataLoadState.Error -> showErrorDialog(dataLoadState.error)
        }
    }


    /**
     * Open Decrypt screen from eID SDK
     */
    private fun handleDecryptDataLoadState(dataLoadState: DataLoadState<DecryptData>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> handler.openDecryptScreen(dataLoadState.data, language)
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
        binding.certificatesLayout.visibility = View.GONE

        // Encrypted data layout
        binding.txtEncryptedData.text = null
        binding.encryptedDataLayout.visibility = View.GONE
        binding.btnDecryptData.visibility = View.GONE

        // Decrypted data layout
        binding.txtDecryptedData.text = null
        binding.decryptedDataLayout.visibility = View.GONE
    }

    private fun scrollToBottom() {
        binding.scrollView.scrollToBottom()
    }

    private fun showConfirmDialog(title: String, message: String, positiveButton: String, tag: String) {
        DialogUtils.showConfirmDialog(title, message, positiveButton, getString(R.string.general__cancel), childFragmentManager, tag)
    }

    private fun showMessageDialog(title: String, message: String) {
        DialogUtils.showMessageDialog(title, message, getString(R.string.general__ok), childFragmentManager)
    }
}
package sk.minv.sample_app.ui.sign

import android.text.TextUtils
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.sample_app.data.Certificate
import sk.minv.sample_app.data.SignData
import sk.minv.sample_app.data.VerifyCertificateResult
import sk.minv.base.base.interactor.onFailure
import sk.minv.base.base.interactor.onSuccess
import sk.minv.base.exceptions.ApplicationException
import sk.minv.base.utils.helpers.launch
import sk.minv.sample_app.data.GenerateHashParams
import sk.minv.sample_app.data.ParseCertificatesParams
import sk.minv.sample_app.interactors.usecase.certificates.ParseCertificatesUseCase
import sk.minv.sample_app.interactors.usecase.generateHash.GenerateHashUseCase

class SignViewModel(
    private val generateHashUseCase: GenerateHashUseCase,
    private val parseCertificatesUseCase: ParseCertificatesUseCase
) : ViewModel() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val _getCertificatesLoadState = MutableLiveData<DataLoadState<Boolean>>()
    val getCertificatesLoadState: LiveData<DataLoadState<Boolean>> = _getCertificatesLoadState

    private val _parseCertificatesLoadState = MutableLiveData<DataLoadState<Certificate>>()
    val parseCertificatesLoadState: LiveData<DataLoadState<Certificate>> = _parseCertificatesLoadState

    private val _verifyCertificatesLoadState = MutableLiveData<DataLoadState<VerifyCertificateResult>>()
    val verifyCertificatesLoadState: LiveData<DataLoadState<VerifyCertificateResult>> = _verifyCertificatesLoadState

    private val _generateHashLoadState = MutableLiveData<DataLoadState<ByteArray>>()
    val generateHashLoadState: LiveData<DataLoadState<ByteArray>> = _generateHashLoadState

    private val _signDataLoadState = MutableLiveData<DataLoadState<SignData>>()
    val signDataLoadState: LiveData<DataLoadState<SignData>> = _signDataLoadState

    private lateinit var textToSign: String

    private var certificate: Certificate? = null
    private var generatedHash: ByteArray? = null

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun clearData() {
        certificate = null
        generatedHash = null
    }

    fun signData(text: String) {
        textToSign = text
        _getCertificatesLoadState.postValue(DataLoadState.Success(true))
    }

    /**
     * Parse certificates JSON retrieved from eID SDK
     */
    fun parseCertificates(certificatesJson: String?) {
        if (TextUtils.isEmpty(certificatesJson)) {
            // Notify UI - Certificate JSON == null
            _parseCertificatesLoadState.postValue(DataLoadState.Error(ApplicationException("Certificates JSON is empty")))
            return
        }

        // Notify UI - Loading
        _parseCertificatesLoadState.postValue(DataLoadState.Loading())

        launch {
            val params = ParseCertificatesParams(certificatesJson!!)
            // Launch use case
            parseCertificatesUseCase(params)
                .onSuccess {
                    // Save loaded certificate
                    certificate = it
                    // Notify UI - Show loaded certificate
                    _parseCertificatesLoadState.postValue(DataLoadState.Success(it))
                    //
                    generateHash()
                }
                .onFailure {
                    // Notify UI - Show error
                    _parseCertificatesLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }

    /**
     * Verify certificate validity via OCSP
     */
    fun verifyCertificate() {
        if (certificate == null) {
            // Notify UI - QES Certificate == null
            _verifyCertificatesLoadState.postValue(DataLoadState.Error(ApplicationException("Signing certificate is empty")))
            return
        }

        // Notify UI - Loading
        _verifyCertificatesLoadState.postValue(DataLoadState.Loading())

        EIDHandler.verifyCertificate(certificate!!.certificateDataEncoded, {
            val result = Gson().fromJson(it, VerifyCertificateResult::class.java)
            _verifyCertificatesLoadState.postValue(DataLoadState.Success(result))
        }, {
            _verifyCertificatesLoadState.postValue(DataLoadState.Error(it))
        })
    }

    /**
     * Sign generated HASH with selected certificate
     */
    fun signGeneratedHash() {
        if (generatedHash == null) {
            // Notify UI - Generated hash == null
            _signDataLoadState.postValue(DataLoadState.Error(ApplicationException("Generated HASH is empty")))
            return
        }

        if (certificate == null) {
            // Notify UI - QES Certificate == null
            _signDataLoadState.postValue(DataLoadState.Error(ApplicationException("Signing certificate is empty")))
            return
        }

        // Select preferred signature scheme from loaded certificate
        val signatureScheme = "1.2.840.113549.1.1.11"
        // Encode generated HASH to Base64
        val dataToSignEncoded = Base64.encodeToString(generatedHash, Base64.NO_WRAP)
        // Set parameters for signing
        val signDataParameters = SignData(certificate!!.certIndex, signatureScheme, dataToSignEncoded)

        // Notify UI - Sign generated hash with selected certificate
        _signDataLoadState.postValue(DataLoadState.Success(signDataParameters))
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    /**
     * Generates SHA 256 HASH from entered text
     */
    private fun generateHash() {
        if (TextUtils.isEmpty(textToSign)) {
            // Notify UI - Entered text is empty
            _generateHashLoadState.postValue(DataLoadState.Error(ApplicationException("Text is empty")))
            return
        }

        // Notify UI - Loading
        _generateHashLoadState.postValue(DataLoadState.Loading())

        launch {
            val params = GenerateHashParams(textToSign)
            // Launch use case
            generateHashUseCase(params)
                .onSuccess {
                    // Save generated SHA-256 HASH
                    generatedHash = it
                    // Notify UI - Show generated SHA-256 HASH
                    _generateHashLoadState.postValue(DataLoadState.Success(it))
                }
                .onFailure {
                    // Notify UI - Show error
                    _generateHashLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }
}
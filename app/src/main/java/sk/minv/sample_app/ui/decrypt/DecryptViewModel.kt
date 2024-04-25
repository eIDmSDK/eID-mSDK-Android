package sk.minv.sample_app.ui.decrypt

import android.text.TextUtils
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.sample_app.data.Certificate
import sk.minv.sample_app.data.DecryptData
import sk.minv.sample_app.data.VerifyCertificateResult
import sk.minv.base.base.interactor.onFailure
import sk.minv.base.base.interactor.onSuccess
import sk.minv.base.exceptions.ApplicationException
import sk.minv.base.utils.helpers.launch
import sk.minv.sample_app.data.*
import sk.minv.sample_app.interactors.usecase.certificates.ParseCertificatesUseCase
import sk.minv.sample_app.interactors.usecase.encrypt.EncryptDataUseCase

class DecryptViewModel(
    private val parseCertificatesUseCase: ParseCertificatesUseCase,
    private val encryptDataUseCase: EncryptDataUseCase
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

    private val _encryptDataLoadState = MutableLiveData<DataLoadState<ByteArray>>()
    val encryptDataLoadState: LiveData<DataLoadState<ByteArray>> = _encryptDataLoadState

    private val _decryptDataLoadState = MutableLiveData<DataLoadState<DecryptData>>()
    val decryptDataLoadState: LiveData<DataLoadState<DecryptData>> = _decryptDataLoadState

    private lateinit var textToEncrypt: String

    private var certificate: Certificate? = null
    private var encryptedData: ByteArray? = null

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun clearData(){
        certificate = null
        encryptedData = null
    }

    fun encryptData(text: String) {
        textToEncrypt = text
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
                    // Encrypt data with public key from  loaded certificate
                    encryptDataWithCertificate(it)
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
     * Decrypt encrypted data with selected certificate
     */
    fun decryptData() {
        if (encryptedData == null) {
            // Notify UI - Generated hash == null
            _decryptDataLoadState.postValue(DataLoadState.Error(ApplicationException("Generated HASH is empty")))
            return
        }

        // Encode encrypted data to Base64
        val dataToDecryptEncoded = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        // Set parameters for decryption
        val decryptDataParameters = DecryptData(dataToDecryptEncoded)

        // Notify UI - Sign generated hash with selected certificate
        _decryptDataLoadState.postValue(DataLoadState.Success(decryptDataParameters))
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    /**
     * Encrypt entered text
     */
    private fun encryptDataWithCertificate(certificate: Certificate) {
        if (TextUtils.isEmpty(textToEncrypt)) {
            // Notify UI - Entered text is empty
            _encryptDataLoadState.postValue(DataLoadState.Error(ApplicationException("Text is empty")))
            return
        }

        // Notify UI - Loading
        _encryptDataLoadState.postValue(DataLoadState.Loading())

        launch {
            val params = EncryptDataParams(textToEncrypt, certificate.publicKey)
            // Launch use case
            encryptDataUseCase(params)
                .onSuccess {
                    // Save generated SHA-256 HASH
                    encryptedData = it
                    // Notify UI - Show generated SHA-256 HASH
                    _encryptDataLoadState.postValue(DataLoadState.Success(it))
                }
                .onFailure {
                    // Notify UI - Show error
                    _encryptDataLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }
}
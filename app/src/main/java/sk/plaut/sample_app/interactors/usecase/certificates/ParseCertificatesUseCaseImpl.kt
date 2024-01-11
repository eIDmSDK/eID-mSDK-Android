package sk.plaut.sample_app.interactors.usecase.certificates

import android.util.Base64
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import sk.plaut.base.base.interactor.Result
import sk.plaut.base.base.interactor.Success
import sk.plaut.base.utils.helpers.AppDispatchers
import sk.plaut.sample_app.data.*
import sk.plaut.sample_app.utils.common.StringUtils
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class ParseCertificatesUseCaseImpl(
    private val appDispatchers: AppDispatchers
) : ParseCertificatesUseCase {

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override suspend fun invoke(params: ParseCertificatesParams): Result<Certificate> {
        return withContext(appDispatchers.IO) {
            // Parse received json
            val gson = Gson()
            val certificatesJson = gson.fromJson(params.certificatesJson, CertificatesJsonObject::class.java)

            // Get required data from parsed json object
            val certificatesParsed = ArrayList<Certificate>()
            for (certificate in certificatesJson.certificates) {
                certificatesParsed.add(getCertificateData(certificate))
            }

            // Select and return preferred certificate for signature
            Success(certificatesParsed[0])
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun getCertificateData(certificateJsonObject: CertificateJsonObject): Certificate {
        // Decode certificate bytes
        val certificateData = Base64.decode(certificateJsonObject.certData, Base64.NO_WRAP)

        // Generate X509Certificate from certificate bytes
        val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
        val certificateBytesStream: InputStream = ByteArrayInputStream(certificateData)
        val certificate: X509Certificate = certificateFactory.generateCertificate(certificateBytesStream) as X509Certificate

        // Get parameters to show on UI
        val slot: String = certificateJsonObject.slot
        val certIndex: Int = certificateJsonObject.certIndex
        var serialNumber: String = StringUtils.EMPTY_STRING
        var fullName: String = StringUtils.EMPTY_STRING
        var supportedSchemesFormatted = StringUtils.EMPTY_STRING
        // Get additional parameters
        val publicKey: PublicKey = certificate.publicKey

        for ((index, supportedScheme) in certificateJsonObject.supportedSchemes.withIndex()) {
            supportedSchemesFormatted = supportedSchemesFormatted.plus(supportedScheme)
            if (index < certificateJsonObject.supportedSchemes.size - 1) {
                supportedSchemesFormatted = supportedSchemesFormatted.plus(", ")
            }
        }

        // Parse parameters from X509Certificate subject DN
        val subjectParameters: List<String> = certificate.subjectDN.toString().split(",")

        for (parameter in subjectParameters) {
            when {
                parameter.contains("CN=") -> fullName = parameter.replace("CN=", "").trim()
                parameter.contains("SERIALNUMBER=") -> serialNumber = parameter.replace("SERIALNUMBER=", "").trim()
            }
        }

        // Return parsed data
        return Certificate(slot, certIndex, serialNumber, fullName, supportedSchemesFormatted, publicKey,certificateJsonObject.certData)
    }
}
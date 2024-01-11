package sk.plaut.sample_app.ui.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import sk.eid.eidhandlerpublic.EIDCertificateType
import sk.eid.eidhandlerpublic.EIDHandler
import sk.plaut.base.base.activity.BaseActivity
import sk.plaut.base.base.activity.NoParameters
import sk.plaut.base.utils.helpers.getSerializable
import sk.plaut.sample_app.data.SignData

/**
 * Sign form screen
 */
class SignActivity : BaseActivity<NoParameters, SignFragment>(), SignHandler {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private lateinit var getCertificatesLauncher: ActivityResultLauncher<Intent>
    private lateinit var signLauncher: ActivityResultLauncher<Intent>
    private lateinit var noActionLauncher: ActivityResultLauncher<Intent>

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SignActivity::class.java)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun createContentFragment(): SignFragment = SignFragment.newInstance()

    override fun onViewReady() {
        // Retrieve certificates JSON from eID SDK
        getCertificatesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val certificatesJson = result.data?.getStringExtra("CERTIFICATES")
                // Process JSON in Fragment
                fragment.processReceivedCertificatesJson(certificatesJson)
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Retrieve exception from eID SDK
                val exception = result.data?.getSerializable<Throwable>("EXCEPTION")
                fragment.onEidExceptionReceived(exception)
            }
        }

        // Retrieve Base64 encoded signed data from eID SDK
        signLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val signedDataEncoded = result.data?.getStringExtra("SIGNED_DATA")
                // Process data in Fragment
                fragment.processReceivedSignedData(signedDataEncoded)
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Retrieve exception from eID SDK
                val exception = result.data?.getSerializable<Throwable>("EXCEPTION")
                fragment.onEidExceptionReceived(exception)
            }
        }

        noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    }

    /*-------------------------*/
    /*     FRAGMENT METHODS    */
    /*-------------------------*/

    override fun openGetCertificatesScreen(certificateType: EIDCertificateType, language: String?) {
        // Get certificates JSON from eID SDK
        EIDHandler.getCertificates(certificateType,this, getCertificatesLauncher, language)
    }

    override fun openSignScreen(signDataParameters: SignData, language: String?) {
        // Sign HASH with selected certificate via eID SDK
        EIDHandler.startSign(
            signDataParameters.certIndex,
            signDataParameters.signatureScheme,
            signDataParameters.dataToSignEncoded,
            this,
            signLauncher,
            language
        )
    }

    override fun openPINManagementScreen(language: String?) {
        // Open PIN management screen
        EIDHandler.startPINManagement(this, noActionLauncher, language)
    }
}
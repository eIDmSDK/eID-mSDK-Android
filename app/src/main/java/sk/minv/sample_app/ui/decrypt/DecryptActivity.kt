package sk.minv.sample_app.ui.decrypt

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import sk.eid.eidhandlerpublic.EIDCertificateType
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.activity.BaseActivity
import sk.minv.base.base.activity.NoParameters
import sk.minv.base.utils.helpers.getSerializable
import sk.minv.sample_app.data.DecryptData

/**
 * Decrypt encrypted data screen
 */
class DecryptActivity : BaseActivity<NoParameters, DecryptFragment>(), DecryptHandler {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private lateinit var getCertificatesLauncher: ActivityResultLauncher<Intent>
    private lateinit var decryptLauncher: ActivityResultLauncher<Intent>
    private lateinit var noActionLauncher: ActivityResultLauncher<Intent>

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, DecryptActivity::class.java)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun createContentFragment(): DecryptFragment = DecryptFragment.newInstance()

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

        // Retrieve Base64 encoded decrypted data from eID SDK
        decryptLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val decryptedDataEncoded = result.data?.getStringExtra("DECRYPTED_DATA")
                // Process data in Fragment
                fragment.processReceivedDecryptedData(decryptedDataEncoded)
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

    override fun openDecryptScreen(decryptDataParameters: DecryptData, language: String?) {
        // Decrypt encrypted data with selected certificate via eID SDK
        EIDHandler.startDecrypt(decryptDataParameters.dataToDecryptEncoded, this, decryptLauncher, language)
    }

    override fun openPINManagementScreen(language: String?) {
        // Open PIN management screen
        EIDHandler.startPINManagement(this, noActionLauncher, language)
    }
}
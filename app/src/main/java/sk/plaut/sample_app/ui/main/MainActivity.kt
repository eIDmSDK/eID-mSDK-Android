package sk.plaut.sample_app.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.core.component.KoinComponent
import sk.eid.eidhandlerpublic.EIDHandler
import sk.plaut.base.base.activity.BaseActivity
import sk.plaut.base.base.activity.NoParameters
import sk.plaut.base.utils.helpers.getSerializable
import sk.plaut.sample_app.ui.decrypt.DecryptActivity
import sk.plaut.sample_app.ui.qr.QRCodeReaderActivity
import sk.plaut.sample_app.ui.sign.SignActivity
import sk.plaut.sample_app.utils.common.AppConstants

/**
 * Main menu - eID Login, Open eID certificate viewer, eID Sign data
 */
class MainActivity : BaseActivity<NoParameters, MainFragment>(), MainHandler, KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

//    private val preferences: Preferences by inject()

    private lateinit var authenticationLauncher: ActivityResultLauncher<Intent>
    private lateinit var qrCodeHandlerLauncher: ActivityResultLauncher<Intent>
    private lateinit var qrReaderLauncher: ActivityResultLauncher<Intent>
    private lateinit var generalActionLauncher: ActivityResultLauncher<Intent>
    private lateinit var noActionLauncher: ActivityResultLauncher<Intent>

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

//    Change language - Application + eID SDK
//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(AppUtils.setLocale(newBase, preferences))
//    }

    override fun createContentFragment(): MainFragment = MainFragment.newInstance()

    override fun onViewReady() {
        authenticationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Retrieve authentication ID token from eID SDK
                val idToken = result.data?.getStringExtra("ID_TOKEN")
                fragment.getUserData(idToken)
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Retrieve exception from eID SDK
                val exception = result.data?.getSerializable<Throwable>("EXCEPTION")
                fragment.onEidExceptionReceived(exception)
            }
        }

        qrCodeHandlerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
           if (result.resultCode == Activity.RESULT_CANCELED) {
                // Retrieve exception from eID SDK
                val exception = result.data?.getSerializable<Throwable>("EXCEPTION")
                fragment.onEidExceptionReceived(exception, true)
            }
        }

        qrReaderLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Retrieve QR code data
                val qrCodeData = result.data?.getStringExtra(QRCodeReaderActivity.EXTRA_QR_DATA)
                fragment.handleQrCode(qrCodeData)
            }
        }

        generalActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
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

    override fun openAuthenticationScreen(language: String?) {
        // Get authentication ID token from eID SDK
        EIDHandler.startAuth(AppConstants.API_KEY_ID, AppConstants.API_KEY_VALUE, AppConstants.API_KEY_ID, AppConstants.API_KEY_VALUE, this, authenticationLauncher, language)
    }

    override fun openQrCodeReader() {
        // Open QR code reader
        qrReaderLauncher.launch(QRCodeReaderActivity.createIntent(this))
    }

    override fun handleQrCode(qrCodeData: String?, language: String?) {
        EIDHandler.handleQRCode(AppConstants.API_KEY_ID, AppConstants.API_KEY_VALUE, qrCodeData, this, qrCodeHandlerLauncher, language) {
            // Handle error
            fragment.onQrCodeExceptionReceived(it)
        }
    }

    override fun openCertificatesScreen(language: String?) {
        // Open eID SDK certificates viewer
        EIDHandler.startCertificates(this, generalActionLauncher, language)
    }

    override fun openSigningScreen() {
        // Open Signing screen
        startActivity(SignActivity.createIntent(this))
    }

    override fun openDecryptScreen() {
        // Open decrypt screen
        startActivity(DecryptActivity.createIntent(this))
    }

    override fun openPINManagementScreen(language: String?) {
        // Open PIN management screen
        EIDHandler.startPINManagement(this, noActionLauncher, language)
    }

    override fun openTutorialScreen(language: String?) {
        // Open Tutorial screen
        EIDHandler.startTutorial(this, noActionLauncher, language)
    }

    override fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
        overridePendingTransition(sk.plaut.base.R.anim.anim_nothing, sk.plaut.base.R.anim.anim_nothing)
    }
}
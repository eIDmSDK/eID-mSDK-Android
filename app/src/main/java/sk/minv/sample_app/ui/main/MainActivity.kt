package sk.minv.sample_app.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.activity.BaseActivity
import sk.minv.base.base.activity.NoParameters
import sk.minv.base.utils.helpers.getSerializable
import sk.minv.sample_app.data.AuthenticationFlow
import sk.minv.sample_app.ui.decrypt.DecryptActivity
import sk.minv.sample_app.ui.settings.SettingsActivity
import sk.minv.sample_app.ui.sign.SignActivity
import sk.minv.sample_app.utils.common.AppUtils
import sk.minv.sample_app.utils.managers.Preferences

/**
 * Main menu - eID Login, Open eID certificate viewer, eID Sign data
 */
class MainActivity : BaseActivity<NoParameters, MainFragment>(), MainHandler, KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val preferences: Preferences by inject()

    private lateinit var authenticationLauncher: ActivityResultLauncher<Intent>
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
                // Retrieve ID token from eID SDK
                val idToken = result.data?.getStringExtra("ID_TOKEN")
                if (idToken != null) {
                    fragment.onIdTokenReceived(idToken)
                }
                // Retrieve Auth code from eID SDK
                val authCode = result.data?.getStringExtra("AUTH_CODE")
                if (authCode != null) {
                    fragment.onAuthCodeReceived(authCode)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Retrieve exception from eID SDK
                val exception = result.data?.getSerializable<Throwable>("EXCEPTION")
                fragment.onEidExceptionReceived(exception)
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
        // Get ID token / Auth code from eID SDK
        when (preferences.getSelectedAuthenticationFlow()) {
            AuthenticationFlow.AUTH_CODE -> {
                val clientId = AppUtils.getClientId(preferences.getSelectedEnvironment())
                EIDHandler.startAuth(clientId, null, null,this, authenticationLauncher, language)
            }
            AuthenticationFlow.IMPLICIT -> {
                val clientId = AppUtils.getClientId(preferences.getSelectedEnvironment())
                val clientSecret = AppUtils.getClientSecret(preferences.getSelectedEnvironment())
                EIDHandler.startAuth(clientId, clientSecret, null, null,this, authenticationLauncher, language)
            }
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

    override fun openSettings() {
        startActivity(SettingsActivity.createIntent(this))
    }
}
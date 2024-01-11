package sk.plaut.sample_app.ui.startup

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sk.eid.eidhandlerpublic.EIDHandler
import sk.plaut.base.base.activity.BaseActivity
import sk.plaut.base.base.activity.NoParameters
import sk.plaut.sample_app.ui.main.MainActivity
import sk.plaut.sample_app.utils.managers.Preferences

/**
 * Splash screen
 */
class StartupActivity : BaseActivity<NoParameters, StartupFragment>(), StartupHandler, KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val preferences: Preferences by inject()

    private lateinit var tutorialActionLauncher: ActivityResultLauncher<Intent>

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun createContentFragment(): StartupFragment = StartupFragment.newInstance()

    override fun onViewReady() {
        tutorialActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            fragment.onTutorialCompleted()
        }
    }

    /*-------------------------*/
    /*     FRAGMENT METHODS    */
    /*-------------------------*/

    override fun openTutorialScreen() {
        val language = preferences.getSelectedLanguage()
        EIDHandler.startTutorial(this, tutorialActionLauncher, language)
    }

    override fun openMainScreen() {
        startActivity(MainActivity.createIntent(this))
        finish()
    }
}
package sk.minv.sample_app.ui.startup

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.core.component.KoinComponent
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.activity.BaseActivity
import sk.minv.base.base.activity.NoParameters
import sk.minv.sample_app.ui.main.MainActivity

/**
 * Splash screen
 */
class StartupActivity : BaseActivity<NoParameters, StartupFragment>(), StartupHandler, KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

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

    override fun openTutorialScreen(language: String?) {
        EIDHandler.startTutorial(this, tutorialActionLauncher, language)
    }

    override fun openMainScreen() {
        startActivity(MainActivity.createIntent(this))
        finish()
    }
}
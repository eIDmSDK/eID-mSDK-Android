package sk.minv.sample_app.ui.settings

import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import sk.minv.base.base.activity.BaseActivity
import sk.minv.base.base.activity.NoHandler
import sk.minv.base.base.activity.NoParameters

class SettingsActivity : BaseActivity<NoParameters, SettingsFragment>(), NoHandler, KoinComponent {

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun createContentFragment(): SettingsFragment = SettingsFragment.newInstance()
}
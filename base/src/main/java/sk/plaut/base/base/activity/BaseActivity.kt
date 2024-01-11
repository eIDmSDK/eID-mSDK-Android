package sk.plaut.base.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import sk.plaut.base.R
import sk.plaut.base.utils.helpers.getExtra
import sk.plaut.base.utils.helpers.showFragment

abstract class BaseActivity<PARAMETERS, FRAGMENT> : AppCompatActivity(R.layout.activity__container) where PARAMETERS : AndroidParameters, FRAGMENT : Fragment {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    protected val TAG = this.javaClass.name

    protected var parameters: PARAMETERS? = null
    protected lateinit var fragment: FRAGMENT

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        parameters = intent.extras?.getExtra()
        super.onCreate(savedInstanceState)

        fragment = createContentFragment()
        showFragment(R.id.container, fragment, TAG)

        onViewReady()
    }

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    protected abstract fun createContentFragment(): FRAGMENT

    protected open fun onViewReady() {}
}
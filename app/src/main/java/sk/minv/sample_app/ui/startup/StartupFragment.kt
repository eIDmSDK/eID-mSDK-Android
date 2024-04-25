package sk.minv.sample_app.ui.startup

import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import sk.minv.base.base.fragment.BaseFragment
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.utils.helpers.subscribe
import sk.minv.sample_app.data.StartupResult
import sk.minv.sample_app.databinding.FragmentStartupBinding
import sk.minv.sample_app.utils.common.AppUtils
import sk.minv.sample_app.utils.managers.Preferences

class StartupFragment : BaseFragment<FragmentStartupBinding, StartupHandler>(), KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: StartupViewModel by viewModel()
    private val preferences: Preferences by inject()

    private var language: String? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): StartupFragment = StartupFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentStartupBinding {
        return FragmentStartupBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.appLoadState.subscribe(this, ::handleAppLoadState)
    }

    override fun onViewModelReady() {
        viewModel.loadApp()
    }

    override fun onResume() {
        super.onResume()
        // Get language - Selected by user or system language
        language = context?.let {
            val configuration = it.resources.configuration
            AppUtils.getLocale(preferences, configuration)?.language
        }
    }

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun onTutorialCompleted() {
        viewModel.onTutorialCompleted()
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun handleAppLoadState(dataLoadState: DataLoadState<StartupResult>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                when (dataLoadState.data) {
                    StartupResult.TUTORIAL -> handler.openTutorialScreen(language)
                    StartupResult.MAIN_SCREEN -> handler.openMainScreen()
                }
            }
            is DataLoadState.Error -> { }
        }
    }
}
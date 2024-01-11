package sk.plaut.sample_app.ui.startup

import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import sk.plaut.base.base.fragment.BaseFragment
import sk.plaut.base.base.interactor.DataLoadState
import sk.plaut.base.utils.helpers.subscribe
import sk.plaut.sample_app.data.StartupResult
import sk.plaut.sample_app.databinding.FragmentStartupBinding

class StartupFragment : BaseFragment<FragmentStartupBinding, StartupHandler>(), KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: StartupViewModel by viewModel()

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
                    StartupResult.TUTORIAL -> handler.openTutorialScreen()
                    StartupResult.MAIN_SCREEN -> handler.openMainScreen()
                }
            }
            is DataLoadState.Error -> { }
        }
    }
}
package sk.minv.sample_app.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.base.base.activity.NoHandler
import sk.minv.base.base.fragment.BaseFragment
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.utils.helpers.onClick
import sk.minv.base.utils.helpers.subscribe
import sk.minv.sample_app.R
import sk.minv.sample_app.data.AppLanguage
import sk.minv.sample_app.data.AuthenticationFlow
import sk.minv.sample_app.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding, NoHandler>(), KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: SettingsViewModel by viewModel()

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.environmentDataLoadState.subscribe(this, ::handleEnvironmentDataLoadState)
        viewModel.languageDataLoadState.subscribe(this, ::handleLanguageDataLoadState)
        viewModel.authenticationFlowDataLoadState.subscribe(this, ::handleAuthenticationFlowDataLoadState)
        viewModel.resetTutorialDataLoadState.subscribe(this, ::handleResetTutorialDataLoadState)
    }

    override fun onViewModelReady() {
        viewModel.loadData()
    }

    override fun onViewReady() {
        binding.btnResetTutorial.onClick {
            viewModel.onResetTutorialClick()
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun handleEnvironmentDataLoadState(dataLoadState: DataLoadState<EIDEnvironment>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                when (dataLoadState.data) {
                    EIDEnvironment.PLAUT_DEV -> binding.rbPlautDev.isChecked = true
                    EIDEnvironment.PLAUT_TEST -> binding.rbPlautTest.isChecked = true
                    EIDEnvironment.MINV_TEST -> binding.rbMinvTest.isChecked = true
                    EIDEnvironment.MINV_PROD -> binding.rbMinvProd.isChecked = true
                }
                binding.rgEnvironment.setOnCheckedChangeListener { _, checkedId ->
                    val selectedEnvironment = viewModel.onEnvironmentSelected(checkedId)
                    context?.let { EIDHandler.initialize(it, selectedEnvironment) }
                }
            }
            is DataLoadState.Error -> { }
        }
    }

    private fun handleLanguageDataLoadState(dataLoadState: DataLoadState<AppLanguage>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                when (dataLoadState.data) {
                    AppLanguage.SYSTEM -> binding.rbSystem.isChecked = true
                    AppLanguage.SLOVAK -> binding.rbSlovak.isChecked = true
                    AppLanguage.ENGLISH -> binding.rbEnglish.isChecked = true
                }
                binding.rgLanguage.setOnCheckedChangeListener { _, checkedId -> viewModel.onLanguageSelected(checkedId) }
            }
            is DataLoadState.Error -> { }
        }
    }

    private fun handleAuthenticationFlowDataLoadState(dataLoadState: DataLoadState<AuthenticationFlow>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                when (dataLoadState.data) {
                    AuthenticationFlow.AUTH_CODE -> binding.rbAuthCode.isChecked = true
                    AuthenticationFlow.IMPLICIT -> binding.rbImplicit.isChecked = true
                }
                binding.rgAuthFlow.setOnCheckedChangeListener { _, checkedId -> viewModel.onAuthenticationFlowSelected(checkedId) }
            }
            is DataLoadState.Error -> { }
        }
    }

    private fun handleResetTutorialDataLoadState(dataLoadState: DataLoadState<Boolean>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> Toast.makeText(context, getString(R.string.settings__reset_tutorial__success), Toast.LENGTH_SHORT).show()
            is DataLoadState.Error -> { }
        }
    }
}
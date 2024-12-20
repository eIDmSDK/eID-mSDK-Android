package sk.minv.sample_app.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.sample_app.R
import sk.minv.sample_app.data.AppLanguage
import sk.minv.sample_app.data.AuthenticationFlow
import sk.minv.sample_app.utils.managers.Preferences

class SettingsViewModel(
    private val preferences: Preferences
) : ViewModel() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val _environmentDataLoadState = MutableLiveData<DataLoadState<EIDEnvironment>>()
    val environmentDataLoadState: LiveData<DataLoadState<EIDEnvironment>> = _environmentDataLoadState
    private val _languageDataLoadState = MutableLiveData<DataLoadState<AppLanguage>>()
    val languageDataLoadState: LiveData<DataLoadState<AppLanguage>> = _languageDataLoadState
    private val _authenticationFlowDataLoadState = MutableLiveData<DataLoadState<AuthenticationFlow>>()
    val authenticationFlowDataLoadState: LiveData<DataLoadState<AuthenticationFlow>> = _authenticationFlowDataLoadState
    private val _resetTutorialDataLoadState = MutableLiveData<DataLoadState<Boolean>>()
    val resetTutorialDataLoadState: LiveData<DataLoadState<Boolean>> = _resetTutorialDataLoadState

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun loadData() {
        _environmentDataLoadState.postValue(DataLoadState.Success(preferences.getSelectedEnvironment()))
        _languageDataLoadState.postValue(DataLoadState.Success(preferences.getSelectedLanguage()))
        _authenticationFlowDataLoadState.postValue(DataLoadState.Success(preferences.getSelectedAuthenticationFlow()))
    }

    fun onEnvironmentSelected(checkedId: Int): EIDEnvironment {
        when (checkedId) {
            R.id.rb_minv_test -> {
                preferences.setSelectedEnvironment(EIDEnvironment.MINV_TEST)
                return EIDEnvironment.MINV_TEST
            }
            R.id.rb_minv_prod -> {
                preferences.setSelectedEnvironment(EIDEnvironment.MINV_PROD)
                return EIDEnvironment.MINV_PROD
            }
        }
        return EIDEnvironment.MINV_TEST
    }

    fun onLanguageSelected(checkedId: Int) {
        when (checkedId) {
            R.id.rb_slovak -> preferences.setSelectedLanguage(AppLanguage.SLOVAK)
            R.id.rb_english -> preferences.setSelectedLanguage(AppLanguage.ENGLISH)
        }
    }

    fun onAuthenticationFlowSelected(checkedId: Int) {
        when (checkedId) {
            R.id.rb_auth_code -> preferences.setSelectedAuthenticationFlow(AuthenticationFlow.AUTH_CODE)
            R.id.rb_implicit -> preferences.setSelectedAuthenticationFlow(AuthenticationFlow.IMPLICIT)
        }
    }

    fun onResetTutorialClick() {
        preferences.resetTutorialCompleted()
        _resetTutorialDataLoadState.postValue(DataLoadState.Success(true))
    }
}
package sk.minv.sample_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.base.interactor.onFailure
import sk.minv.base.base.interactor.onSuccess
import sk.minv.base.exceptions.ApplicationException
import sk.minv.base.utils.helpers.launch
import sk.minv.sample_app.data.GetTokenParams
import sk.minv.sample_app.data.GetUserDataParams
import sk.minv.sample_app.data.UserData
import sk.minv.sample_app.interactors.usecase.getToken.GetTokenUseCase
import sk.minv.sample_app.interactors.usecase.userData.GetUserDataUseCase
import sk.minv.sample_app.utils.common.AppUtils
import sk.minv.sample_app.utils.managers.Preferences

class MainViewModel(
    private val preferences: Preferences,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val _getUserDataLoadState = MutableLiveData<DataLoadState<UserData>>()
    val getUserDataLoadState: LiveData<DataLoadState<UserData>> = _getUserDataLoadState

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun getUserData(idToken: String?) {
        if (idToken == null) {
            // Notify UI - ID token hash == null
            _getUserDataLoadState.postValue(DataLoadState.Error(ApplicationException("ID Token empty")))
            return
        }

        // Notify UI - Loading
        _getUserDataLoadState.postValue(DataLoadState.Loading())

        launch {
            val params = GetUserDataParams(idToken)
            // Launch use case
            getUserDataUseCase(params)
                .onSuccess {
                    // Notify UI - Show user logged in
                    _getUserDataLoadState.postValue(DataLoadState.Success(it))
                }
                .onFailure {
                    // Notify UI - Show error
                    _getUserDataLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }

    fun getToken(authorizationCode: String) {
        launch {
            val environment = preferences.getSelectedEnvironment()
            val url = AppUtils.getUrl(environment)
            val clientId = AppUtils.getClientId(environment)
            val clientSecret = AppUtils.getClientSecret(environment)
            val redirectUri = AppUtils.getRedirectUri(environment)
            val params = GetTokenParams(url = url, clientId = clientId, clientSecret = clientSecret, code = authorizationCode, redirectUri = redirectUri)
            getTokenUseCase(params)
                .onSuccess { getUserData(it.idToken) }
                .onFailure { _getUserDataLoadState.postValue(DataLoadState.Error(it)) }
        }
    }
}
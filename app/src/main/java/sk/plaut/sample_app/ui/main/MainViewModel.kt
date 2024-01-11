package sk.plaut.sample_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.plaut.base.base.interactor.DataLoadState
import sk.plaut.base.base.interactor.onFailure
import sk.plaut.base.base.interactor.onSuccess
import sk.plaut.base.exceptions.ApplicationException
import sk.plaut.base.utils.helpers.launch
import sk.plaut.sample_app.data.GetUserDataParams
import sk.plaut.sample_app.data.UserData
import sk.plaut.sample_app.interactors.usecase.userData.GetUserDataUseCase

class MainViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
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
}
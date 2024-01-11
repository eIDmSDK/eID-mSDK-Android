package sk.plaut.sample_app.ui.startup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import sk.plaut.base.base.interactor.DataLoadState
import sk.plaut.base.base.interactor.onFailure
import sk.plaut.base.base.interactor.onSuccess
import sk.plaut.base.utils.helpers.launch
import sk.plaut.sample_app.data.StartupResult
import sk.plaut.sample_app.interactors.usecase.startup.StartupUseCase
import sk.plaut.sample_app.utils.managers.Preferences

class StartupViewModel(
    private val preferences: Preferences,
    private val startupUseCase: StartupUseCase
) : ViewModel() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val _appLoadState = MutableLiveData<DataLoadState<StartupResult>>()
    val appLoadState: LiveData<DataLoadState<StartupResult>> = _appLoadState

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun loadApp() {
        // Notify UI - Loading data
        _appLoadState.postValue(DataLoadState.Loading())

        launch {
            // Show splash screen for 1 sec
            delay(1000)
            // Launch use case
            startupUseCase()
                .onSuccess {
                    // Notify UI - Show tutorial/main screen
                    _appLoadState.postValue(DataLoadState.Success(it))
                }
                .onFailure {
                    // Notify UI - Show error
                    _appLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }

    fun onTutorialCompleted() {
        // Do not show Tutorial again
        preferences.saveTutorialCompleted()
        // Open main screen
        _appLoadState.postValue(DataLoadState.Success(StartupResult.MAIN_SCREEN))
    }
}
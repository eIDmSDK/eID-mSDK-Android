package sk.minv.sample_app.interactors.usecase.startup

import sk.minv.base.base.interactor.Result
import sk.minv.base.base.interactor.Success
import sk.minv.sample_app.data.StartupResult
import sk.minv.sample_app.utils.managers.Preferences

class StartupUseCaseImpl(
    private val preferences: Preferences,
) : StartupUseCase {

    override suspend fun invoke(): Result<StartupResult> {
        return if (preferences.isTutorialCompleted()) {
            Success(StartupResult.MAIN_SCREEN)
        } else {
            Success(StartupResult.TUTORIAL)
        }
    }
}
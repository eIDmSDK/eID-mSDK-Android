package sk.plaut.sample_app.interactors.usecase.startup

import sk.plaut.base.base.interactor.Result
import sk.plaut.base.base.interactor.Success
import sk.plaut.sample_app.data.StartupResult
import sk.plaut.sample_app.utils.managers.Preferences

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
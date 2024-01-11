package sk.plaut.sample_app.interactors.usecase.startup

import sk.plaut.base.base.interactor.Result
import sk.plaut.sample_app.data.StartupResult

interface StartupUseCase {

    suspend operator fun invoke(): Result<StartupResult>
}
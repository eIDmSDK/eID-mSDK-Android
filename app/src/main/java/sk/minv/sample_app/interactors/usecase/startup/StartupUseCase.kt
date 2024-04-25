package sk.minv.sample_app.interactors.usecase.startup

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.StartupResult

interface StartupUseCase {

    suspend operator fun invoke(): Result<StartupResult>
}
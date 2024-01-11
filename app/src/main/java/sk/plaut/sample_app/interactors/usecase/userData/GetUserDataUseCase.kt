package sk.plaut.sample_app.interactors.usecase.userData

import sk.plaut.base.base.interactor.Result
import sk.plaut.sample_app.data.GetUserDataParams
import sk.plaut.sample_app.data.UserData

interface GetUserDataUseCase {

    suspend operator fun invoke(params: GetUserDataParams): Result<UserData>
}
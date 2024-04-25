package sk.minv.sample_app.interactors.usecase.userData

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.GetUserDataParams
import sk.minv.sample_app.data.UserData

interface GetUserDataUseCase {

    suspend operator fun invoke(params: GetUserDataParams): Result<UserData>
}
package sk.plaut.sample_app.interactors.usecase.certificates

import sk.plaut.base.base.interactor.Result
import sk.plaut.sample_app.data.Certificate
import sk.plaut.sample_app.data.ParseCertificatesParams

interface ParseCertificatesUseCase {

    suspend operator fun invoke(params: ParseCertificatesParams): Result<Certificate>
}
package sk.minv.sample_app.interactors.usecase.certificates

import sk.minv.base.base.interactor.Result
import sk.minv.sample_app.data.Certificate
import sk.minv.sample_app.data.ParseCertificatesParams

interface ParseCertificatesUseCase {

    suspend operator fun invoke(params: ParseCertificatesParams): Result<Certificate>
}
package sk.plaut.sample_app.di

import org.koin.dsl.module
import sk.plaut.sample_app.interactors.usecase.certificates.ParseCertificatesUseCase
import sk.plaut.sample_app.interactors.usecase.certificates.ParseCertificatesUseCaseImpl
import sk.plaut.sample_app.interactors.usecase.encrypt.EncryptDataUseCase
import sk.plaut.sample_app.interactors.usecase.encrypt.EncryptDataUseCaseImpl
import sk.plaut.sample_app.interactors.usecase.generateHash.GenerateHashUseCase
import sk.plaut.sample_app.interactors.usecase.generateHash.GenerateHashUseCaseImpl
import sk.plaut.sample_app.interactors.usecase.startup.StartupUseCase
import sk.plaut.sample_app.interactors.usecase.startup.StartupUseCaseImpl
import sk.plaut.sample_app.interactors.usecase.userData.GetUserDataUseCase
import sk.plaut.sample_app.interactors.usecase.userData.GetUserDataUseCaseImpl

val useCaseModule = module {
    factory<StartupUseCase> { StartupUseCaseImpl(get()) }
    factory<GetUserDataUseCase> { GetUserDataUseCaseImpl(get()) }
    factory<ParseCertificatesUseCase> { ParseCertificatesUseCaseImpl(get()) }
    factory<GenerateHashUseCase> { GenerateHashUseCaseImpl(get()) }
    factory<EncryptDataUseCase> { EncryptDataUseCaseImpl(get()) }
}
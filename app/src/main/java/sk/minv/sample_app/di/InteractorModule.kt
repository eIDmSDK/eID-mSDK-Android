package sk.minv.sample_app.di

import org.koin.dsl.module
import retrofit2.Retrofit
import sk.minv.sample_app.interactors.network.api.AuthenticationApi
import sk.minv.sample_app.interactors.network.service.AuthenticationService
import sk.minv.sample_app.interactors.network.service.AuthenticationServiceImpl
import sk.minv.sample_app.interactors.repository.AuthenticationRepository
import sk.minv.sample_app.interactors.repository.AuthenticationRepositoryImpl
import sk.minv.sample_app.interactors.usecase.certificates.ParseCertificatesUseCase
import sk.minv.sample_app.interactors.usecase.certificates.ParseCertificatesUseCaseImpl
import sk.minv.sample_app.interactors.usecase.encrypt.EncryptDataUseCase
import sk.minv.sample_app.interactors.usecase.encrypt.EncryptDataUseCaseImpl
import sk.minv.sample_app.interactors.usecase.generateHash.GenerateHashUseCase
import sk.minv.sample_app.interactors.usecase.generateHash.GenerateHashUseCaseImpl
import sk.minv.sample_app.interactors.usecase.getToken.GetTokenUseCase
import sk.minv.sample_app.interactors.usecase.getToken.GetTokenUseCaseImpl
import sk.minv.sample_app.interactors.usecase.startup.StartupUseCase
import sk.minv.sample_app.interactors.usecase.startup.StartupUseCaseImpl
import sk.minv.sample_app.interactors.usecase.userData.GetUserDataUseCase
import sk.minv.sample_app.interactors.usecase.userData.GetUserDataUseCaseImpl

val useCaseModule = module {
    factory<StartupUseCase> { StartupUseCaseImpl(get()) }
    factory<GetUserDataUseCase> { GetUserDataUseCaseImpl(get()) }
    factory<ParseCertificatesUseCase> { ParseCertificatesUseCaseImpl(get()) }
    factory<GenerateHashUseCase> { GenerateHashUseCaseImpl(get()) }
    factory<EncryptDataUseCase> { EncryptDataUseCaseImpl(get()) }
    factory<GetTokenUseCase> { GetTokenUseCaseImpl(get()) }
}

val repositoryModule = module {
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
}

val serviceModule = module {
    single<AuthenticationService> { AuthenticationServiceImpl(get(), get()) }
}

val apiModule = module {
    single { get<Retrofit>().create(AuthenticationApi::class.java) }
}
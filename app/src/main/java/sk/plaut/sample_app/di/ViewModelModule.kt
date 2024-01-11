package sk.plaut.sample_app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sk.plaut.sample_app.ui.decrypt.DecryptViewModel
import sk.plaut.sample_app.ui.main.MainViewModel
import sk.plaut.sample_app.ui.sign.SignViewModel
import sk.plaut.sample_app.ui.startup.StartupViewModel

val viewModelModule = module {
    viewModel { StartupViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SignViewModel(get(), get()) }
    viewModel { DecryptViewModel(get(), get()) }
}
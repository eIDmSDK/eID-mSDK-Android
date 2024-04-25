package sk.minv.sample_app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sk.minv.sample_app.ui.decrypt.DecryptViewModel
import sk.minv.sample_app.ui.main.MainViewModel
import sk.minv.sample_app.ui.settings.SettingsViewModel
import sk.minv.sample_app.ui.sign.SignViewModel
import sk.minv.sample_app.ui.startup.StartupViewModel

val viewModelModule = module {
    viewModel { StartupViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SignViewModel(get(), get()) }
    viewModel { DecryptViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
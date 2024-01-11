package sk.plaut.sample_app.di

import org.koin.dsl.module
import sk.plaut.base.utils.helpers.AppDispatchers
import sk.plaut.sample_app.utils.managers.Preferences
import sk.plaut.sample_app.utils.managers.PreferencesImpl

val appModule = module {
    single { AppDispatchers() }
    single<Preferences> { PreferencesImpl(get()) }
}
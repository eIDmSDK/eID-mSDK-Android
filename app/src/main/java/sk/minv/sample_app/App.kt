package sk.minv.sample_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.eid.eidhandlerpublic.EIDHandler
import sk.minv.sample_app.di.*
import sk.minv.sample_app.utils.managers.PreferencesImpl
import timber.log.Timber

class App : Application() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val appModules = listOf(appModule, viewModelModule)
    private val interactionModules = listOf(useCaseModule, repositoryModule, serviceModule, apiModule)

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }

            modules(appModules + interactionModules)
        }

        // EID SDK initialization
        val preferences = PreferencesImpl(applicationContext)
        EIDHandler.initialize(this, preferences.getSelectedEnvironment())

        Timber.plant(Timber.DebugTree())
    }
}
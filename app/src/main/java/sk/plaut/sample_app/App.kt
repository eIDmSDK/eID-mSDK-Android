package sk.plaut.sample_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.eid.eidhandlerpublic.EIDHandler
import sk.plaut.sample_app.di.*
import timber.log.Timber

class App : Application() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val appModules = listOf(appModule, viewModelModule, useCaseModule)

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

            modules(appModules)
        }

        // EID SDK initialization
        EIDHandler.initialize(this, EIDEnvironment.MINV_PROD)

        Timber.plant(Timber.DebugTree())
    }
}
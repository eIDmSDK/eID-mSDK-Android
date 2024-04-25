package sk.minv.sample_app.utils.common

import android.content.Context
import android.content.res.Configuration
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.minv.sample_app.data.AppLanguage
import sk.minv.sample_app.utils.managers.Preferences
import java.lang.Exception
import java.util.*

object AppUtils {

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun setLocale(context: Context, preferences: Preferences): Context {
        val configuration = context.resources.configuration

        val locale = getLocale(preferences, configuration)
        locale?.let {
            Locale.setDefault(it)
            configuration.setLocale(locale)
        }

        return context.createConfigurationContext(configuration)
    }

    fun getLocale(preferences: Preferences, configuration: Configuration): Locale? {
        // Get locale selected and saved by user
        val selectedLanguage = preferences.getSelectedLanguage()
        if (selectedLanguage == AppLanguage.SYSTEM) {
            // If there is no locale saved, get system locale
            val systemLocale = configuration.locales[0]
            if (systemLocale != null) {
                return systemLocale
            }
        } else {
            return selectedLanguage.value?.let { Locale(it) }
        }
        return null
    }

    fun getUrl(environment: EIDEnvironment): String {
        return when (environment) {
            EIDEnvironment.MINV_TEST -> AppConstants.AUTH_BASE_URL_MINV_TEST
            EIDEnvironment.MINV_PROD -> AppConstants.AUTH_BASE_URL_MINV_PROD
            else -> throw Exception("URL not available")
        }
    }

    fun getClientId(environment: EIDEnvironment): String {
        return when (environment) {
            EIDEnvironment.MINV_TEST -> AppConstants.CLIENT_ID_MINV_TEST
            EIDEnvironment.MINV_PROD -> AppConstants.CLIENT_ID_MINV_PROD
            else -> throw Exception("ClientID not available")
        }
    }

    fun getClientSecret(environment: EIDEnvironment): String {
        return when (environment) {
            EIDEnvironment.MINV_TEST -> AppConstants.CLIENT_SECRET_MINV_TEST
            EIDEnvironment.MINV_PROD -> AppConstants.CLIENT_SECRET_MINV_PROD
            else -> throw Exception("ClientSecret not available")
        }
    }
}
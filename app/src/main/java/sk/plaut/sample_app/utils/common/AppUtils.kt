package sk.plaut.sample_app.utils.common

import android.content.Context
import android.content.res.Configuration
import sk.plaut.sample_app.utils.managers.Preferences
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
        if (selectedLanguage != null) {
            return Locale(selectedLanguage)
        }

        // If there is no locale saved, get system locale
        val systemLocale = configuration.locales[0]
        if (systemLocale != null) {
            return systemLocale
        }

        return null
    }
}
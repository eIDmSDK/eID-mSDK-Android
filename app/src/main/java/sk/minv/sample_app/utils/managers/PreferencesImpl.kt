package sk.minv.sample_app.utils.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.minv.sample_app.data.AppLanguage
import sk.minv.sample_app.data.AuthenticationFlow

internal class PreferencesImpl(private val context: Context) : Preferences {

    /*-------------------------*/
    /*        CONSTANTS        */
    /*-------------------------*/

    private companion object {
        private const val FILE_NAME = "secured_preferences"
        private const val ENVIRONMENT = "environment"
        private const val LANGUAGE = "language"
        private const val AUTH_FLOW = "auth_flow"
        private const val IS_TUTORIAL_COMPLETED = "is_tutorial_completed"
    }

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val default by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val secured by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /*-------------------------*/
    /*       SET METHODS       */
    /*-------------------------*/

    override fun setSelectedEnvironment(environment: EIDEnvironment) {
        putInt(default, ENVIRONMENT, environment.ordinal)
    }

    override fun setSelectedLanguage(language: AppLanguage) {
        putInt(default, LANGUAGE, language.ordinal)
    }

    override fun setSelectedAuthenticationFlow(flow: AuthenticationFlow) {
        putInt(default, AUTH_FLOW, flow.ordinal)
    }

    override fun saveTutorialCompleted() {
        putBoolean(default, IS_TUTORIAL_COMPLETED, true)
    }

    override fun resetTutorialCompleted() {
        putBoolean(default, IS_TUTORIAL_COMPLETED, false)
    }

    /*-------------------------*/
    /*       GET METHODS       */
    /*-------------------------*/

    override fun getSelectedEnvironment(): EIDEnvironment {
        return EIDEnvironment.values()[getInt(default, ENVIRONMENT, EIDEnvironment.PLAUT_TEST.ordinal)]
    }

    override fun getSelectedLanguage(): AppLanguage {
        return AppLanguage.values()[getInt(default, LANGUAGE, AppLanguage.SYSTEM.ordinal)]
    }

    override fun getSelectedAuthenticationFlow(): AuthenticationFlow {
        return AuthenticationFlow.values()[getInt(default, AUTH_FLOW, AuthenticationFlow.IMPLICIT.ordinal)]
    }

    override fun isTutorialCompleted(): Boolean {
        return getBoolean(default, IS_TUTORIAL_COMPLETED, false)
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun putString(
        preferences: SharedPreferences,
        key: String,
        value: String?
    ) {
        preferences.edit {
            putString(key, value)
        }
    }

    private fun putInt(
            preferences: SharedPreferences,
            key: String,
            value: Int
    ) {
        preferences.edit {
            putInt(key, value)
        }
    }

    private fun putLong(
            preferences: SharedPreferences,
            key: String,
            value: Long
    ) {
        preferences.edit {
            putLong(key, value)
        }
    }

    private fun putBoolean(
        preferences: SharedPreferences,
        key: String,
        value: Boolean
    ) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    private fun getString(
        preferences: SharedPreferences,
        key: String,
        defValue: String? = null
    ): String? {
        return preferences.getString(key, defValue) ?: defValue
    }

    private fun getInt(
        preferences: SharedPreferences,
        key: String,
        defValue: Int = 0
    ): Int {
        return preferences.getInt(key, defValue)
    }

    private fun getLong(
        preferences: SharedPreferences,
        key: String,
        defValue: Long = 0
    ): Long {
        return preferences.getLong(key, defValue)
    }

    private fun getBoolean(
        preferences: SharedPreferences,
        key: String,
        defValue: Boolean = false
    ): Boolean {
        return preferences.getBoolean(key, defValue)
    }


}
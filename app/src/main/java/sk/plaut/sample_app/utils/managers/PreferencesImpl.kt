package sk.plaut.sample_app.utils.managers

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import sk.plaut.sample_app.utils.common.StringUtils
import java.util.*

internal class PreferencesImpl(private val context: Context) : Preferences {

    /*-------------------------*/
    /*        CONSTANTS        */
    /*-------------------------*/

    private companion object {
        private const val FILE_NAME = "secured_preferences"
        private const val SELECTED_LANGUAGE = "selected_language"
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

    override fun setSelectedLanguage(language: String) {
        putString(default, SELECTED_LANGUAGE, language)
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

    override fun getSelectedLanguage(): String? {
        return getString(default, SELECTED_LANGUAGE)
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
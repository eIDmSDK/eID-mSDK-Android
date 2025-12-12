package sk.minv.sample_app.utils.managers

import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.minv.sample_app.data.AppLanguage

interface Preferences {

    fun setSelectedEnvironment(environment: EIDEnvironment)

    fun setSelectedLanguage(language: AppLanguage)

    fun saveTutorialCompleted()

    fun resetTutorialCompleted()

    fun getSelectedEnvironment(): EIDEnvironment

    fun getSelectedLanguage(): AppLanguage

    fun isTutorialCompleted(): Boolean
}
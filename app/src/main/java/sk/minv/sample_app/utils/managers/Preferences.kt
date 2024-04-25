package sk.minv.sample_app.utils.managers

import sk.eid.eidhandlerpublic.EIDEnvironment
import sk.minv.sample_app.data.AppLanguage
import sk.minv.sample_app.data.AuthenticationFlow

interface Preferences {

    fun setSelectedEnvironment(environment: EIDEnvironment)

    fun setSelectedLanguage(language: AppLanguage)

    fun setSelectedAuthenticationFlow(flow: AuthenticationFlow)

    fun saveTutorialCompleted()

    fun resetTutorialCompleted()

    fun getSelectedEnvironment(): EIDEnvironment

    fun getSelectedLanguage(): AppLanguage

    fun getSelectedAuthenticationFlow(): AuthenticationFlow

    fun isTutorialCompleted(): Boolean
}
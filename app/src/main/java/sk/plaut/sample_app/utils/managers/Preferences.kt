package sk.plaut.sample_app.utils.managers

interface Preferences {

    fun setSelectedLanguage(language: String)

    fun saveTutorialCompleted()

    fun resetTutorialCompleted()

    fun getSelectedLanguage(): String?

    fun isTutorialCompleted(): Boolean
}
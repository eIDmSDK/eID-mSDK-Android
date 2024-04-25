package sk.minv.sample_app.ui.startup

import sk.minv.base.base.activity.BaseHandler

interface StartupHandler : BaseHandler {

    fun openTutorialScreen(language: String?)

    fun openMainScreen()
}
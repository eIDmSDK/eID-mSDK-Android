package sk.plaut.sample_app.ui.startup

import sk.plaut.base.base.activity.BaseHandler

interface StartupHandler : BaseHandler {

    fun openTutorialScreen()

    fun openMainScreen()
}
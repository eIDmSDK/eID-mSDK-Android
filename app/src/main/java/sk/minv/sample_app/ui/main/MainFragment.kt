package sk.minv.sample_app.ui.main

import android.nfc.TagLostException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.inject
import sk.eid.eidexceptions.*
import sk.minv.base.base.fragment.BaseFragment
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.exceptions.ServerException
import sk.minv.base.ui.ConfirmDialogFragment
import sk.minv.base.ui.MessageDialogFragment
import sk.minv.base.utils.common.DialogUtils
import sk.minv.base.utils.helpers.onClick
import sk.minv.base.utils.helpers.subscribe
import sk.minv.sample_app.R
import sk.minv.sample_app.data.UserData
import sk.minv.sample_app.databinding.FragmentMainBinding
import sk.minv.sample_app.utils.common.AppConstants
import sk.minv.sample_app.utils.common.AppUtils
import sk.minv.sample_app.utils.common.StringUtils
import sk.minv.sample_app.utils.managers.Preferences
import timber.log.Timber

class MainFragment : BaseFragment<FragmentMainBinding, MainHandler>(), KoinComponent,
    ConfirmDialogFragment.ConfirmDialogListener, MessageDialogFragment.MessageDialogListener {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: MainViewModel by viewModel()
    private val preferences: Preferences by inject()

    private var language: String? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.getUserDataLoadState.subscribe(this, ::handleGetUserDataLoadState)
    }

    override fun onViewReady() {
        // Show title
        val titlePart1 = getString(R.string.main__title_central_portal)
        val titlePart2 = getString(R.string.main__title_public_administration)
        binding.txtTitle.text = context?.let {
            StringUtils.getTitleSpannableText(
                it,
                titlePart1,
                titlePart2
            )
        }

        // Button actions

        binding.btnLogIn.onClick {
            handler.openAuthenticationScreen(language)
        }

        binding.btnLogOut.onClick {
            binding.btnLogIn.visibility = View.VISIBLE
            binding.btnLogOut.visibility = View.GONE
            binding.txtUserLabel.visibility = View.INVISIBLE
            binding.txtUser.visibility = View.INVISIBLE
        }

        binding.btnCertificates.onClick {
            handler.openCertificatesScreen(language)
        }

        binding.btnSignForm.onClick {
            handler.openSigningScreen()
        }

        binding.btnDecryptData.onClick {
            handler.openDecryptScreen()
        }

        binding.btnPinManagement.onClick {
            handler.openPINManagementScreen(language)
        }

        binding.btnTutorial.onClick {
            handler.openTutorialScreen(language)
        }

        binding.btnSettings.onClick {
            handler.openSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        // Get language - Selected by user or system language
        language = context?.let {
            val configuration = it.resources.configuration
            AppUtils.getLocale(preferences, configuration)?.language
        }
    }

    override fun onConfirmDialogAction(action: ConfirmDialogFragment.ConfirmDialogAction?, tag: String?) {
        if (action == ConfirmDialogFragment.ConfirmDialogAction.POSITIVE_BUTTON_CLICK) {
            when (tag) {
                AppConstants.DIALOG_TAG_PIN_MANAGEMENT -> handler.openPINManagementScreen(language)
            }
        }
    }

    override fun onMessageDialogAction(tag: String?) { }

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun onIdTokenReceived(idToken: String?) {
        // Parse ID token
        viewModel.getUserData(idToken)
    }

    fun onAuthCodeReceived(authCode: String) {
        // Get ID token
        viewModel.getToken(authCode)
    }

    /**
     * Show error
     */
    fun onEidExceptionReceived(exception: Throwable?) {
        exception?.let {
            when (it) {
                is DeviceRootedException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__device_rooted))
                is CertificateNotFoundException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__certificate_not_found))
                is PINNotActivatedException -> showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__pin_not_activated))
                is PINBlockedException -> {
                    showConfirmDialog(
                        getString(R.string.general__error),
                        getString(R.string.error__message__pin_blocked),
                        getString(R.string.error__action__pin_blocked),
                        AppConstants.DIALOG_TAG_PIN_MANAGEMENT
                    )
                }
                is TagLostException,
                is ServerException,
                is EacFailedException -> {
                    showMessageDialog(getString(R.string.general__error), getString(R.string.error__message__unexpected_error))
                }
                is UnsupportedSDKVersionException -> {
                    // Update SDK version
                    Timber.e(it)
                }
                is SecurityException -> showMessageDialog(getString(R.string.general__error), "SecurityException")
            }
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun handleGetUserDataLoadState(userInfoState: DataLoadState<UserData>) {
        when (userInfoState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                // Show logged in user data
                binding.btnLogIn.visibility = View.GONE
                binding.btnLogOut.visibility = View.VISIBLE
                binding.txtUserLabel.visibility = View.VISIBLE
                binding.txtUser.visibility = View.VISIBLE
                binding.txtUser.text = String.format("%s %s", userInfoState.data.given_name, userInfoState.data.family_name)
            }
            is DataLoadState.Error -> showErrorDialog(userInfoState.error)
        }
    }

    private fun showConfirmDialog(title: String, message: String, positiveButton: String, tag: String) {
        DialogUtils.showConfirmDialog(title, message, positiveButton, getString(R.string.general__cancel), childFragmentManager, tag)
    }

    private fun showMessageDialog(title: String, message: String) {
        DialogUtils.showMessageDialog(title, message, getString(R.string.general__ok), childFragmentManager)
    }
}
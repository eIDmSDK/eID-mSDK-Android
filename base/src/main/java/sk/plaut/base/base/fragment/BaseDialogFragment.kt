package sk.plaut.base.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.NullPointerException

abstract class BaseDialogFragment : DialogFragment() {

    /*-------------------------*/
    /*        CONSTANTS        */
    /*-------------------------*/

    companion object {
        const val EMPTY_STRING = ""

        const val EXTRA_TITLE_RES_ID = "title_res_id"
        const val EXTRA_TITLE_TEXT = "title_text"
        const val EXTRA_MESSAGE_RES_ID = "message_res_id"
        const val EXTRA_MESSAGE_TEXT = "message_text"
        const val EXTRA_POSITIVE_BUTTON_RES_ID = "positive_button_res_id"
        const val EXTRA_POSITIVE_BUTTON_TEXT = "positive_button_text"
        const val EXTRA_NEGATIVE_BUTTON_RES_ID = "negative_button_res_id"
        const val EXTRA_NEGATIVE_BUTTON_TEXT = "negative_button_text"
        const val EXTRA_TAG = "tag"
    }

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private var dialogView: View? = null

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    override fun show(manager: FragmentManager, tag: String?) {
        val dialog = manager.findFragmentByTag(tag)
        if (null == dialog) {
            super.show(manager, tag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getParameters()
        setListeners()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            dialogView = LayoutInflater.from(it).inflate(getLayoutRes(), null)

            val builder = AlertDialog.Builder(it, getDialogTheme())
            builder.setView(dialogView)

            val dialog: Dialog = builder.create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            return dialog
        } ?: throw NullPointerException("Attempt to invoke getContext() on a null object reference")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView?.let {
            inflateView(it)
            alterView()
        }
        return dialogView
    }

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    protected abstract fun getParameters()
    protected abstract fun setListeners()
    @LayoutRes
    protected abstract fun getLayoutRes(): Int
    protected abstract fun getDialogTheme(): Int
    protected abstract fun inflateView(view: View)
    protected abstract fun alterView()

    /**
     * Activity listener - if an activity should listen to dialog button action
     *
     * @param clazz
     * @param <E>
     * @return
    </E> */
    protected open fun <E> getActivityListener(clazz: Class<E>): E? {
        return try {
            clazz.cast(activity)
        } catch (ex: ClassCastException) {
            throw ClassCastException(String.format("%s must implement %s", activity.toString(), clazz.toString()))
        }
    }

    /**
     * Fragment listener - if an fragment should listen to dialog button action
     *
     * @param clazz
     * @param <E>
     * @return
    </E> */
    protected open fun <E> getParentFragmentListener(clazz: Class<E>): E? {
        return try {
            clazz.cast(parentFragment)
        } catch (ex: ClassCastException) {
            throw ClassCastException(String.format("%s must implement %s", parentFragment.toString(), clazz.toString()))
        }
    }

    /**
     * Get integer parameter
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected open fun getArgsExtraInt(key: String?, defaultValue: Int): Int {
        val args = arguments
        return args?.getInt(key, defaultValue) ?: defaultValue
    }

    /**
     * Get double parameter
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected open fun getArgsExtraDouble(key: String?, defaultValue: Double): Double {
        val args = arguments
        return args?.getDouble(key, defaultValue) ?: defaultValue
    }

    /**
     * Get string parameter
     *
     * @param keyText
     * @param keyResId
     * @param defaultValue
     * @return
     */
    protected open fun getArgsExtraString(keyText: String?, keyResId: String?, defaultValue: String?): String? {
        val args = arguments
        return if (args != null) {
            if (args.getInt(keyResId, 0) == 0) {
                args.getString(keyText, defaultValue)
            } else args.getString(keyResId)
        } else defaultValue
    }

    /**
     * Request to show keyboard for dialog
     */
    protected open fun setSoftInputVisible() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}
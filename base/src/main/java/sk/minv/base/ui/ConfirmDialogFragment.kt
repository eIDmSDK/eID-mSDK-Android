package sk.minv.base.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import sk.minv.base.R
import sk.minv.base.base.fragment.BaseDialogFragment
import sk.minv.base.utils.helpers.onClick

class ConfirmDialogFragment : BaseDialogFragment() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private var mTitleView: TextView? = null
    private var mMessageView: TextView? = null
    private var mPositiveButtonView: TextView? = null
    private var mNegativeButtonView: TextView? = null

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mPositiveButton: String? = null
    private var mNegativeButton: String? = null
    private var mTag: String? = null
    private var mListener: ConfirmDialogListener? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(args: Bundle): ConfirmDialogFragment {
            val dialogFragment =  ConfirmDialogFragment()
            dialogFragment.arguments = args
            return dialogFragment
        }
    }

    /*-------------------------*/
    /*          ENUMS          */
    /*-------------------------*/

    enum class ConfirmDialogAction {
        POSITIVE_BUTTON_CLICK,
        NEGATIVE_BUTTON_CLICK
    }

    /*-------------------------*/
    /*        LISTENERS        */
    /*-------------------------*/

    interface ConfirmDialogListener {
        fun onConfirmDialogAction(action: ConfirmDialogAction?, tag: String?)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getParameters() {
        mTitle = getArgsExtraString(EXTRA_TITLE_TEXT, EXTRA_TITLE_RES_ID, EMPTY_STRING)
        mMessage = getArgsExtraString(EXTRA_MESSAGE_TEXT, EXTRA_MESSAGE_RES_ID, EMPTY_STRING)
        mPositiveButton = getArgsExtraString(EXTRA_POSITIVE_BUTTON_TEXT, EXTRA_POSITIVE_BUTTON_RES_ID, EMPTY_STRING)
        mNegativeButton = getArgsExtraString(EXTRA_NEGATIVE_BUTTON_TEXT, EXTRA_NEGATIVE_BUTTON_RES_ID, EMPTY_STRING)
        mTag = getArgsExtraString(EXTRA_TAG, EXTRA_TAG, EMPTY_STRING)
    }

    override fun setListeners() {
        mListener = parentFragment?.let {
            getParentFragmentListener(ConfirmDialogListener::class.java)
        } ?: getActivityListener(ConfirmDialogListener::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.dialog_fragment__confirm

    override fun getDialogTheme(): Int = R.style.DialogTheme

    override fun inflateView(view: View) {
        mTitleView = view.findViewById(R.id.dialog_title)
        mMessageView = view.findViewById(R.id.dialog_message)
        mPositiveButtonView = view.findViewById(R.id.dialog_button_positive_text)
        mNegativeButtonView = view.findViewById(R.id.dialog_button_negative_text)
    }

    override fun alterView() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView?.text = mTitle
        }
        if (!TextUtils.isEmpty(mMessage)) {
            mMessageView?.text = mMessage
        }
        if (!TextUtils.isEmpty(mPositiveButton)) {
            mPositiveButtonView?.text = mPositiveButton
        }
        if (!TextUtils.isEmpty(mNegativeButton)) {
            mNegativeButtonView?.text = mNegativeButton
        }

        mPositiveButtonView?.onClick {
            mListener?.onConfirmDialogAction(ConfirmDialogAction.POSITIVE_BUTTON_CLICK, mTag)
            dismiss()
        }

        mNegativeButtonView?.onClick {
            mListener?.onConfirmDialogAction(ConfirmDialogAction.NEGATIVE_BUTTON_CLICK, mTag)
            dismiss()
        }
    }

    /*-------------------------*/
    /*      INNER CLASSES      */
    /*-------------------------*/

    class Builder {
        private val args: Bundle = Bundle()

        fun setTitle(title: String?): Builder {
            args.putString(EXTRA_TITLE_TEXT, title)
            return this
        }

        fun setTitleResId(titleResId: Int): Builder {
            args.putInt(EXTRA_TITLE_RES_ID, titleResId)
            return this
        }

        fun setMessage(message: String?): Builder {
            args.putString(EXTRA_MESSAGE_TEXT, message)
            return this
        }

        fun setMessageResId(messageResId: Int): Builder {
            args.putInt(EXTRA_MESSAGE_RES_ID, messageResId)
            return this
        }

        fun setPositiveButtonText(text: String?): Builder {
            args.putString(EXTRA_POSITIVE_BUTTON_TEXT, text)
            return this
        }

        fun setPositiveButtonTextResId(textResId: Int): Builder {
            args.putInt(EXTRA_POSITIVE_BUTTON_RES_ID, textResId)
            return this
        }

        fun setNegativeButtonText(text: String?): Builder {
            args.putString(EXTRA_NEGATIVE_BUTTON_TEXT, text)
            return this
        }

        fun setNegativeButtonTextResId(textResId: Int): Builder {
            args.putInt(EXTRA_NEGATIVE_BUTTON_RES_ID, textResId)
            return this
        }

        fun setTag(tag: String?): Builder {
            args.putString(EXTRA_TAG, tag)
            return this
        }

        fun build(): ConfirmDialogFragment {
            return newInstance(args)
        }
    }
}
package sk.minv.base.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import sk.minv.base.R
import sk.minv.base.base.fragment.BaseDialogFragment
import sk.minv.base.utils.helpers.onClick

class MessageDialogFragment : BaseDialogFragment() {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private var mTitleView: TextView? = null
    private var mMessageView: TextView? = null
    private var mButtonView: TextView? = null

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mButton: String? = null
    private var mTag: String? = null
    private var mListener: MessageDialogListener? = null

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(args: Bundle): MessageDialogFragment {
            val dialogFragment =  MessageDialogFragment()
            dialogFragment.arguments = args
            return dialogFragment
        }
    }

    /*-------------------------*/
    /*        LISTENERS        */
    /*-------------------------*/

    interface MessageDialogListener {
        fun onMessageDialogAction(tag: String?)
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getParameters() {
        mTitle = getArgsExtraString(EXTRA_TITLE_TEXT, EXTRA_TITLE_RES_ID, EMPTY_STRING)
        mMessage = getArgsExtraString(EXTRA_MESSAGE_TEXT, EXTRA_MESSAGE_RES_ID, EMPTY_STRING)
        mButton = getArgsExtraString(EXTRA_POSITIVE_BUTTON_TEXT, EXTRA_POSITIVE_BUTTON_RES_ID, EMPTY_STRING)
        mTag = getArgsExtraString(EXTRA_TAG, EXTRA_TAG, EMPTY_STRING)
    }

    override fun setListeners() {
        mListener = parentFragment?.let {
            getParentFragmentListener(MessageDialogListener::class.java)
        } ?: getActivityListener(MessageDialogListener::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.dialog_fragment__message

    override fun getDialogTheme(): Int = R.style.DialogTheme

    override fun inflateView(view: View) {
        mTitleView = view.findViewById(R.id.dialog_title)
        mMessageView = view.findViewById(R.id.dialog_message)
        mButtonView = view.findViewById(R.id.dialog_button)
    }

    override fun alterView() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView?.text = mTitle
        }
        if (!TextUtils.isEmpty(mMessage)) {
            mMessageView?.text = mMessage
        }
        if (!TextUtils.isEmpty(mButton)) {
            mButtonView?.text = mButton
        }

        mButtonView?.onClick {
            mListener?.onMessageDialogAction(mTag)
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

        fun setButtonText(text: String?): Builder {
            args.putString(EXTRA_POSITIVE_BUTTON_TEXT, text)
            return this
        }

        fun setButtonTextResId(textResId: Int): Builder {
            args.putInt(EXTRA_POSITIVE_BUTTON_RES_ID, textResId)
            return this
        }

        fun setTag(tag: String?): Builder {
            args.putString(EXTRA_TAG, tag)
            return this
        }

        fun build(): MessageDialogFragment {
            return newInstance(args)
        }
    }
}
package sk.minv.base.utils.common

import androidx.fragment.app.FragmentManager
import sk.minv.base.ui.ConfirmDialogFragment
import sk.minv.base.ui.MessageDialogFragment

object DialogUtils {

    /*-------------------------*/
    /*     Message Dialog      */
    /*-------------------------*/

    fun showMessageDialog(
        titleResId: Int,
        messageResId: Int,
        buttonResId: Int,
        fragmentManager: FragmentManager
    ) {

        val messageDialog: MessageDialogFragment = MessageDialogFragment.Builder()
            .setTitleResId(titleResId)
            .setMessageResId(messageResId)
            .setButtonTextResId(buttonResId)
            .build()

        messageDialog.show(fragmentManager, MessageDialogFragment::class.java.name)
    }

    fun showMessageDialog(
        titleResId: Int,
        messageResId: Int,
        buttonResId: Int,
        fragmentManager: FragmentManager,
        tag: String
    ) {

        val messageDialog: MessageDialogFragment = MessageDialogFragment.Builder()
            .setTitleResId(titleResId)
            .setMessageResId(messageResId)
            .setButtonTextResId(buttonResId)
            .setTag(tag)
            .build()

        messageDialog.show(fragmentManager, MessageDialogFragment::class.java.name)
    }

    fun showMessageDialog(
        titleString: String,
        messageString: String,
        buttonString: String,
        fragmentManager: FragmentManager
    ) {

        val messageDialog: MessageDialogFragment = MessageDialogFragment.Builder()
            .setTitle(titleString)
            .setMessage(messageString)
            .setButtonText(buttonString)
            .build()

        messageDialog.show(fragmentManager, MessageDialogFragment::class.java.name)
    }

    fun showMessageDialog(
        titleString: String,
        messageString: String,
        buttonString: String,
        fragmentManager: FragmentManager,
        tag: String
    ) {

        val messageDialog: MessageDialogFragment = MessageDialogFragment.Builder()
            .setTitle(titleString)
            .setMessage(messageString)
            .setButtonText(buttonString)
            .setTag(tag)
            .build()

        messageDialog.show(fragmentManager, MessageDialogFragment::class.java.name)
    }


    /*-------------------------*/
    /*     Confirm Dialog      */
    /*-------------------------*/

    fun showConfirmDialog(
        titleResId: Int,
        messageResId: Int,
        positiveButtonResId: Int,
        negativeButtonResId: Int,
        fragmentManager: FragmentManager
    ) {

        val confirmDialog: ConfirmDialogFragment = ConfirmDialogFragment.Builder()
                .setTitleResId(titleResId)
                .setMessageResId(messageResId)
                .setPositiveButtonTextResId(positiveButtonResId)
                .setNegativeButtonTextResId(negativeButtonResId)
                .build()

        confirmDialog.show(fragmentManager, ConfirmDialogFragment::class.java.name)
    }

    fun showConfirmDialog(
        titleResId: Int,
        messageResId: Int,
        positiveButtonResId: Int,
        negativeButtonResId: Int,
        fragmentManager: FragmentManager,
        tag: String
    ) {

        val confirmDialog: ConfirmDialogFragment = ConfirmDialogFragment.Builder()
                .setTitleResId(titleResId)
                .setMessageResId(messageResId)
                .setPositiveButtonTextResId(positiveButtonResId)
                .setNegativeButtonTextResId(negativeButtonResId)
                .setTag(tag)
                .build()

        confirmDialog.show(fragmentManager, ConfirmDialogFragment::class.java.name)
    }

    fun showConfirmDialog(
        titleString: String,
        messageString: String,
        positiveButtonString: String,
        negativeButtonString: String,
        fragmentManager: FragmentManager
    ) {

        val confirmDialog: ConfirmDialogFragment = ConfirmDialogFragment.Builder()
                .setTitle(titleString)
                .setMessage(messageString)
                .setPositiveButtonText(positiveButtonString)
                .setNegativeButtonText(negativeButtonString)
                .build()

        confirmDialog.show(fragmentManager, ConfirmDialogFragment::class.java.name)
    }

    fun showConfirmDialog(
        titleString: String,
        messageString: String,
        positiveButtonString: String,
        negativeButtonString: String,
        fragmentManager: FragmentManager,
        tag: String
    ) {

        val confirmDialog: ConfirmDialogFragment = ConfirmDialogFragment.Builder()
                .setTitle(titleString)
                .setMessage(messageString)
                .setPositiveButtonText(positiveButtonString)
                .setNegativeButtonText(negativeButtonString)
                .setTag(tag)
                .build()

        confirmDialog.show(fragmentManager, ConfirmDialogFragment::class.java.name)
    }
}
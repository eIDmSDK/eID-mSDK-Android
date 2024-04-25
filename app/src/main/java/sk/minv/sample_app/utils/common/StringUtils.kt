package sk.minv.sample_app.utils.common

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import sk.minv.sample_app.R

object StringUtils {

    /*-------------------------*/
    /*        CONSTANTS        */
    /*-------------------------*/

    const val EMPTY_STRING = ""

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun getTitleSpannableText(
        context: Context,
        spannableTextRed: String,
        spannableTextPrimary: String
    ): SpannableString {

        val text = String.format("%s %s", spannableTextRed, spannableTextPrimary)
        val spannableText = SpannableString(text)
        var startIndex = text.indexOf(spannableTextRed)
        var endIndex = startIndex + spannableTextRed.length

        spannableText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        startIndex = text.indexOf(spannableTextPrimary)
        endIndex = startIndex + spannableTextPrimary.length

        spannableText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableText
    }
}
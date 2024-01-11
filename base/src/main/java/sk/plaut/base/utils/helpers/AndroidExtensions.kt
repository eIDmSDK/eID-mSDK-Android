package sk.plaut.base.utils.helpers

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sk.plaut.base.base.activity.AndroidParameters
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

/*-------------------------*/
/*        CONSTANT         */
/*-------------------------*/

private const val PARAMETERS_EXTRA_KEY = "PARAMETERS_EXTRA_KEY"

/*-------------------------*/
/*        ACTIVITY         */
/*-------------------------*/

fun <PARAMETERS : AndroidParameters> Intent.putExtra(parameters: PARAMETERS) {
    putExtra(PARAMETERS_EXTRA_KEY, parameters)
}

fun <PARAMETERS : AndroidParameters> Bundle.getExtra(): PARAMETERS? {
    return this.getParcelable(PARAMETERS_EXTRA_KEY)
}

inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Serializable> Bundle.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

fun FragmentActivity.showFragment(@IdRes container: Int, fragment: Fragment, tag: String) {
    supportFragmentManager
        .beginTransaction()
        .add(container, fragment, tag)
        .commit()
}

fun FragmentActivity.getFragment(tag: String) {
    supportFragmentManager.findFragmentByTag(tag)
}

/*-------------------------*/
/*        FRAGMENT         */
/*-------------------------*/

inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onDataReceived: (T) -> Unit) =
    observe(owner, Observer { onDataReceived(it) })

/*-------------------------*/
/*       VIEW MODEL        */
/*-------------------------*/

inline fun ViewModel.launch(
    coroutineContext: CoroutineContext = AppDispatchers().MAIN,
    crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(coroutineContext) { block() }
}

/*-------------------------*/
/*          VIEWS          */
/*-------------------------*/

inline fun View.onClick(crossinline onClick: () -> Unit) {
    setOnClickListener { onClick() }
}

fun ScrollView.scrollToBottom() {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
        val lastChild = getChildAt(childCount - 1)
        val bottom = lastChild.bottom + paddingBottom
        val delta = bottom - (scrollY+ height)
        smoothScrollBy(0, delta)
    }, 500)
}
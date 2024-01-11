package sk.plaut.base.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import sk.plaut.base.base.activity.BaseHandler
import sk.plaut.base.utils.helpers.InstanceStateProvider

abstract class BaseFragment<BINDING, HANDLER> : Fragment() where BINDING : ViewBinding, HANDLER : BaseHandler {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val parameters = Bundle()
    private var _binding: BINDING? = null
    protected val binding get() = _binding!!
    private var _handler: HANDLER? = null
    protected val handler get() = _handler!!

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    init {
        arguments = parameters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (arguments != null) {
            parameters.putAll(arguments)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeToData()
        onViewModelReady()
        onViewReady()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        _handler = context as HANDLER
    }

    override fun onDetach() {
        super.onDetach()
        _handler = null
    }

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    protected fun <T> nullableParameter() = InstanceStateProvider.Nullable<T>(parameters)
    protected fun <T> notNullParameter() = InstanceStateProvider.NotNull<T>(parameters)
    protected fun <T> notNullWithDefaultParameter(defaultValue: T) = InstanceStateProvider.NotNullWithDefault(parameters, defaultValue)

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING

    protected open fun subscribeToData() {}
    protected open fun onViewModelReady() {}
    protected open fun onViewReady() {}

    protected fun showErrorDialog(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }
}
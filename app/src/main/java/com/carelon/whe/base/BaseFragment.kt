package com.carelon.whe.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.carelon.whe.domain.model.ErrorItem
import com.carelon.whe.ui.dialog.ErrorDialogFragment
import com.carelon.whe.util.safeShow

/**
 * Base fragment class for every fragment in this application
 */
abstract class BaseFragment<out T: ViewDataBinding> : Fragment() {

    private var mViewDataBinding : T ?= null

    private var mViewModel: BaseViewModel ?= null

    var mActivity: BaseActivity<*,*>? = null

    /**
     * Assign layout file for the corresponding fragment
     */
    abstract fun getContentView() : Int

    /**
     * Assign binding variable for the corresponding fragment, that helps to achieve data binding
     */
    abstract fun getBindingVariable() : Int

    /**
     * Assign view model for each fragment
     */
    abstract fun getViewModel() : BaseViewModel?

    /**
     * In @onCreateView, we will be initializing @mViewDataBinding with corresponding view binding
     * Assign lifecycle owner to the view biding as well
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        mViewDataBinding?.lifecycleOwner = this
        return mViewDataBinding?.root
    }

    /**
     * Initialize the view model in the @onCreate will help to assign variable to the view data binding
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    /**
     * Assign binding variable to the view data binding and execute any pending bindings
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding?.executePendingBindings()
    }

    /**
     * Function to show the common error popup with retry option
     */
    fun showErrorDialog(errorItem: ErrorItem, retryAction: (() -> Unit)? = null) {
        mActivity?.showErrorDialog(errorItem) {
            retryAction?.invoke()
        }
    }

    /**
     * This method will return instance of ViewDataBinding for the corresponding fragment, that
     * will help to find any view related operation inside the fragment
     */
    fun getDataBinding() = mViewDataBinding

    /**
     * This method will attach the current fragment to the parent activity
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*,*>) {
            val activity = context as BaseActivity<*,*>?
            mActivity = activity
        }
    }

    /**
     * This method will make the activity instance as null
     */
    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewDataBinding = null
    }
}
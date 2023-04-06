package com.carelon.whe.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.carelon.whe.R

/**
 * Base dialog fragment class for every dialog in this application
 */
abstract class BaseDialogFragment<out T: ViewDataBinding> : DialogFragment() {

    private var mViewDataBinding : T ?= null

    private var mViewModel: BaseViewModel ?= null

    var mActivity: BaseActivity<*,*>? = null

    /**
     * Assign layout file for the corresponding dialog fragment
     */
    abstract fun getContentView() : Int

    /**
     * Assign binding variable for the corresponding dialog fragment, that helps to achieve data binding
     */
    abstract fun getBindingVariable() : Int

    /**
     * Assign view model for each dialog fragment
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
        setStyle(STYLE_NO_TITLE, R.style.Theme_WholeHealthExperience_Dialog)
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
     * This method will return instance of ViewDataBinding for the corresponding dialog fragment, that
     * will help to find any view related operation inside the dialog fragment
     */
    fun getDataBinding() = mViewDataBinding

    /**
     * This method will attach the current dialog fragment to the parent activity
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

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
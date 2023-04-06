package com.carelon.whe.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseDialogFragment
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.FragmentErrorDialogBinding
import com.carelon.whe.domain.model.ErrorItem
import com.carelon.whe.util.parcelable
import com.carelon.whe.util.safeDismiss
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialog fragment class to show error popup
 */
@AndroidEntryPoint
class ErrorDialogFragment : BaseDialogFragment<FragmentErrorDialogBinding>() {

    private val errorDialogViewModel: ErrorDialogViewModel by viewModels()

    override fun getContentView() = R.layout.fragment_error_dialog

    override fun getBindingVariable() = BR.errorDialogViewModel

    override fun getViewModel() = errorDialogViewModel

    private val errorItem: ErrorItem? by lazy {
        requireArguments().parcelable(BundleKeyConstants.EXTRA_ERROR_ITEM)
    }

    private var binding: FragmentErrorDialogBinding? = null

    var onRetryListener: ErrorDialogFragment.OnRetryListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getDataBinding()
        initOnClicks()
        errorDialogViewModel.setErrorData(errorItem)
    }

    private fun initOnClicks() {
        binding?.txtRetry?.setSafeOnClickListener {
            onRetryListener?.onRetryClicked()
            safeDismiss(parentFragmentManager)
        }

        binding?.txtCancel?.setSafeOnClickListener {
            safeDismiss(parentFragmentManager)
        }
    }

    companion object {
        fun newInstance(errorItem: ErrorItem) : ErrorDialogFragment {
            val errorDialogFragment = ErrorDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable(BundleKeyConstants.EXTRA_ERROR_ITEM,errorItem)
            errorDialogFragment.arguments = bundle
            return errorDialogFragment
        }
    }

    interface OnRetryListener {
        fun onRetryClicked()
    }

}
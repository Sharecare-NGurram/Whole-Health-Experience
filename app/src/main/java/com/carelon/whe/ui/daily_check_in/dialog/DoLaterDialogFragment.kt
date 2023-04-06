package com.carelon.whe.ui.daily_check_in.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseDialogFragment
import com.carelon.whe.databinding.FragmentDoLaterDialogBinding
import com.carelon.whe.util.safeDismiss
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialog fragment class to do later daily check in popup
 */
@AndroidEntryPoint
class DoLaterDialogFragment : BaseDialogFragment<FragmentDoLaterDialogBinding>() {

    private val doLaterDialogFragment: DoLaterDialogViewModel by viewModels()

    override fun getContentView(): Int = R.layout.fragment_do_later_dialog

    override fun getBindingVariable(): Int = BR.doLaterDialogViewModel

    override fun getViewModel(): DoLaterDialogViewModel = doLaterDialogFragment

    private var binding: FragmentDoLaterDialogBinding? = null
    var onSkipListener: OnSkipListener? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getDataBinding()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding?.txtSkip?.setSafeOnClickListener {
            onSkipListener?.navigateToWeeklyPlanActivity()
            safeDismiss(parentFragmentManager)
        }

        binding?.btnPickAndEarn?.setSafeOnClickListener {
            safeDismiss(parentFragmentManager)
        }
    }

    companion object {

        fun newInstance(): DoLaterDialogFragment {
            return DoLaterDialogFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    interface OnSkipListener {
        fun navigateToWeeklyPlanActivity()
    }
}
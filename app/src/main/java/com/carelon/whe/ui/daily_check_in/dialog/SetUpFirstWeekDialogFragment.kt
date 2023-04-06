package com.carelon.whe.ui.daily_check_in.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseDialogFragment
import com.carelon.whe.databinding.FragmentSetUpFirstWeekDialogBinding
import com.carelon.whe.util.safeDismiss
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialog fragment class to show when setup for first week completed
 */
@AndroidEntryPoint
class SetUpFirstWeekDialogFragment : BaseDialogFragment<FragmentSetUpFirstWeekDialogBinding>() {

    private val setUpFirstWeekDialogViewModel: SetUpFirstWeekDialogViewModel by viewModels()

    override fun getContentView(): Int = R.layout.fragment_set_up_first_week_dialog

    override fun getBindingVariable(): Int = BR.setUpFirstWeekDialogViewModel

    override fun getViewModel(): SetUpFirstWeekDialogViewModel = setUpFirstWeekDialogViewModel

    private var binding: FragmentSetUpFirstWeekDialogBinding? = null
    var setUpFirstWeekListener: SetUpFirstWeekListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getDataBinding()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding?.btnSetUpFirstWeek?.setSafeOnClickListener {
            setUpFirstWeekListener?.navigateToWeeklyPlanActivity()
            safeDismiss(parentFragmentManager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    interface SetUpFirstWeekListener {
        fun navigateToWeeklyPlanActivity()
    }

    companion object {

        fun newInstance(): SetUpFirstWeekDialogFragment {
            return SetUpFirstWeekDialogFragment()
        }

    }

}
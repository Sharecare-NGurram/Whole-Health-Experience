package com.carelon.whe.ui.dashboard.foryou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.ui.dashboard.dialog.WelcomeDialogFragment
import com.carelon.whe.base.BaseFragment
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.databinding.FragmentForYouBinding
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.ui.dashboard.foryou.calender.CurrentDayRecord
import com.carelon.whe.ui.dashboard.foryou.calender.CurrentWeekAdapter
import com.carelon.whe.ui.email_verification.EmailVerificationActivity
import com.carelon.whe.ui.pick_focus_area.PickFocusAreaActivity
import com.carelon.whe.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * this is the landing page for the dashboard whenever user enters the app
 * */
@AndroidEntryPoint
class ForYouFragment : BaseFragment<FragmentForYouBinding>() {

    private val forYouViewModel by viewModels<ForYouViewModel>()

    @Inject
    lateinit var mAdapter: CurrentWeekAdapter

    @Inject
    lateinit var preferenceHelper: AppPreferenceHelper

    override fun getContentView(): Int = R.layout.fragment_for_you

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): BaseViewModel = forYouViewModel

    private var binding: FragmentForYouBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getDataBinding()
        observeData()
        initOnClicks()
        setUpAdapter()
        checkEmailVerifiedOrNot()
    }

    /**
     * Observe data to update the UI
     * */
    private fun observeData() {
        //observer for user name
        forYouViewModel.userName.observe(this) { userName ->
            checkWhetherToShowWelcomeDialogOrNot(userName)
        }
        //observe to check if email is verified or not
        forYouViewModel.forYouStatus.observe(this) { response ->
            markEmailVerified(response.emailVerified)
            setupOnBoardingState(response.isOnBoardingCompleted)
        }
    }

    /**
     * modify UI incase of onboarding status pending or completed
     * */
    private fun setupOnBoardingState(onBoardingComplete: Boolean) {
        binding?.run {
            if (onBoardingComplete) {
                constraintStepBeforeOnboarding.gone()
                constraintStepAfterOnboarding.show()
                txtUnlockFeature.gone()
                llBottomAfterOnboarding.show()
            } else {
                constraintStepBeforeOnboarding.show()
                constraintStepAfterOnboarding.gone()
                txtUnlockFeature.show()
                llBottomAfterOnboarding.gone()
            }
        }
    }

    private fun initOnClicks() {
        binding?.viewConfirmEmail?.setOnActionClickListener {
            //Callback for the launched activity to check if medication has been selected
            startActivityResult.launch(Intent(activity, EmailVerificationActivity::class.java))
        }
        binding?.viewFocusArea?.setOnActionClickListener {
            mActivity?.launch<PickFocusAreaActivity>()
        }
    }

    //Callback for the launched activity to check if email has been verified
    private val startActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                checkEmailVerifiedOrNot()
            }
        }

    /**
     * callback for the selected date
     * data - contains the information for the selected dates
     */
    private fun setUpAdapter() {
        binding?.rvCalendar?.adapter = mAdapter
        mAdapter.mListener = object : CurrentWeekAdapter.OnDateChange {
            override fun onNewDateSelected(data: CurrentDayRecord?) {
                data?.let {
                    forYouViewModel.setSelectedDate(it.date)
                    binding?.executePendingBindings()
                }
            }
        }
    }

    /**
     * Function to show welcome dialog fragment
     */
    private fun showWelcomeDialog(userName: String) {
        val welcomeDialogFragment = WelcomeDialogFragment.newInstance(userName)
        welcomeDialogFragment.safeShow(parentFragmentManager, ForYouFragment::class.java.simpleName)
    }

    /**
     * Function to check whether do we need to show welcome dialog or not
     */
    private fun checkWhetherToShowWelcomeDialogOrNot(userName: String) {
        if (preferenceHelper.showWelcomeDialog) {
            preferenceHelper.showWelcomeDialog = false
            showWelcomeDialog(userName)
        }
    }

    /**
     * Function to check whether email has been verified or not
     * For now we are saving a status along with API to save the verified email state
     */
    private fun checkEmailVerifiedOrNot() {
        forYouViewModel.getUserData()
    }

    /**
     * Update view to mark email verified
     * */
    private fun markEmailVerified(flag:Boolean) {
        binding?.viewConfirmEmail?.markStepCompleted(flag)
    }

    override fun onDestroyView() {
        mAdapter.mListener = null
        getDataBinding()?.rvCalendar?.adapter = null
        super.onDestroyView()
        binding = null
    }

}
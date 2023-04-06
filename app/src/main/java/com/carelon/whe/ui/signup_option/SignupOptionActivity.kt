package com.carelon.whe.ui.signup_option

import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivitySignupBinding
import com.carelon.whe.ui.login.LoginActivity
import com.carelon.whe.util.NotificationHelper
import com.carelon.whe.util.launchAndFinish
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupOptionActivity : BaseActivity<ActivitySignupBinding, SignUpOptionViewModel>() {

    private val signUpViewModel by viewModels<SignUpOptionViewModel>()

    override fun getViewModel(): SignUpOptionViewModel = signUpViewModel

    override fun getContentView(): Int = R.layout.activity_signup

    override fun getBindingVariable(): Int = BR.vm

    override fun showLightStatusBar(): Boolean = false

    private var binding: ActivitySignupBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        observeData()
        initView()
    }

    private fun observeData() {
        signUpViewModel.prescriptionCountText.observe(this){
            if (it.isNotEmpty())
            NotificationHelper.showNewPrescriptionNotification(
                context = baseContext,
                notificationTitle = getString(R.string.str_new_prescription),
                notificationText = it
            )
        }
    }

    private fun initView() {
        binding?.btnAnthemSingin?.setSafeOnClickListener {
            navigateToNextPage()
        }
    }

    private fun navigateToNextPage() {
        launchAndFinish<LoginActivity> {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
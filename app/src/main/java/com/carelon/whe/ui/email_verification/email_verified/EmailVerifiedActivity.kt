package com.carelon.whe.ui.email_verification.email_verified

import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivityEmailVerifiedBinding
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailVerifiedActivity : BaseActivity<ActivityEmailVerifiedBinding, EmailVerifiedViewModel> (){

    private val emailVerifiedViewModel by viewModels<EmailVerifiedViewModel>()

    override fun getContentView(): Int = R.layout.activity_email_verified

    override fun getBindingVariable(): Int  = BR.vm

    override fun getViewModel(): EmailVerifiedViewModel = emailVerifiedViewModel

    override fun showLightStatusBar(): Boolean  = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        getDataBinding()?.run {
            btnContinue.setSafeOnClickListener {
                finish()
            }
        }
    }
}
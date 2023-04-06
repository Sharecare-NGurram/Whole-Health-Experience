package com.carelon.whe.ui.startup

import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivityGetStartedBinding
import com.carelon.whe.ui.dashboard.DashboardActivity
import com.carelon.whe.util.launchAndFinish
import com.carelon.whe.util.setSafeOnClickListener

class GetStartedActivity : BaseActivity<ActivityGetStartedBinding,GetStartedViewModel>() {

    private val getStartedViewModel by viewModels<GetStartedViewModel>()

    override fun getViewModel(): GetStartedViewModel = getStartedViewModel

    override fun getContentView(): Int = R.layout.activity_get_started

    override fun getBindingVariable(): Int = BR.vm

    override fun showLightStatusBar(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        getDataBinding()?.btnGetStarted?.setSafeOnClickListener {
            navigateToNextPage()
        }
    }

    /**
    * Navigate to next page
    **/
    private fun navigateToNextPage() {
        launchAndFinish<DashboardActivity> {  }
    }
}
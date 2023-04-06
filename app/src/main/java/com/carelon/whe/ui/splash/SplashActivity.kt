package com.carelon.whe.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun getViewModel(): SplashViewModel = splashViewModel

    override fun getContentView(): Int = R.layout.activity_splash

    override fun getBindingVariable(): Int = BR.vm

    override fun showLightStatusBar(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        initView()
        splashViewModel.getCurrentRoute()
    }

    private fun observeData() {
        splashViewModel.currentRouteLD.observe(this){
            navigateToNextPage(it)
        }
    }

    private fun initView() {

    }

}
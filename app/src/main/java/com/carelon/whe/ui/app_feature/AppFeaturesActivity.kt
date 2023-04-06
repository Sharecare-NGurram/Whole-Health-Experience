package com.carelon.whe.ui.app_feature

import android.os.Bundle
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivityAppFeaturesBinding
import com.carelon.whe.ui.app_feature.adapter.AppFeaturesAdapter
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * This activity will highlights app main features to user who logged in for 1st time
 * From next time it will not show and we are saving the status for now
 */
@AndroidEntryPoint
class AppFeaturesActivity : BaseActivity<ActivityAppFeaturesBinding, AppFeaturesViewModel>() {

    private val appFeatureViewModel by viewModels<AppFeaturesViewModel>()

    @Inject
    lateinit var featuresAdapter: AppFeaturesAdapter

    private var binding: ActivityAppFeaturesBinding? = null

    override fun getContentView(): Int = R.layout.activity_app_features

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): AppFeaturesViewModel = appFeatureViewModel

    override fun showLightStatusBar(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        observeData()
        initOnClick()
        appFeatureViewModel.getAppFeatures()
    }

    private fun observeData() {
        appFeatureViewModel.appFeaturesLD.observe(this) {
            featuresAdapter.addAll(it)
            binding?.recyclerFeatures?.adapter = featuresAdapter
        }
        appFeatureViewModel.currentRouteLD.observe(this) {
            navigateToNextPage(it)
        }
    }

    private fun initOnClick() {
        binding?.btnGetStarted?.setSafeOnClickListener {
            appFeatureViewModel.saveFeaturePageSeen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
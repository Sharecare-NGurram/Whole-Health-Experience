package com.carelon.whe.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.databinding.ActivityDashboardBinding
import com.carelon.whe.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardViewModel>() {

    private val dashboardViewModel by viewModels<DashboardViewModel>()

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun getContentView(): Int = R.layout.activity_dashboard

    override fun getBindingVariable(): Int = BR.vm

    override fun showLightStatusBar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerData()
        initView()
    }

    private fun observerData() {
        dashboardViewModel.newPrescriptionLD.observe(this) { _newPrescriptionMeds ->
            if (!_newPrescriptionMeds.isNullOrEmpty()) {
                setupNotificationBatch(R.id.fragment_meds, "${_newPrescriptionMeds.size}")
            }
        }
    }

    private fun initView() {
        setupNavController()
        dashboardViewModel.getNewPrescriptionMedicine()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateDefaultTab()
    }

    /**
     * changing tab programmatically from notification
    * */
    private fun updateDefaultTab() {
        val selectedTab = intent.getIntExtra("tab_value", -1)
        getDataBinding()?.let {
            if (selectedTab > -1) {
                when (selectedTab) {
                    0 -> it.bottomNavViewDashboard.selectedItemId = R.id.fragment_forYou
                    1 -> it.bottomNavViewDashboard.selectedItemId = R.id.fragment_care
                    2 -> it.bottomNavViewDashboard.selectedItemId = R.id.fragment_meds
                    3 -> it.bottomNavViewDashboard.selectedItemId = R.id.fragment_activity
                }
            }
        }
    }

    /**
     * set the notification count over the menu id mentioned
     * @id -> is the id of menu in bottomnavigationview to show the batch over
     * */
    private fun setupNotificationBatch(id: Int, value: String) {
        getDataBinding()?.let {
            it.bottomNavViewDashboard.showBadge(baseContext, id, value)
        }
    }

    /**
     * remove the notification count over the menu id mentioned
     * @id -> is the id of menu in bottomnavigationview to hide the batch over
     * */
    fun removeNotificationBatch(id: Int) {
        getDataBinding()?.let {
            it.bottomNavViewDashboard.batchRemove(id)
        }
    }

    /*
    * Link our nav_graph to dashboard. This contain following sections:
    * 1. For You
    * 2. Care
    * 3. Meds
    * 4. Activity
    */
    private fun setupNavController() {
        getDataBinding()?.let {
            val navController: NavController =
                Navigation.findNavController(this, R.id.nav_container_dashboard)
            val bottomNavigationView = it.bottomNavViewDashboard
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.fragment_forYou -> {
                        it.bottomNavViewDashboard.batchRemove(R.id.fragment_forYou)
                    }
                    R.id.fragment_care -> {
                        it.bottomNavViewDashboard.batchRemove(R.id.fragment_care)
                    }
                    R.id.fragment_meds -> {
                        it.bottomNavViewDashboard.batchRemove(R.id.fragment_meds)
                    }
                    R.id.fragment_activity -> {
                        it.bottomNavViewDashboard.batchRemove(R.id.fragment_activity)
                    }
                }
            }
            setupWithNavController(bottomNavigationView, navController)
            updateDefaultTab()
        }
    }

}
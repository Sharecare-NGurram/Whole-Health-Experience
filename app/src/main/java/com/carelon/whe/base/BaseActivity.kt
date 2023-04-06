package com.carelon.whe.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.carelon.whe.R
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.domain.model.ErrorItem
import com.carelon.whe.ui.app_feature.AppFeaturesActivity
import com.carelon.whe.ui.dashboard.DashboardActivity
import com.carelon.whe.ui.dialog.ErrorDialogFragment
import com.carelon.whe.ui.early_access.EarlyAccessActivity
import com.carelon.whe.ui.privacy_policy.PrivacyPolicyActivity
import com.carelon.whe.ui.signup_option.SignupOptionActivity
import com.carelon.whe.util.*
import com.carelon.whe.util.launchAndFinish

/**
 * Base activity class for every activity in this application
 */
abstract class BaseActivity<out T : ViewDataBinding, out V : BaseViewModel> :
    AppCompatActivity(){

    private var mViewDataBinding: T? = null

    private var mViewModel: V? = null

    /**
     * Assign layout file for the corresponding activity
     */
    abstract fun getContentView(): Int

    /**
     * Assign binding variable for the corresponding activity, that helps to achieve data binding
     */
    abstract fun getBindingVariable(): Int

    /**
     * This method will assign view model for each activity
     */
    abstract fun getViewModel(): V?

    /**
     * Abstract function to decide whether to show light status bar or not
     */
    abstract fun showLightStatusBar(): Boolean

    /**
     * This method will perform view data binding operation with the help of @getContentView()
     * After initializing the view data binding assign the lifecycle owner and binding variable
     * to created view data binding.
     * Finally executing any pending bindings
     */
    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getContentView())
        mViewDataBinding?.lifecycleOwner = this
        mViewModel = getViewModel()
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding?.executePendingBindings()
    }

    /**
     * This are the common startup function needed before start of any activity.
     */
    private fun initializeApp() {
        stretchToFullScreen()
    }

    /**
     * This method will return instance of ViewDataBinding for the corresponding activity, that
     * will help to find any view related operation inside the activity
     */
    fun getDataBinding() = mViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeApp()
        setLightStatusBar(showLightStatusBar())
        performDataBinding()
    }

    /**
     * This method check given dynamic permission which is granted already or not
     */
    fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Function to show the common error popup with retry option
     */
    fun showErrorDialog(errorItem: ErrorItem,retryAction: (() -> Unit)? = null) {
        val errorDialog = ErrorDialogFragment.newInstance(errorItem)
        errorDialog.onRetryListener = object : ErrorDialogFragment.OnRetryListener {
            override fun onRetryClicked() {
                retryAction?.invoke()
            }
        }
        errorDialog.safeShow(supportFragmentManager,errorItem.hashCode().toString())
    }

    /**
     * Show common alert
     * @positiveButton is to set text for the button in the alert
     * @isCancelable is to set the alert can auto cancel
     */
    fun showInfoAlert(
        message: String,
        positiveButton: String = "OKAY",
        isCancelable: Boolean = false
    ) {
        val builder = AlertDialog.Builder(this@BaseActivity)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { dialogInterface, i ->
            dialogInterface.cancel()
        }
        builder.setCancelable(isCancelable)
        builder.create().show()
    }

    /**
     * Show permission denied alert
     * @positiveButton is to set text for the button in the alert
     * @isCancelable is to set the alert can auto cancel
     */
    fun showPermissionDeniedAlert(title: String, message: String, positiveButton: String) {
        val builder = AlertDialog.Builder(this@BaseActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { dialogInterface, i ->
            dialogInterface.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.cancel()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    /**
     * navigate to next page based on page routes value which will decide on which page we have to navigate
     */
    fun navigateToNextPage(pageRoutes: PageRoutes) {
        when (pageRoutes) {
            is PageRoutes.SignupOption -> {
                launchAndFinish<SignupOptionActivity>()
            }
            is PageRoutes.AppFeatures -> {
                launchAndFinish<AppFeaturesActivity>()
            }
            is PageRoutes.EarlyAccess -> {
                launchAndFinish<EarlyAccessActivity> {
                    putExtra(
                        BundleKeyConstants.EXTRA_NEED_CONSENT,
                        pageRoutes.consent
                    )
                }
            }
            is PageRoutes.PrivacyPolicy -> {
                launchAndFinish<PrivacyPolicyActivity> {
                    putExtra(
                        BundleKeyConstants.EXTRA_NEED_CONSENT,
                        pageRoutes.consent
                    )
                }
            }
            else -> {
                launchAndFinish<DashboardActivity>()
            }
        }
    }

    /**
     * Function to prevent system font-size changing effects
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewDataBinding = null
    }
}
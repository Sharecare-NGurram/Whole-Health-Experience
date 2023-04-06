package com.carelon.whe.ui.privacy_policy

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.BR
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityPrivacyPolicyBinding
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.ui.dashboard.DashboardActivity
import com.carelon.whe.util.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity to show privacy policy consent to the user
 */
@AndroidEntryPoint
class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding, PrivacyPolicyViewModel>() {

    @Inject
    lateinit var gson: Gson

    private val privacyPolicyViewModel: PrivacyPolicyViewModel by viewModels()

    override fun getContentView(): Int = R.layout.activity_privacy_policy

    override fun getBindingVariable(): Int = BR.privacyPolicyViewModel

    override fun getViewModel(): PrivacyPolicyViewModel = privacyPolicyViewModel

    override fun showLightStatusBar(): Boolean = true

    private val consent: UserNeedsToConsentQuery.NeedConsent? by lazy {
        gson.fromJson(
            intent.getStringExtra(BundleKeyConstants.EXTRA_NEED_CONSENT),
            UserNeedsToConsentQuery.NeedConsent::class.java
        )
    }

    private var binding: ActivityPrivacyPolicyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        subscribeObservers()
        setUpWebViewClient()
        initWebView()
        initOnClick()
    }

    private fun subscribeObservers() {
        privacyPolicyViewModel.isSuccess.observe(this) {
            launchAndFinish<DashboardActivity> ()
        }

        privacyPolicyViewModel.errorViewLiveData.observe(this) { errorItem ->
            showErrorDialog(errorItem) {
                addConsent()
            }
        }
    }

    /**
     * Webview client to handle the page loads
     */
    private fun setUpWebViewClient() {
        binding?.webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.run {
                    progressBar.gone()
                    privacyLayout.show()
                }
            }
        }
    }

    /**
     * Webview to display html content
     */
    private fun initWebView() {
        safeLet(binding, consent?.content) { _binding, htmlContent ->
            _binding.webView.loadDataWithBaseURL(
                "",
                htmlContent,
                Constants.HTML_MIME_TYPE,
                Constants.HTML_ENCODING,
                null
            )
        }
    }

    private fun initOnClick() {
        binding?.btnAgree?.setSafeOnClickListener {
            addConsent()
        }
    }

    private fun addConsent() {
        consent?.let {
            privacyPolicyViewModel.addConsent(
                it, mutationInput_submitConsentV2_input_consents_items_type.PRIVACY
            )
        }
    }

    override fun onDestroy() {
        privacyPolicyViewModel.clearConsents()
        super.onDestroy()
        binding = null
    }

}
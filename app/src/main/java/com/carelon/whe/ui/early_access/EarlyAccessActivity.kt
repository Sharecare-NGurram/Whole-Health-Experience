package com.carelon.whe.ui.early_access

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.databinding.ActivityEarlyAccessBinding
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.util.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity to show early access consent to the user
 */
@AndroidEntryPoint
class EarlyAccessActivity : BaseActivity<ActivityEarlyAccessBinding, EarlyAccessViewModel>() {

    @Inject
    lateinit var gson: Gson

    private val earlyAccessViewModel by viewModels<EarlyAccessViewModel>()

    override fun getContentView(): Int = R.layout.activity_early_access

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): EarlyAccessViewModel = earlyAccessViewModel

    override fun showLightStatusBar(): Boolean = true

    private val consent: UserNeedsToConsentQuery.NeedConsent? by lazy {
        gson.fromJson(
            intent.getStringExtra(BundleKeyConstants.EXTRA_NEED_CONSENT),
            UserNeedsToConsentQuery.NeedConsent::class.java
        )
    }

    private var binding: ActivityEarlyAccessBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding()
        observeData()
        initOnClick()
        setUpWebViewClient()
        initWebView()
    }

    /**
     * Webview to display html content
     */
    private fun initWebView() {
        safeLet(binding, consent?.content) { _binding, htmlContent ->
            _binding.webViewEarlyAccess.loadDataWithBaseURL(
                "",
                htmlContent,
                Constants.HTML_MIME_TYPE,
                Constants.HTML_ENCODING,
                null
            )
        }

    }

    /**
     * Webview client to handle the page loads
     */
    private fun setUpWebViewClient() {
        getDataBinding()?.webViewEarlyAccess?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.run {
                    webViewEarlyAccess.show()
                    btnConsent.show()
                    progress.gone()
                }
            }
        }
    }

    private fun observeData() {
        earlyAccessViewModel.currentRouteLD.observe(this) {
            navigateToNextPage(it)
        }

        earlyAccessViewModel.errorViewLiveData.observe(this) { errorItem ->
            showErrorDialog(errorItem) {
                addConsent()
            }
        }
    }

    private fun initOnClick() {
        binding?.btnConsent?.setSafeOnClickListener {
            addConsent()
        }
    }

    private fun addConsent() {
        consent?.let {
            earlyAccessViewModel.addConsent(it,
                mutationInput_submitConsentV2_input_consents_items_type.EARLY_ACCESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
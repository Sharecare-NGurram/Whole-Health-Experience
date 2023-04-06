package com.carelon.whe.ui.login

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.DemoCredentials
import com.carelon.whe.databinding.ActivityLoginBinding
import com.carelon.whe.ui.signup_option.SignupOptionActivity
import com.carelon.whe.util.*
import com.carelon.whe.util.launchAndFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>() {

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun getViewModel(): LoginViewModel = loginViewModel

    override fun getContentView(): Int = R.layout.activity_login

    override fun getBindingVariable(): Int = BR.vm

    override fun showLightStatusBar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        initView()
        loginViewModel.getConfigData()
    }

    private fun initView() {
        getDataBinding()?.imgClose?.setSafeOnClickListener {
            navigateToPreviousPage()
        }

        getDataBinding()?.btnLogin?.setSafeOnClickListener {
            validateSignInForm()
        }
        setUpWebView()
    }

    private fun setUpWebView() {
        getDataBinding()?.webView?.settings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
        getDataBinding()?.webView?.isClickable = true
        getDataBinding()?.webView?.webChromeClient = WebChromeClient()
        setUpWebViewClient()
    }

    private fun setUpWebViewClient() {
        getDataBinding()?.webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                if (loginViewModel.validateLogin(request?.url)){
                    if (loginViewModel.isAppFeaturesShown()){
                        loginViewModel.getUserNeedToConsent()
                    }else{
                        loginViewModel.getCurrentRoute()
                    }
                    return true
                }
                view?.loadUrl(request?.url.toString().replace("localhost", "10.0.2.2"))
                return false
            }
        }
    }

    private fun observeData() {
        loginViewModel.anthemConfigData.observe(this) {

        }
        loginViewModel.currentRouteLD.observe(this){
            navigateToNextPage(it)
        }
    }

    private fun navigateToPreviousPage() {
        launchAndFinish<SignupOptionActivity> {

        }
    }

    private fun validateSignInForm() {
        hideKeyBoard()
        if(getDataBinding()?.editEmail?.text.toString().equals(DemoCredentials.USERNAME) &&
            getDataBinding()?.editPassword?.text.toString().equals(DemoCredentials.PASSWORD)){
            loginViewModel.getCurrentRoute()
        }else if(getDataBinding()?.editEmail?.text.toString().isBlank()){
            showFailureToast(getString(R.string.str_email_error_msg))
        }else if(getDataBinding()?.editPassword?.text.toString().isBlank()){
            showFailureToast(getString(R.string.str_password_error_msg))
        }else if(!getDataBinding()?.editEmail?.text.toString().equals(DemoCredentials.USERNAME) ||
            !getDataBinding()?.editPassword?.text.toString().equals(DemoCredentials.PASSWORD)){
            showFailureToast(getString(R.string.str_invalid_credential))
        }
    }

}
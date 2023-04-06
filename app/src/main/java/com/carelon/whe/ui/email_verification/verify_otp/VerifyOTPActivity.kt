package com.carelon.whe.ui.email_verification.verify_otp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.ActivityVerifyOtpBinding
import com.carelon.whe.ui.email_verification.email_verified.EmailVerifiedActivity
import com.carelon.whe.util.launchAndFinish
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.widget.otp_view.OTPListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * Screen to validate otp for the email verification
 */
@AndroidEntryPoint
class VerifyOTPActivity : BaseActivity<ActivityVerifyOtpBinding, VerifyOTPViewModel>() {

    private val verifyOtpViewModel by viewModels<VerifyOTPViewModel>()

    override fun getContentView(): Int  = R.layout.activity_verify_otp

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): VerifyOTPViewModel = verifyOtpViewModel

    override fun showLightStatusBar(): Boolean = true

    // Runnable to enable resend code CTA after 30 seconds
    private val labelRunnable = Runnable {
        getDataBinding()?.txtResendCode?.run {
            isEnabled  = true
            text = getString(R.string.resend_code)
            setTextAppearance(R.style.body_17_semiBold_purple)
        }
    }

    private val labelUpdateHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
        changeButtonStatus(false)
        observeData()
    }

    /**
     * Init button listeners to perform desired operations
     */
    private fun initListeners() {
        getDataBinding()?.run {
            btnSubmit.setSafeOnClickListener {
                otpView.getOtp().let {
                    // This condition is only for the testing purpose, Need to remove when real integration
                    if (it == "333333"){
                        otpView.showError()
                        txtInvalidOtpError.visibility = View.VISIBLE
                    }else{
                        verifyOtpViewModel.updateEmail(it, intent?.extras)
                    }
                }

            }
            imgClose.setSafeOnClickListener { finish() }
            otpView.otpListener = object : OTPListener {
                override fun onInteractionListener() {
                    changeButtonStatus(false)
                    txtInvalidOtpError.visibility = View.GONE
                }

                override fun onOTPComplete(otp: String) {
                    changeButtonStatus(true)
                }

                override fun submitOTP(otp: String) {
                    if (otp == "333333"){
                        otpView.showError()
                        txtInvalidOtpError.visibility = View.VISIBLE
                    }else{
                        verifyOtpViewModel.updateEmail(otp, intent?.extras)
                    }
                }
            }
            txtResendCode.setSafeOnClickListener {
                intent?.getStringExtra(BundleKeyConstants.EXTRA_EMAIl)?.let {
                    verifyOtpViewModel.validateEmail(it)
                    txtResendCode.apply {
                        isEnabled  = false
                        text = context.getString(R.string.code_resent)
                        setTextAppearance(R.style.body_17_regular_medium_gray)
                    }
                    labelUpdateHandler.postDelayed(labelRunnable, 30000)
                }
            }
            otpView.setFocus()
        }
    }

    /**
     * Observing API responses
     */
    private fun observeData() {
        verifyOtpViewModel.updateEmailResponseLD.observe(this) {
            setResult(RESULT_OK)
            launchAndFinish<EmailVerifiedActivity> {  }
        }
        verifyOtpViewModel.updateEmailResponseError.observe(this) {
            getDataBinding()?.apply {
                otpView.showError()
                txtInvalidOtpError.visibility = View.VISIBLE
            }
        }
        verifyOtpViewModel.emailValidationResponseLD.observe(this) {
            // If need, should show some messages
        }
    }

    /**
     * change button status based on if a valid email has been entered or not
     * */
    private fun changeButtonStatus(isValid: Boolean) {
        getDataBinding()?.run {
            if (isValid){
                btnSubmit.isEnabled = true
                btnSubmit.backgroundTintList =
                    ContextCompat.getColorStateList(this@VerifyOTPActivity, R.color.button_purple)
                btnSubmit.setTextColor(getColor(R.color.white))
            }else{
                btnSubmit.isEnabled = false
                btnSubmit.backgroundTintList =
                    ContextCompat.getColorStateList(this@VerifyOTPActivity, R.color.light_gray_add_medication)
                btnSubmit.setTextColor(getColor(R.color.medium_gray))
            }
        }
    }
}
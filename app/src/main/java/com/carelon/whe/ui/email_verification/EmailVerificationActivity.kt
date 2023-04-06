package com.carelon.whe.ui.email_verification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseActivity
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.ActivityEmailVerificationBinding
import com.carelon.whe.ui.email_verification.verify_otp.VerifyOTPActivity
import com.carelon.whe.util.isEmailValid
import com.carelon.whe.util.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * this page is added for email verification for the user once login successful
 * */
@AndroidEntryPoint
class EmailVerificationActivity :
    BaseActivity<ActivityEmailVerificationBinding, EmailVerifyViewModel>() {

    private val emailVerifyViewModel by viewModels<EmailVerifyViewModel>()

    override fun getContentView(): Int = R.layout.activity_email_verification

    override fun getBindingVariable(): Int = BR.vm

    override fun getViewModel(): EmailVerifyViewModel = emailVerifyViewModel

    override fun showLightStatusBar(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        initOnListener()
    }

    /**
     * Observe data to update the UI
     * */
    private fun observeData() {
        emailVerifyViewModel.emailValidationResponseLD.observe(this) {
            it?.validateEmail?.let { validateEmail ->
                if (validateEmail.token.isNotEmpty()) {
                    val intent = Intent(this, VerifyOTPActivity::class.java)
                    intent.apply {
                        putExtra(
                            BundleKeyConstants.EXTRA_EMAIl,
                            getDataBinding()?.editEmail?.text.toString()
                        )
                        putExtra(
                            BundleKeyConstants.EXTRA_TOKEN,
                            validateEmail.token
                        )
                        putExtra(
                            BundleKeyConstants.EXTRA_DEVICE_ID,
                            validateEmail.pingDeviceId
                        )
                        putExtra(
                            BundleKeyConstants.EXTRA_USER_ID,
                            validateEmail.pingUserId
                        )
                        putExtra(
                            BundleKeyConstants.EXTRA_RISK_ID,
                            validateEmail.pingRiskId
                        )
                    }
                    otpActivityResultLauncher.launch(intent)
                }
            }
        }
    }

    /**
     * set listeners for UI
     * */
    private fun initOnListener() {
        getDataBinding()?.run {
            imgClose.setSafeOnClickListener {
                finish()
            }
            editEmail.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        changeButtonStatus(editEmail.isEmailValid())
                    }
                }
            )
            editEmail.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    editEmail.background = getDrawable(R.drawable.drawable_edit_bg_purple)
                } else {
                    editEmail.background = getDrawable(R.drawable.drawable_edit_bg_gray)
                }
            }
            btnSubmit.setSafeOnClickListener {
                emailVerifyViewModel.validateEmail(editEmail.text.toString())
            }
            editEmail.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    emailVerifyViewModel.validateEmail(editEmail.text.toString())
                    true
                } else
                    false
            }
            editEmail.requestFocus()
        }
    }

    /**
     * change button status based on if a valid email has been entered or not
     * */
    private fun changeButtonStatus(isValid: Boolean) {
        getDataBinding()?.run {
            if (isValid) {
                btnSubmit.isEnabled = true
                btnSubmit.backgroundTintList =
                    ContextCompat.getColorStateList(
                        this@EmailVerificationActivity,
                        R.color.button_purple
                    )
                btnSubmit.setTextColor(getColor(R.color.white))
                btnSubmit.setText(R.string.str_send_email_verification)
            } else {
                btnSubmit.isEnabled = false
                btnSubmit.backgroundTintList =
                    ContextCompat.getColorStateList(
                        this@EmailVerificationActivity,
                        R.color.light_gray_add_medication
                    )
                btnSubmit.setTextColor(getColor(R.color.medium_gray))
                btnSubmit.setText(R.string.str_enter_email_address)
            }
        }
    }

    private val otpActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }
}
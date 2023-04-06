package com.carelon.whe.ui.email_verification.verify_otp

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.UpdateEmailMutation
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.domain.usecase.EmailVerificationUseCase
import com.carelon.whe.domain.usecase.UserProfileUseCase
import com.carelon.whe.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyOTPViewModel @Inject constructor(
    val useCase: EmailVerificationUseCase,
    val userProfileUseCase: UserProfileUseCase,
): BaseViewModel() {

    val updateEmailResponseLD = MutableLiveData<UpdateEmailMutation.Data>()
    val updateEmailResponseError = MutableLiveData<Boolean>()

    val emailValidationResponseLD = MutableLiveData<ValidateEmailMutation.Data>()

    /**
     * Resend OTP for the email
     */
    fun validateEmail(email: String) {
        viewModelScope.launch {
            useCase.verifyEmail(email).collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        setIsLoading(false)
                        emailValidationResponseLD.value = it.data
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        errorViewLiveData.value = it.error
                    }
                }
            }
        }
    }

    /**
     * Update email with OTP
     */
    fun updateEmail(otp: String, extras: Bundle?) {
        extras?.let {
            viewModelScope.launch {
                useCase.updateEmail(otp,
                    extras.getString(BundleKeyConstants.EXTRA_TOKEN) ?: "",
                    extras.getString(BundleKeyConstants.EXTRA_DEVICE_ID) ?: "",
                    extras.getString(BundleKeyConstants.EXTRA_USER_ID) ?: "",
                    extras.getString(BundleKeyConstants.EXTRA_RISK_ID) ?: "").collect {
                        when (it) {
                            is Resource.Loading -> setIsLoading(true)
                            is Resource.Success -> {
                                setIsLoading(false)
                                userProfileUseCase.setEmailVerified()
                                updateEmailResponseLD.value = it.data
                            }
                            is Resource.Error -> {
                                setIsLoading(false)
                                errorViewLiveData.value = it.error
                                updateEmailResponseError.value = true
                            }
                        }
                }
            }
        }
    }

}
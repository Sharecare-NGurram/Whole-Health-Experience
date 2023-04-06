package com.carelon.whe.ui.email_verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.usecase.EmailVerificationUseCase
import com.carelon.whe.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerifyViewModel @Inject constructor(val useCase: EmailVerificationUseCase) : BaseViewModel() {

    val emailValidationResponseLD = MutableLiveData<ValidateEmailMutation.Data?>()

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
}
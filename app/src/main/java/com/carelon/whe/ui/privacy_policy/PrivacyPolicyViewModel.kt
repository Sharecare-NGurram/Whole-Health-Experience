package com.carelon.whe.ui.privacy_policy

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val consentUseCase: ConsentUseCase
) : BaseViewModel() {


    val isSuccess =  SingleLiveEvent<String>()

    // Add consent to yes
    fun addConsent(
        consent: UserNeedsToConsentQuery.NeedConsent,
        type: mutationInput_submitConsentV2_input_consents_items_type
    ) {
        viewModelScope.launch {
            consentUseCase.saveConsents(consent,type).collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        setIsLoading(false)
                        consentUseCase.setPrivacyPolicyPageShown()
                        isSuccess.value = it.data?.addConsents?.message ?: ""
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    fun clearConsents() {
        consentUseCase.clearConsents()
    }

}
package com.carelon.whe.ui.early_access

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.util.PageRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model to handle early access consent save and redirection
 */
@HiltViewModel
class EarlyAccessViewModel @Inject constructor(val useCase: ConsentUseCase): BaseViewModel() {

    val currentRouteLD = MutableLiveData<PageRoutes>()

    // Add consent to yes
    fun addConsent(
        consent: UserNeedsToConsentQuery.NeedConsent,
        type: mutationInput_submitConsentV2_input_consents_items_type
    ) {
        viewModelScope.launch {
            useCase.saveConsents(consent,type).collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        val response = it.data
                        Log.d("Response is", "addConsent: $response")
                        useCase.setEarlyAccessPageShown()
                        getUserNeedToConsent()
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    // Check user has any pending consents
    private fun getUserNeedToConsent() {
        viewModelScope.launch {
            useCase.getUserNeedsToConsent().collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        setIsLoading(false)
                        it.data?.let { consentList ->
                            if (!consentList.needConsent.isNullOrEmpty()) {
                                currentRouteLD.value = useCase.getCurrentRoute(consentList.needConsent)
                            } else {
                                currentRouteLD.value = PageRoutes.Dashboard
                            }
                        }
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

}
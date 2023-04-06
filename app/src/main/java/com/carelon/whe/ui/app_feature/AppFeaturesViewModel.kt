package com.carelon.whe.ui.app_feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.AppFeatureModel
import com.carelon.whe.domain.usecase.AppFeaturesUseCase
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.PageRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppFeaturesViewModel @Inject constructor(
    private val featureUseCase : AppFeaturesUseCase,
    private val consentUseCase: ConsentUseCase
) : BaseViewModel() {

    val appFeaturesLD = MutableLiveData<MutableList<AppFeatureModel>>()
    val currentRouteLD = MutableLiveData<PageRoutes>()

    /**
     * This will return the app feature points to highlight after user login
     * @description - feature point
     * @backgroundResId - background image for that point
     * */
    fun getAppFeatures(){
        viewModelScope.launch {
            val featureList = featureUseCase.getAppFeatures()
            featureList.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        appFeaturesLD.value = it.data as MutableList<AppFeatureModel>
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }

    /**
     * this will save in local device that user has seen app features
    * */
    fun saveFeaturePageSeen(){
        viewModelScope.launch {
            val response = featureUseCase.setAppFeatureSeen()
            response.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        getUserNeedToConsent()
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }

    // Check user has any pending consents
    private fun getUserNeedToConsent() {
        viewModelScope.launch {
            consentUseCase.getUserNeedsToConsent().collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        setIsLoading(false)
                        it.data?.let { consentList ->
                            if (!consentList.needConsent.isNullOrEmpty()) {
                                currentRouteLD.value = consentUseCase.getCurrentRoute(consentList.needConsent)
                            }else{
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
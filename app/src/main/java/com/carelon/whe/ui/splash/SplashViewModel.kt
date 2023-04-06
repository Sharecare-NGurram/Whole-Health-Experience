package com.carelon.whe.ui.splash

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.usecase.AppFeaturesUseCase
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.domain.usecase.LoginUseCase
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.PageRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val consentUseCase: ConsentUseCase,
    private val featureUseCase: AppFeaturesUseCase,
    @ApplicationContext var context: Context
): BaseViewModel() {
    val currentRouteLD = MutableLiveData<PageRoutes>()

    // Check user has any pending consents
    fun getUserNeedToConsent() {
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

    /**
     * This will provide the route to which we need current page to go
     * it will return @PageRoute param which will decide on which page we need the app to go
     * */
    fun getCurrentRoute(){
        Handler().postDelayed(
            Runnable {
                if(loginUseCase.getAuthToken().isNullOrEmpty()){
                    currentRouteLD.value=PageRoutes.SignupOption
                }else{
                    if (featureUseCase.isAppFeaturesShown()) {
                        getUserNeedToConsent()
                    } else {
                        currentRouteLD.value = PageRoutes.AppFeatures
                    }
                }
            }, 2500
        )
    }

}
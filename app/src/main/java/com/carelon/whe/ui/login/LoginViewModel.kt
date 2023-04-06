package com.carelon.whe.ui.login

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.WholeHealthQuery
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.constants.DemoCredentials
import com.carelon.whe.domain.usecase.AppFeaturesUseCase
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.domain.usecase.LoginUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.PageRoutes
import com.carelon.whe.util.findParameterValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
    private val consentUseCase: ConsentUseCase,
    val featureUseCase : AppFeaturesUseCase
) : BaseViewModel() {

    val webViewLoginUrl = MutableLiveData<String>()
    private val baseUrl = ObservableField<String>("")
    val anthemConfigData = MutableLiveData<WholeHealthQuery.Anthem_auth_config>()
    val currentRouteLD = MutableLiveData<PageRoutes>()
    var authData : WholeHealthQuery.Anthem_auth_config ?= null

    fun getSavedCredentials(): DemoCredentials {
        return DemoCredentials()
    }

    // Get the configuration data to check login validation
    fun getConfigData() {
        viewModelScope.launch {
            useCase.getAuthInfo().collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        setIsLoading(false)
                        it.data?.let { auth ->
                            authData = auth
                            webViewLoginUrl.value =
                                auth.login_url.toString().replace("localhost", "10.0.2.2")
                            baseUrl.set(auth.auth_tokens?.url_base.toString())
                        }
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        errorViewLiveData.value = it.error
                    }
                }
            }
        }
    }

    private fun Uri.startsWith(value: String): Boolean {
        return toString().startsWith(value, true)
    }

    fun validateLogin(newUrl: Uri?): Boolean {
        val isValidLogin = baseUrl.get()?.let { newUrl?.startsWith(it) } == true
        if (isValidLogin){
            authData?.auth_tokens?.query_param?.access_token?.let { token ->
                newUrl?.findParameterValue(token)?.let { useCase.saveAuthToken(it) }
            }
        }
        return isValidLogin
    }

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
     * it will return @navigationPage param which will decide on which page we need the app to go
    * */
    fun getCurrentRoute(){
        viewModelScope.launch {
            val response = featureUseCase.getCurrentRoute()
            response.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        currentRouteLD.value = it.data
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }

    fun isAppFeaturesShown() = featureUseCase.isAppFeaturesShown()

}
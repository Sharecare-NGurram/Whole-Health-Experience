package com.carelon.whe.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carelon.whe.domain.model.ErrorItem

abstract class BaseViewModel : ViewModel() {
    val errorViewLiveData: MutableLiveData<ErrorItem> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
        private set
    fun setIsLoading(isLoadingNew: Boolean) {
        isLoading.value = isLoadingNew
    }
    var isFailedToFetch = MutableLiveData<Boolean>()
        private set
    fun setIsFailedToFetch(isFailed: Boolean) {
        isFailedToFetch.value = isFailed
    }
}
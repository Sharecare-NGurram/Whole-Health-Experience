package com.carelon.whe.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.ErrorItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ErrorDialogViewModel @Inject constructor(): BaseViewModel() {

    val errorTitle = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()

    fun setErrorData(errorItem: ErrorItem?) {
        errorTitle.value = errorItem?.title ?: ""
        errorMessage.value = errorItem?.message ?: ""
    }

}
package com.carelon.whe.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val medsUseCase: MedicationUseCase,
): BaseViewModel() {

    val newPrescriptionLD = MutableLiveData<List<Medicine>?>()

    /**
     * check if any new meds available and show the design to select new meds to account
     * */
    fun getNewPrescriptionMedicine() {
        viewModelScope.launch {
            val medImports = medsUseCase.getNewPrescriptionAvailable()
            medImports.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        newPrescriptionLD.value = it.data as MutableList<Medicine>
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }

}
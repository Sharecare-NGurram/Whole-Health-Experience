package com.carelon.whe.ui.medication_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.usecase.MedicationDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationDetailsViewModel @Inject constructor(
    val useCase: MedicationDetailUseCase
): BaseViewModel() {

    val medicine = MutableLiveData<Medicine?>()

    fun setMedicineToDisplay(medicineToDisplay: Medicine?) {
        medicine.value = medicineToDisplay
    }

    fun saveMedicineDetails() {
        viewModelScope.launch {
            useCase.updateMedicine(medicine.value)
        }
    }

}
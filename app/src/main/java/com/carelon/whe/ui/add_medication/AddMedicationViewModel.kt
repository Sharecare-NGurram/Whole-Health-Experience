package com.carelon.whe.ui.add_medication

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.data.data_source.UpdateMedicationUseCaseImpl
import com.carelon.whe.domain.model.MedicationData
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.usecase.AddMedicationUseCase
import com.carelon.whe.network.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCaseImpl,
    @ApplicationContext val context: Context
) :
    BaseViewModel() {

    val buttonText = MutableLiveData<String>()
    val medicinesLD = MutableLiveData<List<MedicationData?>>()
    val savedMedicinesLD = MutableLiveData<List<MedicationData>?>()
    val selectedMedicines = mutableListOf<MedicationData>()

    private var isFromMeds = MutableLiveData(false)
    var isNoMedicinesSelected = MutableLiveData(false)

    val existingSelectedMedicines = ArrayList<Medicine?>()

    /**
     * Retrieve medicine to import from cloud
     */
    fun fetchMedicine() {
        viewModelScope.launch {
            val medicines = addMedicationUseCase.getMedicineList()
            medicines.collect {
                when (it) {
                    is Resource.Loading -> {
                        setIsLoading(true)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        setIsLoading(false)
                        it.data?.wholeHealth?.dispensedMedications?.let {res->
                            val list = res.filter{ dt-> dt?.details!=null}
                                .map{dt-> dt?.let {MedicationData.ModelMapper.from(dt)}}
                            medicinesLD.value = list
                        }
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(true)
                    }
                }
            }
        }
    }

    /**
     * Save selected medicine to cloud
    * */
    fun saveMedicine(){
        viewModelScope.launch {
            val medicines = updateMedicationUseCase.saveImportedMedicine(selectedMedicines)
            medicines.collect {
                when (it) {
                    is Resource.Loading -> {
                        setIsLoading(true)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        val response = it.data?.addMedications?.medications
                        savedMedicinesLD.value = selectedMedicines
                        //markedImportMedsCompleted()
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(true)
                    }
                }
            }
        }
    }

    fun updateSelectedItems(data: MedicationData) {
        if (selectedMedicines.contains(data)) {
            selectedMedicines.remove(data)
        } else {
            selectedMedicines.add(data)
        }
        updateSubmitButtonText()
    }

    /**
     * Method to help to update the submit button text based on the selected medicine(s)
     */
    fun updateSubmitButtonText() {
        if (isFromMeds.value == true ){
            isNoMedicinesSelected.value = selectedMedicines.size == 0
        }
        when (selectedMedicines.size) {
            0 -> {
                buttonText.value = if (isFromMeds.value == true)
                    context.getString(R.string.select_at_least_one) else
                    context.getString(R.string.add_medication_submit_button_none)
            }
            1 -> {
                buttonText.value = if (isFromMeds.value == true)
                    String.format(
                        context.getString(R.string.add_medication_submit_button_one_medications_add),
                        selectedMedicines.getOrNull(0)?.proprietaryName
                    ) else
                    String.format(
                        context.getString(R.string.add_medication_submit_button_one_medications),
                        selectedMedicines.getOrNull(0)?.proprietaryName
                    )
            }
            else -> {
                buttonText.value = if (isFromMeds.value == true)
                    String.format(
                        context.getString(R.string.add_medication_submit_button_more_than_one_medications_add),
                        selectedMedicines.size
                    ) else
                    String.format(
                        context.getString(R.string.add_medication_submit_button_more_than_one_medications),
                        selectedMedicines.size
                    )
            }
        }
    }

    fun updateIsFromMeds(selectedMeds: String?) {
        val type = object : TypeToken<List<Medicine>>() {}.type
        existingSelectedMedicines.addAll(Gson().fromJson(selectedMeds, type) as ArrayList<Medicine>)
        isFromMeds.value = true
        isNoMedicinesSelected.value = true
        updateSubmitButtonText()
    }
}
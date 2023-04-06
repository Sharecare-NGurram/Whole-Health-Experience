package com.carelon.whe.ui.dashboard.meds

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.data.data_source.UpdateMedicationUseCaseImpl
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.usecase.GetMedicineRemindersUseCase
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.SingleLiveEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val medsUseCase: MedicationUseCase,
    private val getMedicineRemindersUseCase: GetMedicineRemindersUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCaseImpl,
    @ApplicationContext var context: Context
): BaseViewModel() {

    val savedMedsLD = MutableLiveData<MutableList<Medicine?>?>()
    val savedNewPrescriptionLD = SingleLiveEvent<Int>()
    val newPrescriptionLD = SingleLiveEvent<MutableList<Medicine>?>()
    val selectedNewMedicinesLD = MutableLiveData<MutableList<Medicine>>()
    val medsReminderLD = MutableLiveData<List<MedicineReminder>>()
    val selectedNewMedicines = ArrayList<Medicine>()
    val buttonText = MutableLiveData<String>()
    val prescriptionCountText = MutableLiveData<String>()

    /**
     * get imported meds saved locally in add medication
     * */
    fun getSavedMedication() {
        viewModelScope.launch {
            val medImports = medsUseCase.getSavedMedications()
            medImports.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        savedMedsLD.value = it.data as MutableList<Medicine?>?
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
     * fetch saved medicine reminders from database
     * */
    fun getMedicineReminder() {
        viewModelScope.launch {
            getMedicineRemindersUseCase.getMedicineReminders().collect {
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        medsReminderLD.value = it.data
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
     * update the meds saved locally to reflect it in meds section
     * */
    fun saveSelectedMeds(records: List<Medicine>) {
        viewModelScope.launch {
            val selectedMeds = Gson().toJson(records)
            val medImports = medsUseCase.saveSelectedMeds(selectedMeds)
            medImports.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        getSavedMedication()
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
     * update the new prescription added to meds list of user
     * */
    fun saveNewPrescriptionMeds(records: MutableList<Medicine>,savedMedicine: MutableList<Medicine>) {
        viewModelScope.launch {
            val addedRecord = records.size
            records.addAll(savedMedicine)
            val selectedMeds = Gson().toJson(records)
            medsUseCase.saveSelectedMeds(selectedMeds).collect{res->
                when(res){
                    is Resource.Loading -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        selectedNewMedicines.clear()
                        savedNewPrescriptionLD.value = addedRecord
                        getSavedMedication()
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
                        selectedNewMedicines.clear()
                        buttonText.value = context.getString(R.string.str_select_meds)
                        it.data?.let {record->
                            newPrescriptionLD.value = record as MutableList<Medicine>
                            if(record.size==1){
                                updateSelectedItems(record[0])
                                prescriptionCountText.value = String.format(
                                    context.getString(R.string.str_we_found_medication),
                                    record.size
                                )
                            }else{
                                prescriptionCountText.value = String.format(
                                    context.getString(R.string.str_we_found_medications),
                                    record.size
                                )
                            }
                        }
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
     * updated the selected meds to save it in server
    * */
    fun updateSelectedItems(data: Medicine) {
        if (selectedNewMedicines.contains(data)) {
            selectedNewMedicines.remove(data)
        } else {
            selectedNewMedicines.add(data)
        }
        selectedNewMedicinesLD.value = selectedNewMedicines
        updateSubmitButtonText()
    }

    /**
     * update text value based on selected meds
    * */
    private fun updateSubmitButtonText() {
        when (selectedNewMedicines.size) {
            0 -> {
                buttonText.value = context.getString(R.string.str_select_meds)
            }
            1 -> {
                buttonText.value = context.getString(R.string.str_add_med)
            }
            else -> {
                buttonText.value = String.format(
                        context.getString(R.string.str_add_meds_count),
                        selectedNewMedicines.size
                    )
            }
        }
    }

    /**
     * Mark medicine import step is done
     * */
    fun markedImportMedsCompleted(isImported:Boolean=true){
        viewModelScope.launch {
            val medicines = updateMedicationUseCase.trackMedicineCompleted(isImported)
            medicines.collect {
                when (it) {
                    is Resource.Loading -> {
                        setIsLoading(true)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {

                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(false)
                    }
                }
            }
        }
    }

    fun markedNewPrescriptionRead(){
        medsUseCase.markedNewPrescriptionRead()
    }

}
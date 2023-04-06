package com.carelon.whe.ui.pick_focus_area

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.data.data_source.UpdateMedicationUseCaseImpl
import com.carelon.whe.domain.model.FocusAreaItem
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.usecase.GetFocusAreasUseCase
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.domain.usecase.TrackYourStepsUseCase
import com.carelon.whe.domain.usecase.UpdateMedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.SingleLiveEvent
import com.carelon.whe.util.TackMedicationStatus
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickFocusAreaViewModel @Inject constructor(
    private val getFocusAreasUseCase: GetFocusAreasUseCase,
    private val medsUseCase: MedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val trackYourStepsUseCase: TrackYourStepsUseCase,
) : BaseViewModel() {

    val getFocusAreas = MutableLiveData<List<FocusAreaItem>>()
    val medsImported = SingleLiveEvent<Boolean>()
    var pickupFocusArea = mutableListOf<FocusAreaItem>()
    val trackStepActivityLD = SingleLiveEvent<Boolean>()

    fun getFocusAreas() {
        viewModelScope.launch {
            getFocusAreasUseCase.getFocusAreas().collect {
                when (it) {
                    is Resource.Loading -> setIsLoading(true)
                    is Resource.Success -> {
                        pickupFocusArea = it.data
                        getFocusAreas.value = pickupFocusArea
                    }
                    is Resource.Error -> setIsLoading(true)
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
                        val list = it.data as MutableList<Medicine?>? ?: listOf<Medicine>()
                        if(list.isNotEmpty())
                            medsImported.value=true
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
     * Update the trackStepActivity to the api
     */
    fun trackStepActivity() {
        viewModelScope.launch {
            val response = trackYourStepsUseCase.trackStepActivity()
            response.collect{
                when(it){
                    is Resource.Loading -> {
                        setIsLoading(true)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        setIsLoading(false)
                        trackStepActivityLD.value = it.data?.trackStepActivity?.success
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                    }
                }
            }
        }
    }

    fun getTrackStepActivity() = trackYourStepsUseCase.getTrackStepActivity()

    fun updateTrackStepActivity(status: Int) {
        trackYourStepsUseCase.updateTrackStepActivity(status)
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
                        medsImported.value=true
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
     * Is medicine import step completed successfully or not
     * */
    fun trackMedicineStatus(){
        viewModelScope.launch {
            val medicines = updateMedicationUseCase.trackMedicineStatus()
            medicines.collect {
                when (it) {
                    is Resource.Loading -> {
                        setIsLoading(true)
                        setIsFailedToFetch(false)
                    }
                    is Resource.Success -> {
                        medsImported.value = (it.data==TackMedicationStatus.MEDICATION_IMPORT_OK.value
                                ||it.data==TackMedicationStatus.MEDICATION_IMPORT_NOT_NOW.value)
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(true)
                    }
                }
            }
        }
    }

}
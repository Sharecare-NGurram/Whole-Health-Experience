package com.carelon.whe.ui.week_plan

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.carelon.whe.R
import androidx.lifecycle.viewModelScope
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.domain.usecase.TrackYourStepsUseCase
import com.carelon.whe.util.SingleLiveEvent
import com.carelon.whe.widget.span.Span
import com.carelon.whe.widget.span.span
import com.carelon.whe.domain.usecase.UpdateMedicationUseCase
import com.carelon.whe.network.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyPlanViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val preferenceHelper: AppPreferenceHelper,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val trackYourStepsUseCase: TrackYourStepsUseCase,
    private val medsUseCase: MedicationUseCase,
) : BaseViewModel() {

    val trackMedsStatus = SingleLiveEvent<Int>()
    val trackYourStepsStatus = SingleLiveEvent<Int>()

    fun getDailyCheckInTime(): String = preferenceHelper.dailyCheckInTime

    fun getTrackStepStatus(): Int = preferenceHelper.savedTrackStepActivity

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
                        trackMedsStatus.value = it.data
                    }
                    is Resource.Error -> {
                        setIsLoading(false)
                        setIsFailedToFetch(true)
                    }
                }
            }
        }
    }

    fun trackYourStepStatus() {
        viewModelScope.launch {
            trackYourStepsStatus.value = trackYourStepsUseCase.getTrackStepActivity()
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
                        trackMedicineStatus()
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

}
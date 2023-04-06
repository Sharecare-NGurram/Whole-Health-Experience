package com.carelon.whe.ui.medicine_reminder

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.model.TimePickerItem
import com.carelon.whe.domain.usecase.AddMedicineReminderUseCase
import com.carelon.whe.domain.usecase.DeleteMedicineReminderUseCase
import com.carelon.whe.domain.usecase.UpdateMedicineReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MedicineReminderViewModel @Inject constructor(
    private val addMedicineReminderUseCase: AddMedicineReminderUseCase,
    private val updateMedicineReminderUseCase: UpdateMedicineReminderUseCase,
    private val deleteMedicineReminderUseCase: DeleteMedicineReminderUseCase,
    @ApplicationContext val context: Context
) : BaseViewModel() {


    val buttonText = MutableLiveData<String>()
    val eventFlow = MutableLiveData<UiEvent>()

    fun addMedicineReminder(medicineReminder: MedicineReminder) {
        viewModelScope.launch {
            addMedicineReminderUseCase.addMedicineReminder(medicineReminder)
            eventFlow.value = UiEvent.AddReminder(medicineReminder)
        }
    }

    fun updateMedicineReminder(medicineReminder: MedicineReminder) {
        viewModelScope.launch {
            updateMedicineReminderUseCase.updateMedicineReminder(medicineReminder)
            eventFlow.value = UiEvent.UpdateReminder
        }
    }

    fun deleteMedicineReminder(medicineReminder: MedicineReminder) {
        viewModelScope.launch {
            deleteMedicineReminderUseCase.deleteMedicineReminder(medicineReminder)
            eventFlow.value = UiEvent.DeleteReminder
        }
    }

    /**
     * Get minutes list to populate data in recyclerview
     */
    fun getMinutesList() = (0 until 4).map { TimePickerItem(it * 15) }

    /**
     * Get hours list to populate data in recyclerview
     */
    fun getHoursList() = (1 until 13).map { TimePickerItem(it) }

    /**
     * Get format list to populate data in recyclerview
     */
    fun getFormatList() = arrayListOf(
        TimePickerItem(Calendar.AM),
        TimePickerItem(Calendar.PM)
    )

    /**
     * Get the scroll position based on hour
     */
    fun getHourScrollPosition(hour: Int): Int {
        return getHoursList().indexOfFirst { item ->
            item.value == hour
        }
    }

    /**
     * Get the scroll position based on minutes
     */
    fun getMinutesScrollPosition(minutes: Int): Int {
        return getMinutesList().indexOfFirst { item ->
            item.value == minutes
        }
    }

    /**
     * Get the scroll position based on format
     */
    fun getFormatPosition(format: Int): Int {
        return getFormatList().indexOfFirst { item ->
            item.value == format
        }
    }

    /**
     * Add text to the button
     */
    fun addReminderButtonText(
        hour: Int,
        minutes: Int,
        format: Int,
        ifReminderExists: Boolean,
        editReminder: Boolean
    ) {
        val medicineReminder = MedicineReminder(
            hour = hour,
            minutes = minutes,
            format = format
        ).toString()
        if (ifReminderExists) {
            buttonText.value = context.getString(R.string.button_reminder_exists,medicineReminder)
        } else if (editReminder) {
            buttonText.value = context.getString(R.string.button_change_reminder,medicineReminder)
        } else {
            buttonText.value = context.getString(R.string.button_add_reminder,medicineReminder)
        }

    }


    /**
     * Return the list of medicine reminders with comma separated
     */
    suspend fun getExistingMedicineReminderString(medicineReminderList: ArrayList<MedicineReminder>): String {
        val reminderString = withContext(Dispatchers.IO) {
            val existingMedicineReminderList = ArrayList<String>()
            for (medicineReminder in medicineReminderList) {
                existingMedicineReminderList.add(medicineReminder.toString())
            }
            val reminderString = existingMedicineReminderList.joinToString(", ")
            val builder = StringBuilder(reminderString)
            val lastIndexOf = reminderString.lastIndexOf(",")
            if (lastIndexOf != -1) {
                builder.replace(lastIndexOf, lastIndexOf + 1, " and")
            }
            builder.toString()
        }
        return reminderString
    }

    /**
     * Function returns true if reminder exist and false if not
     */
    suspend fun checkReminderExists(
        hour: Int,
        minutes: Int,
        format: Int,
        medicineReminderList: ArrayList<MedicineReminder>
    ): Boolean {
        return withContext(Dispatchers.IO) {
            var isReminderExists = false
            for (item in medicineReminderList) {
                if (item.hour == hour && item.minutes == minutes && item.format == format) {
                    isReminderExists = true
                    break
                }
            }
            return@withContext isReminderExists
        }
    }


    sealed class UiEvent {
        data class AddReminder(val medicineReminder: MedicineReminder) : UiEvent()
        object UpdateReminder : UiEvent()
        object DeleteReminder : UiEvent()
    }


}
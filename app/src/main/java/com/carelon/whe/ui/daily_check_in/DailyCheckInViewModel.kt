package com.carelon.whe.ui.daily_check_in

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.carelon.whe.R
import com.carelon.whe.base.BaseViewModel
import com.carelon.whe.domain.model.DailyCheckIn
import com.carelon.whe.domain.model.TimePickerItem
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DailyCheckInViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val preferenceHelper: AppPreferenceHelper
) : BaseViewModel() {

    val buttonText = MutableLiveData<String>()

    /**
     * Function returns the day start time ie 4:45 AM
     */
    private val dayStartTime: Calendar by lazy {
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR, 4)
        startTime.set(Calendar.MINUTE, 45)
        startTime.set(Calendar.AM_PM, Calendar.AM)
        return@lazy startTime
    }

    /**
     * Function returns the night start time ie 5:00 PM
     */
    private val nightStartTime: Calendar by lazy {
        val endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR, 5)
        endTime.set(Calendar.MINUTE, 0)
        endTime.set(Calendar.AM_PM, Calendar.PM)
        return@lazy endTime
    }

    private val calendarNow: Calendar by lazy {
        Calendar.getInstance()
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
    fun setDailyCheckInButtonText(
        hour: Int,
        minutes: Int,
        format: Int,
    ) {
        val dailyCheckInTime = DailyCheckIn(
            hour = hour,
            minutes = minutes,
            format = format
        ).toString()
        buttonText.value = context.getString(R.string.button_choose_check_in_time, dailyCheckInTime)

    }

    /**
     * Function to check whether the selected time is past day start time or not
     */
    fun checkIfTimeIsInDayOrNot(mHour: Int, mMinutes: Int, mFormat: Int): Boolean {
        val hour = if (mHour == 12) {
            0
        } else {
            mHour
        }
        calendarNow.set(Calendar.HOUR, hour)
        calendarNow.set(Calendar.MINUTE, mMinutes)
        calendarNow.set(Calendar.AM_PM, mFormat)
        return calendarNow.after(dayStartTime) && calendarNow.before(nightStartTime)
    }

    fun saveDailyCheckInTime(dailyCheckInTime: String) {
        preferenceHelper.dailyCheckInTime = dailyCheckInTime
    }

}
package com.carelon.whe.ui.medicine_reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carelon.whe.domain.usecase.GetMedicineRemindersUseCase
import com.carelon.whe.ui.medicine_reminder.scheduler.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Re-schedule the alarms whenever the phone restarts
 */
@AndroidEntryPoint
class RescheduleMedicineReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getMedicineRemindersUseCase: GetMedicineRemindersUseCase

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            val list = getMedicineRemindersUseCase.getMedicineReminderList()
            for(medicineReminder in list) {
                alarmScheduler.scheduleAlarm(medicineReminder)
            }
        }
    }
}
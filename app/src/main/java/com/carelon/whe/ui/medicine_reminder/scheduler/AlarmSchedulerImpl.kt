package com.carelon.whe.ui.medicine_reminder.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.ui.medicine_reminder.receiver.AlarmReceiver
import com.carelon.whe.util.canScheduleAlarms
import java.util.*

/**
 * Class for scheduling and cancelling an alarm
 */
class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleAlarm(medicineReminder: MedicineReminder) {
        /**
         * Check whether we can schedule exact alarms or not
         */
        if (alarmManager.canScheduleAlarms()) {
            /**
             * 0 represents selected hour is 12
             */
            val hour = if (medicineReminder.hour == 12) {
                0
            } else {
                medicineReminder.hour
            }
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, medicineReminder.minutes)
            calendar.set(Calendar.AM_PM, medicineReminder.format)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            var timeInMillis = calendar.timeInMillis
            if (System.currentTimeMillis() >= timeInMillis) {
                timeInMillis = timeInMillis.plus(86400000) // if the selected time is less than current time then set the alarm for next day.
            }
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(BundleKeyConstants.EXTRA_MEDICINE_REMINDER_ITEM, medicineReminder)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                medicineReminder.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    override fun cancelAlarm(medicineReminder: MedicineReminder) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                medicineReminder.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
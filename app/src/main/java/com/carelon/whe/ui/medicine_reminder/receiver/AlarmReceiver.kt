package com.carelon.whe.ui.medicine_reminder.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.carelon.whe.R
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.constants.Constants
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.ui.medicine_reminder.scheduler.AlarmScheduler
import com.carelon.whe.util.parcelable
import com.carelon.whe.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Class for showing the medicine reminder notification
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private val MEDICINE_REMINDER_CHANNEL_ID = "medicine_reminder_channel"
    private val MEDICINE_REMINDER_CHANNEL_NAME = "Medicine Reminder Channel"
    private val MEDICINE_REMINDER_CHANNEL_DESCRIPTION = "Medicine Reminder Channel Description"

    override fun onReceive(context: Context?, intent: Intent?) {
        val medicineReminder =
            intent?.extras?.parcelable(BundleKeyConstants.EXTRA_MEDICINE_REMINDER_ITEM) as MedicineReminder?
        safeLet(context, medicineReminder) { _context, _medicineReminder ->
            scheduleAlarmForNextDay(_medicineReminder)
            if (NotificationManagerCompat.from(_context).areNotificationsEnabled()) {
                val notificationManager =
                    _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val pendingIntent =
                    PendingIntent.getActivity(_context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(
                    MEDICINE_REMINDER_CHANNEL_ID,
                    MEDICINE_REMINDER_CHANNEL_NAME,
                    importance
                )
                mChannel.description = MEDICINE_REMINDER_CHANNEL_DESCRIPTION
                notificationManager.createNotificationChannel(mChannel)
                val notification = Notification.Builder(_context, MEDICINE_REMINDER_CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(Constants.MEDICINE_REMINDER_TITLE)
                    .setContentText(Constants.MEDICINE_REMINDER_MESSAGE)
                    .setSmallIcon(R.drawable.ic_app_notification)
                    .setColor(ContextCompat.getColor(_context,R.color.purple))
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .build()
                notificationManager.notify(System.currentTimeMillis().hashCode(), notification)
            }
        }
    }

    private fun scheduleAlarmForNextDay(medicineReminder: MedicineReminder) {
        alarmScheduler.scheduleAlarm(medicineReminder)
    }

}
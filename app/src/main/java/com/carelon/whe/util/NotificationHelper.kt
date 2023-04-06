package com.carelon.whe.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.carelon.whe.R
import com.carelon.whe.ui.dashboard.DashboardActivity


object NotificationHelper {

    private const val NEW_PRESCRIPTION_CHANNEL_ID = "new_prescription_channel"
    private const val NEW_PRESCRIPTION_CHANNEL_NAME = "New Prescription Channel"
    private const val NEW_PRESCRIPTION_CHANNEL_DESCRIPTION = "New Prescription Channel Description"

    fun showNewPrescriptionNotification(context: Context,notificationTitle:String,notificationText:String){
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationIntent = Intent(context,DashboardActivity::class.java)
            notificationIntent.putExtra("tab_value",2)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, FLAG_IMMUTABLE)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(
                NEW_PRESCRIPTION_CHANNEL_ID,
                NEW_PRESCRIPTION_CHANNEL_NAME,
                importance
            )
            mChannel.description = NEW_PRESCRIPTION_CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(mChannel)
            val notification = Notification.Builder(context, NEW_PRESCRIPTION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setColor(ContextCompat.getColor(context, R.color.purple))
                .setShowWhen(true)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(System.currentTimeMillis().hashCode(), notification)
        }
    }

}
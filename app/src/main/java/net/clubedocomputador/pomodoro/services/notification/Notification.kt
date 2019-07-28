package net.clubedocomputador.pomodoro.services.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object Notification {

    const val NOTIFICATION_ID = 4435
    const val NOTIFICATION_CHANNEL = "pomodoro_channel"



    private fun startChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL, "Pomodoro", NotificationManager.IMPORTANCE_LOW)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun updateNotification(context: Context, notificationId: Int, notification: Notification) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(notificationId, notification)
    }


}


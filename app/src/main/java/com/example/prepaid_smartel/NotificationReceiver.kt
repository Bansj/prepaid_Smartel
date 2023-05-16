package com.example.prepaid_smartel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Get the remaining days value from the intent extras
        val remain = intent.getIntExtra("remainingDays", 0)

        // Create an intent to launch the MainActivity when the notification is tapped
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0)

        // Create a notification builder
        val notificationBuilder = NotificationCompat.Builder(context, "my_notification_channel")
            .setSmallIcon(R.drawable.prepaid_app_icon_noti)
            .setContentTitle("만료 일 알림")
            .setContentText("요금 충전 만료일이 다가옵니다. 충전을 잊지 마세요! $remain day(s).")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        // Get the notification manager system service
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_notification_channel",
                "Prepaid phone recharge reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Display the notification
        notificationManager.notify(0, notificationBuilder.build())
    }
}

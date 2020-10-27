package com.arukas.base.notification

import android.util.Log
import androidx.core.app.NotificationCompat
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult

class MyNotificationExtender : NotificationExtenderService() {
    override fun onNotificationProcessing(receivedResult: OSNotificationReceivedResult): Boolean {
        val overrideSettings = OverrideSettings()
        overrideSettings.extender =
            NotificationCompat.Extender { builder -> //Force remove push from Notification Center after 30 seconds
                builder.setTimeoutAfter(30000)
                // Sets the icon accent color notification color to Green on Android 5.0+ devices.
//                builder.color = BigInteger("FF00FF00", 16).toInt()
//                builder.setContentTitle(receivedResult.payload.title)
//                builder.setContentText(receivedResult.payload.body)
                builder.setChannelId("0")
                builder
            }
        val displayedResult = displayNotification(overrideSettings)
        Log.d(
            "OneSignalExample",
            "Notification displayed with id: " + displayedResult.androidNotificationId
        )

        // Return true to stop the notification from displaying.
        return false
    }
}
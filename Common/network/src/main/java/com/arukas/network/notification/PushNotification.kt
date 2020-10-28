package com.arukas.network.notification

import android.content.Context
import com.google.gson.Gson
import com.onesignal.OneSignal
import org.json.JSONObject

object PushNotification {

    fun innit(context: Context) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.startInit(context)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
            .init()

    }

    fun oneSignalId(): String? {
        return OneSignal.getPermissionSubscriptionState().subscriptionStatus.userId
    }

    fun send(oneSignalIds: List<String>, text: String) {
        val json = NotificationContent()
        json.contents.en = text
        json.include_player_ids = oneSignalIds
        OneSignal.postNotification(
            Gson().toJson(json),
            object : OneSignal.PostNotificationResponseHandler {
                override fun onSuccess(response: JSONObject?) {
                    println("onesignal response $response")
                }

                override fun onFailure(response: JSONObject?) {
                    println("onesignal response $response")
                }

            })
    }

    fun send(oneSignalIds: String, text: String) {
        val json = NotificationContent()
        json.contents.en = text
        json.include_player_ids = listOf(oneSignalIds)
        OneSignal.postNotification(
            Gson().toJson(json),
            object : OneSignal.PostNotificationResponseHandler {
                override fun onSuccess(response: JSONObject?) {
                    println("onesignal response $response")
                }

                override fun onFailure(response: JSONObject?) {
                    println("onesignal response $response")
                }

            })
    }

}
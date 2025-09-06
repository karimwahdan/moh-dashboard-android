package com.kwdevs.hospitalsdashboard.app.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MyFirebaseMessagingService: FirebaseMessagingService() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {

        Log.d("FirebaseMessagingService", "Token Refreshed $token")
        GlobalScope.launch {
            PushNotificationManager.registerTokenOnServer(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data.isNotEmpty().let {
            Log.e("onMessageReceived","message: ${message.data}")
            val data=message.data
            val notificationType = data["type"] ?: return // Ensure 'type' exists

            val jsonObject = JSONObject()
            for ((key, value) in data) {
                try {

                    when {
                        value == "null" -> {
                            // Explicitly set null for "null" string
                            jsonObject.put(key, JSONObject.NULL)
                        }
                        value.trim().startsWith("{") || value.trim().startsWith("[") -> {
                            // If value is a JSON object or array, add it as such
                            jsonObject.put(key, JSONObject(value))
                        }
                        else -> {
                            // Add as plain string
                            jsonObject.put(key, value)
                        }
                    }
                } catch (e: Exception) {
                    jsonObject.put(key, value) // Fallback to string in case of error
                }
            }
            val jsonString = jsonObject.toString()
            when(notificationType){
                "blood-offer" -> NotificationHelper().showNotification(data = jsonString)
                else -> Log.e("FirebaseMessagingService", "Unknown notification type: $notificationType")
            }

        }
    }
}
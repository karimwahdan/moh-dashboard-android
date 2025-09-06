package com.kwdevs.hospitalsdashboard.app.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FcmViewModel: ViewModel() {
    private val receivedNotification = MutableStateFlow<NotificationModel?>(null)
    val notification: StateFlow<NotificationModel?> get() = receivedNotification

    fun updateNotification(notification:NotificationModel?){
        Log.e("FcmViewModel","updateIcuNotification: ${notification?.message?:"no Data"}")
        receivedNotification.value=notification
    }
}
package com.kwdevs.hospitalsdashboard.app.notifications

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class NotificationConverter {
    fun notificationConverter(json: String):NotificationModel?{
        try {
            val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(NotificationModel::class.java)
            val notification=adapter.fromJson(json)
            if(notification!=null) return notification else return null
        }
        catch (e:Exception){ e.printStackTrace();return null }
    }
}
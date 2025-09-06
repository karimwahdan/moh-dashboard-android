package com.kwdevs.hospitalsdashboard.app.notifications

import com.squareup.moshi.Json

data class NotificationModel(
    @Json(name = "message")var message:String,
    @Json(name = "title")var title:String,
    @Json(name = "sender")var sender:String,
    @Json(name = "receiver")var receiver:String,

    )

package com.kwdevs.hospitalsdashboard.bodies.notifications

data class CustomNotificationBody(
    var senderId:Int,
    var receiverId:Int,
    var message:String,
    var title:String
)

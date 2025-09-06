package com.kwdevs.hospitalsdashboard.models.notifications

import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.squareup.moshi.Json

data class CustomNotification(
    @Json(name = "id") var id:Int?=null,
    @Json(name = "sender_id") var senderId:Int?=null,
    @Json(name = "receiver_id") var receiverId:Int?=null,
    @Json(name = "title") var title:String?=null,
    @Json(name = "message") var message:String?=null,
    @Json(name = "sent") var sent:Boolean?=null,
    @Json(name = "sent_time") var sentTime:String?=null,
    @Json(name = "received") var received:Boolean?=null,
    @Json(name = "receive_time") var receiveTime:String?=null,

    @Json(name = "read") var read:Int?=null,
    @Json(name = "read_time") var readTime:String?=null,
    @Json(name = "archived") var archived:Boolean?=null,
    @Json(name = "sender") var sender:SuperUser?=null,
    @Json(name = "receiver") var receiver:SimpleHospitalUser?=null,

    )

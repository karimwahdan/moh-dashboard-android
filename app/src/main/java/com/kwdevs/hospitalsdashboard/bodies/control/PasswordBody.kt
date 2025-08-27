package com.kwdevs.hospitalsdashboard.bodies.control

data class PasswordBody(
    val userId:Int?=null,
    val username:String?=null,
    val oldPassword:String?=null,
    val newPassword:String?=null,
)

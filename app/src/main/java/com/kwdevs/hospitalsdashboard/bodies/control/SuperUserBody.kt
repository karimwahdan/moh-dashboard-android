package com.kwdevs.hospitalsdashboard.bodies.control

data class SuperUserBody(
    val id:Int?=null,
    val name:String?=null,
    val username:String?=null,
    val password:String?=null,
    val titleId:Int?=null,
    val nationalId:String?=null,
    val active:Int?=null,
    val createdBySuperId:Int?=null,
    val updatedBySuperId:Int?=null,
)

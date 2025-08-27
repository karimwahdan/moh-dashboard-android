package com.kwdevs.hospitalsdashboard.bodies.hospital

data class RenalDeviceBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val name:String?=null,
    val deviceTypeId:Int?=null,
    val active:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


)

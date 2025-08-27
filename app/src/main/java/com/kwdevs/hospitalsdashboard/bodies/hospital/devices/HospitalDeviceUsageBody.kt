package com.kwdevs.hospitalsdashboard.bodies.hospital.devices

data class HospitalDeviceUsageBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val deviceId:Int?=null,
    val deviceTypeId:Int?=null,
    val day:String,
    val usage:Int?=null,

    val createdById:Int?=null,
    val updatedById:Int?=null,
    val deletedById:Int?=null,

    val createdBySuperId:Int?=null,
    val updatedBySuperId:Int?=null,
    val deletedBySuperId:Int?=null,


)

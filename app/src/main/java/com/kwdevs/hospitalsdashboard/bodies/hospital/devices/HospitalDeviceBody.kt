package com.kwdevs.hospitalsdashboard.bodies.hospital.devices

data class HospitalDeviceBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val statusId:Int?=null,
    val departmentId:Int?=null,
    val typeId:Int?=null,
    val code:String,
    val name:String,

    val createdById:Int?=null,
    val updatedById:Int?=null,
    val deletedById:Int?=null,

    val createdBySuperId:Int?=null,
    val updatedBySuperId:Int?=null,
    val deletedBySuperId:Int?=null,


)

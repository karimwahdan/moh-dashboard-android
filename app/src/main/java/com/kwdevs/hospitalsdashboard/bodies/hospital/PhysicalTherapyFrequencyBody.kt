package com.kwdevs.hospitalsdashboard.bodies.hospital

data class PhysicalTherapyFrequencyBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val day:String?=null,
    val sessions:Int?=null,
    val patients:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


)

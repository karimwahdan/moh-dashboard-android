package com.kwdevs.hospitalsdashboard.bodies.hospital

data class ReceptionFrequencyBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val day:String?=null,
    val deaths:Int?=null,
    val patients:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


)

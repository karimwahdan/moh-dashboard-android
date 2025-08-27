package com.kwdevs.hospitalsdashboard.bodies.hospital

data class IncubatorBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val name:String?=null,
    val active:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


)

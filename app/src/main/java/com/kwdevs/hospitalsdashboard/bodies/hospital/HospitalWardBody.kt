package com.kwdevs.hospitalsdashboard.bodies.hospital

import com.squareup.moshi.Json

data class HospitalWardBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val typeId:Int?=null,
    val name:String,
    val allUnits:Int?=null,
    val freeUnits:Int?=null,
    val active:Int,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


    val accountType:Int?=null,
)

package com.kwdevs.hospitalsdashboard.bodies.hospital

import com.squareup.moshi.Json

data class InPatientWardBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val name:String,
    val active:Int,
    val allBeds:Int?=null,
    val freeBeds:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


    val accountType:Int,
)

package com.kwdevs.hospitalsdashboard.bodies.hospital

import com.squareup.moshi.Json

data class HospitalDepartmentBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val departmentBid:Int,
    val name:String,
    val active:Int,
    val headId:Int?=null,
    val deputyId:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,


    val accountType:Int,
    )

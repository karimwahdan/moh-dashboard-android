package com.kwdevs.hospitalsdashboard.models.hospital.operationRoom

import com.squareup.moshi.Json

data class OperationRoom(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "name")
    val name:String?=null,
    @Json(name = "active")
    val active:Boolean?=null,
    @Json(name = "major")
    val major:Boolean?=null,
    @Json(name = "operations_count")
    val totalOperations:Int?=null,
    @Json(name = "operations_count_this_month")
    val totalOperationsThisMonth:Int?=null
)

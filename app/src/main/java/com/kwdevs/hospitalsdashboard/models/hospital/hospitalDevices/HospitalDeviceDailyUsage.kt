package com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices

import com.squareup.moshi.Json

data class HospitalDeviceDailyUsage(
    /*
        'created_by_id',
        'deleted_by_id',
        'updated_by_id',

        'created_by_super_id',
        'updated_by_super_id',
        'deleted_by_super_id',

        'created_at_super',
        'updated_at_super',
        'deleted_at_super',
     */
    @Json(name = "id")
    val id:Int? = null,
    @Json(name = "hospital_id")
    val hospitalId:Int? = null,
    @Json(name = "device_type_id")
    val deviceTypeId:Int? = null,
    @Json(name = "device_id")
    val deviceId:Int? = null,
    @Json(name = "day")
    val day:String? = null,
    @Json(name = "usage")
    val usage:Int? = null,



)

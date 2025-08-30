package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital

import com.squareup.moshi.Json

data class HospitalBloodBankKpi(
    @Json(name = "hospital_id") val hospitalId:Int?=null,
    @Json(name = "city_name") val cityName:String?=null,
    @Json(name = "hospital_name") val hospitalName:String?=null,
    @Json(name = "total_collected") val totalCollected:Int?=null,
    @Json(name = "total_issued") val totalIssued:Int?=null,
    @Json(name = "total_expired") val totalExpired:Int?=null,
    @Json(name = "total_hcv") val totalHcv:Int?=null,
    @Json(name = "total_hbv") val totalHbv:Int?=null,
    @Json(name = "total_hiv") val totalHiv:Int?=null,
    @Json(name = "total_syphilis") val totalSyphilis:Int?=null,
    @Json(name = "totalIncomplete") val totalIncomplete:Int?=null,
)

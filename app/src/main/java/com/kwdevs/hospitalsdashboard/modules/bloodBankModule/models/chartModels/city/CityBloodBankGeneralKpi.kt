package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city

import com.squareup.moshi.Json

data class CityBloodBankGeneralKpi(
    @Json(name = "city_id") val cityId:Int?=null,
    @Json(name = "city_name") val cityName:String?=null,
    @Json(name = "total_collected") val totalCollected:Int?=null,
    @Json(name = "total_issued") val totalIssued:Int?=null,
    @Json(name = "total_expired") val totalExpired:Int?=null,
    @Json(name = "total_hcv") val totalHcv:Int?=null,
    @Json(name = "total_hbv") val totalHbv:Int?=null,
    @Json(name = "total_hiv") val totalHiv:Int?=null,
    @Json(name = "total_syphilis") val totalSyphilis:Int?=null,
    @Json(name = "total_incomplete") val totalIncomplete:Int?=null,
)

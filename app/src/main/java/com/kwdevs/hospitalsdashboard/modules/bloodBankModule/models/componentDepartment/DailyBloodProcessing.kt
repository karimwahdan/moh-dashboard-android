package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class DailyBloodProcessing(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "blood_bank_id")
    val bloodBankId:Int?=null,
    @Json(name = "blood_unit_type_id")
    val bloodUnitTypeId:Int?=null,

    @Json(name = "collection_id")
    val collectionId:Int?=null,

    @Json(name = "collection")
    val campaign:DailyBloodCollection?=null,

    @Json(name = "campaign_type_id")
    val campaignTypeId:Int?=null,

    @Json(name = "processing_date")
    val processingDate:String?=null,

    @Json(name = "total")
    val total:Int?=null,

    @Json(name = "blood_unit_type")
    val bloodType:BasicModel?=null,

    @Json(name = "editable")
    val editable:Boolean?=null,
    )

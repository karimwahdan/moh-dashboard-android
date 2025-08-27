package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.squareup.moshi.Json

data class DailyBloodCollection(
    @Json(name = "id")
    val id:Int?=null,
    @Json(name = "hospital_id")
    val hospitalId:Int?=null,
    @Json(name = "blood_bank_id")
    val bloodBankId:Int?=null,
    @Json(name = "blood_unit_type_id")
    val bloodUnitTypeId:Int?=null,

    @Json(name = "blood_group_id")
    val bloodGroupId:Int?=null,

    @Json(name = "donation_type_id")
    val donationTypeId:Int?=null,

    @Json(name = "campaign_type_id")
    val campaignTypeId:Int?=null,

    @Json(name = "collection_date")
    val collectionDate:String?=null,

    @Json(name = "total")
    val total:Int?=null,

    @Json(name = "code")
    val code:String?=null,

    @Json(name = "campaign_location")
    val campaignLocation:String?=null,

    @Json(name = "blood_group")
    val bloodGroup:BasicModel?=null,

    @Json(name = "blood_unit_type")
    val bloodType:BasicModel?=null,

    @Json(name = "donation_type")
    val donationType:BasicModel?=null,

    @Json(name = "campaign_type")
    val campaignType:BasicModel?=null,

    @Json(name = "apheresis_donors")
    val apheresisDonors:Int?=null,

    @Json(name = "incineration")
    val incineration:List<MonthlyIncineration> = emptyList()
    )

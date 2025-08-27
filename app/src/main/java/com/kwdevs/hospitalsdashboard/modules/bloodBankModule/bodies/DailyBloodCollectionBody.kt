package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class DailyBloodCollectionBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val bloodUnitTypeId:Int?=null,
    val code:String?=null,
    val bloodGroupId:Int?=null,
    val donationTypeId:Int?=null,
    val campaignTypeId:Int?=null,
    val total:Int?=null,
    val collectionDate:String?=null,
    val campaignLocation:String?=null,
    val apheresisDonors:Int?=null,
    val createdById:Int?=null
)

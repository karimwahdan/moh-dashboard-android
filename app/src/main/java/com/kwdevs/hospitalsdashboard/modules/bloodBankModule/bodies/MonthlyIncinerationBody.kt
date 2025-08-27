package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class MonthlyIncinerationBody(

    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val bloodUnitTypeId:Int?=null,
    val bloodGroupId:Int?=null,
    val collectionId:Int?=null,
    val month:String?=null,
    val year:String?=null,
    val byBCD:Int?=null,
    val byIssuing:Int?=null,
    val value:Int?=null,
    val incinerationReasonId:Int?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,

    )

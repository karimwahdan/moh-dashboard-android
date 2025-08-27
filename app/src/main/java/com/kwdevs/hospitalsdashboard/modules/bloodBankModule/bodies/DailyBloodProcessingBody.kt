package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class DailyBloodProcessingBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val bloodUnitTypeId:Int?=null,
    val collectionId:Int?=null,
    val total:Int?=null,
    val processingDate:String?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,
    val editable:Int?=null,
)

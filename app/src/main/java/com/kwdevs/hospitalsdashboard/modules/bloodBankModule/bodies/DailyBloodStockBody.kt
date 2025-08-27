package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class DailyBloodStockBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val bloodUnitTypeId:Int?=null,
    val bloodGroupId:Int?=null,
    val entryDate:String?=null,
    val timeBlock:String?=null,
    val amount:Int?=null,
    val emergency:Int?=null,
    val underInspection:Int?=null,
    val createdById:Int?=null
)

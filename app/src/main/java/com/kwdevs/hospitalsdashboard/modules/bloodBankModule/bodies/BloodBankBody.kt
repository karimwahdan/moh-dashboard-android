package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class BloodBankBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val name:String?=null,
    val bloodBankTypeId:Int?=null,
    val isNbts:Int?=null,
    val createdBySuperId:Int?=null,
)

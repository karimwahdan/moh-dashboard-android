package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class KpiBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val departmentId:Int?=null,
    val month:String?=null,
    val year:String?=null,
    val itemId:Int?=null,
    val value:Float?=null,
    val note:String?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,

)

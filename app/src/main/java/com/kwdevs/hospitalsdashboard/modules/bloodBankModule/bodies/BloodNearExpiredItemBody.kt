package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class BloodNearExpiredItemBody(

    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val unitTypeId:Int?=null,
    val bloodGroupId:Int?=null,
    val expiryDate:String?=null,
    val code:String?=null,
    val quantity:Int?=null,
    val statusId:Int?=null,
    val recipientTypeId:Int?=null,
    val recipientId:Int?=null,
    val recipientName:String?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,

)

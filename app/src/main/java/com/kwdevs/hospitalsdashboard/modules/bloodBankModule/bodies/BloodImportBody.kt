package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class BloodImportBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val unitTypeId:Int?=null,
    val bloodGroupId:Int?=null,
    val day:String?=null,
    val month:String?=null,
    val year:String?=null,
    val quantity:Int?=null,
    val byPatient:Boolean?=null,
    val isPrivateSector:Boolean?=null,
    val isGov:Boolean?=null,
    val isNbts:Boolean?=null,
    val senderHospitalId:Int?=null,
    val hospitalName:String?=null,
    val createdById:Int?=null,
)

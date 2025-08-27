package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class MonthlyIssuingReportBody(

    val id:Int?=null,
    val hospitalId:Int?=null,
    val bloodBankId:Int?=null,
    val bloodGroupId:Int?=null,
    val bloodUnitTypeId:Int?=null,
    val month:String?=null,
    val year:String?=null,
    val isPrivateSector:Int?=null,
    val isInPatient:Int?=null,
    val isOutPatient:Int?=null,
    val isHospital:Int?=null,

    val receivingHospitalId:Int?=null,
    val isNationalBloodBank:Int?=null,

    val receivingBloodBankId:Int?=null,

    val hospitalName:String?=null,
    val quantity:Int?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null

)

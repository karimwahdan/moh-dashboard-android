package com.kwdevs.hospitalsdashboard.bodies.patients

data class PatientBody(
    val id:Int?=null,
    val firstName:String?=null,
    val secondName:String?=null,
    val thirdName:String?=null,
    val fourthName:String?=null,
    val nationalId:String?=null,
    val nationalityId:Int?=null,
    val hospitalId:Int?=null,
    val patientCode:String?=null,
    val mobileNumber:String?=null,
    val altMobileNumber:String?=null,
    val gender:Int?=null,
    val active:Int?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,
    val deletedById:Int?=null
)

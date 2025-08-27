package com.kwdevs.hospitalsdashboard.bodies.patients

data class PatientAdmissionBody(
    val id:Int?=null,
    val patientId:Int?=null,
    val hospitalId:Int?=null,
    val wardTypeId:Int?=null,
    val wardId:Int?=null,
    val bedCode:String?=null,
    val patientStateId:Int?=null,
    val admissionTime:String?=null,
    val exitTime:String?=null,
    val patientQuit:Int?=null,
    val patientDie:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,
)

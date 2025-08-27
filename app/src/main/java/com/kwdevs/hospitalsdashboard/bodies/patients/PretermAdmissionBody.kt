package com.kwdevs.hospitalsdashboard.bodies.patients

data class PretermAdmissionBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val motherId:Int?=null,
    val patientId:Int?=null,
    val patientCodeId:Int?=null,

    val admissionDate:String?=null,
    val exitDate:String?=null,
    val patientQuit:Int?=null,
    val patientDie:Int?=null,
    val patientStateId:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,
)

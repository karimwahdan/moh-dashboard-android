package com.kwdevs.hospitalsdashboard.bodies.hospital.clinics

data class ClinicVisitBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val clinicId:Int?=null,
    val day:String?=null,
    val visits:Int?=null,
    val visitTypeId:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,
    )

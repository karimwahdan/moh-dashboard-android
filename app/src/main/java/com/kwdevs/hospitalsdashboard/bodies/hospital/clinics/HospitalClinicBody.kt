package com.kwdevs.hospitalsdashboard.bodies.hospital.clinics

data class HospitalClinicBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val generalClinicId:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,
    )

package com.kwdevs.hospitalsdashboard.bodies.patients

data class CancerCureBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val patientId:Int?=null,
    val cureTypeId:Int?=null,
    val month:Int?=null,
    val sessions:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,

    )

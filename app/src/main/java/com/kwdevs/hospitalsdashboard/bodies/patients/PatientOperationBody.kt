package com.kwdevs.hospitalsdashboard.bodies.patients

data class PatientOperationBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val patientId:Int?=null,
    val operationStatusId:Int?=null,
    val operationTypeId:Int?=null,
    val operationDepartmentTypeId:Int?=null,
    val operationRoomId:Int?=null,
    val operationTime:String?=null,
    val exitTime:String?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,
    )

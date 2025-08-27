package com.kwdevs.hospitalsdashboard.bodies.patients

data class BabyBirthBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val birthdate:String?=null,
    val motherId:Int?=null,
    val birthTypeId:Int?=null,
    val babyDied:Int?=null,
    val motherDied:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,

    )

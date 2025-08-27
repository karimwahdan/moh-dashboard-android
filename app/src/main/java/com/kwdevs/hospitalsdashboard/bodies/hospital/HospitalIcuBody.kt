package com.kwdevs.hospitalsdashboard.bodies.hospital

data class HospitalIcuBody(
    val id:Int?=null,

    val hospitalId:Int?=null,

    val hasIcu:Int,
    val allIcuBeds:Int,
    val freeIcuBeds:Int,

    val hasCcu:Int,
    val allCcuBeds:Int?=null,
    val freeCcuBeds:Int?=null,

    val hasNeurologyCu:Int,
    val allNeurologyCuBeds:Int?=null,
    val freeNeurologyCuBeds:Int?=null,

    val hasNicu:Int,
    val allNicuBeds:Int?=null,
    val freeNicuBeds:Int?=null,

    val hasBurnsCu:Int,
    val allBurnsCuBeds:Int?=null,
    val freeBurnsCuBeds:Int?=null,

    val hasOncologyCu:Int,
    val allOncologyCuBeds:Int?=null,
    val freeOncologyCuBeds:Int?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,

    val accountType:Int,
    )

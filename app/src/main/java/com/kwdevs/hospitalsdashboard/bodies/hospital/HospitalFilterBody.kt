package com.kwdevs.hospitalsdashboard.bodies.hospital

data class HospitalFilterBody(
    val cityId:Int?=null,
    val areaId:Int?=null,
    val typeId:Int?=null,
    val sectorId:Int?=null,

    val hasIcu:Int?=null,
    val icuFreeBedsStart:Int?=null,
    val icuFreeBedsEnd:Int?=null,

    val hasCcu:Int?=null,
    val ccuFreeBedsStart:Int?=null,
    val ccuFreeBedsEnd:Int?=null,

    val hasNeurologyCu:Int?=null,
    val neurologyFreeBedsStart:Int?=null,
    val neurologyFreeBedsEnd:Int?=null,

    val hasNicu:Int?=null,
    val nicuFreeBedsStart:Int?=null,
    val nicuFreeBedsEnd:Int?=null,

    val hasBurnCu:Int?=null,
    val burnsFreeBedsStart:Int?=null,
    val burnsFreeBedsEnd:Int?=null,

    val hasOncologyCu:Int?=null,
    val oncologyFreeBedsStart:Int?=null,
    val oncologyFreeBedsEnd:Int?=null,

    val hasMorgue:Int?=null,
    val morgueFreeBedsStart:Int?=null,
    val morgueFreeBedsEnd:Int?=null,

    val hasDevices:Int?=null,
    val deviceStatusId:Int?=null,
    val deviceDepartmentId:Int?=null,

    val hasRenalWards:Int?=null,
    val renalFreeBedsStart:Int?=null,
    val renalFreeBedsEnd:Int?=null,

    val hasWards:Int?=null,
    val wardsFreeBedsStart:Int?=null,
    val wardsFreeBedsEnd:Int?=null,

    //val accountType:Int,
    )

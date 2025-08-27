package com.kwdevs.hospitalsdashboard.bodies.hospital

import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalDepartmentBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalIcuBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.InPatientWardBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.MorgueBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalDeviceBody
import retrofit2.http.Body

data class HospitalBody(
    val id:Int?=null,
    val name:String,
    val address:String,
    val active:Int,
    val isNbts:Int?=null,
    val cityId:Int?=null,
    val areaId:Int?=null,

    val typeId:Int?=null,

    val sectorId:Int?=null,

    @Body val inpatientWards: List<InPatientWardBody>? = null,
    @Body val renalWards: List<RenalDeviceBody>? = null,

    @Body val departments: List<HospitalDepartmentBody>? = null,

    @Body val icu: HospitalIcuBody? = null,
    @Body val morgue: MorgueBody?=null,

    val createdById:Int?=null,
    val createdBySuperId:Int?=null,

    val updatedById:Int?=null,
    val updatedBySuperId:Int?=null,

    val deletedById:Int?=null,
    val deletedBySuperId:Int?=null,

    val accountType:Int?=null,





    )

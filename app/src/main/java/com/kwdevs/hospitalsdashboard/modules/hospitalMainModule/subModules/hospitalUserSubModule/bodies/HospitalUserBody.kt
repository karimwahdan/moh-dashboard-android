package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies

data class HospitalUserBody(
    val id:Int?=null,
    val hospitalId:Int?=null,
    val name:String?=null,
    val username:String?=null,
    val password:String?=null,
    val titleId:Int?=null,
    val nationalId:String?=null,
    val active:Int?=null,
    val createdById:Int?=null,
    val updatedById:Int?=null,
    val createdBySuperId:Int?=null,
    val updatedBySuperId:Int?=null,
)

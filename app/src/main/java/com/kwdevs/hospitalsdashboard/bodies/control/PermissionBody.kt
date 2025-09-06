package com.kwdevs.hospitalsdashboard.bodies.control

data class PermissionBody(
    val id:Int?=null,
    val name:String,
    val slug:String,
    val roleId:Int?=null,
    val modelId:Int?=null,
    val actionTypeId:Int?=null,
    val columns:List<String>? = emptyList(),
    val conditions:List<ConditionBody>? = emptyList(),
    val userTypeId:Int?=null,
)

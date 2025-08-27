package com.kwdevs.hospitalsdashboard.bodies.control

data class PermissionBody(
    val id:Int?=null,
    val name:String,
    val slug:String,
    val roleId:Int?=null,
    val modelId:Int,
    val actionTypeId:Int,
    val columns:List<String>,
    val conditions:List<ConditionBody>
)

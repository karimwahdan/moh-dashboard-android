package com.kwdevs.hospitalsdashboard.bodies.control

data class ConditionBody(
    val clause:String,
    val columnName:String,
    val operator:String?,
    val value:String
)

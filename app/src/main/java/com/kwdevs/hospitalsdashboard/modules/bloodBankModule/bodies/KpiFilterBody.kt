package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class KpiFilterBody(
    val yearFrom:String?=null,
    val monthFrom:String?=null,
    val dayFrom:String?=null,
    val yearTo:String?=null,
    val monthTo:String?=null,
    val dayTo:String?=null,
)

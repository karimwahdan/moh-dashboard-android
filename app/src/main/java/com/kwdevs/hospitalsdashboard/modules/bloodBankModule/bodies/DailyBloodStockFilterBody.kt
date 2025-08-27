package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies

data class DailyBloodStockFilterBody(
    val hospitalIds:List<Int>?= emptyList(),
    val bloodGroupIds:List<Int>?= emptyList(),
    val sectorIds:List<Int>?= emptyList(),
    val typeIds:List<Int>?= emptyList(),
    val bloodBankIds:List<Int>?= emptyList(),
    val bloodUnitTypeId:Int?=null,
    val startDate:String?=null,
    val endDate:String?=null,
    val timeBlock:String?=null,
)

package com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PieGraph

@Composable
fun DailyBloodStockPieChart(list:List<DailyBloodStock>){
    val byBloodGroup= preparePieChartByBloodGroup(list.filter { (it.bloodUnitTypeId ?: 0) in listOf(1,2) })
    val vs=byBloodGroup.map { Pair(it.key.name?:"",it.value.toInt()) }
    val colors=baseColors.map { it.toArgb() }
    if(list.isNotEmpty())PieGraph(xData = vs, pieCenterText = "نسبة الفصائل")
}
private fun preparePieChartByBloodGroup(data: List<DailyBloodStock>): Map<BasicModel, Float> {
    val groups = data.mapNotNull { it.bloodGroup }.distinct()
    val total = data.sumOf { it.amount ?: 0 }.toFloat()

    return groups.associateWith { group ->
        data.filter { it.bloodGroup == group}.sumOf { it.amount ?: 0 }
            .div(total)
            .times(100f)
    }
}
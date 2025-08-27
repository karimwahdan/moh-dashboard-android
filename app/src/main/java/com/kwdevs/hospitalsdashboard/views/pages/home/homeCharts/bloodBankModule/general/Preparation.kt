package com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general

import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpi
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule.baseColors

fun prepareGroupedData(data: List<HospitalBloodBankKpi>): Pair<List<String>, List<IBarDataSet>> {

    val xLabels = data.map { it.hospitalName ?: "Unknown" } // e.g., Cairo, Giza, etc.
    // Initialize entries for each dataset
    val totalCollectedEntries = mutableListOf<BarEntry>()
    val totalIssuedEntries = mutableListOf<BarEntry>()
    val totalHbvEntries = mutableListOf<BarEntry>()
    val totalHcvEntries = mutableListOf<BarEntry>()
    val totalHivEntries = mutableListOf<BarEntry>()
    val totalSyphilisEntries = mutableListOf<BarEntry>()
    data.forEachIndexed { index, entry ->
        totalCollectedEntries.add(BarEntry(index.toFloat(), (entry.totalCollected ?: 0).toFloat()))
        totalIssuedEntries.add(BarEntry(index.toFloat(), (entry.totalIssued ?: 0).toFloat()))
        totalHbvEntries.add(BarEntry(index.toFloat(), (entry.totalHbv ?: 0).toFloat()))
        totalHcvEntries.add(BarEntry(index.toFloat(), (entry.totalHcv ?: 0).toFloat()))
        totalHivEntries.add(BarEntry(index.toFloat(), (entry.totalHiv ?: 0).toFloat()))
        totalSyphilisEntries.add(BarEntry(index.toFloat(), (entry.totalSyphilis ?: 0).toFloat()))
    }
    val datasets = listOf(
        BarDataSet(totalCollectedEntries, "مجمع"),
        BarDataSet(totalIssuedEntries, "منصرف"),
    ).mapIndexed { index, dataSet ->
        dataSet.apply {
            color = baseColors[index % baseColors.size].toArgb()
            valueTextSize = 12f
        }
    }
    return Pair(xLabels, datasets)
}

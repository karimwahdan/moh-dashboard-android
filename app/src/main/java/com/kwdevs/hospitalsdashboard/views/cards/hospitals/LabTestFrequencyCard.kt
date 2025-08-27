package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequency
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label

@Composable
fun LabTestFrequencyCard(item:LabTestFrequency){
    val patients=item.patientsCount
    val tests=item.testsCount
    val day=item.day

    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Row(modifier=Modifier.padding(5.dp),verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_info_white, background = BLUE)
                Label(day?:"")
            }
            Row(modifier=Modifier.padding(vertical = 5.dp),verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_patient, background = BLUE)
                Label("${patients?:0}")
            }
            Row(modifier=Modifier.padding(5.dp),verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_lab_test, background = BLUE)
                Label("${tests?:0}")
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.views.cards.hospitals.clinics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisit
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MEDICAL_EXAMINATIONS_FREQUENCY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun ClinicVisitCard(item:DailyClinicVisit){
    val day=item.day
    val visitType=item.visitType
    val visits=item.visits

    ColumnContainer {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(day?:"")
                HorizontalSpacer()
                Span(visitType?.name?:"", backgroundColor = BLUE, color = WHITE)
            }
            Label(label = MEDICAL_EXAMINATIONS_FREQUENCY_LABEL, text = (visits?:0).toString())
        }
    }
}
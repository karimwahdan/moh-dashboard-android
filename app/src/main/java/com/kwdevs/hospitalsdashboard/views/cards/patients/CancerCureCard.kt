package com.kwdevs.hospitalsdashboard.views.cards.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCure
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PatientFullName
import com.kwdevs.hospitalsdashboard.views.assets.SESSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun CancerCureCard(item:CancerCure,navHostController: NavHostController){
    val patient = item.patient
    val cureType=item.cureType
    val sessions=item.sessions
    val month=when(item.month){
        1->"يناير"
        2->"فبراير"
        3->"مارس"
        4->"ابريل"
        5->"مايو"
        6->"يونيو"
        7->"يوليو"
        8->"اغسطس"
        9->"سبتمبر"
        10->"اكتوبر"
        11->"نوفمبر"
        12->"ديسمبر"
        else->""
    }
    ColumnContainer {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)){
            PatientFullName(patient)
            HorizontalSpacer()
            Span(cureType?.name?:"", backgroundColor = BLUE, color = WHITE)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Label(label = MONTH_LABEL,text=month)
            Label(label = SESSIONS_LABEL, text = "${sessions ?: 0}")
        }

    }
}
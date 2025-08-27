package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.bloodImport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GOVERNMENTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PRIVATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.monthName

@Composable
fun BloodImportHorizontalCard(item:BloodImport){

    Column {
        Row(modifier= Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){

            val id=item.id
            val quantity=item.quantity
            val day=item.day
            val month=item.month
            val year=item.year
            val bloodType=item.bloodUnitType
            val bloodGroup=item.bloodGroup
            val hospitalName=item.hospitalName
            val sender=item.senderHospital
            val isPrivateSector=item.isPrivateSector
            val isNbts=item.isNbts
            val isGov=item.isGov
            val isPatient=item.byPatient
            val byString=if(isPatient==true) PATIENT_LABEL else{
                if(isGov==true) GOVERNMENTAL_LABEL else if(isPrivateSector==true) PRIVATE_LABEL
                else if(isNbts==true) IS_NBTS_LABEL else EMPTY_STRING
            }
            val color=when(byString){
                PATIENT_LABEL->{BLUE}
                GOVERNMENTAL_LABEL->{ORANGE}
                PRIVATE_LABEL->{TEAL700}
                IS_NBTS_LABEL->{BLACK}
                else->{Color.Transparent}
            }
            val supplierString=if(isPatient==true){
                if(isNbts==true || isGov==true) sender?.name else {
                    if(isPrivateSector==true) hospitalName else sender?.name
                }
            }else{
                if(isNbts==true || isGov==true) sender?.name else {
                    if(isPrivateSector==true) hospitalName else sender?.name
                }
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
                contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Span(text = byString, color = WHITE, backgroundColor = color)
                    Label(supplierString?:"")
                }
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
                contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Span(bloodGroup?.name?: EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
                    Label(bloodType?.name?: EMPTY_STRING)

                }
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
                contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Span(year?: EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
                    Label("$day-${monthName(month)}")
                }
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
                contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Span("$quantity", backgroundColor = BLUE, color = WHITE)
                }
            }
        }
        HorizontalDivider()
    }
}
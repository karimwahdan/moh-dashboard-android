package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.reports.monthlyReport

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GOVERNMENTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_PRIVATE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.OUT_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PLASMA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QUANTITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RANDOM_DONOR_PLATELETS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RECIPIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SINGLE_DONOR_PLATELETS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun MonthlyIssuingReportListCard(items:List<MonthlyIssuingReport>){

    val sorted              = items.sortedWith(compareByDescending<MonthlyIssuingReport> { it.year }.thenByDescending { it.month })
    val bloodStock          = sorted.filter { it.bloodUnitTypeId in listOf(1,2) }
    val plasmaStock         = sorted.filter { it.bloodUnitTypeId in listOf(3,4,5,6) }
    val rdpPlatelets        = sorted.filter { it.bloodUnitTypeId ==7 }
    val sdpPlatelets        = sorted.filter { it.bloodUnitTypeId ==9 }
    val sdpPlateletsCount   = sdpPlatelets.sumOf { it.quantity?:0 }
    val rdpPlateletsCount   = rdpPlatelets.sumOf { it.quantity?:0 }
    val totalPRBCsCount     = bloodStock.sumOf { it.quantity?:0 }
    val totalPlasmaCount    = plasmaStock.sumOf { it.quantity?:0 }

    if(bloodStock.isNotEmpty()){
        LabelSpan(label=BLOOD_UNITS_LABEL,value="$totalPRBCsCount", spanColor = BLUE)
        BloodReport(bloodStock)
        VerticalSpacer()
    }
    if(rdpPlatelets.isNotEmpty()){
        LabelSpan(label=RANDOM_DONOR_PLATELETS_LABEL,value="$rdpPlateletsCount", spanColor = BLUE)
        BloodReport(rdpPlatelets)
        VerticalSpacer()
    }
    if(sdpPlatelets.isNotEmpty()){
        LabelSpan(label= SINGLE_DONOR_PLATELETS_LABEL,value="$sdpPlateletsCount", spanColor = BLUE)
        BloodReport(sdpPlatelets)
        VerticalSpacer()
    }
    if(plasmaStock.isNotEmpty()){
        LabelSpan(label= PLASMA_LABEL,value="$totalPlasmaCount", spanColor = BLUE)
        PlasmaReport(plasmaStock)
        VerticalSpacer()
    }
}

@Composable
private fun PlasmaReport(items: List<MonthlyIssuingReport>){
    if(items.isNotEmpty()){
        val aPlasmaCount      = items.sumOf { if(it.bloodGroupId in listOf(1,2)) it.quantity?:0 else 0 }
        val bPlasmaCount      = items.sumOf { if( it.bloodGroupId in listOf(3,4)) it.quantity?:0 else 0 }
        val oPlasmaCount      = items.sumOf { if( it.bloodGroupId in listOf(5,6)) it.quantity?:0 else 0 }
        val aBPlasmaCount     = items.sumOf { if( it.bloodGroupId in listOf(7,8)) it.quantity?:0 else 0 }

        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="A",value=aPlasmaCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="B",value=bPlasmaCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="O",value=oPlasmaCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="AB",value=aBPlasmaCount.toString())
            }
        }
        VerticalSpacer(10)
        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(WHITE)){
            Row(modifier=Modifier.fillMaxWidth()){
                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(RECIPIENT_LABEL)
                }
                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(BLOOD_GROUP_LABEL)
                }

                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(QUANTITY_LABEL)
                }

            }
            HorizontalDivider()
            items.forEach { item->
                Row(modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            if(item.isInPatient==true){
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    VerticalSpacer(11)
                                    Label(IN_PATIENT_LABEL)
                                    VerticalSpacer(11)
                                }

                            }
                            if(item.isOutPatient==true){
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    VerticalSpacer(11)
                                    Label(OUT_PATIENT_LABEL)
                                    VerticalSpacer(11)
                                }

                            }
                            else{
                                if(item.isNationalBloodBank==true){
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Label(item.receivingBloodBank?.name?:EMPTY_STRING)
                                        Span(text = IS_NBTS_LABEL, backgroundColor = BLUE, color = WHITE)
                                        VerticalSpacer(3)
                                    }
                                }
                                else{
                                    if(item.isPrivateSector == true){
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Label(item.hospitalName?:EMPTY_STRING)
                                            Span(text =IS_PRIVATE_SECTOR_LABEL, backgroundColor = BLUE, color = WHITE)
                                            VerticalSpacer(3)
                                        }
                                    }
                                    else{
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Label(item.receivingHospital?.name?:EMPTY_STRING)
                                            Span(text = GOVERNMENTAL_LABEL, backgroundColor = ORANGE, color = WHITE,
                                                startPadding = 5, endPadding = 5)
                                            VerticalSpacer(3)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Label(item.bloodUnitType?.name?:EMPTY_STRING)
                            Span(item.bloodGroup?.name?:EMPTY_STRING, color = WHITE, backgroundColor = BLUE)
                            VerticalSpacer()

                        }
                    }
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Label("${item.quantity?:0}")
                            Label(UNIT_LABEL)
                            VerticalSpacer()

                        }
                    }

                }


            }

        }
    }

}

@Composable
private fun BloodReport(items: List<MonthlyIssuingReport>){
    val aPosPRBCsCount    = items.sumOf { if(it.bloodGroupId==1) it.quantity?:0 else 0 }
    val aNegPRBCsCount    = items.sumOf { if(it.bloodGroupId==2) it.quantity?:0 else 0 }
    val bPosPRBCsCount    = items.sumOf { if(it.bloodGroupId==3) it.quantity?:0 else 0 }
    val bNegPRBCsCount    = items.sumOf { if(it.bloodGroupId==4) it.quantity?:0 else 0 }
    val oPosPRBCsCount    = items.sumOf { if(it.bloodGroupId==5) it.quantity?:0 else 0 }
    val oNegPRBCsCount    = items.sumOf { if(it.bloodGroupId==6) it.quantity?:0 else 0 }
    val aBPosPRBCsCount   = items.sumOf { if(it.bloodGroupId==7) it.quantity?:0 else 0 }
    val aBNegPRBCsCount   = items.sumOf { if(it.bloodGroupId==8) it.quantity?:0 else 0 }

    if(items.isNotEmpty()){
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).fillMaxWidth().padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="A+",value=aPosPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="A-",value=aNegPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="B+",value=bPosPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="B-",value=bNegPRBCsCount.toString())
            }
        }
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="O+",value=oPosPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="O-",value=oNegPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="AB+",value=aBPosPRBCsCount.toString())
            }
            Box(modifier=Modifier.background(WHITE).border(width = 1.dp, color = GRAY).padding(horizontal = 5.dp).weight(1f),contentAlignment = Alignment.Center){
                LabelSpan(label="AB-",value=aBNegPRBCsCount.toString())
            }
        }
        VerticalSpacer()
        ColumnContainer(shape = RectangleShape) {

            Row(modifier=Modifier.fillMaxWidth()){
                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(RECIPIENT_LABEL)
                }
                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(BLOOD_GROUP_LABEL)
                }

                Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(QUANTITY_LABEL)
                }

            }
            HorizontalDivider()
            items.forEach { item->
                Row(modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            if(item.isInPatient==true){
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    VerticalSpacer(11)
                                    Label("$PATIENT_LABEL $IN_PATIENT_LABEL")
                                    VerticalSpacer(11)
                                }

                            }

                            else{
                                if(item.isOutPatient==true){
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        VerticalSpacer(3)
                                        Label("$PATIENT_LABEL $OUT_PATIENT_LABEL")
                                        VerticalSpacer(3)
                                        Label(item.receivingHospital?.name?:item.hospitalName?:EMPTY_STRING)
                                        VerticalSpacer(3)

                                    }

                                }
                                else if(item.isNationalBloodBank==true){
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Label(item.receivingBloodBank?.name?:EMPTY_STRING)
                                        Span(text = IS_NBTS_LABEL, backgroundColor = BLUE, color = WHITE)
                                        VerticalSpacer(3)
                                    }
                                }
                                else{
                                    if(item.isPrivateSector == true){
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Label(item.hospitalName?:EMPTY_STRING)
                                            Span(text = IS_PRIVATE_SECTOR_LABEL, backgroundColor = BLUE, color = WHITE)
                                            VerticalSpacer(3)
                                        }
                                    }
                                    else{
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Label(item.receivingHospital?.name?:EMPTY_STRING)
                                            Span(text = GOVERNMENTAL_LABEL, backgroundColor = ORANGE, color = WHITE,
                                                startPadding = 5, endPadding = 5)
                                            VerticalSpacer(3)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Label(item.bloodUnitType?.name?:EMPTY_STRING)
                            Span(item.bloodGroup?.name?:EMPTY_STRING, color = WHITE, backgroundColor = BLUE)
                            VerticalSpacer()

                        }
                    }
                    Box(modifier=Modifier.fillMaxWidth().border(width=1.dp, color = Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Label("${item.quantity?:0}")
                            Label(UNIT_LABEL)
                            VerticalSpacer()

                        }
                    }

                }
            }
        }
    }

}
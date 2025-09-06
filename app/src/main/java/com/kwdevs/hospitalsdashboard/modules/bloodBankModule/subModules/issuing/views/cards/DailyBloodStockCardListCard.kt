package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.cards

import android.util.Log
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
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.STRATEGIC_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun DailyBloodStockCardListCard(items:Map<String,List<DailyBloodStock>>){
    val spanSize=Preferences.FontSettings.SpanSettings().get()

    /*
    Column{
        Row(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY), horizontalArrangement = Arrangement.Center){
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1.5f), contentAlignment = Alignment.Center){ Label(TIME_LABEL, fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("A+", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("A-", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("B+", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("B-", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("O+", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("O-", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("AB+", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("AB-", fontSize = 12) }
        }

    }
     */

    items.forEach { (time, items) ->
        //val underInspection=items.filter{it.underInspection==true}
        val blood=items
            //.filter { it.bloodUnitTypeId in listOf(1,2) && it.underInspection!=true}
        //val underInspectionCount=underInspection.sumOf{it.amount?:0}
        val aPosCount=blood.sumOf  { if(it.bloodGroupId ==1 ) it.amount?:0 else 0 }
        val aPosStrategicCount=blood.sumOf  { if(it.bloodGroupId ==1 ) it.emergency?:0 else 0 }
        val aNegCount=blood.sumOf  { if(it.bloodGroupId ==2 ) it.amount?:0 else 0 }
        val aNegStrategicCount=blood.sumOf  { if(it.bloodGroupId ==2 ) it.emergency?:0 else 0 }
        val bPosCount=blood.sumOf  { if(it.bloodGroupId ==3 ) it.amount?:0 else 0 }
        val bPosStrategicCount=blood.sumOf  { if(it.bloodGroupId ==3 ) it.emergency?:0 else 0 }
        val bNegCount=blood.sumOf  { if(it.bloodGroupId ==4 ) it.amount?:0 else 0 }
        val bNegStrategicCount=blood.sumOf  { if(it.bloodGroupId ==4 ) it.emergency?:0 else 0 }
        val oPosCount=blood.sumOf  { if(it.bloodGroupId ==5 ) it.amount?:0 else 0 }
        val oPosStrategicCount=blood.sumOf  { if(it.bloodGroupId ==5 ) it.emergency?:0 else 0 }
        val oNegCount=blood.sumOf  { if(it.bloodGroupId ==6 ) it.amount?:0 else 0 }
        val oNegStrategicCount=blood.sumOf  { if(it.bloodGroupId ==6 ) it.emergency?:0 else 0 }
        val aBPosCount=blood.sumOf { if(it.bloodGroupId ==7 ) it.amount?:0 else 0 }
        val aBPosStrategicCount=blood.sumOf { if(it.bloodGroupId ==7 ) it.emergency?:0 else 0 }
        val aBNegCount=blood.sumOf { if(it.bloodGroupId ==8 ) it.amount?:0 else 0 }
        val aBNegStrategicCount=blood.sumOf { if(it.bloodGroupId ==8 ) it.emergency?:0 else 0 }
        val bloodGroupNameSize=15
        val bloodQuantitySize=13
        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Column(modifier=Modifier.border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Box(modifier=Modifier.padding(horizontal = 5.dp),
                    contentAlignment = Alignment.Center){ Label(TIME_LABEL, fontSize = 12)
                }
                VerticalSpacer(5)

                Box(modifier=Modifier.padding(horizontal = 5.dp),
                    contentAlignment = Alignment.Center){
                    Label(time, fontSize = spanSize, maximumLines = 3)
                }
                VerticalSpacer(5)

                Box(modifier=Modifier.padding(horizontal = 5.dp), contentAlignment = Alignment.Center){
                    Label(STRATEGIC_LABEL, fontSize = spanSize, maximumLines = 3)
                }
                VerticalSpacer(5)

            }
            Row(modifier=Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("A+", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aPosCount", backgroundColor = if(aPosCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aPosStrategicCount",
                            backgroundColor = if(aPosStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("A-", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aNegCount", backgroundColor = if(aNegCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aNegStrategicCount",
                            backgroundColor = if(aNegStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("B+", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$bPosCount", backgroundColor = if(bPosCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$bPosStrategicCount",
                            backgroundColor = if(bPosStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp), contentAlignment = Alignment.Center){
                        Label("B-", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp), contentAlignment = Alignment.Center){
                        Span("$bNegCount",
                            backgroundColor = if(bNegCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),
                        contentAlignment = Alignment.Center){ Span("$bNegStrategicCount",
                        backgroundColor = if(bNegStrategicCount>0)BLUE else Color.Red,
                        color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("O+", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),
                        contentAlignment = Alignment.Center){
                        Span("$oPosCount",
                            backgroundColor = if(oPosCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize)
                    }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$oPosStrategicCount",
                            backgroundColor = if(oPosStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("O-", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$oNegCount",
                            backgroundColor = if(oNegCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$oNegStrategicCount",
                            backgroundColor = if(oNegStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("AB+", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aBPosCount", backgroundColor = if(aBPosCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aBPosStrategicCount",
                            backgroundColor = if(aBPosStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Label("AB-", fontSize = bloodGroupNameSize) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aBNegCount",
                            backgroundColor = if(aBNegCount>0)BLUE else Color.Red, color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){
                        Span("$aBNegStrategicCount",
                            backgroundColor = if(aBNegStrategicCount>0)BLUE else Color.Red,
                            color = WHITE, fontSize = bloodQuantitySize) }
                    VerticalSpacer(2)

                }

            }
        }
        VerticalSpacer()
    }


}

@Composable
fun DailyPlateletsStockCardListCard(items:Map<String,List<DailyBloodStock>>){

    items.forEach { (time, items) ->
        //val underInspection=items.filter{it.underInspection==true}
        val blood=items
        //.filter { it.bloodUnitTypeId in listOf(1,2) && it.underInspection!=true}
        //val underInspectionCount=underInspection.sumOf{it.amount?:0}
        val aPosCount=blood.sumOf  { if(it.bloodGroupId ==1 ) it.amount?:0 else 0 }
        val aNegCount=blood.sumOf  { if(it.bloodGroupId ==2 ) it.amount?:0 else 0 }
        val bPosCount=blood.sumOf  { if(it.bloodGroupId ==3 ) it.amount?:0 else 0 }
        val bNegCount=blood.sumOf  { if(it.bloodGroupId ==4 ) it.amount?:0 else 0 }
        val oPosCount=blood.sumOf  { if(it.bloodGroupId ==5 ) it.amount?:0 else 0 }
        val oNegCount=blood.sumOf  { if(it.bloodGroupId ==6 ) it.amount?:0 else 0 }
        val aBPosCount=blood.sumOf { if(it.bloodGroupId ==7 ) it.amount?:0 else 0 }
        val aBNegCount=blood.sumOf { if(it.bloodGroupId ==8 ) it.amount?:0 else 0 }

        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Column(modifier=Modifier.border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Box(modifier=Modifier.padding(horizontal = 5.dp),
                    contentAlignment = Alignment.Center){ Label(TIME_LABEL, fontSize = 12) }
                VerticalSpacer(5)

                Box(modifier=Modifier.padding(horizontal = 5.dp),
                    contentAlignment = Alignment.Center){ Label(time, fontSize = 13, maximumLines = 3) }
                VerticalSpacer(5)
            }

            Row(modifier=Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("A+", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$aPosCount", backgroundColor = if(aPosCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("A-", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$aNegCount", backgroundColor = if(aNegCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("B+", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$bPosCount", backgroundColor = if(bPosCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp), contentAlignment = Alignment.Center){ Label("B-", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp), contentAlignment = Alignment.Center){ Span("$bNegCount", backgroundColor = if(bNegCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)
                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("O+", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$oPosCount", backgroundColor = if(oPosCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("O-", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$oNegCount", backgroundColor = if(oNegCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()
                    VerticalSpacer(2)

                }

                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("AB+", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$aBPosCount", backgroundColor = if(aBPosCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()

                }
                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(width = 1.dp, color = GRAY),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Label("AB-", fontSize = 12) }
                    HorizontalDivider()
                    VerticalSpacer(2)
                    Box(modifier=Modifier.padding(horizontal = 5.dp),contentAlignment = Alignment.Center){ Span("$aBNegCount", backgroundColor = if(aBNegCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
                    VerticalSpacer(2)
                    HorizontalDivider()

                }

            }

        }

        VerticalSpacer()
    }


}


@Composable
fun DailyPlasmaStockCardListCard(items:Map<String,List<DailyBloodStock>>,unitTypeId:Int=3){

    Column{
        Row(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY), horizontalArrangement = Arrangement.Center){
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1.5f), contentAlignment = Alignment.Center){ Label(TIME_LABEL, fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("A", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("B", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("O", fontSize = 12) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Label("AB", fontSize = 12) }
        }

    }
    items.forEach { (time, items) ->
        if(unitTypeId==3) Log.e("ITEMS","time: $time")
        val aCount=items.sumOf  { if(it.bloodGroupId in listOf(1,2) && it.bloodUnitTypeId==unitTypeId) it.amount?:0 else 0 }
        val bCount=items.sumOf  { if(it.bloodGroupId in listOf(3,4) && it.bloodUnitTypeId==unitTypeId) it.amount?:0 else 0 }
        val oCount=items.sumOf  { if(it.bloodGroupId in listOf(5,6) && it.bloodUnitTypeId==unitTypeId) it.amount?:0 else 0 }
        val aBCount=items.sumOf { if(it.bloodGroupId in listOf(7,8) && it.bloodUnitTypeId==unitTypeId) it.amount?:0 else 0 }
        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1.5f), contentAlignment = Alignment.Center){ Label(time, fontSize = 13, maximumLines = 3) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Span("$aCount", backgroundColor = if(aCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Span("$bCount", backgroundColor = if(bCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Span("$oCount", backgroundColor = if(oCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
            Box(modifier=Modifier.fillMaxWidth().border(width = 1.dp, color = GRAY).weight(1f), contentAlignment = Alignment.Center){ Span("$aBCount", backgroundColor = if(aBCount>0)BLUE else Color.Red, color = WHITE, fontSize = 16) }
        }
        VerticalSpacer()
    }


}
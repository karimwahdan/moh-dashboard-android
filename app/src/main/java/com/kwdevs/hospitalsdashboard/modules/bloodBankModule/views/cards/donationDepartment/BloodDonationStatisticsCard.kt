package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.donationDepartment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGNS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PLANNED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.STREET_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BloodDonationStatisticsCard(items:List<DailyBloodCollection>){
    //val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) // "July 2025"
    val groupedItems = items.sortedByDescending { it.collectionDate }.groupBy { item ->
        val localDate = LocalDate.parse(item.collectionDate?.substring(0, 10)) // if your date has only "yyyy-MM-dd"
        localDate.format(outputFormatter)
    }

    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        groupedItems.forEach { (collectionDate, subList) ->
            var show by remember { mutableStateOf(false) }
            val total                   =   subList.sumOf { it.total?:0 }
            val totalPlanned            =   subList.sumOf { if((it.campaignTypeId?:0)==1) it.total?:0 else 0 }
            val totalStreet             =   subList.sumOf { if((it.campaignTypeId?:0)==2) it.total?:0 else 0 }
            val totalCampaigns          =   subList.count { (it.campaignTypeId?:0) in listOf(1,2) }
            val totalInHouse            =   subList.sumOf { if((it.campaignTypeId?:0)==3 && it.donationTypeId!=2) it.total?:0 else 0 }
            val totalApheresis          =   subList.sumOf { if(it.donationTypeId==2) it.total?:0 else 0 }
            val totalCampaignsDonations =   totalPlanned+totalStreet
            Row(modifier=Modifier.fillMaxWidth().background(color= ORANGE), horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically){
                if(totalCampaigns>0 && totalCampaignsDonations>0){
                    Span(totalCampaigns.toString(), backgroundColor = BLUE, color = WHITE)
                    HorizontalSpacer()
                    LabelSpan(totalCampaignsDonations.toString(), CAMPAIGNS_LABEL, labelColor = WHITE)
                    HorizontalSpacer()

                }
                else{
                    LabelSpan("0",CAMPAIGNS_LABEL, labelColor = WHITE)

                }
                LabelSpan(totalApheresis.toString(),"Apheresis", labelColor = WHITE)
                HorizontalSpacer()
                Label(text=collectionDate, color = WHITE, fontWeight = FontWeight.Bold)
            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                LabelSpan((total-totalApheresis).toString(), TOTAL_LABEL)
                LabelSpan(totalPlanned.toString(),PLANNED_LABEL)
                LabelSpan(totalStreet.toString(),STREET_LABEL)
                LabelSpan(totalInHouse.toString(), IN_PATIENT_LABEL)
            }
            VerticalSpacer()

            Row(modifier=Modifier.fillMaxWidth().background(BLUE),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                IconButton(if(show) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, background = BLUE) {
                    show=!show
                }
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(subList.size.toString(), backgroundColor = ORANGE)
                    Label("Show", color = WHITE, paddingEnd = 5, paddingStart = 5)
                }
            }
            AnimatedVisibility(
                visible = show
            ) {
                Column {
                    Row(modifier=Modifier.fillMaxWidth()){
                        Column(modifier=Modifier.fillMaxWidth().border(width = 1.dp,color= GRAY).weight(0.3f),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Label(DATE_LABEL)
                            HorizontalDivider(color = GRAY)
                            subList.forEach {
                                val localDate = LocalDate.parse(it.collectionDate?.substring(0, 10))
                                Label(text=localDate.toString().substring(4).replace("-"," ").trim().replace(" ","-"), fontSize = 14)
                                HorizontalDivider()
                            }
                        }
                        Column(modifier=Modifier.fillMaxWidth().border(width = 1.dp,color= GRAY).weight(if(subList.any { it.incineration.isNotEmpty() })0.6f else 1.1f),
                            horizontalAlignment = Alignment.CenterHorizontally,){
                            Label(COLLECTION_LABEL)
                            HorizontalDivider(color=GRAY)
                            subList.forEach {

                                Row(verticalAlignment = Alignment.CenterVertically){
                                  Label("${it.total?:0}")
                                  HorizontalSpacer(3)
                                  Span(it.bloodType?.name?:EMPTY_STRING, backgroundColor = BLUE, color = WHITE, textAlign = TextAlign.Start)
                              }

                                HorizontalDivider()
                            }
                        }
                        Column(modifier=Modifier.fillMaxWidth().border(width = 1.dp,color= GRAY).weight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Label(CAMPAIGN_TYPE_LABEL)
                            HorizontalDivider()
                            subList.forEach {
                                Label(it.campaignType?.name?:EMPTY_STRING, fontSize = 14)
                                HorizontalDivider()
                            }
                        }
                        Column(modifier=Modifier.fillMaxWidth().border(width = 1.dp,color= GRAY).weight(if(subList.any { it.incineration.isNotEmpty() })1f else 0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Label(INCINERATION_LABEL)
                            HorizontalDivider()
                            subList.forEach {s->
                                if(s.incineration.isNotEmpty()){
                                    val incinerationByUnitType=s.incineration.groupBy {a-> a.campaign }
                                    LazyRow{
                                        item{
                                            incinerationByUnitType.forEach { (_,list) ->
                                                list.forEach {
                                                    Row(modifier=Modifier.border(width = 1.dp, color = GRAY).padding(horizontal = 3.dp),verticalAlignment = Alignment.CenterVertically){
                                                        LabelSpan(label=it.bloodGroup?.name?:EMPTY_STRING,
                                                            value = "${it.value?:0}", spanColor = Color.Red)
                                                        Label(it.bloodUnitType?.name?:EMPTY_STRING, fontSize = 12)
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    HorizontalDivider()

                                }
                                else{
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Label(SPACE, fontSize = 14)
                                        HorizontalDivider()

                                    }
                                }
                            }
                        }
                    }
                }
            }
            VerticalSpacer()
        }
    }
}

package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.donationDepartment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import java.time.LocalDate

@Composable
fun DailyBloodCollectionCard(item: DailyBloodCollection){
    val localDate = LocalDate.parse(item.collectionDate?.substring(0, 10)) // if your date has only "yyyy-MM-dd"
    ColumnContainer{
        Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp,horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Box(modifier=Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){LabelSpan(item.campaignType?.name?:"","")}
            Box(modifier=Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){LabelSpan((item.total?:0).toString(),"")}
            Box(modifier=Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){LabelSpan(item.bloodType?.name?:"","", maximumLines = 2)}
            Box(modifier=Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){Label("",text=localDate.toString())}
        }
    }
}
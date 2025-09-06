package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan

@Composable
fun BloodDailyStockCard(item:DailyBloodStock){
    val bloodGroup=item.bloodGroup
    val unitType=item.bloodUnitType
    val amount = item.amount
    val underInspection=item.underInspection
    ColumnContainer {
        if((underInspection?:0)==0)
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            Label(unitType?.name?:"")
            HorizontalSpacer()
            LabelSpan(label = bloodGroup?.name?:"", value = amount.toString(), spanColor = if((amount?:0)>0) BLUE else Color.Red)
        }else{
            LabelSpan(label="Under Inspection", value = underInspection.toString())}
    }
}
package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun KpiCard(item: BloodBankKpi){
    val kpiItem=item.item
    val value=item.value
    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Row(){
                Label(kpiItem?.name?: EMPTY_STRING)
                HorizontalSpacer()
                Label("(${kpiItem?.target?:0f})")
            }
            Span(" $value ", backgroundColor = BLUE, color = WHITE, startPadding = 5, endPadding = 5)
        }
    }
}
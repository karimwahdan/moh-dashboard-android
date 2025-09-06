package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.incineration.views.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun IncinerationCard(item:MonthlyIncineration){
    val bloodGroup=item.bloodGroup
    val unitType=item.bloodUnitType
    val reason=item.reason
    val value=item.value
    val date="${item.year} - ${item.month}"
    val campaign=item.campaign
    ColumnContainer {
        LabelSpan(label=date, value = reason?.name?:"", spanColor = ORANGE)
        Row(verticalAlignment = Alignment.CenterVertically){
            Row {
                Label(bloodGroup?.name?:"")
                HorizontalSpacer()
                Label(unitType?.name?:"N/A")
                HorizontalSpacer()
                Span(text="$value",color= WHITE, backgroundColor = Color.Red)
            }
        }
    }
}
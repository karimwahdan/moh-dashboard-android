package com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequency
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun RenalWashFrequencyCard(item:RenalWashFrequency){
    val day=item.day
    val sessions=item.sessions
    ColumnContainer {
        Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically,){
                Icon(icon=R.drawable.ic_info_white,
                    background = colorResource(R.color.teal_700),
                    containerSize = 26)
                HorizontalSpacer()
                Label(day?:"")
            }

            Span("${sessions?:""}", backgroundColor = BLUE, color = WHITE)
        }
    }
}
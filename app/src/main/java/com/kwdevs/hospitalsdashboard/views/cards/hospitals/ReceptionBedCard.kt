package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBed
import com.kwdevs.hospitalsdashboard.views.assets.BEDS_COUNT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label

@Composable
fun ReceptionBedCard(item:ReceptionBed){
    val beds=item.beds
    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Icon(R.drawable.ic_info_white, background = BLUE)
            Label(BEDS_COUNT_LABEL,"${beds?:0}")
        }
    }
}
package com.kwdevs.hospitalsdashboard.views.cards.hospitals.devices.usage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceDailyUsage
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun DeviceUsageCard(item:HospitalDeviceDailyUsage,showEditButton:Boolean=false,onClick:()->Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Label(text = "${item.day}")
            Span(text = "${item.usage}", backgroundColor = BLUE, color = WHITE)
        }
        if(showEditButton) IconButton(R.drawable.ic_edit_blue, onClick = onClick)

    }
}
package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor

@Composable
fun MorgueCard(item:Morgue){
    val type=item.type
    val status=item.status
    val totalUnits=item.allUnits

    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            type?.let { Label(it.name) }
            status?.let {
                HorizontalSpacer()
                Span(it.name, backgroundColor = hexToComposeColor(it.color), color = WHITE)
            }
            totalUnits?.let {
                HorizontalSpacer(20)
                Label(label = TOTAL_UNITS_LABEL, text = "$it") }

        }
    }
}
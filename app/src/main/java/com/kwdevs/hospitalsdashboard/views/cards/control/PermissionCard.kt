package com.kwdevs.hospitalsdashboard.views.cards.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE


@Composable
fun PermissionCard(item: Permission){
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .background(WHITE).padding(vertical = 5.dp)
        .border(width = 1.dp, shape = RectangleShape, color = GRAY)){
        Span(text =  item.name?: EMPTY_STRING, backgroundColor = BLUE, color = WHITE, maximumLines = 3)
        Label(text =SLUG_LABEL, maximumLines = 3, fontSize = 12)
        Span(text =  item.slug?: EMPTY_STRING, backgroundColor = ORANGE, color = WHITE, maximumLines = 3)
    }

}
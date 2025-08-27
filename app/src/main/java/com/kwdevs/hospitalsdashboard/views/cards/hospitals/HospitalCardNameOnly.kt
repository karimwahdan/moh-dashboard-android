package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun HospitalCardNameOnly(item: Hospital, modifier: Modifier = Modifier, onCLick:()->Unit){
    Column(
        modifier = modifier
            .padding(5.dp)
            .shadow(elevation = 5.dp, shape = rcs(5))
            .background(color= Color.White, shape = rcs(5))
            .border(width = 1.dp, shape = rcs(5), color = Color.LightGray)
            .clickable { onCLick.invoke() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        VerticalSpacer()
        Icon(R.drawable.ic_hospital_white,size=32, containerSize = 36, background = colorResource(R.color.blue))
        Label(item.name, paddingStart = 5, paddingEnd = 5, paddingBottom = 5, paddingTop = 5)
        Span(text=if(item.active==true) "Active" else "In-Active",
            backgroundColor = if(item.active==true) GREEN else Color.Red,
            color = WHITE)
        VerticalSpacer()
    }
}

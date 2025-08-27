package com.kwdevs.hospitalsdashboard.views.cards.sector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun SectorWithCountCard(item: Sector, modifier: Modifier = Modifier, onCLick:()->Unit){
    Column(
        modifier = modifier
            .shadow(elevation = 5.dp, shape = rcs(5))
            .background(color= Color.White, shape = rcs(5))
            .border(width = 1.dp, shape = rcs(5), color = Color.LightGray)
            .clickable { onCLick.invoke() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        VerticalSpacer()
        Icon(R.drawable.ic_home_work_white,size=32, containerSize = 36, background = colorResource(R.color.blue))
        Label(item.name, fontSize = 14,
            maximumLines = 1, textOverflow = TextOverflow.Ellipsis, softWrap = true)
        VerticalSpacer()
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Label("المستشفيات")
                Span("${item.hospitalsCount}", color = Color.White, backgroundColor = if((item.hospitalsCount?:0)>0) GREEN else Color.Red)
            }
        }
        VerticalSpacer()
    }
}
package com.kwdevs.hospitalsdashboard.views.cards.control

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer


@Composable
fun ModelCard(item: CustomModel, onClick:()->Unit){
    val name=item.name
    val columns=item.columns
    var show by remember { mutableStateOf(false) }
    ColumnContainer {
        Row(modifier= Modifier.fillMaxWidth()
            .background(color= colorResource(R.color.blue3))
            .clickable { show=!show },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(name, fontWeight = FontWeight.Bold,
                    color = Color.White, paddingStart = 10, paddingEnd = 10)
                HorizontalSpacer()
                IconButton(R.drawable.ic_edit_blue, paddingTop = 5, paddingBottom = 5, paddingStart = 5, paddingEnd = 5, onClick = onClick)
            }
            Box(modifier= Modifier.padding(vertical = 5.dp,horizontal = 10.dp), contentAlignment = Alignment.Center){
                Icon(if(show) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26,
                    background = colorResource(R.color.blue3)
                )
            }
        }
        AnimatedVisibility(visible = show,
            enter=  fadeIn() + expandVertically(),
            exit =  shrinkVertically() + fadeOut(),) {
            Column(modifier= Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    columns.forEachIndexed { i, column ->
                        if(i<4) Box { Span(column, color = colorResource(R.color.white), backgroundColor = colorResource(
                            R.color.blue)
                        ) }
                    }
                }

                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    columns.forEachIndexed { i, column ->
                        if(i in 4..7) Box { Span(column, color = colorResource(R.color.white), backgroundColor = colorResource(
                            R.color.blue)
                        ) }
                    }
                }

                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    columns.forEachIndexed { i, column ->
                        if(i in 8..11) Box { Span(column, color = colorResource(R.color.white), backgroundColor = colorResource(
                            R.color.blue)
                        ) }
                    }
                }

                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    columns.forEachIndexed { i, column ->
                        if(i in 12..15) Box { Span(column, color = colorResource(R.color.white), backgroundColor = colorResource(
                            R.color.blue)
                        ) }
                    }
                }

                VerticalSpacer()
            }
        }
        if(show) VerticalSpacer()

    }
}

package com.kwdevs.hospitalsdashboard.views.cards.control

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.views.assets.ACTION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.COLUMNS_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor
import com.kwdevs.hospitalsdashboard.views.rcs


@Composable
fun PermissionCard(item: Permission){
    val model=item.model
    val columns=item.columns
    val actionType=item.actionType
    Column(modifier= Modifier.padding(5.dp).border(width = 1.dp, shape = RectangleShape, color = GRAY)){
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround){
            Row{
                HorizontalSpacer()
                Label(text= NAME_LABEL)
                HorizontalSpacer()
                Span(text=item.name?:"", color = WHITE, backgroundColor = BLUE)

            }
            Row{
                HorizontalSpacer()
                Label(text= SLUG_LABEL)
                HorizontalSpacer()
                Span(text=item.slug?:"", color = WHITE, backgroundColor = BLUE)

            }
        }
        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround){
            Row{
                HorizontalSpacer()
                Label(text= TABLE_LABEL)
                HorizontalSpacer()
                Span(text=model?.name?:"", color = WHITE, backgroundColor = BLUE)

            }
            Row{
                HorizontalSpacer()
                Label(text= ACTION_TYPE_LABEL)
                HorizontalSpacer()
                actionType?.let{
                    Span(text=it.name?:"",
                        color = hexToComposeColor(it.fontColor),
                        backgroundColor = hexToComposeColor(it.color))

                }
            }
        }


        if(!columns.isNullOrEmpty()){
            Row(modifier= Modifier.fillMaxWidth()){
                Column(modifier= Modifier.fillMaxWidth().padding(5.dp).weight(1f)
                    .border(width = 1.dp, shape = rcs(5),color= GRAY))
                {
                    Label(COLUMNS_NAME_LABEL, paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5)
                    Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                        columns.forEachIndexed { index, column ->
                            if(index in 0..2){
                                Row{
                                    Span(text=column, color = Color.White, backgroundColor = colorResource(
                                        R.color.teal_700)
                                    )
                                    HorizontalSpacer()
                                }
                            }
                        }
                    }
                    VerticalSpacer()
                    if(columns.size>2){
                        Row(modifier= Modifier.padding(horizontal = 5.dp)){
                            columns.forEachIndexed { index, column ->
                                if(index in 3..5){
                                    Row{
                                        Span(text=column, color = Color.White, backgroundColor = colorResource(
                                            R.color.teal_700)
                                        )
                                        HorizontalSpacer()
                                    }
                                }
                            }
                        }
                        VerticalSpacer()
                    }
                    if(columns.size>5){
                        Row(modifier= Modifier.padding(horizontal = 5.dp)){
                            columns.forEachIndexed { index, column ->
                                if(index in 6..8){
                                    Row{
                                        Span(text=column, color = Color.White, backgroundColor = colorResource(
                                            R.color.teal_700)
                                        )
                                        HorizontalSpacer()
                                    }
                                }
                            }
                        }
                        VerticalSpacer()
                    }
                    if(columns.size>8){
                        Row(modifier= Modifier.padding(horizontal = 5.dp)){
                            columns.forEachIndexed { index, column ->
                                if(index in 9..11){
                                    Row{
                                        Span(text=column, color = Color.White, backgroundColor = colorResource(
                                            R.color.teal_700)
                                        )
                                        HorizontalSpacer()
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }
}
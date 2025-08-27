package com.kwdevs.hospitalsdashboard.views.assets.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun PaginationContainer(shape: Shape= RectangleShape,
                        currentPage:MutableIntState,
                        lastPage:Int,
                        totalItems:Int,
                        borderColor: Color= colorResource(R.color.teal_700),
                        showFilterButton:Boolean=false,
                        onFilterClick:()->Unit={},
                        content:@Composable ()->Unit){
    Column(modifier=Modifier.fillMaxSize().padding(5.dp)
        .shadow(elevation = 5.dp,shape=shape)
        .background(color= Color.White, shape = shape)
        .border(width = 1.dp, shape = shape, color = borderColor)
        .padding(5.dp)){
        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Label(" اجمالى المدخلات في الصفحة: $totalItems")
            if(showFilterButton){
                IconButton(R.drawable.ic_filter_white,
                    background = colorResource(R.color.teal_700), onClick = onFilterClick)

            }
        }
        HorizontalDivider()
        VerticalSpacer()
        Box(modifier=Modifier.fillMaxSize().weight(1f),
            contentAlignment = Alignment.Center){
            content()
        }
        HorizontalDivider()
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){

            IconButton(icon=R.drawable.ic_arrow_back_teal, containerSize = 28,onClick = {
                if(currentPage.intValue>1) currentPage.intValue--
            })

            Row(modifier=Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                HorizontalSpacer()
                if(lastPage<5){
                    for(i in 1..<lastPage){
                        Box(modifier=Modifier.size(36.dp)
                            .background(color = colorResource(if(currentPage.intValue==i) R.color.blue else R.color.black),
                                shape= CircleShape)
                            .clickable { currentPage.intValue=i },
                            contentAlignment = Alignment.Center){
                            Label("$i", color =  WHITE, fontSize = 16 )
                        }
                        HorizontalSpacer()
                    }
                }else{
                    for(i in 1..4){
                        Box(modifier=Modifier.size(36.dp)
                            .background(color = colorResource(if(currentPage.intValue==i) R.color.blue else R.color.black),
                                shape= CircleShape)
                            .clickable { currentPage.intValue=i },
                            contentAlignment = Alignment.Center){
                            Label("$i", color =  WHITE, fontSize = 16 )
                        }
                        HorizontalSpacer()
                    }
                }
                HorizontalSpacer()
            }

            IconButton(icon=R.drawable.ic_arrow_forward_teal, containerSize = 28,onClick = {
                if(currentPage.intValue<lastPage) currentPage.intValue++
            })


        }

    }
}
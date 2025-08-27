package com.kwdevs.hospitalsdashboard.views.assets.basicSceens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.TRY_AGAIN_LATER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun FailScreen(modifier:Modifier=Modifier,label:String=ERROR_LOADING_DATA_LABEL){
    Column(modifier=modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label(label, color = Color.Red, maximumLines = Int.MAX_VALUE)
        VerticalSpacer()
        Label(TRY_AGAIN_LATER_LABEL, color = Color.Red)
    }
}

@Composable
fun FailScreen(modifier:Modifier=Modifier,label:String=ERROR_LOADING_DATA_LABEL,
               message:String?,
               errors:Map<String,List<String>>?){
    Column(modifier=modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier=Modifier.size(144.dp).background(color= WHITE, shape = CircleShape)
                .clip(CircleShape),
            painter = painterResource(R.drawable.img_wrong_account),
            contentScale = ContentScale.FillBounds,
            contentDescription = null)
        Label(label, color = Color.Red)
        VerticalSpacer()
        message?.let{ Label(message, color = Color.Red) }
        VerticalSpacer()
        LazyColumn {
            item{
                errors?.let{errors->
                    errors.forEach{error->
                        ErrorWidget(error.key,error.value)
                        VerticalSpacer()
                    }

                }
            }
        }
    }
}

@Composable
fun ErrorWidget(key:String,errors:List<String>){
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .border(width = 1.dp, shape = rcs(20), color = colorResource(R.color.gray))){
        Label(paddingStart = 10, paddingEnd = 5,text=key, color = colorResource(R.color.red), fontSize = 16, fontWeight = FontWeight.Bold)
        errors.forEach { error->
            Label(paddingStart = 5, paddingEnd = 5,text=error, color = colorResource(R.color.darkRed), fontSize = 14, fontWeight = FontWeight.Normal)
            VerticalSpacer()
        }
        VerticalSpacer(5)
    }
}
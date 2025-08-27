package com.kwdevs.hospitalsdashboard.views.assets.basicSceens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.assets.LOADING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer

@Composable
fun LoadingScreen(modifier: Modifier =Modifier){
    Column(modifier=modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            modifier=Modifier.size(72.dp),
            painter = painterResource(R.drawable.logo),
            contentScale = ContentScale.FillBounds,
            contentDescription = null)
        CircularProgressIndicator(color = colorResource(R.color.teal_700))
        VerticalSpacer()
        Label(LOADING_LABEL)
    }
}
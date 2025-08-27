package com.kwdevs.hospitalsdashboard.views.assets.basicSceens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SuccessScreen(modifier: Modifier = Modifier,content:@Composable ()->Unit){
    Column(modifier=modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        content()
    }
}
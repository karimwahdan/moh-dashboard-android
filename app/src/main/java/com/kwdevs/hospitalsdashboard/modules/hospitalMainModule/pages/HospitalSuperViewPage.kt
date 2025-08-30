package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.HIDE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THE_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalSuperViewPage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    val showSheet = remember { mutableStateOf(false) }
    var showModal by remember { mutableStateOf(false) }
    Container(
        title = "$THE_HOSPITAL_LABEL ${hospital?.name?: EMPTY_STRING}",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HomeRoute.route)}
    ) {
        Row(modifier=Modifier.fillMaxWidth().background(color= ORANGE).clickable { showModal=!showModal },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Label(if(showModal)SHOW_LABEL else HIDE_LABEL, paddingStart = 5, paddingEnd = 5, paddingBottom = 5, paddingTop = 5)
            Box(modifier=Modifier.padding(5.dp), contentAlignment = Alignment.Center){
                Icon(if(showModal)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white)
            }
        }
        AnimatedVisibility(visible = showModal) {
            Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)) {

            }
        }
    }
}
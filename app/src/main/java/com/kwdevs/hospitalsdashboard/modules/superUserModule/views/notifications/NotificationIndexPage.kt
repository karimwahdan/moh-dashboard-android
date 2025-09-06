package com.kwdevs.hospitalsdashboard.modules.superUserModule.views.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.modules.superUserModule.routes.NotificationCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.NOTIFICATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIndexPage(navHostController: NavHostController){
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = NOTIFICATIONS_LABEL,
        showSheet = showSheet
    ) {
        Column(modifier=Modifier.fillMaxSize()){
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                CustomButton(label = "Send") {
                    navHostController.navigate(NotificationCreateRoute.route)
                }

            }

        }
    }
}
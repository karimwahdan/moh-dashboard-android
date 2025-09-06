package com.kwdevs.hospitalsdashboard.views.assets.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_PROMPT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.YES_LABEL
import com.kwdevs.hospitalsdashboard.views.deleteData
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun SignOutDialogBox(showDialog:MutableState<Boolean>,navHostController: NavHostController){

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                VerticalSpacer()
                Label(EXIT_LABEL)
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                    Label(EXIT_PROMPT_LABEL)
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    CustomButton(label = NO_LABEL,
                        buttonShape = rcs(15),
                        borderColor = colorResource(R.color.light_green),
                        hasBorder = true,
                        enabledBackgroundColor = Color.Transparent,
                        enabledFontColor = colorResource(R.color.green)) {
                        showDialog.value=false
                    }
                    CustomButton(label = YES_LABEL,
                        buttonShape = rcs(15),
                        enabledFontColor = WHITE,
                        enabledBackgroundColor = colorResource(R.color.red4)) {
                        deleteData()
                        navHostController.navigate(LoginRoute.route)
                        showDialog.value=false
                    }
                }
            }
        }
    }
}
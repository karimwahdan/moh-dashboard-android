package com.kwdevs.hospitalsdashboard.views.pages.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SETTINGS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPAN_FONT_SIZE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val userType=Preferences.User().getType()
    val superUser=Preferences.User().getSuper()
    val spanFontSize = remember { mutableStateOf(EMPTY_STRING) }
    val showSheet = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        spanFontSize.value=Preferences.FontSettings.SpanSettings().get().toString()
    }
    Container(
        title = SETTINGS_LABEL,
        showSheet = showSheet,
        headerOnClick = { navHostController.navigate(when(userType){
                ViewType.HOSPITAL_USER->HospitalHomeRoute.route
                ViewType.SUPER_USER->HomeRoute.route
                else->LoginRoute.route
            }) },
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE
    ) {
        Column(){
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item{
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier= Modifier.weight(1f)){
                            Column(){
                                Row(verticalAlignment = Alignment.CenterVertically){}
                                Label(SPAN_FONT_SIZE_LABEL, paddingStart = 5, paddingEnd = 5)
                                Span(text = SPAN_FONT_SIZE_LABEL, backgroundColor = BLUE, color = WHITE, paddingStart = 5, paddingEnd = 5,
                                    fontSize = if(spanFontSize.value.toIntOrNull()!=null) spanFontSize.value.toInt() else 14)

                                CustomInput(value = spanFontSize, label = SPAN_FONT_SIZE_LABEL, keyboardOptions = numericKeyBoard,
                                    onTextChange = {text->if(text.toIntOrNull()!=null) spanFontSize.value=text else spanFontSize.value=EMPTY_STRING})
                            }
                        }
                        IconButton(R.drawable.ic_check_circle_green) {
                            if(spanFontSize.value.toIntOrNull()!=null){
                                Preferences.FontSettings.SpanSettings().set(spanFontSize.value.toInt())
                            }
                        }
                    }
                    VerticalSpacer()

                }
            }
        }
    }
}
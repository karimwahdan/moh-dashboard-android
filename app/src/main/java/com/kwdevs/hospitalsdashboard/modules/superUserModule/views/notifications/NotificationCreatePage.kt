package com.kwdevs.hospitalsdashboard.modules.superUserModule.views.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.views.assets.ALL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HIDE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NOTIFICATION_MESSAGE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NOTIFICATION_TITLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SEND_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TITLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcsB

@Composable
fun NotificationCreatePage(navHostController: NavHostController){
    val settingsController:SettingsController= viewModel()
    val allowedReceiversState by settingsController.allowedReceiversState.observeAsState()
    val title                   =   remember { mutableStateOf(EMPTY_STRING) }
    val message                 =   remember { mutableStateOf(EMPTY_STRING) }
    var hospitalUsers           by  remember { mutableStateOf<List<SimpleHospitalUser>>(emptyList()) }
    var selectedReceivers       by remember { mutableStateOf<List<SimpleHospitalUser>>(emptyList()) }

    var showReceiversList       by remember { mutableStateOf(false) }

    when(allowedReceiversState){
        is UiState.Success->{
            val s = allowedReceiversState as UiState.Success<HospitalUsersSimpleResponse>
            val r =s.data
            val data=r.data
            hospitalUsers=data
        }
        else->{settingsController.allowedNotificationReceiversList()}
    }
    ColumnContainer {
        Column(modifier=Modifier.fillMaxSize()) {
            LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                item{
                    if(hospitalUsers.isNotEmpty()){
                        CustomInput(value = title, label = NOTIFICATION_TITLE_LABEL)
                        CustomInput(value = message, label = NOTIFICATION_MESSAGE_LABEL)
                        Row(modifier=Modifier.fillMaxWidth().clickable { showReceiversList=!showReceiversList }.background(BLUE).padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically){
                            Label(if(showReceiversList) HIDE_LABEL else SHOW_LABEL, color = WHITE)
                            Icon(background = BLUE,icon=if(showReceiversList)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white)
                        }
                        AnimatedVisibility(visible = showReceiversList){
                            ColumnContainer(shape = rcsB(20)) {
                                Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                    CustomCheckbox(label = ALL_LABEL, active = selectedReceivers.size==hospitalUsers.size, onCheckChange = {
                                        if(it){selectedReceivers=hospitalUsers}
                                        else{selectedReceivers= emptyList()}
                                    })
                                    hospitalUsers.forEach { r->
                                        CustomCheckbox(label = r.name,active=selectedReceivers.contains(r), onCheckChange = {
                                            if(!it){
                                                selectedReceivers=selectedReceivers.filter { f->f!=r }
                                            }else{
                                                val newList= mutableListOf<SimpleHospitalUser>()
                                                newList.addAll(selectedReceivers)
                                                if(!newList.contains(r)) newList.add(r)
                                                selectedReceivers=newList
                                            }
                                        })
                                    }

                                }
                            }
                        }
                    }

                }
            }
            Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                CustomButton(label = SEND_LABEL,
                    enabledBackgroundColor = Color.Transparent,
                    enabledFontColor = GREEN,
                    borderColor = GREEN,
                    hasBorder = true) {
                }
                CustomButton(label = CANCEL_LABEL,
                    enabledBackgroundColor = Color.Transparent,
                    enabledFontColor = Color.Red,
                    borderColor = Color.Red,
                    hasBorder = true) {

                }
            }

        }
    }
}
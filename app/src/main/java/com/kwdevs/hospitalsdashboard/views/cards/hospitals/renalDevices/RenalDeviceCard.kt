package com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalWashFrequencyBody
import com.kwdevs.hospitalsdashboard.controller.hospital.RenalDevicesController
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequency
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DELETE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_WASH_SESSIONS_FREQUENCY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@Composable
fun RenalDeviceCard(item: HospitalRenalDevice,controller: RenalDevicesController){
    val name=item.name
    val type=item.type
    val active=if (item.active)R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red
    val showFrequenciesDialog= remember { mutableStateOf(false) }
    FrequenciesDialog(showDialog =  showFrequenciesDialog,item= item, controller = controller)
    ColumnContainer{
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(icon=R.drawable.ic_info_white,
                    background = colorResource(R.color.teal_700),
                    containerSize = 26)
                HorizontalSpacer()
                Label(name?:"")
                HorizontalSpacer()
                Span(text=type?.name?:"", backgroundColor = BLUE, color = WHITE)
            }
            Icon(active)
        }
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
            verticalAlignment = Alignment.Top,){

            CustomButtonWithImage(icon=R.drawable.ic_edit_blue, label = EDIT_LABEL,
                iconSize = 26, maxWidth = 42) {  }
            CustomButtonWithImage(icon=R.drawable.ic_delete_red, label = DELETE_LABEL,
                iconSize = 26, maxWidth = 42) {  }
            CustomButtonWithImage(icon=R.drawable.ic_frequency, label = RENAL_WASH_SESSIONS_FREQUENCY_LABEL,
                iconSize = 26, maxWidth = 42) {
                showFrequenciesDialog.value=true
            }
        }
    }
}

@Composable
private fun FrequenciesDialog(showDialog:MutableState<Boolean>,
                              item: HospitalRenalDevice,
                              controller: RenalDevicesController
){

    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    var items by remember { mutableStateOf<List<RenalWashFrequency>>(emptyList()) }
    val state by controller.frequencyPaginatedState.observeAsState()
    val showStoreFrequencyDialog = remember { mutableStateOf(false) }


    StoreFrequencyDialog(showStoreFrequencyDialog,item,controller)
    if (showDialog.value){
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = state as UiState.Success<ApiResponse<PaginationData<RenalWashFrequency>>>
                val r = s.data
                val pagination=r.pagination
                lastPage=pagination.lastPage
                val data=pagination.data
                items=data
            }
            else->{ controller.indexFrequencyByDevice(page=currentPage.intValue,id=item.id?:0)}
        }
        Dialog(onDismissRequest = {
            showDialog.value=false
            controller.reload()
        }) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),Arrangement.End) {
                    IconButton(R.drawable.ic_cancel_red) {
                        showDialog.value=false
                        controller.reload()

                    }
                }
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),Arrangement.Center) {
                    CustomButton(label = ADD_NEW_LABEL, enabledBackgroundColor = BLUE, buttonShadowElevation = 6, buttonShape = RectangleShape) {
                        showStoreFrequencyDialog.value=true
                    }
                }
                PaginationContainer(
                    currentPage=currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        items(items){
                            RenalWashFrequencyCard(it)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreFrequencyDialog(showDialog: MutableState<Boolean>, item: HospitalRenalDevice,controller: RenalDevicesController){
    val user= Preferences.User().get()
    val sessions= remember { mutableStateOf("") }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }
    val state by controller.frequencySingleState.observeAsState()
    DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
    if(showDialog.value){
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    showDialog.value=false
                    sessions.value=""
                    enterTime.value=""
                    controller.indexFrequencyByDevice(1,item.id?:0)
                }
            }
            else->{}
        }

        Dialog(onDismissRequest = {showDialog.value=false;controller.reload()}) {
            ColumnContainer {
                VerticalSpacer()
                Label(item.name?:"N/A")
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                VerticalSpacer()
                if(enterTime.value.trim()!=""){
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Label(label = DATE_LABEL,text=enterTime.value)
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                        enabledBackgroundColor = ORANGE,
                        onClick = { showEnterTimePicker.value = !showEnterTimePicker.value })
                }
                CustomInput(value = sessions, label = RENAL_WASH_SESSIONS_FREQUENCY_LABEL)
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    CustomButton(label = SAVE_CHANGES_LABEL) {
                        val body = RenalWashFrequencyBody(
                            hospitalId = item.hospitalId,
                            deviceId = item.id,
                            day = enterTime.value,
                            sessions = sessions.value.toInt(),
                            createdById = user?.id
                        )
                        controller.storeFrequencyNormal(body)
                    }
                }
            }
        }
    }

}

package com.kwdevs.hospitalsdashboard.views.pages.hospitals.renalDevices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalDeviceBody
import com.kwdevs.hospitalsdashboard.controller.hospital.RenalDevicesController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceTypeResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices.RenalDeviceCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenalDevicesIndexPage(navHostController: NavHostController){

    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    var items by remember { mutableStateOf<List<HospitalRenalDevice>>(emptyList()) }
    val controller: RenalDevicesController = viewModel()
    val state by controller.paginatedState.observeAsState()
    val showNewDeviceDialog = remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<HospitalRenalDevice>>>
            val r = s.data
            val pagination=r.pagination
            val data = pagination.data
            lastPage=pagination.lastPage
            items = data
        }
        else->{controller.indexByHospital(currentPage.intValue)}
    }
    NewRenalDeviceDialog(showDialog = showNewDeviceDialog,controller )
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = RENAL_DEVICES_LABEL,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                buttonShape = RectangleShape,
                onClick = { showNewDeviceDialog.value=true })
        }

        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = items.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(items){ RenalDeviceCard(it,controller) }
            }
        }
    }
}

@Composable
private fun NewRenalDeviceDialog(showDialog:MutableState<Boolean>,controller: RenalDevicesController){
    val hospital=Preferences.Hospitals().get()
    val user = Preferences.User().get()
    val state by controller.singleState.observeAsState()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.deviceTypeState.observeAsState()
    val name = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(true) }
    var types by remember { mutableStateOf<List<DeviceType>>(emptyList()) }
    val selectedType = remember { mutableStateOf<DeviceType?>(null) }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<DeviceTypeResponse>
            val r = s.data
            val data = r.data
            types=data

        }
        else->{settingsController.renalDeviceTypesIndex()}
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                showDialog.value=false
                name.value=""
                controller.indexByHospital(1)
            }
        }
        else->{}
    }
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value}){
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.End){
                    IconButton(R.drawable.ic_cancel_red) {
                        showDialog.value=false
                    }
                }
                CustomInput(name, NAME_LABEL)
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(title = DEVICE_TYPE_LABEL,
                        loadedItems = types, selectedItem = selectedType,
                        selectedContent = {
                            CustomInput(selectedType.value?.name?: SELECT_DEPARTMENT_TYPE_LABEL)
                        }) {
                        Label(it?.name?:"")
                    }
                }
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    CustomButton(label = SAVE_CHANGES_LABEL) {
                        val body = RenalDeviceBody(
                            name = name.value,
                            hospitalId = hospital?.id,
                            deviceTypeId = selectedType.value?.id,
                            active = if(active.value) 1 else 0,
                            createdById = user?.id,
                        )
                        controller.storeNormal(body)
                    }
                }

            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalDeviceController
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.devices.HospitalDeviceCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalDevicesPage(navHostController: NavHostController){

    val devicesController:HospitalDeviceController= viewModel()
    var devices by remember { mutableStateOf<List<HospitalDevice>>(emptyList()) }
    val state by devicesController.state.observeAsState()
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<HospitalDevice>>>
            val r = s.data
            val paginationData=r.pagination
            val data=paginationData.data
            devices=data
            lastPage=paginationData.lastPage
        }
        else->{devicesController.index(currentPage.intValue)}
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = DEVICES_LABEL,
        headerShowBackButton = true,
        showSheet = showSheet,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}) {
        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = devices.size
        ) {
            Column {
                Row(modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = ADD_NEW_LABEL, buttonShape = RectangleShape,
                        enabledBackgroundColor = GREEN) {
                        navHostController.navigate(HospitalDeviceCreateRoute.route)
                    }
                }
                LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                    items(devices){
                        HospitalDeviceCard(it,navHostController)
                    }
                }

            }
        }
    }
}
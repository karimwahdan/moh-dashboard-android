package com.kwdevs.hospitalsdashboard.views.pages.hospitals.operationRooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.OperationRoomBody
import com.kwdevs.hospitalsdashboard.controller.hospital.OperationRoomController
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoomSingleResponse
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.MAJOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_OPERATION_ROOMS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationRoomCreatePage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    var item by remember { mutableStateOf<OperationRoom?>(null) }
    val controller:OperationRoomController= viewModel()
    val state by controller.singleState.observeAsState()
    val name = remember { mutableStateOf("") }
    val major = remember { mutableStateOf(false) }
    val active = remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<OperationRoomSingleResponse>
            val r = s.data
            val data=r.data
            item=data
            LaunchedEffect(Unit) { navHostController.navigate(OperationRoomIndexRoute.route) }
        }
        else->{}
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = NEW_OPERATION_ROOMS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)},
        showSheet = showSheet
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(5.dp)){

            CustomInput(name, NAME_LABEL)

            VerticalSpacer()

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                CustomCheckbox(MAJOR_LABEL,major)
                CustomCheckbox(ACTIVE_LABEL,active)
            }

            VerticalSpacer()

            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label= SAVE_CHANGES_LABEL, enabledBackgroundColor = GREEN) {
                    val body = OperationRoomBody(
                        hospitalId =hospital?.id ,
                        name=name.value,
                        major = if(major.value) 1 else 0,
                        active = if(active.value) 1 else 0
                    )
                    controller.store(body)
                }
            }

        }
    }
}
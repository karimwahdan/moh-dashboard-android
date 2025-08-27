package com.kwdevs.hospitalsdashboard.views.pages.hospitals.morgues

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.MorgueBody
import com.kwdevs.hospitalsdashboard.controller.hospital.MorgueController
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.MorgueSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.MorgueType
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.responses.options.MorgueOptionsData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.MorguesIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NEW_MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorgueCreatePage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val controller:MorgueController= viewModel()
    val state by controller.singleState.observeAsState()
    val optionsState by controller.optionsState.observeAsState()
    var item by remember { mutableStateOf<Morgue?>(null) }
    var statuses by remember { mutableStateOf<List<Status>>(emptyList()) }
    val selectedStatus = remember { mutableStateOf<Status?>(null) }
    var types by remember { mutableStateOf<List<MorgueType>>(emptyList()) }
    val selectedType= remember { mutableStateOf<MorgueType?>(null) }
    val totalUnits = remember { mutableStateOf("") }

    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<MorgueOptionsData>
            val r = s.data
            val data=r.data
            statuses=data.statuses
            types=data.types
        }
        else->{
            controller.options()
        }
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<MorgueSingleResponse>
            val r = s.data
            val data=r.data
            item=data
            LaunchedEffect(Unit) {
                navHostController.navigate(MorguesIndexRoute.route)
            }
        }
        else->{}
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = NEW_MORGUE_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            navHostController.navigate(HospitalHomeRoute.route)}) {
        Column(modifier=Modifier.fillMaxWidth()){
            VerticalSpacer()
            //TODO MODEL SELECTOR
            Box(modifier=Modifier.padding(5.dp)){
                ComboBox(title = HOSPITAL_TYPE_LABEL,
                    loadedItems = types, selectedItem = selectedType, selectedContent = {
                        CustomInput(selectedType.value?.name?: SELECT_TYPE_LABEL)
                    }) {
                    Label(it?.name?:"")
                }

            }
            Box(modifier=Modifier.padding(5.dp)){
                ComboBox(title = DEVICE_STATUS_LABEL,
                    loadedItems = statuses, selectedItem = selectedStatus, selectedContent = {
                        CustomInput(selectedStatus.value?.name?: SELECT_DEVICE_STATUS_LABEL)
                    }) {
                    Label(it?.name?:"")
                }

            }
            Box(modifier=Modifier.padding(5.dp)){
                CustomInput(value = totalUnits, label = TOTAL_UNITS_LABEL)
            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label = SAVE_CHANGES_LABEL, enabledBackgroundColor = GREEN, buttonShape = RectangleShape) {
                    if(selectedStatus.value!=null && selectedType.value!=null && totalUnits.value.trim()!=""){
                        val body= MorgueBody(
                            hospitalId = hospital?.id,
                            typeId = selectedType.value?.id,
                            statusId = selectedStatus.value?.id,
                            allUnits = totalUnits.value.toInt(),
                            createdById = user?.id
                        )
                        controller.storeByNormalUser(body)
                    }
                }
            }

        }
    }

}
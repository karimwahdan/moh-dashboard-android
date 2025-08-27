package com.kwdevs.hospitalsdashboard.views.pages.hospitals.wards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalWardBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalWardController
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardTypeResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalWardSingleResponse
import com.kwdevs.hospitalsdashboard.routes.NormalUserWardsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_WARD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardCreatePage(navHostController: NavHostController){

    val controller:HospitalWardController= viewModel()
    val state by controller.singleState.observeAsState()
    val hospital = Preferences.Hospitals().get()
    val normalUser = Preferences.User().get()
    val settingsController: SettingsController = viewModel()
    val settingsState by settingsController.wardTypeState.observeAsState()
    val name = remember { mutableStateOf("") }
    var wardTypes by remember { mutableStateOf<List<WardType>>(emptyList()) }
    val selectedWardType = remember { mutableStateOf<WardType?>(null) }
    val allUnits = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    when(settingsState){
        is UiState.Loading->{loading=true}
        is UiState.Error->{loading=false}
        is UiState.Success->{
            loading=false
            val s = settingsState as UiState.Success<WardTypeResponse>
            val r = s.data
            val data = r.data
            wardTypes=data
        }
        else->{ settingsController.wardTypesIndex() }
    }
    when(state){
        is UiState.Loading->{loading=true}
        is UiState.Error->{loading=false}
        is UiState.Success->{
            loading=false
            val s = state as UiState.Success<HospitalWardSingleResponse>
            val r = s.data
            val data = r.data
            LaunchedEffect(Unit) { if(data!=null) navHostController.navigate(NormalUserWardsIndexRoute.route) }

        }
        else->{ }
    }
    val showSheet = remember { mutableStateOf(false) }
    Container(title = NEW_WARD_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(NormalUserWardsIndexRoute.route)}) {
        CustomInput(value = name, label = NAME_LABEL)
        Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                ComboBox(hasTitle = false,selectedItem = selectedWardType, loadedItems = wardTypes, selectedContent = {
                    CustomInput(value=if(selectedWardType.value==null) "Select Ward Type" else selectedWardType.value?.name?:"",
                        readOnly = true,
                        icon = R.drawable.ic_arrow_drop_down_blue)
                }) {
                    Label(
                        it?.name?:"",
                        color = if(selectedWardType.value==it) BLUE else BLACK
                    )
                }
            }
            if(selectedWardType.value!=null){
                IconButton(icon= R.drawable.ic_cancel_red) { selectedWardType.value=null}
            }
            else{
                Box(modifier= Modifier.width(26.dp))
            }
        }
        CustomInput(value = allUnits, label = TOTAL_UNITS_LABEL)
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Start){
            CustomCheckbox(label = ACTIVE_LABEL,active = active)
        }
        Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = SAVE_CHANGES_LABEL
                , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
            ){
                val body = HospitalWardBody(
                    hospitalId = hospital?.id,
                    typeId = selectedWardType.value?.id,
                    name = name.value,
                    allUnits = allUnits.value.toInt(),
                    createdById = normalUser?.id,
                    active = if(active.value) 1 else 0,
                )
                controller.storeNormal(body)

            }
        }
    }
}
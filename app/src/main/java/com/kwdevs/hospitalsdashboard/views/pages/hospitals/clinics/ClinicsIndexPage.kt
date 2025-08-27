package com.kwdevs.hospitalsdashboard.views.pages.hospitals.clinics

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.HospitalClinicBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalClinicController
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.settings.generalClinics.GeneralClinic
import com.kwdevs.hospitalsdashboard.models.settings.generalClinics.GeneralClinicResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CLINICS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_CLINIC_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.clinics.ClinicCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicsIndexPage(navHostController: NavHostController){

    val showStoreDialog = remember { mutableStateOf(false) }
    val controller:HospitalClinicController= viewModel()
    val state by controller.paginationState.observeAsState()
    var items by remember { mutableStateOf<List<HospitalClinic>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<HospitalClinic>>>
            val r = s.data
            val pagination=r.pagination
            lastPage=pagination.lastPage
            items=pagination.data
        }
        else->{
            controller.indexByHospital(currentPage.intValue)
        }
    }
    NewHospitalClinicDialog(showStoreDialog,controller)
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = CLINICS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            navHostController.navigate(HospitalHomeRoute.route)
        }
    ) {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL,
                buttonShadowElevation = 6,
                buttonShape = RectangleShape,
                enabledBackgroundColor = BLUE) {
                showStoreDialog.value=true
            }
        }
        Box(modifier=Modifier.padding(5.dp),contentAlignment = Alignment.Center){
            PaginationContainer(
                currentPage = currentPage,
                lastPage = lastPage,
                totalItems = items.size
            ) {
                LazyColumn(modifier=Modifier.fillMaxSize()) {
                    items(items){
                        ClinicCard(it,controller)
                    }
                }
            }

        }
    }
}

@Composable
fun NewHospitalClinicDialog(showDialog:MutableState<Boolean>,controller: HospitalClinicController){
    val hospital= Preferences.Hospitals().get()
    val user= Preferences.User().get()
    val settingsController: SettingsController = viewModel()
    val clinicTypesState by settingsController.generalClinicsStatesState.observeAsState()
    var generalClinics by remember { mutableStateOf<List<GeneralClinic>>(emptyList()) }
    val selectedClinic = remember { mutableStateOf<GeneralClinic?>(null) }
    val state by controller.singleState.observeAsState()
    when(clinicTypesState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = clinicTypesState as UiState.Success<GeneralClinicResponse>
            val r = s.data
            val data = r.data
            generalClinics=data

        }
        else->{settingsController.generalClinicsIndex()}
    }

    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                controller.indexByHospital(1)
                showDialog.value=false
            }

        }
        else->{}
    }
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.End){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(selectedItem = selectedClinic, loadedItems = generalClinics,
                        selectedContent = {
                            CustomInput(selectedClinic.value?.name?:SELECT_CLINIC_LABEL)
                        }) {
                        Label(it?.name?:"")
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    CustomButton(label = SAVE_CHANGES_LABEL) {
                        val body = HospitalClinicBody(
                            hospitalId = hospital?.id,
                            generalClinicId = selectedClinic.value?.id,
                            createdById = user?.id
                        )
                        controller.store(body)
                    }
                }
            }
        }

    }
}
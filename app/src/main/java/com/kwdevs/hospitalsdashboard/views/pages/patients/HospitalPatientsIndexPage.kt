package com.kwdevs.hospitalsdashboard.views.pages.patients

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.PatientViewType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_FILTER_PROMPT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DIE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_QUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.patients.PatientCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalPatientsIndexPage(navHostController: NavHostController){

    val controller: PatientsController = viewModel()
    val state by controller.state.observeAsState()
    val patients = remember { mutableStateOf<List<Patient>>(emptyList())}
    var allPatients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(1) }
    val viewType=Preferences.ViewTypes().getPatientViewType()
    val showFilterDialog= remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<Patient>>>
            val r = s.data
            val pagination = r.pagination
            patients.value=pagination.data
            allPatients=pagination.data
            totalPages=pagination.lastPage
        }
        else->{
            Log.e("ViewType","HospitalPatientsIndexPage: $viewType")
            LaunchedEffect(Unit) {
                when(viewType){
                    PatientViewType.BY_HOSPITAL->{controller.indexByHospital(currentPage.intValue)}
                    PatientViewType.BY_WARD->{controller.indexByWard(currentPage.intValue)}
                    PatientViewType.BY_OPERATION_ROOM->{controller.indexByOperationRoom(currentPage.intValue)}
                    else->{controller.indexByHospital(currentPage.intValue)}
                }

            }

        }
    }
    FilterPatientsDialog(showFilterDialog,controller,allPatients,patients)
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = PATIENTS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)},
    ) {
        PaginationContainer(
            shape = RectangleShape,
            currentPage = currentPage,
            lastPage = totalPages,
            showFilterButton = true,
            onFilterClick = {showFilterDialog.value=true},
            totalItems = patients.value.size) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(patients.value){ PatientCard(it,navHostController) }
            }
        }
    }
}

@Composable
private fun FilterPatientsDialog(showDialog:MutableState<Boolean>,controller: PatientsController,all:List<Patient>,filtered:MutableState<List<Patient>>){

    val columns= listOf("By Patient's Name","By National ID","By Code")
    val selectedColumn = remember { mutableStateOf(columns[columns.size-1]) }
    val searchValue= remember { mutableStateOf("") }
    val patientDied = remember { mutableStateOf(false) }
    val patientQuit = remember { mutableStateOf(false) }

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Column(modifier=Modifier.padding(5.dp)) {
                    Label(PATIENTS_FILTER_PROMPT_LABEL,
                        maximumLines = 3)
                    Box(modifier=Modifier.padding(horizontal = 5.dp)){
                        ComboBox(selectedItem = selectedColumn, loadedItems = columns, selectedContent = {
                            CustomInput(selectedColumn.value)
                        }) {
                            Label(it)
                        }
                    }
                    CustomInput(searchValue,"Value")
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomCheckbox(PATIENT_DIE_LABEL,patientDied)
                        CustomCheckbox(PATIENT_QUIT_LABEL,patientQuit)
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label= FILTER_LABEL, enabledBackgroundColor = BLUE) {
                            filtered.value=filtered.value.filter {
                                if(selectedColumn.value==columns[0]){

                                    (it.firstName?:"").lowercase().contains(searchValue.value.lowercase())
                                            ||
                                            (it.secondName?:"").lowercase().contains(searchValue.value.lowercase())
                                            ||
                                            (it.thirdName?:"").lowercase().contains(searchValue.value.lowercase())
                                            ||
                                            (it.fourthName?:"").lowercase().contains(searchValue.value.lowercase())
                                }
                                else if(selectedColumn.value==columns[1]){
                                    it.nationalId==searchValue.value
                                }
                                else {
                                    it.patientCode?.code==searchValue.value
                                }
                            }
                            if(patientQuit.value){
                                filtered.value=filtered.value.filter {
                                    it.admissions.any { ad -> ad.patientQuit == true }
                                }
                            }
                            if(patientDied.value){
                                filtered.value= filtered.value.filter { it.admissions.any { ad->ad.patientDie==true } }
                            }
                            showDialog.value=false
                        }
                        CustomButton(label= CANCEL_LABEL, enabledBackgroundColor = Color.Red) {
                            filtered.value=all
                            showDialog.value=false
                        }
                    }
                }
            }

        }

    }
}


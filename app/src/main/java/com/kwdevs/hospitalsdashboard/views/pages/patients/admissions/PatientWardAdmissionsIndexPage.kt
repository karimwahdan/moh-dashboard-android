package com.kwdevs.hospitalsdashboard.views.pages.patients.admissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.PatientFullName
import com.kwdevs.hospitalsdashboard.views.cards.patients.PatientAdmissionCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientWardAdmissionsIndexPage(navHostController: NavHostController){
    val patient=Preferences.Patients().get()
    val controller: PatientsController = viewModel()
    val state by controller.admissionsState.observeAsState()
    var admissions by remember { mutableStateOf<List<PatientAdmission>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
           LaunchedEffect(Unit) {
               val s = state as UiState.Success<ApiResponse<PaginationData<PatientAdmission>>>
               val r = s.data
               val pagination=r.pagination
               val data = pagination.data
               lastPage=pagination.lastPage
               admissions=data
           }
        }
        else->{ LaunchedEffect(Unit) {
            controller.indexAdmissionsByHospitalAndPatient(currentPage.intValue) }
        }
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = ADMISSIONS_LABEL,
        headerShowBackButton = true,
        showSheet = showSheet,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(PatientsIndexRoute.route)}
    ) {
        patient?.let {
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center){
                PatientFullName(patient)
            }

        }
        PaginationContainer(currentPage = currentPage,
            lastPage = lastPage,
            totalItems = admissions.size) {
            LazyColumn(modifier= Modifier.fillMaxSize()) {
                items(admissions){
                    PatientAdmissionCard(it,navHostController)
                }
            }
        }
    }
}

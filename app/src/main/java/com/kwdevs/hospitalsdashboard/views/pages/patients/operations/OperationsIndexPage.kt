package com.kwdevs.hospitalsdashboard.views.pages.patients.operations

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.patients.PatientOperationController
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperation
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.OperationCreateRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientViewRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.patients.OperationCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsIndexPage(navHostController: NavHostController){
    val savedPatient=Preferences.Patients().get()
    var items by remember { mutableStateOf<List<PatientOperation>>(emptyList()) }
    val controller: PatientOperationController = viewModel()
    val state by controller.paginationState.observeAsState()
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val viewType=Preferences.ViewTypes().get()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<PatientOperation>>>
            val r = s.data
            val pagination=r.pagination
            lastPage=pagination.lastPage
            val data=pagination.data
            items=data
        }
        else->{
            when(viewType){
                ViewType.BY_PATIENT->{controller.indexByPatient(currentPage.intValue)}
                ViewType.BY_OPERATION_ROOM->{controller.indexByRoom(currentPage.intValue)}
                ViewType.BY_HOSPITAL->{controller.indexByHospital(currentPage.intValue)}
                else->{controller.indexByHospital(currentPage.intValue)}
            }


        }
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = OPERATIONS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            when(viewType){
                ViewType.BY_HOSPITAL->{navHostController.navigate(HospitalHomeRoute.route)}
                ViewType.BY_PATIENT->{navHostController.navigate(PatientViewRoute.route)}
                ViewType.BY_OPERATION_ROOM->{navHostController.navigate(OperationRoomIndexRoute.route)}
                else->{navHostController.navigate(HospitalHomeRoute.route)}
            }
            }) {
        Column(modifier= Modifier.fillMaxWidth().padding(5.dp)){
            Row(modifier= Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                if(viewType==ViewType.BY_PATIENT){
                    if((savedPatient?.admissions?: emptyList()).isNotEmpty()){
                        val admissions=savedPatient?.admissions?: emptyList()
                        //val notQuit=admissions.none {ad-> ad.patientQuit == false || ad.patientQuit == null }
                        val notDie=admissions.all {ad-> ad.patientDie == false || ad.patientDie == null }
                        val stillInHospital=admissions.any { it.exitTime == null }
                        val notDieInOperation=items.none { it.patientDied == true}
                        Log.e("Quit","notDie $notDie , stillInHospital: $stillInHospital , notDieInOperation: $notDieInOperation")
                        if((notDie && stillInHospital && notDieInOperation )){
                            CustomButton(label= ADD_NEW_LABEL,
                                enabledBackgroundColor = BLUE,
                                buttonShape = RectangleShape,
                                buttonShadowElevation = 6) {
                                navHostController.navigate(OperationCreateRoute.route)
                            }
                        }

                    }
                }

            }
            PaginationContainer(
                currentPage=currentPage,
                lastPage = lastPage,
                totalItems = items.size
            ) {
                LazyColumn(modifier= Modifier.fillMaxSize().weight(1f)) {
                    items(items){ OperationCard(it , controller) }
                }
            }
        }
    }

}
package com.kwdevs.hospitalsdashboard.views.pages.hospitals.labTests

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.LabTestFrequencyBody
import com.kwdevs.hospitalsdashboard.controller.hospital.LabTestsController
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequency
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.LAB_TESTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.LabTestFrequencyCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabTestsIndexPage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val controller: LabTestsController = viewModel()
    val state by controller.paginationState.observeAsState()
    var items by remember { mutableStateOf<List<LabTestFrequency>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val showCreateDialog = remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<LabTestFrequency>>>
            val r = s.data
            val pagination = r.pagination
            lastPage=pagination.lastPage
            val data=pagination.data
            items=data
        }
        else->{
            controller.index(currentPage.intValue)
        }
    }
    LabTestCreateDialog(showCreateDialog,controller)
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = LAB_TESTS_LABEL,
        headerShowBackButton = true,
        showSheet = showSheet,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                buttonShape = RectangleShape,
                onClick = { showCreateDialog.value=true })
        }
        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = items.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(items){
                    LabTestFrequencyCard(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabTestCreateDialog(showDialog:MutableState<Boolean>,controller: LabTestsController){
    val hospital=Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val patientsCount = remember { mutableStateOf("") }
    val testsCount = remember { mutableStateOf("") }
    val dayState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val day = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val state by controller.singleState.observeAsState()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                showDialog.value=false
                day.value=""
                patientsCount.value=""
                testsCount.value=""
                controller.index(1)
            }
        }
        else->{

        }
    }
    DatePickerWidget(showDatePicker,dayState,day)

    if(showDialog.value){
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                    enabledBackgroundColor = ORANGE,
                    onClick = { showDatePicker.value = !showDatePicker.value })
                Label(label = DATE_LABEL,day.value)
                CustomInput(testsCount, LAB_TESTS_LABEL)
                CustomInput(patientsCount, PATIENTS_LABEL)
                Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = SAVE_CHANGES_LABEL
                        , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                    ){
                        val body = LabTestFrequencyBody(
                            hospitalId = hospital?.id,
                            patients =patientsCount.value.toInt(),
                            tests = testsCount.value.toInt(),
                            day = day.value,
                            createdById = user?.id
                        )
                        controller.storeNormal(body)

                    }
                }
            }
        }
    }
}
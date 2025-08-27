package com.kwdevs.hospitalsdashboard.views.pages.patients.operations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
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
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientOperationBody
import com.kwdevs.hospitalsdashboard.controller.patients.PatientOperationController
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperation
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperationSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationDepartmentType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationType
import com.kwdevs.hospitalsdashboard.responses.options.OperationOptionsData
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.DateTimePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.ENTER_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NEW_OPERATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_ROOM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_ROOM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_ENTER_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_EXIT_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationCreatePage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    val patient=Preferences.Patients().get()
    val user = Preferences.User().get()
    val controller: PatientOperationController = viewModel()
    val state by controller.singleState.observeAsState()
    val optionsState by controller.optionsState.observeAsState()
    val operation = remember { mutableStateOf<PatientOperation?>(null) }
    val selectedDepartmentType = remember { mutableStateOf<OperationDepartmentType?>(null) }
    var departmentTypes by remember { mutableStateOf<List<OperationDepartmentType>>(emptyList()) }
    val selectedOperationType = remember { mutableStateOf<OperationType?>(null) }
    var operationTypes by remember { mutableStateOf<List<OperationType>>(emptyList()) }
    val selectedOperationRoom = remember { mutableStateOf<OperationRoom?>(null) }
    var operationRooms by remember { mutableStateOf<List<OperationRoom>>(emptyList()) }
    var operationStatuses by remember { mutableStateOf<List<OperationStatus>>(emptyList()) }
    val selectedOperationStatus = remember { mutableStateOf<OperationStatus?>(null) }

    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }
    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }
    val showExitTimePicker = remember { mutableStateOf(false) }

    when(optionsState) {
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<OperationOptionsData>
            val r = s.data
            val data=r.data
            departmentTypes=data.departmentTypes
            operationTypes=data.operationTypes
            operationRooms=data.operationRooms
            operationStatuses=data.statuses
        }
        else->{controller.options()}
    }
    when(state) {
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<PatientOperationSingleResponse>
            val r = s.data
            val data=r.data
            operation.value=data
            LaunchedEffect(Unit) {
                navHostController.navigate(OperationsIndexRoute.route)
            }
        }
        else->{}
    }

    val showSheet = remember { mutableStateOf(false) }

    Container(title = "$NEW_OPERATION_LABEL $FOR_LABEL ${(patient?.firstName?:"")} ${(patient?.fourthName?:"")}",
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(PatientsIndexRoute.route)}) {
        Box(modifier = Modifier
            .fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = DEPARTMENT_LABEL, loadedItems = departmentTypes, selectedItem = selectedDepartmentType, selectedContent = {
                CustomInput(selectedDepartmentType.value?.name?: SELECT_DEPARTMENT_TYPE_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_STATUS_LABEL, loadedItems = operationStatuses, selectedItem = selectedOperationStatus, selectedContent = {
                CustomInput(selectedOperationStatus.value?.name?: SELECT_OPERATION_STATUS_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        VerticalSpacer()
        Box(modifier = Modifier
            .fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_TYPE_LABEL, loadedItems = operationTypes, selectedItem = selectedOperationType, selectedContent = {
                CustomInput(selectedOperationType.value?.name?: SELECT_OPERATION_TYPE_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_ROOM_LABEL, loadedItems = operationRooms, selectedItem = selectedOperationRoom, selectedContent = {
                CustomInput(selectedOperationRoom.value?.name?: SELECT_OPERATION_ROOM_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        VerticalSpacer()

        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            if(enterTime.value.trim()!=""){
                Label(label = ENTER_DATE_LABEL,text=enterTime.value)
            }
            if(exitTime.value.trim()!=""){
                Label(label = EXIT_DATE_LABEL,text=exitTime.value)
            }
        }

        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButton(label = SHOW_ENTER_DATE_PICKER_LABEL ,
                enabledBackgroundColor = ORANGE,
                onClick = { showEnterTimePicker.value = !showEnterTimePicker.value })
            CustomButton(label = SHOW_EXIT_DATE_PICKER_LABEL ,
                enabledBackgroundColor = ORANGE,
                onClick = { showExitTimePicker.value = !showExitTimePicker.value })
        }
        VerticalSpacer()
        CustomButton(label = SAVE_CHANGES_LABEL, buttonShape = RectangleShape, enabledBackgroundColor = GREEN) {
            if(selectedOperationRoom.value!=null &&
                selectedDepartmentType.value!=null &&
                selectedOperationType.value!=null &&
                enterTime.value!="" ){
                val body= PatientOperationBody(
                    hospitalId=hospital?.id,
                    patientId=patient?.id,
                    operationTypeId=selectedOperationType.value?.id,
                    operationDepartmentTypeId=selectedDepartmentType.value?.id,
                    operationRoomId =selectedOperationRoom.value?.id,
                    operationTime=enterTime.value,
                    exitTime                    =   exitTime.value,

                    operationStatusId = selectedOperationStatus.value?.id,
                    createdById=user?.id,
                )
                controller.store(body)

            }
        }

    }

    DateTimePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
    DateTimePickerWidget(showExitTimePicker,exitTimeState,exitTime)

}
package com.kwdevs.hospitalsdashboard.views.cards.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientOperationBody
import com.kwdevs.hospitalsdashboard.controller.patients.PatientOperationController
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperation
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationDepartmentType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationType
import com.kwdevs.hospitalsdashboard.responses.options.OperationOptionsData
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomDialog
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DateTimePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.ENTER_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_ROOM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DIE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_ROOM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_OPERATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_ENTER_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_EXIT_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor

@Composable
fun OperationCard(item: PatientOperation,controller: PatientOperationController){

    val room            = item.operationRoom
    val operationTime   = item.operationTime
    val exitTime        = item.exitTime
    val type            = item.operationType
    val departmentType  = item.departmentType
    val patientDied     = item.patientDied
    val patient         = item.patient
    val gender          = item.patient?.gender
    val status          = item.status
    val genderIcon      = if(gender==1) R.drawable.ic_male else if(gender==2) R.drawable.ic_female else R.drawable.ic_patient
    val showEditDialog  = remember { mutableStateOf(false) }
    ColumnContainer {
        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Label("${patient?.firstName?:""} ${patient?.secondName?:""} ${patient?.fourthName?:""}" )
            Icon(genderIcon)
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End){
                room?.let{
                    Span(it.name?:"", backgroundColor = BLUE, color = WHITE)
                }

                HorizontalSpacer()
                Span(type?.name?:"", backgroundColor = ORANGE, color = WHITE)
                HorizontalSpacer()

            }
        }
        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically) {
            Label(departmentType?.name?:"")
            HorizontalSpacer()
            status?.let {
                Span(it.name, backgroundColor = hexToComposeColor(it.color), color = WHITE)
            }
        }
        operationTime?.let {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)) {
                Label(text= it,label=OPERATION_TIME_LABEL)
            }
        }

        VerticalSpacer()
        exitTime?.let {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)) {
                Label(text= it,label= EXIT_TIME_LABEL)

            }
        }
        VerticalSpacer(10)
        if(patientDied==true){
            VerticalSpacer()
            Span(PATIENT_DIE_LABEL, backgroundColor = Color.Red, color = WHITE)
        }

        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalArrangement = Arrangement.End) {
            IconButton(R.drawable.ic_edit_blue, elevation = 5) {
                showEditDialog.value=true
            }
            HorizontalSpacer()
            IconButton(R.drawable.ic_home_work_blue, elevation = 5) {

            }
            HorizontalSpacer()
            IconButton(R.drawable.ic_cancel_red, elevation = 5) {

            }
        }

    }
    EditDialog(showEditDialog,controller,item)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(showDialog:MutableState<Boolean>,controller:PatientOperationController,item:PatientOperation){
    val user=Preferences.User().get()
    //val hospital=Preferences.Hospitals().get()
    val patient=item.patient
    val operationDate=item.operationTime
    val type=item.operationType
    val room=item.operationRoom
    val patientDied=item.patientDied
    val departmentType=item.departmentType
    val status=item.status
    val optionsState by controller.optionsState.observeAsState()
    val selectedDepartmentType = remember { mutableStateOf<OperationDepartmentType?>(null) }
    var departmentTypes by remember { mutableStateOf<List<OperationDepartmentType>>(emptyList()) }
    val selectedOperationType = remember { mutableStateOf<OperationType?>(null) }
    var operationTypes by remember { mutableStateOf<List<OperationType>>(emptyList()) }
    val selectedOperationRoom = remember { mutableStateOf<OperationRoom?>(null) }
    var operationRooms by remember { mutableStateOf<List<OperationRoom>>(emptyList()) }
    var operationStatuses by remember { mutableStateOf<List<OperationStatus>>(emptyList()) }
    val selectedOperationStatus = remember { mutableStateOf<OperationStatus?>(null) }

    val deathStatus = remember { mutableStateOf(false) }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }

    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }
    val showExitTimePicker = remember { mutableStateOf(false) }
    val state by controller.singleState.observeAsState()
    LaunchedEffect(Unit) {
        selectedOperationRoom.value=room
        selectedOperationType.value=type
        selectedDepartmentType.value=departmentType
        enterTime.value=operationDate?:""
        exitTime.value=item.exitTime?:""
        deathStatus.value=patientDied?:false
        selectedOperationStatus.value=status
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val viewType=Preferences.ViewTypes().get()
            LaunchedEffect(Unit) {
                when(viewType){
                    ViewType.BY_PATIENT->{controller.indexByPatient(1)}
                    ViewType.BY_OPERATION_ROOM->{controller.indexByRoom(1)}
                    ViewType.BY_HOSPITAL->{controller.indexByHospital(1)}
                    else->{controller.indexByHospital(1)}
                }
            }
        }
        else->{}
    }

    if(showDialog.value){
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
    }
    CustomDialog(showDialog) {
        Label(text = "${patient?.firstName?:""} ${patient?.secondName?:""} ",label=PATIENT_LABEL)
        VerticalSpacer()
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = DEPARTMENT_LABEL, loadedItems = departmentTypes, selectedItem = selectedDepartmentType, selectedContent = {
                CustomInput(selectedDepartmentType.value?.name?: SELECT_DEPARTMENT_TYPE_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        VerticalSpacer()
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_TYPE_LABEL, loadedItems = operationTypes, selectedItem = selectedOperationType, selectedContent = {
                CustomInput(selectedOperationType.value?.name?: SELECT_OPERATION_TYPE_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_ROOM_LABEL, loadedItems = operationRooms, selectedItem = selectedOperationRoom, selectedContent = {
                CustomInput(selectedOperationRoom.value?.name?: SELECT_OPERATION_ROOM_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
            ComboBox(title = OPERATION_STATUS_LABEL, loadedItems = operationStatuses, selectedItem = selectedOperationStatus, selectedContent = {
                CustomInput(selectedOperationStatus.value?.name?: SELECT_OPERATION_STATUS_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }
        VerticalSpacer()
        if(enterTime.value.trim()!=""){
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Label(label = ENTER_DATE_LABEL,text=enterTime.value)
            }
        }
        if(exitTime.value.trim()!=""){
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
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
        CustomButton(label = SAVE_CHANGES_LABEL,
            buttonShape = RectangleShape, enabledBackgroundColor = GREEN) {
            if(selectedOperationRoom.value!=null &&
                selectedDepartmentType.value!=null &&
                selectedOperationType.value!=null &&
                enterTime.value!="" ){
                val body= PatientOperationBody(
                    id                          =   item.id,
                    hospitalId                  =   item.hospitalId,
                    patientId                   =   item.patientId,
                    operationTypeId             =   selectedOperationType.value?.id,
                    operationDepartmentTypeId   =   selectedDepartmentType.value?.id,
                    operationRoomId             =   selectedOperationRoom.value?.id,
                    operationTime               =   enterTime.value,
                    exitTime                    =   exitTime.value,
                    operationStatusId           =   selectedOperationStatus.value?.id,
                    updatedById                 =   user?.id,
                )
                controller.update(body)
                showDialog.value=false
            }
        }
    }
    DateTimePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
    DateTimePickerWidget(showExitTimePicker,exitTimeState,exitTime)
}


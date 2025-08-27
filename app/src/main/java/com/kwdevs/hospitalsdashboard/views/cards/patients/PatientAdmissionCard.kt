package com.kwdevs.hospitalsdashboard.views.cards.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientAdmissionBody
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalWardController
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.wards.HospitalWardsResponse
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.responses.settings.PatientMedicalStatesResponse
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BED_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEATH_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIE_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ENTER_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LIST_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DIE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_QUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QUIT_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEATH_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_PATIENT_STATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_ENTER_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_EXIT_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_WARDS
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TRANSFER_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun PatientAdmissionCard(item: PatientAdmission,navHostController: NavHostController){
    val ward = item.ward
    val wardType=item.wardType
    val enterTime = item.admissionTime
    val exitTime = item.exitTime
    val patientQuit=item.patientQuit
    val patientState=item.patientState
    val patientDie=item.patientDie
    val showPatientTransferDialog = remember { mutableStateOf(false) }
    val showPatientQuitDialog = remember { mutableStateOf(false) }
    val showPatientDieDialog = remember { mutableStateOf(false) }
    val showAdmissionEditDialog = remember { mutableStateOf(false) }

    AdmissionEditDialog(showAdmissionEditDialog,item)
    TransferPatientDialog(showPatientTransferDialog,item)
    QuitPatientDialog(showPatientQuitDialog,item)
    DiePatientDialog(showPatientDieDialog,item)

    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Column(modifier=Modifier.fillMaxWidth().weight(1f)){
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    ward?.let{Label(it.name)}
                    wardType?.let { Span(it.name, backgroundColor = BLUE, color = WHITE) }
                }
                VerticalSpacer(5)
                enterTime?.let {
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                        Label(label = ENTER_DATE_LABEL, text = it)
                        patientState?.let{ Span(it.name, backgroundColor = BLUE, color = WHITE) }
                    }
                }
                exitTime?.let {
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Label(label = if(patientDie == false || patientDie==null) EXIT_DATE_LABEL else DEATH_DATE_LABEL, text = it)
                        patientQuit?.let { if(it) Span(PATIENT_QUIT_LABEL, backgroundColor = GREEN) }
                        patientDie?.let { if(it) Span(PATIENT_DIE_LABEL, backgroundColor = Color.Red) }
                    }
                }
            }
            CustomButtonWithImage(icon = R.drawable.ic_edit_blue,
                label = EDIT_LABEL,
                iconSize = 26)  { showAdmissionEditDialog.value=true}
        }

        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End){

            HorizontalSpacer()
            if(item.exitTime==null){
                CustomButtonWithImage(icon = R.drawable.ic_patient_transfer,
                    label = TRANSFER_PATIENT_LABEL,
                    iconSize = 26,
                    maxWidth = 62) { showPatientTransferDialog.value=true }
                //HorizontalSpacer()
                //CustomButtonWithImage(icon=R.drawable.ic_exit_red, label = EXIT_LABEL,iconSize = 26) { showPatientQuitDialog.value=true }
                //HorizontalSpacer()
                //CustomButtonWithImage(R.drawable.ic_medical_morgue, label = PATIENT_DIE_LABEL,iconSize = 26) { showPatientDieDialog.value=true }
                HorizontalSpacer()
                CustomButtonWithImage(icon = R.drawable.ic_operation_room_grayscale,
                    label = OPERATIONS_LIST_LABEL,
                    iconSize = 26,
                    maxWidth = 52) {
                    //Preferences.Patients().set(item.patient)
                    Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                    navHostController.navigate(OperationsIndexRoute.route)
                }            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferPatientDialog(showDialog:MutableState<Boolean>,item: PatientAdmission){

    val patientController: PatientsController = viewModel()
    val admissionState by patientController.admissionState.observeAsState()
    val user = Preferences.User().get()
    var wardTypes by remember { mutableStateOf<List<WardType>>(emptyList()) }
    val selectedWardType = remember { mutableStateOf<WardType?>(null) }
    val settingsController: SettingsController = viewModel()
    val settingsState by settingsController.hospitalWardOptionsState.observeAsState()
    var patientMedicalStates by remember { mutableStateOf<List<PatientState>>(emptyList()) }
    val selectedPatientMedicalState = remember { mutableStateOf<PatientState?>(null) }

    val wardsController: HospitalWardController = viewModel()
    val wardsState by wardsController.state.observeAsState()
    var wards by remember{ mutableStateOf<List<HospitalWard>>(emptyList())}
    val selectedWard = remember { mutableStateOf<HospitalWard?>(null) }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }

    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }
    val showExitTimePicker = remember { mutableStateOf(false) }
    val patientQuit = remember { mutableStateOf(false) }
    val patientDie = remember { mutableStateOf(false) }

    val bedCode = remember { mutableStateOf("") }


    if(showDialog.value){
        LaunchedEffect(Unit) {
            exitTime.value=item.exitTime?:""
            enterTime.value=item.admissionTime?:""
            selectedWard.value=item.ward
            selectedWardType.value=item.wardType
            selectedPatientMedicalState.value=item.patientState
            if(item.patientDie==true) patientDie.value=true
            if(item.patientQuit==true) patientQuit.value=true
        }
        when(settingsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = settingsState as UiState.Success<HospitalWardAdmissionOptionsData>
                val r = s.data
                val data = r.data
                wardTypes=data.types
                patientMedicalStates=data.states

            }
            else->{
                settingsController.wardAdmissionOptions()
            }
        }
        when(wardsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = wardsState as UiState.Success<HospitalWardsResponse>
                val r = s.data
                val data = r.data
                wards=data

            }
            else->{
            }
        }
        when(admissionState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    patientController.indexAdmissionsByHospitalAndPatient()
                }

            }
            else->{}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier=Modifier.fillMaxWidth().background(WHITE, shape = rcs(5))) {
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.End){
                        IconButton(R.drawable.ic_cancel_red) {showDialog.value=false }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center){
                        Label("$TRANSFER_PATIENT_LABEL ${item.patient?.firstName?:""} $FROM_LABEL ${item.ward?.name?:""}")
                    }

                }
                Column {
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            VerticalSpacer(10)

                            Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                                    ComboBox(hasTitle = false,selectedItem = selectedPatientMedicalState, loadedItems = patientMedicalStates, selectedContent = {
                                        CustomInput(value=if(selectedPatientMedicalState.value==null) SELECT_PATIENT_STATE_LABEL else selectedPatientMedicalState.value?.name?:"",
                                            readOnly = true,
                                            icon = R.drawable.ic_arrow_drop_down_blue)
                                    }) {
                                        Label(
                                            it?.name?:"",
                                            color = if(selectedPatientMedicalState.value==it) BLUE else BLACK
                                        )
                                    }
                                }
                                Box(modifier= Modifier.width(26.dp))
                            }
                            VerticalSpacer(10)
                            Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                                    ComboBox(hasTitle = false,selectedItem = selectedWardType, loadedItems = wardTypes, selectedContent = {
                                        CustomInput(value=if(selectedWardType.value==null) SELECT_DEPARTMENT_TYPE_LABEL else selectedWardType.value?.name?:"",
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
                            VerticalSpacer(10)
                            if(!patientDie.value && !patientQuit.value){
                                if(selectedWardType.value != null  ){
                                    CustomButton(label = SHOW_WARDS
                                        , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                                    ) {
                                        wardsController.indexByTypeAndHospital(selectedWardType.value?.id?:0)
                                    }
                                }
                                VerticalSpacer(10)
                                if(wards.isNotEmpty()){
                                    Row(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                                        Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                                            ComboBox(hasTitle = false,selectedItem = selectedWard, loadedItems = wards, selectedContent = {
                                                CustomInput(value=if(selectedWard.value==null) "Select Ward " else selectedWard.value?.name?:"",
                                                    readOnly = true,
                                                    icon = R.drawable.ic_arrow_drop_down_blue)
                                            }) {
                                                Label(
                                                    it?.name?:"",
                                                    color = if(selectedWard.value==it) BLUE else BLACK
                                                )
                                            }
                                        }
                                        Box(modifier= Modifier.width(26.dp)){}
                                    }
                                    VerticalSpacer()
                                    CustomInput(bedCode, BED_CODE_LABEL)


                                }
                                VerticalSpacer(10)
                            }
                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomButton(label = SHOW_ENTER_DATE_PICKER_LABEL ,
                                    enabledBackgroundColor = BLUE,
                                    onClick = { showEnterTimePicker.value = !showEnterTimePicker.value })
                                CustomButton(label = SHOW_EXIT_DATE_PICKER_LABEL ,
                                    enabledBackgroundColor = ORANGE,
                                    onClick = { showExitTimePicker.value = !showExitTimePicker.value })
                            }

                            Column(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Label(label = ENTER_DATE_LABEL,enterTime.value)
                                    if (enterTime.value.trim()!=""){
                                        IconButton(icon = R.drawable.ic_cancel_red) {enterTime.value=""}
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Label(label = EXIT_DATE_LABEL,exitTime.value)
                                    if (exitTime.value.trim()!=""){
                                        IconButton(icon = R.drawable.ic_cancel_red) { exitTime.value="" }
                                    }
                                }
                            }

                            VerticalSpacer(10)

                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomCheckbox(label = PATIENT_QUIT_LABEL,active = patientQuit)
                                CustomCheckbox(label = PATIENT_DIE_LABEL, active = patientDie)
                            }
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomButton(label = TRANSFER_PATIENT_LABEL,
                            buttonShape = RectangleShape,
                            enabledBackgroundColor = BLUE) {
                            val body = PatientAdmissionBody(
                                id = item.id,
                                wardTypeId = selectedWardType.value?.id,
                                patientStateId = selectedPatientMedicalState.value?.id,
                                wardId = selectedWard.value?.id,
                                admissionTime = enterTime.value,
                                exitTime = exitTime.value,
                                patientDie = if(patientDie.value) 1 else null,
                                patientQuit = if(patientQuit.value) 1 else null,
                                createdById = user?.id,
                                updatedById = user?.id
                            )
                            patientController.transferAdmission(body)
                            showDialog.value=false

                        }

                    }

                }
            }
        }

        DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdmissionEditDialog(showDialog:MutableState<Boolean>,item: PatientAdmission){
    val settingsController: SettingsController = viewModel()
    val settingsState by settingsController.hospitalWardOptionsState.observeAsState()
    val patientController: PatientsController = viewModel()
    val admissionState by patientController.admissionState.observeAsState()
    val bedCode = remember { mutableStateOf("") }
    var patientMedicalStates by remember { mutableStateOf<List<PatientState>>(emptyList()) }
    val selectedPatientMedicalState = remember { mutableStateOf<PatientState?>(null) }
    var wardTypes by remember { mutableStateOf<List<WardType>>(emptyList()) }
    val selectedWardType = remember { mutableStateOf<WardType?>(null) }
    val wardsController: HospitalWardController = viewModel()
    val wardsState by wardsController.state.observeAsState()
    var wards by remember { mutableStateOf<List<HospitalWard>>(emptyList()) }
    val selectedWard = remember { mutableStateOf<HospitalWard?>(null) }

    val patientQuit = remember { mutableStateOf(false) }
    val patientDie = remember { mutableStateOf(false) }
    var exitLabel by remember { mutableStateOf(EXIT_DATE_LABEL) }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }


    val showEnterTimePicker = remember { mutableStateOf(false) }
    val showExitTimePicker = remember { mutableStateOf(false) }

    if(showDialog.value){
        LaunchedEffect(Unit) {
            patientQuit.value=item.patientQuit?:false
            patientDie.value=item.patientDie?:false
            selectedWard.value=item.ward
            selectedWardType.value=item.wardType
            selectedPatientMedicalState.value=item.patientState
            enterTime.value=item.admissionTime?:""
            exitTime.value=item.exitTime?:""
            bedCode.value=item.bedCode?:""
        }
        LaunchedEffect(patientQuit.value,patientDie.value) {
            if(patientQuit.value) patientDie.value=false
            if(patientDie.value) patientQuit.value=false
        }
        LaunchedEffect(patientDie.value) {
            exitLabel=if(patientDie.value) DEATH_DATE_LABEL else EXIT_DATE_LABEL
        }
        when(settingsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = settingsState as UiState.Success<HospitalWardAdmissionOptionsData>
                val r = s.data
                val data = r.data
                wardTypes=data.types
                patientMedicalStates=data.states

            }
            else->{
                settingsController.wardAdmissionOptions()
            }
        }

        when(wardsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = wardsState as UiState.Success<HospitalWardsResponse>
                val r = s.data
                val data = r.data
                wards=data

            }
            else->{}
        }

        when(admissionState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    patientController.indexAdmissionsByHospitalAndPatient(1)
                } }
            else->{}
        }

        DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)

        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                Column(modifier=Modifier.fillMaxWidth().padding(5.dp)) {
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        item.ward?.let{Label(it.name)}
                        item.wardType?.let { Span(it.name, backgroundColor = BLUE, color = WHITE) }
                    }
                    VerticalSpacer(5)
                    item.admissionTime?.let { Label(label = ENTER_DATE_LABEL, text = it) }
                    item.exitTime?.let { Label(label = exitLabel, text = it) }

                    Row(modifier=Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center){
                        item.patientQuit?.let { if(it) Span(PATIENT_QUIT_LABEL, backgroundColor = GREEN) }
                        item.patientDie?.let { if(it) Span(PATIENT_DIE_LABEL, backgroundColor = Color.Red) }
                    }
                    VerticalSpacer()
                }
                Column(modifier=Modifier.fillMaxSize()){
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            VerticalSpacer(10)

                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                                    ComboBox(hasTitle = false,selectedItem = selectedPatientMedicalState, loadedItems = patientMedicalStates, selectedContent = {
                                        CustomInput(value=if(selectedPatientMedicalState.value==null) "Select State" else selectedPatientMedicalState.value?.name?:"",
                                            readOnly = true,
                                            icon = R.drawable.ic_arrow_drop_down_blue)
                                    }) {
                                        Label(
                                            it?.name?:"",
                                            color = if(selectedPatientMedicalState.value==it) BLUE else BLACK
                                        )
                                    }
                                }
                                Box(modifier=Modifier.width(26.dp))
                            }

                            VerticalSpacer(10)

                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
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
                                    IconButton(icon=R.drawable.ic_cancel_red) { selectedWardType.value=null}
                                }
                                else{
                                    Box(modifier=Modifier.width(26.dp))
                                }
                            }

                            VerticalSpacer(10)

                            if(selectedWardType.value != null  ){
                                CustomButton(label = SHOW_WARDS
                                    , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                                ) {
                                    wardsController.indexByTypeAndHospital(selectedWardType.value?.id?:0)
                                }
                            }

                            VerticalSpacer(10)

                            if(wards.isNotEmpty()){
                                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                                    Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                                        ComboBox(hasTitle = false,selectedItem = selectedWard, loadedItems = wards.filter { it!=item.ward }, selectedContent = {
                                            CustomInput(value=if(selectedWard.value==null) "Select Ward " else selectedWard.value?.name?:"",
                                                readOnly = true,
                                                icon = R.drawable.ic_arrow_drop_down_blue)
                                        }) {
                                            Label(
                                                it?.name?:"",
                                                color = if(selectedWard.value==it) BLUE else BLACK
                                            )
                                        }
                                    }
                                    Box(modifier=Modifier.width(26.dp)){
                                    }
                                }
                                VerticalSpacer()
                                CustomInput(bedCode,BED_CODE_LABEL)
                            }

                            VerticalSpacer(10)

                            Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomButton(label = SHOW_ENTER_DATE_PICKER_LABEL ,
                                    enabledBackgroundColor = BLUE,
                                    onClick = { showEnterTimePicker.value = !showEnterTimePicker.value })
                                CustomButton(label = SHOW_EXIT_DATE_PICKER_LABEL ,
                                    enabledBackgroundColor = ORANGE,
                                    onClick = { showExitTimePicker.value = !showExitTimePicker.value })
                            }

                            Column(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Label(label = ENTER_DATE_LABEL,enterTime.value)
                                    if (enterTime.value.trim()!=""){
                                        IconButton(icon = R.drawable.ic_cancel_red) {enterTime.value=""}
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Label(label = EXIT_DATE_LABEL,exitTime.value)
                                    if (exitTime.value.trim()!=""){
                                        IconButton(icon = R.drawable.ic_cancel_red) { exitTime.value="" }
                                    }
                                }
                            }

                            VerticalSpacer(10)

                            Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomCheckbox(label = PATIENT_QUIT_LABEL,active = patientQuit)
                                CustomCheckbox(label = PATIENT_DIE_LABEL, active = patientDie)
                            }

                        }
                    }
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center){
                        CustomButton(label = SAVE_CHANGES_LABEL
                            , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                        ){
                            val body = PatientAdmissionBody(
                                id=item.id,
                                wardId = selectedWard.value?.id,
                                wardTypeId = selectedWardType.value?.id,
                                patientStateId = selectedPatientMedicalState.value?.id,
                                bedCode = bedCode.value,
                                admissionTime = enterTime.value,
                                exitTime = exitTime.value,
                                patientDie = if(patientDie.value) 1 else null,
                                patientQuit = if(patientQuit.value) 1 else null,
                                updatedById = Preferences.User().get()?.id
                            )
                            patientController.updateAdmission(body)
                            showDialog.value=false


                        }
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuitPatientDialog(showDialog:MutableState<Boolean>,item: PatientAdmission){

    val patientController: PatientsController = viewModel()
    val admissionState by patientController.admissionState.observeAsState()
    val user = Preferences.User().get()
    val settingsController: SettingsController = viewModel()
    val settingsState by settingsController.medicalStatesState.observeAsState()

    var patientMedicalStates by remember { mutableStateOf<List<PatientState>>(emptyList()) }
    val selectedPatientMedicalState = remember { mutableStateOf<PatientState?>(null) }

    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }
    val showExitTimePicker = remember { mutableStateOf(false) }



    if(showDialog.value){
        when(settingsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = settingsState as UiState.Success<PatientMedicalStatesResponse>
                val r = s.data
                val data = r.data
                patientMedicalStates=data

            }
            else->{
                settingsController.wardAdmissionOptions()
            }
        }

        when(admissionState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    patientController.indexAdmissionsByHospitalAndPatient()
                }
            }
            else->{}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier=Modifier.fillMaxWidth().background(WHITE, shape = rcs(5))) {
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.End){
                        IconButton(R.drawable.ic_cancel_red) {showDialog.value=false }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center){
                        Label("$QUIT_PATIENT_LABEL ${item.patient?.firstName?:""} $FROM_LABEL ${item.ward?.name?:""}")
                    }

                }
                Column {
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            VerticalSpacer(10)

                            Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                                    ComboBox(hasTitle = false,selectedItem = selectedPatientMedicalState, loadedItems = patientMedicalStates, selectedContent = {
                                        CustomInput(value=if(selectedPatientMedicalState.value==null) "Select State" else selectedPatientMedicalState.value?.name?:"",
                                            readOnly = true,
                                            icon = R.drawable.ic_arrow_drop_down_blue)
                                    }) {
                                        Label(
                                            it?.name?:"",
                                            color = if(selectedPatientMedicalState.value==it) BLUE else BLACK
                                        )
                                    }
                                }
                                Box(modifier= Modifier.width(26.dp))
                            }
                            VerticalSpacer(10)

                            VerticalSpacer(10)
                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.Center){
                                CustomButton(label = SHOW_EXIT_DATE_PICKER_LABEL ,
                                    enabledBackgroundColor = ORANGE,
                                    onClick = { showExitTimePicker.value = !showExitTimePicker.value })
                            }

                            Column(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Label(label = EXIT_DATE_LABEL,exitTime.value)
                                    if (exitTime.value.trim()!=""){
                                        IconButton(icon = R.drawable.ic_cancel_red) { exitTime.value="" }
                                    }
                                }
                            }

                            VerticalSpacer(10)

                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomButton(label = TRANSFER_PATIENT_LABEL,
                            buttonShape = RectangleShape,
                            enabledBackgroundColor = BLUE) {
                            val body = PatientAdmissionBody(
                                id = item.id,
                                patientStateId = selectedPatientMedicalState.value?.id,
                                exitTime = exitTime.value,
                                patientDie = 0,
                                patientQuit = 1,
                                updatedById = user?.id
                            )
                            patientController.quit(body)
                            showDialog.value=false

                        }

                    }

                }
            }
        }
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiePatientDialog(showDialog:MutableState<Boolean>,item: PatientAdmission){

    val patientController: PatientsController = viewModel()
    val admissionState by patientController.admissionState.observeAsState()
    val user = Preferences.User().get()

    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }
    val showExitTimePicker = remember { mutableStateOf(false) }




    if(showDialog.value){
        when(admissionState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    patientController.indexAdmissionsByHospitalAndPatient()
                }
            }
            else->{}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier=Modifier.fillMaxWidth().background(WHITE, shape = rcs(5))) {
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.End){
                        IconButton(R.drawable.ic_cancel_red) {showDialog.value=false }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center){
                        Label("$DIE_PATIENT_LABEL ${item.patient?.firstName?:""}")
                    }
                }
                Column {
                    VerticalSpacer(10)
                    Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.Center){
                        CustomButton(label = SELECT_DEATH_DATE_LABEL ,
                            enabledBackgroundColor = ORANGE,
                            onClick = { showExitTimePicker.value = !showExitTimePicker.value })
                    }
                    Column(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label(label = DEATH_DATE_LABEL,exitTime.value)
                            if (exitTime.value.trim()!=""){
                                IconButton(icon = R.drawable.ic_cancel_red) { exitTime.value="" }
                            }
                        }
                    }
                    VerticalSpacer(10)
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomButton(label = TRANSFER_PATIENT_LABEL,
                            buttonShape = RectangleShape,
                            enabledBackgroundColor = BLUE) {
                            val body = PatientAdmissionBody(
                                id = item.id,
                                exitTime = exitTime.value,
                                patientDie = 1,
                                patientQuit = 0,
                                updatedById = user?.id
                            )
                            patientController.die(body)
                            showDialog.value=false

                        }

                    }
                }
            }
        }
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)
    }
}


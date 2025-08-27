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
import androidx.compose.material3.HorizontalDivider
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
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.bodies.patients.PretermAdmissionBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.PretermAdmissionsController
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmission
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.responses.settings.PatientMedicalStatesResponse
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALTERNATIVE_MOBILE_NUMBER_LABEL
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
import com.kwdevs.hospitalsdashboard.views.assets.ENTER_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FEMALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FIRST_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOURTH_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAND_FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.INFANT_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOTHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DIE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_QUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PatientFullName
import com.kwdevs.hospitalsdashboard.views.assets.QUIT_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEATH_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_GENDER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_ENTER_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_EXIT_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TRANSFER_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun PretermAdmissionCard(item: PretermAdmission){
    val mother = item.mother
    val patient=item.patient
    val enterTime = item.admissionTime
    val exitTime = item.exitTime
    val patientQuit=item.patientQuit
    val patientDie=item.patientDie
    val showPatientTransferDialog = remember { mutableStateOf(false) }
    val showPatientQuitDialog = remember { mutableStateOf(false) }
    val showPatientDieDialog = remember { mutableStateOf(false) }
    val showAdmissionEditDialog = remember { mutableStateOf(false) }
    val controller:PretermAdmissionsController= viewModel()
    PretermAdmissionEditDialog(showAdmissionEditDialog,item,controller)
    QuitPretermDialog(showPatientQuitDialog,item,controller)
    DiePretermDialog(showPatientDieDialog,item,controller)

    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            mother?.let{
                Label(MOTHER_NAME_LABEL)
                PatientFullName(it)
            }

        }
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            patient?.let{
                Label(INFANT_NAME_LABEL)
                PatientFullName(it)
            }

        }

        VerticalSpacer(5)
        enterTime?.let { Label(label = ENTER_DATE_LABEL, text = it) }
        exitTime?.let { Label(label = if(patientDie == false || patientDie==null) EXIT_DATE_LABEL else DEATH_DATE_LABEL, text = it) }
        Row(modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            patientQuit?.let { if(it) Span(PATIENT_QUIT_LABEL, backgroundColor = GREEN) }
            patientDie?.let { if(it) Span(PATIENT_DIE_LABEL, backgroundColor = Color.Red) }
        }
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
            IconButton(R.drawable.ic_edit_blue, elevation = 6) { showAdmissionEditDialog.value=true}
            HorizontalSpacer()
            if(item.exitTime==null){
                IconButton(R.drawable.ic_home_work_blue, elevation = 6) { showPatientTransferDialog.value=true }
                HorizontalSpacer()
                IconButton(R.drawable.ic_exit_red, elevation = 6) { showPatientQuitDialog.value=true }
                HorizontalSpacer()
                IconButton(R.drawable.ic_cancel_red, elevation = 6) { showPatientDieDialog.value=true }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PretermAdmissionEditDialog(showDialog:MutableState<Boolean>,item: PretermAdmission,controller: PretermAdmissionsController){
    val settingsController: SettingsController = viewModel()
    val settingsState by settingsController.hospitalWardOptionsState.observeAsState()
    val patientController: PatientsController = viewModel()
    val state by controller.singleState.observeAsState()
    var patientMedicalStates by remember { mutableStateOf<List<PatientState>>(emptyList()) }
    val selectedPatientMedicalState = remember { mutableStateOf<PatientState?>(null) }

    val patientQuit = remember { mutableStateOf(false) }
    val patientDie = remember { mutableStateOf(false) }

    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }


    val showEnterTimePicker = remember { mutableStateOf(false) }
    val showExitTimePicker = remember { mutableStateOf(false) }
    val showNewPatientDialog = remember { mutableStateOf(false) }
    val mothersState by patientController.allState.observeAsState()
    val patientsState by patientController.allState.observeAsState()

    val selectedPatient = remember { mutableStateOf<Patient?>(null) }
    val motherNationalId = remember { mutableStateOf("") }
    val patientNationalId = remember { mutableStateOf("") }

    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    val selectedMother = remember { mutableStateOf<Patient?>(null) }
    var mothers by remember { mutableStateOf<List<Patient>>(emptyList()) }

    LaunchedEffect(Unit) {
        patientQuit.value=item.patientQuit?:false
        patientDie.value=item.patientDie?:false
        selectedPatientMedicalState.value=item.patientState
        enterTime.value=item.admissionTime?:""
        exitTime.value=item.exitTime?:""
    }
    LaunchedEffect(patientQuit.value,patientDie.value) {
        if(patientQuit.value) patientDie.value=false
        if(patientDie.value) patientQuit.value=false
    }


    if(showDialog.value){
        when(mothersState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = mothersState as UiState.Success<PatientsResponse>
                val r = s.data
                val data = r.data
                mothers = data
                if(mothers.isNotEmpty())selectedMother.value=mothers[0]
            }
            else->{}
        }
        when(patientsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = patientsState as UiState.Success<PatientsResponse>
                val r = s.data
                val data = r.data
                patients = data
            }
            else->{}
        }

        when(settingsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = settingsState as UiState.Success<HospitalWardAdmissionOptionsData>
                val r = s.data
                val data = r.data
                patientMedicalStates=data.states

            }
            else->{
                settingsController.wardAdmissionOptions()
            }
        }
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) { showDialog.value=false } }
            else->{}
        }
        NewPatientDialog(showNewPatientDialog)
        DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)

        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                Column(modifier=Modifier.fillMaxWidth().padding(5.dp)) {
                    Row(modifier=Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                            CustomInput(value = motherNationalId, label = NATIONAL_ID_LABEL)
                        }
                        CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                            if(motherNationalId.value.trim() !="" && motherNationalId.value.length>2) patientController.filterFemale(motherNationalId.value)
                        }
                    }
                    if(mothers.isEmpty() || motherNationalId.value.trim() == "" ){
                        CustomButton(label = ADD_NEW_LABEL
                            , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                        ) {
                            showNewPatientDialog.value=true
                        }
                    }

                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                            ComboBox(hasTitle = false,selectedItem = selectedMother, loadedItems = mothers, selectedContent = {
                                CustomInput(value=if(selectedMother.value==null) "Select Patient" else "${selectedMother.value?.firstName?:""} ${selectedMother.value?.secondName?:""} ${selectedMother.value?.thirdName?:""} ${selectedMother.value?.fourthName?:""}",
                                    readOnly = true,
                                    icon = R.drawable.ic_arrow_drop_down_blue)
                            }) {
                                Label(
                                    "${it?.firstName?:""} ${it?.secondName?:""} ${it?.thirdName?:""} ${it?.fourthName?:""}",
                                    color = if(selectedMother.value==it) BLUE else BLACK
                                )
                            }
                        }
                        if(selectedMother.value!=null){
                            IconButton(icon=R.drawable.ic_cancel_red) { motherNationalId.value="";selectedMother.value=null }
                        }else{
                            Box(modifier=Modifier.width(26.dp))
                        }
                    }

                    VerticalSpacer(10)

                    HorizontalDivider()

                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                            CustomInput(value = patientNationalId, label = NATIONAL_ID_LABEL)
                        }
                        CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                            if(patientNationalId.value.trim() !="" && patientNationalId.value.length>2) patientController.filter(patientNationalId.value)
                        }
                    }
                    if(patients.isEmpty() || patientNationalId.value.trim() == "" ){
                        CustomButton(label = ADD_NEW_LABEL
                            , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                        ) {
                            showNewPatientDialog.value=true
                        }
                    }

                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                            ComboBox(hasTitle = false,selectedItem = selectedPatient, loadedItems = patients, selectedContent = {
                                CustomInput(value=if(selectedPatient.value==null) "Select Patient" else "${selectedPatient.value?.firstName?:""} ${selectedPatient.value?.secondName?:""} ${selectedPatient.value?.thirdName?:""} ${selectedPatient.value?.fourthName?:""}",
                                    readOnly = true,
                                    icon = R.drawable.ic_arrow_drop_down_blue)
                            }) {
                                Label(
                                    "${it?.firstName?:""} ${it?.secondName?:""} ${it?.thirdName?:""} ${it?.fourthName?:""}",
                                    color = if(selectedPatient.value==it) BLUE else BLACK
                                )
                            }
                        }
                        if(selectedPatient.value!=null){
                            IconButton(icon=R.drawable.ic_cancel_red) { patientNationalId.value="";selectedPatient.value=null }
                        }else{
                            Box(modifier=Modifier.width(26.dp))
                        }
                    }

                    VerticalSpacer()

                    HorizontalDivider()
                    VerticalSpacer(5)
                    item.admissionTime?.let { Label(label = ENTER_DATE_LABEL, text = it) }
                    item.exitTime?.let { Label(label = if(item.patientDie == false || item.patientDie==null) EXIT_DATE_LABEL else DEATH_DATE_LABEL, text = it) }

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
                            val body = PretermAdmissionBody(
                                id=item.id,
                                motherId = selectedMother.value?.id,
                                patientId = selectedPatient.value?.id,
                                patientStateId = selectedPatientMedicalState.value?.id,
                                admissionDate = enterTime.value,
                                exitDate = exitTime.value,
                                patientDie = if(patientDie.value) 1 else null,
                                patientQuit = if(patientQuit.value) 1 else null,
                                updatedById = Preferences.User().get()?.id
                            )
                            controller.updateAdmission(body)

                        }
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuitPretermDialog(showDialog:MutableState<Boolean>,item: PretermAdmission,controller: PretermAdmissionsController){

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
                LaunchedEffect(Unit) { showDialog.value=false }
                patientController.indexAdmissionsByHospitalAndPatient()
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
                        Label("$QUIT_PATIENT_LABEL ${item.patient?.firstName?:""}")
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
                            val body = PretermAdmissionBody(
                                id = item.id,
                                patientStateId = selectedPatientMedicalState.value?.id,
                                exitDate = exitTime.value,
                                patientDie = 0,
                                patientQuit = 1,
                                updatedById = user?.id
                            )
                            controller.quit(body)

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
fun DiePretermDialog(showDialog:MutableState<Boolean>,item: PretermAdmission,controller: PretermAdmissionsController){

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
                LaunchedEffect(Unit) { showDialog.value=false }
                patientController.indexAdmissionsByHospitalAndPatient()
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
                            val body = PretermAdmissionBody(
                                id = item.id,
                                exitDate = exitTime.value,
                                patientDie = 1,
                                patientQuit = 0,
                                updatedById = user?.id
                            )
                            controller.die(body)

                        }

                    }
                }
            }
        }
        DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)
    }
}


@Composable
private fun NewPatientDialog(showDialog:MutableState<Boolean>){
    val genders= listOf(MALE_LABEL, FEMALE_LABEL)
    val selectedGender = remember { mutableStateOf(genders[0]) }
    val settingsController: SettingsController = viewModel()
    val controller: PatientsController= viewModel()
    val state by controller.singleState.observeAsState()

    //var patient by remember { mutableStateOf<Patient?>(null) }
    val firstName           =   remember { mutableStateOf("") }
    val secondName          =   remember { mutableStateOf("") }
    val thirdName           =   remember { mutableStateOf("") }
    val fourthName          =   remember { mutableStateOf("") }
    val nationalId          =   remember { mutableStateOf("") }
    val mobile              =   remember { mutableStateOf("") }
    val alternativeMobile   =   remember { mutableStateOf("") }
    val nationality         =   remember { mutableStateOf<Nationality?>(null) }
    var nationalities       by  remember { mutableStateOf<List<Nationality>>(emptyList()) }
    var enabled by remember { mutableStateOf(true) }
    LaunchedEffect(firstName.value,nationalId.value,nationality.value) {
        enabled = (firstName.value.trim()!="") && (nationalId.value.trim()!="") && (nationality.value!=null)
    }
    val nationalitiesState by settingsController.nationalitiesState.observeAsState()

    if(showDialog.value){
        when(nationalitiesState){
            is UiState.Loading->{}
            is UiState.Success->{
                val s = nationalitiesState as UiState.Success<NationalitiesResponse>
                val r = s.data
                val data = r.data
                nationalities=data
            }
            is UiState.Error->{}
            else->{settingsController.nationalitiesIndex()}
        }
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) { showDialog.value=false }
            }
            else->{}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier=Modifier.fillMaxWidth().background(color=WHITE, shape = rcs(5)),
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    IconButton(R.drawable.ic_cancel_red) {
                        showDialog.value=false
                    }
                }
                LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f)) {
                    item{
                        CustomInput(value = firstName, label = FIRST_NAME_LABEL)
                        CustomInput(value = secondName, label = FATHER_NAME_LABEL)
                        CustomInput(value = thirdName, label = GRAND_FATHER_NAME_LABEL)
                        CustomInput(value = fourthName, label = FOURTH_NAME_LABEL)
                        Box(modifier=Modifier.padding(horizontal = 5.dp)){
                            ComboBox(selectedItem = nationality, loadedItems = nationalities, title = NATIONALITY_LABEL,
                                hasTitle = false,
                                selectedContent = {
                                    CustomInput(nationality.value?.name?: SELECT_NATIONALITY_LABEL)

                                }) {
                                Label(it?.name?:"")
                            }

                        }
                        CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
                        CustomInput(value = mobile, label = MOBILE_NUMBER_LABEL)
                        CustomInput(value = alternativeMobile, label = ALTERNATIVE_MOBILE_NUMBER_LABEL)
                        Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                            ComboBox(hasTitle = false,selectedItem = selectedGender, loadedItems = genders, selectedContent = {
                                CustomInput(value=if(selectedGender.value.trim()=="") SELECT_GENDER_LABEL else selectedGender.value,
                                    readOnly = true,
                                    icon = R.drawable.ic_arrow_drop_down_blue)
                            }) {
                                Label(
                                    it,
                                    color = if(selectedGender.value==it) BLUE else BLACK
                                )
                            }
                        }
                    }
                }
                VerticalSpacer()
                CustomButton(label = SAVE_CHANGES_LABEL
                    , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                    enabled = enabled
                ) {
                    if(firstName.value.trim()!="" && secondName.value.trim()!=""
                        && thirdName.value.trim()!="" && mobile.value.trim()!=""
                        && nationalId.value.trim()!="" && nationality.value!=null){
                        val body = PatientBody(
                            firstName = firstName.value,
                            secondName = secondName.value,
                            thirdName = thirdName.value,
                            fourthName = fourthName.value,
                            mobileNumber = mobile.value,
                            altMobileNumber = alternativeMobile.value,
                            nationalId = nationalId.value,
                            nationalityId = nationality.value?.id
                        )
                        controller.store(body)

                    }
                }
            }
        }
    }
}


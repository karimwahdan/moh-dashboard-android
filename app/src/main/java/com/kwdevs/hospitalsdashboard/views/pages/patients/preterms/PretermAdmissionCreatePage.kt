package com.kwdevs.hospitalsdashboard.views.pages.patients.preterms

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsIndexRoute
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
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.ENTER_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXIT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FEMALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FIRST_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOURTH_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAND_FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.INFANT_NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOTHER_NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_ADMISSION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DIE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_QUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_GENDER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_ENTER_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_EXIT_DATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PretermAdmissionCreatePage(navHostController: NavHostController){

    val hospital = Preferences.Hospitals().get()
    val settingsController:SettingsController= viewModel()
    val settingsState by settingsController.hospitalWardOptionsState.observeAsState()
    val patientController: PatientsController = viewModel()
    val controller: PretermAdmissionsController = viewModel()

    val state by controller.admissionsState.observeAsState()
    val mothersState by patientController.allState.observeAsState()
    val patientsState by patientController.allState.observeAsState()

    val selectedPatient = remember { mutableStateOf<Patient?>(null) }
    val motherNationalId = remember { mutableStateOf("") }
    val patientNationalId = remember { mutableStateOf("") }

    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    val selectedMother = remember { mutableStateOf<Patient?>(null) }
    var mothers by remember { mutableStateOf<List<Patient>>(emptyList()) }

    val showNewPatientDialog = remember { mutableStateOf(false) }
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

    LaunchedEffect(patients) { if(patients.isEmpty()) selectedPatient.value=null  }
    LaunchedEffect(patientQuit.value,patientDie.value) {
        if(patientQuit.value) patientDie.value=false
        if(patientDie.value) patientQuit.value=false
    }
    NewPatientDialog(showNewPatientDialog,patientController)
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
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) { navHostController.navigate(PretermAdmissionsIndexRoute.route) }
        }
        else->{}
    }
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = NEW_ADMISSION_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                    item{
                        ColumnContainer {
                            //Mother National Id
                            Row(modifier=Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){
                                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                    CustomInput(value = motherNationalId, label = MOTHER_NATIONAL_ID_LABEL)
                                }
                                CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                                    if(motherNationalId.value.trim() !="" && motherNationalId.value.length>2) patientController.filterFemale(motherNationalId.value)
                                }
                            }

                            //Mother ComboBox
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

                            //New Mother Dialog
                            if(mothers.isEmpty() || motherNationalId.value.trim() == "" ){
                                CustomButton(label = ADD_NEW_LABEL
                                    , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                                ) {
                                    showNewPatientDialog.value=true
                                }
                            }
                            VerticalSpacer(10)

                            //Infant National Id
                            Row(modifier=Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){
                                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                    CustomInput(value = patientNationalId, label = INFANT_NATIONAL_ID_LABEL)
                                }
                                CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                                    if(patientNationalId.value.trim() !="" && patientNationalId.value.length>2) patientController.filter(patientNationalId.value)
                                }
                            }

                            //Infant ComboBox
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

                            //New Patient Dialog
                            if(patients.isEmpty() || patientNationalId.value.trim() == "" ){
                                CustomButton(label = ADD_NEW_LABEL
                                    , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                                ) {
                                    showNewPatientDialog.value=true
                                }
                            }

                            //Medical State
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

                            HorizontalDivider()

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

                            Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center){
                                CustomButton(label = SAVE_CHANGES_LABEL
                                    , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                                ){
                                    val body = PretermAdmissionBody(
                                        patientId = selectedPatient.value?.id,
                                        hospitalId = hospital?.id,
                                        motherId = selectedMother.value?.id,
                                        patientStateId = selectedPatientMedicalState.value?.id,
                                        admissionDate = enterTime.value,
                                        exitDate = exitTime.value,
                                        patientDie = if(patientDie.value) 1 else null,
                                        patientQuit = if(patientQuit.value) 1 else null,
                                        createdById = Preferences.User().get()?.id
                                    )
                                    controller.storeAdmission(body)

                                }
                            }
                        }
                    }
                }
            }
        }
    }
    DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
    DatePickerWidget(showExitTimePicker,exitTimeState,exitTime)

}

@Composable
private fun NewPatientDialog(showDialog:MutableState<Boolean>,controller: PatientsController){
    val genders= listOf(MALE_LABEL,FEMALE_LABEL)
    val selectedGender = remember { mutableStateOf(genders[0]) }
    val settingsController: SettingsController = viewModel()
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
    if(showDialog.value){
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
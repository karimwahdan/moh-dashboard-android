package com.kwdevs.hospitalsdashboard.views.pages.patients.admissions

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
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientAdmissionBody
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalWardController
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.wards.HospitalWardsResponse
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardType
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.NormalUserWardsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALTERNATIVE_MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BED_CODE_LABEL
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
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
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
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_WARDS
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientWardAdmissionCreatePage(navHostController: NavHostController){

    val hospital = Preferences.Hospitals().get()
    val settingsController:SettingsController= viewModel()
    val settingsState by settingsController.hospitalWardOptionsState.observeAsState()
    val controller: PatientsController = viewModel()
    val state by controller.admissionState.observeAsState()
    val filteredState by controller.allState.observeAsState()
    val bedCode = remember { mutableStateOf("") }
    val selectedPatient = remember { mutableStateOf<Patient?>(null) }
    val nationalId = remember { mutableStateOf("") }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    //val showNewPatientDialog = remember { mutableStateOf(false) }

    var patientMedicalStates by remember { mutableStateOf<List<PatientState>>(emptyList()) }
    val selectedPatientMedicalState = remember { mutableStateOf<PatientState?>(null) }
    var wardTypes by remember { mutableStateOf<List<WardType>>(emptyList()) }
    val selectedWardType = remember { mutableStateOf<WardType?>(null) }
    val wardsController:HospitalWardController= viewModel()
    val wardsState by wardsController.state.observeAsState()
    var wards by remember { mutableStateOf<List<HospitalWard>>(emptyList()) }
    val selectedWard = remember { mutableStateOf<HospitalWard?>(null) }
    val patientQuit = remember { mutableStateOf(false) }
    val patientDie = remember { mutableStateOf(false) }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }

    val exitTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val exitTime = remember { mutableStateOf("") }

    val showExitTimePicker = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(patients) { if(patients.isEmpty()) selectedPatient.value=null  }
    LaunchedEffect(patientQuit.value,patientDie.value) {
        if(patientQuit.value) patientDie.value=false
        if(patientDie.value) patientQuit.value=false
    }
    //NewPatientDialog(showNewPatientDialog,controller)
    when(settingsState){
        is UiState.Loading->{
            isLoading=true;isError=false;isSuccess=false
        }
        is UiState.Error->{
            isLoading=false;isError=true;isSuccess=false

        }
        is UiState.Success->{
            val s = settingsState as UiState.Success<HospitalWardAdmissionOptionsData>
            val r = s.data
            val data = r.data
            wardTypes=data.types
            patientMedicalStates=data.states

        }
        else->{ LaunchedEffect(Unit) { settingsController.wardAdmissionOptions()  }}
    }
    LaunchedEffect(Unit) {
        val savedPatient=Preferences.Patients().get()
        if(savedPatient!=null){
            controller.filter(savedPatient.nationalId?:"")
        }
    }
    when(filteredState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = filteredState as UiState.Success<PatientsResponse>
                val r = s.data
                val data = r.data
                patients = data
                if(patients.isNotEmpty())selectedPatient.value=patients[0]

            }
        }
        else->{}
    }

    when(wardsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = wardsState as UiState.Success<HospitalWardsResponse>
                val r = s.data
                val data = r.data
                wards=data
            }

        }
        else->{ }
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) { navHostController.navigate(PatientWardAdmissionsIndexRoute.route) }
        }
        else->{}
    }
    val showSheet = remember { mutableStateOf(false) }
    val viewType=Preferences.ViewTypes().get()
    Container(
        title = NEW_ADMISSION_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            when(viewType){
                ViewType.BY_PATIENT->{
                    val patient=Preferences.Patients().get()
                    if((patient?.admissions ?: emptyList()).isNotEmpty()){
                        navHostController.navigate(PatientWardAdmissionsIndexRoute.route)
                    }
                    else{ navHostController.navigate(PatientsIndexRoute.route) }
                }
                ViewType.BY_HOSPITAL->{navHostController.navigate(HospitalHomeRoute.route)}
                ViewType.BY_WARD->{navHostController.navigate(NormalUserWardsIndexRoute.route)}
                else->{
                    navHostController.navigate(HospitalHomeRoute.route)

                }
            }
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                    CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
                }
                CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                    if(nationalId.value.trim() !="" && nationalId.value.length>2) controller.filter(nationalId.value)
                }
            }
            if(patients.isEmpty() || nationalId.value.trim() == "" ){
                CustomButton(label = ADD_NEW_LABEL
                    , buttonShape = RectangleShape, enabledBackgroundColor = BLUE
                ) {
                    navHostController.navigate(PatientCreateRoute.route)
                    Preferences.CrudTypes().set(CrudType.CREATE)
                    //showNewPatientDialog.value=true
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
                    IconButton(icon=R.drawable.ic_cancel_red) { nationalId.value="";selectedPatient.value=null }
                }else{
                    Box(modifier=Modifier.width(26.dp))
                }
            }

            VerticalSpacer(10)

            HorizontalDivider()

            LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                item{
                    ColumnContainer {

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
            }

            VerticalSpacer()

            Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label = SAVE_CHANGES_LABEL
                    , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                ){
                    val body = PatientAdmissionBody(
                        patientId = selectedPatient.value?.id,
                        hospitalId = hospital?.id,
                        wardId = selectedWard.value?.id,
                        wardTypeId = selectedWardType.value?.id,
                        patientStateId = selectedPatientMedicalState.value?.id,
                        bedCode = bedCode.value,
                        admissionTime = enterTime.value,
                        exitTime = exitTime.value,
                        patientDie = if(patientDie.value) 1 else null,
                        patientQuit = if(patientQuit.value) 1 else null,
                        createdById = Preferences.User().get()?.id
                    )
                    controller.storeAdmission(body)
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
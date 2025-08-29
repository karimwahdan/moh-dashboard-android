package com.kwdevs.hospitalsdashboard.views.pages.patients.cancerCures

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.CancerCureBody
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.patients.CancerCureController
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.models.settings.cureTypes.CureType
import com.kwdevs.hospitalsdashboard.models.settings.cureTypes.CureTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALTERNATIVE_MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCER_CURE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
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
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_GENDER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SESSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancerCureCreatePage(navHostController: NavHostController){
    val savedPatient=Preferences.Patients().get()
    val hospital=Preferences.Hospitals().get()
    val months= listOf(
        Pair(1,"يناير"),
        Pair(2,"فبراير"),
        Pair(3,"مارس"),
        Pair(4,"ابريل"),
        Pair(5,"مايو"),
        Pair(6,"يونيو"),
        Pair(7,"يوليو"),
        Pair(8,"اغسطس"),
        Pair(9,"سبمتمبر"),
        Pair(10,"اكتوبر"),
        Pair(11,"نوفمبر"),
        Pair(12,"ديسمبر"),
        )
    val selectedMonth = remember { mutableStateOf<Pair<Int,String>?>(null) }
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.cureTypesStatesState.observeAsState()
    var cureTypes by remember { mutableStateOf<List<CureType>>(emptyList()) }
    val selectedCureType = remember { mutableStateOf<CureType?>(null) }

    val controller:CancerCureController= viewModel()
    val state by controller.singleState.observeAsState()

    val patientsController: PatientsController = viewModel()
    val filteredState by patientsController.allState.observeAsState()
    val selectedPatient = remember { mutableStateOf(savedPatient) }
    val nationalId = remember { mutableStateOf(EMPTY_STRING) }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    val showNewPatientDialog = remember { mutableStateOf(false) }
    val sessions = remember { mutableStateOf(EMPTY_STRING) }
    val viewType = Preferences.ViewTypes().get()

    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
           LaunchedEffect(Unit){navHostController.navigate(CancerCureIndexRoute.route)}
        }
        else->{}
    }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<CureTypeResponse>
            val r = s.data
            val data=r.data
            cureTypes=data
        }
        else->{
            settingsController.cureTypesIndex()
        }
    }
    when(filteredState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = filteredState as UiState.Success<PatientsResponse>
            val r = s.data
            val data = r.data
            patients = data
            if(patients.isNotEmpty())selectedPatient.value=patients[0]
        }
        else->{}
    }

    NewPatientDialog(showNewPatientDialog,patientsController)
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = CANCER_CURE_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            when(viewType){
                ViewType.BY_PATIENT->{

                    navHostController.navigate(PatientsIndexRoute.route)
                }
                else->{navHostController.navigate(HospitalHomeRoute.route)}
            }
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            if(savedPatient==null){
                Row(modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                        CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
                    }
                    CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                        if(nationalId.value.trim() !=EMPTY_STRING && nationalId.value.length>2) patientsController.filter(nationalId.value)
                    }
                }
                if(patients.isEmpty() || nationalId.value.trim() == EMPTY_STRING ){
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
                            CustomInput(value=if(selectedPatient.value==null) "Select Patient" else "${selectedPatient.value?.firstName?:EMPTY_STRING} ${selectedPatient.value?.secondName?:EMPTY_STRING} ${selectedPatient.value?.thirdName?:EMPTY_STRING} ${selectedPatient.value?.fourthName?:EMPTY_STRING}",
                                readOnly = true,
                                icon = R.drawable.ic_arrow_drop_down_blue)
                        }) {
                            Label(
                                "${it?.firstName?:EMPTY_STRING} ${it?.secondName?:EMPTY_STRING} ${it?.thirdName?:EMPTY_STRING} ${it?.fourthName?:EMPTY_STRING}",
                                color = if(selectedPatient.value==it) BLUE else BLACK
                            )
                        }
                    }
                    if(selectedPatient.value!=null){
                        IconButton(icon=R.drawable.ic_cancel_red) { nationalId.value=EMPTY_STRING;selectedPatient.value=null }
                    }else{
                        Box(modifier=Modifier.width(26.dp))
                    }
                }
                VerticalSpacer(10)
                HorizontalDivider()
            }
            Box(modifier=Modifier.padding(5.dp)){
                ComboBox(hasTitle = false,selectedItem = selectedCureType, loadedItems = cureTypes, selectedContent = {
                    CustomInput(value=selectedCureType.value?.name?:"Select Cure Type",
                        readOnly = true,
                        icon = R.drawable.ic_arrow_drop_down_blue)
                }) {
                    Label(
                        it?.name?:EMPTY_STRING,
                        color = if(selectedCureType.value==it) BLUE else BLACK
                    )
                }
            }
            CustomInput(label = SESSIONS_LABEL,value=sessions, keyboardOptions = numericKeyBoard,enabled=true )
            Box(modifier=Modifier.padding(horizontal = 5.dp)){
                ComboBox(hasTitle = false,selectedItem = selectedMonth, loadedItems = months, selectedContent = {
                    CustomInput(value=selectedMonth.value?.second?:"Select Month",
                        readOnly = true,
                        icon = R.drawable.ic_arrow_drop_down_blue)
                }) {
                    Label(
                        it?.second?:EMPTY_STRING,
                        color = if(selectedMonth.value==it) BLUE else BLACK
                    )
                }
            }
            Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label = SAVE_CHANGES_LABEL
                    , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                ){
                    val body = CancerCureBody(
                        patientId = selectedPatient.value?.id,
                        hospitalId = hospital?.id,
                        month = selectedMonth.value?.first,
                        sessions = sessions.value.toInt(),
                        cureTypeId = selectedCureType.value?.id,
                        createdById = Preferences.User().get()?.id
                    )
                    controller.storeNormal(body)

                }
            }
        }
    }
}


@Composable
private fun NewPatientDialog(showDialog: MutableState<Boolean>, controller: PatientsController){
    val genders= listOf(MALE_LABEL, FEMALE_LABEL)
    val selectedGender = remember { mutableStateOf(genders[0]) }
    val settingsController: SettingsController = viewModel()
    val state by controller.singleState.observeAsState()
    //var patient by remember { mutableStateOf<Patient?>(null) }
    val firstName           =   remember { mutableStateOf(EMPTY_STRING) }
    val secondName          =   remember { mutableStateOf(EMPTY_STRING) }
    val thirdName           =   remember { mutableStateOf(EMPTY_STRING) }
    val fourthName          =   remember { mutableStateOf(EMPTY_STRING) }
    val nationalId          =   remember { mutableStateOf(EMPTY_STRING) }
    val mobile              =   remember { mutableStateOf(EMPTY_STRING) }
    val alternativeMobile   =   remember { mutableStateOf(EMPTY_STRING) }
    val nationality         =   remember { mutableStateOf<Nationality?>(null) }
    var nationalities       by  remember { mutableStateOf<List<Nationality>>(emptyList()) }
    var enabled by remember { mutableStateOf(true) }
    LaunchedEffect(firstName.value,nationalId.value,nationality.value) {
        enabled = (firstName.value.trim()!=EMPTY_STRING) && (nationalId.value.trim()!=EMPTY_STRING) && (nationality.value!=null)
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
            Column(modifier= Modifier.fillMaxWidth().background(color= WHITE, shape = rcs(5)),
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                    IconButton(R.drawable.ic_cancel_red) {
                        showDialog.value=false
                    }
                }
                LazyColumn(modifier= Modifier.fillMaxWidth().weight(1f)) {
                    item{
                        CustomInput(value = firstName, label = FIRST_NAME_LABEL)
                        CustomInput(value = secondName, label = FATHER_NAME_LABEL)
                        CustomInput(value = thirdName, label = GRAND_FATHER_NAME_LABEL)
                        CustomInput(value = fourthName, label = FOURTH_NAME_LABEL)
                        Box(modifier= Modifier.padding(horizontal = 5.dp)){
                            ComboBox(selectedItem = nationality, loadedItems = nationalities, title = NATIONALITY_LABEL,
                                hasTitle = false,
                                selectedContent = {
                                    CustomInput(nationality.value?.name?: SELECT_NATIONALITY_LABEL)

                                }) {
                                Label(it?.name?:EMPTY_STRING)
                            }

                        }
                        CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
                        CustomInput(value = mobile, label = MOBILE_NUMBER_LABEL)
                        CustomInput(value = alternativeMobile, label = ALTERNATIVE_MOBILE_NUMBER_LABEL)
                        Box(modifier= Modifier.padding(horizontal = 5.dp).weight(1f)){
                            ComboBox(hasTitle = false,selectedItem = selectedGender, loadedItems = genders, selectedContent = {
                                CustomInput(value=if(selectedGender.value.trim()==EMPTY_STRING) SELECT_GENDER_LABEL else selectedGender.value,
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
                    if(firstName.value.trim()!=EMPTY_STRING && secondName.value.trim()!=EMPTY_STRING
                        && thirdName.value.trim()!=EMPTY_STRING && mobile.value.trim()!=EMPTY_STRING
                        && nationalId.value.trim()!=EMPTY_STRING && nationality.value!=null){
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
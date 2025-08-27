package com.kwdevs.hospitalsdashboard.views.pages.patients.babybirths

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.kwdevs.hospitalsdashboard.bodies.patients.BabyBirthBody
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.controller.patients.BabyBirthController
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.routes.BabyBirthIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALTERNATIVE_MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BABY_DIED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHDATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTH_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CESAREAN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FIRST_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOURTH_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAND_FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOTHER_DIED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NORMAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BIRTH_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_BIRTHDATE_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthCreatePage(navHostController: NavHostController){
    val controller: BabyBirthController = viewModel()
    val state by controller.singleState.observeAsState()
    val user = Preferences.User().get()
    val birthTypes = listOf(NORMAL_LABEL,CESAREAN_LABEL )
    val selectedBirthType = remember { mutableStateOf("") }
    val hospital=Preferences.Hospitals().get()
    val birthdateState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val birthdate = remember { mutableStateOf("") }
    val showBirthdatePicker = remember { mutableStateOf(false) }
    val selectedPatient = remember { mutableStateOf<Patient?>(null) }
    val nationalId = remember { mutableStateOf("") }
    val motherDied= remember { mutableStateOf(false) }
    val babyDied= remember { mutableStateOf(false) }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    val patientsController: PatientsController = viewModel()
    val filteredState by patientsController.allState.observeAsState()
    val showNewPatientDialog = remember { mutableStateOf(false) }

    LaunchedEffect(patients) { if(patients.isEmpty()) selectedPatient.value=null  }
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
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) { navHostController.navigate(BabyBirthIndexRoute.route) }
        }
        else->{}
    }
    NewPatientDialog( showNewPatientDialog, patientsController)
    DatePickerWidget(showBirthdatePicker,birthdateState,birthdate)
    val showSheet = remember { mutableStateOf(false) }

    Container(title = BIRTHS_LABEL,
        showSheet = showSheet) {
        Row(modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Box(modifier=Modifier.fillMaxWidth().padding(vertical=5.dp, horizontal = 10.dp).weight(1f)){
                CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
            }
            CustomButtonWithImage(icon = R.drawable.ic_filter_blue, iconSize = 26, maxWidth = 26) {
                if(nationalId.value.trim() !="" && nationalId.value.length>2) patientsController.filterFemale(nationalId.value)
            }
        }
        if(patients.isEmpty() || nationalId.value.trim() == "" ){
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
                IconButton(icon=R.drawable.ic_cancel_red) { nationalId.value="";selectedPatient.value=null }
            }
            else{
                Box(modifier=Modifier.width(26.dp))
            }
        }
        VerticalSpacer()
        HorizontalDivider()
        VerticalSpacer()
        Box(modifier=Modifier.padding(horizontal = 5.dp)){
            ComboBox(selectedItem = selectedBirthType, loadedItems = birthTypes, title = BIRTH_TYPE_LABEL,
                hasTitle = false,
                selectedContent = {
                    CustomInput(if(selectedBirthType.value.trim()=="") SELECT_BIRTH_TYPE_LABEL else selectedBirthType.value)

                }) {
                Label(it)
            }

        }

        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            Label(label = BIRTHDATE_LABEL, text = birthdate.value)

        }
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = SHOW_BIRTHDATE_PICKER_LABEL ,
                enabledBackgroundColor = ORANGE,
                onClick = { showBirthdatePicker.value = !showBirthdatePicker.value })

        }
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomCheckbox(label = MOTHER_DIED_LABEL,active = motherDied)
            CustomCheckbox(label = BABY_DIED_LABEL, active = babyDied)
        }
        Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = SAVE_CHANGES_LABEL
                , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
            ){
                val body = BabyBirthBody(
                    motherId = selectedPatient.value?.id,
                    hospitalId = hospital?.id,
                    birthTypeId = if(selectedBirthType.value== NORMAL_LABEL) 1 else 2,
                    birthdate = birthdate.value,
                    motherDied = if(motherDied.value) 1 else null,
                    babyDied = if(babyDied.value) 1 else null,
                    createdById = user?.id?:0
                )
                controller.storeNormal(body)

            }
        }
    }

}


@Composable
private fun NewPatientDialog(showDialog: MutableState<Boolean>, controller: PatientsController){
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
            Column(modifier=Modifier.fillMaxWidth().background(color= WHITE, shape = rcs(5)),
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
                            nationalityId = nationality.value?.id,
                            gender = 2
                        )
                        controller.store(body)

                    }
                }
            }
        }
    }
}
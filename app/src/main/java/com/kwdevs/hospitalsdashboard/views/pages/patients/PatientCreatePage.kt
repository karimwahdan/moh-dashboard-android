package com.kwdevs.hospitalsdashboard.views.pages.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.patients.PatientsController
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientCode
import com.kwdevs.hospitalsdashboard.models.patients.PatientSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.nationality.Nationality
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALTERNATIVE_MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomDialog
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DELETE_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FEMALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FIRST_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOURTH_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAND_FATHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MALE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PINK
import com.kwdevs.hospitalsdashboard.views.assets.PatientCreatedDialog
import com.kwdevs.hospitalsdashboard.views.assets.PatientFullName
import com.kwdevs.hospitalsdashboard.views.assets.RESTORE_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_GENDER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.UPDATE_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.WhiteLabel
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientCreatePage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    val crudType=Preferences.CrudTypes().get()
    val isCreate=crudType==CrudType.CREATE
    val isUpdate=crudType==CrudType.UPDATE
    val isDelete=crudType==CrudType.DELETE
    val isRestore=crudType==CrudType.RESTORE
    val old=Preferences.Patients().get()
    val genders= listOf(MALE_LABEL, FEMALE_LABEL)
    val selectedGender= remember { mutableStateOf(genders[0]) }
    val settingsController  :   SettingsController = viewModel()
    val firstName           =   remember { mutableStateOf("") }
    val secondName          =   remember { mutableStateOf("") }
    val thirdName           =   remember { mutableStateOf("") }
    val fourthName          =   remember { mutableStateOf("") }
    val nationalId          =   remember { mutableStateOf("") }
    val patientCode         =   remember { mutableStateOf("") }
    val mobile              =   remember { mutableStateOf("") }
    val alternativeMobile   =   remember { mutableStateOf("") }
    val nationality         =   remember { mutableStateOf<Nationality?>(null) }
    var nationalities       by  remember { mutableStateOf<List<Nationality>>(emptyList()) }
    var patient             by  remember { mutableStateOf<Patient?>(null) }
    var enabled             by  remember { mutableStateOf(true) }
    val nationalitiesState  by  settingsController.nationalitiesState.observeAsState()
    val controller          : PatientsController = viewModel()
    val state               by  controller.singleState.observeAsState()
    val active = remember { mutableStateOf(true) }
    val showCrudDialog = remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }

    when(state){
        is UiState.Loading->{
            enabled=false
            isLoading=true;isError=false;isSuccess.value=false
        }
        is UiState.Error->{
            enabled=true
            isLoading=false;isError=true;isSuccess.value=false
        }
        is UiState.Success->{
            isLoading=false;isError=false;isSuccess.value=true
            val s = state as UiState.Success<PatientSingleResponse>
            val r = s.data
            val data = r.data
            patient=data
            Preferences.Patients().set(data)
            enabled=false
            LaunchedEffect(Unit) {
                //if(crudType==CrudType.CREATE){ navHostController.navigate(AdmissionCreateRoute.route) }
                //else {navHostController.navigate(PatientsIndexRoute.route)}
            }
        }
        else->{ enabled=true }
    }
    when(nationalitiesState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = nationalitiesState as UiState.Success<NationalitiesResponse>
            val r = s.data
            val data = r.data
            nationalities=data
            LaunchedEffect(Unit) { if(crudType==CrudType.UPDATE) nationality.value=old?.nationality }

        }
        else->{
            settingsController.nationalitiesIndex()
        }
    }
    LaunchedEffect(Unit) {
        if(crudType==CrudType.UPDATE) {
            old?.let{
                firstName.value=old.firstName?:""
                secondName.value=old.secondName?:""
                thirdName.value=old.thirdName?:""
                fourthName.value=it.fourthName?:""
                selectedGender.value=if(it.gender==1) genders[0] else genders[1]
                nationalId.value=it.nationalId?:""
                mobile.value=it.mobileNumber?:""
                alternativeMobile.value=it.alternativeMobileNumber?:""
                patientCode.value=it.codes?.find {code-> code.hospitalId==(hospital?.id?:0) }?.code?:""
            }

        }
    }
    val showSheet = remember { mutableStateOf(false) }
    Container(title = if(isCreate) NEW_PATIENT_LABEL else if(isUpdate) UPDATE_PATIENT_LABEL else if(isDelete) DELETE_PATIENT_LABEL else if(isRestore) RESTORE_PATIENT_LABEL else PATIENTS_LABEL,
        showSheet=showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            navHostController.navigate(PatientsIndexRoute.route)
                        },
        sheetContent = {
            if(firstName.value.trim()==""){
                WhiteLabel(text = "First Name is Required")
            }
            else if(secondName.value.trim()==""){
                WhiteLabel(text="Father Name is Required")
            }
            else if(thirdName.value.trim()==""){
                WhiteLabel("Grand Father Name is Required")
            }
            else if(fourthName.value.trim()==""){
                WhiteLabel("Family Name is Required")
            }
            else if(patientCode.value.trim()==""){
                WhiteLabel("Patient Code is Required")
            }
            else if(nationality.value==null){
                WhiteLabel("Select Nationality")
            }
            else if(nationalId.value.trim()==""){
                WhiteLabel("National Id is Required")
            }
            else if(selectedGender.value.trim()==""){
                WhiteLabel("Select Gender")
            }
            else if(mobile.value.trim()==""){
                WhiteLabel("Mobile Number is Required")
            }
        }) {
        Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if(isUpdate){
                Label("يرجى التأكد من صحة البيانات قبل تعديلها", color = Color.Red, fontWeight = FontWeight.Bold, maximumLines = 2)}
            LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                item{
                    CustomInput(value = firstName, label = FIRST_NAME_LABEL)
                    CustomInput(value = secondName, label = FATHER_NAME_LABEL)
                    CustomInput(value = thirdName, label = GRAND_FATHER_NAME_LABEL)
                    CustomInput(value = fourthName, label = FOURTH_NAME_LABEL)
                    CustomInput(value = patientCode, label = PATIENT_CODE_LABEL)

                    Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(selectedItem = nationality, loadedItems = nationalities, title = NATIONALITY_LABEL,
                            selectedContent = {
                                CustomInput(nationality.value?.name?: SELECT_NATIONALITY_LABEL)

                            }) {
                            Label(it?.name?:"")
                        }

                    }
                    Box(modifier=Modifier.padding(horizontal = 5.dp).weight(1f)){
                        ComboBox(hasTitle = false,selectedItem = selectedGender, loadedItems = genders, selectedContent = {
                            CustomInput(value=if(selectedGender.value.trim()=="") SELECT_GENDER_LABEL else selectedGender.value,
                                readOnly = true,
                                icon = R.drawable.ic_arrow_drop_down_blue)
                        }) {
                            Label(it, color = if(selectedGender.value==it) BLUE else BLACK)
                        }
                    }

                    CustomInput(value = nationalId, label = NATIONAL_ID_LABEL)
                    CustomInput(value = mobile, label = MOBILE_NUMBER_LABEL)
                    CustomInput(value = alternativeMobile, label = ALTERNATIVE_MOBILE_NUMBER_LABEL)
                    CustomCheckbox(ACTIVE_LABEL,active)
                }
            }
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){

                CustomButton(label = SAVE_CHANGES_LABEL,
                    buttonShape = RectangleShape,
                    enabledBackgroundColor = BLUE,
                    enabled = enabled) {
                    if(firstName.value.trim()!="" && secondName.value.trim()!=""
                        && thirdName.value.trim()!="" && mobile.value.trim()!=""
                        && nationalId.value.trim()!="" && nationality.value!=null){
                        showCrudDialog.value=true
                        /*
                            val body = PatientBody(
                            id = if(isUpdate || isDelete || isRestore ) old?.id else null,
                            firstName = firstName.value,
                            secondName = secondName.value,
                            thirdName = thirdName.value,
                            fourthName = fourthName.value,
                            mobileNumber = mobile.value,
                            altMobileNumber = alternativeMobile.value,
                            nationalId = nationalId.value,
                            nationalityId = nationality.value?.id,
                            active = if(active.value) 1 else 0,
                            gender = if(selectedGender.value == genders[0]) 1 else 2,
                            createdById = if(isCreate) user?.id else null,
                            updatedById = if(isUpdate) user?.id else null,
                            deletedById = if(isDelete) user?.id else null
                        )
                         */
                        //if(isCreate) controller.store(body)
                        //else if(isUpdate) controller.update(body)
                        //else if(isDelete) controller.delete(body)
                        //else if(isRestore) controller.restore(body)

                    }
                    else {showSheet.value=true}
                }
            }
        }
    }
SavePatientDialog(showDialog= showCrudDialog,
    patient = Patient(id=if(isUpdate) old?.id else null,
        firstName=firstName.value,
        secondName=secondName.value,
        thirdName=thirdName.value,
        fourthName=fourthName.value,
        nationalId=nationalId.value,
        nationality = nationality.value,
        mobileNumber = mobile.value,
        patientCode = PatientCode(patientId = if(isUpdate) old?.id else null,
            hospitalId = Preferences.Hospitals().get()?.id,
            code = patientCode.value
        ),
        alternativeMobileNumber = if(alternativeMobile.value.trim()!="") alternativeMobile.value.trim() else null,
        gender =if(selectedGender.value==genders[0]) 1 else 2,
        active = active.value),
    controller=controller,isCreate,isUpdate, isDelete, isRestore)

    PatientCreatedDialog(isSuccess, navHostController = navHostController)
}


@Composable
fun SavePatientDialog(showDialog:MutableState<Boolean>,
                      patient:Patient,
                      controller: PatientsController,
                      isCreate:Boolean,
                      isUpdate:Boolean,
                      isDelete:Boolean,
                      isRestore:Boolean){
    val user=Preferences.User().get()
    val gender=patient.gender
    val nationality=patient.nationality
    val mobile=patient.mobileNumber
    val altMobile=patient.alternativeMobileNumber
    val active = patient.active
    val nationalId=patient.nationalId

    CustomDialog(showDialog) {
        Label(SAVE_PROMPT)
        PatientFullName(patient = patient)
        Row(verticalAlignment = Alignment.CenterVertically){
            Span(if(gender==1) MALE_LABEL else if(gender==2) FEMALE_LABEL else "N/A", backgroundColor = if(gender==1) BLUE else if(gender==2) PINK else Color.Red, color = WHITE)
            HorizontalSpacer()
            if(gender==1) Icon(R.drawable.ic_male) else if(gender==2) Icon(R.drawable.ic_female)
            HorizontalSpacer()
            Span(text=if(active==true) ACTIVE_LABEL else IN_ACTIVE_LABEL, color = WHITE, backgroundColor = if(active==true) GREEN else Color.Red)
            HorizontalSpacer()
            Span(nationality?.name?:"", backgroundColor = BLUE, color = WHITE)
        }
        Label(text=nationalId?.trim()?:"N/A",label= NATIONAL_ID_LABEL)
        Label(text=mobile?.trim()?:"N/A", label = MOBILE_NUMBER_LABEL)
        Label(text = altMobile?.trim()?:"N/A", label = ALTERNATIVE_MOBILE_NUMBER_LABEL)
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = SAVE_CHANGES_LABEL,
                buttonShape = RectangleShape,
                enabledBackgroundColor = BLUE,
                enabled = true) {
                if(patient.firstName?.trim()!="" && patient.secondName?.trim()!=""
                    && patient.thirdName?.trim()!="" && mobile?.trim()!=""
                    && nationalId?.trim()!="" && nationality!=null){
                    val body = PatientBody(
                        id = patient.id,
                        firstName = patient.firstName,
                        secondName = patient.secondName,
                        thirdName = patient.thirdName,
                        fourthName = patient.fourthName,
                        mobileNumber = mobile,
                        altMobileNumber = altMobile,
                        nationalId = nationalId,
                        patientCode = patient.patientCode?.code,
                        nationalityId = nationality.id,
                        active = if(active==true) 1 else 0,
                        gender = gender,
                        createdById = if(isCreate) user?.id else null,
                        updatedById = if(isUpdate) user?.id else null,
                        deletedById = if(isDelete) user?.id else null
                    )
                    if(isCreate) controller.store(body)
                    else if(isUpdate) controller.update(body)
                    else if(isDelete) controller.delete(body)
                    else if(isRestore) controller.restore(body)

                    showDialog.value=false

                }
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.pages.reports.monthlyIssuingReports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.RecipientType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIssuingReportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsIndexRoute
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.EXPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GOVERNMENTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OUT_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PRIVATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RECIPIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RECIPIENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_RECIPIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNITS_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.USERNAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.assets.months
import com.kwdevs.hospitalsdashboard.views.assets.years
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyIssuingReportsCreatePage(navHostController: NavHostController){

    val recipientType= listOf(Pair(RecipientType.IN_PATIENT, IN_PATIENT_LABEL),
        Pair(RecipientType.OUT_PATIENT, OUT_PATIENT_LABEL),
        Pair(RecipientType.GOVERNMENTAL_HOSPITAL, GOVERNMENTAL_LABEL),
        Pair(RecipientType.PRIVATE_HOSPITAL, PRIVATE_LABEL),
        Pair(RecipientType.NBTS, IS_NBTS_LABEL))
    val hospital=Preferences.Hospitals().get()
    val bloodBank=Preferences.BloodBanks().get()
    val user = Preferences.User().get()
    val controller:BloodBankIssuingDepartmentController= viewModel()
    val state by controller.monthlyIssuingReportsSingleState.observeAsState()
    val settingsController: SettingsController = viewModel()
    val optionsState by settingsController.bloodOptionsState.observeAsState()
    val selectedRecipientType = remember { mutableStateOf<Pair<RecipientType,String>?>(null) }
    var bloodTypes by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedBloodType = remember { mutableStateOf<BasicModel?>(null) }
    val body = remember { mutableStateOf<MonthlyIssuingReportBody?>(null) }
    var bloodGroups by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedBloodGroup = remember { mutableStateOf<BasicModel?>(null) }
    var bloodBanks by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    var filteredBloodBanks by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    val selectedBloodBank = remember { mutableStateOf<SimpleHospital?>(null) }
    var hospitals by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    var filteredHospitals by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }

    val selectedHospital = remember { mutableStateOf<SimpleHospital?>(null) }

    val quantity = remember { mutableStateOf(EMPTY_STRING) }

    val selectedYear = remember { mutableStateOf(EMPTY_STRING) }
    val selectedMonth= remember { mutableStateOf(EMPTY_STRING) }
    val hospitalName = remember { mutableStateOf(EMPTY_STRING) }
    val showSheet = remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val showDialog = remember { mutableStateOf(false) }
    var item by remember { mutableStateOf<MonthlyIssuingReport?>(null) }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<BloodOptionsData>
            val r = s.data
            val data=r.data
            hospitals=data.hospitals.filter { it.isNbts==false || it.isNbts!=true}.filter { it.id!=hospital?.id }
            bloodGroups=data.bloodGroups
            bloodTypes=data.bloodTypes
            bloodBanks=data.bloodBanks.filter { it.id!=hospital?.id }
            filteredBloodBanks=bloodBanks
            filteredHospitals=hospitals
        }
        else->{ settingsController.bloodOptions() }
    }
    when(state){
        is UiState.Loading->{loading=true;success=false;fail=false}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:EMPTY_STRING
                errors=exception.errors?: emptyMap()
                loading=false;success=false;fail=true;showSheet.value=true
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                success=true;loading=false;fail=false;showSheet.value=true
                selectedBloodGroup.value=null
                quantity.value= EMPTY_STRING
            }

        }
        else->{  }
    }

    SaveDialog(showDialog,controller,body,item)
    Container(
        title = EXPORTS_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(MonthlyIssuingReportsIndexRoute.route)},
        sheetColor = if(success) GREEN else if(fail) Color.Red else BLUE,
        sheetOnClick = {
            success=false;fail=false
            selectedHospital.value=null
            selectedYear.value= EMPTY_STRING
            selectedMonth.value= EMPTY_STRING
            selectedBloodBank.value=null
            selectedBloodType.value=null
            selectedBloodGroup.value=null
            hospitalName.value= EMPTY_STRING
            filteredHospitals=hospitals
            filteredBloodBanks=bloodBanks
            quantity.value= EMPTY_STRING
            selectedRecipientType.value=null
        },
        sheetContent = {
            if(success) Label(DATA_SAVED_LABEL)
            if(fail) {
                Column {
                    Label(errorMessage, color = WHITE)
                    errors.forEach { (t, u) ->
                        Label(t, color = WHITE)
                        u.forEach {
                            Label(it, color = WHITE)
                        }
                    }
                }
            }
        }
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            Column(modifier=Modifier.fillMaxSize()){
                LazyColumn(modifier= Modifier
                    .fillMaxSize()
                    .weight(1f)) {
                   item{
                       Row(modifier= Modifier
                           .fillMaxWidth()
                           .padding(5.dp)){
                           Box(modifier= Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 5.dp)
                               .weight(1f),
                               contentAlignment = Alignment.Center){
                               ComboBox(
                                   title = MONTH_LABEL,
                                   loadedItems = months,
                                   selectedItem = selectedMonth,
                                   selectedContent = {
                                       CustomInput(monthName(selectedMonth.value))
                                   }
                               ) {
                                   Label(monthName(it))
                               }
                           }
                           Box(modifier= Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 5.dp)
                               .weight(1f),
                               contentAlignment = Alignment.Center){
                               ComboBox(
                                   title = YEAR_LABEL,
                                   loadedItems = years,
                                   selectedItem = selectedYear,
                                   selectedContent = {
                                       CustomInput(selectedYear.value)
                                   }
                               ) {
                                   Label(it)
                               }
                           }
                       }
                       VerticalSpacer()
                       if(selectedYear.value!= EMPTY_STRING && selectedMonth.value!= EMPTY_STRING){
                           Row(modifier= Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 5.dp)){
                               Box(modifier= Modifier
                                   .fillMaxWidth()
                                   .padding(horizontal = 5.dp)){
                                   ComboBox(selectedItem = selectedRecipientType,
                                       loadedItems = recipientType,
                                       selectedContent = { CustomInput(selectedRecipientType.value?.second?:SELECT_RECIPIENT_LABEL)}) {
                                       Label(it?.second?: EMPTY_STRING)
                                   }
                               }

                           }
                           if(selectedRecipientType.value!=null){
                               if(selectedRecipientType.value?.first in listOf(RecipientType.NBTS,RecipientType.GOVERNMENTAL_HOSPITAL)){
                                   Row(modifier=Modifier.fillMaxWidth()){
                                       val name = remember { mutableStateOf(EMPTY_STRING) }
                                       Box(modifier= Modifier
                                           .fillMaxWidth()
                                           .padding(horizontal = 5.dp)){
                                           CustomInput(name, FILTER_LABEL,
                                               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                               onTextChange = {t->
                                                   name.value=t
                                                   when(selectedRecipientType.value?.first){
                                                       RecipientType.NBTS->{
                                                           if(t!= EMPTY_STRING) filteredBloodBanks = bloodBanks.filter { bb->(bb.name?: EMPTY_STRING).contains(t) }
                                                           else filteredBloodBanks=bloodBanks
                                                       }
                                                       RecipientType.GOVERNMENTAL_HOSPITAL->{
                                                           if(t!= EMPTY_STRING) filteredHospitals = hospitals.filter { bb->(bb.name?: EMPTY_STRING).contains(t) }
                                                           else filteredHospitals=hospitals
                                                       }
                                                       else->{filteredBloodBanks=bloodBanks;filteredHospitals=hospitals}
                                                   }
                                               }
                                           )
                                       }
                                       IconButton(R.drawable.ic_cancel_red) {
                                           filteredBloodBanks=bloodBanks;filteredHospitals=hospitals
                                       }

                                   }
                               }
                               when(selectedRecipientType.value?.first){
                                   RecipientType.NBTS->{
                                       Box(modifier= Modifier
                                           .fillMaxWidth()
                                           .padding(10.dp)){
                                           ComboBox(
                                               title = RECIPIENT_LABEL,
                                               hasTitle = true,
                                               selectedItem = selectedBloodBank,
                                               loadedItems = filteredBloodBanks,
                                               selectedContent = {
                                                   CustomInput(
                                                   value=selectedBloodBank.value?.name?: EMPTY_STRING,
                                                   label= SELECT_BLOOD_BANK_LABEL,)}
                                           ) {
                                               Label(it?.name?:EMPTY_STRING)
                                           }
                                       }
                                   }
                                   RecipientType.PRIVATE_HOSPITAL->{
                                       Box(modifier= Modifier
                                           .fillMaxWidth()
                                           .padding(5.dp)){
                                           CustomInput(value = hospitalName, label = HOSPITAL_NAME_LABEL)
                                       }
                                   }
                                   RecipientType.GOVERNMENTAL_HOSPITAL->{
                                       Box(modifier= Modifier
                                           .fillMaxWidth()
                                           .padding(10.dp)){
                                           ComboBox(
                                               title = RECIPIENT_LABEL,
                                               hasTitle = true,
                                               selectedItem = selectedHospital,
                                               loadedItems = filteredHospitals,
                                               selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                           ) {
                                               Label(it?.name?:EMPTY_STRING)
                                           }
                                       }
                                   }
                                   else->{}
                               }
                               val inSelectors=selectedRecipientType.value?.first in listOf(RecipientType.NBTS,RecipientType.GOVERNMENTAL_HOSPITAL)
                               val inOthers=selectedRecipientType.value?.first in listOf(RecipientType.IN_PATIENT,RecipientType.OUT_PATIENT)
                               val isPrivate=selectedRecipientType.value?.first ==RecipientType.PRIVATE_HOSPITAL && hospitalName.value.trim()!= EMPTY_STRING
                               val selected=selectedHospital.value!=null || selectedBloodBank.value!=null
                               val inSelectorsAndSelected=inSelectors && selected
                               if(selectedRecipientType.value!=null && (inSelectorsAndSelected || inOthers || isPrivate)){
                                   Row(modifier= Modifier
                                       .fillMaxWidth()
                                       .padding(5.dp)){

                                       Box(modifier= Modifier
                                           .fillMaxWidth()
                                           .padding(5.dp)
                                           .weight(1f),
                                           contentAlignment = Alignment.Center){
                                           ComboBox(
                                               title = UNIT_TYPE_LABEL,
                                               loadedItems = bloodTypes,
                                               selectedItem = selectedBloodType,
                                               selectedContent = {
                                                   CustomInput(selectedBloodType.value?.name?:SELECT_UNIT_TYPE_LABEL)
                                               }
                                           ) {
                                               Label(it?.name?: EMPTY_STRING)
                                           }
                                       }
                                       if(selectedBloodType.value!=null){
                                           Box(modifier= Modifier
                                               .fillMaxWidth()
                                               .padding(5.dp)
                                               .weight(1f),
                                               contentAlignment = Alignment.Center){
                                               ComboBox(
                                                   title = BLOOD_GROUP_LABEL,
                                                   loadedItems = if(selectedBloodType.value?.id in listOf(3,4,5,6)) bloodGroups.filter { it.id in listOf(1,3,5,7) }.map { model ->
                                                       model.copy(name = (model.name?: EMPTY_STRING).replace("pos", EMPTY_STRING))} else bloodGroups,
                                                   selectedItem = selectedBloodGroup,
                                                   selectedContent = {
                                                       CustomInput(selectedBloodGroup.value?.name?:SELECT_BLOOD_GROUP_LABEL)
                                                   }
                                               ) {
                                                   Label(it?.name?: EMPTY_STRING)
                                               }
                                           }

                                       }
                                   }
                                   if(selectedBloodType.value!=null && selectedBloodGroup.value!=null){
                                       CustomInput(
                                           value=quantity,
                                           label= UNITS_NUMBER_LABEL,
                                           keyboardOptions = numericKeyBoard,
                                           onTextChange = {it->
                                               val isInteger=it.toIntOrNull() !=null
                                               if (isInteger) quantity.value=it else quantity.value=EMPTY_STRING }
                                       )
                                   }
                               }

                           }

                       }
                   }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.Center){
                    CustomButton(label= SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                        enabledBackgroundColor = BLUE,
                        enabled = (selectedMonth.value!= EMPTY_STRING &&
                                selectedYear.value!= EMPTY_STRING &&
                                selectedRecipientType.value!=null &&
                                selectedBloodGroup.value!=null &&
                                selectedBloodGroup.value!=null &&
                                quantity.value.trim().toIntOrNull()!=null &&
                                quantity.value.trim()!= EMPTY_STRING)
                    ) {
                        val hasBloodGroup=selectedBloodGroup.value!=null
                        val hasBloodType=selectedBloodType.value!=null
                        val hasMonth=selectedMonth.value!=EMPTY_STRING
                        val hasYear=selectedYear.value!=EMPTY_STRING
                        val isHospital=selectedRecipientType.value?.first in
                                listOf(RecipientType.GOVERNMENTAL_HOSPITAL,RecipientType.PRIVATE_HOSPITAL)
                        if(hospital!=null && bloodBank!=null && hasBloodGroup && hasBloodType &&
                            selectedRecipientType.value!=null &&
                            hasMonth && hasYear && user!=null && quantity.value.trim()!=EMPTY_STRING){

                            item= MonthlyIssuingReport(
                                bloodGroup = selectedBloodGroup.value,
                                bloodUnitType = selectedBloodType.value,
                                month = selectedMonth.value,
                                year = selectedYear.value,
                                isPrivateSector = if(selectedRecipientType.value?.first==RecipientType.PRIVATE_HOSPITAL) true else null,
                                isInPatient = if(selectedRecipientType.value?.first==RecipientType.IN_PATIENT) true else null,
                                isOutPatient = if(selectedRecipientType.value?.first==RecipientType.OUT_PATIENT) true else null,
                                isHospital = if(isHospital) true else null,
                                isNationalBloodBank = if(selectedRecipientType.value?.first==RecipientType.NBTS) true else null,
                                receivingHospital = if(selectedRecipientType.value?.first==RecipientType.GOVERNMENTAL_HOSPITAL) selectedHospital.value else null,
                                receivingBloodBank = if(selectedRecipientType.value?.first==RecipientType.NBTS) selectedBloodBank.value else null,
                                hospitalName = if(selectedRecipientType.value?.first==RecipientType.PRIVATE_HOSPITAL) hospitalName.value else null,
                                quantity = quantity.value.toInt(),

                            )
                            body.value = MonthlyIssuingReportBody(
                                hospitalId = hospital.id,
                                bloodBankId = bloodBank.id,
                                bloodGroupId = selectedBloodGroup.value?.id,
                                bloodUnitTypeId = selectedBloodType.value?.id,
                                month = selectedMonth.value,
                                year = selectedYear.value,
                                isPrivateSector = if(selectedRecipientType.value?.first==RecipientType.PRIVATE_HOSPITAL) 1 else 0,
                                isInPatient = if(selectedRecipientType.value?.first==RecipientType.IN_PATIENT) 1 else 0,
                                isOutPatient = if(selectedRecipientType.value?.first==RecipientType.OUT_PATIENT)1 else 0,
                                isHospital = if(isHospital) 1 else 0,
                                isNationalBloodBank = if(selectedRecipientType.value?.first==RecipientType.NBTS) 1 else 0,
                                receivingHospitalId = if(selectedRecipientType.value?.first==RecipientType.GOVERNMENTAL_HOSPITAL) selectedHospital.value?.id else null,
                                receivingBloodBankId = if(selectedRecipientType.value?.first==RecipientType.NBTS) selectedBloodBank.value?.id else null,
                                hospitalName = if(selectedRecipientType.value?.first==RecipientType.PRIVATE_HOSPITAL) hospitalName.value else null,
                                quantity = quantity.value.toInt(),
                                createdById = user.id
                            )
                            showDialog.value=true
                        }
                        else{
                            val newErrors= mutableMapOf<String,List<String>>()
                            if (hospital==null) newErrors[HOSPITAL_NAME_LABEL] = listOf("Select Hospital")
                            if (bloodBank==null) newErrors[BLOOD_BANK_LABEL] = listOf("Select Blood Bank")
                            if (user==null) newErrors[USERNAME_LABEL] = listOf("Re-Login")
                            if (!hasBloodGroup) newErrors[BLOOD_GROUP_LABEL] = listOf("Select Blood Group")
                            if (!hasBloodType) newErrors[UNIT_TYPE_LABEL] = listOf("Select Unit Type")
                            if (!hasMonth) newErrors[MONTH_LABEL] = listOf("Select Month")
                            if (!hasYear) newErrors[YEAR_LABEL] = listOf("Select Year")

                            errors=newErrors
                            fail=true
                            showSheet.value=true


                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SaveDialog(showDialog: MutableState<Boolean>,
                       controller: BloodBankIssuingDepartmentController,
                       body: MutableState<MonthlyIssuingReportBody?>,
                       item:MonthlyIssuingReport?){

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                VerticalSpacer()
                Label(SAVE_PROMPT)
                VerticalSpacer()
                Column(modifier=Modifier.padding(5.dp)){
                    LazyColumn() {
                        item{
                            item?.let {
                                val recipientType=
                                    if(it.isOutPatient==true) OUT_PATIENT_LABEL
                                else if(it.isInPatient==true) IN_PATIENT_LABEL
                                else if(it.isHospital==true){ if(it.isPrivateSector==true) PRIVATE_LABEL else GOVERNMENTAL_LABEL}
                                else if(it.isNationalBloodBank==true) IS_NBTS_LABEL
                                else EMPTY_STRING
                                val name=if(it.isHospital==true){
                                    if(it.isPrivateSector==true) it.hospitalName?: EMPTY_STRING
                                    else it.receivingHospital?.name?: EMPTY_STRING
                                }
                                else if(it.isNationalBloodBank==true) it.receivingBloodBank?.name?: EMPTY_STRING
                                else EMPTY_STRING
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Label(YEAR_LABEL,item.year?: EMPTY_STRING)
                                    Label(MONTH_LABEL, monthName(item.month))
                                }
                                Label(RECIPIENT_TYPE_LABEL,recipientType)
                                VerticalSpacer()
                                if(name!= EMPTY_STRING) Label(RECIPIENT_LABEL,name)

                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Label(UNIT_TYPE_LABEL,it.bloodUnitType?.name?: EMPTY_STRING)
                                    Label(BLOOD_GROUP_LABEL,it.bloodGroup?.name?: EMPTY_STRING)
                                }
                                LabelSpan(label=UNITS_NUMBER_LABEL,value="${it.quantity?:0}", spanColor = BLUE)
                            }

                        }
                    }
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(5.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label = SAVE_CHANGES_LABEL,
                            buttonShadowElevation = 6, buttonShape = RectangleShape,
                            enabledBackgroundColor = GREEN) {
                            body.value?.let{
                                controller.storeMonthlyIssuingReportNormal(it)
                                showDialog.value=false
                            }
                        }
                        CustomButton(label = CANCEL_LABEL,
                            buttonShadowElevation = 6, buttonShape = RectangleShape,
                            enabledBackgroundColor = Color.Red) {
                            showDialog.value=false
                        }
                    }
                }
            }
        }

    }
}
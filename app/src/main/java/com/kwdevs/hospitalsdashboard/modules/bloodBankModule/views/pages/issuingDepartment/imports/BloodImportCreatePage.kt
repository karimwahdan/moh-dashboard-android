package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.imports

import androidx.activity.compose.BackHandler
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
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.RecipientType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodImportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportIndexRoute
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BY_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DAY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GOVERNMENTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_IMPORT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PRIVATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QUANTITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.UNITS_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.days
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.assets.years

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodImportCreatePage(navHostController: NavHostController){

    val importSources=listOf(
        Pair(RecipientType.NBTS,IS_NBTS_LABEL),
        Pair(RecipientType.GOVERNMENTAL_HOSPITAL, GOVERNMENTAL_LABEL),
        Pair(RecipientType.PRIVATE_HOSPITAL, PRIVATE_LABEL))
    val selectedSource = remember { mutableStateOf<Pair<RecipientType,String>?>(null) }
    val user=Preferences.User().get()
    val savedHospital=Preferences.Hospitals().get()
    val savedBloodBank=Preferences.BloodBanks().get()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.bloodOptionsState.observeAsState()
    val controller:BloodBankIssuingDepartmentController= viewModel()
    val state by controller.bloodImportsSingleState.observeAsState()
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val showSheet = remember { mutableStateOf(false) }
    val quantity = remember { mutableStateOf(EMPTY_STRING) }
    val day = remember { mutableStateOf(EMPTY_STRING) }
    val month = remember { mutableStateOf(EMPTY_STRING) }
    val year = remember { mutableStateOf(EMPTY_STRING) }
    val byPatient = remember { mutableStateOf(false) }
    val isPrivateSector= remember { mutableStateOf(false) }
    val isGov= remember { mutableStateOf(false) }
    val isNbts= remember { mutableStateOf(false) }
    val body = remember { mutableStateOf<BloodImportBody?>(null) }
    var bloodBanks by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    var hospitals by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    val selectedHospital = remember { mutableStateOf<SimpleHospital?>(null) }
    val hospitalName= remember { mutableStateOf(EMPTY_STRING) }

    var bloodTypes by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedUnitType = remember { mutableStateOf<BasicModel?>(null) }

    var bloodGroups by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedBloodGroup = remember { mutableStateOf<BasicModel?>(null) }
    val showDialog= remember { mutableStateOf(false) }
    var item by remember { mutableStateOf<BloodImport?>(null) }
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loading=true;fail=false;success=false
            }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
            }

        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                navHostController.navigate(BloodImportIndexRoute.route)
            }
        }
        else->{
        }
    }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s =optionsState as UiState.Success<BloodOptionsData>
                val response = s.data
                val data=response.data
                hospitals=data.hospitals.filter { it.id!=savedHospital?.id }
                bloodTypes=data.bloodTypes
                bloodGroups=data.bloodGroups
                bloodBanks=data.bloodBanks.filter { it.id!=savedBloodBank?.id }
            }
        }
        else->{
            settingsController.bloodOptions()
        }
    }
    LaunchedEffect(isPrivateSector.value) {
        if(isPrivateSector.value){isGov.value=false;isNbts.value=false}
        selectedHospital.value=null
    }
    LaunchedEffect(isNbts.value) { if(isNbts.value){isGov.value=false;isPrivateSector.value=false} }
    LaunchedEffect(isGov.value) { if(isGov.value){isNbts.value=false;isPrivateSector.value=false} }
    LaunchedEffect(selectedHospital.value) {
        if(!isPrivateSector.value){ hospitalName.value=selectedHospital.value?.name?: EMPTY_STRING }

    }
    LaunchedEffect(selectedUnitType.value) { selectedBloodGroup.value=null }
    SaveDialog(showDialog = showDialog,controller, body,item )
    Container(
        title = NEW_IMPORT_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(BloodImportIndexRoute.route)},
        showSheet = showSheet
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(!success && !fail){
                Column(modifier=Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center) {
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 3.dp)){
                                    ComboBox(
                                        title = DAY_LABEL,
                                        hasTitle = true,
                                        selectedItem = day, loadedItems = days,
                                        selectedContent = { CustomInput(day.value)}
                                    ) {
                                        Label(it)
                                    }
                                }
                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 3.dp)){
                                    ComboBox(
                                        title = MONTH_LABEL,
                                        hasTitle = true,
                                        selectedItem = month, loadedItems = days,
                                        selectedContent = { CustomInput(monthName(month.value))}
                                    ) {
                                        Label(monthName(it))
                                    }
                                }
                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 3.dp)){
                                    ComboBox(
                                        title= YEAR_LABEL,
                                        hasTitle = true,
                                        selectedItem = year, loadedItems = years,
                                        selectedContent = { CustomInput(year.value)}
                                    ) {
                                        Label(it)
                                    }
                                }
                            }
                            if(day.value!= EMPTY_STRING && year.value!= EMPTY_STRING && month.value!= EMPTY_STRING){
                                Row(modifier=Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center){
                                    Label(UNIT_SOURCE_LABEL)
                                }
                                CustomCheckbox(label=PATIENT_LABEL,byPatient)
                                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                                    ComboBox(
                                        title = UNIT_SOURCE_LABEL,
                                        hasTitle = true,
                                        selectedItem = selectedSource,
                                        loadedItems = importSources,
                                        selectedContent = { CustomInput(selectedSource.value?.second?: EMPTY_STRING)}) {
                                        Label(it?.second?: EMPTY_STRING)
                                    }
                                }
                                when(selectedSource.value?.first){
                                    RecipientType.PRIVATE_HOSPITAL->{
                                        Row(modifier=Modifier.fillMaxWidth().padding(start=5.dp,end=5.dp,
                                            top = 30.dp)){
                                            CustomInput(hospitalName, HOSPITAL_NAME_LABEL)
                                        }
                                    }
                                    RecipientType.GOVERNMENTAL_HOSPITAL->{
                                        Box(modifier=Modifier.fillMaxWidth().padding(10.dp)){
                                            ComboBox(
                                                title = HOSPITAL_NAME_LABEL,
                                                hasTitle = true,
                                                loadedItems = hospitals,
                                                selectedItem = selectedHospital,
                                                selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                            ) {
                                                Label(it?.name?: EMPTY_STRING)
                                            }

                                        }

                                    }
                                    RecipientType.NBTS->{
                                        Box(modifier=Modifier.fillMaxWidth().padding(10.dp)){
                                            ComboBox(
                                                title = BLOOD_BANK_LABEL,
                                                hasTitle = true,
                                                loadedItems = bloodBanks,
                                                selectedItem = selectedHospital,
                                                selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                            ) {
                                                Label(it?.name?: EMPTY_STRING)
                                            }

                                        }
                                    }
                                    else->{Label(SELECT_UNIT_SOURCE_LABEL, color = Color.Red)}
                                }
                                if(selectedSource.value!=null && (selectedSource.value?.first in listOf(RecipientType.NBTS,RecipientType.GOVERNMENTAL_HOSPITAL) &&
                                            selectedHospital.value!=null) ||
                                    (selectedSource.value?.first==RecipientType.PRIVATE_HOSPITAL &&
                                            hospitalName.value!= EMPTY_STRING)
                                ){
                                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                        Box(modifier=Modifier.fillMaxWidth().padding(5.dp).weight(1f)){
                                            ComboBox(
                                                loadedItems = bloodTypes,
                                                selectedItem = selectedUnitType,
                                                selectedContent = { CustomInput(selectedUnitType.value?.name?: SELECT_UNIT_TYPE_LABEL)}
                                            ) {
                                                Label(it?.name?: EMPTY_STRING)
                                            }
                                        }
                                        Box(modifier=Modifier.fillMaxWidth().padding(5.dp).weight(1f)){
                                            ComboBox(
                                                loadedItems = if(selectedUnitType.value?.id in listOf(3,4,5,6)) bloodGroups.filter { it.id in listOf(1,3,5,7) }.map { model ->
                                                    model.copy(name = (model.name?: EMPTY_STRING).replace("pos", EMPTY_STRING))} else bloodGroups,
                                                selectedItem = selectedBloodGroup,
                                                selectedContent = { CustomInput(selectedBloodGroup.value?.name?: SELECT_BLOOD_GROUP_LABEL)}
                                            ) {
                                                Label(it?.name?: EMPTY_STRING)
                                            }
                                        }

                                    }
                                    if(selectedUnitType.value!=null && selectedBloodGroup.value!=null){
                                        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                            CustomInput(quantity, QUANTITY_LABEL,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                onTextChange = {t->if(t.toIntOrNull()!=null) quantity.value=t else quantity.value= EMPTY_STRING})
                                        }

                                    }
                                }
                                VerticalSpacer()
                            }
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        CustomButton(label = SAVE_CHANGES_LABEL,
                            enabledBackgroundColor = GREEN,
                            buttonShape = RectangleShape,
                            enabled = (quantity.value.trim()!= EMPTY_STRING &&
                                    selectedSource.value!=null && day.value.trim()!= EMPTY_STRING
                                    && month.value.trim()!= EMPTY_STRING
                                    && year.value.trim()!= EMPTY_STRING &&
                                    (
                                            (selectedSource.value?.first in listOf(RecipientType.NBTS,RecipientType.GOVERNMENTAL_HOSPITAL) &&
                                            selectedHospital.value!=null) ||
                                                    (selectedSource.value?.first==RecipientType.PRIVATE_HOSPITAL &&
                                                            hospitalName.value!= EMPTY_STRING)
                                            )
                                    ),
                            buttonShadowElevation = 6) {
                            if(quantity.value.trim()!= EMPTY_STRING &&
                                selectedSource.value!=null && day.value.trim()!= EMPTY_STRING
                                && month.value.trim()!= EMPTY_STRING
                                && year.value.trim()!= EMPTY_STRING){
                                item=BloodImport(

                                    quantity = quantity.value.toInt(),
                                    day = day.value,
                                    month = month.value,
                                    year = year.value,
                                    isNbts = selectedSource.value?.first==RecipientType.NBTS,
                                    isGov = selectedSource.value?.first==RecipientType.GOVERNMENTAL_HOSPITAL,
                                    isPrivateSector = selectedSource.value?.first==RecipientType.PRIVATE_HOSPITAL,
                                    byPatient = byPatient.value,
                                    hospitalName =if(selectedSource.value?.first==RecipientType.PRIVATE_HOSPITAL) hospitalName.value else null,
                                    senderHospital = selectedHospital.value,
                                    bloodGroup = selectedBloodGroup.value,
                                    bloodUnitType = selectedUnitType.value,

                                    )
                                body.value=BloodImportBody(
                                    hospitalId = savedHospital?.id,
                                    bloodBankId = savedBloodBank?.id,
                                    isNbts = selectedSource.value?.first==RecipientType.NBTS,
                                    isGov = selectedSource.value?.first==RecipientType.GOVERNMENTAL_HOSPITAL,
                                    isPrivateSector = selectedSource.value?.first==RecipientType.PRIVATE_HOSPITAL,
                                    byPatient = byPatient.value,
                                    bloodGroupId = selectedBloodGroup.value?.id,
                                    unitTypeId = selectedUnitType.value?.id,
                                    quantity = quantity.value.toInt(),
                                    day = day.value,
                                    month = month.value,
                                    year = year.value,
                                    hospitalName = hospitalName.value,
                                    senderHospitalId = selectedHospital.value?.id,
                                    createdById = user?.id,
                                )

                                showDialog.value=true
                            }
                        }

                    }
                }
            }
            else if(fail) FailScreen(modifier=Modifier.fillMaxSize(),errors = errors,message = errorMessage)
        }
    }
    BackHandler { if(fail){fail=false;controller.reload()} }
}

@Composable
private fun SaveDialog(showDialog:MutableState<Boolean>,
                       controller: BloodBankIssuingDepartmentController,
                       body: MutableState<BloodImportBody?>,
                       item:BloodImport?){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                VerticalSpacer()
                Label(SAVE_PROMPT)
                VerticalSpacer()
                item?.let{
                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)) {
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            Label(label = DATE_LABEL,text="${it.year} - ${monthName(it.month)} - ${it.day}")
                        }
                        VerticalSpacer()
                        Label(label = UNIT_SOURCE_LABEL,
                            text=if(it.byPatient==true){
                                if(it.isNbts==true) BY_PATIENT_LABEL+ SPACE+ FROM_LABEL+SPACE+ IS_NBTS_LABEL
                                else if(it.isGov==true) BY_PATIENT_LABEL+ SPACE+ FROM_LABEL+SPACE+ GOVERNMENTAL_LABEL
                                else if(it.isPrivateSector==true) BY_PATIENT_LABEL+ SPACE+ FROM_LABEL+SPACE + PRIVATE_LABEL
                                else EMPTY_STRING
                            }
                            else{
                                if(it.isNbts==true) IS_NBTS_LABEL
                                else if(it.isGov==true) GOVERNMENTAL_LABEL
                                else if(it.isPrivateSector==true) PRIVATE_LABEL
                                else EMPTY_STRING
                            }
                        )
                        VerticalSpacer()
                        if(it.isPrivateSector==true)Label(label= HOSPITAL_NAME_LABEL,it.hospitalName?: EMPTY_STRING)
                        else if(it.isNbts==true ) Label(BLOOD_BANK_LABEL,it.senderHospital?.name?: EMPTY_STRING)
                        else if(it.isGov==true) Label(HOSPITAL_NAME_LABEL,it.senderHospital?.name?: EMPTY_STRING)
                        VerticalSpacer()
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            Label(label = UNIT_TYPE_LABEL,text=it.bloodUnitType?.name?: EMPTY_STRING)
                            Label(label = BLOOD_GROUP_LABEL,text=it.bloodGroup?.name?: EMPTY_STRING)

                        }
                        Label(UNITS_NUMBER_LABEL,"${it.quantity?:0}")
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    CustomButton(label=SAVE_CHANGES_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = GREEN) {

                        body.value?.let {
                            controller.storeBloodImports(it)
                            showDialog.value=false

                        }
                    }
                    CustomButton(label=CANCEL_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = Color.Red) {
                        body.value=null
                        showDialog.value=false
                    }
                }
                VerticalSpacer()
            }
        }
    }
}
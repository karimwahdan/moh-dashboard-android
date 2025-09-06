package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.pages.expiries

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodNearExpiredItemBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodNearExpiredController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredIndexRoute
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BoxContainer
import com.kwdevs.hospitalsdashboard.views.assets.CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.EXPIRY_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NEAR_EXPIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_EXPIRY_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNITS_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearExpiryCreatePage(navHostController: NavHostController){
    val settingsController      :  SettingsController = viewModel()
    val controller              :  BloodNearExpiredController = viewModel()
    val savedItem               =  Preferences.BloodBanks.NearExpiredBloodUnits().get()
    val user                    =  Preferences.User().get()
    val thisHospital            =  Preferences.Hospitals().get()
    val thisBloodBank           =  Preferences.BloodBanks().get()
    val selectedUnitType        =  remember { mutableStateOf<BasicModel?>(null) }
    val selectedBloodGroup      =  remember { mutableStateOf<BasicModel?>(null) }
    val selectedStatus          =  remember { mutableStateOf<BasicModel?>(null) }
    val quantity                =  remember { mutableStateOf(EMPTY_STRING) }
    val code                    =  remember { mutableStateOf(EMPTY_STRING) }
    val expiryDateState         =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val expiryDate              =  remember { mutableStateOf(EMPTY_STRING) }
    val showExpiryDatePicker    =  remember { mutableStateOf(false) }
    val showSheet               =  remember { mutableStateOf(false) }
    val state                   by controller.singleState.observeAsState()
    val bloodOptionsState       by settingsController.bloodOptionsState.observeAsState()
    var unitTypes               by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var bloodGroups             by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var statuses                by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var hospitals               by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
    var loadingBloodOptions     by remember { mutableStateOf(false) }
    var failBloodOptions        by remember { mutableStateOf(false) }
    var successBloodOptions     by remember { mutableStateOf(false) }
    var loading                 by remember { mutableStateOf(false) }
    var fail                    by remember { mutableStateOf(false) }
    var success                 by remember { mutableStateOf(false) }

    when(bloodOptionsState){
        is UiState.Loading->{
            LaunchedEffect(Unit) {loadingBloodOptions=true;failBloodOptions=false;successBloodOptions=false }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {loadingBloodOptions=false;failBloodOptions=false;successBloodOptions=false }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {

                loadingBloodOptions=false
                failBloodOptions=false;successBloodOptions=true
                val s=bloodOptionsState as UiState.Success<BloodOptionsData>
                val d=s.data
                val r=d.data
                bloodGroups=r.bloodGroups
                unitTypes=r.bloodTypes
                statuses=r.statuses
                hospitals=r.hospitals.filter { it.id!=thisHospital?.id }
                if(savedItem!=null){
                    selectedBloodGroup.value=savedItem.bloodGroup
                    selectedUnitType.value=savedItem.unitType
                    quantity.value=(savedItem.quantity?:0).toString()
                    selectedStatus.value=savedItem.status
                    code.value=savedItem.code?: EMPTY_STRING

                }
            }
        }
        else->{loadingBloodOptions=true;settingsController.bloodOptions()}
    }
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {loading=true;fail=false;success=false }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {loading=false;fail=true;success=false }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false
                fail=false;success=true
            }
        }
        else->{}
    }
    LaunchedEffect(Unit) {
        if(savedItem!=null){
            selectedStatus.value=savedItem.status
            selectedUnitType.value=savedItem.unitType
            expiryDate.value=savedItem.expiryDate?: EMPTY_STRING
            code.value=savedItem.code?: EMPTY_STRING
            selectedBloodGroup.value=savedItem.bloodGroup
            quantity.value=(savedItem.quantity?:0).toString()
        }
    }
    LaunchedEffect(selectedUnitType.value) {
        selectedBloodGroup.value=null
        if(selectedUnitType.value?.id !in listOf(3,4,5,6)) quantity.value=(1).toString()
    }
    DatePickerWidget(showExpiryDatePicker,expiryDateState,expiryDate)
    Container(title = NEAR_EXPIRED_LABEL,showSheet = showSheet,
        headerShowBackButton = true,headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(NearExpiredIndexRoute.route)}) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(!success && !fail){
                if(loadingBloodOptions) LoadingScreen()
                else{
                    if(successBloodOptions){
                        Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                            Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)) {
                                ComboBox(title = UNIT_TYPE_LABEL, hasTitle = true,
                                    loadedItems = unitTypes, selectedItem = selectedUnitType,
                                    selectedContent = { CustomInput(selectedUnitType.value?.name?: SELECT_UNIT_TYPE_LABEL)}) {
                                    Label(it?.name?: EMPTY_STRING)
                                }
                            }
                            Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)) {
                                ComboBox(title = BLOOD_GROUP_LABEL, hasTitle = true,
                                    loadedItems = if(selectedUnitType.value?.id in listOf(3,4,5,6))bloodGroups
                                        .filter { it.id !in listOf(2,4,6,8) }.map { model ->
                                        model.copy(name = (model.name?: EMPTY_STRING).replace("pos", EMPTY_STRING))} else bloodGroups, selectedItem = selectedBloodGroup,
                                    selectedContent = { CustomInput(selectedBloodGroup.value?.name?: SELECT_BLOOD_GROUP_LABEL)}) {
                                    Label(it?.name?: EMPTY_STRING)
                                }
                            }
                        }
                        if(selectedUnitType.value!=null && selectedBloodGroup.value!=null){
                            CustomInput(
                                value=quantity,
                                enabled = selectedUnitType.value!=null && selectedBloodGroup.value!=null &&
                                        selectedUnitType.value?.id in listOf(3,4,5,6),
                                label=UNITS_NUMBER_LABEL, keyboardOptions = numericKeyBoard,
                                onTextChange = {t->
                                    if(selectedUnitType.value?.id in listOf(3,4,5,6)){
                                        if(t.toIntOrNull()!=null) quantity.value=t else quantity.value=EMPTY_STRING
                                    }else{
                                        quantity.value=(1).toString()
                                    }
                                }
                            )
                            if(quantity.value.toIntOrNull() ==1 &&
                                selectedUnitType.value?.id !in listOf(3,4,5,6))
                            {CustomInput(code, CODE_LABEL)}
                            Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                BoxContainer(hasBorder = false) {
                                    ComboBox(title = STATUS_LABEL, hasTitle = true,
                                        loadedItems = statuses, selectedItem = selectedStatus,
                                        selectedContent = { CustomInput(selectedStatus.value?.name?: SELECT_STATUS_LABEL)}) {
                                        Label(it?.name?: EMPTY_STRING)
                                    }
                                }
                            }
                            VerticalSpacer()
                            BoxContainer(hasBorder = false) {
                                Label(text=if(expiryDate.value!= EMPTY_STRING)expiryDate.value else SELECT_EXPIRY_DATE_LABEL,
                                    label = EXPIRY_DATE_LABEL,
                                    color = if(expiryDate.value!= EMPTY_STRING) BLACK else Color.Red,)
                            }
                            BoxContainer(hasBorder = false) { CustomButton(label = SELECT_DATE_LABEL, enabledBackgroundColor = ORANGE) {showExpiryDatePicker.value=true} }
                            VerticalSpacer()
                            HorizontalDivider()
                            VerticalSpacer()
                        }
                        VerticalSpacer()
                        BoxContainer(hasBorder = false) {
                            CustomButton(label = SAVE_CHANGES_LABEL,
                                buttonShadowElevation = 6,
                                buttonShape = RectangleShape,
                                enabled = (selectedUnitType.value!=null && selectedBloodGroup.value!=null &&
                                        ((selectedUnitType.value?.id in listOf(3,4,5,6) && selectedBloodGroup.value?.id !in listOf(2,4,6,8)) ||
                                                selectedUnitType.value?.id !in listOf(3,4,5,6)) &&
                                        quantity.value!= EMPTY_STRING && quantity.value.toIntOrNull()!=null &&
                                        expiryDate.value!= EMPTY_STRING) && user!=null && thisHospital!=null

                            ) {
                                val bloodGroupAndUnitSelected=selectedUnitType.value!=null && selectedBloodGroup.value!=null
                                val validPlasmaGroup= selectedUnitType.value?.id in listOf(3,4,5,6) && selectedBloodGroup.value?.id !in listOf(2,4,6,8)
                                val validBloodGroup= selectedUnitType.value?.id !in listOf(3,4,5,6)
                                val validSelectionOfBloodGroup=validBloodGroup || validPlasmaGroup
                                val validQuantity = quantity.value!= EMPTY_STRING && quantity.value.toIntOrNull()!=null
                                val validExpiryDate=expiryDate.value!= EMPTY_STRING
                                val existing=savedItem!=null
                                if(bloodGroupAndUnitSelected && validSelectionOfBloodGroup && validQuantity && validExpiryDate){

                                    val body = BloodNearExpiredItemBody(
                                        id = if(existing) savedItem?.id else null,
                                        hospitalId = thisHospital?.id,
                                        bloodBankId = thisBloodBank?.id,
                                        unitTypeId = selectedUnitType.value?.id,
                                        bloodGroupId = selectedBloodGroup.value?.id,
                                        quantity = quantity.value.toIntOrNull(),
                                        code = if(selectedUnitType.value?.id !in listOf(3,4,5,6)) code.value else null,
                                        expiryDate =expiryDate.value,
                                        statusId = selectedStatus.value?.id,
                                        createdById = if(!existing) null else user?.id,
                                        updatedById = if(existing) user?.id else null,
                                    )
                                    if(existing) controller.update(body) else controller.store(body)
                                }
                            }
                        }
                        VerticalSpacer()
                    }
                }

            }
            else if(success) SuccessScreen(modifier=Modifier.fillMaxSize()) {
                Column(modifier=Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Image(modifier=Modifier.size(144.dp),contentScale = ContentScale.FillBounds,painter = painterResource(R.drawable.logo), contentDescription = null)
                    Label(DATA_SAVED_LABEL, fontWeight = FontWeight.Bold)
                    CustomButton(label = ADD_NEW_LABEL) {
                        success=false;fail=false;loading=false
                        selectedUnitType.value=null
                        selectedBloodGroup.value=null
                        quantity.value= EMPTY_STRING
                        expiryDate.value= EMPTY_STRING
                        code.value= EMPTY_STRING
                        Preferences.BloodBanks.NearExpiredBloodUnits().delete()
                        controller.reload()
                    }
                }
            }
            else FailScreen(modifier=Modifier.fillMaxSize())
        }

    }
}

@Composable
private fun SaveDialog(showDialog: MutableState<Boolean>, item:BloodNearExpiredItem, body:BloodNearExpiredItemBody){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}){}
    }
}
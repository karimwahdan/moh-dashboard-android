package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.incineration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIncinerationBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.IncinerationController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.IncinerationCard
import com.kwdevs.hospitalsdashboard.views.assets.ADD_MULTIPLE_ITEMS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BLUE3
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_COMPONENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_REASON_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_INCINERATION_ITEM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SEARCH_BY_CAMPAIGN_CODE
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_REASON_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.UNITS_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.assets.months
import com.kwdevs.hospitalsdashboard.views.assets.years

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncinerationCreatePage(navHostController: NavHostController){
    val controller:IncinerationController= viewModel()
    val state               by controller.paginationState.observeAsState()
    var items               by remember { mutableStateOf<List<MonthlyIncineration>>(emptyList()) }
    val optionsState        by controller.optionsState.observeAsState()
    var reasons             by remember { mutableStateOf<List<IncinerationReason>>(emptyList()) }
    var campaigns           by remember { mutableStateOf<List<DailyBloodCollection>>(emptyList()) }
    var bloodGroups         by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var unitTypes           by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var loading             by remember { mutableStateOf(false) }
    var success             by remember { mutableStateOf(false) }
    var fail                by remember { mutableStateOf(false) }
    var errorMessage        by remember { mutableStateOf(EMPTY_STRING) }
    val hospital            =  Preferences.Hospitals().get()
    val user                =  Preferences.User().get()
    val department          =  Preferences.BloodBanks.Departments().get()
    val departmentName      =  if(department==BloodBankDepartment.ISSUING_DEPARTMENT) DEPARTMENT_ISSUING_LABEL else DEPARTMENT_COMPONENT_LABEL
    val bodies              =  remember { mutableStateOf<List<MonthlyIncinerationBody>>(emptyList()) }
    val campaignCode        =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedCampaign    =  remember { mutableStateOf<DailyBloodCollection?>(null) }
    val selectedReason      =  remember { mutableStateOf<IncinerationReason?>(null) }
    val selectedBloodGroup  =  remember { mutableStateOf<BasicModel?>(null) }
    val selectedUnitType    =  remember { mutableStateOf<BasicModel?>(null) }
    val value               =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedYear        = remember { mutableStateOf(EMPTY_STRING) }
    val selectedMonth       = remember { mutableStateOf(EMPTY_STRING) }
    val showSheet           = remember { mutableStateOf(false) }
    val showDialog          = remember { mutableStateOf(false) }

    when(optionsState){
        is UiState.Loading->{ }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
            }
        }
        is UiState.Success->{
            val s = optionsState as UiState.Success<IncinerationOptionsData>
            val r = s.data
            val data=r.data
            reasons=data.reasons
            bloodGroups=data.bloodGroups
            unitTypes=data.types
            campaigns=data.campaigns
        }
        else->{ controller.options() }
    }
    when(state){
        is UiState.Loading->{LaunchedEffect(Unit){loading=true;fail=false;success=false}}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;success=false;fail=true
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:EMPTY_STRING
                showSheet.value=true
            }
        }
        is UiState.Success->{

            LaunchedEffect(Unit) {
                success=true;fail=false;showSheet.value=true
            }
        }
        else->{}
    }
    LaunchedEffect(selectedUnitType.value) { selectedBloodGroup.value=null }
    SaveDialog(showDialog,controller,bodies,items)
    Container(
        title = "$NEW_INCINERATION_ITEM_LABEL ($departmentName)",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(IncinerationIndexRoute.route)},
        sheetColor = if(success) GREEN else Color.Red,
        sheetOnClick = {
            success=false;fail=false;showSheet.value=false
            selectedReason.value=null;selectedUnitType.value=null
            selectedBloodGroup.value=null;value.value=EMPTY_STRING},
        sheetContent = {
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                if(success) Label(DATA_SAVED_LABEL, color = WHITE)
                if(fail) Label(errorMessage, color = WHITE)
            }
        }
    ) {
        Column(modifier=Modifier.fillMaxSize()){
            LazyColumn(modifier=Modifier.fillMaxSize().weight(2.5f)) {
                item{
                    Row(modifier=Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){

                    }
                    Row(modifier=Modifier.fillMaxWidth().background(BLUE3),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        CustomButton(label = ADD_NEW_LABEL, enabledBackgroundColor = BLUE,
                            enabled = (selectedUnitType.value!=null &&
                                    value.value.trim()!=EMPTY_STRING &&
                                    selectedMonth.value!=EMPTY_STRING &&
                                    selectedYear.value!=EMPTY_STRING &&
                                    selectedReason.value!=null),
                            buttonShape = RectangleShape,
                            buttonShadowElevation = 6) {
                            if(selectedUnitType.value!=null &&
                                value.value.trim()!=EMPTY_STRING &&
                                selectedMonth.value!=EMPTY_STRING &&
                                selectedYear.value!=EMPTY_STRING &&
                                selectedUnitType.value!=null &&
                                selectedReason.value!=null){
                                val item=MonthlyIncineration(
                                    bloodGroupId    = selectedBloodGroup.value?.id,
                                    bloodGroup      = selectedBloodGroup.value,
                                    bloodUnitTypeId = selectedUnitType.value?.id,
                                    bloodUnitType   = selectedUnitType.value,
                                    value           = if(value.value.trim()!=EMPTY_STRING) value.value.toInt() else 0,
                                    month           = selectedMonth.value,
                                    campaign        = selectedCampaign.value,
                                    year            = selectedYear.value,
                                    reason          = selectedReason.value,
                                    reasonId        = selectedReason.value?.id
                                )
                                val currentItems= mutableListOf(item)
                                if(items.find { it==item }==null){
                                    currentItems.addAll(items.filter { it!=item })
                                    items=currentItems
                                }
                                val body=MonthlyIncinerationBody(
                                    hospitalId = hospital?.id,
                                    createdById = user?.id,
                                    month = item.month,
                                    year = item.year,
                                    collectionId = selectedCampaign.value?.id,
                                    byBCD = if(department==BloodBankDepartment.COMPONENT_DEPARTMENT) 1 else 0,
                                    byIssuing = if(department==BloodBankDepartment.ISSUING_DEPARTMENT) 1 else 0,
                                    bloodGroupId = item.bloodGroupId,
                                    bloodUnitTypeId = item.bloodUnitTypeId,
                                    incinerationReasonId = item.reasonId,
                                    value = if(value.value.trim()!=EMPTY_STRING) value.value.toInt() else 0,
                                )
                                val currentBodies= mutableListOf(body)
                                if(bodies.value.find { it==body }==null){
                                    currentBodies.addAll(bodies.value.filter { it!=body })
                                }
                                bodies.value=currentBodies
                                selectedUnitType.value=null
                                selectedReason.value=null
                                selectedBloodGroup.value=null
                                value.value= EMPTY_STRING
                            }
                        }

                        Label(ADD_MULTIPLE_ITEMS_LABEL, paddingStart = 5, paddingEnd = 5, color = WHITE, fontWeight = FontWeight.Bold, maximumLines = 5)
                    }
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth()){
                        Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).weight(1f),
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
                        Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).weight(1f),
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
                    if(selectedMonth.value!= EMPTY_STRING && selectedYear.value!= EMPTY_STRING){
                        if(department==BloodBankDepartment.COMPONENT_DEPARTMENT){
                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                    contentAlignment = Alignment.Center){
                                    var filteredCampaigns by remember { mutableStateOf<List<DailyBloodCollection>>(emptyList()) }
                                    ComboBox(
                                        title = CAMPAIGN_LABEL+(selectedCampaign.value?.code?:""),
                                        loadedItems = filteredCampaigns.ifEmpty { campaigns },
                                        selectedItem = selectedCampaign,
                                        selectedContent = {
                                            //campaignCode.value=selectedCampaign.value?.code?:EMPTY_STRING
                                            //val collectionDate= (selectedCampaign.value?.collectionDate?:EMPTY_STRING)
                                            TextField(
                                                modifier= Modifier.fillMaxWidth(),
                                                value=campaignCode.value,
                                                placeholder = {Label(SEARCH_BY_CAMPAIGN_CODE)},
                                                onValueChange = {text->
                                                    campaignCode.value=text
                                                    filteredCampaigns=campaigns.filter {c-> (c.code?: EMPTY_STRING).contains(text) }
                                                },
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.White,
                                                    unfocusedContainerColor = Color.White,
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ))
                                        }
                                    ) {
                                        val code=it?.code?:EMPTY_STRING
                                        val collectionDate= (it?.collectionDate?:EMPTY_STRING)
                                        val dateOnly=collectionDate.replaceAfterLast(SPACE,EMPTY_STRING)
                                        Label(text="$code - $dateOnly",textAlign = TextAlign.End, maximumLines = 2)                    }
                                }
                                Column(modifier=Modifier.padding(horizontal = 5.dp)) {
                                    VerticalSpacer(40)
                                    IconButton(R.drawable.ic_cancel_red) {
                                        selectedCampaign.value=null
                                    }
                                }
                            }
                        }
                        if((department==BloodBankDepartment.COMPONENT_DEPARTMENT && selectedCampaign.value!=null) || department==BloodBankDepartment.ISSUING_DEPARTMENT){
                            Row(modifier=Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically){
                                Row(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                    Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                        contentAlignment = Alignment.Center){
                                        ComboBox(
                                            title = UNIT_TYPE_LABEL,
                                            loadedItems = unitTypes,
                                            selectedItem = selectedUnitType,
                                            selectedContent = {
                                                CustomInput(selectedUnitType.value?.name?: SELECT_UNIT_TYPE_LABEL)
                                            }
                                        ) {
                                            Label(it?.name?:EMPTY_STRING)
                                        }
                                    }
                                }

                                Row(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                    Column {
                                        VerticalSpacer(35)
                                        IconButton(R.drawable.ic_cancel_red) {
                                            selectedBloodGroup.value=null
                                        }
                                    }
                                    Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                        contentAlignment = Alignment.Center){
                                        ComboBox(
                                            title = BLOOD_GROUP_LABEL,
                                            loadedItems = if(selectedUnitType.value?.id in listOf(3,4,5,6)) bloodGroups.filter { b->b.id in listOf(1,3,5,7) }
                                                .map { model ->
                                                    model.copy(name = (model.name?: EMPTY_STRING).replace("pos", EMPTY_STRING))} else bloodGroups,
                                            selectedItem = selectedBloodGroup,
                                            selectedContent = {
                                                CustomInput(selectedBloodGroup.value?.name?: SELECT_BLOOD_GROUP_LABEL)
                                            }
                                        ) {
                                            Label(it?.name?:EMPTY_STRING)
                                        }
                                    }
                                }

                            }
                            if(selectedUnitType.value!=null){
                                Row(modifier=Modifier.fillMaxWidth()){
                                    Box(modifier=Modifier.fillMaxWidth().padding(5.dp).weight(1f),
                                        contentAlignment = Alignment.Center){
                                        ComboBox(
                                            title = INCINERATION_REASON_LABEL,
                                            loadedItems = reasons,
                                            selectedItem = selectedReason,
                                            selectedContent = {
                                                CustomInput(selectedReason.value?.name?:SELECT_REASON_LABEL)
                                            }
                                        ) {
                                            Label(it?.name?:EMPTY_STRING)
                                        }
                                    }
                                }
                                if(selectedReason.value!=null){
                                    CustomInput(value=value, label = UNITS_NUMBER_LABEL,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onTextChange = {if(it.toIntOrNull()!=null) value.value=it else value.value="0"})
                                }

                            }

                        }
                    }

                }
            }
            VerticalSpacer()
            HorizontalDivider()
            LazyColumn(modifier=Modifier.fillMaxSize().weight(0.7f)) {
                items(items){item->
                    Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center){ IncinerationCard(item) }
                        IconButton(R.drawable.ic_delete_red) { items=items.filter { it!=item } }
                    }
                }
            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label = SAVE_CHANGES_LABEL,
                    buttonShadowElevation = 6,
                    buttonShape = RectangleShape,
                    enabled = ((department==BloodBankDepartment.COMPONENT_DEPARTMENT && bodies.value.all { it.collectionId!=null }) || department==BloodBankDepartment.ISSUING_DEPARTMENT) &&  bodies.value.isNotEmpty(),
                    enabledBackgroundColor = GREEN,
                    ) {
                    val isBCD=department==BloodBankDepartment.COMPONENT_DEPARTMENT
                    val isIssuing=department==BloodBankDepartment.ISSUING_DEPARTMENT

                    val hasCollectionId=bodies.value.all { it.collectionId!=null }
                    val validBcdBodies= isBCD && hasCollectionId
                    if((validBcdBodies || isIssuing) &&  bodies.value.isNotEmpty()){
                        showDialog.value=true
                    }
                }

            }
        }
    }
}

@Composable
private fun SaveDialog(
    showDialog: MutableState<Boolean>,
    controller: IncinerationController,
    bodies: MutableState<List<MonthlyIncinerationBody>>,
    items:List<MonthlyIncineration>,
) {
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}){
            ColumnContainer {
                Column{
                    Label(SAVE_PROMPT)
                    VerticalSpacer()
                    LazyColumn(modifier=Modifier.weight(1f)) { items(items){ IncinerationCard(it)} }
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label=SAVE_CHANGES_LABEL, buttonShape = RectangleShape) {
                            controller.storeMonthlyIncineration(bodies.value)
                            showDialog.value=false
                        }
                        CustomButton(label= CANCEL_LABEL, buttonShape = RectangleShape, enabledBackgroundColor = Color.Red) {
                            showDialog.value=false
                        }
                    }
                }
            }
        }
    }
}

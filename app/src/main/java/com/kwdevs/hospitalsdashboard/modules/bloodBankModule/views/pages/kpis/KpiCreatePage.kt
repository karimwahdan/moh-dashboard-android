package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.kpis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankKPIController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BBKpiOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BBKpiOptionsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpiItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_COMPONENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DONATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_SEROLOGY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_THERAPEUTIC_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NOTE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.assets.months
import com.kwdevs.hospitalsdashboard.views.assets.years
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpiCreatePage(navHostController: NavHostController){
    val controller:BloodBankKPIController= viewModel()
    val department=Preferences.BloodBanks.Departments().get()
    val departmentName=when(department) {
        BloodBankDepartment.ISSUING_DEPARTMENT->{
            DEPARTMENT_ISSUING_LABEL}
        BloodBankDepartment.COMPONENT_DEPARTMENT->{
            DEPARTMENT_COMPONENT_LABEL}
        BloodBankDepartment.DONATION_DEPARTMENT->{
            DEPARTMENT_DONATION_LABEL}
        BloodBankDepartment.SEROLOGY_DEPARTMENT->{
            DEPARTMENT_SEROLOGY_LABEL}
        BloodBankDepartment.THERAPEUTIC_UNIT->{
            DEPARTMENT_THERAPEUTIC_LABEL}
        else->{
            SPACE}
    }
    val user            =  Preferences.User().get()
    val hospital        =  Preferences.Hospitals().get()
    val bloodBank       =  Preferences.BloodBanks().get()
    val showSheet       =  remember { mutableStateOf(false) }
    val selectedYear    =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedMonth   =  remember { mutableStateOf(EMPTY_STRING) }
    val showDialog      =  remember { mutableStateOf(false) }
    val optionsState    by controller.optionsState.observeAsState()
    val state           by controller.kpisState.observeAsState()
    var items           by remember { mutableStateOf<List<BloodBankKpiItem>>(emptyList()) }
    var data            by remember { mutableStateOf<BBKpiOptionsData?>(null) }
    var bodies          by remember { mutableStateOf<List<KpiBody>>(emptyList()) }
    var bodyItems       by remember { mutableStateOf<List<BloodBankKpi>>(emptyList()) }
    var loading         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    when(optionsState){
        is UiState.Loading->{ LaunchedEffect(Unit) {loading=true;fail=false;success=false } }
        is UiState.Error->{ LaunchedEffect(Unit) {loading=false;fail=true;success=false } }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = optionsState as UiState.Success<BBKpiOptionsResponse>
                val response=s.data
                data=response.data
                items=data?.items?: emptyList()

            }
        }
        else->{controller.options(year = selectedYear.value, month = selectedMonth.value,
            departmentId = when(department) {
                BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                BloodBankDepartment.DONATION_DEPARTMENT->{3}
                BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                else->{0}
            }
            )}
    }
    when(state){
        is UiState.Loading->{ LaunchedEffect(Unit) {loading=true;fail=false;success=false } }
        is UiState.Error->{ LaunchedEffect(Unit) {loading=false;fail=true;success=false } }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                when(department) {
                    BloodBankDepartment.ISSUING_DEPARTMENT->{navHostController.navigate(IssuingDepartmentHomeRoute.route)}
                    BloodBankDepartment.COMPONENT_DEPARTMENT->{navHostController.navigate(ComponentDepartmentHomeRoute.route)}
                    BloodBankDepartment.DONATION_DEPARTMENT->{navHostController.navigate(DonationDepartmentHomeRoute.route)}
                    BloodBankDepartment.SEROLOGY_DEPARTMENT->{}
                    BloodBankDepartment.THERAPEUTIC_UNIT->{}
                    else->{navHostController.navigate(BloodBankHomeRoute.route)}
                }
            }
        }
        else->{}
    }

    SaveDialog(showDialog=showDialog,bodies=bodies,items=bodyItems,controller=controller)
    Container(
        title = "$KPI_LABEL $departmentName",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = { navHostController.navigate(BloodBankKpiIndexRoute.route) }
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success && items.isNotEmpty()){
                Column(modifier = Modifier.fillMaxSize()){
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(5.dp)){
                        Box(modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 30.dp)){
                            ComboBox(title = YEAR_LABEL, hasTitle = true,
                                loadedItems = years, selectedItem = selectedYear, selectedContent = { CustomInput(selectedYear.value)}) { Label(it) }
                        }
                        Box(modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 5.dp, end = 25.dp)){
                            ComboBox(title = MONTH_LABEL, hasTitle = true,
                                loadedItems = months, selectedItem = selectedMonth, selectedContent = { CustomInput(monthName(selectedMonth.value))}) { Label(monthName(it)) }
                        }
                    }
                    VerticalSpacer()
                    if(bodyItems.isNotEmpty()){
                        Label("${bodyItems.size} من ${items.size}")
                        LazyColumn(modifier= Modifier
                            .fillMaxSize()
                            .weight(0.5f)) {
                            item {
                                bodyItems.sortedBy { it.itemId?:0 }.chunked(2).forEach { list->
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        list.forEach { o->
                                            var showNoteDialog by remember { mutableStateOf(false) }
                                            Row(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(horizontal = 5.dp)
                                                .border(
                                                    width = 1.dp,
                                                    shape = rcs(5),
                                                    color = Color.LightGray
                                                ),verticalAlignment = Alignment.CenterVertically){
                                                Column(modifier= Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f),horizontalAlignment = Alignment.CenterHorizontally){
                                                    Label(text=o.item?.name?: EMPTY_STRING,
                                                        maximumLines = 3,
                                                        softWrap = true,
                                                        textOverflow = TextOverflow.Ellipsis)
                                                    Label(text="${o.value?:0f}")
                                                }
                                                Column {
                                                    IconButton(R.drawable.ic_delete_red) {
                                                        bodyItems=bodyItems.filter { it!=o }
                                                    }
                                                    if(o.note!=null){
                                                        IconButton(R.drawable.ic_eye_blue) {
                                                            showNoteDialog=true
                                                        }

                                                    }

                                                }
                                                if(showNoteDialog){
                                                    Dialog(onDismissRequest = {showNoteDialog=false}) {
                                                        ColumnContainer {
                                                            Label(text=o.note?: EMPTY_STRING, maximumLines = Int.MAX_VALUE)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                    VerticalSpacer()
                    if(selectedMonth.value!= EMPTY_STRING && selectedYear.value!= EMPTY_STRING){
                        LazyColumn(modifier= Modifier
                            .fillMaxSize()
                            .weight(1f)) {
                            items(items){item->
                                Row(modifier=Modifier.fillMaxWidth()){
                                    val note = remember { mutableStateOf(EMPTY_STRING) }
                                    val value = remember { mutableStateOf(EMPTY_STRING) }
                                        /*
                                        rememberSaveable(item.id) {
                                        mutableStateOf(
                                            when (item.id) {
                                                1 -> {if(data?.bloodIssuing!=null)data?.bloodIssuing.toString() else EMPTY_STRING}
                                                2 -> {if(data?.plasmaIssuing!=null)data?.bloodIssuing.toString() else EMPTY_STRING}
                                                3 -> {if(data?.rdpIssuing!=null)data?.rdpIssuing.toString() else EMPTY_STRING}
                                                4 -> {if(data?.pooledIssuing!=null)data?.pooledIssuing.toString() else EMPTY_STRING}
                                                9 -> {if(data?.returned!=null) data?.returned.toString() else EMPTY_STRING}
                                                10 -> {if(data?.expiredPlatelets!=null)data?.expiredPlatelets.toString() else EMPTY_STRING}
                                                11 -> {if(data?.expiredPlasma!=null)data?.expiredPlasma.toString() else EMPTY_STRING}
                                                12 -> {if(data?.expiredRbc!=null)data?.expiredRbc.toString() else EMPTY_STRING}
                                                13 -> {if(data?.brokenPlasma!=null)data?.brokenPlasma.toString() else EMPTY_STRING}
                                                14 -> {if(data?.datPos!=null)data?.datPos.toString() else EMPTY_STRING}
                                                else -> EMPTY_STRING
                                            }
                                        )
                                    }
                                         */
                                    LaunchedEffect(value.value) {
                                        val isFloat=value.value.toFloatOrNull()!=null
                                        val isInt=value.value.toIntOrNull()!=null
                                        val notEmpty=value.value!= EMPTY_STRING
                                        val isFloatOrInt=notEmpty && (isFloat || isInt)
                                        val hasSelectedMonth=selectedMonth.value.trim()!= EMPTY_STRING
                                        val hasSelectedYear=selectedYear.value.trim()!= EMPTY_STRING
                                        val hasDate=hasSelectedMonth && hasSelectedYear

                                        if(hasDate && isFloatOrInt){
                                            val body=KpiBody(
                                                hospitalId = hospital?.id,
                                                bloodBankId = bloodBank?.id,
                                                departmentId = when(department) {
                                                    BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                                                    BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                                                    BloodBankDepartment.DONATION_DEPARTMENT->{3}
                                                    BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                                                    BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                                                    else->{0}
                                                },
                                                month = selectedMonth.value,
                                                year = selectedYear.value,
                                                createdById = user?.id,
                                                note = note.value,
                                                itemId = item.id,
                                                value = value.value.toFloatOrNull()
                                            )
                                            val newBodies= mutableListOf<KpiBody>()
                                            newBodies.add(body)
                                            newBodies.addAll(bodies.filter { it.itemId!=body.itemId })
                                            bodies=newBodies

                                            val kpiItem=BloodBankKpi(
                                                hospitalId = hospital?.id,
                                                bloodBankId = bloodBank?.id,
                                                departmentId = when(department) {
                                                    BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                                                    BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                                                    BloodBankDepartment.DONATION_DEPARTMENT->{3}
                                                    BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                                                    BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                                                    else->{0}
                                                },
                                                itemId = item.id,
                                                item = item,
                                                note = note.value,
                                                value = value.value.toFloatOrNull()
                                            )
                                            val newItems= mutableListOf<BloodBankKpi>()
                                            newItems.add(kpiItem)
                                            newItems.addAll(bodyItems.filter { it.itemId!=kpiItem.itemId })
                                            bodyItems=newItems
                                        }
                                    }
                                    var noteDialog by remember { mutableStateOf(false) }
                                    if(noteDialog) Dialog(onDismissRequest = {noteDialog=false}) {ColumnContainer {CustomInput(note,NOTE_LABEL)}}
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier= Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .background(
                                                color = if (item.id in bodyItems.map { it.itemId }) colorResource(
                                                    R.color.light_green
                                                ) else colorResource(R.color.pale_pink),
                                                shape = rcs(5)
                                            )){
                                            CustomInput(value=value,
                                                label=item.name?: EMPTY_STRING,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                onTextChange = {text->
                                                    val isFloat=text.toFloatOrNull()!=null
                                                    val isInt=text.toIntOrNull()!=null
                                                    val isFloatOrInt=isFloat || isInt
                                                    val hasSelectedMonth=selectedMonth.value.trim()!= EMPTY_STRING
                                                    val hasSelectedYear=selectedYear.value.trim()!= EMPTY_STRING
                                                    val hasDate=hasSelectedMonth && hasSelectedYear

                                                    if(hasDate && isFloatOrInt){
                                                    value.value=text

                                                        val body=KpiBody(
                                                            hospitalId = hospital?.id,
                                                            bloodBankId = bloodBank?.id,
                                                            departmentId = when(department) {
                                                                BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                                                                BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                                                                BloodBankDepartment.DONATION_DEPARTMENT->{3}
                                                                BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                                                                BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                                                                else->{0}
                                                            },
                                                            month = selectedMonth.value,
                                                            year = selectedYear.value,
                                                            createdById = user?.id,
                                                            note = note.value,
                                                            itemId = item.id,
                                                            value = value.value.toFloatOrNull()
                                                        )
                                                        val newBodies= mutableListOf<KpiBody>()
                                                        newBodies.add(body)
                                                        newBodies.addAll(bodies.filter { it.itemId!=body.itemId })
                                                        bodies=newBodies

                                                        val kpiItem=BloodBankKpi(
                                                            hospitalId = hospital?.id,
                                                            bloodBankId = bloodBank?.id,
                                                            departmentId = when(department) {
                                                                BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                                                                BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                                                                BloodBankDepartment.DONATION_DEPARTMENT->{3}
                                                                BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                                                                BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                                                                else->{0}
                                                            },
                                                            itemId = item.id,
                                                            item = item,
                                                            note = note.value,
                                                            value = value.value.toFloatOrNull()
                                                        )
                                                        val newItems= mutableListOf<BloodBankKpi>()
                                                        newItems.add(kpiItem)
                                                        newItems.addAll(bodyItems.filter { it.itemId!=kpiItem.itemId })
                                                        bodyItems=newItems
                                                }
                                                else value.value= EMPTY_STRING

                                            })
                                        }
                                        IconButton(R.drawable.ic_note_blue) {noteDialog=true }
                                    }
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical=5.dp,horizontal=10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label = SAVE_CHANGES_LABEL,
                            buttonShadowElevation = 6,
                            enabledBackgroundColor = GREEN,
                            buttonShape = RectangleShape,
                            enabled = (items.size==bodies.size && bodies.isNotEmpty() )) {
                            if(items.size==bodies.size) showDialog.value=true
                        }
                        CustomButton(label = CANCEL_LABEL, buttonShadowElevation = 6, enabledBackgroundColor = Color.Red, buttonShape = RectangleShape) {
                            navHostController.navigate(BloodBankKpiIndexRoute.route)
                        }
                    }

                }
            }
        }
    }

}

@Composable
private fun SaveDialog(
    showDialog: MutableState<Boolean>,
    bodies: List<KpiBody>,
    items: List<BloodBankKpi>,
    controller: BloodBankKPIController
) {
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Column {
                    Label(SAVE_PROMPT)
                    Label("${bodies.size}")
                    LazyColumn(modifier= Modifier
                        .fillMaxSize()
                        .weight(0.5f)) {
                        item {
                            items.sortedBy { it.itemId?:0 }.chunked(2).forEach { list->
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                    verticalAlignment = Alignment.CenterVertically){
                                    list.forEach { o->
                                        Row(modifier= Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(horizontal = 5.dp)
                                            .border(
                                                width = 1.dp,
                                                shape = rcs(5),
                                                color = Color.LightGray
                                            ),verticalAlignment = Alignment.CenterVertically){
                                            Column(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f),horizontalAlignment = Alignment.CenterHorizontally){
                                                Label(text=o.item?.name?: EMPTY_STRING,
                                                    maximumLines = 3,
                                                    softWrap = true,
                                                    textOverflow = TextOverflow.Ellipsis)
                                                Label(text="${o.value?:0f}")
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label = SAVE_CHANGES_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape) {
                            controller.storeMultipleKpis(bodies)
                            showDialog.value=false
                        }
                        CustomButton(label = CANCEL_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = Color.Red) {
                            showDialog.value=false
                        }
                    }
                }
            }
        }
    }
}

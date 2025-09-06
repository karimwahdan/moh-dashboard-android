package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.kpis.views.pages

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankKPIController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.BloodBankKpisResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_COMPONENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DONATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_SEROLOGY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_THERAPEUTIC_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpiIndexPage(navHostController: NavHostController){

    val controller:BloodBankKPIController= viewModel()
    val hospital        =  Preferences.Hospitals().get()
    val bloodBank       =  Preferences.BloodBanks().get()
    val department      =  Preferences.BloodBanks.Departments().get()
    val showSheet       =  remember { mutableStateOf(false) }
    val currentPage     =  remember { mutableIntStateOf(1) }
    val state           by controller.kpisState.observeAsState()
    var items           by remember { mutableStateOf<List<BloodBankKpi>>(emptyList()) }
    var lastPage        by remember { mutableIntStateOf(1) }
    var loading         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    val departmentName=when(department) {
        BloodBankDepartment.ISSUING_DEPARTMENT->{
            DEPARTMENT_ISSUING_LABEL
        }
        BloodBankDepartment.COMPONENT_DEPARTMENT->{
            DEPARTMENT_COMPONENT_LABEL
        }
        BloodBankDepartment.DONATION_DEPARTMENT->{
            DEPARTMENT_DONATION_LABEL
        }
        BloodBankDepartment.SEROLOGY_DEPARTMENT->{
            DEPARTMENT_SEROLOGY_LABEL
        }
        BloodBankDepartment.THERAPEUTIC_UNIT->{
            DEPARTMENT_THERAPEUTIC_LABEL
        }
        else->{
            SPACE
        }
    }
    val cairoZone = ZoneId.of("Africa/Cairo")
    val today = LocalDate.now(cairoZone)

    // Current year as String (e.g., "2025")
    val yearString = today.year.toString()

    // Current month number as String (1–12)
    val monthString = today.monthValue.toString()
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {loading=true;fail=false;success=false }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {loading=false;fail=true;success=false }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<BloodBankKpisResponse>
                val r=s.data
                val data=r.data
                items=data?: emptyList()

            }

        }
        else->{
            val body=KpiBody(
                hospitalId = hospital?.id,
                bloodBankId = bloodBank?.id,
                year = yearString,
                month = monthString,
                departmentId =when(department) {
                    BloodBankDepartment.ISSUING_DEPARTMENT->{1}
                    BloodBankDepartment.COMPONENT_DEPARTMENT->{2}
                    BloodBankDepartment.DONATION_DEPARTMENT->{3}
                    BloodBankDepartment.SEROLOGY_DEPARTMENT->{4}
                    BloodBankDepartment.THERAPEUTIC_UNIT->{5}
                    else->{0}
                }
            )
            controller.indexKpis(body)
        }
    }
    Container(
        title = "$KPI_LABEL $departmentName",
        showSheet = showSheet,
        headerOnClick = {
            when(department){
                BloodBankDepartment.ISSUING_DEPARTMENT->{navHostController.navigate(IssuingDepartmentHomeRoute.route)}
                BloodBankDepartment.COMPONENT_DEPARTMENT->{navHostController.navigate(ComponentDepartmentHomeRoute.route)}
                BloodBankDepartment.DONATION_DEPARTMENT->{navHostController.navigate(DonationDepartmentHomeRoute.route)}
                BloodBankDepartment.SEROLOGY_DEPARTMENT->{navHostController.navigate(BloodBankHomeRoute.route)}
                BloodBankDepartment.THERAPEUTIC_UNIT->{navHostController.navigate(BloodBankHomeRoute.route)}
                else->{navHostController.navigate(BloodBankHomeRoute.route)}
            }
                        },
        headerIconButtonBackground = BLUE,
        headerShowBackButton = true
    ) {
        CustomButton(label = ADD_NEW_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = BLUE) {
            navHostController.navigate(BloodBankKpiCreateRoute.route)
        }
        if(loading) LoadingScreen(modifier = Modifier.fillMaxSize())
        else{
            if(success){
                PaginationContainer(
                    currentPage=currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    val grouped2= items.groupBy { it.year }
                    LazyColumn {
                        item{
                            Column(modifier=Modifier.fillMaxSize()){
                                grouped2.forEach { (year, list) ->
                                    Label(year?: EMPTY_STRING)
                                    KpiTable(list)
                                }

                            }

                        }
                    }
                    /*
                    LazyColumn {
                        item{
                            grouped.forEach { (year, list) ->

                                Label("${year.first} - ${monthName(year.second)}")
                                list.forEach {
                                    KpiCard(it)

                                }
                            }

                        }
                    }
                     */

                }

            }
        }
    }
}

@Composable
fun KpiTable(items:List<BloodBankKpi>){
    val months = items.mapNotNull { it.month }.distinct().sorted()
    val grouped = items.groupBy { it.item?.name ?: "Unknown" }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)){
        Box(){

            Column {
                Row(modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp, bottom = 4.dp)) {
                    Text(
                        text = "Item",
                        modifier = Modifier.width(140.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    months.forEach { month ->
                        Text(
                            text = monthName(month),
                            modifier = Modifier.width(80.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                grouped.toList().forEach { pair->
                    val itemName=pair.first
                    val kpiList=pair.second
                    Row(modifier=Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = itemName,
                            modifier = Modifier.width(140.dp),
                            fontWeight = FontWeight.Medium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = true
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(vertical = 2.dp)
                        ) {
                            // First column: Item name (soft wrap + maxLines=3)


                            // Values by month (centered horizontally)
                            months.forEach { month ->
                                val value = kpiList.find { it.month == month }?.value ?: 0f
                                Text(
                                    text = value.toString(),
                                    modifier = Modifier.width(80.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                }

            }
        }
    }
    if(1>2){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)
        ) {
            // ---- Sticky Header (Months row) ----
            stickyHeader {
                Row(modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp)) {
                    Text(
                        text = "Item",
                        modifier = Modifier.width(140.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    months.forEach { month ->
                        Text(
                            text = monthName(month),
                            modifier = Modifier.width(80.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ---- Data Rows ----
            items(grouped.toList()) { (itemName, kpiList) ->
                Row(modifier=Modifier.fillMaxWidth()){
                    Text(
                        text = itemName,
                        modifier = Modifier.width(140.dp),
                        fontWeight = FontWeight.Medium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 2.dp)
                    ) {
                        // First column: Item name (soft wrap + maxLines=3)


                        // Values by month (centered horizontally)
                        months.forEach { month ->
                            val value = kpiList.find { it.month == month }?.value ?: 0f
                            Text(
                                text = value.toString(),
                                modifier = Modifier.width(80.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.reports.monthlyIssuingReports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.reports.monthlyReport.MonthlyIssuingReportListCard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.EXPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTHLY_REPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.assets.monthName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyIssuingReportsIndexPage(navHostController: NavHostController){


    val controller  :  BloodBankIssuingDepartmentController= viewModel()
    val currentPage =  remember { mutableIntStateOf(1) }
    val showSheet   =  remember { mutableStateOf(false) }
    val state       by controller.monthlyIssuingReportsPaginationState.observeAsState()
    var items       by remember {mutableStateOf<List<MonthlyIssuingReport>>(emptyList())}
    var lastPage    by remember { mutableIntStateOf(1) }
    var loading     by remember { mutableStateOf(false) }
    var fail        by remember { mutableStateOf(false) }
    var success     by remember { mutableStateOf(false) }



    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                success=false;fail=false;loading=true

            }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                success=false;fail=true;loading=false

            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                success=true;fail=false;loading=false
                val s = state as UiState.Success<ApiResponse<PaginationData<MonthlyIssuingReport>>>
                val r = s.data
                val pagination=r.pagination
                lastPage=pagination.lastPage
                val data=pagination.data
                items=data
            }

        }
        else->{
            controller.indexMonthlyIssuingReportsByHospital(currentPage.intValue)
        }
    }
    LaunchedEffect(currentPage.intValue) {
        controller.indexMonthlyIssuingReportsByHospital(currentPage.intValue)
    }

    Container(
        title = EXPORTS_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(IssuingDepartmentHomeRoute.route)}
    ) {
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            CustomButton(label=ADD_NEW_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = BLUE) {
                navHostController.navigate(MonthlyIssuingReportsCreateRoute.route)
            }
        }
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {

                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        item{
                            val byYear=items
                                //.sortedByDescending { it.year }.sortedByDescending { (it.month?:"0").toInt() }
                                .groupBy { it.year }
                            byYear.forEach { (year, list) ->
                                val byMonth=list.groupBy { it.month }
                                byMonth.forEach { (month, subList) ->
                                    val monthString= monthName(month)
                                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                        Label("$monthString - $year", fontSize = 21)
                                    }
                                    ColumnContainer(background = colorResource(R.color.pale_orange)) {
                                        VerticalSpacer(10)
                                        MonthlyIssuingReportListCard(subList)
                                        VerticalSpacer(10)
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
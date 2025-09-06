package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.pages.stocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.cards.DailyBloodStockCardListCard
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.cards.DailyPlasmaStockCardListCard
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.cards.DailyPlateletsStockCardListCard
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.InnerModule
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasInnerModulePermission
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.BloodStockCreateRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AT_12_PM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AT_6_AM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AT_6_PM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CRAYO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CSP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonLeftIcon
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FFP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MIDNIGHT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NO_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RANDOM_DONOR_PLATELETS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SINGLE_DONOR_PLATELETS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.UNDER_INSPECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodStockIndexPage(navHostController: NavHostController){
    val user = Preferences.User().get()
    val controller:BloodBankController= viewModel()
    val state by controller.dailyBloodStocksPaginationState.observeAsState()
    var items by remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    var total by remember { mutableIntStateOf(0) }
    var totalUnderInspection by remember { mutableIntStateOf(0) }
    val showSheet = remember { mutableStateOf(false) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    when(state){
        is UiState.Loading->{loading=true;success=false;fail=false}
        is UiState.Error->{
            loading=false;success=false;fail=true
            val s = state as UiState.Error
            val exception=s.exception
            errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
            errors=exception.errors?: emptyMap()
        }
        is UiState.Success->{
            loading=false;fail=false
            val s = state as UiState.Success<ApiResponse<PaginationData<DailyBloodStock>>>
            val r = s.data
            val pagination = r.pagination
            val data=pagination.data
            success=true
            items=data
            total=items.sumOf { it.amount?:0 }
            totalUnderInspection=items.sumOf { if(it.underInspection==true) it.amount?:0 else 0 }
            lastPage=pagination.lastPage
        }
        else->{ controller.indexBloodStocksByHospitalToday(currentPage.intValue) }
    }
    LaunchedEffect(currentPage.intValue) {
        controller.indexBloodStocksByHospitalToday(currentPage.intValue)
    }
    Container(
        title = STOCK_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            navHostController.navigate(IssuingDepartmentHomeRoute.route)
        }
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                VerticalSpacer()
                if(user?.hasInnerModulePermission(CRUD.CREATE,InnerModule.BLOOD_ISSUING_INCINERATION) == true){
                    CustomButtonLeftIcon(
                        label=ADD_NEW_LABEL,
                        enabledBackgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        borderColor = GREEN,
                        hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                        buttonShadowElevation = 6,
                        buttonShape = rcs(5),
                        horizontalPadding = 10,
                        enabledFontColor = GREEN,
                    )
                    { navHostController.navigate(BloodStockCreateRoute.route) }
                }
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    if(items.isNotEmpty()){
                        Column(modifier=Modifier.fillMaxSize()) {
                            LazyColumn(modifier= Modifier.fillMaxSize().weight(1f)) {
                                item{

                                    val bloodItems=items.filter { it.bloodUnitTypeId in listOf(1,2) }
                                    val freshFrozenPlasmaItems=items.filter { it.bloodUnitTypeId==3 }
                                    val frozenPlasmaItems=items.filter { it.bloodUnitTypeId==4 }
                                    val cspPlasmaItems=items.filter { it.bloodUnitTypeId==5 }
                                    val crayoPlasmaItems=items.filter { it.bloodUnitTypeId==6 }
                                    val rdpItems=items.filter{it.bloodUnitTypeId==7}
                                    val sdpItems=items.filter{it.bloodUnitTypeId==9}
                                    val underInspectionItems=items.filter { it.bloodUnitTypeId==null && it.underInspection==true }
                                    val bloodGroupedBy6Hrs= groupBySixHourBlocks(bloodItems)
                                    val freshFrozenPlasmaGroupedBy6Hrs= groupBySixHourBlocks(freshFrozenPlasmaItems)

                                    val rdpGroupedBy6Hrs= groupBySixHourBlocks(rdpItems)
                                    val sdpGroupedBy6Hrs= groupBySixHourBlocks(sdpItems)

                                    val frozenPlasmaGroupedBy6Hrs= groupBySixHourBlocks(frozenPlasmaItems)
                                    val cspPlasmaGroupedBy6Hrs= groupBySixHourBlocks(cspPlasmaItems)
                                    val crayoPlasmaGroupedBy6Hrs= groupBySixHourBlocks(crayoPlasmaItems)

                                    Label(items[0].entryDate?:EMPTY_STRING, fontWeight = FontWeight.Bold)
                                    if(underInspectionItems.isNotEmpty()){
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(UNDER_INSPECTION_LABEL, paddingStart = 5, paddingEnd = 5)
                                            Span(underInspectionItems.sumOf{ it.amount?:0 }.toString(), backgroundColor = BLUE, color = WHITE)
                                        }

                                    }
                                    if(bloodGroupedBy6Hrs.isNotEmpty() ){
                                        Label(BLOOD_UNITS_LABEL)
                                        DailyBloodStockCardListCard(bloodGroupedBy6Hrs)
                                        VerticalSpacer()
                                    }
                                    if(rdpItems.isNotEmpty() ){
                                        Label(RANDOM_DONOR_PLATELETS_LABEL)
                                        DailyPlateletsStockCardListCard(rdpGroupedBy6Hrs)
                                        VerticalSpacer()
                                    }
                                    if(sdpItems.isNotEmpty() ){
                                        Label(SINGLE_DONOR_PLATELETS_LABEL)
                                        DailyPlateletsStockCardListCard(sdpGroupedBy6Hrs)
                                        VerticalSpacer()
                                    }

                                    if(freshFrozenPlasmaGroupedBy6Hrs.isNotEmpty()){
                                        Label(FFP_LABEL)
                                        DailyPlasmaStockCardListCard(freshFrozenPlasmaGroupedBy6Hrs,3)
                                        VerticalSpacer()
                                    }
                                    if(frozenPlasmaGroupedBy6Hrs.isNotEmpty()){
                                        Label(FP_LABEL)
                                        DailyPlasmaStockCardListCard(frozenPlasmaGroupedBy6Hrs,4)
                                        VerticalSpacer()
                                    }
                                    if(cspPlasmaGroupedBy6Hrs.isNotEmpty()){
                                        Label(CSP_LABEL)
                                        DailyPlasmaStockCardListCard(cspPlasmaGroupedBy6Hrs,5)
                                        VerticalSpacer()
                                    }
                                    if(crayoPlasmaGroupedBy6Hrs.isNotEmpty()){
                                        Label(CRAYO_LABEL)
                                        DailyPlasmaStockCardListCard(crayoPlasmaGroupedBy6Hrs,6)
                                        VerticalSpacer()
                                    }

                                }
                            }
                        }
                    }
                    else{
                        Label(NO_DATA_LABEL, fontSize = 24, fontWeight = FontWeight.Bold, color = BLUE)
                    }
                }
            }


        }
    }
}

fun groupBySixHourBlocks(entries: List<DailyBloodStock>): Map<String, List<DailyBloodStock>> {

    return entries.groupBy { entry ->
        val blockStart=entry.timeBlock?:EMPTY_STRING
        val timeRangeLabel = when (blockStart) {
            "00"  -> MIDNIGHT_LABEL //- 6 AM add this if you want to get range
            "06"  -> AT_6_AM_LABEL //- 12 PM add this if you want to get range
            "12" -> AT_12_PM_LABEL //- 6 PM add this if you want to get range
            "18" -> AT_6_PM_LABEL //- 12 AM add this if you want to get range
            else -> "N/A"
        }
        // Use date + time label as key
        timeRangeLabel
    }
}
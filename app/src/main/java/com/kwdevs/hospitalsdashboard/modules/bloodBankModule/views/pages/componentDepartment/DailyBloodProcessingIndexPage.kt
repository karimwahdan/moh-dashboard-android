package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankComponentDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DailyBloodProcessingCreateRoute
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_PROCESSING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PROCESSING_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBloodProcessingIndexPage(navHostController: NavHostController){
    val controller:BloodBankComponentDepartmentController= viewModel()
    val state       by controller.dailyBloodProcessingPaginationState.observeAsState()
    var items       by remember { mutableStateOf<List<DailyBloodProcessing>>(emptyList()) }
    var lastPage    by remember { mutableIntStateOf(1) }
    var loading     by remember { mutableStateOf(false) }
    var success     by remember { mutableStateOf(false) }
    var fail        by remember { mutableStateOf(false) }
    val currentPage =  remember { mutableIntStateOf(1) }
    val showSheet   =  remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loading=true;fail=false;success=false
            }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<ApiResponse<PaginationData<DailyBloodProcessing>>>
                val r = s.data
                val paginationData=r.pagination
                lastPage=paginationData.lastPage
                items = paginationData.data
            }

        }
        else->{
            LaunchedEffect(currentPage.intValue) {
                controller.indexDailyProcessingByHospital(currentPage.intValue)
            }
        }
    }
    LaunchedEffect(currentPage.intValue) {
        controller.indexDailyProcessingByHospital(currentPage.intValue)
    }
    Container(
        title = DAILY_PROCESSING_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(ComponentDepartmentHomeRoute.route)}

    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                VerticalSpacer()
                CustomButton(label= ADD_NEW_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape, enabledBackgroundColor = BLUE) {
                    Preferences.CrudTypes().set(CrudType.CREATE)
                    Preferences.BloodBanks.DailyProcesses().delete()
                    navHostController.navigate(DailyBloodProcessingCreateRoute.route)
                }
                HorizontalSpacer()
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        item{
                            val byCampaign=items.sortedByDescending { it.campaign?.collectionDate }.groupBy { it.campaign }
                            Row(modifier=Modifier.fillMaxWidth()){
                                Column(modifier=Modifier.fillMaxWidth().weight(0.3f).border(1.dp, Color.LightGray),horizontalAlignment = Alignment.CenterHorizontally){
                                    Label(CAMPAIGN_DATE_LABEL+"\n", fontSize = 12,
                                            maximumLines = 2, softWrap = true, textOverflow = TextOverflow.Ellipsis)
                                    HorizontalDivider()
                                    byCampaign.forEach { (campaign, processes) ->
                                        processes.forEach { _->
                                            val collectionDate= if(campaign?.collectionDate!=null){
                                                ((campaign.collectionDate).replaceRange(startIndex=0,endIndex=5,EMPTY_STRING))
                                            }else EMPTY_STRING
                                            val dateOnly=collectionDate.replaceAfterLast(SPACE,EMPTY_STRING)
                                            Label(SPACE, fontSize = 12)
                                            Label(dateOnly, fontSize = 12)
                                            VerticalSpacer()
                                            HorizontalDivider()
                                        }

                                    }
                                }
                                Column(modifier=Modifier.fillMaxWidth().weight(0.3f).border(1.dp, Color.LightGray),horizontalAlignment = Alignment.CenterHorizontally){
                                    Label(PROCESSING_DATE_LABEL+"\n", fontSize = 12,
                                        maximumLines = 2, softWrap = true, textOverflow = TextOverflow.Ellipsis)
                                    HorizontalDivider()
                                    byCampaign.forEach { (_, processes) ->
                                        processes.forEach { process->
                                            val date=if(process.processingDate!=null)process.processingDate.replaceRange(startIndex = 0, endIndex = 5,"")else{""}
                                            Label(SPACE, fontSize = 12)
                                            Label(date, fontSize = 12)
                                            VerticalSpacer()
                                            HorizontalDivider()
                                        }

                                    }
                                }
                                Column(modifier=Modifier.fillMaxWidth().weight(0.5f).border(1.dp, Color.LightGray),horizontalAlignment = Alignment.CenterHorizontally){
                                    Label(TOTAL_LABEL, fontSize = 12)
                                    Label(EMPTY_STRING, fontSize = 12)
                                    HorizontalDivider()
                                    byCampaign.forEach { (campaign, processes) ->
                                        processes.forEach { process->
                                            val bgColor=if((process.total?:0)<(campaign?.total?:0)) Color.Red else GREEN
                                            Label(process.unitType?.name?:"", fontSize = 12)
                                            Row{
                                                Label((campaign?.total?:0).toString(), fontSize = 12)
                                                HorizontalSpacer()
                                                Label("->", fontSize = 12)
                                                HorizontalSpacer()
                                                Span((process.total?:0).toString(), fontSize = 12,backgroundColor = bgColor, color = WHITE)

                                            }
                                            VerticalSpacer()
                                            HorizontalDivider()
                                        }
                                    }
                                }
                                Column(modifier=Modifier.fillMaxWidth().weight(1f).border(1.dp, Color.LightGray),horizontalAlignment = Alignment.CenterHorizontally){
                                    Label(CODE_LABEL, fontSize = 12)
                                    Label(EMPTY_STRING, fontSize = 12)
                                    HorizontalDivider()
                                    byCampaign.forEach { (campaign, processes) ->
                                        processes.forEach {
                                            Row(verticalAlignment = Alignment.CenterVertically){
                                                Column(modifier=Modifier.fillMaxWidth().weight(1f),
                                                    ){
                                                    Label(SPACE, fontSize = 12)
                                                    Label(campaign?.code?: EMPTY_STRING, fontSize = 12)
                                                }
                                                if(it.editable!=false){
                                                    IconButton(R.drawable.ic_edit_blue) {
                                                        Preferences.CrudTypes().set(CrudType.UPDATE)
                                                        Preferences.BloodBanks.DailyProcesses().set(it)
                                                        navHostController.navigate(DailyBloodProcessingCreateRoute.route)
                                                    }
                                                }
                                            }
                                            VerticalSpacer()
                                            HorizontalDivider()

                                        }

                                    }
                                }

                            }
                            /*
                                                    byCampaign.forEach { (campaign, processes) ->
                                val code=campaign?.code?:""
                                val totalCollected=campaign?.total?:0
                                val campaignType = campaign?.campaignType?.name
                                val collectionDate= (campaign?.collectionDate?:"")
                                val dateOnly=collectionDate.replaceAfterLast(" ","")
                                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(
                                    ORANGE)){
                                    LabelSpan(label="Campaign: $code - $campaignType - $dateOnly",value="$totalCollected",
                                        layoutDirection =  RIGHT_LAYOUT_DIRECTION,
                                        labelColor = WHITE)

                                }
                                processes.forEach {process-> DailyBloodProcessingCard(process) }
                                HorizontalDivider()
                                VerticalSpacer()
                            }

                             */
                        }
                    }
                }
            }
        }
    }
}
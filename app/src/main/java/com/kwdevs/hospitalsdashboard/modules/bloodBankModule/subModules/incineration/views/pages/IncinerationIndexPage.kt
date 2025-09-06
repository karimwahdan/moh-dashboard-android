package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.incineration.views.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.IncinerationController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationCreateRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canCreateBloodComponentIncineration
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canCreateBloodIssuingIncineration
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.RIGHT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonLeftIcon
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_BLOOD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_INFO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_REASON_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.MAX_PERCENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PERCENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_INCINERATED_ITEMS
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VIEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncinerationIndexPage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val controller:IncinerationController= viewModel()
    val state               by controller.paginationState.observeAsState()
    var lastPage            by remember { mutableIntStateOf(1) }
    var items               by remember { mutableStateOf<List<MonthlyIncineration>>(emptyList()) }
    var loading             by remember { mutableStateOf(false) }
    var success             by remember { mutableStateOf(false) }
    var fail                by remember { mutableStateOf(false) }
    val bloodBankDepartment =  Preferences.BloodBanks.Departments().get()
    val isIssuing           =  bloodBankDepartment==BloodBankDepartment.ISSUING_DEPARTMENT
    val currentPage         =  remember { mutableIntStateOf(1) }
    val selectedItem        =  remember { mutableStateOf<MonthlyIncineration?>(null) }
    val showSheet           =  remember { mutableStateOf(false) }
    val showDialog          =  remember { mutableStateOf(false) }
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
                val s = state as UiState.Success<ApiResponse<PaginationData<MonthlyIncineration>>>
                val r = s.data
                val pagination =r.pagination
                lastPage=pagination.lastPage
                val data=pagination.data
                items=data.filter { if(isIssuing) it.byIssuing==true else it.byBCD==true }

            }

        }
        else->{
            LaunchedEffect(Unit) {
                controller.indexByHospital(currentPage.intValue)
            }

        }
    }

    LaunchedEffect(currentPage.intValue) {
        controller.indexByHospital(currentPage.intValue)

    }

    CompositionLocalProvider(RIGHT_LAYOUT_DIRECTION){
        IncinerationDetailsDialog(showDialog,selectedItem)
        Container(
            title = "إعدام ${ if(isIssuing){"الصرف"}else{"المشتقات"} }",
            showSheet = showSheet,
            headerShowBackButton = true,
            headerIconButtonBackground = BLUE,
            headerOnClick = {
                navHostController.navigate(if(isIssuing)IssuingDepartmentHomeRoute.route else ComponentDepartmentHomeRoute.route)
            }
        ) {
            if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
            else{
                if(success){
                    VerticalSpacer()
                    if(user.canCreateBloodIssuingIncineration() || user.canCreateBloodComponentIncineration()){
                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center){
                            CustomButtonLeftIcon(label = ADD_NEW_LABEL,
                                enabledBackgroundColor = Color.Transparent,
                                disabledBackgroundColor = Color.Transparent,
                                enabledFontColor = GREEN,
                                borderColor = GREEN,
                                hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                                buttonShadowElevation = 6,
                                buttonShape = rcs(5),
                                horizontalPadding = 10,
                                ) {
                                navHostController.navigate(IncinerationCreateRoute.route)
                            }
                        }

                    }
                    LabelSpan(label = TOTAL_INCINERATED_ITEMS, value = "${items.sumOf { (it.value?:0) }}", spanColor = Color.Red)
                    PaginationContainer(
                        currentPage = currentPage,
                        lastPage = lastPage,
                        totalItems = items.size
                    ) {
                        LazyColumn(modifier=Modifier.fillMaxSize()) {
                            item {
                                val grouped=items.groupBy { it.year?: EMPTY_STRING }
                                grouped.forEach { (year, byYearItems) ->
                                    val totalIncinerated=byYearItems.sumOf { if(it.bloodUnitType?.id in listOf(1,2))it.value?:0 else 0 }
                                    val totalCollected=byYearItems.sumOf { it.campaign?.total?:0 }
                                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                        Span(text = year, backgroundColor = BLUE, color = WHITE, fontWeight = FontWeight.Bold)
                                        HorizontalSpacer()

                                        if(!isIssuing){
                                            Row(verticalAlignment = Alignment.CenterVertically){
                                                Label(COLLECTION_LABEL)
                                                HorizontalSpacer()
                                                Span(text = "$totalCollected", backgroundColor = BLUE, color = WHITE, fontWeight = FontWeight.Bold)

                                            }
                                        }
                                        HorizontalSpacer()
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(INCINERATION_BLOOD_LABEL, fontSize = 12)
                                            HorizontalSpacer()
                                            Span(text = "$totalIncinerated", backgroundColor = Color.Red, color = WHITE, fontWeight = FontWeight.Bold)

                                        }
                                    }
                                    VerticalSpacer()
                                    Row(modifier=Modifier.fillMaxWidth()
                                        .border(width=1.dp,color= Color.LightGray),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier=Modifier.fillMaxWidth().weight(0.4f),
                                            contentAlignment = Alignment.Center){
                                            Label(MONTH_LABEL+"\n", textOverflow = TextOverflow.Ellipsis,
                                                softWrap = true, maximumLines = 2)                                        }
                                        Box(modifier=Modifier.fillMaxWidth().weight(0.4f),
                                            contentAlignment = Alignment.Center){
                                            Label(UNIT_TYPE_LABEL+"\n", fontSize = 12, textOverflow = TextOverflow.Ellipsis,
                                                softWrap = true, maximumLines = 2)                                        }
                                        if(!isIssuing){
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                                contentAlignment = Alignment.Center){
                                                Label(CAMPAIGN_CODE_LABEL+"\n", fontSize = 12, textOverflow = TextOverflow.Ellipsis,
                                                    softWrap = true, maximumLines = 2)
                                            }
                                        }

                                        Box(modifier=Modifier.fillMaxWidth().weight(0.25f),
                                            contentAlignment = Alignment.Center){
                                            Label(VIEW_LABEL+"\n", fontSize = 12, textOverflow = TextOverflow.Ellipsis,
                                                softWrap = true, maximumLines = 2)
                                        }

                                    }
                                    byYearItems.forEach {
                                        Row(modifier=Modifier.fillMaxWidth()
                                            .border(width=1.dp,color= Color.LightGray),
                                            verticalAlignment = Alignment.CenterVertically){
                                            Box(modifier=Modifier.fillMaxWidth().weight(0.4f),
                                                contentAlignment = Alignment.Center){
                                                Label(monthName(it.month), fontSize = 12)
                                            }
                                            Box(modifier=Modifier.fillMaxWidth().weight(0.4f),
                                                contentAlignment = Alignment.Center){
                                                Row(verticalAlignment = Alignment.CenterVertically){
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                                                        Label(it.bloodUnitType?.name?:EMPTY_STRING, fontSize = 12)
                                                        Label(if(it.bloodUnitTypeId in listOf(3,4,5,6)) (it.bloodGroup?.name?: EMPTY_STRING).replace("pos",EMPTY_STRING)
                                                        else it.bloodGroup?.name?:EMPTY_STRING, fontSize = 12)
                                                    }
                                                    HorizontalSpacer()
                                                    Span(text = "${it.value?:0}",
                                                        fontSize = 16,
                                                        backgroundColor = Color.Red, color = WHITE,
                                                        fontWeight = FontWeight.Bold)

                                                }
                                            }
                                            if(!isIssuing){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                                    contentAlignment = Alignment.Center){
                                                    if(it.campaign!=null){
                                                        Label((it.campaign.code?: SPACE), fontSize = 12,
                                                            fontFamily = FontFamily.SansSerif,
                                                            textOverflow = TextOverflow.Ellipsis,
                                                            softWrap = true, maximumLines = 2)
                                                    }
                                                    else {
                                                        Icon(
                                                            R.drawable.ic_round_help_white,
                                                            background = BLUE,
                                                            size = 18,
                                                            containerSize = 23
                                                        )
                                                        VerticalSpacer(1)
                                                    }
                                                }

                                            }

                                            Box(modifier=Modifier.fillMaxWidth().weight(0.25f),
                                                contentAlignment = Alignment.Center){
                                                Box(modifier=Modifier.clickable {
                                                    selectedItem.value=it
                                                    showDialog.value=true
                                                }){
                                                    Icon(R.drawable.ic_eye_blue, size = 26, containerSize = 28)
                                                }
                                            }

                                        }
                                    }
                                    VerticalSpacer()
                                }

                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun IncinerationDetailsDialog(showDialog:MutableState<Boolean>,selectedItem:MutableState<MonthlyIncineration?>){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Box(modifier= Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center){
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)){
                        IconButton(R.drawable.ic_cancel_red) {
                            selectedItem.value=null
                            showDialog.value=false
                        }
                    }
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center){
                       Label(INCINERATION_INFO_LABEL)
                    }

                }
                HorizontalDivider()
                selectedItem.value?.let {item->
                    val year=item.year
                    val month=item.month
                    val monthName=monthName(month)
                    val value=(item.value?:0)
                    val reason=item.reason
                    val maxPercent=item.reason?.maximumPercent?:0f
                    val bloodGroup=item.bloodGroup
                    val campaign=item.campaign
                    val unitTypeId=item.bloodUnitTypeId
                    val unitType=item.bloodUnitType
                    val currentPercent=if(campaign!=null){
                        val totalCampUnits=(campaign.total?:0)
                        val result=(value.toFloat()/totalCampUnits.toFloat())
                        result
                    }else{0f}

                    //val bloodGroup=item.bloodGroup
                    //val unitType=item.bloodUnitType
                    Label(label = year?: EMPTY_STRING,text=monthName)
                    Label(label=UNIT_TYPE_LABEL,text=unitType?.name?: EMPTY_STRING)

                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){

                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label(TOTAL_UNITS_LABEL)
                            HorizontalSpacer()
                            Span(text = "$value", backgroundColor = Color.Red, color = WHITE)
                        }
                        Label(label= BLOOD_GROUP_LABEL,text=if(unitTypeId in listOf(3,4,5,6)) (bloodGroup?.name?: EMPTY_STRING).replace("pos",EMPTY_STRING)
                        else bloodGroup?.name?:EMPTY_STRING, fontSize = 12)
                    }
                    VerticalSpacer()

                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label(INCINERATION_REASON_LABEL)
                            HorizontalSpacer()
                            Span(text = "${reason?.name}", backgroundColor = Color.Red, color = WHITE)

                        }

                        HorizontalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label(MAX_PERCENT_LABEL)
                            HorizontalSpacer()
                            val p=if(maxPercent==100f)"N/A" else {"$maxPercent"}
                            Span(text = p, backgroundColor =if(maxPercent==100f) BLUE else Color.Red, color = WHITE)

                        }
                    }
                    if(campaign!=null){
                        VerticalSpacer()
                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                            Row(verticalAlignment = Alignment.CenterVertically){
                                Label(COLLECTION_LABEL)
                                HorizontalSpacer()
                                Span(text = "${campaign.total}", backgroundColor = BLUE, color = WHITE)

                            }
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Label(PERCENT_LABEL)
                                HorizontalSpacer()
                                Span(text = "${(currentPercent*100)}%", backgroundColor = if((currentPercent*100)>maxPercent) Color.Red else BLUE, color = WHITE)
                            }

                        }
                        VerticalSpacer()

                        Label(CAMPAIGN_LABEL,campaign.code?:EMPTY_STRING)
                        VerticalSpacer()
                        campaign.collectionDate?.let {
                            val dateOnly=it.replaceRange(10,it.length,EMPTY_STRING)
                            Label(INCINERATION_DATE_LABEL, dateOnly)
                        }



                    }

                }
                VerticalSpacer(10)
            }
        }
    }
}
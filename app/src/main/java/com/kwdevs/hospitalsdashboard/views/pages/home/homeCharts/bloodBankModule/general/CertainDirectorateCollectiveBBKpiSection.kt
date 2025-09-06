package com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.KpiType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ALEXANDRIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ALL_DIRECTORATES
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASUIT
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASWAN
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BANI_SUIF
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BUHIRA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CAIRO
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAKAHLIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAMIATTA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.Directorate
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.FAYUM
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GHARBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GIZA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ISMAILIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.KAFR_EL_SHEIKH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.LUXOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MATROUH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MENUFIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MINIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NEW_VALLEY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NORTH_SINAI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PORT_SAID
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PermissionSector
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QALUBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QENA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.RED_SEA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SHARQIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SOHAG
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SOUTH_SINAI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SUEZ
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasDirectoratePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasPermission
import com.kwdevs.hospitalsdashboard.views.assets.ALEXANDRIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALL_DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ASUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ASWAN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BANI_SUIF_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BUHIRA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAIRO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DAKAHLIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DAMIATTA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DAY_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DAY_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DualTableColumn
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.FAYUM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GHARBIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GIZA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.ISMAILIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.KAFR_EL_SHEIKH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.LUXOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MATROUH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MENUFIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MINIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_VALLEY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NORTH_SINAI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PORT_SAID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QALUBIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QENA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RED_SEA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHARQIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SOHAG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SUEZ_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THE_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TableColumn
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.monthName
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.pages.home.saveExcelFile
import com.kwdevs.hospitalsdashboard.views.rcs
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun CertainDirectorateCollectiveBBKpiSection(controller:HomeController){
    val context= LocalContext.current
    val settingsController:SettingsController= viewModel()
    val kpiBody       =  remember { mutableStateOf<KpiFilterBody?>(null) }
    val state         by controller.certainDirectorateState.observeAsState()
    var items         by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var reasons       by remember { mutableStateOf<List<IncinerationReason>>(emptyList()) }
    var chartXLabels  by remember { mutableStateOf<List<String>>(emptyList())             }
    var chartDatasets by remember { mutableStateOf<List<IBarDataSet>>(emptyList())        }
    val showDialog    =  remember { mutableStateOf(false) }

    var toggleReactive by remember { mutableStateOf(false) }
    val toggleReactiveIcon=if(toggleReactive)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white
    var toggleIncinerated by remember { mutableStateOf(false) }
    val toggleIncineratedIcon=if(toggleIncinerated)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white
    var toggleAchievement by remember { mutableStateOf(false) }
    val toggleAchievementIcon=if(toggleAchievement)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white
    val exportState by settingsController.certainDirectorateBloodExcelState.observeAsState()
    var downloading by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var showSaveExcelDialog by remember { mutableStateOf(false) }
    val excelFileName       = remember { mutableStateOf("directorate_kpis") }

    LaunchedEffect(items) {
        val (x,ds)= prepareGroupedData(data = items)
        chartXLabels=x
        chartDatasets=ds
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
                loading=false;fail=false;success=true
                val s = state as UiState.Success<HospitalBloodBankKpiResponse>
                val r=s.data
                val data=r.data
                items=data?.hospitals?: emptyList()
                reasons=data?.reasons?: emptyList()

            }

        }
        else->{}
    }
    when (exportState) {
        is UiState.Loading->{LaunchedEffect(Unit) {downloading=true}}
        is UiState.Success<*> -> {
            LaunchedEffect(Unit) {
                downloading=false
                val s =exportState as UiState.Success<Response<ResponseBody>>
                val r =s.data
                if (r.isSuccessful) {
                    val saved = saveExcelFile(
                        responseBody = r.body()!!,
                        fileName = "${excelFileName.value}.xlsx"
                    )
                    toast(context,if (saved) "Downloaded to Downloads folder" else "Failed to save file")

                } else toast(context,"Download failed: ${r.code()}")
            }

        }
        is UiState.Error -> {
            LaunchedEffect(Unit) {
                downloading=false
                val message = (exportState as UiState.Error).exception.message ?: "Unknown error"
                toast(context,"Error: $message")
            }

        }
        else -> {downloading=false}
    }
    if(showSaveExcelDialog){
        Dialog(onDismissRequest = {showSaveExcelDialog=false}) {
            ColumnContainer {
                VerticalSpacer()
                Label(SAVE_PROMPT)
                CustomInput(excelFileName, NAME_LABEL)
                CustomButton(label = SAVE_CHANGES_LABEL,
                    enabled = excelFileName.value!= EMPTY_STRING
                ) {
                    if(excelFileName.value!= EMPTY_STRING){
                        val b=kpiBody.value
                        b?.let{
                            settingsController.saveCertainDirectorateBloodKpiToExcel(it)
                        }
                    }
                    showSaveExcelDialog=false
                }
                VerticalSpacer()
            }
        }
    }
    Row(modifier=Modifier.fillMaxWidth()
        .background(WHITE).padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        CustomButton(
            label = " مؤشرات أداء بنوك الدم \nالتابعة للمديرية ",
            enabledBackgroundColor = Color.Transparent,
            disabledFontColor = GRAY,
            enabledFontColor = BLUE,
            hasBorder = true,
            borderColor = BLUE,
            buttonShape = rcs(8),
            icon = R.drawable.ic_bar_chart_blue
        ) { showDialog.value=true }
        if(kpiBody.value!=null && items.isNotEmpty()){
            IconButton(R.drawable.ic_save_blue) { if(!downloading){ kpiBody.value?.let{showSaveExcelDialog=true} } }
        }else Box{}

    }
    KpiDialog(showDialog = showDialog,controller = controller, kpiBody = kpiBody)

    if(loading) LoadingScreen()
    else{
        if(success){
            if(items.isNotEmpty()){
                VerticalSpacer()
                HorizontalDivider()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .background(ORANGE)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleReactiveIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleReactive=!toggleReactive }
                    Label("المجمع/المنصرف/التهاب بي/التهاب سي/ايدز/زهرى",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleReactive)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AnimatedVisibility(visible = toggleReactive /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "$THE_HOSPITAL_LABEL\n",
                                items = items.map { it.hospitalName?:EMPTY_STRING })
                            LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){
                                item{
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المجمع\n",
                                        items = items.map { (it.totalCollected?:0).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المنصرف\n",
                                        items = items.map { (it.totalIssued?:0).toString() })
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = "فيروس بى",
                                        firstSubHeader = "العدد",
                                        secondSubHeader = "النسبة <${reasons[3].maximumPercent}",
                                        firstList = items.map { (it.totalHbv?:0).toString() },
                                        secondList = items.map {
                                            val value=it.totalHbv?:0
                                            val collected=it.totalCollected
                                            val posCollected=if(collected==0 || collected==null) 1 else collected
                                            val ratio=value.toFloat()/posCollected.toFloat()*100
                                            (ceil(ratio).toString())

                                        }
                                    )
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = "فيروس سي",
                                        firstSubHeader = "العدد",
                                        secondSubHeader = "النسبة <${reasons[2].maximumPercent}",
                                        firstList = items.map { (it.totalHcv?:0).toString() },
                                        secondList = items.map {
                                            val value=it.totalHcv?:0
                                            val collected=it.totalCollected
                                            val posCollected=if(collected==0 || collected==null) 1 else collected
                                            val ratio=value.toFloat()/posCollected.toFloat()*100
                                            (ceil(ratio).toString())

                                        }
                                    )
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = "ايدز",
                                        firstSubHeader = "العدد",
                                        secondSubHeader = "النسبة <${reasons[4].maximumPercent}",
                                        firstList = items.map { (it.totalHiv?:0).toString() },
                                        secondList = items.map {
                                            val value=it.totalHiv?:0
                                            val collected=it.totalCollected
                                            val posCollected=if(collected==0 || collected==null) 1 else collected
                                            val ratio=value.toFloat()/posCollected.toFloat()*100
                                            (ceil(ratio).toString()) }
                                    )
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = "زهرى",
                                        firstSubHeader = "العدد",
                                        secondSubHeader = "النسبة <${reasons[5].maximumPercent}",
                                        firstList = items.map { (it.totalSyphilis?:0).toString() },
                                        secondList = items.map {
                                            val value=it.totalSyphilis?:0
                                            val collected=it.totalCollected
                                            val posCollected=if(collected==0 || collected==null) 1 else collected
                                            val ratio=value.toFloat()/posCollected.toFloat()*100
                                            (ceil(ratio).toString())
                                        }
                                    )

                                }
                            }
                        }
                    }
                }
                VerticalSpacer()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleIncineratedIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleIncinerated=!toggleIncinerated }
                    Label("ايجابى/لم يتم/منتهى الصلاحية",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleIncinerated)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AnimatedVisibility(visible = toggleIncinerated /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(2f),
                                header = "$THE_HOSPITAL_LABEL\n",
                                items = items.map { it.hospitalName?:EMPTY_STRING })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(0.7f),
                                header = "مجمع\n",
                                items = items.map { (it.totalCollected?:0).toString() })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(0.8f),
                                header = "منصرف\n",
                                items = items.map { (it.totalIssued?:0).toString() })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(0.6f),
                                header = "إيجابى\n",
                                items = items.map { ((it.totalHbv?:0)+(it.totalHcv?:0)+(it.totalHiv?:0)+(it.totalSyphilis?:0)).toString() })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "منتهى\nالصلاحية",
                                headFontSize = 11,
                                items = items.map { (it.totalExpired?:0).toString() })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "غير\nمكتمل",
                                headFontSize = 11,
                                items = items.map { (it.totalIncomplete?:0).toString() })
                            //LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){item{}}

                        }
                    }
                }
                VerticalSpacer()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE)
                    .padding(horizontal =10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleAchievementIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleAchievement=!toggleAchievement }
                    Label("المجمع/المنصرف/التحقيق",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleAchievement)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AnimatedVisibility(visible = toggleAchievement /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "$THE_HOSPITAL_LABEL\n",
                                items = items.map { it.hospitalName?:EMPTY_STRING })
                            LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){
                                item{
                                TableColumn(
                                    modifier=Modifier,
                                    header = "المجمع\n",
                                    items = items.map { (it.totalCollected?:0).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "المنصرف\n",
                                    items = items.map { (it.totalIssued?:0).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "العجز\n",
                                    items = items.map {
                                        val totalCollected=it.totalCollected?:1
                                        val totalIssued=it.totalIssued?:0
                                        val totalDifference=if((totalIssued-totalCollected)>0) totalIssued-totalCollected else 0
                                        (totalDifference).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "نسبة\n العجز",
                                    items = items.map {
                                        val totalCollected=(it.totalCollected?:1) //90
                                        val totalIssued=(it.totalIssued?:0) //100
                                        //649+(649*(20/100))
                                        val notNullCol=if(totalCollected==0) 1 else totalCollected //90
                                        val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f)) //90+(90*20/100)

                                        val posIssued=if(totalIssued>0) 0f+totalIssued+(totalIssued*(20f/100f)) else 0f
                                        val totalRequired =if (totalIssued > totalCollected) posIssued else collectedIncrement
                                        // 649/778
                                        val achievement=if(totalIssued>totalCollected)totalCollected/totalRequired else 1f
                                        val achievementPercent=floor(achievement*100f)

                                        val differencePercent=floor(100-achievementPercent)
                                        differencePercent.toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "المستهدف\n",
                                    items = items.map {
                                        val totalCollected=(it.totalCollected?:1) //649
                                        val totalIssued=(it.totalIssued?:0) //290
                                        //649+(649*(20/100))
                                        val notNullCol=if(totalCollected==0) 1 else totalCollected
                                        val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f))
                                        //290+(290*(20/100))
                                        val posIssued=if(totalIssued>0) 0f+totalIssued+(totalIssued*(20f/100f)) else 1f
                                        val totalRequired =if (totalIssued > totalCollected) posIssued else collectedIncrement
                                        val ceil= ceil(totalRequired).toInt()
                                        ceil.toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "نسبة\n التحقيق",
                                    items = items.map {
                                        val totalCollected=(it.totalCollected?:1) //649
                                        val totalIssued=(it.totalIssued?:0) //290
                                        // 649+(649*(20/100))
                                        val notNullCol=if(totalCollected==0) 1 else totalCollected
                                        val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f))
                                        val totalRequired=if(totalIssued>totalCollected) 100f else collectedIncrement
                                        val achievement=totalCollected/totalRequired
                                        val achievementPercent=floor(achievement*100f)
                                        achievementPercent.toString() })
                                }
                            }
                        }
                    }
                }
                VerticalSpacer()
                HorizontalDivider()
                VerticalSpacer()
            }
        }
    }
}

@Composable
private fun KpiDialog(showDialog:MutableState<Boolean>,controller: HomeController,
                      kpiBody: MutableState<KpiFilterBody?>){
    val superUser=Preferences.User().getSuper()
    val certainDirectoratePermission=superUser?.hasPermission(action = CRUD.VIEW, resource =PermissionSector.CERTAIN_DIRECTORATE )?:false
    val yearFrom = remember { mutableStateOf("2025") }
    val monthFrom = remember { mutableStateOf("1") }
    val dayFrom = remember { mutableStateOf("1") }
    val yearTo = remember { mutableStateOf("2025") }
    val monthTo = remember { mutableStateOf("12") }
    val dayTo = remember { mutableStateOf("28") }
    val years: List<String> = (2025..2065).map { it.toString() }
    val months: List<String> = (1..12).map { it.toString() }
    val days: List<String> = (1..31).map { it.toString() }
    val firstQ=monthFrom.value.toInt() in listOf(1,2,3)
    val secondQ=monthFrom.value.toInt() in listOf(4,5,6)
    val thirdQ=monthFrom.value.toInt() in listOf(7,8,9)
    val fourthQ=monthFrom.value.toInt() in listOf(7,8,9)
    val firstSim=monthTo.value.toInt() in listOf(1,2,3,4,5,6)
    val secondSim=monthTo.value.toInt() in listOf(7,8,9,10,11,12)
    val selectedDirectorate= remember { mutableStateOf<Pair<Directorate,String>?>(null) }
    var range by remember { mutableStateOf(EMPTY_STRING) }
    val directoratesList= remember { mutableStateOf<List<Pair<Directorate,String>>>(emptyList()) }

    range=when(Pair(monthFrom.value,monthTo.value)){

        Pair(if(firstQ) monthFrom.value else "1","3")->"عن الربع الاول"
        Pair(if(secondQ) monthFrom.value else "4","6")->"عن الربع الثانى"
        Pair(if(thirdQ) monthFrom.value else "7","9")->"عن الربع الثالث"
        Pair(if(fourthQ) monthFrom.value else "10","12")->"عن الربع الرابع"
        Pair(if(firstSim) monthFrom.value else "1",if(firstSim) monthTo.value else "6")->"عن النصف الاول"
        Pair(if(secondSim) monthFrom.value else "7",if(secondSim) monthTo.value else "12")->"عن النصف الثانى"
        else->{EMPTY_STRING}
    }
    LaunchedEffect(certainDirectoratePermission) {
        if(certainDirectoratePermission){
            val availableDirectoratesList= mutableListOf<Pair<Directorate,String>>()
            superUser?.let {
                val canViewAllDirectorates=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ALL)
                val canViewAlexandria=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ALEXANDRIA)
                val canViewAswan=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ASWAN)
                val canViewAsuit=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ASUIT)

                val canViewBaniSuif=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.BANI_SUIF)
                val canViewBuhira=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.BUHIRA)

                val canViewCairo=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.CAIRO)

                val canViewDamiatta=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.DAMIATTA)
                val canViewDakahlia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.DAKAHLIA)

                val canViewFayum=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.FAYUM)

                val canViewGiza=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.GIZA)
                val canViewGharbia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.GHARBIA)

                val canViewIsmailia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ISMAILIA)

                val canViewKafrElShiekh=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.KAFR_EL_SHEIKH)

                val canViewLuxor=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.LUXOR)

                val canViewMatrouh=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MATROUH)
                val canViewMenufia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MENUFIA)
                val canViewMinia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MINIA)

                val canViewNewValley=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.NEW_VALLEY)
                val canViewNorthSinai=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.NORTH_SINAI)

                val canViewPortSaid=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.PORT_SAID)

                val canViewQena=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.QENA)
                val canViewQalubia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.QALUBIA)

                val canViewRedSea=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.RED_SEA)

                val canViewSohag=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SOHAG)
                val canViewSuez=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SUEZ)
                val canViewSharqia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SHARQIA)

                if(canViewAllDirectorates){availableDirectoratesList.add(Pair(Directorate.ALL,
                    ALL_DIRECTORATES_LABEL
                ))}
                else{
                    if(canViewAlexandria){availableDirectoratesList.add(Pair(Directorate.ALEXANDRIA,
                        ALEXANDRIA_LABEL
                    ))}
                    if(canViewAswan){availableDirectoratesList.add(Pair(Directorate.ASWAN,
                        ASWAN_LABEL
                    ))}
                    if(canViewAsuit){availableDirectoratesList.add(Pair(Directorate.ASUIT,
                        ASUIT_LABEL
                    ))}

                    if(canViewBaniSuif){availableDirectoratesList.add(Pair(Directorate.BANI_SUIF,
                        BANI_SUIF_LABEL
                    ))}
                    if(canViewBuhira){availableDirectoratesList.add(Pair(Directorate.BUHIRA,
                        BUHIRA_LABEL
                    ))}
                    
                    if(canViewCairo){availableDirectoratesList.add(Pair(Directorate.CAIRO,CAIRO_LABEL))}

                    if(canViewDamiatta){availableDirectoratesList.add(Pair(Directorate.DAMIATTA,DAMIATTA_LABEL))}
                    if(canViewDakahlia){availableDirectoratesList.add(Pair(Directorate.DAKAHLIA,DAKAHLIA_LABEL))}

                    if(canViewFayum){availableDirectoratesList.add(Pair(Directorate.FAYUM,FAYUM_LABEL))}

                    if(canViewGiza){availableDirectoratesList.add(Pair(Directorate.GIZA, GIZA_LABEL))}
                    if(canViewGharbia){availableDirectoratesList.add(Pair(Directorate.GHARBIA,GHARBIA_LABEL))}

                    if(canViewIsmailia){availableDirectoratesList.add(Pair(Directorate.ISMAILIA,ISMAILIA_LABEL))}

                    if(canViewKafrElShiekh){availableDirectoratesList.add(Pair(Directorate.KAFR_EL_SHEIKH,KAFR_EL_SHEIKH_LABEL))}

                    if(canViewLuxor){availableDirectoratesList.add(Pair(Directorate.LUXOR,LUXOR_LABEL))}

                    if(canViewMinia){availableDirectoratesList.add(Pair(Directorate.MINIA,MINIA_LABEL))}
                    if(canViewMatrouh){availableDirectoratesList.add(Pair(Directorate.MATROUH,MATROUH_LABEL))}
                    if(canViewMenufia){availableDirectoratesList.add(Pair(Directorate.MENUFIA,MENUFIA_LABEL))}

                    if(canViewNewValley){availableDirectoratesList.add(Pair(Directorate.NEW_VALLEY,NEW_VALLEY_LABEL))}
                    if(canViewNorthSinai){availableDirectoratesList.add(Pair(Directorate.NORTH_SINAI,NORTH_SINAI_LABEL))}

                    if(canViewPortSaid){availableDirectoratesList.add(Pair(Directorate.PORT_SAID,PORT_SAID_LABEL))}

                    if(canViewQalubia){availableDirectoratesList.add(Pair(Directorate.QALUBIA,QALUBIA_LABEL))}
                    if(canViewQena){availableDirectoratesList.add(Pair(Directorate.QENA, QENA_LABEL))}

                    if(canViewRedSea){availableDirectoratesList.add(Pair(Directorate.RED_SEA,RED_SEA_LABEL))}

                    if(canViewSharqia){availableDirectoratesList.add(Pair(Directorate.SHARQIA,SHARQIA_LABEL))}
                    if(canViewSuez){availableDirectoratesList.add(Pair(Directorate.SUEZ, SUEZ_LABEL))}
                    if(canViewSohag){availableDirectoratesList.add(Pair(Directorate.SOHAG,SOHAG_LABEL))}
                }
                directoratesList.value=availableDirectoratesList

            }

        }

    }
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}){
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(
                            title = YEAR_FROM_LABEL,
                            hasTitle = true,
                            selectedItem = yearFrom,
                            loadedItems = years,
                            selectedContent = { CustomInput(yearFrom.value)}) { Label(it) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(title = MONTH_FROM_LABEL, hasTitle = true,selectedItem = monthFrom, loadedItems = months, selectedContent = { CustomInput(
                            monthName(monthFrom.value))}) { Label(monthName(it)) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(
                            hasTitle = true,
                            title = DAY_FROM_LABEL,
                            selectedItem = dayFrom, loadedItems = days, selectedContent = { CustomInput(dayFrom.value)}) { Label(it) }
                    }
                }
                VerticalSpacer(10)
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(title = YEAR_TO_LABEL, hasTitle = true,selectedItem = yearTo, loadedItems = years, selectedContent = { CustomInput(yearTo.value)}) { Label(it) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(title = MONTH_TO_LABEL, hasTitle = true,selectedItem = monthTo, loadedItems = months,
                            selectedContent = { CustomInput(monthName(monthTo.value))}) { Label(monthName(it)) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(
                            hasTitle = true,
                            title = DAY_TO_LABEL,
                            selectedItem = dayTo, loadedItems = days, selectedContent = { CustomInput(dayTo.value)}) { Label(it) }
                    }
                }
                VerticalSpacer(10)
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                    Box(modifier= Modifier.fillMaxWidth()){
                        ComboBox(
                            hasTitle = true,
                            title = DIRECTORATE_LABEL,
                            selectedItem = selectedDirectorate, loadedItems = directoratesList.value, selectedContent = { CustomInput(selectedDirectorate.value?.second?: SELECT_DIRECTORATE_LABEL)}) { Label(it?.second?: EMPTY_STRING) }
                    }
                }
                VerticalSpacer(10)
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                    CustomButton(label= FILTER_LABEL,
                        enabledBackgroundColor = Color.Transparent,
                        buttonShadowElevation = 6,
                        disabledBackgroundColor = Color.Transparent,
                        enabledFontColor = BLUE,
                        enabled = selectedDirectorate.value!=null,
                        disabledFontColor = GRAY,
                        hasBorder = true,
                        borderColor = if(selectedDirectorate.value!=null) BLUE else GRAY,
                        icon = if(selectedDirectorate.value!=null) R.drawable.ic_filter_blue else R.drawable.ic_filter_gray,
                        buttonShape = rcs(10)) {
                        val slug=when(selectedDirectorate.value?.first){
                            Directorate.ALL->ALL_DIRECTORATES
                            Directorate.ALEXANDRIA-> ALEXANDRIA
                            Directorate.ASWAN-> ASWAN
                            Directorate.ASUIT-> ASUIT

                            Directorate.BUHIRA-> BUHIRA
                            Directorate.BANI_SUIF-> BANI_SUIF

                            Directorate.DAKAHLIA-> DAKAHLIA
                            Directorate.DAMIATTA-> DAMIATTA

                            Directorate.CAIRO-> CAIRO

                            Directorate.FAYUM-> FAYUM

                            Directorate.GIZA-> GIZA
                            Directorate.GHARBIA-> GHARBIA

                            Directorate.ISMAILIA-> ISMAILIA

                            Directorate.KAFR_EL_SHEIKH-> KAFR_EL_SHEIKH

                            Directorate.LUXOR-> LUXOR

                            Directorate.MATROUH-> MATROUH
                            Directorate.MENUFIA-> MENUFIA
                            Directorate.MINIA-> MINIA
                            Directorate.NEW_VALLEY-> NEW_VALLEY
                            Directorate.NORTH_SINAI-> NORTH_SINAI

                            Directorate.PORT_SAID-> PORT_SAID

                            Directorate.QALUBIA-> QALUBIA
                            Directorate.QENA-> QENA

                            Directorate.RED_SEA-> RED_SEA

                            Directorate.SOHAG-> SOHAG
                            Directorate.SUEZ-> SUEZ
                            Directorate.SHARQIA-> SHARQIA
                            Directorate.SOUTH_SINAI-> SOUTH_SINAI

                            else->{EMPTY_STRING}
                        }.replace("directorate@", EMPTY_STRING)
                        kpiBody.value = KpiFilterBody(
                            yearFrom    = yearFrom.value    ,
                            monthFrom   = monthFrom.value   ,
                            dayFrom     = dayFrom.value     ,
                            yearTo      = yearTo.value      ,
                            monthTo     = monthTo.value     ,
                            dayTo       = dayTo.value       ,
                            citySlug    =slug,
                        )
                        Log.e("Slug",slug)
                        kpiBody.value?.let {controller.getCertainDirectorateCharts(it)}
                        showDialog.value=false
                    }
                }
            }
        }
    }
}

@Composable
fun CDFinalBarChart(modifier: Modifier,//this controls the chart modifier
                  chartXLabels:List<String>,
                  datasets:List<IBarDataSet>, ){
    if(datasets.isNotEmpty()){
        AndroidView(
            modifier = modifier,
            factory = { context ->
                BarChart(context).apply {
                    description.isEnabled = false
                    axisRight.isEnabled = false
                    setDrawValueAboveBar(true)
                    setPinchZoom(true)
                    setScaleEnabled(true)
                    setDrawGridBackground(false)
                    xAxis.setCenterAxisLabels(true)
                    moveViewToX(0f)
                    xAxis.axisMinimum=0f
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f
                    legend.isEnabled = true
                    legend.textSize=12f
                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                }
            },
            update = { chart ->
                val barData = BarData(datasets)
                val datasetsSize=datasets.size
                val groupCount = chartXLabels.size // 28
                val groupSpace = 0.3f
                val barSpace = 0.02f
                val width=1f
                val barWidth = (width - groupSpace) / datasetsSize - barSpace //(1-0.2)/(2-0.02) 0.8/1.8
                barData.barWidth = barWidth
                chart.data = barData
                chart.setFitBars(true)
                chart.isDragEnabled = true

                chart.setVisibleXRangeMaximum(12f * datasetsSize) // or another reasonable value

                val groupWidth = barData.getGroupWidth(groupSpace, barSpace)
                val separatorLineColor = GRAY.toArgb()  // or any color you prefer
                val separatorLineWidth = 2f             // width in dp

                for (i in chartXLabels.indices) {
                    val separatorXPosition = i * (datasetsSize * (barWidth + barSpace) + groupSpace)
                    val separatorLine = LimitLine(separatorXPosition, EMPTY_STRING)
                    separatorLine.lineColor = separatorLineColor
                    separatorLine.lineWidth = separatorLineWidth
                    chart.xAxis.addLimitLine(separatorLine)
                }

                chart.axisLeft.axisMinimum=0f

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(chartXLabels)
                    setLabelCount(chartXLabels.size, /*force=*/true)
                    setDrawLabels(true)
                    spaceMax=-4f
                    //spaceMin=-2f
                    labelRotationAngle=90f
                    setDrawGridLines(false)
                    textSize    = 10f
                    //axisMaximum = 0f + (groupWidth * groupCount)
                    if(groupCount>1){
                        try{
                            chart.xAxis.setCenterAxisLabels(true)
                            chart.groupBars(0f, groupSpace, barSpace)
                            chart.xAxis.axisMaximum = 0f + (groupWidth * groupCount)
                        }
                        catch (e:Exception){ e.printStackTrace() }
                    }

                }

                chart.notifyDataSetChanged()
                chart.invalidate()
            },
        )
    }
}


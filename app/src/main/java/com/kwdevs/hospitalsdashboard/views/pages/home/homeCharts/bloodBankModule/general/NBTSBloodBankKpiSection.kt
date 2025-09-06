package com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.KpiType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DualTableColumn
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.TableColumn
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.pages.home.saveExcelFile
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun NBTSBloodBankKpiSection(controller:HomeController){
    val context       = LocalContext.current
    val kpiBody       =  remember { mutableStateOf<KpiFilterBody?>(null) }
    val state         by controller.nBTSKpiState.observeAsState()
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

    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

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
    var downloading by remember { mutableStateOf(false) }
    val issuingDepartmentController:BloodBankIssuingDepartmentController= viewModel()
    val exportState by issuingDepartmentController.nbtsExcelState.observeAsState()

    LaunchedEffect(exportState) {
        when (exportState) {
            is UiState.Loading->{downloading=true}
            is UiState.Success<*> -> {
                downloading=false
                val s =exportState as UiState.Success<Response<ResponseBody>>
                val r =s.data
                if (r.isSuccessful) {
                    val saved = saveExcelFile(
                        responseBody = r.body()!!,
                        fileName = "nbts_bb_kpi.xlsx"
                    )
                    toast(context,if (saved) "Downloaded to Downloads folder" else "Failed to save file")

                } else toast(context,"Download failed: ${r.code()}")
            }
            is UiState.Error -> {
                downloading=false
                val message = (exportState as UiState.Error).exception.message ?: "Unknown error"
                toast(context,"Error: $message")
            }
            else -> {downloading=false}
        }
    }
    val excelFileName       = remember { mutableStateOf("nbts_kpis") }
    var showSaveExcelDialog by remember { mutableStateOf(false) }
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
                            issuingDepartmentController.saveKpiExcelFile(KpiType.NBTS,it)
                        }
                    }
                    showSaveExcelDialog=false
                }
                VerticalSpacer()
            }
        }
    }
    Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        IconButton(R.drawable.ic_filter_blue) { showDialog.value=true }
        Label(" مؤشرات أداء خدمات نقل \nالدم القومية ",
            fontSize = 12,
            textAlign = TextAlign.Center,
            maximumLines = 2,
            fontWeight = FontWeight.Bold,
            color = BLUE)
        if(kpiBody.value!=null && items.isNotEmpty()){
            IconButton(R.drawable.ic_save_blue) {
                if(!downloading){ kpiBody.value?.let{showSaveExcelDialog=true} }
            }
        }else{
            Box{}
        }
    }
    KpiDialog(showDialog = showDialog,controller = controller, kpiBody = kpiBody)
    if(loading) LoadingScreen()
    else{
        if(success){
            if(items.isNotEmpty()){
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleReactiveIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleReactive=!toggleReactive }
                    Label("المجمع/المنصرف/التهاب بي/التهاب سي/ايدز/زهرى",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleReactive)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                ReactiveSection(toggleReactive,items,reasons)
                VerticalSpacer()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleIncineratedIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleIncinerated=!toggleIncinerated }
                    Label("الاعدام/لم يتم/منتهى الصلاحية",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleIncinerated)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                IncineratedSection(toggle=toggleIncinerated,items=items)

                VerticalSpacer()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleAchievementIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleAchievement=!toggleAchievement }
                    Label("المجمع/المنصرف/التحقيق",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleAchievement)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AchievementSection(toggle=toggleAchievement,items=items)
            }
        }
    }
}

@Composable
fun IncineratedSection(toggle: Boolean, items: List<HospitalBloodBankKpi>) {
    AnimatedVisibility(visible = toggle /**/) {
        Column{
            Row(modifier=Modifier.fillMaxWidth()){
                TableColumn(
                    modifier=Modifier.fillMaxWidth().weight(2f),
                    header = "الاسم\n",
                    items = items.map { it.hospitalName?:"" })
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
                            header = "إعدام\nإيجابى",
                            items = items.map { ((it.totalHbv?:0)+(it.totalHcv?:0)+(it.totalHiv?:0)+(it.totalSyphilis?:0)).toString() })
                        TableColumn(
                            modifier=Modifier,
                            header = "منتهى\n الصلاحية",
                            items = items.map { (it.totalExpired?:0).toString() })
                        TableColumn(
                            modifier=Modifier,
                            header = "غير\nمكتمل",
                            items = items.map { (it.totalIncomplete?:0).toString() })

                    }
                }

            }
        }
    }

}

@Composable
fun AchievementSection(toggle: Boolean, items: List<HospitalBloodBankKpi>) {
    AnimatedVisibility(visible = toggle) {
        Column{
            Row(modifier=Modifier.fillMaxWidth()){
                TableColumn(
                    modifier=Modifier.fillMaxWidth().weight(2f),
                    header = "الاسم\n",
                    items = items.map { it.hospitalName?:"" })
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
}

@Composable
private fun ReactiveSection(toggle: Boolean, items: List<HospitalBloodBankKpi>,reasons:List<IncinerationReason>){
    AnimatedVisibility(visible = toggle /**/) {
        Column{
            Row(modifier=Modifier.fillMaxWidth()){
                TableColumn(
                    modifier=Modifier.fillMaxWidth().weight(2f),
                    header = "الاسم\n",
                    items = items.map { it.hospitalName?:"" })
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

}

@Composable
private fun KpiDialog(showDialog:MutableState<Boolean>,controller: HomeController,
                      kpiBody: MutableState<KpiFilterBody?>){
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
    var range by remember { mutableStateOf("") }
    range=when(Pair(monthFrom.value,monthTo.value)){

        Pair(if(firstQ) monthFrom.value else "1","3")->"عن الربع الاول"
        Pair(if(secondQ) monthFrom.value else "4","6")->"عن الربع الثانى"
        Pair(if(thirdQ) monthFrom.value else "7","9")->"عن الربع الثالث"
        Pair(if(fourthQ) monthFrom.value else "10","12")->"عن الربع الرابع"
        Pair(if(firstSim) monthFrom.value else "1",if(firstSim) monthTo.value else "6")->"عن النصف الاول"
        Pair(if(secondSim) monthFrom.value else "7",if(secondSim) monthTo.value else "12")->"عن النصف الثانى"
        else->{""}
    }

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}){
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
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
                        ComboBox(title = MONTH_FROM_LABEL, hasTitle = true,selectedItem = monthFrom, loadedItems = months, selectedContent = { CustomInput(monthFrom.value)}) { Label(it) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(selectedItem = dayFrom, loadedItems = days, selectedContent = { CustomInput(dayFrom.value)}) { Label(it) }
                    }
                }
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
                        ComboBox(title = MONTH_TO_LABEL, hasTitle = true,selectedItem = monthTo, loadedItems = months, selectedContent = { CustomInput(monthTo.value)}) { Label(it) }
                    }
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 5.dp)){
                        ComboBox(selectedItem = dayTo, loadedItems = days, selectedContent = { CustomInput(dayTo.value)}) { Label(it) }
                    }
                }
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    CustomButton(label="Filter",
                        enabledBackgroundColor = BLUE,
                        buttonShadowElevation = 6,
                        buttonShape = RectangleShape) {
                        kpiBody.value = KpiFilterBody(
                            yearFrom    = yearFrom.value    ,
                            monthFrom   = monthFrom.value   ,
                            dayFrom     = dayFrom.value     ,
                            yearTo      = yearTo.value      ,
                            monthTo     = monthTo.value     ,
                            dayTo       = dayTo.value       ,
                        )
                        kpiBody.value?.let {controller.getNBTSCharts(it)}
                        showDialog.value=false
                    }
                }
            }
        }
    }
}

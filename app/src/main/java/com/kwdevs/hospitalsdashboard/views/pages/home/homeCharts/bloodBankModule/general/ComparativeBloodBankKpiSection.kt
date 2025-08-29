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
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityBloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.ComparativeBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpi
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DualTableColumn
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.TableColumn
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule.baseColors
import com.kwdevs.hospitalsdashboard.views.pages.home.saveExcelFile
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ComparativeBloodBankKpiSection(controller:HomeController){
    val context                 = LocalContext.current
    val kpiBody                 =  remember { mutableStateOf<KpiFilterBody?>(null) }
    val state                   by controller.comparativeKpiState.observeAsState()
    var directorate             by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var insurance               by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var educational             by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var specialized             by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var nbts                    by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }
    var curative                by remember { mutableStateOf<List<HospitalBloodBankKpi>>(emptyList())   }

    var reasons                 by remember { mutableStateOf<List<IncinerationReason>>(emptyList()) }
    var chartXLabels            by remember { mutableStateOf<List<String>>(emptyList())             }
    var chartDatasets           by remember { mutableStateOf<List<IBarDataSet>>(emptyList())        }
    val showDialog              =  remember { mutableStateOf(false) }

    var toggleReactive          by remember { mutableStateOf(false) }
    val toggleReactiveIcon      =  if(toggleReactive)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white
    var toggleIncinerated       by remember { mutableStateOf(false) }
    val toggleIncineratedIcon   =  if(toggleIncinerated)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white
    var toggleAchievement       by remember { mutableStateOf(false) }
    val toggleAchievementIcon   =  if(toggleAchievement)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    var loading                 by remember { mutableStateOf(false) }
    var fail                    by remember { mutableStateOf(false) }
    var success                 by remember { mutableStateOf(false) }

    LaunchedEffect(directorate) {
        val (x,ds)= prepareGroupedData(data = directorate)
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
                val s = state as UiState.Success<ComparativeBloodBankKpiResponse>
                val r=s.data
                val data=r.data
                directorate=data?.directorate?: emptyList()
                reasons=data?.reasons?: emptyList()

            }

        }
        else->{}
    }
    var downloading by remember { mutableStateOf(false) }
    val issuingDepartmentController:BloodBankIssuingDepartmentController= viewModel()
    val exportState by issuingDepartmentController.directorateKpiExcelState.observeAsState()

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
                        fileName = "city_collective_bb_kpi.xlsx"
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
    Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        IconButton(R.drawable.ic_filter_blue) { showDialog.value=true }
        Label(" مؤشرات أداء بنوك الدم \nالتابعة لمديريات الشئون الصحية ",
            fontSize = 12,
            textAlign = TextAlign.Center,
            maximumLines = 2,
            fontWeight = FontWeight.Bold,
            color = BLUE)
        Box{}
    }
    KpiDialog(showDialog = showDialog,controller = controller, kpiBody = kpiBody)
    if(loading) LoadingScreen()
    else{
        if(success){
            if(directorate.isNotEmpty()){
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
                AnimatedVisibility(visible = toggleReactive /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "مديرية\n",
                                items = directorate.map { it.hospitalName?:EMPTY_STRING })
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "امانة\n",
                                items = specialized.map { it.hospitalName?:EMPTY_STRING })
                            LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){
                                item{
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المجمع\n",
                                        items = directorate.map { (it.totalCollected?:0).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المنصرف\n",
                                        items = directorate.map { (it.totalIssued?:0).toString() })
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = "فيروس بى",
                                        firstSubHeader = "العدد",
                                        secondSubHeader = "النسبة <${reasons[3].maximumPercent}",
                                        firstList = directorate.map { (it.totalHbv?:0).toString() },
                                        secondList = directorate.map {
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
                                        firstList = directorate.map { (it.totalHcv?:0).toString() },
                                        secondList = directorate.map {
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
                                        firstList = directorate.map { (it.totalHiv?:0).toString() },
                                        secondList = directorate.map {
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
                                        firstList = directorate.map { (it.totalSyphilis?:0).toString() },
                                        secondList = directorate.map {
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
                    .background(ORANGE),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleIncineratedIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleIncinerated=!toggleIncinerated }
                    Label("الاعدام/لم يتم/منتهى الصلاحية",
                        fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleIncinerated)"إخفاء" else "إظهار", fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AnimatedVisibility(visible = toggleIncinerated /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "مديرية\n",
                                items = directorate.map { it.hospitalName?: EMPTY_STRING })
                            LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){
                                item{
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المجمع\n",
                                        items = directorate.map { (it.totalCollected?:0).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "المنصرف\n",
                                        items = directorate.map { (it.totalIssued?:0).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "إعدام\nإيجابى",
                                        items = directorate.map { ((it.totalHbv?:0)+(it.totalHcv?:0)+(it.totalHiv?:0)+(it.totalSyphilis?:0)).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "منتهى\n الصلاحية",
                                        items = directorate.map { (it.totalExpired?:0).toString() })
                                    TableColumn(
                                        modifier=Modifier,
                                        header = "غير\nمكتمل",
                                        items = directorate.map { (it.totalIncomplete?:0).toString() })

                                }
                            }

                        }
                    }
                }
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
                AnimatedVisibility(visible = toggleAchievement /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier=Modifier.fillMaxWidth().weight(1f),
                                header = "مديرية\n",
                                items = directorate.map { it.hospitalName?:EMPTY_STRING })
                            LazyRow(modifier=Modifier.fillMaxWidth().weight(3f)){
                                item{
                                TableColumn(
                                    modifier=Modifier,
                                    header = "المجمع\n",
                                    items = directorate.map { (it.totalCollected?:0).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "المنصرف\n",
                                    items = directorate.map { (it.totalIssued?:0).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "العجز\n",
                                    items = directorate.map {
                                        val totalCollected=it.totalCollected?:1
                                        val totalIssued=it.totalIssued?:0
                                        val totalDifference=if((totalIssued-totalCollected)>0) totalIssued-totalCollected else 0
                                        (totalDifference).toString() })
                                TableColumn(
                                    modifier=Modifier,
                                    header = "نسبة\n العجز",
                                    items = directorate.map {
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
                                    items = directorate.map {
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
                                    items = directorate.map {
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
    var range by remember { mutableStateOf(EMPTY_STRING) }
    range=when(Pair(monthFrom.value,monthTo.value)){

        Pair(if(firstQ) monthFrom.value else "1","3")->"عن الربع الاول"
        Pair(if(secondQ) monthFrom.value else "4","6")->"عن الربع الثانى"
        Pair(if(thirdQ) monthFrom.value else "7","9")->"عن الربع الثالث"
        Pair(if(fourthQ) monthFrom.value else "10","12")->"عن الربع الرابع"
        Pair(if(firstSim) monthFrom.value else "1",if(firstSim) monthTo.value else "6")->"عن النصف الاول"
        Pair(if(secondSim) monthFrom.value else "7",if(secondSim) monthTo.value else "12")->"عن النصف الثانى"
        else->{EMPTY_STRING}
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
                        kpiBody.value?.let {controller.getComparativeCharts(it)}
                        showDialog.value=false
                    }
                }
            }
        }
    }
}

@Composable
fun FinalComparativeBarChart(modifier: Modifier,//this controls the chart modifier
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

fun prepareComparativeGroupedData(data: List<CityBloodBankKpi>): Pair<List<String>, List<IBarDataSet>> {

    val xLabels = data.map { it.cityName ?: "Unknown" } // e.g., Cairo, Giza, etc.
    // Initialize entries for each dataset
    val totalCollectedEntries = mutableListOf<BarEntry>()
    val totalIssuedEntries = mutableListOf<BarEntry>()
    val totalHbvEntries = mutableListOf<BarEntry>()
    val totalHcvEntries = mutableListOf<BarEntry>()
    val totalHivEntries = mutableListOf<BarEntry>()
    val totalSyphilisEntries = mutableListOf<BarEntry>()
    data.forEachIndexed { index, entry ->
        totalCollectedEntries.add(BarEntry(index.toFloat(), (entry.totalCollected ?: 0).toFloat()))
        totalIssuedEntries.add(BarEntry(index.toFloat(), (entry.totalIssued ?: 0).toFloat()))
        totalHbvEntries.add(BarEntry(index.toFloat(), (entry.totalHbv ?: 0).toFloat()))
        totalHcvEntries.add(BarEntry(index.toFloat(), (entry.totalHcv ?: 0).toFloat()))
        totalHivEntries.add(BarEntry(index.toFloat(), (entry.totalHiv ?: 0).toFloat()))
        totalSyphilisEntries.add(BarEntry(index.toFloat(), (entry.totalSyphilis ?: 0).toFloat()))
    }
    val datasets = listOf(
        BarDataSet(totalCollectedEntries, "مجمع"),
        BarDataSet(totalIssuedEntries, "منصرف"),
    ).mapIndexed { index, dataSet ->
        dataSet.apply {
            color = baseColors[index % baseColors.size].toArgb()
            valueTextSize = 12f
        }
    }
    return Pair(xLabels, datasets)
}
/*
val test= listOf(
    CityBloodBankKpi(cityId=1,cityName = "Cairo", totalCollected = 100, totalIssued = 90),
    CityBloodBankKpi(cityId=2,cityName = "Giza", totalCollected = 120, totalIssued = 100),
    CityBloodBankKpi(cityId=3,cityName = "Qaliubia", totalCollected = 110, totalIssued = 80),
    CityBloodBankKpi(cityId=4,cityName = "Alexandria", totalCollected = 90, totalIssued = 70),
    CityBloodBankKpi(cityId=5,cityName = "Ismailia", totalCollected = 75, totalIssued = 70),
    CityBloodBankKpi(cityId=6,cityName = "Aswan", totalCollected = 40, totalIssued = 30),
    CityBloodBankKpi(cityId=7,cityName = "Asiut", totalCollected = 65, totalIssued = 60),
    CityBloodBankKpi(cityId=8,cityName = "Luxor", totalCollected = 35, totalIssued = 30),
    CityBloodBankKpi(cityId=9,cityName = "Red Sea", totalCollected = 25, totalIssued = 20),
    CityBloodBankKpi(cityId=10,cityName = "Buhaira", totalCollected = 70, totalIssued = 65),
    CityBloodBankKpi(cityId=11,cityName = "Bani Suif", totalCollected = 60, totalIssued = 55),
    CityBloodBankKpi(cityId=12,cityName = "Port Said", totalCollected = 50, totalIssued = 65),
    CityBloodBankKpi(cityId=13,cityName = "South Sinai", totalCollected = 30, totalIssued = 35),
    CityBloodBankKpi(cityId=14,cityName = "Dakahlia", totalCollected = 65, totalIssued = 60),
    CityBloodBankKpi(cityId=15,cityName = "Damitta", totalCollected = 100, totalIssued = 90),
    CityBloodBankKpi(cityId=16,cityName = "Sohag", totalCollected = 35, totalIssued = 40),
    CityBloodBankKpi(cityId=17,cityName = "Suez", totalCollected = 100, totalIssued = 90),
    CityBloodBankKpi(cityId=18,cityName = "Sharqia", totalCollected = 95, totalIssued = 90),
    CityBloodBankKpi(cityId=19,cityName = "North Sinai", totalCollected = 80, totalIssued = 70),
    CityBloodBankKpi(cityId=20,cityName = "Gharbia", totalCollected = 130, totalIssued = 120),
    CityBloodBankKpi(cityId=21,cityName = "Fayuom", totalCollected = 90, totalIssued = 80),
    CityBloodBankKpi(cityId=22,cityName = "Qena", totalCollected = 40, totalIssued = 35),
    CityBloodBankKpi(cityId=23,cityName = "Kafr El-sheikh", totalCollected = 35, totalIssued = 30),
    CityBloodBankKpi(cityId=24,cityName = "Matrouh", totalCollected = 35, totalIssued = 30),
    CityBloodBankKpi(cityId=25,cityName = "Menufia", totalCollected = 40, totalIssued = 35),
    CityBloodBankKpi(cityId=26,cityName = "Menia", totalCollected = 30, totalIssued = 25),
    CityBloodBankKpi(cityId=27,cityName = "New Valley", totalCollected = 15, totalIssued = 10),
    )
 */

/*
  VerticalSpacer()
Row(modifier=Modifier.fillMaxWidth()){
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.3f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("م")
        Label(EMPTY_STRING )
        Label(EMPTY_STRING )
        HorizontalDivider()
        items.forEachIndexed { index, _ ->
            Label("${index+1}")
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.6f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("المدينة")
        Label(EMPTY_STRING)
        Label(EMPTY_STRING)
        HorizontalDivider()
        items.forEachIndexed { _, cityBloodBankKpi ->
            Label(cityBloodBankKpi.cityName?:EMPTY_STRING)
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.6f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("إجمالى", fontSize = 12)
        Label("المجمع", fontSize = 12)
        Label(EMPTY_STRING, fontSize = 10)
        HorizontalDivider()
        items.forEachIndexed { _, cityBloodBankKpi ->
            Label("${cityBloodBankKpi.totalCollected}")
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.6f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("إجمالى", fontSize = 12)
        Label("المنصرف", fontSize = 12)
        Label(EMPTY_STRING, fontSize = 10)

        HorizontalDivider()
        items.forEachIndexed { _, cityBloodBankKpi ->
            Label("${cityBloodBankKpi.totalIssued}")
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(1f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val reasonPercent=reasons.find { it.id==2 }?.maximumPercent?:0

        Label("منتهى", fontSize = 10)
        Label("الصلاحية", fontSize = 10)
        HorizontalDivider()
        Row{
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray),
                contentAlignment = Alignment.Center){
                Label("عدد الوحدات")
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.light_green))
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray),
                contentAlignment = Alignment.Center){Label("$reasonPercent>")}
        }
        //HorizontalDivider()
        items.forEachIndexed { _, cityBloodBankKpi ->
            val totalCollected=cityBloodBankKpi.totalCollected?:1
            val floatTotalCollected=(if(totalCollected<1) 1 else totalCollected ).toFloat()
            val totalReason=cityBloodBankKpi.totalExpired?:0
            val totalReasonFloat=totalReason.toFloat()
            val division=totalReasonFloat/floatTotalCollected
            val percent=roundDownPercentToTenth(division*100)

            Row{
                Column(modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.dp, color = Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Label("${cityBloodBankKpi.totalExpired}")
                }
                Column(modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.dp, color = Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val color=if(percent>(reasonPercent).toFloat()) colorResource(R.color.pale_pink) else colorResource(R.color.white)
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .background(color = color),
                        contentAlignment = Alignment.Center){
                        Label("$percent")
                    }
                }
            }
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("اعدام", fontSize = 10)
        Label("فيروسات", fontSize = 10)
        HorizontalDivider()
        Row{
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray),
                contentAlignment = Alignment.Center){
                Label("عدد الوحدات")
            }
        }
        items.forEachIndexed { _, cityBloodBankKpi ->
            val totalHbv=cityBloodBankKpi.totalHbv?:0
            val totalHcv=cityBloodBankKpi.totalHcv?:0
            val totalHiv=cityBloodBankKpi.totalHiv?:0
            val totalSyphilis=cityBloodBankKpi.totalSyphilis?:0
            val totalReason=totalHbv+totalHcv+totalHiv+totalSyphilis
            Label("$totalReason")
            HorizontalDivider()
        }
    }
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val reasonPercent=reasons.find { it.id==4 }?.maximumPercent?:0
        Label("لم يتم", fontSize = 10)
        Label(EMPTY_STRING, fontSize = 12)

        Row{
            Box(modifier= Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray),
                contentAlignment = Alignment.Center){
                Label("عدد الوحدات")
            }
            Box(modifier= Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.light_green))
                .weight(1f)
                .border(width = 1.dp, color = Color.LightGray),
                contentAlignment = Alignment.Center){Label("$reasonPercent>")}
        }
        items.forEachIndexed { _, cityBloodBankKpi ->
            val totalCollected=cityBloodBankKpi.totalCollected?:1
            val notNullCollected=if(totalCollected==0) 1f else 0f+totalCollected
            val totalReason=cityBloodBankKpi.totalIncomplete?:0
            val totalReasonFloat=0f+totalReason
            val division=totalReasonFloat/notNullCollected
            val percent=roundDownPercentToTenth(division*100)

            Row{
                Column(modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.dp, color = Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Label("${cityBloodBankKpi.totalIncomplete?:0}")
                }
                Column(modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.dp, color = Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {
                    val color=if(percent>(reasonPercent).toFloat()) colorResource(R.color.pale_pink) else colorResource(R.color.white)
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .background(color = color), contentAlignment = Alignment.Center){
                        Label("$percent")
                    }
                }
            }
            HorizontalDivider()
        }
    }
}
VerticalSpacer()
Row(modifier=Modifier.fillMaxWidth()){
    //Index
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.3f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("م")
        Label(EMPTY_STRING)
        HorizontalDivider()
        items.forEachIndexed { index, _ ->
            Label("${index+1}", fontSize = 16)
            HorizontalDivider()
        }
    }
    //City
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("المدينة")
        Label(EMPTY_STRING)
        HorizontalDivider()
        items.forEachIndexed { _, cityBloodBankKpi ->
            Label(cityBloodBankKpi.cityName?:EMPTY_STRING)
            HorizontalDivider()
        }
    }
    //Total Collected
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.4f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("إجمالى", fontSize = 12)
        Label("المجمع", fontSize = 12)
        HorizontalDivider()
        items.forEach{
            val totalCollected=it.totalCollected?:1
            Label("$totalCollected", fontSize = 16)
            HorizontalDivider()
        }
    }
    //Total Issued
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.4f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("إجمالى", fontSize = 12)
        Label("المنصرف", fontSize = 12)
        HorizontalDivider()
        items.forEach{
            val totalIssued=it.totalIssued?:0
            Label("$totalIssued", fontSize = 16)
            HorizontalDivider()
        }
    }
    //Total Difference
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("العجز", fontSize = 12)
        Label(EMPTY_STRING, fontSize = 16)

        HorizontalDivider()
        items.forEach {
            val totalCollected=it.totalCollected?:1
            val totalIssued=it.totalIssued?:0
            val totalDifference=if((totalIssued-totalCollected)>0) totalIssued-totalCollected else 0

            Label("$totalDifference", fontSize = 16)

            HorizontalDivider()
        }
    }
    //Achievement Percent
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Label("نسبة", fontSize = 12)
        Label("التحقيق", fontSize = 12)
        HorizontalDivider()

        items.forEachIndexed { _, cityBloodBankKpi ->
            val totalCollected=(cityBloodBankKpi.totalCollected?:1) //649
            val totalIssued=(cityBloodBankKpi.totalIssued?:0) //290
                                    // 649+(649*(20/100))
            val notNullCol=if(totalCollected==0) 1 else totalCollected
            val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f))
            val posIssued=if(totalIssued>0) 0f+totalIssued+(totalIssued*(20f/100f)) else 1f
            Log.e("TotReq","${totalIssued>totalCollected} Tot I $totalIssued Tot C $totalCollected")
            val totalRequired=if(totalIssued>totalCollected) posIssued else collectedIncrement
            val achievement=totalCollected/totalRequired
            val achievementPercent=floor(achievement*100f)
            Label("$achievementPercent", fontSize = 16)
            HorizontalDivider()
        }
    }
    //Difference Percent
    Column(modifier= Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Label("نسبة", fontSize = 12)
        Label("العجز", fontSize = 12)
        HorizontalDivider()

        items.forEachIndexed { _, cityBloodBankKpi ->
            val totalCollected=(cityBloodBankKpi.totalCollected?:1) //649
            val totalIssued=(cityBloodBankKpi.totalIssued?:0) //290
                                        //649+(649*(20/100))
            val notNullCol=if(totalCollected==0) 1 else totalCollected
            val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f))
                                    //290+(290*(20/100))
            val posIssued=if(totalIssued>0) 0f+totalIssued+(totalIssued*(20f/100f)) else 1f
            val totalRequired =if (totalIssued > totalCollected) posIssued else collectedIncrement
                                // 649/778
            val achievement=totalCollected/totalRequired
            val achievementPercent=floor(achievement*100f)

            val differencePercent=floor(100-achievementPercent)
            Label("$differencePercent", fontSize = 16)
            HorizontalDivider()
        }
    }
}
        LazyRow {
    item{
        FinalBarChart(
            modifier = Modifier.width(1200.dp).height(300.dp),//this controls the chart modifier
            chartXLabels = chartXLabels,
            datasets = chartDatasets,
        )
    }
}

 */

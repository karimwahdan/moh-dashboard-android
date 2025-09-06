package com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
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
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.THE_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import java.time.LocalDate
import java.time.LocalTime

/*
data class GroupedBarData(
    val groupIndex: Int,
    val barEntries: List<BarEntry>,
    val groupLabels: List<String>,
    val colors: List<Color>,
    val xAxisLabels: List<String>
)
 */

val baseColors = listOf(
    Color.Red, BLUE, GREEN, Color.Cyan, Color.Magenta,
    Color.Yellow, ORANGE, Color.DarkGray, Color.LightGray,
    Color(android.graphics.Color.parseColor("#FF5722")), // Deep Orange
    Color(android.graphics.Color.parseColor("#3F51B5")), // Indigo
    Color(android.graphics.Color.parseColor("#795548")), // Brown
    Color(android.graphics.Color.parseColor("#607D8B")), // Blue Grey
)
fun prepareGroupedData(
    dailyStocks: List<DailyBloodStock>,
    keys: MutableState<List<String>>,
    sectorKeys:MutableState<List<String>>,
    hospitalTypeKeys:MutableState<List<String>>,
    bySector:MutableState<Boolean>,
    byHospitalType:MutableState<Boolean>,
    byCity:MutableState<Boolean>,
    byArea:MutableState<Boolean>,
    byHospital:MutableState<Boolean>
    //groupBy: (DailyBloodStock) -> String // e.g., { it.hospital?.city?.name ?: "Unknown" }
): Triple<List<String>, List<IBarDataSet>, List<String>> {

    // All unique blood group labels (used as X-axis labels)
    val listOfBloodGroups= listOf(
        BasicModel(1,"A+"),BasicModel(2,"A-"),
        BasicModel(3,"B+"),BasicModel(4,"B-"),
        BasicModel(5,"O+"),BasicModel(6,"O-"),
        BasicModel(7,"AB+"),BasicModel(8,"AB-"),
    ).sortedBy { it.id }
    // All unique group keys (e.g., city names)
    val mainSelectorKeys = keys.value.map{it}.distinct().sorted()

    val dataSets = mainSelectorKeys.mapIndexed { selectorIndex, selectorKey ->
        val sortedByBloodGroup=dailyStocks.sortedBy { it.bloodGroup?.id }
        val entries = listOfBloodGroups.mapIndexed { bloodGroupIndex, bloodGroup ->
            val filteredByBloodGroupName=sortedByBloodGroup.filter { it.bloodGroup?.id == bloodGroup.id}

            val filteredByKeys=filteredByBloodGroupName
                .filter {
                    val cityName=it.hospital?.city?.name
                    val areaName=it.hospital?.area?.name
                    val hospitalName=it.hospital?.name
                    if(byHospital.value && !byCity.value && !byArea.value) hospitalName == selectorKey
                    else if(byCity.value && !byHospital.value && !byArea.value) cityName == selectorKey
                    else if(byArea.value && !byHospital.value && !byCity.value) areaName == selectorKey
                    else {hospitalName !=null}
                }
            val filteredFBySector=if(bySector.value){
                filteredByKeys.filter {item->
                    val sectorName=item.hospital?.sector?.name
                    if(sectorKeys.value.isNotEmpty()) {sectorName in sectorKeys.value}
                    else{sectorName!=null}
                }
            }else{
                filteredByKeys
            }
            val filteredByType=if(byHospitalType.value){
                filteredFBySector.filter {item->
                    val typeName=item.hospital?.type?.name
                    if(hospitalTypeKeys.value.isNotEmpty()) {typeName in hospitalTypeKeys.value}
                    else{typeName!=null}
                }
            }
            else filteredFBySector
            val totalAmount = filteredByType
                .filter {
                    val cityName=it.hospital?.city?.name
                    val areaName=it.hospital?.area?.name
                    val hospitalName=it.hospital?.name
                    if(byHospital.value && !byCity.value && !byArea.value) hospitalName == selectorKey
                    else if(byCity.value && !byHospital.value && !byArea.value) cityName == selectorKey
                    else if(byArea.value && !byHospital.value && !byCity.value) areaName == selectorKey
                    else {hospitalName !=null}
                }
                .sumOf { it.amount?:0 }
            BarEntry(bloodGroupIndex.toFloat(), totalAmount.toFloat())
        }
        BarDataSet(entries, selectorKey).apply {
            color = baseColors[selectorIndex % baseColors.size].toArgb()
            valueTextSize = 12f
        }

    }

    return Triple(listOfBloodGroups.map { it.name?:"" }, dataSets, mainSelectorKeys)
}

@Composable
fun GroupedBarChart(
    modifier: Modifier = Modifier,//this controls the chart modifier
    dailyStocks: MutableState<List<DailyBloodStock>>,
    keys: MutableState<List<String>>,
    sectorKeys:MutableState<List<String>>,
    hospitalTypeKeys:MutableState<List<String>>,
    bySector:MutableState<Boolean>,
    byHospitalType:MutableState<Boolean>,
    byCity:MutableState<Boolean>,
    byArea:MutableState<Boolean>,
    byHospital:MutableState<Boolean>
    //groupBy: (DailyBloodStock) -> String // can be city, hospital, etc.
) {
    var chartXLabels by remember { mutableStateOf<List<String>>(emptyList()) }
    var chartDatasets by remember { mutableStateOf<List<IBarDataSet>>(emptyList()) }
    var chartMainKeys by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(dailyStocks.value) {
        byHospital.value=true
        val (x,ds,gk)=prepareGroupedData(
            dailyStocks= dailyStocks.value,
            keys=keys,
            sectorKeys=sectorKeys,
            hospitalTypeKeys=hospitalTypeKeys,
            bySector=bySector,
            byHospitalType=byHospitalType,
            byCity = byCity,
            byArea=byArea,
            byHospital=byHospital,)
        chartXLabels=x
        chartDatasets=ds
        chartMainKeys=gk
    }

    FinalBarChart(
        modifier=modifier,//this controls the chart modifier
        data=dailyStocks,
        xLabels=chartXLabels,
        datasets=chartDatasets,
        chartMainKeys=chartMainKeys)
    //val (xLabels, dataSets, groupKeys) = remember(dailyStocks.value) {prepareGroupedData(dailyStocks= dailyStocks.value.sortedBy { it.bloodGroup?.id },keys=keys,byCity = byCity,byArea=byArea,byHospital=byHospital,)}

}

@Composable
fun FinalBarChart(modifier: Modifier,//this controls the chart modifier
                  data: MutableState<List<DailyBloodStock>>,
                  xLabels:List<String>,
                  datasets:List<IBarDataSet>,
                  chartMainKeys:List<String>){
    if(data.value.isNotEmpty()){
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
                    xAxis.setCenterAxisLabels(false)
                    //setVisibleXRangeMaximum(datasets.size.toFloat()) // show 8 groups
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
                val datasetCount = chartMainKeys.size
                val groupSpace = 0.08f
                val barSpace = 0.02f
                val width=1f
                val barWidth = (width - groupSpace) / datasetCount - barSpace
                val separatorLineColor = GRAY.toArgb() // or any color you prefer
                val separatorLineWidth = 1f         // width in dp

                //8 for blood groups as they are always 8 A+,A-.B+,B-,O+,O-
                for (i in 1 ..8) {
                    val separatorXPosition = i * (datasetCount * (barWidth + barSpace) + groupSpace)
                    val separatorLine = LimitLine(separatorXPosition, "")
                    separatorLine.lineColor = separatorLineColor
                    separatorLine.lineWidth = separatorLineWidth
                    if(datasetCount>1){ chart.xAxis.addLimitLine(separatorLine) }
                }
                barData.barWidth = barWidth
                chart.axisLeft.axisMinimum=0f

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(xLabels)
                    setDrawLabels(true)
                    setDrawGridLines(false)
                    textSize = 12f
                    axisMaximum=8f
                }

                chart.data = barData
                chart.setFitBars(true)

                if(datasetCount>1){
                    try{
                        chart.groupBars(0f, groupSpace, barSpace)
                        //chart.xAxis.axisMaximum = 0f + chart.barData.getGroupWidth(groupSpace, barSpace) * xLabels.size
                        chart.xAxis.setCenterAxisLabels(true)

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                chart.notifyDataSetChanged()
                chart.invalidate()

            }
        )
    }
}

fun getCurrent(data: MutableState<List<DailyBloodStock>>): MutableState<List<DailyBloodStock>> {
    Log.e("getCurrent","original data size ${data.value.size}")
    val currentBlock = getCurrentEntryBlock()

    // Step 1: Try to get data for current block
    val currentData = data.value.filter { it.timeBlock == currentBlock }

    if (currentData.isNotEmpty()) return data

    // Step 2: If no data for current block, find latest available block by order: 18 > 12 > 06 > 00
    val blockOrder = listOf("18", "12", "06", "00")
    for (block in blockOrder) {
        val blockData = data.value.filter { it.timeBlock == block }
        if (blockData.isNotEmpty()) data.value=data.value.filter { it.timeBlock == block };return data
    }

    // Step 3: Fallback if no blocks exist at all
    data.value= emptyList()
    return data
}

@Composable
fun DailyStockBarChart(
    data: MutableState<List<DailyBloodStock>>,
    keys:MutableState<List<String>>,
    sectorKeys:MutableState<List<String>>,
    hospitalTypeKeys:MutableState<List<String>>,
    bySector:MutableState<Boolean>,
    byHospitalType:MutableState<Boolean>,
    byCity:MutableState<Boolean>,
    byArea:MutableState<Boolean>,
    byHospital:MutableState<Boolean>) {
    val x=data.value.map {
        if(byCity.value) it.hospital?.city
        if(byArea.value) it.hospital?.area
        if(byHospital.value) it.hospital
        else it.hospital?.city
    }.distinct().size
    var keysCount by remember { mutableIntStateOf(x) }
    var size by remember { mutableIntStateOf(500) }

    LaunchedEffect(data.value) {
        keysCount=keys.value.distinct().size
        size=if(keysCount>2) keysCount*20*8 else 500
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION){
        LazyRow {
            item {
                GroupedBarChart(
                    modifier = Modifier.widthIn(min = size.dp).height(300.dp),//this controls the chart modifier
                    dailyStocks = getCurrent(data),
                    keys=keys,
                    sectorKeys=sectorKeys,
                    hospitalTypeKeys=hospitalTypeKeys,
                    bySector=bySector,
                    byHospitalType=byHospitalType,
                    byCity=byCity,
                    byArea=byArea,
                    byHospital=byHospital
                )
            }
        }


    }
}

fun getCurrentEntryBlock(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 0 until 6 -> "00"
        in 6 until 12 -> "06"
        in 12 until 18 -> "12"
        else -> "18"
    }
}
@Composable
fun BloodStocksCharts(
    bloodStocks:List<DailyBloodStock>,
    keys:MutableState<List<String>>,
    sectorKeys:MutableState<List<String>>,
    hospitalTypeKeys:MutableState<List<String>>,
    selectedBloodStocks:MutableState<List<DailyBloodStock>>){
    val showFilterDialog        =  remember { mutableStateOf(false) }
    val byDate                  =  remember { mutableStateOf(false) }
    val selectedDate            =  remember { mutableStateOf("") }

    //val byBloodGroup            =  remember { mutableStateOf(false) }
    val byUnitType              =  remember { mutableStateOf(false) }
    val byCity                  =  remember { mutableStateOf(false) }
    val byArea                  =  remember { mutableStateOf(false) }
    val bySector                =  remember { mutableStateOf(false) }
    val byHospitalType          =  remember { mutableStateOf(false) }
    val byHospital              =  remember { mutableStateOf(false) }

    val selectedBloodGroup      =  remember { mutableStateOf<BasicModel?>(null) }
    var bloodGroups             by remember { mutableStateOf<List<BasicModel?>>(emptyList()) }
    var selectedBloodGroups     by remember { mutableStateOf<List<BasicModel>>(emptyList()) }

    val selectedUnitType        =  remember { mutableStateOf<BasicModel?>(null) }
    var unitTypes               by remember { mutableStateOf<List<BasicModel?>>(emptyList()) }
    var selectedUnitTypes       by remember { mutableStateOf<List<BasicModel>>(emptyList()) }

    val selectedCity            =  remember { mutableStateOf<City?>(null) }
    var cities                  by remember { mutableStateOf<List<City?>>(emptyList()) }
    var selectedCities          by remember { mutableStateOf<List<City>>(emptyList()) }

    val selectedArea            =  remember { mutableStateOf<Area?>(null) }
    var areas                   by remember { mutableStateOf<List<Area?>>(emptyList()) }
    var selectedAreas           by remember { mutableStateOf<List<Area>>(emptyList()) }

    val selectedSector          =  remember { mutableStateOf<Sector?>(null) }
    var sectors                 by remember { mutableStateOf<List<Sector?>>(emptyList()) }
    var selectedSectors         by remember { mutableStateOf<List<Sector>>(emptyList()) }

    val selectedHospitalType    =  remember { mutableStateOf<HospitalType?>(null) }
    var hospitalTypes           by remember { mutableStateOf<List<HospitalType?>>(emptyList()) }
    var selectedHospitalTypes   by remember { mutableStateOf<List<HospitalType>>(emptyList()) }

    val selectedHospital        =  remember { mutableStateOf<SimpleHospital?>(null) }
    var hospitals               by remember { mutableStateOf<List<SimpleHospital?>>(emptyList()) }
    var selectedHospitals       by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }

    var dates                   by remember { mutableStateOf<List<String>>(emptyList()) }

    //var newList by remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    var sortedByString by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {byHospital.value=true;sortedByString= THE_HOSPITAL_LABEL}

    if(showFilterDialog.value){
        LaunchedEffect(Unit) {
            bloodGroups=bloodStocks.distinctBy { it.bloodGroup }.map { it.bloodGroup }
            unitTypes=bloodStocks.distinctBy { it.bloodUnitType }.map { it.bloodUnitType }
            hospitals=bloodStocks.distinctBy { it.hospital }.map { it.hospital }
            cities=hospitals.distinctBy { it?.city }.mapNotNull { it?.city }
            areas=hospitals.distinctBy { it?.area }.mapNotNull { it?.area }
            sectors=hospitals.distinctBy { it?.sector }.mapNotNull { it?.sector }
            hospitalTypes=hospitals.distinctBy { it?.type }.mapNotNull { it?.type }
            dates=bloodStocks.distinctBy { it.entryDate }.map { (it.entryDate?:"") }
        }
        LaunchedEffect(selectedCity.value) {
            if(selectedCity.value!=null){
                val newItems= mutableListOf<String>()
                selectedCity.value?.let{ newItems.add(it.name?:"") }
                newItems.addAll(selectedCities.filter { current->current!=selectedCity.value }.mapNotNull{it.name})
                keys.value=newItems

                val newModels= mutableListOf<City>()
                selectedCity.value?.let{newModels.add(it)}
                newModels.addAll(selectedCities.filter { current->current!=selectedCity.value })
                selectedCities=newModels

                selectedCity.value=null


            }

        }
        LaunchedEffect(selectedArea.value) {
            if(selectedArea.value!=null){
                val newItems= mutableListOf<String>()
                selectedArea.value?.let{ newItems.add(it.name?:"") }
                newItems.addAll(selectedAreas.filter { current->current!=selectedArea.value }
                    .mapNotNull{it.name})
                keys.value=newItems

                val newModels= mutableListOf<Area>()
                selectedArea.value?.let{newModels.add(it)}
                newModels.addAll(selectedAreas.filter { current->current!=selectedArea.value })
                selectedAreas=newModels

                selectedArea.value=null
            }
        }
        LaunchedEffect(selectedHospital.value) {
            if(selectedHospital.value!=null){
                val newItems= mutableListOf<String>()
                selectedHospital.value?.let{ newItems.add(it.name?:"") }
                newItems.addAll(selectedHospitals.filter { current->current!=selectedHospital.value }
                    .mapNotNull{it.name})
                keys.value=newItems

                val newModels= mutableListOf<SimpleHospital>()
                selectedHospital.value?.let{newModels.add(it)}
                newModels.addAll(selectedHospitals.filter { current->current!=selectedHospital.value })
                selectedHospitals=newModels

                selectedHospital.value=null

            }
        }
        LaunchedEffect(selectedBloodGroup.value) {
            if(selectedBloodGroup.value!=null){
                val newItems= mutableListOf<BasicModel>()
                selectedBloodGroup.value?.let{ newItems.add(it) }
                newItems.addAll(selectedBloodGroups.filter { c->c!=selectedBloodGroup.value })
                selectedBloodGroups=newItems
                selectedBloodGroup.value=null
            }

        }
        LaunchedEffect(selectedUnitType.value) {
            if(selectedUnitType.value!=null){
                val newItems= mutableListOf<BasicModel>()
                selectedUnitType.value?.let{ newItems.add(it) }
                newItems.addAll(selectedUnitTypes.filter { c->c!=selectedUnitType.value })
                selectedUnitTypes=newItems
                selectedUnitType.value=null
            }

        }
        LaunchedEffect(selectedSector.value) {
            if(selectedSector.value!=null){
                val newItems= mutableListOf<String>()
                selectedSector.value?.let{ newItems.add(it.name) }
                newItems.addAll(selectedSectors.filter { c->c!=selectedSector.value }.map { it.name })
                sectorKeys.value=newItems

                val newModels= mutableListOf<Sector>()
                selectedSector.value?.let{newModels.add(it)}
                newModels.addAll(selectedSectors.filter { current->current!=selectedSector.value })
                selectedSectors=newModels

                selectedSector.value=null
            }
        }
        LaunchedEffect(selectedHospitalType.value) {
            if(selectedUnitType.value!=null){
                val newItems= mutableListOf<String>()
                selectedHospitalType.value?.let{ newItems.add(it.name) }
                newItems.addAll(selectedHospitalTypes.filter { c->c!=selectedHospitalType.value }.map { it.name })
                hospitalTypeKeys.value=newItems

                val newModels= mutableListOf<HospitalType>()
                selectedHospitalType.value?.let{newModels.add(it)}
                newModels.addAll(selectedHospitalTypes.filter { current->current!=selectedHospitalType.value })
                selectedHospitalTypes=newModels

                selectedHospitalType.value=null
            }
        }
        LaunchedEffect(byArea.value) {
            if(byCity.value) {
                byArea.value=false
                byHospital.value=false
                sortedByString= AREA_LABEL
            }
        }
        LaunchedEffect(byCity.value) {
            if(byCity.value) {
                byArea.value=false
                byHospital.value=false
                sortedByString= DIRECTORATE_LABEL
            }
        }
        LaunchedEffect(byHospital.value) {
            if(byHospital.value){
                byCity.value=false
                byArea.value=false
                sortedByString= THE_HOSPITAL_LABEL
            }
        }

        Dialog(onDismissRequest = {showFilterDialog.value=false}) {
            ColumnContainer {
                Column {
                    VerticalSpacer(10)
                    LazyColumn {
                        item{
                            //val bloodGroupsChucked=selectedBloodGroups.chunked(3)
                            val unitTypesChucked=selectedUnitTypes.chunked(3)
                            val sectorsChucked=selectedSectors.chunked(3)
                            val hospitalTypesChucked=selectedHospitalTypes.chunked(3)
                            val citiesChucked=selectedCities.chunked(3)
                            val areasChucked=selectedAreas.chunked(3)
                            val hospitalsChucked=selectedHospitals.chunked(3)

                            if(byUnitType.value && selectedUnitTypes.isNotEmpty()){
                                Span("Selected Unit Types", backgroundColor = BLUE, color = WHITE)
                                unitTypesChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name?:"")
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedUnitTypes=selectedUnitTypes.filter { it!=item }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                            if(bySector.value && selectedSectors.isNotEmpty()){
                                Span("Selected Sectors", backgroundColor = BLUE, color = WHITE)
                                sectorsChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name)
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedSectors=selectedSectors.filter { it!=item }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                            if(byHospitalType.value && selectedHospitalTypes.isNotEmpty()){
                                Span("Selected Hospital Types", backgroundColor = BLUE, color = WHITE)
                                hospitalTypesChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name)
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedHospitalTypes=selectedHospitalTypes.filter { it!=item }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                            if(byCity.value && selectedCities.isNotEmpty()){
                                Span("Selected Cities", backgroundColor = BLUE, color = WHITE)
                                citiesChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name?:"")
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedCities=selectedCities.filter { it!=item }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                            if(byArea.value && selectedAreas.isNotEmpty()){
                                Span("Selected Areas", backgroundColor = BLUE, color = WHITE)
                                areasChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name?:"")
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedAreas=selectedAreas.filter { it!=item }
                                                        }

                                                    }
                                                }

                                            }

                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                            if(byHospital.value && selectedHospitals.isNotEmpty()){
                                Span("Selected Hospitals", backgroundColor = BLUE, color = WHITE)
                                hospitalsChucked.forEach { subList->
                                    Row(modifier=Modifier.fillMaxWidth()){
                                        subList.forEach { item->
                                            Box(modifier= Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(5.dp)){
                                                ColumnContainer {
                                                    Row(verticalAlignment = Alignment.CenterVertically){
                                                        Label(item.name?:"")
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedHospitals=selectedHospitals.filter { it!=item }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                    if(byDate.value){
                        Box(modifier=Modifier.fillMaxWidth()){
                            ComboBox(selectedItem = selectedDate,
                                loadedItems = dates,
                                selectedContent = { CustomInput(selectedDate.value) }) {
                                Label(it)
                            }
                        }
                    }
                    if(byUnitType.value){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Box(modifier= Modifier
                                .fillMaxWidth()){
                                ComboBox(selectedItem = selectedUnitType,
                                    loadedItems = unitTypes,
                                    selectedContent = { CustomInput(selectedUnitType.value?.name?:"Select Unit Type") }) {
                                    Label(it?.name?:"")
                                }
                            }
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        CustomCheckbox("By Sector",bySector)
                        CustomCheckbox("By Hospital Type",byHospitalType)
                    }
                    Row(modifier=Modifier.fillMaxWidth()){
                        if(bySector.value){
                            Row(modifier= Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)){
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f)){
                                    ComboBox(selectedItem = selectedSector,
                                        loadedItems = sectors,
                                        selectedContent = { CustomInput(selectedSector.value?.name?:"Select Sector") }) {
                                        Label(it?.name?:"")
                                    }
                                }
                            }
                        }
                        if(byHospitalType.value){
                            Row(modifier= Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)){
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f)){
                                    ComboBox(selectedItem = selectedHospitalType,
                                        loadedItems = hospitalTypes,
                                        selectedContent = { CustomInput(selectedHospitalType.value?.name?:"Select Hospital Type") }) {
                                        Label(it?.name?:"")
                                    }
                                }
                            }
                        }
                    }

                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        CustomCheckbox("By City",byCity)
                        CustomCheckbox("By Area",byArea)
                    }
                    Row(modifier=Modifier.fillMaxWidth()){
                        if(byCity.value){
                            Row(modifier= Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 5.dp)){
                                Box(modifier= Modifier
                                    .fillMaxWidth()){
                                    ComboBox(selectedItem = selectedCity,
                                        loadedItems = cities,
                                        selectedContent = { CustomInput(selectedCity.value?.name?:"Select City") }) {
                                        Label(it?.name?:"")
                                    }
                                }

                            }
                        }
                        if(byArea.value && !byCity.value){
                            Row(modifier= Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 5.dp)){
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f)){
                                    ComboBox(selectedItem = selectedArea,
                                        loadedItems = areas,
                                        selectedContent = { CustomInput(selectedArea.value?.name?:"Select Area") }) {
                                        Label(it?.name?:"")
                                    }
                                }

                            }
                        }
                    }

                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomCheckbox("By Blood Bank",byHospital)
                    }
                    if(byHospital.value && !byCity.value && !byArea.value){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Box(modifier= Modifier
                                .fillMaxWidth()
                                .weight(1f)){
                                ComboBox(selectedItem = selectedHospital,
                                    loadedItems = hospitals,
                                    selectedContent = { CustomInput(selectedHospital.value?.name?:"Select Hospital") }) {
                                    Label(it?.name?:"")
                                }
                            }

                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomButton(label = "Filter") {
                            if(byDate.value){
                                selectedBloodStocks.value=bloodStocks.filter { it.entryDate == selectedDate.value }
                                //newList=bloodStocks.filter { it.entryDate == selectedDate.value }
                            }
                            else{
                                val today = LocalDate.now().toString()
                                selectedBloodStocks.value=bloodStocks.filter { (it.entryDate?:"").startsWith(today) }
                                //newList=bloodStocks.filter { (it.entryDate?:"").startsWith(today) }

                            }

                            /*
                            if(byBloodGroup.value && selectedBloodGroups.isNotEmpty()){
                                selectedBloodStocks.value=bloodStocks.filter { it.bloodGroup in selectedBloodGroups }
                                //newList=bloodStocks.filter { it.bloodGroup in selectedBloodGroups }
                            }
                             */
                            if(byUnitType.value && selectedUnitTypes.isNotEmpty()){
                                selectedBloodStocks.value=bloodStocks.filter { it.bloodUnitType in selectedUnitTypes }
                                //newList=bloodStocks.filter { it.bloodUnitType in selectedUnitTypes }
                            }
                            if(bySector.value){sectorKeys.value=selectedSectors.map{ it.name }}
                            if(byHospitalType.value){hospitalTypeKeys.value=selectedHospitalTypes.map {it.name  }}
                            if(byCity.value && !byArea.value && !byHospital.value && selectedCities.isNotEmpty()){
                                //selectedBloodStocks.value=bloodStocks.filter { it.hospital != null && it.hospital.city in selectedCities }
                                keys.value=selectedCities.mapNotNull { it.name }
                                //newList=bloodStocks.filter { it.hospital != null && it.hospital.city in selectedCities }
                            }
                            if(byArea.value && !byCity.value && !byHospital.value && selectedAreas.isNotEmpty()){
                                //selectedBloodStocks.value=bloodStocks.filter { it.hospital != null && it.hospital.area in selectedAreas }
                                keys.value=selectedAreas.mapNotNull { it.name }
                                //newList=bloodStocks.filter { it.hospital != null && it.hospital.area in selectedAreas }

                            }
                            if(byHospital.value && !byArea.value && !byCity.value && selectedHospitals.isNotEmpty()){
                                //selectedBloodStocks.value=bloodStocks.filter { it.hospital != null && it.hospital in selectedHospitals }
                                keys.value=selectedHospitals.mapNotNull { it.name }
                                //newList=bloodStocks.filter { it.hospital != null && it.hospital in selectedHospitals }
                            }

                            showFilterDialog.value=false
                        }
                    }

                }
            }
        }
    }

    VerticalSpacer()
    if(!showFilterDialog.value && 1>2){
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Label(text="اجمالى وحدات الدم حسب $sortedByString",
                fontSize = 16, fontWeight = FontWeight.Bold,
                maximumLines = 3,
                color = colorResource(R.color.teal_700)
            )
            IconButton(R.drawable.ic_filter_blue) {showFilterDialog.value=!showFilterDialog.value }
        }
        selectedBloodStocks.apply {
            if(selectedBloodStocks.value.isNotEmpty()){

                DailyStockBarChart(
                    data=selectedBloodStocks,
                    keys=keys,
                    sectorKeys=sectorKeys,
                    hospitalTypeKeys=hospitalTypeKeys,
                    bySector=bySector,
                    byHospitalType=byHospitalType,
                    byCity = byCity,
                    byArea=byArea,
                    byHospital=byHospital,
                    //byDate = byDate.value,
                    //date = /* if(selectedDate.value.trim()!="") selectedDate.value else */ null
                )

            }
            else{Label("No Data")}
        }

    }
}


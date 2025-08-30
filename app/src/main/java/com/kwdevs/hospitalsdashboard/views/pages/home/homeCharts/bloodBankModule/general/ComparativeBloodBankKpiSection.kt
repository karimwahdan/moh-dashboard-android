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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityBloodBankGeneralKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityComparativeBloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.ComparativeBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.views.assets.ACHIEVEMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COMPLETE_BLOOD_KPI_COMPARISON_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CURATIVE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEFICIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DualTableColumn
import com.kwdevs.hospitalsdashboard.views.assets.EDUCATIONAL_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.EXPIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_FIRST_HALF_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_FIRST_QUARTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_FOURTH_QUARTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_SECOND_HALF_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_SECOND_QUARTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOR_THIRD_QUARTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HBV_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HCV_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HIDE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HIV_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCOMPLETE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INSURANCE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MONTH_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PERCENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PERCENT_LESS_THAN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QuadrableTableColumn
import com.kwdevs.hospitalsdashboard.views.assets.REACTIVE_INCINERATED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPECIALIZED_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SYPHILIS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TARGET_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TableColumn
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.YEAR_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ComparativeBloodBankKpiSection(controller:HomeController){
    //val context                 = LocalContext.current
    val kpiBody                 =  remember { mutableStateOf<KpiFilterBody?>(null) }
    val state                   by controller.comparativeKpiState.observeAsState()
    var comparatives            by remember { mutableStateOf<List<CityComparativeBloodBankKpi>>(emptyList()) }
    var directorate             by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }
    var insurance               by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }
    var educational             by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }
    var specialized             by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }
    var nbts                    by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }
    var curative                by remember { mutableStateOf<List<CityBloodBankGeneralKpi>>(emptyList())   }

    var reasons                 by remember { mutableStateOf<List<IncinerationReason>>(emptyList()) }
    val showDialog              =  remember { mutableStateOf(false) }


//toggleSyphilis

    var toggleAchievement       by remember { mutableStateOf(false) }
    val toggleAchievementIcon   =  if(toggleAchievement)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    var loading                 by remember { mutableStateOf(false) }
    var fail                    by remember { mutableStateOf(false) }
    var success                 by remember { mutableStateOf(false) }

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
                comparatives=data?.sectors?: emptyList()
                directorate=data?.directorate?: emptyList()
                insurance=data?.insurance?: emptyList()
                curative=data?.curative?: emptyList()
                educational=data?.educational?: emptyList()
                nbts=data?.nbts?: emptyList()
                specialized=data?.specialized?: emptyList()

                reasons=data?.reasons?: emptyList()

            }

        }
        else->{}
    }

    Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        IconButton(R.drawable.ic_filter_blue) { showDialog.value=true }
        Label(COMPLETE_BLOOD_KPI_COMPARISON_LABEL,
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
            if(comparatives.isNotEmpty()){
                CollectionTable(comparatives)
                IssuedTable(comparatives)
                ReactiveTable(comparatives)
                HbvTable(comparatives,reasons)
                HcvTable(comparatives,reasons)
                HivTable(comparatives,reasons)
                SyphilisTable(comparatives,reasons)
                IncompleteTable(comparatives,reasons)
                ExpiredTable(comparatives,reasons,1)
                AchievementTable(comparatives)

                VerticalSpacer()
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(ORANGE),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(icon=toggleAchievementIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggleAchievement=!toggleAchievement }
                    Label(ACHIEVEMENT_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
                    Label(if(toggleAchievement)HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
                }
                AnimatedVisibility(visible = toggleAchievement /**/) {
                    Column{
                        Row(modifier=Modifier.fillMaxWidth()){
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CITY_LABEL\n",
                                items = comparatives.map { it.cityName?:EMPTY_STRING })
                            LazyRow(modifier= Modifier
                                .fillMaxWidth()
                                .weight(3f)){
                                item{
                                    DualTableColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        header = CURATIVE_SECTOR_LABEL,
                                        firstSubHeader = NUMBER_LABEL,
                                        secondSubHeader = PERCENT_LABEL,
                                        firstList = comparatives.map{((it.totalIssuedCurative?:0)-(it.totalCollectedCurative?:0)).toString()},
                                        secondList = comparatives.map{
                                            val issued= 0f + (it.totalIssuedCurative?: 0)
                                            val collected=0f+(it.totalCollectedCurative?:1)
                                            val diff=issued/collected
                                            val percent=diff*100f
                                            (percent).toString()}
                                    )
                                TableColumn(
                                    modifier=Modifier,
                                    header = "$PERCENT_LABEL\n $DEFICIT_LABEL",
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
                                    header = "$TARGET_LABEL\n",
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
                                    header = "$PERCENT_LABEL\n $ACHIEVEMENT_LABEL",
                                    items = directorate.map {
                                        val totalCollected=(it.totalCollected?:1) //649
                                        val totalIssued=(it.totalIssued?:0) //290
                                        // 649+(649*(20/100))
                                        val notNullCol=if(totalCollected==0) 1 else totalCollected
                                        val collectedIncrement=0f+notNullCol+(notNullCol*(20f/100f))
                                        val totalRequired=if(totalIssued<totalCollected) 100f else collectedIncrement
                                        val achievement=totalCollected/totalRequired
                                        val achievementPercent=floor(achievement*100f)
                                        achievementPercent.toString()})
                                }
                            }
                        }
                    }
                }
            }
        }
        if(fail) FailScreen()
    }
}

@Composable
private fun AchievementTable(comparatives: List<CityComparativeBloodBankKpi>){
    var toggle       by remember { mutableStateOf(false) }
    val toggleIcon   =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(ACHIEVEMENT_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle)HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })
                    LazyRow(modifier= Modifier.fillMaxWidth().weight(3f)){
                        item{
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedDirectorate?:0
                                    val totalCollected=it.totalCollectedDirectorate?:0
                                    val diff=if(totalIssued>totalCollected)totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                                            },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedDirectorate?: 0)
                                    val collected=0f+(it.totalCollectedDirectorate?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedDirectorate?:0) //90
                                    val totalIssued=(it.totalIssuedDirectorate?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedDirectorate?:0) //649
                                    val totalIssued=(it.totalIssuedDirectorate?:0) //290
                                    val posCollected=if(totalCollected < 1)1 else totalCollected

                                    // 649+(649*(20/100))
                                    val achievement=if(totalIssued<totalCollected) 0f+1 else 0f+(totalIssued/posCollected)
                                    val achievementPercent=floor(achievement*100f)
                                    achievementPercent.toString()}
                            )
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedSpecialized?:0
                                    val totalCollected=it.totalCollectedSpecialized?:0
                                    val diff=if(totalIssued>totalCollected) totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                                            },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedSpecialized?: 0)
                                    val collected=0f+(it.totalCollectedSpecialized?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedSpecialized?:0) //90
                                    val totalIssued=(it.totalIssuedSpecialized?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedSpecialized?:0) //649
                                    val posCollected=if(totalCollected < 1)1 else totalCollected
                                    val totalIssued=(it.totalIssuedSpecialized?:0) //290

                                    // 649+(649*(20/100))
                                    val achievement=if(totalIssued<totalCollected) 0f+1 else 0f+(totalIssued/posCollected)
                                    val achievementPercent=floor(achievement*100f)
                                    achievementPercent.toString()}
                            )
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedEducational?:0
                                    val totalCollected=it.totalCollectedEducational?:0
                                    val diff=if(totalIssued>totalCollected)totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                                            },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedEducational?: 0)
                                    val collected=0f+(it.totalCollectedEducational?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedEducational?:0) //90
                                    val totalIssued=(it.totalIssuedEducational?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedEducational?:0) //649
                                    val posCollected=if(totalCollected < 1)1 else totalCollected
                                    val totalIssued=(it.totalIssuedEducational?:0) //290

                                    // 649+(649*(20/100))
                                    val achievement=if(totalIssued<totalCollected) 0f+1 else 0f+(totalIssued/posCollected)
                                    val achievementPercent=floor(achievement*100f)
                                    achievementPercent.toString()}
                            )
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedNBTS?:0
                                    val totalCollected=it.totalCollectedNBTS?:0
                                    val diff=if(totalIssued>totalCollected)totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedNBTS?: 0)
                                    val collected=0f+(it.totalCollectedNBTS?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedNBTS?:0) //90
                                    val totalIssued=(it.totalIssuedNBTS?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedNBTS?:0) //649
                                    val posCollected=if(totalCollected < 1)1 else totalCollected
                                    val totalIssued=(it.totalIssuedNBTS?:0) //290

                                    // 649+(649*(20/100))
                                    val achievement=if(totalIssued<totalCollected) 0f+1 else 0f+(totalIssued/posCollected)
                                    val achievementPercent=floor(achievement*100f)
                                    achievementPercent.toString()}
                            )
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedInsurance?:0
                                    val totalCollected=it.totalCollectedInsurance?:0
                                    val diff=if(totalIssued>totalCollected)totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedInsurance?: 0)
                                    val collected=0f+(it.totalCollectedInsurance?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedInsurance?:0) //90
                                    val totalIssued=(it.totalIssuedInsurance?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedInsurance?:0) //649
                                    val posCollected=if(totalCollected < 1)1 else totalCollected
                                    val totalIssued=(it.totalIssuedInsurance?:0) //290

                                    // 649+(649*(20/100))
                                    val achievement=if(totalIssued<totalCollected) 0f+1 else 0f+(totalIssued/posCollected)
                                    val achievementPercent=floor(achievement*100f)
                                    achievementPercent.toString()}
                            )
                            QuadrableTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                thirdSubHeader = DEFICIT_LABEL,
                                fourthSubHeader=ACHIEVEMENT_LABEL,
                                firstList = comparatives.map{
                                    val totalIssued=it.totalIssuedCurative?:0
                                    val totalCollected=it.totalCollectedCurative?:0
                                    val diff=if(totalIssued>totalCollected)totalIssued-totalCollected else totalCollected
                                    (diff).toString()
                                },
                                secondList = comparatives.map{
                                    val issued= 0f + (it.totalIssuedCurative?: 0)
                                    val collected=0f+(it.totalCollectedCurative?:1)
                                    val zeroValues=(issued==0f && collected==0f)
                                    val issuedIsMore=issued <=collected
                                    val diff=if(zeroValues) 0f
                                    else if(!zeroValues && issuedIsMore)  1f
                                    else issued/collected
                                    val percent=diff*100f
                                    (percent).toString()
                                                             },
                                thirdList = comparatives.map {
                                    val totalCollected=(it.totalCollectedCurative?:0) //90
                                    val totalIssued=(it.totalIssuedCurative?:0) //100
                                    val difference=if(totalIssued-totalCollected>0) totalIssued-totalCollected else 0
                                    difference.toString() },
                                fourthList=comparatives.map {
                                    val totalCollected=(it.totalCollectedCurative?:0) //649
                                    val posCollected=if(totalCollected < 1)1 else totalCollected
                                    val totalIssued=(it.totalIssuedCurative?:0) //290

                                    /*
                                    val issuedRequired=totalIssued+(totalIssued*(20f/100f))
                                    val collectedRequired=totalCollected+(totalCollected*(20f/100f))
                                    val totalRequired=if(totalIssued <= totalCollected  ) collectedRequired
                                    else issuedRequired
                                    val ceil=if(totalRequired==0f) 0 else ceil(totalRequired).toInt()
                                    ceil.toString()
                                     */
                                    // 649+(649*(20/100))
                                    val achievement=if(totalCollected >= totalIssued) 100f else floor((0f+(totalIssued/posCollected))*100f)
                                    achievement.toString()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun CollectionTable(comparatives: List<CityComparativeBloodBankKpi>){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(COLLECTION_LABEL,
                fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun IssuedTable(comparatives: List<CityComparativeBloodBankKpi>){
    var toggle       by remember { mutableStateOf(false) }
    val toggleIcon   =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {

        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(ISSUING_LABEL,
                fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?: EMPTY_STRING })
                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedDirectorate ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedSpecialized ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedEducational ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedNBTS ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedInsurance ?: 0}" })
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalIssuedCurative ?: 0}" })
                       }
                    }
                }
            }
        }
    }
}
@Composable
private fun HbvTable(comparatives:List<CityComparativeBloodBankKpi>,
                     reasons:List<IncinerationReason>,reasonInt:Int=3){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(HBV_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHbvCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHbvCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun HcvTable(comparatives:List<CityComparativeBloodBankKpi>,
                     reasons:List<IncinerationReason>,reasonInt:Int=2){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(HCV_LABEL,
                fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHcvCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHcvCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun HivTable(comparatives:List<CityComparativeBloodBankKpi>,
                     reasons:List<IncinerationReason>,reasonInt:Int=4){

    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(HIV_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalHivCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalHivCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun SyphilisTable(comparatives:List<CityComparativeBloodBankKpi>,
                     reasons:List<IncinerationReason>,reasonInt:Int=5){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(SYPHILIS_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalSyphilisCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalSyphilisCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun IncompleteTable(comparatives:List<CityComparativeBloodBankKpi>,
                          reasons:List<IncinerationReason>,reasonInt:Int=6){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column{
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(INCOMPLETE_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalIncompleteCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalIncompleteCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun ExpiredTable(comparatives:List<CityComparativeBloodBankKpi>,
                            reasons:List<IncinerationReason>,reasonInt:Int){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(EXPIRED_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodDirectorate?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodDirectorate?:0
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodSpecialized?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodSpecialized?:0
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodEducational?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodEducational?:0
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodNBTS?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodNBTS?:0
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodInsurance?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodInsurance?:0
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = "$PERCENT_LESS_THAN_LABEL ${reasons[reasonInt].maximumPercent}",
                                firstList = comparatives.map { (it.totalExpiredBloodCurative?:0).toString() },
                                secondList = comparatives.map {
                                    val value=it.totalExpiredBloodCurative?:0
                                    val collected=it.totalCollectedCurative
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
}

@Composable
private fun ReactiveTable(comparatives:List<CityComparativeBloodBankKpi>){
    var toggle          by remember { mutableStateOf(false) }
    val toggleIcon      =  if(toggle)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white

    Column {
        VerticalSpacer()
        Row(modifier= Modifier
            .fillMaxWidth()
            .background(ORANGE),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(icon=toggleIcon, background = BLUE, paddingEnd = 5, paddingStart = 5) {toggle=!toggle }
            Label(REACTIVE_INCINERATED_LABEL,fontWeight = FontWeight.Bold,color=Color.White, fontSize = 11)
            Label(if(toggle) HIDE_LABEL else SHOW_LABEL, fontWeight = FontWeight.Bold, color = Color.White, paddingStart = 5, paddingEnd = 5)
        }
        AnimatedVisibility(visible = toggle /**/) {
            Column{
                Row(modifier=Modifier.fillMaxWidth()){
                    TableColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        header = "$CITY_LABEL\n",
                        items = comparatives.map { it.cityName?:EMPTY_STRING })

                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                        .weight(3f)){
                        item{

                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$DIRECTORATE_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedDirectorate ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = DIRECTORATE_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map {
                                    (
                                                    (it.totalHbvDirectorate?:0)+
                                                    (it.totalHcvDirectorate?:0)+
                                                    (it.totalHivDirectorate?:0)+
                                                    (it.totalSyphilisDirectorate?:0)
                                    )
                                        .toString()
                                                             },
                                secondList = comparatives.map {
                                    val value=  (
                                            (it.totalHbvDirectorate?:0)+
                                                    (it.totalHcvDirectorate?:0)+
                                                    (it.totalHivDirectorate?:0)+
                                                    (it.totalSyphilisDirectorate?:0)
                                            )
                                    val collected=it.totalCollectedDirectorate
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())
                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$SPECIALIZED_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedSpecialized ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = SPECIALIZED_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map { (
                                        (it.totalHbvSpecialized?:0)+
                                                (it.totalHcvSpecialized?:0)+
                                                (it.totalHivSpecialized?:0)+
                                                (it.totalSyphilisSpecialized?:0)
                                ).toString() },
                                secondList = comparatives.map {
                                    val value=((it.totalHbvSpecialized?:0)+
                                            (it.totalHcvSpecialized?:0)+
                                            (it.totalHivSpecialized?:0)+
                                            (it.totalSyphilisSpecialized?:0))
                                    val collected=it.totalCollectedSpecialized
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$EDUCATIONAL_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedEducational ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = EDUCATIONAL_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map { (
                                        (it.totalHbvEducational?:0)+
                                                (it.totalHcvEducational?:0)+
                                                (it.totalHivEducational?:0)+
                                                (it.totalSyphilisEducational?:0)
                                ).toString() },
                                secondList = comparatives.map {
                                    val value= (
                                            (it.totalHbvEducational?:0)+
                                                    (it.totalHcvEducational?:0)+
                                                    (it.totalHivEducational?:0)+
                                                    (it.totalSyphilisEducational?:0)
                                            )
                                    val collected=it.totalCollectedEducational
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$IS_NBTS_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedNBTS ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = IS_NBTS_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map {
                                    (
                                            (it.totalHbvNBTS?:0)+
                                                    (it.totalHcvNBTS?:0)+
                                                    (it.totalHivNBTS?:0)+
                                                    (it.totalSyphilisNBTS?:0)
                                            ).toString() },
                                secondList = comparatives.map {
                                    val value=(
                                            (it.totalHbvNBTS?:0)+
                                                    (it.totalHcvNBTS?:0)+
                                                    (it.totalHivNBTS?:0)+
                                                    (it.totalSyphilisNBTS?:0)
                                            )
                                    val collected=it.totalCollectedNBTS
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$INSURANCE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedInsurance ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = INSURANCE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map {
                                    (
                                            (it.totalHbvInsurance?:0)+
                                                    (it.totalHcvInsurance?:0)+
                                                    (it.totalHivInsurance?:0)+
                                                    (it.totalSyphilisInsurance?:0)
                                            ).toString() },
                                secondList = comparatives.map {
                                    val value=(
                                            (it.totalHbvInsurance?:0)+
                                                    (it.totalHcvInsurance?:0)+
                                                    (it.totalHivInsurance?:0)+
                                                    (it.totalSyphilisInsurance?:0)
                                            )
                                    val collected=it.totalCollectedInsurance
                                    val posCollected=if(collected==0 || collected==null) 1 else collected
                                    val ratio=value.toFloat()/posCollected.toFloat()*100
                                    (ceil(ratio).toString())

                                }
                            )
                            TableColumn(
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                header = "$CURATIVE_SECTOR_LABEL\n",
                                items = comparatives.map { "${it.totalCollectedCurative ?: 0}" })
                            DualTableColumn(
                                modifier = Modifier.fillMaxWidth(),
                                header = CURATIVE_SECTOR_LABEL,
                                firstSubHeader = NUMBER_LABEL,
                                secondSubHeader = PERCENT_LABEL,
                                firstList = comparatives.map {
                                    (
                                            (it.totalHbvCurative?:0)+
                                                    (it.totalHcvCurative?:0)+
                                                    (it.totalHivCurative?:0)+
                                                    (it.totalSyphilisCurative?:0)
                                            ).toString() },
                                secondList = comparatives.map {
                                    val value=(
                                            (it.totalHbvCurative?:0)+
                                                    (it.totalHcvCurative?:0)+
                                                    (it.totalHivCurative?:0)+
                                                    (it.totalSyphilisCurative?:0)
                                            )
                                    val collected=it.totalCollectedCurative
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

        Pair(if(firstQ) monthFrom.value else "1","3")-> FOR_FIRST_QUARTER_LABEL
        Pair(if(secondQ) monthFrom.value else "4","6")-> FOR_SECOND_QUARTER_LABEL
        Pair(if(thirdQ) monthFrom.value else "7","9")-> FOR_THIRD_QUARTER_LABEL
        Pair(if(fourthQ) monthFrom.value else "10","12")-> FOR_FOURTH_QUARTER_LABEL
        Pair(if(firstSim) monthFrom.value else "1",if(firstSim) monthTo.value else "6")-> FOR_FIRST_HALF_LABEL
        Pair(if(secondSim) monthFrom.value else "7",if(secondSim) monthTo.value else "12")-> FOR_SECOND_HALF_LABEL
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

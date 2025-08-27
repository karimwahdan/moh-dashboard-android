package com.kwdevs.hospitalsdashboard.views.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.models.Quadruple
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalGeneralCreateRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CREATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.UPDATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.routes.AreaViewRoute
import com.kwdevs.hospitalsdashboard.routes.ChangePasswordRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.routes.UserControlRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BOTTOM
import com.kwdevs.hospitalsdashboard.views.assets.BarGraph
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LineGraph
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PieGraph
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WELCOME_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.cards.AreaCard
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.CityBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.CurativeBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.EducationalBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.InsuranceBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.NBTSBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.SpecializedBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule.BloodStocksCharts
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.issuingDepartmentModule.DailyBloodStockPieChart
import com.kwdevs.hospitalsdashboard.views.pages.home.sections.HomeCitiesSection
import com.kwdevs.hospitalsdashboard.views.pages.home.sections.HomeHospitalTypesSection
import com.kwdevs.hospitalsdashboard.views.pages.home.sections.HomeSectorsSection
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController){
    val userType    = Preferences.User().getType()
    val superUser   = userType?.let{ if(it==ViewType.SUPER_USER) Preferences.User().getSuper() else null}
    val normalUser = userType?.let{ if(it==ViewType.HOSPITAL_USER) Preferences.User().get() else null}
    val controller : HomeController= viewModel()
    val state by controller.singleState.observeAsState()
    val showSheet = remember { mutableStateOf(false) }

    if(userType==ViewType.SUPER_USER) {if(superUser==null)navHostController.navigate(LoginRoute.route)}
    else if(userType==ViewType.HOSPITAL_USER) {if(normalUser==null)navHostController.navigate(LoginRoute.route)}
    else navHostController.navigate(LoginRoute)

    var sectors                 by remember { mutableStateOf<List<Sector>>(emptyList())}
    var types                   by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    var cities                  by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    var areas                   by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }
    val bloodStocks             =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val filteredBloodStocks     =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val filteredPRBcsStocks     =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }

    var incinerationList        by remember { mutableStateOf<List<MonthlyIncineration>>(emptyList()) }
    val hospitals               =  remember { mutableStateOf<List<Hospital>>(emptyList())}

    val showFilterDialog        =  remember { mutableStateOf(false) }
    var loading                 by remember { mutableStateOf(true) }
    var success                 by remember { mutableStateOf(false) }
    var fail                    by remember { mutableStateOf(false) }
    var errorMessage            by remember { mutableStateOf("")}
    var errors                  by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var superUserRoles          by remember { mutableStateOf<List<String>>(emptyList()) }

    var canBrowseCities         by remember { mutableStateOf(false) }
    var canReadCities           by remember { mutableStateOf(false) }

    var canBrowseAreas          by remember { mutableStateOf(false) }
    var canReadAreas            by remember { mutableStateOf(false) }

    var canBrowseSectors        by remember { mutableStateOf(false) }
    var canReadSectors          by remember { mutableStateOf(false) }

    var canBrowseHospitalTypes  by remember { mutableStateOf(false) }
    var canReadHospitalTypes    by remember { mutableStateOf(false) }

    var canBrowseHospitals      by remember { mutableStateOf(false) }
    var canReadHospitals        by remember { mutableStateOf(false) }

    var canCreateHospital       by remember { mutableStateOf(false) }
    var canUpdateHospital       by remember { mutableStateOf(false) }

    val keys                =   remember { mutableStateOf<List<String>>(emptyList()) }
    val sectorKeys          =   remember { mutableStateOf<List<String>>(emptyList()) }
    val hospitalTypeKeys    =   remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        if(superUser!=null){
            val isSuper=superUser.isSuper
            if(!isSuper){
                val roles=superUser.roles
                superUserRoles=roles.map { it.slug }

                if(roles.isNotEmpty()){
                    roles.forEach {role->
                        val permissions=role.permissions.map { p->p.slug }
                        canBrowseCities=permissions.contains(BROWSE_CITY)
                        permissions.forEach {p->
                            when(p){
                                BROWSE_CITY->{canBrowseCities=true}
                                BROWSE_SECTOR->{canBrowseSectors=true}
                                BROWSE_AREA->{canBrowseAreas=true}
                                BROWSE_HOSPITAL_TYPE->{canBrowseHospitalTypes=true}
                            }
                        }

                        canCreateHospital=permissions.contains(CREATE_HOSPITAL)
                        canUpdateHospital=permissions.contains(UPDATE_HOSPITAL)
                        canBrowseHospitals=permissions.contains(BROWSE_HOSPITAL)
                        canReadHospitals=permissions.contains(READ_HOSPITAL)

                        canBrowseCities=permissions.contains(BROWSE_CITY)
                        canReadCities=permissions.contains(READ_CITY)

                        canBrowseAreas=permissions.contains(BROWSE_AREA)
                        canReadAreas=permissions.contains(READ_AREA)

                        canBrowseSectors=permissions.contains(BROWSE_SECTOR)
                        canReadSectors=permissions.contains(READ_SECTOR)

                        canBrowseHospitalTypes=permissions.contains(BROWSE_HOSPITAL_TYPE)
                        canReadHospitalTypes=permissions.contains(READ_HOSPITAL_TYPE)

                    }
                }
                else{
                    canBrowseHospitals=false
                    canReadHospitals=false
                    canCreateHospital=false
                    canUpdateHospital=false

                    canBrowseCities=false
                    canReadCities=false

                    canBrowseAreas=false
                    canReadAreas=false

                    canBrowseSectors=false
                    canReadSectors=false

                    canBrowseHospitalTypes=false
                    canReadHospitalTypes=false
                }
            }
            else{

                canBrowseHospitals=true
                canReadHospitals=true
                canCreateHospital=true
                canUpdateHospital=true

                canBrowseCities=true
                canReadCities=true

                canBrowseAreas=true
                canReadAreas=true

                canBrowseSectors=true
                canReadSectors=true

                canBrowseHospitalTypes=true
                canReadHospitalTypes=true



            }
        }
    }
    when(state){
        is UiState.Loading->{
            loading=true;fail=false;success=false
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
                errors=exception.errors?: emptyMap()
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<HomeResponse>
                val response = s.data
                val data = response.data
                sectors=data.sectors
                types=data.types
                cities=data.cities
                areas=data.areas
                hospitals.value=data.hospitals
                bloodStocks.value=data.bloodStocksToday
                keys.value=bloodStocks.value.mapNotNull { it.hospital?.name }
                filteredBloodStocks.value=data.bloodStocksToday
                filteredPRBcsStocks.value=data.bloodStocksToday.filter { it.bloodUnitTypeId==2 }
                if(canBrowseSectors) sectorKeys.value=filteredBloodStocks.value.map { it.hospital?.sector?.name?:"" }
                if(canBrowseHospitalTypes) hospitalTypeKeys.value=filteredBloodStocks.value.map { it.hospital?.type?.name?:"" }
                incinerationList=data.incinerations
            }

        }
        else->{
            LaunchedEffect(Unit) {controller.getHome()}
        }
    }

    HospitalsFilterDialog(
        showDialog = showFilterDialog,
        cities     = cities,  types  = types,
        sectors    = sectors, result = hospitals,
    )
    Container(
        title="",
        showSheet=showSheet,
        sheetColor = if(fail) Color.Red else GREEN,
        sheetContent = { if(fail) Label(errorMessage) }
    ) {
        Column(modifier= Modifier.fillMaxWidth().background(color = WHITE),
            verticalArrangement = Arrangement.Center){
            HomeHead(superUser,navHostController)
            if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
            if(fail) FailScreen(modifier = Modifier.fillMaxSize())
            if(success){
                VerticalSpacer()
                LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    item{


                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically){
                            if(canCreateHospital){
                                Box(modifier=Modifier.padding(5.dp)){
                                    CustomButtonWithImage(
                                        enabled = canCreateHospital,
                                        label = "Add new Hospital",
                                        maxWidth = 82,
                                        icon = R.drawable.ic_hospital_white,
                                        background = BLUE
                                    ) {
                                        if(canCreateHospital){
                                            Preferences.CrudTypes().set(CrudType.CREATE)
                                            navHostController.navigate(HospitalGeneralCreateRoute.route)
                                        }
                                    }
                                }
                            }

                            superUser?.let {
                                if(it.isSuper){
                                    HorizontalSpacer()
                                    CustomButtonWithImage(label="Main Admin", maxWidth = 82, minWidth = 120) {
                                        navHostController.navigate(AdminHomeRoute.route)
                                    }
                                }
                            }

                        }

                        VerticalSpacer()
                        HomeSectorsSection(hasPermission=canBrowseSectors,sectors,navHostController)
                        HomeHospitalTypesSection(hasPermission=canBrowseHospitalTypes,types,navHostController)
                        HomeCitiesSection(hasPermission=canBrowseCities,superUser=superUser,items=cities,navHostController=navHostController)

                        if(areas.isNotEmpty()){
                            if(!canBrowseAreas){
                                val areaHeads=areas.map { it.headId }
                                if(areaHeads.isNotEmpty()){
                                    Column(modifier= Modifier
                                        .fillMaxWidth()
                                        .border(width = 1.dp, color = Color.LightGray, shape = rcs(10)),)
                                    {
                                        VerticalSpacer()
                                        Row(modifier=Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            verticalAlignment = Alignment.CenterVertically){
                                            HorizontalSpacer()
                                            areas.forEach {
                                                if(it.headId==(superUser?.id?:0)){
                                                    AreaCard(it, modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        Preferences.Areas().set(Area(id=it.id, cityId = it.cityId,name=it.name, headId = it.headId))
                                                        navHostController.navigate(AreaViewRoute.route)
                                                    }
                                                    HorizontalSpacer()
                                                }
                                            }
                                        }
                                        VerticalSpacer()
                                    }
                                }
                            }
                        }
                        VerticalSpacer()
                        val emptyBloodStocks = bloodStocks.value.isEmpty()
                        val sectorHospitals  = sectors.sumOf { it.hospitalsCount?:0 }
                        val stocksSize       = bloodStocks.value.groupBy { it.hospital }.size

                        if(!emptyBloodStocks /*|| (sectorHospitals > stocksSize)*/){
                            Row(modifier=Modifier.fillMaxWidth().background(BLACK),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,){
                                Icon(R.drawable.ic_info_white, background = BLUE)
                                Label(
                                    text="اجمالى ${bloodStocks.value.filter{it.hospital?.isNbts==false}.groupBy { it.hospital }.size} مستشفى من ${sectors.sumOf { (it.hospitalsCount?:0)}-28}",
                                    color = if((bloodStocks.value.filter{it.hospital?.isNbts==false}.groupBy { it.hospital }.size)<(sectors.sumOf { (it.hospitalsCount?:0) }-28)) Color.Red else GREEN,
                                    fontWeight = FontWeight.Bold,
                                    maximumLines = 2,
                                    textAlign = TextAlign.Start,
                                    paddingEnd = 5,
                                    paddingStart = 5)
                            }
                            VerticalSpacer()
                            Row(modifier=Modifier.fillMaxWidth().background(BLACK),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically){
                                Icon(R.drawable.ic_info_white, background = BLUE)
                                Label(
                                    text="اجمالى ${bloodStocks.value.filter{it.hospital?.isNbts==true}.groupBy { it.hospital }.size} اقليمي من 28 ",
                                    color = if((bloodStocks.value.filter{it.hospital?.isNbts==true}.groupBy { it.hospital }.size)<28) Color.Red else GREEN,
                                    fontWeight = FontWeight.ExtraBold,
                                    maximumLines = 2,
                                    paddingEnd = 5,
                                    paddingStart = 5)
                            }
                        }

                        BloodStockTable(bloodStocks)
                        VerticalSpacer()
                        BloodStocksCharts(
                            bloodStocks         = bloodStocks.value,
                            keys                = keys,
                            sectorKeys          = sectorKeys,
                            hospitalTypeKeys    = hospitalTypeKeys,
                            selectedBloodStocks = filteredPRBcsStocks,
                        )
                        if(!emptyBloodStocks) DailyBloodStockPieChart(bloodStocks.value)
                        HorizontalDivider()
                        VerticalSpacer()
                        CityBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()
                        InsuranceBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()
                        EducationalBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()
                        CurativeBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()
                        SpecializedBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()
                        NBTSBloodBankKpiSection(controller)
                        HorizontalDivider()
                        VerticalSpacer()

                    }
                }
                VerticalSpacer()
            }
        }
    }
}

@Composable
private fun IncinerationCharts(incinerationData:List<MonthlyIncineration>){
    val (xLabels, yValues) = prepareMonthlyIncinerationLineChartData(incinerationData.filter { (it.bloodUnitTypeId?:0)==2 && (it.year?:"")=="2025" })
    //val context= LocalContext.current
    Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
        Label(text="Monthly Incinerated PRBCs Units across the country\n in 2025",
            fontSize = 16, fontWeight = FontWeight.Bold,
            maximumLines = 3,
            color = colorResource(R.color.teal_700))
    }
    LineGraph(
        modifier=Modifier.height(300.dp),
        xData = xLabels,
        yData = yValues,
        label = "Monthly Incinerated Units",
        lineColor = Color.Red,
        fillColors = Color.Red,
        axisTextColor = Color.Black,
        drawMarkers = true,
        drawFilled = true,
        description = "Trend of Monthly Incineration",
        descriptionEnabled = true,
        legendEnabled = true,
        xAxisPosition = BOTTOM
    )
    val byYear=incinerationData.groupBy { it.year }
    byYear.forEach { (year, listOfIncinerationData) ->
        Span(text=year?:"", color = WHITE, backgroundColor = BLUE)
        val byMonthAndType=listOfIncinerationData.groupBy { it.month }
        val byBloodGroup=preparePieChartByBloodGroup(listOfIncinerationData.filter { (it.bloodUnitTypeId?:0)==2 })
        val vs=byBloodGroup.map { Pair(it.key.name?:"",it.value.toInt()) }
        PieGraph(
            xData = vs
        )
        byMonthAndType.forEach { (month, list) ->
            val monthString=when(month){
                "1"->"Jan"
                "2"->"Feb"
                "3"->"Mar"
                "4"->"Apr"
                "5"->"May"
                "6"->"Jun"
                "7"->"Jul"
                "8"->"Aug"
                "9"->"Sep"
                "10"->"Oct"
                "11"->"Nov"
                "12"->"Dec"

                else->""
            }
            VerticalSpacer()
            Span(text= monthString, color = WHITE, backgroundColor = ORANGE)

            val (x,y)=prepareBarByUnitType(list)
            BarGraph(
                modifier = Modifier.height(300.dp),
                xData=x.map { it.name?:"" },yData=y,
                chartDescriptionText = "",
            )
        }
    }
}

fun processForTodayByCityBarGraph(data: List<DailyBloodStock>,byCity:Boolean,byArea:Boolean,byHospital:Boolean,
                                  byDate:Boolean,date:String?=null): Pair<List<String>, List<Int>> {

    //val formatter = DateTimeFormatter.ofLocalizedDate(MEDIUM).withLocale(getDefault())
    //val customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh", ENGLISH)
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    // Filter today's records
    val todayData = data.filter {
        if(byDate && date!=null) {
            val block = getTimeBlock(date)
            block.startsWith(today)
            (it.entryDate?:today).split(" ")[1] == block
        }
        else {
            val block = getTimeBlock(it.entryDate ?: "")
            block.startsWith(today)
            //it.timeBLock?.startsWith(today) == true
        }
    }
    if (todayData.isEmpty()) return Pair(emptyList(), emptyList())
    // Map timeBLock -> timeBlock
    val dataWithBlocks = todayData.mapNotNull { stock ->
        val timeBlock = stock.entryDate?.let { getTimeBlock(it) } ?: return@mapNotNull null
        Triple(
            stock,
            if(byCity)stock.hospital?.city?.name?:"N/A"
            else if(byArea)stock.hospital?.area?.name?:"N/A"
            else if(byHospital) stock.hospital?.name?:"N/A"
            else stock.hospital?.city?.name?:"N/A",
            timeBlock)
    }

    // Get the latest block today
    val latestBlock = dataWithBlocks.maxOfOrNull { it.third } ?: "00"
    // Filter only entries in the latest block
    val latestBlockData = dataWithBlocks.filter { it.third == latestBlock }
    // Group by city and sum the amounts
    val groupedByCity = latestBlockData.groupBy { it.second }
        .mapValues { (_, group) ->
            group.sumOf { it.first.amount ?: 0 }
        }

    // Sort alphabetically (optional)
    val sorted = groupedByCity.toSortedMap()

    val cities = sorted.keys.toList()
    val amounts = sorted.values.toList()
    return Pair(cities, amounts)
}

fun processGroupedDataByHospitalLocationLabeled(
    data: List<DailyBloodStock>,
    groupByCity: Boolean = false,
    groupByArea: Boolean = false,
    groupByHospital: Boolean = false,
    groupByDate: Boolean = false,
    selectedDate: String? = null
): Pair<List<String>, Map<String, List<Int>>> {
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    // Filter data
    val filteredData = data.filter {
        if (groupByDate && selectedDate != null) {
            val block = getTimeBlock(selectedDate)
            block.startsWith(today) &&
                    (it.entryDate?.split(" ")?.getOrNull(1) == block.split(" ").getOrNull(1))
        } else {
            val block = getTimeBlock(it.entryDate ?: "")
            block.startsWith(today)
        }
    }

    if (filteredData.isEmpty()) return Pair(emptyList(), emptyMap())

    // Map each item into Quadruple: data, label (city/area/hospital), bloodGroup, block
    val bloodGroupLabels=data.map { it.bloodGroup?.name?:"" }
    val locationLabels = data.map {
        if(groupByCity) it.hospital?.city?.name
        if(groupByArea) it.hospital?.area?.name
        if(groupByHospital) it.hospital?.name
        else "N/A"
    }.distinct()

    val groupedData = bloodGroupLabels.associateWith { group ->
        locationLabels.map { location ->
            data.filter {
                val city=it.hospital?.city
                val bloodGroup=it.bloodGroup
                (bloodGroup?.name == group) && (city?.name == location)
            }.sumOf { it.amount ?: 0 }
        }
    }

    val grouped = filteredData.map { item ->
        val timeBlock = item.entryDate?.let { getTimeBlock(it) } ?: "00"
        val label = when {
            groupByCity -> item.hospital?.city?.name ?: "N/A"
            groupByArea -> item.hospital?.area?.name ?: "N/A"
            groupByHospital -> item.hospital?.name ?: "N/A"
            else -> item.hospital?.city?.name ?: "N/A"
        }
        val bloodGroup = item.bloodGroup
        Quadruple(item.amount ?: 0, label, bloodGroup?.name?:"", timeBlock)
    }

    // Get all unique X labels (cities, areas, or hospitals)
    val xLabels = grouped.map { it.second }.distinct().sorted()

    // Get all blood group labels
    val bloodGroups = grouped.map { it.third }.distinct().sorted()

    // Get latest time block only (as in your original version)
    val latestBlock = grouped.maxOfOrNull { it.fourth } ?: "00"
    val latestBlockData = grouped.filter { it.fourth == latestBlock }

    // For each blood group, build a list of values for each xLabel
    val groupedYData: Map<String, List<Int>> = bloodGroups.associateWith { groupLabel ->
        xLabels.map { label ->
            latestBlockData
                .filter { it.second == label && it.third == groupLabel }
                .sumOf { it.first }
        }
    }

    return Pair(xLabels, groupedYData)
}

private fun prepareMonthlyIncinerationLineChartData(data: List<MonthlyIncineration>): Pair<List<String>, List<Int>> {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct", "Nov", "Dec")

    val monthlyMap = data.groupBy { it.month?.toIntOrNull() ?: 0 }
        .mapValues { (_, records) ->
            records.sumOf { it.value ?: 0 }
        }

    val yData = (1..12).map { monthIndex ->
        monthlyMap.getOrDefault(monthIndex, 0)
    }

    return months to yData
}

private fun prepareBarByUnitType(data: List<MonthlyIncineration>): Pair<List<BasicModel>, List<Int>> {
    val types = data.mapNotNull { it.bloodUnitType }.distinct()
    val values = types.map { type ->
        data.filter { it.bloodUnitType == type }.sumOf { it.value ?: 0 }
    }
    return types to values
}

private fun preparePieChartByBloodGroup(data: List<MonthlyIncineration>): Map<BasicModel, Float> {
    val groups = data.mapNotNull { it.bloodGroup }.distinct()
    val total = data.sumOf { it.value ?: 0 }.toFloat()

    return groups.associateWith { group ->
        data.filter { it.bloodGroup == group }
            .sumOf { it.value ?: 0 }
            .div(total)
            .times(100f)
    }
}

@Composable
fun Charts(){
    Row(modifier=Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
        Label(text="Hospitals By Icu Free Beds In Cairo",
            fontSize = 16, fontWeight = FontWeight.Bold,
            color = colorResource(R.color.teal_700))
    }
    val strings =listOf("h1"  ,"h2" ,"h3" ,"h4" ,"h5",
        "h6" ,"h7" ,"h8" ,"h9" ,"h10",
        "h11","h12","h13","h14","h15",
        "h16","h17","h18","h19","h20",
        "h21","h22","h23","h24","h25",
        "h26","h27","h28","h29","h30",
        "h31")
    val ints=listOf(1 ,2 ,3 , 5,2 ,
        6 ,7 ,12, 4,23,
        5 ,45,76, 8,12,
        14,16,34,45,23,
        12,87,23,13,24,
        45)
    LineGraph(
        modifier= Modifier
            .fillMaxWidth()
            .height(250.dp),
        xData = strings,
        yData = ints,
        label = "",
        description = ""
    )
    val z=listOf(Pair("Cardiology",7),
        Pair("Oncology",5),Pair("Hematology",12),
        Pair("Other",10),Pair("Nursing",6),Pair("Lab",3),Pair("Blood Bank",8))
    val pieColors= listOf(
        colorResource(R.color.red).toArgb(),
        colorResource(R.color.green).toArgb(),
        colorResource(R.color.blue).toArgb(),
        colorResource(R.color.orange).toArgb(),
        colorResource(R.color.teal_700).toArgb(),
        colorResource(R.color.purple_500).toArgb(),
        colorResource(R.color.blue3).toArgb()
    )
    PieGraph(z, pieCenterText = "Departments", pieColors = pieColors)
    LazyRow{ item{} }
}

@Composable
private fun HomeHead(user:SuperUser?,navHostController: NavHostController){
    val userType=Preferences.User().getType()
    Column(modifier=Modifier.background(color = colorResource(R.color.teal_700), shape = rcsB(20))) {
        Box{
            Header(HOME_LABEL, fontSize = 21, fontWeight = FontWeight.Bold, color = Color.White)
            Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                VerticalSpacer()
                Row(verticalAlignment = Alignment.CenterVertically){
                    HorizontalSpacer()
                    IconButton(R.drawable.ic_lock_blue, containerSize = 30) {
                        navHostController.navigate(ChangePasswordRoute.route)
                    }
                    HorizontalSpacer()
                    IconButton(icon = R.drawable.ic_exit_red, containerSize = 30) {
                        //A
                        Preferences.Areas().delete()
                        //B
                        Preferences.BloodBanks().delete()
                        //C
                        Preferences.Cities().delete()
                        Preferences.CrudTypes().delete()
                        //H
                        Preferences.Hospitals().delete()
                        Preferences.HospitalTypes().delete()
                        Preferences.HospitalDevices().delete()
                        //O
                        Preferences.OperationRooms().delete()

                        //P
                        Preferences.Patients().delete()

                        //R
                        Preferences.Roles().delete()
                        Preferences.RenalDevices().delete()

                        //S
                        Preferences.Sectors().delete()

                        //U
                        Preferences.User().delete()
                        Preferences.User().deleteSuper()
                        Preferences.User().deleteType()

                        //V
                        Preferences.ViewTypes().delete()
                        Preferences.ViewTypes().deletePatientViewType()

                        //W
                        Preferences.Wards().delete()
                        navHostController.navigate(LoginRoute.route)
                    }


                    if(userType==ViewType.SUPER_USER && user?.isSuper==true){
                        HorizontalSpacer()
                        IconButton(icon = R.drawable.ic_manage_accounts_blue, containerSize = 30) {
                            navHostController.navigate(UserControlRoute.route)
                        }
                    }
                    HorizontalSpacer()
                }
            }
        }
        Box(modifier=Modifier.fillMaxWidth()){
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                if(userType==ViewType.SUPER_USER && user !=null){
                    Label(label=WELCOME_USER_LABEL,text = "${user.title?.name} ${user.name}",
                        color = Color.White, labelColor = Color.White,
                        fontSize = 16, fontWeight = FontWeight.Bold)
                }
            }
        }
        VerticalSpacer()
    }
}

fun getTimeBlock(timeBlock: String): String {
    // timeBlock expected format: "dd-MM-yyyy HH"
    return try {
        val parts = timeBlock.split(" ")
        if (parts.size < 2) return timeBlock // fallback to original

        val date = parts[0]
        val hour = parts[1].toIntOrNull() ?: return "$date 00"

        val block = when (hour) {
            in 0..5 -> "00"
            in 6..11 -> "06"
            in 12..17 -> "12"
            else -> "18"
        }

        "$date $block"
    } catch (e: Exception) {
        timeBlock // fallback
    }
}

@Composable
private fun SimpleHospitalCard(item:Hospital,navHostController: NavHostController){
    val name=item.name
    val sector=item.sector
    val type=item.type
    val active=item.active
    val hospitalArea=item.area
    val city=item.city
    val address=item.address
    val isNBTS=item.isNBTS
    Box(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .clip(rcs(20))
        .background(color = WHITE,shape=rcs(20)).clickable {
            val simple= ModelConverter().convertHospitalToSimple(item)
            Preferences.Hospitals().set(simple)
            navHostController.navigate(HospitalsViewRoute.route)
        }){
        ColumnContainer(
            background = if(isNBTS==true) PALE_ORANGE else WHITE
        ) {
            Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
                Row(modifier=Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_home_work_blue)
                    HorizontalSpacer()
                    Label(name, fontWeight = FontWeight.Bold, maximumLines = 5)
                }
                Icon(if (active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                IconButton(R.drawable.ic_view_timeline_blue) {
                    val simple=ModelConverter().convertHospitalToSimple(item)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalModuleSelectorRoute.route)
                }
            }
            Row(modifier=Modifier.fillMaxWidth()){
                Span(sector?.name?:"", fontSize = 14, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5)
                Span(type?.name?:"", fontSize = 14, color = WHITE, backgroundColor = ORANGE, startPadding = 5, endPadding = 5)

            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                Span(city?.name?:"", fontSize = 14, color = WHITE, backgroundColor = BLUE)
                HorizontalSpacer()
                Span(hospitalArea?.name?:"", fontSize = 14, color = WHITE, backgroundColor = BLACK)
                if(item.isNBTS==true) Icon(R.drawable.ic_blood_drop)
            }
            VerticalSpacer()
            Row(modifier=Modifier.padding(horizontal = 5.dp)){
                Label(text=address?:"")
            }
            VerticalSpacer()
        }
    }
}

@Preview
@Composable
private fun Preview(){ HomeScreen(rememberNavController()) }
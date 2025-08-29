package com.kwdevs.hospitalsdashboard.views.pages.hospitals

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.controller.AreaController
import com.kwdevs.hospitalsdashboard.controller.CityController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartment
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartmentResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.sector.SectorResponse
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.settings.statuses.StatusResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CITY_HEAD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CREATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DETAILED_FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FOLLOWS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_BURNS_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_CCU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NEUROLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ONCOLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_RENAL_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_WARDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITALS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IN_CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MAX_FREE_BEDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MIN_FREE_BEDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OF_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB
import com.kwdevs.hospitalsdashboard.views.rcsT

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun HospitalsIndexPage(navHostController: NavHostController){
    val userType = Preferences.User().getType()
    val superUser = userType?.let{ if(it== ViewType.SUPER_USER) Preferences.User().getSuper() else null}
    if(userType== ViewType.SUPER_USER) {if(superUser==null)navHostController.navigate(LoginRoute.route)}
    var superUserRoles by remember { mutableStateOf<List<String>>(emptyList()) }
    var canBrowseCities by remember { mutableStateOf(false) }
    var canReadCities by remember { mutableStateOf(false) }
    var canBrowseAreas by remember { mutableStateOf(false) }
    var canReadAreas by remember { mutableStateOf(false) }
    var canBrowseSectors by remember { mutableStateOf(false) }
    var canReadSectors by remember { mutableStateOf(false) }

    var canBrowseHospitalTypes by remember { mutableStateOf(false) }
    var canReadHospitalTypes by remember { mutableStateOf(false) }

    var canBrowseHospitals by remember { mutableStateOf(false) }
    var canReadHospitals by remember { mutableStateOf(false) }
    var canCreateHospitals by remember { mutableStateOf(false) }
    val controller: HospitalController= viewModel()
    val paginationState by controller.paginationState.observeAsState()
    val hospitals = remember { mutableStateOf<List<Hospital>>(emptyList()) }
    val filtered = remember { mutableStateOf<List<Hospital>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(2) }
    var total by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val showFilterDialog= remember { mutableStateOf(false) }
    val showSheet= remember { mutableStateOf(false) }
    when(paginationState){
        is UiState.Loading->{loading=true;fail=false;success=false}
        is UiState.Error->{
            loading=false;fail=true;success=false
            val s = paginationState as UiState.Error
            val exception=s.exception
            errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
            errors=exception.errors?: emptyMap()

        }
        is UiState.Success->{
            LaunchedEffect(Unit) {

                loading=false;fail=false;success=true

                val state = paginationState as UiState.Success< ApiResponse< PaginationData<Hospital> > >
                val response=state.data
                val paginationData=response.pagination
                val data =paginationData.data
                hospitals.value = data
                lastPage=paginationData.lastPage
                total=data.size
            }
        }
        else->{
            val bySector=Preferences.Hospitals().bySector()
            val byType=Preferences.Hospitals().byType()
            if(bySector){
                val sector=Preferences.Sectors().getItem()
                sector?.let {
                    controller.bySector(page = currentPage.intValue, sectorId = sector.id)
                }
            }
            else if(byType){
                val type = Preferences.HospitalTypes().getItem()
                type?.let{controller.byType(page = currentPage.intValue, typeId = type.id)}
            }
        }
    }
    LaunchedEffect(currentPage.intValue) {
        val bySector=Preferences.Hospitals().bySector()
        val byType=Preferences.Hospitals().byType()
        if(bySector){
            val sector=Preferences.Sectors().getItem()
            sector?.let {
                controller.bySector(page = currentPage.intValue, sectorId = sector.id)
            }
        }
        else if(byType){
            val type = Preferences.HospitalTypes().getItem()
            type?.let{controller.byType(page = currentPage.intValue, typeId = type.id)}
        }
    }

    LaunchedEffect(Unit) {
        if(superUser!=null){
            val isSuper=superUser.isSuper
            if(!isSuper){
                val roles=superUser.roles
                superUserRoles=roles.map { it.slug }
                canBrowseCities=superUserRoles.contains(CITY_HEAD)
                if(roles.isNotEmpty()){
                    roles.forEach {role->
                        val permissions=role.permissions.map { p->p.slug }
                        canBrowseHospitals=permissions.contains(BROWSE_HOSPITAL)
                        canReadHospitals=permissions.contains(READ_HOSPITAL)
                        canCreateHospitals=permissions.contains(CREATE_HOSPITAL)

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
                canCreateHospitals=true
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

    HospitalsFilterDialog(showFilterDialog,
        result = filtered)
    Container(
        title = HOSPITALS_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = { navHostController.navigate(HomeRoute.route) }
    ) {
        if(loading){
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
        else{
            if(success){
                PaginationContainer(shape=rcs(20),
                    currentPage=currentPage,
                    totalItems = total,
                    lastPage=lastPage,
                    showFilterButton = true,
                    onFilterClick = {showFilterDialog.value=true}) {
                    LazyColumn {
                        item{
                            hospitals.value.forEach { hospital->
                                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), contentAlignment = Alignment.Center){
                                    SimpleHospitalCard(hospital,navHostController)
                                }
                            }
                        }
                    }
                }

            }
            if(fail){
                FailScreen(modifier = Modifier.fillMaxSize(),
                    message = errorMessage, errors = errors)
            }
        }
    }
}

@Composable
private fun SimpleHospitalCard(item: Hospital, navHostController: NavHostController){
    val name=item.name
    val sector=item.sector
    val type=item.type
    val active=item.active
    val hospitalArea=item.area
    val city=item.city
    val address=item.address
    val isNBTS=item.isNBTS
    val user=Preferences.User().getSuper()
    val role=Preferences.Roles().get()
    val permissions=role?.permissions?.map { it.slug }
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
            Label(name, fontWeight = FontWeight.Bold, maximumLines = 5)


            Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Span(sector?.name?:EMPTY_STRING, fontSize = 12, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5)
                if(item.isNBTS==true) Icon(R.drawable.ic_blood_drop)
                Row(modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Span(type?.name?:EMPTY_STRING, fontSize = 12, color = WHITE, backgroundColor = ORANGE, startPadding = 5, endPadding = 5)
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(if (active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                        if(user?.isSuper==true || permissions?.contains("browse.modules")==true){
                            IconButton(R.drawable.ic_view_timeline_blue) {
                                val simple=ModelConverter().convertHospitalToSimple(item)
                                Preferences.Hospitals().set(simple)
                                navHostController.navigate(HospitalModuleSelectorRoute.route)
                            }

                        }

                    }
                }

            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                Span(city?.name?:EMPTY_STRING, fontSize = 12, color = WHITE, backgroundColor = BLUE)
                HorizontalSpacer()
                Span(hospitalArea?.name?:EMPTY_STRING, fontSize = 12, color = WHITE, backgroundColor = BLACK)
            }
            VerticalSpacer()
            Row(modifier=Modifier.padding(horizontal = 5.dp)){
                Label(text=address?:EMPTY_STRING)
            }
            VerticalSpacer()
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
private fun HospitalsFilterDialog(showDialog: MutableState<Boolean>, result: MutableState<List<Hospital>>){

    val hospitalController: HospitalController= viewModel()
    val hospitalsState by hospitalController.state.observeAsState()
    var loading by remember { mutableStateOf(true) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }

    val citiesController : CityController= viewModel()
    val citiesState by citiesController.state.observeAsState()
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    var loadingCities by remember { mutableStateOf(true) }
    var successCities by remember { mutableStateOf(false) }
    var failCities by remember { mutableStateOf(false) }

    val selectedCity = remember { mutableStateOf<CityWithCount?>(null)}
    val selectedCategory = remember { mutableStateOf<Sector?>(null) }
    val selectedType = remember { mutableStateOf<HospitalType?>(null) }

    val areaController: AreaController= viewModel()
    val areaState by areaController.state.observeAsState()
    var areas by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }

    var loadingAreas by remember { mutableStateOf(true) }
    var successAreas by remember { mutableStateOf(false) }
    var failAreas by remember { mutableStateOf(false) }
    val selectedArea = remember { mutableStateOf<AreaWithCount?>(null)}

    var showDetails by remember { mutableStateOf(false) }
    val hasIcu = remember { mutableStateOf(false) }
    val minFreeIcuBeds = remember { mutableStateOf("0") }
    val maxFreeIcuBeds = remember { mutableStateOf("0") }
    val hasCCU = remember { mutableStateOf(false) }
    val minFreeCcuBeds = remember { mutableStateOf("0") }
    val maxFreeCcuBeds = remember { mutableStateOf("0") }

    val hasNicu = remember { mutableStateOf(false) }
    val minFreeNicuBeds = remember { mutableStateOf("0") }
    val maxFreeNicuBeds = remember { mutableStateOf("0") }

    val hasOncologyCu = remember { mutableStateOf(false) }
    val minFreeOncologyCuBeds = remember { mutableStateOf("0") }
    val maxFreeOncologyCuBeds = remember { mutableStateOf("0") }

    val hasBurnsCu = remember { mutableStateOf(false) }
    val minFreeBurnsCuBeds = remember { mutableStateOf("0") }
    val maxFreeBurnsCuBeds = remember { mutableStateOf("0") }

    val hasNeurologyCu = remember { mutableStateOf(false) }
    val minFreeNeurologyCuBeds = remember { mutableStateOf("0") }
    val maxFreeNeurologyCuBeds = remember { mutableStateOf("0") }

    val hasMorgue = remember { mutableStateOf(false) }
    val minFreeMorgueBeds = remember { mutableStateOf("0") }
    val maxFreeMorgueBeds = remember { mutableStateOf("0") }

    val hasRenalWards = remember { mutableStateOf(false) }
    val minFreeRenalBeds = remember { mutableStateOf("0") }
    val maxFreeRenalBeds = remember { mutableStateOf("0") }

    val hasWards = remember { mutableStateOf(false) }
    val minFreeBeds = remember { mutableStateOf("0") }
    val maxFreeBeds = remember { mutableStateOf("0") }

    val hasDevices = remember { mutableStateOf(false) }
    val hasDepartments = remember { mutableStateOf(false) }
    val settingsController: SettingsController= viewModel()
    val sectorsState by settingsController.sectorsState.observeAsState()
    val typesState by settingsController.typesState.observeAsState()
    var types by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    var sectors by remember { mutableStateOf<List<Sector>>(emptyList()) }
    var basicDepartments by remember { mutableStateOf<List<BasicDepartment>>(emptyList()) }
    val departmentState by settingsController.basicDepartmentState.observeAsState()
    val selectedDepartment = remember { mutableStateOf<BasicDepartment?>(null) }
    var loadingBasicDepartments by remember { mutableStateOf(true) }
    var successBasicDepartments by remember { mutableStateOf(false) }
    var failBasicDepartments by remember { mutableStateOf(false) }

    val selectedDeviceStatus = remember { mutableStateOf<Status?>(null) }
    val deviceStatusesState by settingsController.deviceStatusState.observeAsState()
    var deviceStatuses by remember { mutableStateOf<List<Status>>(emptyList()) }
    var loadingDeviceStatuses by remember { mutableStateOf(true) }
    var successDeviceStatuses by remember { mutableStateOf(false) }
    var failDeviceStatuses by remember { mutableStateOf(false) }

    if(showDialog.value){
        LaunchedEffect(selectedCity.value) {
            if(selectedCity.value!=null){
                areaController.index(selectedCity.value?.id?:0)
            }
        }
        when(citiesState){
            is UiState.Loading->{loadingCities=true;failCities=false;successCities=false}
            is UiState.Error->{loadingCities=false;failCities=true;successCities=false}
            is UiState.Success->{
                loadingCities=false;failCities=false;successCities=true
                val s = citiesState as UiState.Success<CityWithCountResponse>
                val response = s.data
                val data = response.data
                cities=data
            }
            else->{citiesController.index()}
        }
        when(sectorsState){
            is UiState.Success->{
                val s = sectorsState as UiState.Success<SectorResponse>
                val response = s.data
                val data = response.data
                sectors= data
            }
            else->{settingsController.sectorsIndex()}
        }
        when(typesState){
            is UiState.Success->{
                val s = typesState as UiState.Success<HospitalTypeResponse>
                val response = s.data
                val data = response.data
                types= data
            }
            else->{settingsController.typesIndex()}
        }
        when(areaState){
            is UiState.Loading->{
                loadingAreas=true;failAreas=false;successAreas=false
            }
            is UiState.Error->{
                loadingAreas=false;failAreas=true;successAreas=false
            }
            is UiState.Success->{
                loadingAreas=false;failAreas=false;successAreas=true
                val s = areaState as UiState.Success<AreaWithCountResponse>
                val response = s.data
                val data = response.data
                areas=data
            }
            else->{

            }
        }
        when(deviceStatusesState){
            is UiState.Loading->{
                loadingDeviceStatuses=true;failDeviceStatuses=false;successDeviceStatuses=false
            }
            is UiState.Error->{loadingDeviceStatuses=false;failDeviceStatuses=true;successDeviceStatuses=false}
            is UiState.Success->{
                loadingDeviceStatuses=false;failDeviceStatuses=false;successDeviceStatuses=true
                val s = deviceStatusesState as UiState.Success<StatusResponse>
                val response = s.data
                val data = response.data
                deviceStatuses=data
            }
            else->{settingsController.deviceStatusesIndex()}
        }
        when(departmentState){
            is UiState.Loading->{
                loadingBasicDepartments=true;failBasicDepartments=false;successBasicDepartments=false
            }
            is UiState.Error->{loadingBasicDepartments=false;failBasicDepartments=true;successBasicDepartments=false}
            is UiState.Success->{
                loadingBasicDepartments=false;failBasicDepartments=false;successBasicDepartments=true
                val s = departmentState as UiState.Success<BasicDepartmentResponse>
                val response = s.data
                val data = response.data
                basicDepartments=data
            }
            else->{settingsController.basicDepartmentsIndex()}
        }
        when(hospitalsState){
            is UiState.Loading->{
                loading=true;fail=false;success=false
            }
            is UiState.Error->{loading=false;fail=true;success=false}
            is UiState.Success->{
                loading=false;fail=false;success=true
                val s = hospitalsState as UiState.Success<HospitalsResponse>
                val response = s.data
                val data = response.data
                result.value=data
                LaunchedEffect(Unit) {showDialog.value=false }
            }
            else->{}
        }

        Dialog(onDismissRequest = {showDialog.value=!showDialog.value}) {
            Column(modifier = Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color=Color.White, shape = rcs(20))
                .border(width = 1.dp, shape = rcs(20),
                    color = colorResource(R.color.teal_700))){
                Row(modifier=Modifier.fillMaxWidth()
                    .background(color= colorResource(R.color.teal_700),
                        shape = rcsT(20)
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Box(modifier = Modifier.padding(5.dp)){
                        IconButton(icon = R.drawable.ic_check_circle_green, background = colorResource(R.color.white)) {
                            val body = HospitalFilterBody(
                                cityId = selectedCity.value?.id,
                                areaId = selectedArea.value?.id,
                                sectorId = selectedCategory.value?.id,
                                typeId = selectedType.value?.id,

                                hasIcu = if(hasIcu.value) 1 else null,
                                icuFreeBedsStart = minFreeIcuBeds.value.toIntOrNull(),
                                icuFreeBedsEnd = maxFreeIcuBeds.value.toIntOrNull(),

                                hasCcu = if(hasCCU.value) 1 else null,
                                ccuFreeBedsStart = minFreeCcuBeds.value.toIntOrNull(),
                                ccuFreeBedsEnd = maxFreeCcuBeds.value.toIntOrNull(),

                                hasNicu = if(hasNicu.value) 1 else null,
                                nicuFreeBedsStart = minFreeNicuBeds.value.toIntOrNull(),
                                nicuFreeBedsEnd = maxFreeNicuBeds.value.toIntOrNull(),

                                hasNeurologyCu = if(hasNeurologyCu.value) 1 else null,
                                neurologyFreeBedsStart = minFreeNeurologyCuBeds.value.toIntOrNull(),
                                neurologyFreeBedsEnd = maxFreeNeurologyCuBeds.value.toIntOrNull(),

                                hasOncologyCu = if(hasOncologyCu.value) 1 else null,
                                oncologyFreeBedsStart = minFreeOncologyCuBeds.value.toIntOrNull(),
                                oncologyFreeBedsEnd = maxFreeOncologyCuBeds.value.toIntOrNull(),

                                hasBurnCu = if(hasBurnsCu.value) 1 else null,
                                burnsFreeBedsStart = minFreeBurnsCuBeds.value.toIntOrNull(),
                                burnsFreeBedsEnd = maxFreeBurnsCuBeds.value.toIntOrNull(),

                                hasMorgue = if(hasMorgue.value) 1 else null,
                                morgueFreeBedsStart = minFreeMorgueBeds.value.toIntOrNull(),
                                morgueFreeBedsEnd = maxFreeMorgueBeds.value.toIntOrNull(),

                                hasRenalWards = if(hasRenalWards.value) 1 else null,
                                renalFreeBedsStart = minFreeRenalBeds.value.toIntOrNull(),
                                renalFreeBedsEnd = maxFreeRenalBeds.value.toIntOrNull(),

                                hasWards = if(hasWards.value) 1 else null,
                                wardsFreeBedsStart = minFreeBeds.value.toIntOrNull(),
                                wardsFreeBedsEnd = maxFreeBeds.value.toIntOrNull(),

                                hasDevices = if(hasDevices.value) 1 else null,
                                deviceStatusId = selectedDeviceStatus.value?.id,
                                deviceDepartmentId = selectedDepartment.value?.id,
                            )
                            hospitalController.filter(filterBody = body)

                        }
                    }
                    Box(modifier = Modifier.padding(5.dp)){
                        IconButton(icon = R.drawable.ic_cancel_red, background = colorResource(R.color.white)) {showDialog.value=false}
                    }
                }
                Row{
                    Label(HOSPITALS_LABEL)
                    HorizontalSpacer()
                    if(selectedCity.value!=null){
                        Label(IN_CITY_LABEL)
                        HorizontalSpacer()
                        Label(selectedCity.value?.name?:EMPTY_STRING)
                    }
                    HorizontalSpacer()
                    if(selectedArea.value!=null){
                        Label(IN_AREA_LABEL)
                        HorizontalSpacer()
                        Label(selectedArea.value?.name?:EMPTY_STRING)
                    }
                }
                Row{
                    if(selectedCategory.value!=null){
                        Label(OF_TYPE_LABEL)
                        HorizontalSpacer()
                        Label(selectedCategory.value?.name?:EMPTY_STRING)
                    }
                    HorizontalSpacer()
                    if(selectedType.value!=null){
                        Label(FOLLOWS_LABEL)
                        HorizontalSpacer()
                        Label(selectedType.value?.name?:EMPTY_STRING)
                    }
                }
                VerticalSpacer()
                LazyColumn (modifier = Modifier.fillMaxWidth().padding(5.dp).weight(1f)) {
                    item{
                        VerticalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                ComboBox(title = CITY_LABEL, loadedItems = cities, selectedItem = selectedCity, selectedContent = {
                                    CustomInput(selectedCity.value?.name?: SELECT_CITY_LABEL)
                                }) {
                                    Label(it?.name?:EMPTY_STRING)
                                }

                            }
                            Column{
                                VerticalSpacer(15)
                                IconButton(icon=R.drawable.ic_cancel_red) {
                                    selectedCity.value=null
                                }
                            }
                        }
                        VerticalSpacer()
                        if(selectedCity.value!=null && areas.isNotEmpty()){
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                    ComboBox(title = AREA_LABEL, loadedItems = areas, selectedItem = selectedArea, selectedContent = {
                                        CustomInput(selectedArea.value?.name?: SELECT_AREA_LABEL)
                                    }) {
                                        Label(it?.name?:EMPTY_STRING)
                                    }
                                }
                                Column{
                                    VerticalSpacer(15)
                                    IconButton(icon=R.drawable.ic_cancel_red) {
                                        selectedArea.value=null
                                    }
                                }
                            }

                        }
                        VerticalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                ComboBox(title = SECTOR_LABEL, loadedItems = sectors, selectedItem = selectedCategory, selectedContent = {
                                    CustomInput(selectedCategory.value?.name?: SELECT_SECTOR_LABEL)
                                }) {
                                    Label(it?.name?:EMPTY_STRING)
                                }

                            }
                            Column {
                                VerticalSpacer(15)
                                IconButton(icon=R.drawable.ic_cancel_red) {
                                    selectedCategory.value=null
                                }
                            }
                        }
                        VerticalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                ComboBox(title = HOSPITAL_TYPE_LABEL, loadedItems = types, selectedItem = selectedType, selectedContent = {
                                    CustomInput(selectedType.value?.name?: SELECT_TYPE_LABEL)
                                }) {
                                    Label(it?.name?:EMPTY_STRING)
                                }
                            }
                            Column {
                                VerticalSpacer(15)
                                IconButton(icon=R.drawable.ic_cancel_red) {
                                    selectedType.value=null
                                }
                            }
                        }
                        VerticalSpacer()
                        HorizontalDivider()
                        VerticalSpacer()
                        Row(modifier=Modifier.clickable { showDetails=!showDetails }.fillMaxWidth()
                            .background(color= colorResource(R.color.teal_700)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Row{
                                HorizontalSpacer()
                                Span(text = DETAILED_FILTER_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                            }
                            Icon(if(showDetails) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                        }
                        AnimatedVisibility(visible = showDetails) {
                            Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                                .border(width = 1.dp, shape = rcsB(20), color = Color.LightGray)) {
                                CustomCheckbox(label = HAS_ICU_LABEL,active=hasIcu)
                                if(hasIcu.value){
                                    AnimatedVisibility(visible = hasIcu.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeIcuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeIcuBeds.value=t else minFreeIcuBeds.value = EMPTY_STRING}
                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeIcuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeIcuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeIcuBeds.value=t else maxFreeIcuBeds.value=EMPTY_STRING}
                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeIcuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }

                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_CCU_LABEL,active=hasCCU)
                                if(hasCCU.value){
                                    AnimatedVisibility(visible = hasCCU.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeIcuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeIcuBeds.value=t else minFreeIcuBeds.value=EMPTY_STRING}
                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeCcuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeCcuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeCcuBeds.value=t else maxFreeCcuBeds.value=EMPTY_STRING}
                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeCcuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }

                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_NICU_LABEL,active=hasNicu)
                                if(hasNicu.value){
                                    AnimatedVisibility(visible = hasNicu.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeNicuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeNicuBeds.value=t else minFreeNicuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeNicuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeNicuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeNicuBeds.value=t else maxFreeNicuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeNicuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }

                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_ONCOLOGY_CU_LABEL,active=hasOncologyCu)
                                if(hasOncologyCu.value){
                                    AnimatedVisibility(visible = hasOncologyCu.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeOncologyCuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeOncologyCuBeds.value=t else minFreeOncologyCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeOncologyCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeOncologyCuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeOncologyCuBeds.value=t else maxFreeOncologyCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeOncologyCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }

                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_BURNS_CU_LABEL,active=hasBurnsCu)
                                if(hasBurnsCu.value){
                                    AnimatedVisibility(visible = hasBurnsCu.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeBurnsCuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeBurnsCuBeds.value=t else minFreeBurnsCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeBurnsCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeBurnsCuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeBurnsCuBeds.value=t else maxFreeBurnsCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeBurnsCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }

                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_NEUROLOGY_CU_LABEL,active=hasNeurologyCu)
                                if(hasNeurologyCu.value){
                                    AnimatedVisibility(visible = hasNeurologyCu.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeNeurologyCuBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeNeurologyCuBeds.value=t else minFreeNeurologyCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeNeurologyCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeNeurologyCuBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeNeurologyCuBeds.value=t else maxFreeNeurologyCuBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeNeurologyCuBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_MORGUE_LABEL,active=hasMorgue)
                                if(hasMorgue.value){
                                    AnimatedVisibility(visible = hasMorgue.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeMorgueBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeMorgueBeds.value=t else minFreeMorgueBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeMorgueBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeMorgueBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeMorgueBeds.value=t else maxFreeMorgueBeds.value=EMPTY_STRING}


                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeMorgueBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer(10)

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_RENAL_DEVICES_LABEL,active=hasRenalWards)
                                if(hasRenalWards.value){
                                    AnimatedVisibility(visible = hasRenalWards.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeRenalBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeRenalBeds.value=t else minFreeRenalBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeRenalBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeRenalBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeRenalBeds.value=t else maxFreeRenalBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeRenalBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_WARDS_LABEL,active=hasWards)
                                if(hasWards.value){
                                    AnimatedVisibility(visible = hasWards.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = minFreeBeds,
                                                        label = MIN_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)minFreeBeds.value=t else minFreeBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    minFreeBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(horizontal = 5.dp)){
                                                    CustomInput(value = maxFreeBeds,
                                                        label = MAX_FREE_BEDS_LABEL,
                                                        keyboardOptions = numericKeyBoard,enabled=true,
                                                        onTextChange = {t->if(t.toIntOrNull()!=null)maxFreeBeds.value=t else maxFreeBeds.value=EMPTY_STRING}

                                                    )
                                                }
                                                IconButton(icon = R.drawable.ic_cancel_red) {
                                                    maxFreeBeds.value="0"
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_DEVICES_LABEL,active=hasDevices)
                                if(hasDevices.value){
                                    AnimatedVisibility(visible = hasDevices.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                                    ComboBox(title = DEVICE_STATUS_LABEL, loadedItems = deviceStatuses, selectedItem = selectedDeviceStatus, selectedContent = {
                                                        CustomInput(selectedDeviceStatus.value?.name?: SELECT_DEVICE_STATUS_LABEL)
                                                    }) {
                                                        Label(it?.name?:EMPTY_STRING)
                                                    }
                                                }
                                                Column {
                                                    VerticalSpacer(15)
                                                    IconButton(icon=R.drawable.ic_cancel_red) {
                                                        selectedDeviceStatus.value=null
                                                    }
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)

                                CustomCheckbox(label = HAS_DEPARTMENTS_LABEL,active=hasDepartments)
                                if(hasDepartments.value){
                                    AnimatedVisibility(visible = hasDepartments.value) {
                                        Column {
                                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier = Modifier.fillMaxWidth().weight(1f)){
                                                    ComboBox(title = DEPARTMENT_LABEL, loadedItems = basicDepartments, selectedItem = selectedDepartment, selectedContent = {
                                                        CustomInput(selectedDeviceStatus.value?.name?: SELECT_DEPARTMENT_LABEL)
                                                    }) {
                                                        Label(it?.name?:EMPTY_STRING)
                                                    }
                                                }
                                                Column {
                                                    VerticalSpacer(15)
                                                    IconButton(icon=R.drawable.ic_cancel_red) {
                                                        selectedDepartment.value=null
                                                    }
                                                }
                                            }
                                            VerticalSpacer()
                                        }
                                    }
                                }
                                VerticalSpacer()
                                HorizontalDivider()

                                VerticalSpacer(10)
                            }
                        }
                    }
                }
            }
        }
    }
}

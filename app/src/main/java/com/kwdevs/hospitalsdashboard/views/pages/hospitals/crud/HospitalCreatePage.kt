package com.kwdevs.hospitalsdashboard.views.pages.hospitals.crud

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalDepartmentBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalIcuBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.InPatientWardBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.MorgueBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalDeviceBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.controller.AreaController
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.controller.hospital.RenalDevicesController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.IntensiveCare
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartment
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartmentResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BACK_TO_HOSPITALS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BACK_TO_MAIN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CHANGES_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CREATE_NEW_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DETAILED_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FREE_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_BURNS_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_CCU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ICU_DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NEUROLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ONCOLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_RENAL_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_WARDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_ADDRESS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MAIN_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PREVIEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_DEVICE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.HospitalCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices.RenalDeviceCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.WardCard
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB

@Composable
fun HospitalCreatePage(navHostController: NavHostController){
    //val context= LocalContext.current

    val controller : HospitalController= viewModel()
    val state by controller.singleState.observeAsState()
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    val item = remember { mutableStateOf<Hospital?>(null) }
    var errorMessage by remember { mutableStateOf<String?>("") }
    var errorsList by remember { mutableStateOf<Map<String,List<String>>?>(emptyMap()) }
    val accountType=Preferences.User().getType()
    val savedSuper=Preferences.User().getSuper()
    val user=Preferences.User().get()
    val simpleUser= user?.let{
        SimpleHospitalUser(id=it.id,hospital=it.hospital, hospitalId = it.hospitalId,name=it.name,title=it.title,active=it.active)
    }
    val simpleSuper= savedSuper?.let{
        SimpleSuperUser(id=it.id,name=it.name,title=it.title,titleId=it.titleId,active=it.active,isSuper=it.isSuper)
    }
    val accountTypeOrdinal=if(accountType==ViewType.SUPER_USER && simpleSuper!=null) { 1 }  else if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) {2} else 0
    val name = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(true) }
    val defaultCity = remember { mutableStateOf<CityWithCount?>(null) }
    val selectedCity = remember { mutableStateOf<CityWithCount?>(null) }
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }

    var hasName by remember { mutableStateOf(false) }
    var hasAddress by remember { mutableStateOf(false) }
    var hasCity by remember { mutableStateOf(false) }
    var hasArea by remember { mutableStateOf(false) }
    var hasSector by remember { mutableStateOf(false) }
    var hasType by remember { mutableStateOf(false) }
    val areaController: AreaController= viewModel()
    val areaState by areaController.state.observeAsState()
    var areas by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }

    var loadingAreas by remember { mutableStateOf(true) }
    var successAreas by remember { mutableStateOf(false) }
    var failAreas by remember { mutableStateOf(false) }
    val selectedArea = remember { mutableStateOf<AreaWithCount?>(null)}

    val currentBody = remember { mutableStateOf<HospitalBody?>(null) }
    var showMainData by remember { mutableStateOf(true) }
    var showDetails by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    val hasIcuDepartment = remember { mutableStateOf(false) }

    val hasIcu = remember { mutableStateOf(false) }
    val allIcuBeds = remember { mutableStateOf("0") }
    val freeIcuBeds = remember { mutableStateOf("0") }

    val hasCCU = remember { mutableStateOf(false) }
    val allCcuBeds = remember { mutableStateOf("0") }
    val freeCcuBeds = remember { mutableStateOf("0") }

    val hasNicu = remember { mutableStateOf(false) }
    val allNicuBeds = remember { mutableStateOf("0") }
    val freeNicuBeds = remember { mutableStateOf("0") }

    val hasOncologyCu = remember { mutableStateOf(false) }
    val allOncologyCuBeds = remember { mutableStateOf("0") }
    val freeOncologyCuBeds = remember { mutableStateOf("0") }

    val hasBurnsCu = remember { mutableStateOf(false) }
    val allBurnsCuBeds = remember { mutableStateOf("0") }
    val freeBurnsCuBeds = remember { mutableStateOf("0") }

    val hasNeurologyCu = remember { mutableStateOf(false) }
    val allNeurologyCuBeds = remember { mutableStateOf("0") }
    val freeNeurologyCuBeds = remember { mutableStateOf("0") }

    val hasMorgue = remember { mutableStateOf(false) }
    val allMorgueBeds = remember { mutableStateOf("0") }
    val freeMorgueBeds = remember { mutableStateOf("0") }

    val renalDevices = remember { mutableStateOf<List<HospitalRenalDevice>>(emptyList()) }
    val hasRenalDevices = remember { mutableStateOf(false) }

    val wards = remember { mutableStateOf<List<HospitalWard>>(emptyList()) }
    val hasWards = remember { mutableStateOf(false) }

    val homeController: HomeController= viewModel()
    val settingsController: SettingsController= viewModel()
    val departmentsState by settingsController.basicDepartmentState.observeAsState()

    val hasDepartments = remember { mutableStateOf(false) }
    val hospitalDepartments = remember { mutableStateOf<List<HospitalDepartment>>(emptyList()) }
    var basicDepartments by remember { mutableStateOf<List<BasicDepartment>>(emptyList()) }
    val homeState by homeController.singleState.observeAsState()
    //val defaultDepartment = remember { mutableStateOf<BasicDepartment?>(null) }
    //val selectedDepartment = remember { mutableStateOf<BasicDepartment?>(null) }
    var loadingHome by remember { mutableStateOf(true) }
    var successHome by remember { mutableStateOf(false) }
    var failHome by remember { mutableStateOf(false) }

    var hospitalTypes by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    val selectedHospitalType = remember { mutableStateOf<HospitalType?>(null) }
    val defaultHospitalType = remember { mutableStateOf<HospitalType?>(null) }

    var sectors by remember { mutableStateOf<List<Sector>>(emptyList()) }
    val selectedSector = remember { mutableStateOf<Sector?>(null) }
    val defaultSector = remember { mutableStateOf<Sector?>(null) }

    val hospitalModel = remember { mutableStateOf<Hospital?>(null) }
    val hospitalBody = remember { mutableStateOf<HospitalBody?>(null) }
    val showSavePromptDialog = remember { mutableStateOf(false) }
    var showMorgueSection by remember { mutableStateOf(false) }
    var showRenalSection by remember { mutableStateOf(false) }
    var showInpatientSection by remember { mutableStateOf(false) }
    var showDepartmentSection by remember { mutableStateOf(false) }
    //TODO Launched Effects -------- start
    LaunchedEffect(defaultCity.value) {
        if(defaultCity.value!=null) selectedCity.value=defaultCity.value
    }
    LaunchedEffect(defaultHospitalType.value) {
        if(defaultHospitalType.value!=null) selectedHospitalType.value=defaultHospitalType.value
    }
    LaunchedEffect(defaultSector.value) {
        if(defaultSector.value!=null) selectedSector.value=defaultSector.value
    }
    LaunchedEffect(selectedCity.value) {
        hasCity=selectedCity.value!=null
        if(selectedCity.value!=null) areaController.index(selectedCity.value?.id?:0)
    }
    LaunchedEffect(selectedArea.value) { hasArea=selectedArea.value!=null }
    LaunchedEffect(selectedHospitalType.value) { hasType=selectedHospitalType.value!=null }
    LaunchedEffect(selectedSector.value) { hasSector=selectedSector.value!=null }
    LaunchedEffect(name.value) { hasName=name.value!="" && name.value.length>3 }
    LaunchedEffect(address.value) {hasAddress=address.value!="" && address.value.length>10 }
    //TODO Launched Effects -------- end


    //TODO States -------- start
    when(areaState){
        is UiState.Loading->{
            loadingAreas=true;failAreas=false;successAreas=false
        }
        is UiState.Error->{loadingAreas=false;failAreas=true;successAreas=false}
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
    when(homeState){
        is UiState.Loading->{loadingHome=true;failHome=false;successHome=false}
        is UiState.Error->{loadingHome=false;failHome=true;successHome=false}
        is UiState.Success->{
            loadingHome=false;failHome=false;successHome=true
            val s = homeState as UiState.Success<HomeResponse>
            val response =s.data
            val data=response.data
            sectors=data.sectors
            hospitalTypes=data.types
            cities=data.cities
        }
        else->{homeController.getHome()}
    }
    when(departmentsState){
        is UiState.Success->{
            val s = departmentsState as UiState.Success<BasicDepartmentResponse>
            val response = s.data
            val data=response.data
            basicDepartments=data
        }
        else->{settingsController.basicDepartmentsIndex()}
    }
    when(state){
        is UiState.Loading->{ loading=true;fail=false;success=false }
        is UiState.Error->{
            loading=false;fail=true;success=false
            val s = state as UiState.Error
            val exception=s.exception
            errorsList=exception.errors
            errorMessage=exception.message
        }
        is UiState.Success->{
            loading=false;fail=false;success=true
            val s = state as UiState.Success<HospitalSingleResponse>
            val response = s.data
            val data=response.data
            item.value=data
        }
        else->{ loading=false;fail=false;success=false }
    }

    //TODO States -------- end

    //TODO Composable -------- start

    SaveHospitalDialog(
        showDialog =  showSavePromptDialog,
        model= hospitalModel.value,
        navHostController = navHostController) {
        val body=hospitalBody.value
        body?.let{ controller.store(body) }
        showSavePromptDialog.value=false
    }
    Column(modifier=Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.teal_700), shape = rcsB(20)),
            contentAlignment = Alignment.Center){
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Label(CREATE_NEW_HOSPITAL_LABEL, fontSize = 26,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White)
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(icon=R.drawable.ic_arrow_back_white, background = colorResource(R.color.teal_700)) {
                    navHostController.navigate(HospitalsIndexRoute.route)
                }
            }
        }
        VerticalSpacer(10)
        if(!loading && !fail && !success){
            LazyColumn(modifier= Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(5.dp)
                .shadow(elevation = 5.dp, shape = rcs(10))
                .background(color = Color.White, shape = rcs(10))
                .border(width = 1.dp, shape = rcs(10), color = Color.Gray)) {
                item{
                    //TODO Preview start-----------------------------
                    Row(modifier= Modifier
                        .clickable { showPreview = !showPreview }
                        .fillMaxWidth()
                        .background(color = colorResource(R.color.teal_700)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row{
                            HorizontalSpacer()
                            Span(text = PREVIEW_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                        }
                        Icon(if(showPreview) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                    }
                    AnimatedVisibility(visible = showPreview){
                        Column(modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .shadow(elevation = 5.dp, shape = rcsB(20))
                            .background(color = Color.White, shape = rcsB(20))
                            .border(
                                width = 1.dp, shape = rcsB(20),
                                color = colorResource(R.color.teal_700)
                            )
                            .padding(horizontal = 5.dp)
                        ){

                            if(name.value!=""){
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= NAME_LABEL,text=name.value)

                                VerticalSpacer()
                            }
                            if(selectedCity.value!=null || selectedArea.value!=null){
                                Row{
                                    if(selectedCity.value!=null){Label(icon=R.drawable.ic_info_white,
                                        iconBackground = colorResource(R.color.teal_700),
                                        label= CITY_LABEL,text=selectedCity.value?.name?:"")}
                                    HorizontalSpacer()
                                    if(selectedArea.value!=null){
                                        Label(icon=R.drawable.ic_info_white,
                                            iconBackground = colorResource(R.color.teal_700),
                                            label= AREA_LABEL,text=selectedArea.value?.name?:"")
                                    }

                                }
                                VerticalSpacer()
                            }
                            if(address.value!=""){
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= HOSPITAL_ADDRESS_LABEL,text=address.value)

                                VerticalSpacer()
                            }
                            if(selectedHospitalType.value!=null){
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= HOSPITAL_TYPE_LABEL,text=selectedHospitalType.value?.name?:"")

                                VerticalSpacer()
                            }
                            if(selectedSector.value!=null){
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= SECTOR_LABEL,text=selectedSector.value?.name?:"")
                                VerticalSpacer()
                            }
                            HorizontalDivider()
                            Box(modifier=Modifier.padding(5.dp)){
                                Label(DETAILED_DATA_LABEL)
                            }
                            if(hasIcu.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text=HAS_ICU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allIcuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeIcuBeds.value)
                                }
                                VerticalSpacer()
                            }
                            if(hasCCU.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text=HAS_CCU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allCcuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeCcuBeds.value)
                                }
                                HorizontalDivider()

                                VerticalSpacer()
                            }
                            if(hasNicu.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text=HAS_NICU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allNicuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeNicuBeds.value)
                                }
                                HorizontalDivider()

                                VerticalSpacer()
                            }
                            if(hasOncologyCu.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_ONCOLOGY_CU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allOncologyCuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeOncologyCuBeds.value)
                                }
                                HorizontalDivider()

                                VerticalSpacer()
                            }
                            if(hasBurnsCu.value){
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .shadow(elevation = 5.dp, shape = rcs(20))
                                    .background(color = Color.White, shape = rcs(20))
                                    .border(width = 1.dp, color = Color.White, shape = rcs(20))){}
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_BURNS_CU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allBurnsCuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeBurnsCuBeds.value)
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                            if(hasNeurologyCu.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_NEUROLOGY_CU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allNeurologyCuBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeNeurologyCuBeds.value)
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                            if(hasMorgue.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_MORGUE_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= allMorgueBeds.value)
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= freeMorgueBeds.value)
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                            if(hasRenalDevices.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(HAS_RENAL_DEVICES_LABEL)
                                }
                                renalDevices.value.forEach { item->
                                    val renalDevicesController: RenalDevicesController = viewModel()
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier= Modifier
                                            .fillMaxWidth()
                                            .weight(1f)) {
                                            RenalDeviceCard(item,renalDevicesController)
                                        }
                                        HorizontalSpacer()
                                        IconButton(icon=R.drawable.ic_delete_red, containerSize = 26) {
                                            renalDevices.value=renalDevices.value.filter { it!=item }
                                        }
                                    }
                                    VerticalSpacer()
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                            if(hasWards.value){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(HAS_WARDS_LABEL)
                                }
                                wards.value.forEach { ward->
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier= Modifier
                                            .fillMaxWidth()
                                            .weight(1f)){
                                            WardCard(ward,navHostController)
                                        }
                                        HorizontalSpacer()
                                        IconButton(icon=R.drawable.ic_delete_red, containerSize = 26) {
                                            wards.value=wards.value.filter { it!=ward }
                                        }
                                    }
                                    VerticalSpacer()
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                            if(hasDepartments.value){
                                Label(icon=R.drawable.ic_check_circle_green,
                                    iconBackground = colorResource(R.color.white),
                                    label = DEPARTMENTS_LABEL, text = hospitalDepartments.value.size.toString())
                                hospitalDepartments.value.forEach { department->
                                    Row(modifier= Modifier.padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        Span(text = department.name, color = Color.White, backgroundColor = colorResource(R.color.teal_700))
                                        IconButton(icon=R.drawable.ic_delete_red, containerSize = 26) {
                                            hospitalDepartments.value=hospitalDepartments.value.filter { it!=department }
                                        }
                                    }
                                }
                                HorizontalDivider()
                                VerticalSpacer()
                            }
                        }
                    }

                    //TODO Preview end-----------------------------

                    VerticalSpacer()

                    //TODO Main Data start-----------------------------
                    Row(modifier= Modifier
                        .clickable { showMainData = !showMainData }
                        .fillMaxWidth()
                        .background(color = colorResource(R.color.teal_700)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row{
                            HorizontalSpacer()
                            Span(text = MAIN_DATA_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                        }
                        Icon(if(showMainData) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                    }
                    AnimatedVisibility(visible = showMainData) {
                        Column(modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .border(width = 1.dp, shape = rcsB(20), color = Color.LightGray)) {
                            HospitalNameInput(hasName,name)
                            HospitalAddressInput(hasAddress,address)
                            CitySelector(hasCity,cities,selectedCity,defaultCity)
                            AreaSelector(hasArea,areas,selectedArea)
                            SectorSelector(hasSector,sectors,selectedSector,defaultSector)
                            TypeSelector(hasType,hospitalTypes,selectedHospitalType,defaultHospitalType)
                            CustomCheckbox(label = ACTIVE_LABEL,active=active)
                            VerticalSpacer()
                        }
                    }

                    //TODO Main Data end-----------------------------

                    VerticalSpacer()

                    //TODO Details Data start-----------------------------

                    Row(modifier= Modifier
                        .clickable { showDetails = !showDetails }
                        .fillMaxWidth()
                        .background(color = colorResource(R.color.teal_700)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row{
                            HorizontalSpacer()
                            Span(text = DETAILED_DATA_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                        }
                        Icon(if(showDetails) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                    }
                    AnimatedVisibility(visible = showDetails) {
                        Column(modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .border(width = 1.dp, shape = rcsB(20), color = Color.LightGray)) {
                            CustomCheckbox(label = HAS_ICU_DEPARTMENTS_LABEL,active = hasIcuDepartment)
                            if(hasIcuDepartment.value){
                                Column(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .border(
                                        width = 1.dp, shape = rcs(20),
                                        color = colorResource(R.color.teal_700)
                                    )){
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasIcu, HAS_ICU_LABEL,allIcuBeds,freeIcuBeds)
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasCCU, HAS_CCU_LABEL,allCcuBeds,freeCcuBeds)
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasNicu, HAS_NICU_LABEL,allNicuBeds,freeNicuBeds)
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasOncologyCu, HAS_ONCOLOGY_CU_LABEL,allOncologyCuBeds,freeOncologyCuBeds)
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasBurnsCu, HAS_BURNS_CU_LABEL,allBurnsCuBeds,freeBurnsCuBeds)
                                    VerticalSpacer(10)
                                    SpecialWardInput(hasNeurologyCu, HAS_NEUROLOGY_CU_LABEL,allNeurologyCuBeds,freeNeurologyCuBeds)
                                    VerticalSpacer(10)
                                }
                            }
                            HorizontalDivider()
                            VerticalSpacer(10)

                            Row(modifier= Modifier
                                .clickable { showMorgueSection = !showMorgueSection }
                                .fillMaxWidth()
                                .background(color = colorResource(R.color.teal_700)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Row{
                                    HorizontalSpacer()
                                    Span(text = MORGUE_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                                }
                                Icon(if(showMorgueSection) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                            }
                            if(showMorgueSection){
                                SpecialWardInput(hasMorgue,HAS_MORGUE_LABEL,allMorgueBeds,freeMorgueBeds)
                                HorizontalDivider()
                            }
                            Row(modifier= Modifier
                                .clickable { showRenalSection = !showRenalSection }
                                .fillMaxWidth()
                                .background(color = colorResource(R.color.teal_700)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Row{
                                    HorizontalSpacer()
                                    Span(text = RENAL_DEVICE_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                                }
                                Icon(if(showRenalSection) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                            }
                            if(showRenalSection){
                                RenalDevicesInput(hasRenalDevices,renalDevices)
                                HorizontalDivider()
                            }
                            Row(modifier= Modifier
                                .clickable { showInpatientSection = !showInpatientSection }
                                .fillMaxWidth()
                                .background(color = colorResource(R.color.teal_700)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Row{
                                    HorizontalSpacer()
                                    Span(text = IN_PATIENT_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                                }
                                Icon(if(showInpatientSection) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                            }
                            if(showInpatientSection){
                                InPatientWardsInput(hasWards,wards)
                                HorizontalDivider()
                            }
                            Row(modifier= Modifier
                                .clickable { showDepartmentSection = !showDepartmentSection }
                                .fillMaxWidth()
                                .background(color = colorResource(R.color.teal_700)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Row{
                                    HorizontalSpacer()
                                    Span(text = DEPARTMENTS_LABEL, color = Color.Black, backgroundColor = colorResource(R.color.teal_200))
                                }
                                Icon(if(showDepartmentSection) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = colorResource(R.color.teal_700))
                            }
                            if(showDepartmentSection){
                                HospitalDepartmentsInput(hasDepartments,basicDepartments,hospitalDepartments)
                                HorizontalDivider()
                            }


                        }
                    }

                    //TODO Details Data end-----------------------------

                    VerticalSpacer(10)

                }
            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.green),
                        contentColor = Color.White
                    ),
                    onClick =    {
                        hasName=name.value.trim() != ""
                        hasAddress = address.value.trim() !=""
                        hasCity=selectedCity.value!=null
                        hasArea=selectedArea.value!=null
                        hasSector= selectedSector.value!=null
                        hasType=selectedHospitalType.value!=null
                        val allDataValid=hasName && hasAddress && hasCity && hasArea && hasSector && hasType
                        if(allDataValid){

                            val icuBody= HospitalIcuBody(
                                hasIcu=if(hasIcu.value) 1 else 0,
                                allIcuBeds = if(allIcuBeds.value!="") {allIcuBeds.value.toInt()} else 0,
                                freeIcuBeds = if(freeIcuBeds.value!="") {freeIcuBeds.value.toInt()} else 0,
                                hasCcu = if(hasCCU.value) 1 else 0,
                                allCcuBeds = if(hasCCU.value) allCcuBeds.value.toIntOrNull() else null,
                                freeCcuBeds = if(hasCCU.value) freeCcuBeds.value.toIntOrNull() else null,
                                hasNicu = if(hasNicu.value) 1 else 0,
                                allNicuBeds = if(hasCCU.value) allNicuBeds.value.toIntOrNull() else null,
                                freeNicuBeds = if(hasCCU.value) freeNicuBeds.value.toIntOrNull() else null,
                                hasOncologyCu = if(hasOncologyCu.value) 1 else 0,
                                allOncologyCuBeds = if(hasCCU.value) allOncologyCuBeds.value.toIntOrNull() else null,
                                freeOncologyCuBeds = if(hasCCU.value) freeOncologyCuBeds.value.toIntOrNull() else null,
                                hasNeurologyCu = if(hasNeurologyCu.value) 1 else 0,
                                allNeurologyCuBeds = if(hasCCU.value) allNeurologyCuBeds.value.toIntOrNull() else null,
                                freeNeurologyCuBeds = if(hasCCU.value) freeNeurologyCuBeds.value.toIntOrNull() else null,
                                hasBurnsCu = if(hasBurnsCu.value) 1 else 0,
                                allBurnsCuBeds = if(hasCCU.value) allBurnsCuBeds.value.toIntOrNull() else null,
                                freeBurnsCuBeds = if(hasCCU.value) freeBurnsCuBeds.value.toIntOrNull() else null,

                                updatedById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                updatedBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null,
                                accountType = accountTypeOrdinal,

                                )
                            val icuModel=IntensiveCare(
                                hasIcu=hasIcu.value,
                                allIcuBeds = allIcuBeds.value.toIntOrNull(),
                                freeIcuBeds = freeIcuBeds.value.toIntOrNull(),

                                hasCcu = hasCCU.value,
                                allCcuBeds = allCcuBeds.value.toIntOrNull(),
                                freeCcuBeds = freeCcuBeds.value.toIntOrNull(),

                                hasNicu = hasNicu.value,
                                allNicuBeds = allNicuBeds.value.toIntOrNull(),
                                freeNicuBeds = freeNicuBeds.value.toIntOrNull(),

                                hasNeuroCu = hasNeurologyCu.value,
                                allNeurologyCareUnitBeds = allNeurologyCuBeds.value.toIntOrNull(),
                                freeNeurologyCareUnitBeds = freeNeurologyCuBeds.value.toIntOrNull(),

                                hasOncologyCareUnit = hasOncologyCu.value,
                                allOncologyCareUnitBeds = allOncologyCuBeds.value.toIntOrNull(),
                                freeOncologyCareUnitBeds = freeOncologyCuBeds.value.toIntOrNull(),

                                hasBurnCareUnit = hasBurnsCu.value,
                                allBurnCareUnitBeds = allBurnsCuBeds.value.toIntOrNull(),
                                freeBurnCareUnitBeds = freeBurnsCuBeds.value.toIntOrNull(),
                            )

                            val hdpList= mutableListOf<HospitalDepartmentBody>()
                            hospitalDepartments.value.forEach {hospitalDp->
                                val hospitalDepartmentBody= HospitalDepartmentBody(
                                    departmentBid = hospitalDp.basicDepartmentId,
                                    name = hospitalDp.name,
                                    active = if(hospitalDp.active) 1 else 0,
                                    createdById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                    createdBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null,
                                    accountType = accountTypeOrdinal,
                                )
                                hdpList.add(hospitalDepartmentBody)
                            }
                            val wardsBodyList= mutableListOf<InPatientWardBody>()
                            wards.value.forEach { ward->
                                val body = InPatientWardBody(
                                    name=ward.name,
                                    allBeds = ward.allBeds,
                                    freeBeds = ward.freeBeds,
                                    active = if(ward.active) 1 else 0,
                                    createdById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                    createdBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null,
                                    accountType = accountTypeOrdinal,
                                )
                                wardsBodyList.add(body)
                            }
                            val renalWardsBodyList= mutableListOf<RenalDeviceBody>()
                            renalDevices.value.forEach { ward->
                                val body = RenalDeviceBody(
                                    name=ward.name,
                                    deviceTypeId = ward.typeId,
                                    active = if(ward.active) 1 else 0,
                                    createdById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                    createdBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null,
                                )
                                renalWardsBodyList.add(body)
                            }

                            val morgueBody= MorgueBody(
                                statusId  = if(hasMorgue.value) 1 else 2,
                                allUnits  = allMorgueBeds.value.toIntOrNull(),
                                createdById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                createdBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null
                                )
                            val morgueModel= Morgue(
                                statusId = if(hasMorgue.value) 1 else 2,
                                allUnits  = allMorgueBeds.value.toIntOrNull(),
                                freeUnits = freeMorgueBeds.value.toIntOrNull(),
                            )
                            val body = HospitalBody(
                                name = name.value,
                                address = address.value,
                                active = if(active.value) 1 else 0,
                                cityId = selectedCity.value?.id,
                                areaId = selectedArea.value?.id,
                                sectorId = selectedSector.value?.id,
                                typeId = selectedHospitalType.value?.id,
                                icu = if(hasIcuDepartment.value) icuBody else null,
                                morgue = if(hasMorgue.value) morgueBody else null,
                                departments = hdpList.ifEmpty { null },
                                inpatientWards = wardsBodyList.ifEmpty { null },
                                renalWards = renalWardsBodyList.ifEmpty { null },
                                createdById = if(accountType==ViewType.HOSPITAL_USER && simpleUser!=null) simpleUser.id else null,
                                createdBySuperId = if(accountType==ViewType.SUPER_USER && simpleSuper!=null) simpleSuper.id else null,
                                accountType = accountTypeOrdinal,
                            )
                            currentBody.value=body
                            val editedHospital= Hospital(
                                id=0,
                                name=name.value,
                                address=address.value,
                                active=active.value,
                                cityId=selectedCity.value!!.id,
                                city=selectedCity.value!!,
                                areaId=selectedArea.value!!.id,
                                area=selectedArea.value!!,
                                sectorId=selectedSector.value!!.id,
                                sector=selectedSector.value!!,
                                typeId=selectedHospitalType.value!!.id,
                                type=selectedHospitalType.value!!,
                                icu=if(hasIcuDepartment.value) icuModel else null,
                                departments = if(hasDepartments.value) hospitalDepartments.value else null,
                                wards=if(hasWards.value) wards.value else null,
                                renalDevices = if(hasRenalDevices.value) renalDevices.value else null,
                                morgue = if(hasMorgue.value) morgueModel else null,
                                longitude = null,
                                latitude = null,
                            )
                            hospitalModel.value=editedHospital
                            hospitalBody.value=body
                            showSavePromptDialog.value=true
                        }
                    },
                    shape = rcs(20)) {
                    Label(SAVE_CHANGES_LABEL, color = Color.White)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.red),
                        contentColor = Color.White
                    ),
                    onClick = {
                        navHostController.navigate(HospitalsIndexRoute.route)
                    },
                    shape = rcs(20)) {
                    Label(CANCEL_LABEL, color = Color.White)
                }

            }
        }
        else{
            if(loading) LoadingScreen(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp))
            if(fail){
                if(accountType==ViewType.SUPER_USER){
                    if(simpleSuper!=null && simpleSuper.isSuper){
                        FailScreen(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp),
                            label=ERROR_CREATING_DATA_LABEL,
                            errors=errorsList,
                            message=errorMessage)
                    }
                    else{
                        FailScreen(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp),
                            label=ERROR_CREATING_DATA_LABEL)
                    }
                }
                else{
                    FailScreen(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp),
                        label=ERROR_CREATING_DATA_LABEL)
                }
            }
            if(success){
                SuccessScreen(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp)
                    .border(width=1.dp,color= colorResource(R.color.teal_700), shape = rcs(20))) {
                    Label(CHANGES_SAVED_LABEL, fontSize = 30, color = colorResource(R.color.teal_700))
                    VerticalSpacer()
                    if(item.value!=null){
                        val h=item.value
                        h?.let{
                            Box(modifier=Modifier.padding(5.dp)){
                                HospitalCard(h,navHostController)
                            }
                        }
                    }
                    VerticalSpacer()
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround){
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.green),
                                    contentColor = colorResource(R.color.white)
                                ),
                                onClick = {controller.reload()}) {
                                Label(ADD_NEW_LABEL, color = colorResource(R.color.white))
                            }

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.blue3),
                                    contentColor = colorResource(R.color.white)
                                ),
                                onClick = {navHostController.navigate(HomeRoute.route)}) {
                                Label(BACK_TO_MAIN_LABEL, color = colorResource(R.color.white))
                            }
                        }
                        VerticalSpacer(10)
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.teal_700),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {navHostController.navigate(HospitalsIndexRoute.route)}) {
                            Label(BACK_TO_HOSPITALS_LABEL, color = colorResource(R.color.white))
                        }
                    }
                }
            }
        }

        VerticalSpacer(10)
    }

    //TODO Composable -------- end

}


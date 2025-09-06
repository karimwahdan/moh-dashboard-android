package com.kwdevs.hospitalsdashboard.views.pages.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.kwdevs.hospitalsdashboard.bodies.control.SlugListBody
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.Quadruple
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.modules.Module
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalGeneralCreateRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ALEXANDRIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASUIT
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASWAN
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BANI_SUIF
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTORS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BUHIRA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CAIRO
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CREATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAKAHLIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAMIATTA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.Directorate
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.EDIT_HOSPITAL_MODULES
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.FAYUM
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GHARBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GIZA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ISMAILIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.InnerModule
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.KAFR_EL_SHEIKH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.LUXOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MATROUH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MENUFIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MINIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NEW_VALLEY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NORTH_SINAI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PORT_SAID
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PermissionSector
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QALUBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QENA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.RED_SEA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SHARQIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SOHAG
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SOUTH_SINAI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SUEZ
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.UPDATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_ALL_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_CURATIVE_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_DIRECTORATE_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_EDUCATIONAL_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_INSURANCE_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_NBTS_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_NBTS_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_SPECIALIZED_BLOOD_KPI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodBankModule
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodStocks
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.citiesSlugs
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.directoratesList
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasDirectoratePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasInnerModulePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasModulePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasPermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.routes.NotificationIndexRoute
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.routes.ChangePasswordRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.routes.SettingsRoute
import com.kwdevs.hospitalsdashboard.routes.SuperBloodKpiRoute
import com.kwdevs.hospitalsdashboard.routes.SuperBloodStocksRoute
import com.kwdevs.hospitalsdashboard.routes.UserControlRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADMIN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BOTTOM
import com.kwdevs.hospitalsdashboard.views.assets.BarGraph
import com.kwdevs.hospitalsdashboard.views.assets.CHANGE_PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DrawerButton
import com.kwdevs.hospitalsdashboard.views.assets.DrawerMenuButton
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LineGraph
import com.kwdevs.hospitalsdashboard.views.assets.MANAGE_ACCOUNTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NOTIFICATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PieGraph
import com.kwdevs.hospitalsdashboard.views.assets.SETTINGS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SIGN_OUT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WELCOME_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.dialogs.SignOutDialogBox
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.ComparativeBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.DirectoratesBloodBankKpiSection
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController){
    val drawerState         =  rememberDrawerState(initialValue = DrawerValue.Closed)

    val userType    = Preferences.User().getType()
    val superUser   = userType?.let{ if(it==ViewType.SUPER_USER) Preferences.User().getSuper() else null}
    val normalUser = userType?.let{ if(it==ViewType.HOSPITAL_USER) Preferences.User().get() else null}
    val isSuper=superUser?.isSuper?:false
    val roles=superUser?.roles?: emptyList()
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }
    val controller : HomeController= viewModel()
    val hospitalController:HospitalController= viewModel()
    val hospitalState by hospitalController.state.observeAsState()
    val state by controller.singleState.observeAsState()
    val showSheet = remember { mutableStateOf(false) }

    if(userType==ViewType.SUPER_USER) {if(superUser==null)navHostController.navigate(LoginRoute.route)}
    else if(userType==ViewType.HOSPITAL_USER) {if(normalUser==null)navHostController.navigate(LoginRoute.route)}
    else navHostController.navigate(LoginRoute.route)

    var sectors                 by remember { mutableStateOf<List<Sector>>(emptyList())}
    var types                   by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    var cities                  by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    val bloodStocks             =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val filteredBloodStocks     =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val filteredPRBcsStocks     =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val showFilterDialog                         =  remember { mutableStateOf(false) }
    val hospitals               =  remember { mutableStateOf<List<Hospital>>(emptyList())}

    var directorateHospitals    by remember { mutableStateOf<List<Hospital>>(emptyList()) }

    var loading                                  by remember { mutableStateOf(true) }
    var success                                  by remember { mutableStateOf(false) }
    var fail                                     by remember { mutableStateOf(false) }
    var loadingHospitals                         by remember { mutableStateOf(true) }
    var successHospitals                         by remember { mutableStateOf(false) }
    var failHospitals                            by remember { mutableStateOf(false) }

    var errorMessage                             by remember { mutableStateOf(EMPTY_STRING)}
    var errors                                   by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var superUserRoles                           by remember { mutableStateOf<List<String>>(emptyList()) }

    var canBrowseDirectorates                          by remember { mutableStateOf(false) }
    var canViewDirectorate                            by remember { mutableStateOf(false) }

    var canBrowseSectors                         by remember { mutableStateOf(false) }
    var canReadSector                            by remember { mutableStateOf(false) }

    var canBrowseHospitalTypes                   by remember { mutableStateOf(false) }

    var canBrowseHospitals                       by remember { mutableStateOf(false) }
    var canReadHospitals                         by remember { mutableStateOf(false) }

    var canCreateHospital                        by remember { mutableStateOf(false) }
    var canUpdateHospital                        by remember { mutableStateOf(false) }

    var canViewAllBloodKpi                       by remember { mutableStateOf(false) }
    var canViewDirectorateBloodKpi               by remember { mutableStateOf(false) }
    var canViewSpecializedBloodKpi               by remember { mutableStateOf(false) }
    var canViewEducationalBloodKpi               by remember { mutableStateOf(false) }
    var canViewNBTSBloodKpi                      by remember { mutableStateOf(false) }
    var canViewInsuranceBloodKpi                 by remember { mutableStateOf(false) }
    var canViewCurativeBloodKpi                  by remember { mutableStateOf(false) }
    val keys                                     =  remember { mutableStateOf<List<String>>(emptyList()) }
    val sectorKeys                               =  remember { mutableStateOf<List<String>>(emptyList()) }
    val hospitalTypeKeys                         =  remember { mutableStateOf<List<String>>(emptyList()) }
    val showExitDialog                           =  remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    when(hospitalState){
        is UiState.Loading->{
            LaunchedEffect(Unit){loadingHospitals=true;failHospitals=false;successHospitals=false}}
        is UiState.Error->{
            LaunchedEffect(Unit){
                loadingHospitals=false;failHospitals=true;successHospitals=false
                val s=hospitalState as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
                errors=exception.errors?: emptyMap()
            }}
        is UiState.Success->{
            LaunchedEffect(Unit){
                loadingHospitals=false;failHospitals=false;successHospitals=true
                val s= hospitalState as UiState.Success<HospitalsResponse>
                val r=s.data
                val d=r.data
                directorateHospitals=d
            }
        }
        else->{}
    }
    LaunchedEffect(Unit) {
        canViewDirectorate=superUser.hasPermission(CRUD.VIEW, PermissionSector.CERTAIN_DIRECTORATE)

        if(canViewDirectorate){
            val availableDirectorateList= mutableListOf<String>()
            directoratesList.forEach {
                if(superUser.hasDirectoratePermission(CRUD.VIEW,it)){
                    val d=when(it){
                        Directorate.CAIRO-> CAIRO
                        Directorate.GIZA-> GIZA
                        Directorate.QALUBIA-> QALUBIA
                        Directorate.ALEXANDRIA-> ALEXANDRIA
                        Directorate.ISMAILIA-> ISMAILIA
                        Directorate.ASWAN-> ASWAN
                        Directorate.ASUIT-> ASUIT
                        Directorate.LUXOR-> LUXOR
                        Directorate.RED_SEA-> RED_SEA
                        Directorate.BUHIRA-> BUHIRA
                        Directorate.BANI_SUIF-> BANI_SUIF
                        Directorate.PORT_SAID-> PORT_SAID
                        Directorate.DAKAHLIA-> DAKAHLIA
                        Directorate.DAMIATTA-> DAMIATTA
                        Directorate.SOHAG-> SOHAG
                        Directorate.SUEZ-> SUEZ
                        Directorate.SHARQIA-> SHARQIA
                        Directorate.SOUTH_SINAI-> SOUTH_SINAI
                        Directorate.GHARBIA-> GHARBIA
                        Directorate.NORTH_SINAI-> NORTH_SINAI
                        Directorate.FAYUM-> FAYUM
                        Directorate.QENA-> QENA
                        Directorate.KAFR_EL_SHEIKH-> KAFR_EL_SHEIKH
                        Directorate.MATROUH-> MATROUH
                        Directorate.MENUFIA-> MENUFIA
                        Directorate.MINIA-> MINIA
                        Directorate.NEW_VALLEY-> NEW_VALLEY
                        else-> EMPTY_STRING
                    }
                    if(d!= EMPTY_STRING && superUser?.isSuper==false) availableDirectorateList.add(d.replace("directorate@", EMPTY_STRING))
                    else if(it==Directorate.ALL) availableDirectorateList.addAll(citiesSlugs)

                }
            }
            availableDirectorateList.forEach{ Log.e("Directorate Available",it) }
            if(availableDirectorateList.isNotEmpty()) hospitalController.indexByCitySlugList(SlugListBody(availableDirectorateList))
        }
    }
    LaunchedEffect(Unit) {
        canBrowseDirectorates=superUser.hasPermission(CRUD.BROWSE, PermissionSector.DIRECTORATE_SECTOR)
        canBrowseSectors=superUser.hasPermission(CRUD.BROWSE, PermissionSector.ALL_SECTORS)
        canCreateHospital=superUser.hasModulePermission(CRUD.CREATE, MODULE.HOSPITAL)
        canViewAllBloodKpi=superUser.hasInnerModulePermission(CRUD.VIEW, InnerModule.BLOOD_BANK_KPIS)
    }
    when(state){
        is UiState.Loading->{ loading=true;fail=false;success=false }
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

                //hospitals.value=data.hospitals
                bloodStocks.value=data.bloodStocksToday
                keys.value=bloodStocks.value.mapNotNull { it.hospital?.name }
                filteredBloodStocks.value=data.bloodStocksToday
                filteredPRBcsStocks.value=data.bloodStocksToday.filter { it.bloodUnitTypeId==2 }
                if(canBrowseSectors) sectorKeys.value=filteredBloodStocks.value.map { it.hospital?.sector?.name?:EMPTY_STRING }
                if(isSuper) hospitalTypeKeys.value=filteredBloodStocks.value.map { it.hospital?.type?.name?:EMPTY_STRING }
                //val permissionCities=permissions.intersect(citiesPermissions.toSet())
                //val pcl=permissionCities.mapNotNull{it}
                //directorateHospitals=hospitals.value.filter {(it.city?.slug?:EMPTY_STRING) in pcl }
            }

        }
        else->{ LaunchedEffect(Unit) {controller.getHome()} }
    }
    SignOutDialogBox(showExitDialog,navHostController)
    HospitalsFilterDialog(showDialog = showFilterDialog,
        cities     = cities,  types  = types,
        sectors    = sectors, result = hospitals,
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(modifier=Modifier.fillMaxHeight().width(300.dp).background(color= Color.DarkGray, shape = rcs(topEnd = 20, topStart = 0, bottomEnd = 20, bottomStart = 0))) {
                item{
                    VerticalSpacer(20)
                    Label("App version 1.0.0", color = WHITE, paddingStart = 5, paddingEnd = 5)
                    VerticalSpacer()
                    DrawerButton(
                        icon = R.drawable.ic_exit_red,
                        label = SIGN_OUT_LABEL){ showExitDialog.value=true }
                    VerticalSpacer()
                    DrawerButton(
                        navHostController=navHostController,
                        route = NotificationIndexRoute,
                        icon = R.drawable.ic_notifications_blue,
                        label = NOTIFICATIONS_LABEL,
                    )
                    VerticalSpacer()
                    DrawerButton(
                        navHostController=navHostController,
                        route = ChangePasswordRoute,
                        icon = R.drawable.ic_lock_blue,
                        label = CHANGE_PASSWORD_LABEL,
                    )
                    VerticalSpacer()
                    DrawerButton(
                        navHostController=navHostController,
                        route = SettingsRoute,
                        icon = R.drawable.ic_settings_blue,
                        label = SETTINGS_LABEL,
                    )
                    if(canCreateHospital || isSuper){
                        VerticalSpacer()
                        DrawerButton(
                            icon = R.drawable.ic_hospital_blue,
                            label = ADD_NEW_HOSPITAL_LABEL){
                            Preferences.CrudTypes().set(CrudType.CREATE)
                            navHostController.navigate(HospitalGeneralCreateRoute.route)
                        }
                        if(isSuper){
                           VerticalSpacer()
                            DrawerButton(
                                icon = R.drawable.logo,
                                label = ADMIN_LABEL,
                                fontColor = WHITE,
                            ){
                                navHostController.navigate(AdminHomeRoute.route)
                            }
                            VerticalSpacer()
                            DrawerButton(
                                icon = R.drawable.ic_manage_accounts_blue,
                                label = MANAGE_ACCOUNTS_LABEL,
                                fontColor = WHITE,
                            ){
                                navHostController.navigate(UserControlRoute.route)
                            }
                        }
                    }
                    if(success){
                        if(superUser?.hasPermission(CRUD.VIEW,PermissionSector.CERTAIN_DIRECTORATE)==true){
                            if(directorateHospitals.isNotEmpty()){
                                VerticalSpacer()
                                directorateHospitals.groupBy { it.sector }.forEach { (sector, hospitalsBySectorList) ->
                                    if(hospitalsBySectorList.isNotEmpty()){
                                        DrawerMenuButton(label = sector?.name?: EMPTY_STRING,
                                            buttonColor = GRAY,
                                            icon = R.drawable.ic_view_timeline_blue) {
                                            hospitalsBySectorList.groupBy { it.city }.forEach{(city,hospitalsByCityList)->
                                                if(hospitalsByCityList.isNotEmpty()){
                                                    val cityName=city?.name?: EMPTY_STRING
                                                    DrawerMenuButton(label = cityName,buttonColor = Color.DarkGray,
                                                        icon = R.drawable.ic_location_city_blue) {
                                                        hospitalsByCityList.groupBy { it.type }.forEach { (type, hospitalsByTypeList) ->
                                                            if(hospitalsByTypeList.isNotEmpty()){
                                                                DrawerMenuButton(label = type?.name?: EMPTY_STRING,
                                                                    buttonColor = Color.DarkGray,
                                                                    icon = R.drawable.ic_dataset_blue) {
                                                                    hospitalsByTypeList.forEach { hos->
                                                                        DrawerButton(label =hos.name,
                                                                            buttonColor = Color.DarkGray,
                                                                            icon = R.drawable.ic_hospital_blue){
                                                                            val simple= ModelConverter().convertHospitalToSimple(hos)
                                                                            Preferences.Hospitals().set(simple)
                                                                            navHostController.navigate(HospitalViewRoute.route)

                                                                        }
                                                                    }

                                                                }
                                                            }
                                                            HorizontalDivider()
                                                        }

                                                    }


                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }

                    }

                }

            }
        }
    ) {
        Container(
            title= EMPTY_STRING,
            showSheet=showSheet,
            shape = rcsB(20),
            sheetColor = if(fail) Color.Red else GREEN,
            sheetContent = {
                if(fail) Label(errorMessage)
            }
        ) {
            Column(modifier= Modifier.fillMaxWidth().background(color = WHITE),
                verticalArrangement = Arrangement.Center){
                HomeHead(superUser,drawerState,scope)
                if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
                if(fail) FailScreen(modifier = Modifier.fillMaxSize(),
                    errors = errors,
                    message = errorMessage)
                if(success){
                    VerticalSpacer()
                    LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            VerticalSpacer()
                            HomeSectorsSection(hasPermission=canBrowseSectors || isSuper,sectors,navHostController)
                            HomeHospitalTypesSection(hasPermission=canBrowseHospitalTypes|| isSuper,types,navHostController)
                            HomeCitiesSection(hasPermission=canBrowseDirectorates|| isSuper,superUser=superUser,items=cities,navHostController=navHostController)
                            VerticalSpacer()
                            if(superUser.canViewBloodBankModule()){
                                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    if(superUser.canViewBloodStocks()){
                                        CustomButton(
                                            label = BLOOD_STOCK_LABEL,
                                            enabled = superUser.canViewBloodStocks(),
                                            enabledBackgroundColor = Color.Transparent,
                                            icon = R.drawable.ic_blood_drop,
                                            hasBorder = true,borderColor = BLUE,
                                            enabledFontColor = BLUE,buttonShadowElevation = 6,buttonShape = rcs(15),
                                            onClick = {navHostController.navigate(SuperBloodStocksRoute.route)}
                                        )
                                    }
                                    HorizontalSpacer(20)
                                    if(superUser.hasInnerModulePermission(action = CRUD.VIEW, innerModule = InnerModule.BLOOD_KPIS)){
                                        CustomButton(label = BLOOD_BANK_KPI_LABEL,
                                            enabled = superUser.hasInnerModulePermission(action = CRUD.VIEW, innerModule = InnerModule.BLOOD_KPIS),
                                            enabledBackgroundColor = Color.Transparent,
                                            icon = R.drawable.ic_bar_chart_blue,
                                            hasBorder = true, borderColor = BLUE,
                                            enabledFontColor = BLUE,buttonShadowElevation = 6,buttonShape = rcs(15),
                                            onClick = {navHostController.navigate(SuperBloodKpiRoute.route)}
                                        )
                                    }
                                }
                            }
                            val emptyBloodStocks = bloodStocks.value.isEmpty()

                            if(!emptyBloodStocks){
                                Row(modifier=Modifier.fillMaxWidth().background(BLACK),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,){
                                    Icon(R.drawable.ic_info_white, background = BLUE)
                                    Label(
                                        text="$TOTAL_LABEL ${bloodStocks.value.filter{it.hospital?.isNbts==false}.groupBy { it.hospital }.size} $FROM_LABEL ${sectors.sumOf { (it.hospitalsCount?:0)}-28}",
                                        color = if((bloodStocks.value.filter{it.hospital?.isNbts==false}.groupBy { it.hospital }.size)<(sectors.sumOf { (it.hospitalsCount?:0) }-28)) Color.Red else GREEN,
                                        fontWeight = FontWeight.Bold,
                                        maximumLines = 2,
                                        textAlign = TextAlign.Start,
                                        paddingEnd = 5,
                                        paddingStart = 5)
                                }
                                VerticalSpacer()
                                if(permissions.contains(VIEW_NBTS_BLOOD_STOCKS)){
                                    Row(modifier=Modifier.fillMaxWidth().background(BLACK),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Icon(R.drawable.ic_info_white, background = BLUE)
                                        Label(
                                            text="$TOTAL_LABEL ${bloodStocks.value.filter{it.hospital?.isNbts==true}.groupBy { it.hospital }.size} اقليمي من 28 ",
                                            color = if((bloodStocks.value.filter{it.hospital?.isNbts==true}.groupBy { it.hospital }.size)<28) Color.Red else GREEN,
                                            fontWeight = FontWeight.ExtraBold,
                                            maximumLines = 2,
                                            paddingEnd = 5,
                                            paddingStart = 5)
                                    }

                                }
                            }

                            //BloodStockSection(bloodStocks)
                            VerticalSpacer()
                            BloodStocksCharts(
                                bloodStocks         = bloodStocks.value,
                                keys                = keys,
                                sectorKeys          = sectorKeys,
                                hospitalTypeKeys    = hospitalTypeKeys,
                                selectedBloodStocks = filteredPRBcsStocks,
                            )
                            if(!emptyBloodStocks) DailyBloodStockPieChart(bloodStocks.value)
                            //if(isSuper){HorizontalDivider();VerticalSpacer();CertainDirectorateCollectiveBBKpiSection(controller)}
                            if(isSuper){HorizontalDivider();VerticalSpacer();DirectoratesBloodBankKpiSection(controller)}
                            if(isSuper){HorizontalDivider();VerticalSpacer();InsuranceBloodBankKpiSection(controller)
                            }
                            if(isSuper){HorizontalDivider();VerticalSpacer();EducationalBloodBankKpiSection(controller)
                            }
                            if(isSuper){HorizontalDivider();VerticalSpacer();ComparativeBloodBankKpiSection(controller)
                            }
                            if(isSuper){HorizontalDivider();VerticalSpacer();SpecializedBloodBankKpiSection(controller)}
                            if(isSuper){HorizontalDivider();VerticalSpacer();NBTSBloodBankKpiSection(controller)}
                            if(isSuper || canViewAllBloodKpi){HorizontalDivider();VerticalSpacer();ComparativeBloodBankKpiSection(controller)}


                            VerticalSpacer()
                        }
                    }
                    VerticalSpacer()
                }
            }
        }
    }
    BackHandler {
        scope.launch {
            if(drawerState.isClosed){drawerState.open()}
            else{drawerState.close()}
        }
    }

}

@Composable
private fun IncinerationCharts(incinerationData:List<MonthlyIncineration>){
    val (xLabels, yValues) = prepareMonthlyIncinerationLineChartData(incinerationData.filter { (it.bloodUnitTypeId?:0)==2 && (it.year?:EMPTY_STRING)=="2025" })
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
        Span(text=year?:EMPTY_STRING, color = WHITE, backgroundColor = BLUE)
        val byMonthAndType=listOfIncinerationData.groupBy { it.month }
        val byBloodGroup=preparePieChartByBloodGroup(listOfIncinerationData.filter { (it.bloodUnitTypeId?:0)==2 })
        val vs=byBloodGroup.map { Pair(it.key.name?:EMPTY_STRING,it.value.toInt()) }
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

                else->EMPTY_STRING
            }
            VerticalSpacer()
            Span(text= monthString, color = WHITE, backgroundColor = ORANGE)

            val (x,y)=prepareBarByUnitType(list)
            BarGraph(
                modifier = Modifier.height(300.dp),
                xData=x.map { it.name?:EMPTY_STRING },yData=y,
                chartDescriptionText = EMPTY_STRING,
            )
        }
    }
}

@Suppress("unused")
fun processForTodayByCityBarGraph(data: List<DailyBloodStock>, byCity:Boolean, byArea:Boolean, byHospital:Boolean,
                                  byDate:Boolean, date:String?=null): Pair<List<String>, List<Int>> {

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
            val block = getTimeBlock(it.entryDate ?: EMPTY_STRING)
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
            if(byCity)stock.hospital?.city?.name?:EMPTY_STRING
            else if(byArea)stock.hospital?.area?.name?:EMPTY_STRING
            else if(byHospital) stock.hospital?.name?:EMPTY_STRING
            else stock.hospital?.city?.name?:EMPTY_STRING,
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

@Suppress("unused")
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
            val block = getTimeBlock(it.entryDate ?: EMPTY_STRING)
            block.startsWith(today)
        }
    }

    if (filteredData.isEmpty()) return Pair(emptyList(), emptyMap())

    // Map each item into Quadruple: data, label (city/area/hospital), bloodGroup, block
    val bloodGroupLabels=data.map { it.bloodGroup?.name?:EMPTY_STRING }
    val locationLabels = data.map {
        if(groupByCity) it.hospital?.city?.name
        if(groupByArea) it.hospital?.area?.name
        if(groupByHospital) it.hospital?.name
        else EMPTY_STRING
    }.distinct()

    @Suppress("UNUSED_VARIABLE")
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
            groupByCity -> item.hospital?.city?.name ?: EMPTY_STRING
            groupByArea -> item.hospital?.area?.name ?: EMPTY_STRING
            groupByHospital -> item.hospital?.name ?: EMPTY_STRING
            else -> item.hospital?.city?.name ?: EMPTY_STRING
        }
        val bloodGroup = item.bloodGroup
        Quadruple(item.amount ?: 0, label, bloodGroup?.name?:EMPTY_STRING, timeBlock)
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
        label = EMPTY_STRING,
        description = EMPTY_STRING
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
private fun HomeHead(user:SuperUser?,drawerState: DrawerState,scope:CoroutineScope){
    val userType=Preferences.User().getType()
    Box(modifier=Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            Column(modifier=Modifier.background(color = colorResource(R.color.teal_700), shape = rcsB(20))) {
                Box{
                    Header(HOME_LABEL, fontSize = 21, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Box(modifier=Modifier.fillMaxWidth()){
                    Row(modifier=Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        if(userType==ViewType.SUPER_USER && user !=null){
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center){
                                Label(label=WELCOME_USER_LABEL,text = user.name,
                                    color = Color.White, labelColor = Color.White,
                                    maximumLines = 3,
                                    fontSize = 16, fontWeight = FontWeight.Bold)
                                Label(text = user.title?.name?: EMPTY_STRING,
                                    color = Color.White,
                                    maximumLines = 3,fontWeight = FontWeight.Bold)

                            }
                        }
                    }
                }
                VerticalSpacer()
            }
        }
        IconButton(R.drawable.ic_menu_white, background = colorResource(R.color.teal_700), paddingStart = 5, paddingEnd = 5) {
            scope.launch {
                if(drawerState.isClosed) drawerState.open() else drawerState.close()
            }

        }

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
    }
    catch (e: Exception) {e.printStackTrace();timeBlock /* fallback */ }
}

@Composable
private fun SimpleHospitalCard(item:Hospital,navHostController: NavHostController){
    val user=Preferences.User().getSuper()
    val isSuper=user?.isSuper?:false
    val roles=user?.roles
    val permissions=roles?.flatMap { it.permissions.map { p-> p.slug?: EMPTY_STRING } }?: emptyList()
    val name=item.name
    val active=item.active
    val isNBTS=item.isNBTS
    val editHospitalModules=permissions.contains(EDIT_HOSPITAL_MODULES)
    Box(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .clip(rcs(20))
        .background(color = WHITE,shape=rcs(20)).clickable {
            val simple= ModelConverter().convertHospitalToSimple(item)
            Preferences.Hospitals().set(simple)
            navHostController.navigate(HospitalViewRoute.route)
        }){
        ColumnContainer(
            background = if(isNBTS==true) PALE_ORANGE else WHITE
        ) {
            Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
                Row(modifier=Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    if(item.isNBTS==true) Icon(R.drawable.ic_blood_drop)else Icon(R.drawable.ic_home_work_blue)
                    HorizontalSpacer()
                    Label(name, fontWeight = FontWeight.Bold, maximumLines = 5)
                }
                Icon(if (active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                if(editHospitalModules || isSuper){
                    IconButton(R.drawable.ic_view_timeline_blue) {
                        val simple=ModelConverter().convertHospitalToSimple(item)
                        Preferences.Hospitals().set(simple)
                        navHostController.navigate(HospitalModuleSelectorRoute.route)
                    }

                }
            }
            VerticalSpacer()
        }
    }

}

@Preview
@Composable
private fun Preview(){ HomeScreen(rememberNavController()) }
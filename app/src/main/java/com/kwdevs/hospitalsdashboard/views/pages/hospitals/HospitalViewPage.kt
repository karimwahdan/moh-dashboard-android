package com.kwdevs.hospitalsdashboard.views.pages.hospitals

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.kpis.KpiTable
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CITY_HEAD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_BLOOD_MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_DEVICE_MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_WARD_MODULE
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.routes.AreaViewRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDepartmentCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.ARROW_DOWN
import com.kwdevs.hospitalsdashboard.views.ARROW_UP
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.LOADING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.HospitalDepartmentCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.WardCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.devices.HospitalDeviceCard

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun HospitalViewPage(navHostController: NavHostController){
    val savedItem=Preferences.Hospitals().get()
    val area=Preferences.Areas().get()
    val userType = Preferences.User().getType()
    val superUser = userType?.let{ if(it== ViewType.SUPER_USER) Preferences.User().getSuper() else null}
    val roles=superUser?.roles
    val permissions= roles?.flatMap { r-> r.permissions}?.map { p-> p.slug?: EMPTY_STRING }?: emptyList()
    if(userType==ViewType.SUPER_USER) {if(superUser==null)navHostController.navigate(LoginRoute.route)}
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

    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail    by remember{ mutableStateOf(false)}

    var empty   by remember { mutableStateOf(true)}
    var hospital by remember { mutableStateOf<Hospital?>(null)}
    val controller = HospitalController()
    val state by controller.singleState.observeAsState()
    var devices by remember { mutableStateOf<List<HospitalDevice>>(emptyList()) }
    var showDevices by remember { mutableStateOf(false) }
    var departments by remember { mutableStateOf<List<HospitalDepartment>>(emptyList()) }
    var showDepartments by remember { mutableStateOf(false) }
    var showBloodKpi by remember { mutableStateOf(false) }
    var wards by remember { mutableStateOf<List<HospitalWard>>(emptyList()) }
    var showWards by remember { mutableStateOf(false) }
    var bloodKpis by remember { mutableStateOf<List<BloodBankKpi>>(emptyList()) }
    val showSheet = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if(superUser!=null){
            val isSuper=superUser.isSuper
            if(!isSuper){
                superUserRoles=roles?.map { it.slug }?: emptyList()
                canBrowseCities=superUserRoles.contains(CITY_HEAD)
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
            else{

                canBrowseHospitals=true
                canReadHospitals=true

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
        is UiState.Loading->{LaunchedEffect(Unit){loading=true;fail=false;success=false;empty=true} }
        is UiState.Error->{LaunchedEffect(Unit){loading=false;fail=true;success=false;empty=true}}
        is UiState.Success->{
            LaunchedEffect(Unit){
                loading=false;fail=false;success=true
                val s = state as UiState.Success<HospitalSingleResponse>
                val r = s.data
                val d= r.data
                empty=  d==null
                hospital=d
                d?.devices?.let {devices=it}
                d?.departments?.let{departments=it}
                d?.wards?.let{wards=it}
                d?.kpis?.let { bloodKpis=it }
            }
        }
        else->{
            if(savedItem!=null)controller.view()
            else navHostController.navigate(HospitalsIndexRoute.route)
        }
    }

    Container(
        title = hospital?.name?: LOADING_LABEL,
        headerShowBackButton = true,
        showSheet = showSheet,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            navHostController.navigate( if(canBrowseHospitals && area!=null ) AreaViewRoute.route  else HospitalsIndexRoute.route )
        }
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                Column(modifier=Modifier.fillMaxSize()){
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                        item{
                            if(hospital?.isNBTS==false){
                                if(permissions.contains(VIEW_HOSPITAL_DEVICE_MODULE)){
                                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Label(DEVICES_LABEL, fontWeight = FontWeight.Bold)
                                        HorizontalSpacer()
                                        IconButton(if(showDevices) ARROW_UP else ARROW_DOWN, background = BLUE){showDevices=!showDevices}
                                    }
                                    HorizontalDivider()
                                    DevicesSection(hospital,navHostController,showDevices,devices)
                                    if(showDevices) HorizontalDivider()
                                }
                                if(permissions.contains(VIEW_HOSPITAL_DEPARTMENT_MODULE)){
                                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Label(DEPARTMENTS_LABEL, fontWeight = FontWeight.Bold)
                                        HorizontalSpacer()
                                        IconButton(if(showDepartments) ARROW_UP else ARROW_DOWN, background = BLUE){showDepartments=!showDepartments}
                                    }
                                    HorizontalDivider()
                                    DepartmentsSection(hospital,navHostController,showDepartments,departments)
                                    if(showDepartments) HorizontalDivider()
                                }
                                if(permissions.contains(VIEW_HOSPITAL_WARD_MODULE)){
                                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Label(IN_PATIENT_LABEL, fontWeight = FontWeight.Bold)
                                        HorizontalSpacer()
                                        IconButton(if(showWards) ARROW_UP else ARROW_DOWN, background = BLUE){showWards=!showWards}
                                    }
                                    WardsSection(hospital,navHostController,showWards,wards)
                                }
                            }
                            if(permissions.contains(VIEW_HOSPITAL_BLOOD_MODULE) || superUser?.isSuper==true){
                                if(bloodKpis.isNotEmpty()){
                                    HorizontalDivider()
                                    Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically){
                                        Label(KPI_LABEL, fontWeight = FontWeight.Bold)
                                        HorizontalSpacer()
                                        IconButton(if(showBloodKpi) ARROW_UP else ARROW_DOWN, background = BLUE){showBloodKpi=!showBloodKpi}
                                    }
                                    KpiSection(bloodKpis,showBloodKpi)
                                }
                            }

                        }
                    }


                }
            }
            if(fail) FailScreen(modifier=Modifier.fillMaxSize())
        }
    }

}
@Composable
private fun KpiSection(bloodKpis:List<BloodBankKpi>,showKpi:Boolean){
    AnimatedVisibility(visible = showKpi,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            val byDepartment=bloodKpis.groupBy { it.department }
            byDepartment.forEach { (department, kpis) ->
                Label(department?.name?: EMPTY_STRING)
                KpiTable(kpis)
            }
        }
    }
}
@Composable
private fun DevicesSection(
    hospital: Hospital?,
    navHostController: NavHostController,
    showDevices: Boolean,
    devices: List<HospitalDevice>
) {

    AnimatedVisibility(visible = showDevices,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            hospital?.let {
                CustomButton(
                    label=ADD_NEW_LABEL) {
                    val simple= ModelConverter().convertHospitalToSimple(it)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalDeviceCreateRoute.route)
                }
            }
            devices.forEach {
                HospitalDeviceCard(it,navHostController)
            }
        }
    }
}

@Composable
private fun DepartmentsSection(
    hospital: Hospital?,
    navHostController: NavHostController,
    showDepartments: Boolean,
    departments: List<HospitalDepartment>
) {
    AnimatedVisibility(visible = showDepartments) {
        Column(modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){

            hospital?.let {
                CustomButton(label=ADD_NEW_LABEL) {
                    val simple= ModelConverter().convertHospitalToSimple(it)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalDepartmentCreateRoute.route)
                }
            }

            departments.forEach {
                HospitalDepartmentCard(it,navHostController)
            }
        }
    }
}

@Composable
private fun WardsSection(hospital: Hospital?,navHostController: NavHostController,showWards: Boolean, wards: List<HospitalWard>) {
    AnimatedVisibility(visible = showWards) {
        Column(modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            hospital?.let {
                CustomButton(label=ADD_NEW_LABEL) {
                    val simple= ModelConverter().convertHospitalToSimple(it)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalDeviceCreateRoute.route)
                }
            }

            wards.forEach { ward->
                WardCard(ward,navHostController)
                VerticalSpacer()
            }
        }
    }

}

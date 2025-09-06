package com.kwdevs.hospitalsdashboard.views.pages.hospitalHome

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.PatientViewType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.modules.ADMISSIONS_MODULE
import com.kwdevs.hospitalsdashboard.modules.BIRTHS_MODULE
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_MODULE
import com.kwdevs.hospitalsdashboard.modules.CLINICS_MODULE
import com.kwdevs.hospitalsdashboard.modules.INPATIENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.LABORATORY_MODULE
import com.kwdevs.hospitalsdashboard.modules.MEDICAL_DEVICES_MODULE
import com.kwdevs.hospitalsdashboard.modules.MORGUE_MODULE
import com.kwdevs.hospitalsdashboard.modules.OPERATIONS_MODULE
import com.kwdevs.hospitalsdashboard.modules.PATIENTS_MODULE
import com.kwdevs.hospitalsdashboard.modules.PHYSICAL_THERAPY_MODULE
import com.kwdevs.hospitalsdashboard.modules.PRETERMS_MODULE
import com.kwdevs.hospitalsdashboard.modules.RENAL_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.TUMORS_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_BLOOD_BANK_MODULE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasModule
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.routes.AdmissionCreateRoute
import com.kwdevs.hospitalsdashboard.routes.BabyBirthIndexRoute
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.ChangePasswordRoute
import com.kwdevs.hospitalsdashboard.routes.ClinicsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDevicesRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalUserViewRoute
import com.kwdevs.hospitalsdashboard.routes.IncubatorIndexRoute
import com.kwdevs.hospitalsdashboard.routes.LabTestIndexRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.routes.MorguesIndexRoute
import com.kwdevs.hospitalsdashboard.routes.NationalIdScannerRoute
import com.kwdevs.hospitalsdashboard.routes.NormalUserWardsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PhysicalTherapyIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.ReceptionBedsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.RenalDevicesIndexRoute
import com.kwdevs.hospitalsdashboard.routes.SettingsRoute
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CHANGE_PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CLINICS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DrawerButton
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCUBATORS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.LAB_TESTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MY_ACCOUNT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_ADMISSION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_RENAL_WASH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_ROOMS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PHYSICAL_THERAPY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PRETERM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RECEPTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SCAN_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SETTINGS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SIGN_OUT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TUMORS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WARDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.deleteData
import com.kwdevs.hospitalsdashboard.views.rcs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalHomePage(navHostController: NavHostController){
    val drawerState         =  rememberDrawerState(initialValue = DrawerValue.Closed)
    val bloodBank=Preferences.BloodBanks().get()
    val controller:HospitalController = viewModel()
    val state by controller.singleState.observeAsState()
    var hospital by remember { mutableStateOf<Hospital?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val user = Preferences.User().get()
    val roles=user?.roles?: emptyList()
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }
    var moduleSlugs by remember { mutableStateOf<List<String>>(emptyList()) }
    var canViewBloodBank by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val scope = rememberCoroutineScope()
    when(state){
        is UiState.Loading->{
            loading=true;fail=false;success=false
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL

            }

        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true

                val s = state as UiState.Success<HospitalSingleResponse>
                val r = s.data
                val data = r.data
                hospital=data
                moduleSlugs=data?.modules?.map { it.slug?:EMPTY_STRING }?: emptyList()
                data?.let{
                    it.bloodBank?.let{i-> Preferences.BloodBanks().set(i)}
                    Preferences.Hospitals().set(
                        SimpleHospital(
                        id          = it.id?:0,
                        name        = it.name,
                        cityId      = it.cityId,
                        areaId      = it.areaId,
                        address     = it.address,
                        sectorId    = it.sectorId,
                        typeId      = it.typeId,
                        longitude   = it.longitude,
                        latitude    = it.latitude,
                        active      = it.active,
                        isNbts      = it.isNBTS,
                        city        = City(id=it.city?.id,name=it.city?.name,headId=it.city?.headId,slug=it.city?.slug),
                        area        = Area(id=it.areaId,cityId=it.area?.cityId, name = it.area?.name, headId = it.area?.headId),
                        sector      = it.sector,
                        type        = it.type,
                        modules     = it.modules)
                    )
                }
                canViewBloodBank=moduleSlugs.contains(BLOOD_BANK_MODULE) && permissions.contains(VIEW_HOSPITAL_BLOOD_BANK_MODULE)
            }
        }
        else->{ controller.view() }
    }
    BackHandler { scope.launch { if(drawerState.isClosed) drawerState.open() else drawerState.close() } }
    if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
    if(fail){
        FailScreen(modifier = Modifier.fillMaxSize(),
            errors = errors,
            message = errorMessage)
    }
    if(success){
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                LazyColumn(modifier=Modifier.fillMaxHeight().width(260.dp).background(color= Color.DarkGray, shape = rcs(topEnd = 20, topStart = 0, bottomEnd = 20, bottomStart = 0))) {
                    item{
                        VerticalSpacer(20)
                        DrawerButton(
                            icon = R.drawable.ic_exit_red,
                            label = SIGN_OUT_LABEL,
                            fontColor = WHITE
                        ){
                            deleteData()
                            navHostController.navigate(LoginRoute.route)
                        }
                        VerticalSpacer()
                        DrawerButton(
                            navHostController=navHostController,
                            route = ChangePasswordRoute,
                            icon = R.drawable.ic_lock_blue,
                            fontColor = WHITE,
                            label = CHANGE_PASSWORD_LABEL,
                        )
                        VerticalSpacer()
                        DrawerButton(
                            navHostController=navHostController,
                            route = SettingsRoute,
                            icon = R.drawable.ic_settings_blue,
                            fontColor = WHITE,
                            label = SETTINGS_LABEL,
                        )
                        VerticalSpacer()
                        DrawerButton(
                            navHostController=navHostController,
                            route = HospitalUserViewRoute,
                            icon = R.drawable.ic_account_circle_blue,
                            fontColor = WHITE,
                            label = MY_ACCOUNT_LABEL,
                        )
                    }
                }

            }
        ){
            Container(
                title = hospital?.name?: EMPTY_STRING,
                headerFontSize = 16,
                showSheet = showSheet,
                headerShowBackButton = false,
                headerIconButtonBackground = WHITE,
                headerOnClick = {
                    Preferences.User().delete()
                    Preferences.User().deleteType()
                    Preferences.User().deleteSuper()
                    Preferences.Hospitals().delete()
                    Preferences.Hospitals().deleteTypeOption()
                    Preferences.Hospitals().deleteSectorOption()
                    Preferences.Patients().delete()
                    Preferences.Wards().delete()
                    Preferences.OperationRooms().delete()
                    navHostController.navigate(LoginRoute.route)
                }
            ) {
                Column(modifier=Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(R.drawable.ic_account_circle_blue, size = 52, containerSize = 52)
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        //IconButton(R.drawable.ic_log) { }
                        Label(text=user?.name?:EMPTY_STRING)
                        //IconButton(R.drawable.ic_settings_blue) { navHostController.navigate(HospitalUserViewRoute.route) }
                    }
                    VerticalSpacer()
                    HorizontalDivider()
                    LazyRow(modifier= Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        item{
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(
                                    icon=R.drawable.ic_patient,
                                    label = PATIENTS_LABEL,
                                    enabled = moduleSlugs.contains(PATIENTS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    Preferences.ViewTypes().setPatientViewType(PatientViewType.BY_HOSPITAL)
                                    navHostController.navigate(PatientsIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_physical_therapy,
                                    label = PHYSICAL_THERAPY_LABEL,
                                    enabled = moduleSlugs.contains(PHYSICAL_THERAPY_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(PhysicalTherapyIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(
                                    icon=R.drawable.ic_premature_baby,
                                    label = PRETERM_LABEL,
                                    enabled = moduleSlugs.contains(PRETERMS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(PretermAdmissionsIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon= R.drawable.ic_medical_operation,
                                    label = OPERATIONS_LABEL,
                                    enabled = moduleSlugs.contains(OPERATIONS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    Preferences.ViewTypes().set(ViewType.BY_HOSPITAL)
                                    navHostController.navigate(OperationsIndexRoute.route)

                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(
                                    icon=R.drawable.ic_baby,
                                    enabled=moduleSlugs.containsAll(listOf(OPERATIONS_MODULE, BIRTHS_MODULE)),
                                    maxWidth = 82,
                                    minWidth = 82,
                                    label = BIRTHS_LABEL) { navHostController.navigate(BabyBirthIndexRoute.route) }
                            }

                        }
                    }
                    VerticalSpacer()
                    LazyRow(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        item{
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_medical_ward,
                                    label = WARDS_LABEL,
                                    enabled = moduleSlugs.contains(INPATIENT_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(NormalUserWardsIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_renal_ward,
                                    label = RENAL_DEVICES_LABEL,
                                    enabled = moduleSlugs.contains(RENAL_DEPARTMENT_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(RenalDevicesIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_medical_morgue,
                                    label = MORGUE_LABEL,
                                    enabled = moduleSlugs.contains(MORGUE_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    Preferences.ViewTypes().set(ViewType.BY_HOSPITAL)
                                    navHostController.navigate(MorguesIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_medical_operation_room,
                                    label = OPERATION_ROOMS_LABEL,
                                    enabled = moduleSlugs.contains(OPERATIONS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(OperationRoomIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_medical_clinic,
                                    label = CLINICS_LABEL,
                                    enabled = moduleSlugs.contains(CLINICS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(ClinicsIndexRoute.route)
                                }
                            }

                        }
                    }
                    VerticalSpacer()
                    LazyRow(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        item{
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_medical_device,
                                    label = DEVICES_LABEL,
                                    enabled = moduleSlugs.contains(MEDICAL_DEVICES_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(HospitalDevicesRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_cancer,
                                    label = TUMORS_LABEL,
                                    enabled = moduleSlugs.contains(TUMORS_DEPARTMENT_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    Preferences.Patients().delete()
                                    Preferences.ViewTypes().set(ViewType.BY_HOSPITAL)
                                    navHostController.navigate(CancerCureIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_lab_test,
                                    enabled = moduleSlugs.contains(LABORATORY_MODULE),
                                    label = LAB_TESTS_LABEL,
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(LabTestIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_infant_incubator,
                                    label = INCUBATORS_LABEL,
                                    enabled = moduleSlugs.contains(PRETERMS_MODULE),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(IncubatorIndexRoute.route)
                                }
                            }
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_reception,
                                    label = RECEPTION_LABEL,
                                    enabled = moduleSlugs.containsAll(listOf(PATIENTS_MODULE, ADMISSIONS_MODULE)),
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(ReceptionBedsIndexRoute.route)
                                }
                            }
                        }
                    }
                    VerticalSpacer()
                    LazyRow(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        item{
                            Box(modifier=Modifier.padding(horizontal = 10.dp)){
                                CustomButtonWithImage(icon=R.drawable.ic_blood_drop,
                                    label = BLOOD_BANK_LABEL,
                                    enabled = hospital?.hasModule(MODULE.BLOOD_BANK)?:false && bloodBank!=null,
                                    maxWidth = 82,
                                    minWidth = 82,) {
                                    navHostController.navigate(BloodBankHomeRoute.route)
                                }
                            }
                        }
                    }
                    VerticalSpacer()
                    HorizontalDivider()
                    VerticalSpacer(10)
                    CustomButton(
                        modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        label = NEW_ADMISSION_LABEL,
                        buttonShape = RectangleShape, enabledBackgroundColor = BLUE,
                        enabled = moduleSlugs.contains(ADMISSIONS_MODULE)) {
                        navHostController.navigate(AdmissionCreateRoute.route)
                    }
                    VerticalSpacer()
                    if(moduleSlugs.contains(RENAL_DEPARTMENT_MODULE)){
                        CustomButton(
                            modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                            label = NEW_RENAL_WASH_LABEL,
                            buttonShape = RectangleShape, enabledBackgroundColor = BLUE) {
                            navHostController.navigate(NationalIdScannerRoute.route)
                        }

                    }
                    VerticalSpacer()
                    CustomButton(
                        modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        label = SCAN_ID_LABEL,
                        enabled = false,
                        buttonShape = RectangleShape, enabledBackgroundColor = BLUE) {
                        navHostController.navigate(NationalIdScannerRoute.route)
                    }
                    VerticalSpacer()

                }
            }

        }

    }
}

@Preview
@Composable
private fun Preview(){ HospitalHomePage(rememberNavController()) }
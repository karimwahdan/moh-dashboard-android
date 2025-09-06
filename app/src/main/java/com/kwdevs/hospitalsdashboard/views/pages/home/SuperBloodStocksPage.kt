package com.kwdevs.hospitalsdashboard.views.pages.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.Directorate
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PermissionSector
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.getDirectoratePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasDirectoratePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hospitalSourcesList
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ALEXANDRIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ALL_DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ASUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ASWAN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BANI_SUIF_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BUHIRA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAIRO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DAKAHLIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DAMIATTA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SAVE_EXCEL_FILE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FAYUM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILE_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GHARBIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GIZA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HIDE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITALS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ISMAILIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.KAFR_EL_SHEIKH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.LUXOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.MATROUH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MENUFIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MINIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEW_VALLEY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NORTH_SINAI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NO_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PORT_SAID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QALUBIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.QENA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RED_SEA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ALL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHARQIA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SOHAG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SUEZ_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.timeBlocks
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB
import okhttp3.ResponseBody
import retrofit2.Response

@Suppress("SpellCheckingInspection")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperBloodStocksPage(navHostController: NavHostController){
    val controller          :  BloodBankIssuingDepartmentController= viewModel()
    val hospitalController  :  HospitalController= viewModel()

    val context             =  LocalContext.current
    val superUser           =  Preferences.User().getSuper()
    val sources             =  hospitalSourcesList(superUser)
    //val settingsController: SettingsController = viewModel()
    val selectedHospital    =  remember { mutableStateOf<Hospital?>(null) }
    val selectedHospitals   =  remember { mutableStateOf<List<Hospital>>(emptyList()) }
    val excelFileName       =  remember { mutableStateOf("blood_stocks") }
    val exportState         by controller.stockExcelState.observeAsState()
    val state               by controller.hospitalsStateState.observeAsState()
    val hospitalState       by hospitalController.state.observeAsState()
    var hospitals           by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var hospitalsWithStocks by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var loadingExport       by remember { mutableStateOf(false) }
    var failExport          by remember { mutableStateOf(false) }
    var successExport       by remember { mutableStateOf(false) }
    var loading             by remember { mutableStateOf(false) }
    var fail                by remember { mutableStateOf(false) }
    var success             by remember { mutableStateOf(false) }
    var loadingOptions      by remember { mutableStateOf(false) }
    var failOptions         by remember { mutableStateOf(false) }
    var successOptions      by remember { mutableStateOf(false) }

    val selectedSource      = remember { mutableStateOf<Pair<PermissionSector,String>?>(null) }
    val directoratesList    = remember { mutableStateOf<List<Pair<Directorate,String>>>(emptyList()) }
    val body                =  remember { mutableStateOf<DailyBloodStockFilterBody?>(null) }
    val selectedDirectorate = remember { mutableStateOf<Pair<Directorate,String>?>(null) }
    val startDateState      =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val startDate           =  remember { mutableStateOf(EMPTY_STRING) }
    val showStartDatePicker =  remember { mutableStateOf(false) }

    val endDateState        =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val endDate             =  remember { mutableStateOf(EMPTY_STRING) }
    val showEndDatePicker   =  remember { mutableStateOf(false) }

    val selectedTimeBlock = remember { mutableStateOf<Pair<String,String>?>(null) }

    var showModal           by remember { mutableStateOf(true) }

    //var sortedBloodRecords  by remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    //var records             by remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    var showSaveExcelDialog by remember { mutableStateOf(false) }
    when(hospitalState){
        is UiState.Loading->{ loadingOptions=true;failOptions=false;successOptions=false }
        is UiState.Error->{ loadingOptions=false;failOptions=true;successOptions=false }
        is UiState.Success->{
            loadingOptions=false;failOptions=false;successOptions=true
            val s =hospitalState as UiState.Success<HospitalsResponse>
            val r=s.data
            val data=r.data
            hospitals=data
        }
        else->{loadingOptions=false;failOptions=false;successOptions=false}
    }
    LaunchedEffect(state) {
        when(state){
            is UiState.Loading->{
                loading=true;fail=false;success=false
                showModal=false
            }
            is UiState.Error->{
                loading=false;fail=true;success=false
                showModal=false
            }
            is UiState.Success->{
                loading=false;fail=false;success=true
                showModal=false
                val s=state as UiState.Success<HospitalsResponse>
                val r=s.data
                val data=r.data
                hospitalsWithStocks=data
                //records=data.flatMap { it.stocks }
                val sortedBloodRecordsNotUsed=hospitalsWithStocks.flatMap{it.stocks}.sortedByDescending { it.entryDate }
                    .filter { it.bloodUnitTypeId in listOf(null,1,2) }
                    .sortedWith(compareByDescending<DailyBloodStock> { it.entryDate }
                        .thenByDescending { it.hospital?.cityId ?: Int.MIN_VALUE}
                        .thenByDescending { it.hospital?.areaId ?: Int.MIN_VALUE }
                        .thenByDescending { it.hospitalId ?: Int.MIN_VALUE }
                        .thenBy { it.timeBlock})
            }
            else->{}
        }

    }
    when (exportState) {
        is UiState.Loading->{
            LaunchedEffect(Unit) {loadingExport=true;failExport=false;successExport=false}
        }
        is UiState.Error -> {
            LaunchedEffect(Unit) {
                loadingExport=false;failExport=true;successExport=false
                val message = (exportState as UiState.Error).exception.message ?: ERROR_LOADING_DATA_LABEL
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                showSaveExcelDialog=false

            }
        }
        is UiState.Success<*> -> {
            LaunchedEffect(Unit){
                loadingExport=false;failExport=false
                val response = (exportState as UiState.Success<Response<ResponseBody>>).data
                if (response.isSuccessful) {
                    successExport=true
                    val saved = saveExcelFile(
                        responseBody = response.body()!!,
                        fileName = "${excelFileName.value}.xlsx"
                    )
                    toast(context,if (saved) FILE_SAVED_LABEL else ERROR_SAVE_EXCEL_FILE_LABEL)
                }
                else toast(context, "Download failed: ${response.code()}")
                showSaveExcelDialog=false
            }

        }
        else -> {}
    }


    LaunchedEffect(selectedSource.value) {
        val permissionSectorValue = selectedSource.value
        permissionSectorValue?.let { permissionSector ->
            when (permissionSector.first) {
                PermissionSector.CURATIVE_SECTOR -> {hospitalController.indexBySector(1)}
                PermissionSector.INSURANCE_SECTOR -> {hospitalController.indexBySector(2)}
                PermissionSector.EDUCATIONAL_SECTOR -> {hospitalController.indexBySector(3)}
                PermissionSector.SPECIALIZED_SECTOR -> {hospitalController.indexBySector(4)}
                PermissionSector.DIRECTORATE_SECTOR -> {hospitalController.indexBySector(5)}
                PermissionSector.NBTS_SECTOR -> {hospitalController.indexBySector(6)}
                PermissionSector.ALL_SECTORS -> {hospitalController.indexAllHospitals()}
                PermissionSector.CERTAIN_DIRECTORATE->{
                    val availableDirectoratesList= mutableListOf<Pair<Directorate,String>>()
                    superUser?.let {
                        val canViewAllDirectorates=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ALL)
                        val canViewAlexandria=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ALEXANDRIA)
                        val canViewAswan=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ASWAN)
                        val canViewAsuit=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ASUIT)

                        val canViewBaniSuif=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.BANI_SUIF)
                        val canViewBuhira=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.BUHIRA)

                        val canViewCairo=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.CAIRO)

                        val canViewDamiatta=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.DAMIATTA)
                        val canViewDakahlia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.DAKAHLIA)

                        val canViewFayum=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.FAYUM)

                        val canViewGiza=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.GIZA)
                        val canViewGharbia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.GHARBIA)

                        val canViewIsmailia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.ISMAILIA)

                        val canViewKafrElShiekh=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.KAFR_EL_SHEIKH)

                        val canViewLuxor=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.LUXOR)

                        val canViewMatrouh=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MATROUH)
                        val canViewMenufia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MENUFIA)
                        val canViewMinia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.MINIA)

                        val canViewNewValley=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.NEW_VALLEY)
                        val canViewNorthSinai=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.NORTH_SINAI)

                        val canViewPortSaid=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.PORT_SAID)

                        val canViewQena=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.QENA)
                        val canViewQalubia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.QALUBIA)

                        val canViewRedSea=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.RED_SEA)

                        val canViewSohag=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SOHAG)
                        val canViewSuez=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SUEZ)
                        val canViewSharqia=superUser.hasDirectoratePermission(CRUD.VIEW,Directorate.SHARQIA)

                        if(canViewAllDirectorates){availableDirectoratesList.add(Pair(Directorate.ALL,ALL_DIRECTORATES_LABEL))}
                        if(canViewAlexandria){availableDirectoratesList.add(Pair(Directorate.ALEXANDRIA,
                            ALEXANDRIA_LABEL
                        ))}
                        if(canViewAswan){availableDirectoratesList.add(Pair(Directorate.ASWAN,
                            ASWAN_LABEL
                        ))}
                        if(canViewAsuit){availableDirectoratesList.add(Pair(Directorate.ASUIT,
                            ASUIT_LABEL
                        ))}

                        if(canViewBaniSuif){availableDirectoratesList.add(Pair(Directorate.BANI_SUIF,
                            BANI_SUIF_LABEL
                        ))}
                        if(canViewBuhira){availableDirectoratesList.add(Pair(Directorate.BUHIRA,
                            BUHIRA_LABEL
                        ))}

                        if(canViewCairo){availableDirectoratesList.add(Pair(Directorate.CAIRO,CAIRO_LABEL))}

                        if(canViewDamiatta){availableDirectoratesList.add(Pair(Directorate.DAMIATTA,DAMIATTA_LABEL))}
                        if(canViewDakahlia){availableDirectoratesList.add(Pair(Directorate.DAKAHLIA,DAKAHLIA_LABEL))}

                        if(canViewFayum){availableDirectoratesList.add(Pair(Directorate.FAYUM,FAYUM_LABEL))}

                        if(canViewGiza){availableDirectoratesList.add(Pair(Directorate.GIZA, GIZA_LABEL))}
                        if(canViewGharbia){availableDirectoratesList.add(Pair(Directorate.GHARBIA,GHARBIA_LABEL))}

                        if(canViewIsmailia){availableDirectoratesList.add(Pair(Directorate.ISMAILIA,ISMAILIA_LABEL))}

                        if(canViewKafrElShiekh){availableDirectoratesList.add(Pair(Directorate.KAFR_EL_SHEIKH,KAFR_EL_SHEIKH_LABEL))}

                        if(canViewLuxor){availableDirectoratesList.add(Pair(Directorate.LUXOR,LUXOR_LABEL))}

                        if(canViewMinia){availableDirectoratesList.add(Pair(Directorate.MINIA,MINIA_LABEL))}
                        if(canViewMatrouh){availableDirectoratesList.add(Pair(Directorate.MATROUH,MATROUH_LABEL))}
                        if(canViewMenufia){availableDirectoratesList.add(Pair(Directorate.MENUFIA,MENUFIA_LABEL))}

                        if(canViewNewValley){availableDirectoratesList.add(Pair(Directorate.NEW_VALLEY,NEW_VALLEY_LABEL))}
                        if(canViewNorthSinai){availableDirectoratesList.add(Pair(Directorate.NORTH_SINAI,NORTH_SINAI_LABEL))}

                        if(canViewPortSaid){availableDirectoratesList.add(Pair(Directorate.PORT_SAID,PORT_SAID_LABEL))}

                        if(canViewQalubia){availableDirectoratesList.add(Pair(Directorate.QALUBIA,QALUBIA_LABEL))}
                        if(canViewQena){availableDirectoratesList.add(Pair(Directorate.QENA, QENA_LABEL))}

                        if(canViewRedSea){availableDirectoratesList.add(Pair(Directorate.RED_SEA,RED_SEA_LABEL))}

                        if(canViewSharqia){availableDirectoratesList.add(Pair(Directorate.SHARQIA,SHARQIA_LABEL))}
                        if(canViewSuez){availableDirectoratesList.add(Pair(Directorate.SUEZ, SUEZ_LABEL))}
                        if(canViewSohag){availableDirectoratesList.add(Pair(Directorate.SOHAG,SOHAG_LABEL))}

                        directoratesList.value=availableDirectoratesList

                    }

                }
            }
        }
    }
    if(showSaveExcelDialog){
        Dialog(onDismissRequest = {showSaveExcelDialog=false}) {
            ColumnContainer {
                VerticalSpacer()
                Label(SAVE_PROMPT)
                CustomInput(excelFileName, NAME_LABEL)
                CustomButton(label = SAVE_CHANGES_LABEL,
                    enabled = excelFileName.value!= EMPTY_STRING) {
                    if(excelFileName.value!= EMPTY_STRING){
                        val b=body.value
                        b?.let{
                            controller.saveStockExcelFile(it)
                        }
                    }
                    showSaveExcelDialog=false
                }
                VerticalSpacer()
            }
        }
    }
    LaunchedEffect(selectedDirectorate.value) {
        val selectedDirectorateValue=selectedDirectorate.value
        when(selectedSource.value?.first){
            PermissionSector.CERTAIN_DIRECTORATE->{
                selectedDirectorateValue?.first?.let { directorate->
                    val directoratePermission=superUser?.getDirectoratePermission(CRUD.VIEW,directorate)
                    directoratePermission?.let { p->
                        val directorateSlug=p.replaceBefore(":",EMPTY_STRING)
                            .replace("directorate@",EMPTY_STRING)
                            .replace(":",EMPTY_STRING)
                        hospitalController.byCity(directorateSlug)

                    }

                }
            }
            PermissionSector.DIRECTORATE_SECTOR->{
                hospitalController.indexDirectorateHospitals()
            }
            else->{}
        }
    }
    LaunchedEffect(selectedHospital.value) {
        val new= mutableListOf<Hospital>()
        new.addAll(selectedHospitals.value)
        val hasNone= new.none { it==selectedHospital.value }
        if(hasNone) selectedHospital.value?.let{new.add(it)}
        selectedHospital.value=null
        selectedHospitals.value=new
    }
    DatePickerWidget(showStartDatePicker,startDateState,startDate)
    DatePickerWidget(showEndDatePicker,endDateState,endDate)
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = BLOOD_STOCK_LABEL,
        showSheet=showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HomeRoute.route)}
    ) {
        Column(modifier=Modifier.fillMaxSize()) {
            VerticalSpacer(10)
            if(body.value!=null && hospitalsWithStocks.flatMap { it.stocks }.isNotEmpty()) IconButton(R.drawable.ic_save_blue) {showSaveExcelDialog=true}

            VerticalSpacer()
            Row(modifier=Modifier.clickable { showModal=!showModal }.fillMaxWidth()
                .background(BLUE).padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Label(FILTER_LABEL,color=WHITE, paddingEnd = 5, paddingStart = 5)
                Row(verticalAlignment = Alignment.CenterVertically){
                    Label(if(showModal) HIDE_LABEL else SHOW_LABEL, color = WHITE, paddingStart = 5, paddingEnd = 5)
                    Icon(if(showModal)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, background = BLUE)

                }
            }
            AnimatedVisibility(
                visible = showModal,
                enter = fadeIn()+ expandVertically(),
                exit = fadeOut()+ shrinkVertically()
            ) {
                Column(modifier=Modifier.padding(start=10.dp,end=10.dp,bottom = 10.dp)
                    .border(width=1.dp, shape = rcsB(20), color = BLUE)
                    .background(color= WHITE,shape=rcsB(20))) {
                    VerticalSpacer(10)
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        CustomButton(label= FILTER_LABEL,
                            buttonShape = rcs(10),
                            enabledBackgroundColor = Color.Transparent,
                            disabledBackgroundColor = Color.Transparent,
                            enabledFontColor = BLUE,
                            disabledFontColor = GRAY,
                            hasBorder = true,
                            horizontalPadding = 30,
                            icon = if(selectedHospitals.value.isNotEmpty())R.drawable.ic_filter_blue else R.drawable.ic_filter_gray,
                            borderColor = if(selectedHospitals.value.isNotEmpty()) BLUE else GRAY,
                            buttonShadowElevation = 6,
                            enabled = (selectedHospitals.value.isNotEmpty())
                        ) {
                            val created=DailyBloodStockFilterBody(
                                hospitalIds =  if(selectedHospitals.value.isNotEmpty())selectedHospitals.value.mapNotNull { it.id } else null,
                                //bloodGroupIds = if(selectedBloodGroups.value.isNotEmpty())selectedBloodGroups.value.map { it.id?:0 } else null,
                                //sectorIds =  if(selectedSectors.value.isNotEmpty())selectedSectors.value.map { it.id } else null,
                                //typeIds =  if(selectedHospitalTypes.value.isNotEmpty())selectedHospitalTypes.value.map { it.id } else null,
                                //bloodBankIds =  if(selectedBloodBanks.value.isNotEmpty())selectedBloodBanks.value.map { it.id } else null,
                                //bloodUnitTypeId = selectedUnitType.value?.id,
                                startDate = if(startDate.value.trim()!=EMPTY_STRING)startDate.value else null,
                                endDate = if(endDate.value.trim()!=EMPTY_STRING)endDate.value else null,
                                timeBlock = selectedTimeBlock.value?.first,
                            )
                            body.value=created
                            controller.filterHospitalsBloodStock(created)

                        }
                    }

                    LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        item{
                            Row(modifier= Modifier.fillMaxWidth()
                                .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically){
                                if(startDate.value!= EMPTY_STRING){
                                    Row(modifier=Modifier.fillMaxWidth().weight(1f),
                                        verticalAlignment = Alignment.CenterVertically){
                                        LabelSpan(value=startDate.value, label = FROM_LABEL)
                                        if(startDate.value.trim()!=EMPTY_STRING){IconButton(R.drawable.ic_cancel_red) { startDate.value=EMPTY_STRING }}
                                    }
                                }
                                if(endDate.value!= EMPTY_STRING){
                                    Row(modifier=Modifier.fillMaxWidth().weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End){
                                        LabelSpan(value=endDate.value, label = TO_LABEL)
                                        if(endDate.value.trim()!=EMPTY_STRING){IconButton(R.drawable.ic_cancel_red) { endDate.value=EMPTY_STRING }}

                                    }
                                }

                            }
                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomButton(label = DATE_FROM_LABEL ,
                                    enabledBackgroundColor = Color.Transparent,
                                    borderColor = ORANGE, hasBorder = true,
                                    enabledFontColor = ORANGE,
                                    buttonShape = rcs(15),
                                    icon = R.drawable.ic_calendar_month_orange,
                                    onClick = { showStartDatePicker.value = !showStartDatePicker.value })

                                CustomButton(label = DATE_TO_LABEL ,
                                    enabledBackgroundColor = Color.Transparent,
                                    borderColor = ORANGE, hasBorder = true,
                                    enabledFontColor = ORANGE,
                                    buttonShape = rcs(15),
                                    icon = R.drawable.ic_calendar_month_orange,
                                    onClick = { showEndDatePicker.value = !showEndDatePicker.value })
                            }
                            if(startDate.value!= EMPTY_STRING){
                                Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically){
                                    Box(modifier= Modifier.padding(5.dp).fillMaxWidth()){
                                        ComboBox(
                                            hasTitle = true,
                                            title = TIME_LABEL,
                                            loadedItems = timeBlocks,
                                            selectedItem = selectedTimeBlock,
                                            selectedContent = { CustomInput(selectedTimeBlock.value?.second?: SELECT_TIME_LABEL)},
                                            itemContent = {Label(it?.second?:EMPTY_STRING)}
                                        )
                                    }
                                }
                                if(selectedTimeBlock.value!=null){
                                    Row(modifier=Modifier.fillMaxWidth().padding(10.dp)){
                                        Box(modifier= Modifier
                                            .fillMaxWidth()
                                            .weight(1f)){
                                            ComboBox(
                                                title = UNIT_SOURCE_LABEL,
                                                hasTitle = true,
                                                loadedItems = sources,
                                                selectedItem = selectedSource,
                                                itemContent = {Label(it?.second?: EMPTY_STRING)},
                                                selectedContent = { CustomInput(selectedSource.value?.second?: SELECT_UNIT_SOURCE_LABEL)}
                                            )
                                        }
                                    }
                                    if(selectedSource.value!=null){
                                        when(selectedSource.value?.first){
                                            PermissionSector.CERTAIN_DIRECTORATE->{
                                                Row(modifier=Modifier.fillMaxWidth().padding(10.dp)){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            title = DIRECTORATES_LABEL,
                                                            hasTitle = true,
                                                            loadedItems = directoratesList.value,
                                                            selectedItem = selectedDirectorate,
                                                            itemContent = {Label(it?.second?: EMPTY_STRING)},
                                                            selectedContent = { CustomInput(selectedDirectorate.value?.second?: SELECT_DIRECTORATE_LABEL)}
                                                        )
                                                    }
                                                }
                                            }
                                            else->{}
                                        }
                                        if(loadingOptions) LoadingScreen()
                                        if(failOptions) FailScreen()
                                        if(successOptions){
                                            if(hospitals.isNotEmpty()){
                                                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                                                    verticalAlignment = Alignment.Bottom){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 5.dp)
                                                        .weight(1f),
                                                    ){
                                                        ComboBox(
                                                            title = HOSPITALS_LABEL,
                                                            hasTitle = true,
                                                            loadedItems = hospitals,
                                                            selectedItem = selectedHospital,
                                                            itemContent = {Label(it?.name?: EMPTY_STRING)},
                                                            selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                                        )
                                                    }
                                                    CustomButton(label=SELECT_ALL_LABEL,
                                                        enabledBackgroundColor = BLUE,
                                                        buttonShadowElevation = 6,
                                                        buttonShape = RectangleShape,
                                                        onClick = {
                                                            val new= mutableListOf<Hospital>()
                                                            new.addAll(selectedHospitals.value.filter { h->h !in new })
                                                            new.addAll(hospitals.filter { h->h !in new })
                                                            selectedHospitals.value=new
                                                        })
                                                }
                                            }
                                            if(selectedHospitals.value.isNotEmpty()){
                                                Column(modifier=Modifier.fillMaxWidth().padding(5.dp).weight(1f), horizontalAlignment = Alignment.Start){
                                                    Span(HOSPITALS_LABEL, backgroundColor = BLUE, color = WHITE)
                                                    selectedHospitals.value.forEach {
                                                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp).border(width=1.dp, color = Color.LightGray).background(
                                                            WHITE).padding(5.dp),verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween){
                                                            Label(it.name)
                                                            IconButton(R.drawable.ic_delete_red) {
                                                                selectedHospitals.value=selectedHospitals.value.filter { f->f!=it }
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
            LazyColumn {
                item{
                    Column(modifier = Modifier.fillMaxWidth()
                        .scrollable(state = rememberScrollState(),
                            orientation = Orientation.Horizontal))
                    {
                        // Header Row
                        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
                        else{
                            if(success){
                                if(hospitalsWithStocks.isNotEmpty()) BloodStockTable(hospitalsWithStocks.toBloodStockSummaries())
                                else{
                                    Column(modifier=Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center) {
                                        Label(NO_DATA_LABEL, color = Color.Red, fontSize = 26)

                                    }
                                }
                            }
                            if(fail) FailScreen(modifier=Modifier.fillMaxSize())
                        }

                    }
                }
            }

        }

    }
}
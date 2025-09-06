package com.kwdevs.hospitalsdashboard.modules.adminModule.views.pages

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.AreaController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.AdminCreateBloodBankRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalGeneralCreateRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.AdminHospitalUsersIndexRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PermissionSector
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.options.TitlesTypesSectorsCitiesOptionsData
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CURATIVE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EDUCATIONAL_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HIDE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITALS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INSURANCE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SEARCH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPECIALIZED_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcsB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomePage(navHostController: NavHostController){

    val settingsController:SettingsController= viewModel()
    val optionsState            by settingsController.titlesTypesSectorsCitiesOptionsState.observeAsState()
    val hospitalOptionsState    by settingsController.hospitalOptionsState.observeAsState()
    var directorates            by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    var types                   by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    var sectors                 by remember { mutableStateOf<List<Sector>>(emptyList()) }
    var hospitals               by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var loadingHospitals        by remember { mutableStateOf(false) }
    var failHospitals           by remember { mutableStateOf(false) }
    var successHospitals        by remember { mutableStateOf(false) }

    var loadingOptions          by remember { mutableStateOf(false) }
    var failOptions             by remember { mutableStateOf(false) }
    var successOptions          by remember { mutableStateOf(false) }
    var showModal               by remember { mutableStateOf(true) }
    val selectedDirectorate     =  remember { mutableStateOf<CityWithCount?>(null) }
    val selectedType            =  remember { mutableStateOf<HospitalType?>(null) }
    val selectedSector          =  remember { mutableStateOf<Sector?>(null) }
    val showSheet               =  remember { mutableStateOf(false) }
    val selectedFilter          =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedHospital        =  remember { mutableStateOf<Hospital?>(null) }


    val filterList = listOf("Sector","Type","Directorate")

    LaunchedEffect(selectedDirectorate.value) {
        if(selectedDirectorate.value!=null) {
            Log.e("Loading","Loading Directorate")
            val d=selectedDirectorate.value
            settingsController.cityHospitalOptions(d?.id?:0)
            //areas= emptyList();if(selectedArea.value==null && selectedSector.value==null && selectedType.value==null)hospitals= emptyList()
        }

        //areasController.index(selectedDirectorate.value?.id?:0)
    }
    LaunchedEffect(selectedSector.value) {
        if(selectedSector.value==null){
            selectedDirectorate.value=null
            settingsController.sectorHospitalOptions(selectedSector.value?.id?:0)
        }
    }
    LaunchedEffect(selectedType.value) {
        if(selectedType.value==null){
            selectedDirectorate.value=null;
            settingsController.typeHospitalOptions(selectedType.value?.id?:0)
        }
    }
    LaunchedEffect(selectedHospital.value) {
        if(selectedHospital.value!=null)showModal=false
    }
    when(optionsState){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loadingOptions=true;failOptions=false;successOptions=false
            }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loadingOptions=false;failOptions=true;successOptions=false
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loadingOptions=false;failOptions=false;successOptions=true
                val s=optionsState as UiState.Success<TitlesTypesSectorsCitiesOptionsData>
                val r = s.data
                val data=r.data
                directorates=data.cities
                types=data.types
                sectors=data.sectors
            }
        }
        else->{ settingsController.titlesTypesSectorsCitiesOptions() }
    }


    when(hospitalOptionsState){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loadingHospitals=true;failHospitals=false;successHospitals=false
            }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loadingHospitals=false;failHospitals=true;successHospitals=false
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loadingHospitals=false;failHospitals=false;successHospitals=true
                val s=hospitalOptionsState as UiState.Success<HospitalsResponse>
                val r = s.data
                val data=r.data
                hospitals=data
            }

        }
        else->{ }
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION) {
        Container(
            title = "Admin Page",
            showSheet = showSheet
        ) {
            Column(modifier=Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally){
                VerticalSpacer()
                CustomButtonWithImage(label = "Super User View", maxWidth = 82) { navHostController.navigate(HomeRoute.route) }
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .clickable { showModal = !showModal;selectedHospital.value = null }
                    .background(
                        BLUE
                    )
                    .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Label(if(showModal) HIDE_LABEL else SHOW_LABEL, color = WHITE)
                    Icon(background = BLUE, icon = if(showModal) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white)
                }
                AnimatedVisibility(visible = showModal) {
                    ColumnContainer(shape = rcsB(20)){
                        LazyColumn(modifier= Modifier
                            .fillMaxSize()
                            .background(PALE_ORANGE),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            item{
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)){
                                    ComboBox(hasTitle = true,title = "Filter By",
                                        selectedItem = selectedFilter,
                                        loadedItems = filterList,
                                        selectedContent = { CustomInput(selectedFilter.value)}
                                    ) {
                                        Label(it)
                                    }

                                }
                                if(loadingOptions) LoadingScreen()
                                else{
                                    if(failOptions) FailScreen(label="Error Loading Options")
                                    if(successOptions){
                                        when(selectedFilter.value){
                                            filterList[0]->{
                                                Row(modifier= Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            hasTitle = true,
                                                            title = "Sectors",
                                                            selectedItem = selectedSector,
                                                            loadedItems = sectors,
                                                            selectedContent = { CustomInput(selectedSector.value?.name?:"Select Sector")}
                                                        ) {
                                                            Label(it?.name?:EMPTY_STRING)
                                                        }

                                                    }

                                                }
                                            }
                                            filterList[1]->{
                                                Row(modifier= Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            hasTitle = true,
                                                            title = "Types",
                                                            selectedItem = selectedType,
                                                            loadedItems = types,
                                                            selectedContent = { CustomInput(selectedType.value?.name?:"Select Hospital Type")}
                                                        ) {
                                                            Label(it?.name?:EMPTY_STRING)
                                                        }

                                                    }

                                                }
                                            }
                                            filterList[2]->{
                                                Row(modifier= Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            hasTitle = true,
                                                            title = DIRECTORATES_LABEL,
                                                            selectedItem = selectedDirectorate,
                                                            loadedItems = directorates,
                                                            selectedContent = { CustomInput(selectedDirectorate.value?.name?: SELECT_DIRECTORATE_LABEL)}
                                                        ) {
                                                            Label(it?.name?:EMPTY_STRING)
                                                        }

                                                    }
                                                }
                                            }

                                        }
                                        if(selectedFilter.value!= EMPTY_STRING && (selectedDirectorate.value!=null || selectedSector.value!=null || selectedType.value!=null)){
                                            if(loadingHospitals) LoadingScreen()
                                            else{
                                                if(failHospitals) FailScreen(label = "Error Loading Hospitals !!")
                                                if(successHospitals){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 5.dp)){
                                                        ComboBox(
                                                            hasTitle = true,
                                                            title = HOSPITALS_LABEL,
                                                            loadedItems = hospitals,
                                                            selectedItem = selectedHospital,
                                                            selectedContent =
                                                            {
                                                                val searchValue= remember { mutableStateOf(EMPTY_STRING) }
                                                                val originalList= hospitals
                                                                Column {
                                                                    CustomInput(value=searchValue, maxLines = 1, label = SEARCH_LABEL,
                                                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                                                        onTextChange = {text->
                                                                            searchValue.value=text
                                                                            if(text!= EMPTY_STRING) hospitals=originalList.filter { h->h.name.contains(text) }
                                                                            else hospitals=originalList
                                                                        })
                                                                    CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)
                                                                }
                                                            }) {
                                                            Label(it?.name?:EMPTY_STRING)
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
                VerticalSpacer(10)
                if(!showModal){
                    Column(modifier= Modifier
                        .fillMaxSize()
                        .weight(1f)){
                        VerticalSpacer()
                        if(successOptions && successHospitals){
                            selectedHospital.value?.let { it ->
                                val bb=it.bloodBank
                                Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Box(modifier= Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp),
                                        contentAlignment = Alignment.Center){
                                        CustomButton(label = "Edit Modules",
                                            labelFontSize = 12,
                                            enabledBackgroundColor = ORANGE,
                                            buttonShape = RectangleShape,
                                            buttonShadowElevation = 6,) {
                                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                            navHostController.navigate(HospitalModuleSelectorRoute.route)
                                        }

                                    }
                                    Box(modifier= Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp),
                                        contentAlignment = Alignment.Center) {
                                        CustomButton(label = "Users", buttonShadowElevation = 6,
                                            labelFontSize = 12,
                                            enabledBackgroundColor = BLUE, buttonShape = RectangleShape) {
                                            Preferences.ViewTypes().set(ViewType.VIEW_TYPE)
                                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                            navHostController.navigate(AdminHospitalUsersIndexRoute.route)

                                        }
                                    }
                                    Box(modifier= Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp),
                                        contentAlignment = Alignment.Center){
                                        CustomButton(label = "Edit",
                                            enabledBackgroundColor = ORANGE,
                                            labelFontSize = 12,
                                            buttonShape = RectangleShape,
                                            buttonShadowElevation = 6,) {
                                            Preferences.CrudTypes().set(CrudType.UPDATE)
                                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                            navHostController.navigate(HospitalGeneralCreateRoute.route)

                                        }

                                    }
                                    Box(modifier= Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp),
                                        contentAlignment = Alignment.Center){
                                        if(bb!=null){
                                            CustomButton(label = "Edit Blood Bank",
                                                labelFontSize = 12,
                                                enabledBackgroundColor = ORANGE,
                                                buttonShape = RectangleShape,

                                                buttonShadowElevation = 6,) {
                                                Preferences.CrudTypes().set(CrudType.UPDATE)
                                                Preferences.BloodBanks().set(bb)
                                                Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                                navHostController.navigate(AdminCreateBloodBankRoute.route)
                                            }
                                        }
                                        else{
                                            CustomButton(label = "Add Blood Bank",
                                                enabledBackgroundColor = GREEN,
                                                buttonShape = RectangleShape,
                                                labelFontSize = 12,
                                                buttonShadowElevation = 6,) {
                                                Preferences.CrudTypes().set(CrudType.CREATE)
                                                Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                                navHostController.navigate(AdminCreateBloodBankRoute.route)
                                            }
                                        }
                                    }

                                }
                                VerticalSpacer()
                                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                    Label("Blood Bank: ${bb?.name} (${bb?.type?.name})")

                                }
                                VerticalSpacer()
                                val modules = it.modules
                                if(modules.isNotEmpty()){
                                    val chucked=modules.chunked(2)
                                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                                        Span("Modules", backgroundColor = BLUE, color = WHITE)
                                    }
                                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                                        item{
                                            chucked.forEach { subList->
                                                LazyRow(modifier=Modifier.fillMaxWidth()) {
                                                    items(subList){
                                                        Box(modifier=Modifier.padding(5.dp),
                                                            contentAlignment = Alignment.Center){
                                                            ColumnContainer(shape = RectangleShape) {
                                                                Label(it.name?:EMPTY_STRING)
                                                                Span(it.slug?:EMPTY_STRING, backgroundColor = BLUE, color = Color.White)
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



        }

    }

}
package com.kwdevs.hospitalsdashboard.modules.adminModule.views.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.options.TitlesTypesSectorsCitiesOptionsData
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomePage(navHostController: NavHostController){

    val settingsController:SettingsController= viewModel()
    val areasController:AreaController= viewModel()
    val areasState by areasController.state.observeAsState()
    val optionsState by settingsController.titlesTypesSectorsCitiesOptionsState.observeAsState()
    val hospitalOptionsState by settingsController.hospitalOptionsState.observeAsState()
    var areas by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }
    val selectedArea = remember { mutableStateOf<AreaWithCount?>(null) }
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    val selectedCity = remember { mutableStateOf<CityWithCount?>(null) }
    var types by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    val selectedType = remember { mutableStateOf<HospitalType?>(null) }
    var sectors by remember { mutableStateOf<List<Sector>>(emptyList()) }
    val selectedSector = remember { mutableStateOf<Sector?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val filterList = listOf("Sector","Type","City")
    val selectedFilter = remember { mutableStateOf("") }
    var hospitals by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    val selectedHospital = remember { mutableStateOf<Hospital?>(null) }
    LaunchedEffect(selectedCity.value) {
        if(selectedCity.value==null) {
            areas= emptyList()
            if(selectedArea.value==null && selectedSector.value==null && selectedType.value==null)hospitals= emptyList()
        }
        areasController.index(selectedCity.value?.id?:0)
    }
    LaunchedEffect(selectedSector.value) {
        if(selectedSector.value==null){selectedCity.value=null}
        settingsController.sectorHospitalOptions(selectedSector.value?.id?:0)
    }
    LaunchedEffect(selectedType.value) {
        if(selectedType.value==null){selectedCity.value=null;}
        settingsController.typeHospitalOptions(selectedType.value?.id?:0)

    }
    LaunchedEffect(selectedArea.value) {
        settingsController.areaHospitalOptions(selectedArea.value?.id?:0)
    }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s=optionsState as UiState.Success<TitlesTypesSectorsCitiesOptionsData>
            val r = s.data
            val data=r.data
            cities=data.cities
            types=data.types
            sectors=data.sectors
        }
        else->{
            settingsController.titlesTypesSectorsCitiesOptions()
        }
    }
    when(areasState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s=areasState as UiState.Success<AreaWithCountResponse>
            val r = s.data
            val data=r.data
            areas=data

        }
        else->{
        }
    }
    when(hospitalOptionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s=hospitalOptionsState as UiState.Success<HospitalsResponse>
            val r = s.data
            val data=r.data
            hospitals=data

        }
        else->{
        }
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION) {
        Container(
            title = "Admin Page",
            showSheet = showSheet
        ) {
            VerticalSpacer()
            CustomButtonWithImage(label = "Super User View", maxWidth = 82) {
                navHostController.navigate(HomeRoute.route)
            }
            Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                ComboBox(
                    hasTitle = true,
                    title = "Filter By",
                    selectedItem = selectedFilter,
                    loadedItems = filterList,
                    selectedContent = { CustomInput(selectedFilter.value)}
                ) {
                    Label(it)
                }

            }
            when(selectedFilter.value){
                filterList[0]->{
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                            ComboBox(
                                hasTitle = true,
                                title = "Sectors",
                                selectedItem = selectedSector,
                                loadedItems = sectors,
                                selectedContent = { CustomInput(selectedSector.value?.name?:"Select Sector")}
                            ) {
                                Label(it?.name?:"")
                            }

                        }

                    }
                }
                filterList[1]->{
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                            ComboBox(
                                hasTitle = true,
                                title = "Types",
                                selectedItem = selectedType,
                                loadedItems = types,
                                selectedContent = { CustomInput(selectedType.value?.name?:"Select Hospital Type")}
                            ) {
                                Label(it?.name?:"")
                            }

                        }

                    }
                }
                filterList[2]->{
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                            ComboBox(
                                hasTitle = true,
                                title = "Cities",
                                selectedItem = selectedCity,
                                loadedItems = cities,
                                selectedContent = { CustomInput(selectedCity.value?.name?:"Select City")}
                            ) {
                                Label(it?.name?:"")
                            }

                        }

                    }
                    if(areas.isNotEmpty()){
                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                ComboBox(
                                    hasTitle = true,
                                    title = "Areas",
                                    selectedItem = selectedArea,
                                    loadedItems = areas,
                                    selectedContent = { CustomInput(selectedArea.value?.name?:"Select Area")}
                                ) {
                                    Label(it?.name?:"")
                                }

                            }

                        }
                    }
                }

            }
            if(hospitals.isNotEmpty()){
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(
                        hasTitle = true,
                        title = "Hospitals",
                        selectedItem = selectedHospital,
                        loadedItems = hospitals,
                        selectedContent = { CustomInput(selectedHospital.value?.name?:"Select Hospital")}
                    ) {
                        Label(it?.name?:"")
                    }
                }
                selectedHospital.value?.let { it ->
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label = "Edit Modules",
                            enabledBackgroundColor = ORANGE,
                            buttonShadowElevation = 6,) {
                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                            navHostController.navigate(HospitalModuleSelectorRoute.route)
                        }

                        CustomButton(label = "Users", buttonShadowElevation = 6) {
                            Preferences.ViewTypes().set(ViewType.VIEW_TYPE)
                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                            navHostController.navigate(AdminHospitalUsersIndexRoute.route)

                        }

                        CustomButton(label = "Edit",
                            enabledBackgroundColor = BLUE,
                            buttonShadowElevation = 6,) {
                            Preferences.CrudTypes().set(CrudType.UPDATE)
                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                            navHostController.navigate(HospitalGeneralCreateRoute.route)

                        }

                    }

                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        if(selectedHospital.value?.bloodBank!=null){
                            CustomButton(label = "Edit Blood Bank Main Data",
                                enabledBackgroundColor = ORANGE,
                                buttonShadowElevation = 6,) {
                                Preferences.CrudTypes().set(CrudType.UPDATE)
                                val bb=it.bloodBank
                                bb?.let{b-> Preferences.BloodBanks().set(b)}
                                Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                navHostController.navigate(AdminCreateBloodBankRoute.route)
                            }
                        }else{
                            CustomButton(label = "Add Blood Bank to Hospital",
                                enabledBackgroundColor = GREEN,
                                buttonShadowElevation = 6,) {
                                Preferences.CrudTypes().set(CrudType.CREATE)
                                Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                                navHostController.navigate(AdminCreateBloodBankRoute.route)
                            }
                        }


                        CustomButton(label = "Users", buttonShadowElevation = 6) {
                            Preferences.ViewTypes().set(ViewType.VIEW_TYPE)
                            Preferences.Hospitals().set(ModelConverter().convertHospitalToSimple(it))
                            navHostController.navigate(AdminHospitalUsersIndexRoute.route)

                        }
                    }

                    val modules = it.modules
                    if(modules.isNotEmpty()){
                        val chucked=modules.chunked(2)
                        Row(modifier=Modifier.fillMaxWidth()){
                            Span("Modules", backgroundColor = BLUE, color = WHITE)
                        }
                        chucked.forEach { subList->
                            LazyRow {
                                items(subList){
                                    Box(modifier=Modifier.padding(5.dp),
                                        contentAlignment = Alignment.Center){
                                        ColumnContainer(shape = RectangleShape) {
                                            Label(it.name?:"")
                                            Span(it.slug?:"", backgroundColor = BLUE, color = Color.White)
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
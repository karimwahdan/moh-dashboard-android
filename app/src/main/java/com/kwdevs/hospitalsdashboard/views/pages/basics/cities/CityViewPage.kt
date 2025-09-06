package com.kwdevs.hospitalsdashboard.views.pages.basics.cities

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.CityController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreaSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreas
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTORS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CITY_HEAD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.routes.AreaViewRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityViewPage(navHostController: NavHostController){
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

    val controller:CityController= viewModel()
    val state by controller.singleCityWithAreaState.observeAsState()
    var city by remember { mutableStateOf<CityWithAreas?>(null) }
    var areas by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }
    var hospitals by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }

    when(state){
        is UiState.Loading->{loading=true;fail=false;success=false}
        is UiState.Error->{loading=false;fail=true;success=false}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<CityWithAreaSingleResponse>
                val response = s.data
                val data = response.data
                city=data
                areas=data.areas?: emptyList()
                val allHospitals=data.hospitals?: emptyList()
                hospitals=allHospitals.filter { it.sectorId==5 }
            }

        }
        else->{
            LaunchedEffect(Unit) {
                val saved=Preferences.Cities().get()
                saved?.let{controller.view(it.id?:0)}
            }
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


                        canBrowseCities=permissions.contains(BROWSE_CITY)
                        canReadCities=permissions.contains(READ_CITY)

                        canBrowseAreas=permissions.contains(BROWSE_AREA)
                        canReadAreas=permissions.contains(READ_AREA)

                        canBrowseSectors=permissions.contains(BROWSE_SECTORS)
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


    if(superUser!=null){
        Column(modifier= Modifier.fillMaxWidth()
        ) {
            if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
            if(fail) FailScreen(modifier = Modifier.fillMaxSize())
            if(success){
                Container(
                    title = city?.name?:"تفاصيل المدينة",
                    showSheet = showSheet,
                    headerShowBackButton = true,
                    headerIconButtonBackground = BLUE,
                    headerOnClick = { navHostController.navigate(HomeRoute.route) }

                ) {
                    if(city!=null){
                        LazyColumn(modifier= Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ){
                            item{
                                VerticalSpacer(20)
                                VerticalSpacer(space)

                                val chucked=areas.chunked(3)
                                chucked.forEach {list->
                                    if(list.size==6){
                                        Row(){
                                            list.forEach {item ->
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(5.dp)){
                                                    AreaCard(item, Modifier.fillMaxWidth()){
                                                        Preferences.Areas().set(Area(id=item.id,cityId= item.cityId, name = item.name))
                                                        navHostController.navigate(AreaViewRoute.route)
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    else if(list.size==5){
                                        Row(){
                                            list.forEach {item ->
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(5.dp)){
                                                    AreaCard(item, Modifier.fillMaxWidth()){
                                                        Preferences.Areas().set(Area(id=item.id,cityId= item.cityId, name = item.name))
                                                        navHostController.navigate(AreaViewRoute.route)
                                                    }
                                                }
                                            }
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                ColumnContainer {
                                                    VerticalSpacer(32)
                                                    Label("These are the Last Items", maximumLines = Int.MAX_VALUE)
                                                    VerticalSpacer(32)
                                                }
                                            }

                                        }
                                    }
                                    else if(list.size==4){
                                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                            list.forEach { item ->
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                    AreaCard(item, Modifier.fillMaxWidth()){
                                                        Preferences.Areas().set(Area(id=item.id,cityId= item.cityId, name = item.name))
                                                        navHostController.navigate(AreaViewRoute.route)
                                                    }
                                                }
                                            }
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                ColumnContainer {
                                                    VerticalSpacer(32)
                                                    Label("This is the Last Item", maximumLines = Int.MAX_VALUE)
                                                    VerticalSpacer(32)
                                                }
                                            }
                                        }
                                    }
                                }
                                hospitals.forEach {item->
                                    Box(modifier=Modifier.fillMaxWidth().weight(1f).padding(5.dp)){
                                        SimpleHospitalCard(item, navHostController)
                                    }
                                }
                                VerticalSpacer(space)
                            }
                        }
                    }

                }
            }

        }
    }

}


@Composable
private fun AreaCard(item:AreaWithCount, modifier: Modifier=Modifier,onCLick:()->Unit){
    Column(
        modifier = modifier
            .shadow(elevation = 5.dp, shape = rcs(5))
            .background(color= Color.White, shape = rcs(5))
            .border(width = 1.dp, shape = rcs(5), color = Color.LightGray)
            .clickable { onCLick.invoke() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        VerticalSpacer()
        Icon(R.drawable.ic_home_work_white,size=32, containerSize = 36, background = colorResource(R.color.blue))
        Label(item.name?: EMPTY_STRING, fontSize = 18)
        VerticalSpacer()
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Label("المستشفيات")
            Span("${item.hospitalsCount}", color = Color.White, backgroundColor = if((item.hospitalsCount?:0)>0) GREEN else Color.Red)
        }
        VerticalSpacer()
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
            Label(name, fontWeight = FontWeight.Bold, maximumLines = 5)

            Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
                Icon(if (active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                IconButton(R.drawable.ic_view_timeline_blue) {
                    val simple= ModelConverter().convertHospitalToSimple(item)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalModuleSelectorRoute.route)
                }
            }
            Row(modifier=Modifier.fillMaxWidth()){
                Span(sector?.name?:EMPTY_STRING, fontSize = 14, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5)
                Span(type?.name?:EMPTY_STRING, fontSize = 14, color = WHITE, backgroundColor = ORANGE, paddingStart = 5, paddingEnd = 5)

            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                Span(city?.name?:EMPTY_STRING, fontSize = 14, color = WHITE, backgroundColor = BLUE)
                HorizontalSpacer()
                Span(hospitalArea?.name?:EMPTY_STRING, fontSize = 14, color = WHITE, backgroundColor = BLACK)
                if(item.isNBTS==true) Icon(R.drawable.ic_blood_drop)
            }
            VerticalSpacer()
            Row(modifier=Modifier.padding(horizontal = 5.dp)){
                Label(text=address?:EMPTY_STRING)
            }
            VerticalSpacer()
        }
    }
}

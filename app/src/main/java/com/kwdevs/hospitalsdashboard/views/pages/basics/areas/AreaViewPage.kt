package com.kwdevs.hospitalsdashboard.views.pages.basics.areas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetails
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalGeneralCreateRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_SECTORS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CITY_HEAD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CREATE_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_AREA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_CITY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_HOSPITAL_TYPE
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.READ_SECTOR
import com.kwdevs.hospitalsdashboard.routes.CityViewRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.RIGHT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.LOGIN_FIRST_FIRST_LABEL
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
fun AreaViewPage(navHostController: NavHostController){
    val context= LocalContext.current
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
    var canCreateHospitals by remember { mutableStateOf(false) }

    var canBrowseHospitals by remember { mutableStateOf(false) }
    var canReadHospitals by remember { mutableStateOf(false) }

    val controller :AreaController= viewModel()
    val state by controller.singleState.observeAsState()
    var area by remember { mutableStateOf<AreaWithHospitalDetails?>(null) }
    var hospitals by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf("") }
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

                        canBrowseSectors=permissions.contains(BROWSE_SECTORS)
                        canReadSectors=permissions.contains(READ_SECTOR)

                        canBrowseHospitalTypes=permissions.contains(BROWSE_HOSPITAL_TYPE)
                        canReadHospitalTypes=permissions.contains(READ_HOSPITAL_TYPE)


                    }
                }
                else{
                    canBrowseHospitals=false
                    canReadHospitals=false
                    canCreateHospitals=false

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

    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loading=true;fail=false;success=false
            }
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
                val s = state as UiState.Success<AreaWithHospitalDetailSingleResponse>
                val response = s.data
                val data = response.data
                area=data
                hospitals=data.hospitals?: emptyList()
            }
        }
        else->{
            LaunchedEffect(Unit) {
                val saved= Preferences.Areas().get()
                saved?.let{controller.view(it.id?:0)}
            }

        }
    }
    val showSheet= remember { mutableStateOf(false) }
    CompositionLocalProvider(RIGHT_LAYOUT_DIRECTION) {
        if(superUser!=null){
            Container(
                showSheet = showSheet,
                headerShowBackButton = true,
                headerIconButtonBackground = BLUE,
                title = "$AREA_LABEL ${area?.name?:""} - ${area?.city?.name?:""}",
                headerOnClick = {
                    navHostController.navigate( if(canBrowseCities) CityViewRoute.route else HomeRoute.route )
                }
            ) {
                if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
                if(success){
                    if(area!=null){
                        LazyColumn(modifier= Modifier.fillMaxWidth()
                        ){
                            item{
                                VerticalSpacer(20)
                                Box(modifier=Modifier.padding(horizontal = 5.dp)){
                                    Label(text = "${hospitals.size}",label="الاجمالى:")

                                }
                                VerticalSpacer()
                                val chucked=hospitals.chunked(2)
                                chucked.forEach { list->
                                    if(list.size>1){
                                        Row(modifier=Modifier.fillMaxWidth()){
                                            list.forEach {
                                                    item ->
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                    SimpleHospitalCard(item,navHostController)
                                                }

                                            }
                                        }

                                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                ColumnContainer {
                                                    VerticalSpacer()
                                                    Box(modifier=Modifier.padding(5.dp)){
                                                        CustomButtonWithImage(
                                                            enabled = canCreateHospitals,
                                                            label = "Add new Hospital",
                                                            maxWidth = 82,
                                                            icon = R.drawable.ic_hospital_white,
                                                            background = BLUE
                                                        ) {
                                                            if(canCreateHospitals){
                                                                Preferences.CrudTypes().set(CrudType.CREATE)
                                                                navHostController.navigate(
                                                                    HospitalGeneralCreateRoute.route)
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f))

                                        }
                                    }
                                    else{
                                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                            list.forEach { item ->
                                                Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                    SimpleHospitalCard(item,navHostController)
                                                }
                                            }
                                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){
                                                ColumnContainer {
                                                    VerticalSpacer()
                                                    Box(modifier=Modifier.padding(5.dp)){
                                                        CustomButtonWithImage(
                                                            enabled = canCreateHospitals,
                                                            label = "Add new Hospital",
                                                            maxWidth = 82,
                                                            icon = R.drawable.ic_hospital_white,
                                                            background = BLUE
                                                        ) {
                                                            if(canCreateHospitals){
                                                                Preferences.CrudTypes().set(CrudType.CREATE)
                                                                navHostController.navigate(
                                                                    HospitalGeneralCreateRoute.route)
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
                if(fail){
                    FailScreen(modifier=Modifier.fillMaxSize(),
                        errors=errors,
                        message = errorMessage)
                }
            }
        }
        else{
            LaunchedEffect(Unit) {
                navHostController.navigate(LoginRoute.route)
                Toast.makeText(context,LOGIN_FIRST_FIRST_LABEL,Toast.LENGTH_SHORT).show()

            }
        }
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
            navHostController.navigate(HospitalViewRoute.route)
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
                Span(sector?.name?:"", fontSize = 14, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5)
                Span(type?.name?:"", fontSize = 14, color = WHITE, backgroundColor = ORANGE, paddingStart = 5, paddingEnd = 5)

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

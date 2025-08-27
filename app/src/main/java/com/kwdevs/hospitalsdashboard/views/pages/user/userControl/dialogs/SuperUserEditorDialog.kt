package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.SuperUserBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.controller.AreaController
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.sector.SectorResponse
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_SUPER_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TITLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TITLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.USERNAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen

@Composable
fun NewSuperUserDialog(showDialog: MutableState<Boolean>, permissionsController: PermissionsController) {
    val userController      : UsersController = viewModel()
    val controller          : HomeController = viewModel()
    val areasController     : AreaController = viewModel()
    val hospitalController  : HospitalController = viewModel()
    val userState           by userController.hospitalUserSingleState.observeAsState()
    val dataState           by controller.singleState.observeAsState()
    val areasState          by areasController.state.observeAsState()
    val hospitalState       by hospitalController.state.observeAsState()

    var cities              by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    var areas               by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }
    var titles              by remember { mutableStateOf<List<Title>>(emptyList()) }
    var hospitals           by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var types               by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    var sectors             by remember { mutableStateOf<List<Sector>>(emptyList()) }
    val selectedCity        =  remember { mutableStateOf<CityWithCount?>(null) }
    val selectedArea        =  remember { mutableStateOf<AreaWithCount?>(null) }
    val selectedTitle       =  remember { mutableStateOf<Title?>(null) }
    val selectedType        =  remember { mutableStateOf<HospitalType?>(null) }
    val selectedSector      = remember { mutableStateOf<Sector?>(null) }
    val name                = remember { mutableStateOf(EMPTY_STRING) }
    val username            = remember { mutableStateOf(EMPTY_STRING) }
    val password            = remember { mutableStateOf(EMPTY_STRING) }
    val nationalId          = remember { mutableStateOf(EMPTY_STRING) }
    val active              = remember { mutableStateOf(true) }
    var stale by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    if(showDialog.value){
        when(userState)    {
            is UiState.Loading->{
                loading=true;success=false;fail=false;stale=false

            }
            is UiState.Error->{
                loading=false;success=false;fail=true;stale=false
                val s = userState as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL

            }
            is UiState.Success->{ LaunchedEffect(Unit) {
                loading=false;success=true;fail=false;stale=false
                showDialog.value=false
                permissionsController.index()
            } }
            else->{
                stale=true
            }
        }
        when(areasState)   {
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = areasState as UiState.Success<AreaWithCountResponse>
                val response = s.data
                val data=response.data
                areas=data
            }
            is UiState.Reload->{}
            else->{}
        }
        when(dataState)    {
            is UiState.Loading->{
            }
            is UiState.Error->{
            }
            is UiState.Success->{
                val s = dataState as UiState.Success<HomeResponse>
                val response = s.data
                val data=response.data
                titles=data.titles
                cities=data.cities
                sectors=data.sectors
                types=data.types
            }
            else->{ controller.getHome() }
        }
        when(hospitalState){
            is UiState.Success->{
                val s = hospitalState as UiState.Success<HospitalsResponse>
                val response = s.data
                val data=response.data
                hospitals=data
            }
            else->{}
        }
        LaunchedEffect(selectedCity.value) {
            if(selectedCity.value!=null){
                val v=selectedCity.value
                v?.let {areasController.index(v.id?:0)}
            }
        }
        LaunchedEffect(
            selectedCity.value,
            selectedArea.value,
            selectedType.value,
            selectedSector.value) {
            if(selectedCity.value!=null &&
                selectedArea.value!=null &&
                selectedType.value!=null &&
                selectedSector.value!=null){
                val selectedC=selectedCity.value
                val selectedA=selectedArea.value
                val selectedT=selectedType.value
                val selectedS=selectedSector.value
                val filterBody= HospitalFilterBody(
                    cityId = selectedC?.id,
                    areaId = selectedA?.id,
                    sectorId = selectedS?.id,
                    typeId = selectedT?.id
                )
                hospitalController.filter(filterBody)
            }
        }
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                Column(modifier= Modifier.fillMaxSize().padding(5.dp)){
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center){
                            Label(ADD_NEW_SUPER_USER_LABEL)
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween){
                            IconButton(R.drawable.ic_cancel_red, paddingStart = 10, paddingEnd = 10) {showDialog.value=false }
                            IconButton(R.drawable.ic_check_circle_green, paddingStart = 10, paddingEnd = 10, onClick = {
                                if(username.value.trim()!= EMPTY_STRING && password.value.trim()!= EMPTY_STRING
                                    && selectedTitle.value!=null){
                                    val body= SuperUserBody(
                                        name=name.value,
                                        username=username.value,
                                        password = password.value,
                                        titleId = selectedTitle.value?.id,
                                        nationalId = nationalId.value,
                                        active = if(active.value) 1 else 0
                                    )
                                    userController.createSuper(body)
                                    showDialog.value=false
                                }
                            })
                        }
                    }
                    VerticalSpacer()
                    if(stale){
                        LazyColumn(modifier= Modifier.fillMaxSize()) {
                            item{
                                ComboBox(title= TITLE_LABEL, loadedItems = titles, selectedItem = selectedTitle,
                                    selectedContent = {
                                        CustomInput(selectedTitle.value?.name?: SELECT_TITLE_LABEL,readOnly = true)
                                    })
                                { Label(it?.name?: EMPTY_STRING) }

                                CustomInput(value=name, label = NAME_LABEL)

                                CustomInput(value=username, label = USERNAME_LABEL)

                                CustomInput(value=password, label = PASSWORD_LABEL)
                                CustomInput(value=nationalId, label = NATIONAL_ID_LABEL)

                                CustomCheckbox(label = ACTIVE_LABEL,active=active)
                            }
                        }

                    }
                    else{
                        if(loading) LoadingScreen(modifier= Modifier.fillMaxSize())
                        if(fail){
                            FailScreen(
                                modifier = Modifier.fillMaxSize(),
                                errors=errors,
                                message = errorMessage
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuperUserRoleEditorDialog(
    showDialog: MutableState<Boolean>,
    user: SimpleSuperUser,
    roles: List<Role>,
    controller: PermissionsController
) {
    val selectedRole= remember { mutableStateOf<Role?>(null) }
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp), contentAlignment = Alignment.Center){
                    ComboBox(
                        loadedItems = roles, selectedItem = selectedRole, selectedContent = {
                            CustomInput(selectedRole.value?.name?: SELECT_ROLE_LABEL)
                        }
                    ) {
                        Label(it?.name?: EMPTY_STRING)
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                    IconButton(R.drawable.ic_add_circle_green) {
                        if(user.id!=null && selectedRole.value!=null){
                            selectedRole.value?.let {
                                controller.addRoleToSuperUser(userId = user.id, roleId = it.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuperUserDetailsDialog(showDialog: MutableState<Boolean>, user: SimpleSuperUser?,
                                   loadedRoles: List<Role>,controller: PermissionsController){
    val roles=user?.roles
    val showUserRoleEditorDialog= remember { mutableStateOf(false) }
    val showSectorHeadDialog= remember { mutableStateOf(false) }
    val showCityHeadDialog= remember { mutableStateOf(false) }

    if(showDialog.value && user!=null){
        SuperUserRoleEditorDialog(showDialog=showUserRoleEditorDialog,user=user,roles=loadedRoles, controller =controller )
        SectorHeadDialog(showSectorHeadDialog,user, controller)
        CityHeadDialog(showCityHeadDialog,user, controller)
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(vertical = 5.dp,horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Label(user.name)
                    if(user.isSuper){
                        Span(text="Super", backgroundColor = GREEN, color = WHITE)
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Label(user.title?.name?: EMPTY_STRING)

                }
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Label("Add Role")
                        IconButton(R.drawable.ic_add_circle_green) {
                            showUserRoleEditorDialog.value=true
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Label("Set Sector Head")
                        IconButton(R.drawable.ic_edit_blue) {
                            showSectorHeadDialog.value=true
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Label("Set City Head")
                        IconButton(R.drawable.ic_edit_blue) {
                            showCityHeadDialog.value=true
                        }
                    }
                }
                roles?.let {
                    roles.forEach {
                        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Span(text=it.name, backgroundColor = ORANGE, color = WHITE)
                            if(user.id!=null){
                                IconButton(R.drawable.ic_delete_red) {controller.removeRoleFromSuperUser(userId = user.id, roleId = it.id) }
                            }
                        }
                        val permissions=it.permissions
                        permissions.forEach { p->
                            Row(modifier=Modifier.fillMaxWidth()){
                                Label(p.name?: EMPTY_STRING, paddingStart = 10, paddingEnd = 10)

                            }
                        }
                    }
                }
                VerticalSpacer()
            }
        }
    }
}

@Composable
fun CityHeadDialog(showDialog: MutableState<Boolean>, user: SimpleSuperUser?,controller: PermissionsController){
    val settingsController:SettingsController= viewModel()
    val citiesState by settingsController.citiesState.observeAsState()
    val selectedCity = remember { mutableStateOf<CityWithCount?>(null) }
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    if(showDialog.value){
        when(citiesState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s=citiesState as UiState.Success<CityWithCountResponse>
                val r = s.data
                val data=r.data
                cities=data
            }
            else->{settingsController.citiesOptions()}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){ IconButton(R.drawable.ic_cancel_red){showDialog.value=false} }
                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp), contentAlignment = Alignment.Center){
                    ComboBox(selectedItem = selectedCity, loadedItems = cities, selectedContent = {
                        CustomInput(selectedCity.value?.name?: SELECT_CITY_LABEL)
                    }) {
                        Label(it?.name?: EMPTY_STRING)
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                        IconButton(R.drawable.ic_check_circle_green){
                            if(user!=null){
                                selectedCity.value?.let{
                                    val userId=user.id
                                    userId?.let { id->
                                        val cityId=it.id
                                        cityId?.let {cid->
                                            controller.addCityHead(userId=id, cityId = cid)
                                        }

                                    }

                                }

                            }
                        } }

                }
            }
        }
    }
}

@Composable
fun SectorHeadDialog(showDialog: MutableState<Boolean>, user: SimpleSuperUser?,controller: PermissionsController){
    val settingsController:SettingsController= viewModel()
    val sectorsState by settingsController.sectorsState.observeAsState()
    val selectedSector = remember { mutableStateOf<Sector?>(null) }
    var sectors by remember { mutableStateOf<List<Sector>>(emptyList()) }
    if(showDialog.value){
        when(sectorsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s=sectorsState as UiState.Success<SectorResponse>
                val r = s.data
                val data=r.data
                sectors=data
            }
            else->{settingsController.sectorsIndex()}
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){ IconButton(R.drawable.ic_cancel_red){showDialog.value=false} }
                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp), contentAlignment = Alignment.Center){
                    ComboBox(selectedItem = selectedSector, loadedItems = sectors, selectedContent = {
                        CustomInput(selectedSector.value?.name?: SELECT_SECTOR_LABEL)
                    }) {
                        Label(it?.name?: EMPTY_STRING)
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                        IconButton(R.drawable.ic_check_circle_green){
                            if(user!=null){
                                selectedSector.value?.let{
                                    val userId=user.id
                                    userId?.let { id->
                                        controller.addSectorHead(userId=id, sectorId = it.id)


                                    }

                                }

                            }
                        } }

                }
            }
        }
    }
}




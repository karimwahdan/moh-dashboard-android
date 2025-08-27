package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createPage

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.controller.AreaController
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.options.TitlesTypesSectorsCitiesOptionsData
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_ADDRESS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TRY_AGAIN_LATER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.crud.AreaSelector
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.crud.CitySelector
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalGeneralCreatePage(navHostController: NavHostController){

    val hospital = Preferences.Hospitals().get()
    val crudType=Preferences.CrudTypes().get()
    val controller : HospitalController = viewModel()
    val state by controller.singleState.observeAsState()
    val user=Preferences.User().getSuper()
    val accountType=Preferences.User().getType()
    val name = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(true) }
    val isNbts = remember { mutableStateOf(false) }
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.titlesTypesSectorsCitiesOptionsState.observeAsState()
    var sectors by remember { mutableStateOf<List<Sector>>(emptyList()) }
    val selectedSector = remember { mutableStateOf<Sector?>(null) }

    var hospitalTypes by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
    val selectedHospitalType = remember { mutableStateOf<HospitalType?>(null) }

    val selectedCity = remember { mutableStateOf<CityWithCount?>(null) }
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    val showSheet = remember { mutableStateOf(false) }
    val selectedArea = remember { mutableStateOf<AreaWithCount?>(null)}
    var areas by remember { mutableStateOf<List<AreaWithCount>>(emptyList()) }
    val areaController: AreaController = viewModel()
    val areaState by areaController.state.observeAsState()
    var loadingAreas by remember { mutableStateOf(true) }
    var successAreas by remember { mutableStateOf(false) }
    var failAreas by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var model by remember { mutableStateOf<Hospital?>(null) }
    val showSavePromptDialog = remember { mutableStateOf(false) }
    LaunchedEffect(selectedCity.value) {
        if(selectedCity.value!=null) areaController.index(selectedCity.value?.id?:0)
    }
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
        else->{ }
    }
    LaunchedEffect(selectedCity.value,selectedArea.value,selectedSector.value,selectedHospitalType.value,name.value) {
        if(selectedCity.value!=null && selectedArea.value!=null && selectedSector.value !=null && selectedHospitalType.value!=null && name.value!= EMPTY_STRING ){
            isValid=true
        }
        else isValid=false
    }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<TitlesTypesSectorsCitiesOptionsData>
            val r = s.data
            val data=r.data
            cities=data.cities
            sectors=data.sectors
            hospitalTypes=data.types
        }
        else->{settingsController.titlesTypesSectorsCitiesOptions()}
    }
    when(state){
        is UiState.Loading->{ loading=true;fail=false;success=false }
        is UiState.Error->{
            loading=false;fail=true;success=false
            val s = state as UiState.Error
            val exception=s.exception
            errorMessage=exception.message?:""
            LaunchedEffect(Unit) {
                showSheet.value=true
                showSavePromptDialog.value=false
            }
        }
        is UiState.Success->{
            loading=false;fail=false;success=true
            LaunchedEffect(Unit) {
                showSheet.value=true
                showSavePromptDialog.value=false
                val s = state as UiState.Success<HospitalSingleResponse>
                val r = s.data
                val data=r.data
                data?.let{
                    val simple= ModelConverter().convertHospitalToSimple(it)
                    Preferences.Hospitals().set(simple)
                }
                navHostController.navigate(HospitalModuleSelectorRoute.route)
            }
        }
        else->{ loading=false;fail=false;success=false }
    }
    val showNotes = remember { mutableStateOf(true) }
    val showFields = remember { mutableStateOf(false) }
    val showConfirmatoryButtons= remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if(crudType==CrudType.UPDATE){
            if(hospital!=null){
                name.value=hospital.name?:""
                address.value=hospital.address?:""
                active.value=hospital.active?:false
                isNbts.value=hospital.isNbts?:false
                selectedSector.value=hospital.sector
                selectedHospitalType.value=hospital.type
                selectedArea.value= AreaWithCount(id=hospital.area?.id?:0, cityId = hospital.area?.cityId?:0,name=hospital.area?.name)
                selectedCity.value= CityWithCount(id=hospital.city?.id, name = hospital.city?.name)
            }
        }
    }
    SaveHospitalDialog(
        showDialog =  showSavePromptDialog,
        model= model) {
        val body=HospitalBody(
            id = if(hospital!=null) hospital.id else null,
            name = name.value,
            address = address.value,
            cityId = selectedCity.value?.id,
            areaId = selectedArea.value?.id,
            sectorId = selectedSector.value?.id,
            typeId = selectedHospitalType.value?.id,
            active = if(active.value) 1 else 0,
            isNbts = if(isNbts.value) 1 else 0,
            createdBySuperId = user?.id,
            accountType = if(accountType==ViewType.SUPER_USER) 1 else 2
        )
        if(isValid){
            if(Preferences.CrudTypes().get()==CrudType.CREATE){
                controller.store(body)
            }else{
                controller.update(body)
            }
            showSavePromptDialog.value=false
        }
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION){
        Container(
            title = if(crudType==CrudType.CREATE) "Create New Hospital" else "Update Hospital",
            showSheet = showSheet,
            headerShowBackButton = true,
            headerIconButtonBackground = BLUE,
            headerOnClick = {navHostController.navigate(HomeRoute.route)},
            sheetColor = if(!loading && !fail && !success){if(isValid) GREEN else Color.Red} else {if(fail) Color.Red else if(success) GREEN else BLUE},
            sheetOnClick = {
                if(success || fail){
                    loading=false;fail=false;success=false
                    name.value=""
                    address.value=""
                }
            },
            sheetContent = {
                Label(
                    text=if(!loading && !fail && !success){if(!isValid) "Some data is not valid" else ""} else {if(success)"Data Saved Successfully" else if(fail) errorMessage else ""}
                )
            }
        ) {
            Box(modifier=Modifier.fillMaxWidth().background(BLUE), contentAlignment = Alignment.Center){
                Label("Welcome to hospital Creator", color = WHITE, fontSize = 24, fontWeight = FontWeight.Bold)
            }
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                item{
                    if(crudType==CrudType.CREATE){
                        Box(modifier=Modifier.fillMaxWidth().background(WHITE), contentAlignment = Alignment.TopStart){
                            Label("First Step is to Insert basic data",
                                textAlign = TextAlign.Start,
                                color = BLACK, fontSize = 16,
                                fontWeight = FontWeight.Bold,
                                paddingStart = 5)
                        }
                    }
                    Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
                        Label("Form Helper notes")
                        HorizontalSpacer()
                        Box(modifier=Modifier.clickable { showNotes.value=!showNotes.value }){
                            Span(text= "Click To See/Hide", color=WHITE , backgroundColor = BLUE)
                        }

                    }

                    AnimatedVisibility(visible = showNotes.value,
                        enter= fadeIn() + expandVertically(),
                        exit = shrinkOut() + fadeOut(),) {
                        Column(){
                            LazyRow(modifier=Modifier.fillMaxWidth()){
                                item{
                                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(WHITE).padding(horizontal = 5.dp),
                                        horizontalAlignment = Alignment.Start){
                                        Label(label = "Name:","Hospital Name as stated in Official Governmental Documents")
                                        Label(label = "City:","The City Where the Hospital is established")
                                        Label("once you select the city, Area Field will appear")
                                        Label("Area:","The Area Within the City Where the hospital is established")
                                        Label("Sector:","The Sector which the hospital is under its directorate")
                                        Label("Type:","The Type of Hospital")
                                        Label("Address:","The detailed address of the hospital ex: street name, famous landmark etc.")
                                        Label("Active:","a checkbox to state whither the Hospital is active and running or not")
                                        Label("unchecking this choice will make all operations done to/within this hospital unavailable", color = Color.Red)
                                        Label(
                                            textAlign = TextAlign.Start,
                                            label="Is N.B.T.S:",
                                            text="a checkbox to state that its just a Branch of the National Blood Transfusion Centers \n and will not contain other modules as ordinary hospitals", maximumLines = 2)
                                        VerticalSpacer()
                                        Label("You can scroll left/right to view the Helper notes", color = BLUE, fontWeight = FontWeight.Bold)
                                        VerticalSpacer()
                                        Label("You can scroll down/up to view other mandatory fields", color = BLUE, fontWeight = FontWeight.Bold)
                                        VerticalSpacer()
                                        Row(){
                                            Label("Once you complete the form, You can press the", color = BLUE, fontWeight = FontWeight.Bold)
                                            HorizontalSpacer(2)
                                            Label("Green Save Changes Button", color = GREEN, fontWeight = FontWeight.Bold)
                                        }
                                        Label("Confirmation box will appear to confirm your Entries", color = BLUE, fontWeight = FontWeight.Bold)
                                        VerticalSpacer()
                                        Label("By Clicking on the Save Changes Button in the Confirmation Box", color = Color.Red)
                                        Label("You cannot revert back, but you can later change the data", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                    VerticalSpacer()
                    if(showConfirmatoryButtons.value){
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            Label(text="Shall We Begin?", fontSize = 24, fontWeight = FontWeight.Bold, color = GREEN)
                        }
                        AnimatedVisibility(visible = showConfirmatoryButtons.value,
                            enter= fadeIn() + expandVertically(),
                            exit = shrinkOut() + fadeOut(),){
                            Row(modifier=Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                CustomButton(label = "Yes, lets proceed", buttonShadowElevation = 5, buttonShape = RectangleShape) {
                                    showFields.value=true
                                    showNotes.value=false
                                    showConfirmatoryButtons.value=false
                                }
                                CustomButton(label = "No, take me back", buttonShadowElevation = 5, buttonShape = RectangleShape,
                                    enabledBackgroundColor = Color.Red) {
                                    navHostController.navigate(HomeRoute.route)
                                }

                            }

                        }

                    }

                    AnimatedVisibility(visible = showFields.value,
                        enter= fadeIn() + expandVertically(),
                        exit = shrinkOut() + fadeOut(),) {
                        Column(){
                            CustomInput(name, HOSPITAL_NAME_LABEL)
                            CitySelector(selectedCity.value!=null,cities,selectedCity,selectedCity)
                            AreaSelector(selectedArea.value!=null,areas,selectedArea)
                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).weight(1f),
                                    contentAlignment = Alignment.Center){
                                    ComboBox(title = SECTOR_LABEL,
                                        selectedItem = selectedSector,
                                        loadedItems = sectors,
                                        selectedContent = {
                                            CustomInput(selectedSector.value?.name?: SELECT_SECTOR_LABEL)
                                        }) { Label(it?.name?:"") }
                                }
                                Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).weight(1f),
                                    contentAlignment = Alignment.Center){
                                    ComboBox(title = HOSPITAL_TYPE_LABEL,
                                        selectedItem = selectedHospitalType,
                                        loadedItems = hospitalTypes,
                                        selectedContent = {
                                            CustomInput(selectedHospitalType.value?.name?: SELECT_TYPE_LABEL)
                                        }) { Label(it?.name?:"") }
                                }
                            }
                            CustomInput(address, HOSPITAL_ADDRESS_LABEL)
                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                                CustomCheckbox(ACTIVE_LABEL,active)
                                CustomCheckbox(IS_NBTS_LABEL,isNbts)

                            }
                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center){
                                CustomButton(label = SAVE_CHANGES_LABEL, enabledBackgroundColor = GREEN, buttonShadowElevation = 6, buttonShape = RectangleShape) {
                                    model= Hospital(
                                        name=name.value,
                                        address = address.value,
                                        active = active.value,
                                        isNBTS = isNbts.value,
                                        sectorId = selectedSector.value?.id,
                                        sector = selectedSector.value,
                                        typeId = selectedHospitalType.value?.id,
                                        type = selectedHospitalType.value,
                                        cityId = selectedCity.value?.id,
                                        city = selectedCity.value,
                                        areaId = selectedArea.value?.id,
                                        area = selectedArea.value
                                    )
                                    showSavePromptDialog.value=true
                                }
                            }

                        }
                    }
                }
            }

        }

    }
}

@Composable
fun SaveHospitalDialog(showDialog: MutableState<Boolean>,
                       model: Hospital?, onSaveClick:()->Unit){
    val context= LocalContext.current
    if(showDialog.value){
        if(model!=null){
            Dialog(onDismissRequest = {showDialog.value=false}) {
                Column(modifier=Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .shadow(elevation = 5.dp, shape = rcsB(20))
                    .background(color=Color.White, shape = rcsB(20))
                    .border(width = 1.dp, shape = rcsB(20),
                        color = colorResource(R.color.teal_700)
                    )
                    .padding(horizontal = 5.dp)
                ){
                    LazyColumn(modifier=Modifier.fillMaxWidth()) {
                        item{
                            Label(icon= R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= NAME_LABEL,text=model.name)

                            VerticalSpacer()
                            Row{
                                Label(icon= R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= CITY_LABEL,text=model.city?.name?:"")
                                HorizontalSpacer()
                                Label(icon= R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= AREA_LABEL,text=model.area?.name?:"")
                            }
                            VerticalSpacer()
                            Label(icon= R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= HOSPITAL_ADDRESS_LABEL,text=model.address?:"")
                            LabelSpan(label="Type",value=if(model.isNBTS==true)"NBTS Blood Bank" else "Hospital", spanColor = BLUE, labelColor = BLACK)
                            VerticalSpacer()
                            Label(icon= R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= HOSPITAL_TYPE_LABEL,text=model.type?.name?:"")

                            VerticalSpacer()
                            Label(icon= R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= SECTOR_LABEL,text=model.sector?.name?:"")
                            VerticalSpacer()
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround){
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.green),
                                contentColor = Color.White
                            ),
                            onClick = onSaveClick,
                            shape = rcs(20)
                        ) {
                            Label(SAVE_CHANGES_LABEL, color = Color.White)
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.red),
                                contentColor = Color.White
                            ),
                            onClick = {showDialog.value=false},
                            shape = rcs(20)
                        ) {
                            Label(CANCEL_LABEL, color = Color.White)
                        }
                    }


                }
            }
        }
        else{
            Toast.makeText(context, TRY_AGAIN_LATER_LABEL, Toast.LENGTH_SHORT).show()
        }
    }
}
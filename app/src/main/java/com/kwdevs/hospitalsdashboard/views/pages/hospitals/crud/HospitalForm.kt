package com.kwdevs.hospitalsdashboard.views.pages.hospitals.crud

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.RenalDevicesController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartment
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_ADDRESS_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_AREA_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CITY_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_NAME_FILED_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SECTOR_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_TYPE_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FREE_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_BURNS_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_CCU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NEUROLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_NICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_ONCOLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_RENAL_DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HAS_WARDS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_ADDRESS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_AREA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_CITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPECIAL_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TRY_AGAIN_LATER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.WardCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices.RenalDeviceCard
import com.kwdevs.hospitalsdashboard.views.numericKeyBoard
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB

@Composable
fun HospitalNameInput(valid:Boolean, name:MutableState<String>){
    Column{
        CustomInput(value = name, label = HOSPITAL_NAME_LABEL)
        if (!valid) Label(ERROR_NAME_FILED_REQUIRED_LABEL, color = Color.Red)
        VerticalSpacer()
    }
}

@Composable
fun HospitalAddressInput(valid:Boolean,value:MutableState<String>){
    CustomInput(value = value, label = HOSPITAL_ADDRESS_LABEL)
    if (!valid) Row(modifier= Modifier.padding(horizontal = 5.dp)){Label(
        ERROR_ADDRESS_FIELD_REQUIRED_LABEL, color = Color.Red)}
    VerticalSpacer()
}

@Composable
fun CitySelector(valid: Boolean, cities:List<CityWithCount>, selectedItem:MutableState<CityWithCount?>, defaultItem:MutableState<CityWithCount?>){

    var filter by remember { mutableStateOf(EMPTY_STRING) }
    var filtered by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }
    LaunchedEffect(selectedItem.value) {
        if(selectedItem.value!=null)filter=selectedItem.value?.name?: SELECT_CITY_LABEL
    }
    LaunchedEffect(filter) {
        if(filter.trim()!= EMPTY_STRING){
            filtered=cities.filter { (it.name?: EMPTY_STRING).contains(filter) }
        }else filtered=cities
    }
    Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            ComboBox(title = CITY_LABEL, loadedItems = filtered, selectedItem = selectedItem, selectedContent = {
                TextField(
                    modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = WHITE,
                        unfocusedContainerColor = WHITE
                    ),
                    trailingIcon = { Icon(R.drawable.ic_arrow_drop_down_blue)},
                    value = if(filter.trim()!= EMPTY_STRING || filter.trim()!= SELECT_CITY_LABEL)filter else SELECT_CITY_LABEL,
                    onValueChange = {filterText->filter=filterText
                        filtered = if(filterText.trim()!= EMPTY_STRING) cities.filter { it.name==filterText } else cities
                    }
                )
                //CustomInput(selectedItem.value?.name?: SELECT_CITY_LABEL)
            }) {
                Label(it?.name?:"")
            }

        }

        Column{
            VerticalSpacer(25)
            IconButton(icon= R.drawable.ic_cancel_red) {
                selectedItem.value=null
                defaultItem.value=null
            }
            VerticalSpacer()
        }
    }
    if (!valid) Row(modifier=Modifier.padding(horizontal = 5.dp)){Label(ERROR_CITY_FIELD_REQUIRED_LABEL, color = Color.Red)}

}

@Composable
fun AreaSelector(valid: Boolean,items:List<AreaWithCount>,selectedItem:MutableState<AreaWithCount?>){
    if(items.isNotEmpty()){
        VerticalSpacer()
        Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)){
                ComboBox(title = AREA_LABEL, loadedItems = items, selectedItem = selectedItem, selectedContent = {
                    CustomInput(selectedItem.value?.name?: SELECT_AREA_LABEL)
                }) {
                    Label(it?.name?:"")
                }
            }

            Column{
                VerticalSpacer(25)
                IconButton(icon=R.drawable.ic_cancel_red) {
                    selectedItem.value=null
                }

            }
        }
        if (!valid) Row(modifier=Modifier.padding(horizontal = 5.dp)){ Label(
            ERROR_AREA_FIELD_REQUIRED_LABEL, color = Color.Red) }
    }
    VerticalSpacer()
}

@Composable
fun SectorSelector(valid: Boolean,items: List<Sector>,selectedItem: MutableState<Sector?>,defaultItem: MutableState<Sector?>){
    Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            ComboBox(title = SECTOR_LABEL, loadedItems = items, selectedItem = selectedItem, selectedContent = {
                CustomInput(selectedItem.value?.name?: SELECT_SECTOR_LABEL)
            }) {
                Label(it?.name?:"")
            }
        }

        Column{
            VerticalSpacer(25)
            IconButton(icon=R.drawable.ic_cancel_red) {
                selectedItem.value=null
                defaultItem.value=null
            }
            VerticalSpacer()
            IconButton(icon=R.drawable.ic_save_blue) {
                defaultItem.value=selectedItem.value
            }
        }
    }
    if (!valid) Row(modifier=Modifier.padding(horizontal = 5.dp)){Label(
        ERROR_SECTOR_FIELD_REQUIRED_LABEL, color = Color.Red)}
    VerticalSpacer()
}

@Composable
fun TypeSelector(valid: Boolean,items:List<HospitalType>,selectedItem: MutableState<HospitalType?>,defaultItem: MutableState<HospitalType?>){
    Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            ComboBox(title = HOSPITAL_TYPE_LABEL, loadedItems = items, selectedItem = selectedItem, selectedContent = {
                CustomInput(selectedItem.value?.name?: SELECT_TYPE_LABEL)
            }) {
                Label(it?.name?:"")
            }

        }

        Column{
            VerticalSpacer(25)
            IconButton(icon=R.drawable.ic_cancel_red) {
                selectedItem.value=null
                defaultItem.value=null
            }
            VerticalSpacer()
            IconButton(icon=R.drawable.ic_save_blue) {
                defaultItem.value=selectedItem.value
            }
        }
    }
    if (!valid) Row(modifier=Modifier.padding(horizontal = 5.dp)){Label(ERROR_TYPE_FIELD_REQUIRED_LABEL, color = Color.Red)}
    VerticalSpacer()
}

@Composable
fun SpecialWardInput(hasWard:MutableState<Boolean>, label:String, allBeds:MutableState<String>, freeBeds:MutableState<String>){
    CustomCheckbox(label = label,active=hasWard)
    if(hasWard.value){
        ColumnContainer {
            VerticalSpacer()
            CustomInput(value = allBeds,
                label = TOTAL_UNITS_LABEL,keyboardOptions = numericKeyBoard, enabled =true,
                onTextChange = {t->if(t.toIntOrNull()!=null) allBeds.value=t else allBeds.value=EMPTY_STRING}
            )
            VerticalSpacer()
            CustomInput(value = freeBeds,label = FREE_UNITS_LABEL,keyboardOptions = numericKeyBoard, enabled = true,
                onTextChange = {t->if(t.toIntOrNull()!=null) freeBeds.value=t else freeBeds.value=EMPTY_STRING}
            )
            VerticalSpacer()
        }
        VerticalSpacer()
        HorizontalDivider()
        VerticalSpacer(10)
    }
}

@Composable
fun RenalDevicesInput(hasWard: MutableState<Boolean>, devices:MutableState<List<HospitalRenalDevice>>){
    val accountType= Preferences.User().getType()
    val savedSuper= Preferences.User().getSuper()
    val user= Preferences.User().get()
    val settingsController:SettingsController = viewModel()
    val optionsState by settingsController.deviceTypeState.observeAsState()
    var types by remember { mutableStateOf<List<DeviceType>>(emptyList()) }
    val selectedType = remember { mutableStateOf<DeviceType?>(null) }

    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<DeviceTypeResponse>
            val r = s.data
            val data= r.data
            types=data

        }
        else->{settingsController.renalDeviceTypesIndex()}
    }
    val simpleUser= user?.let{
        SimpleHospitalUser(id=it.id,hospital=it.hospital, hospitalId = it.hospitalId,name=it.name,title=it.title,active=it.active)
    }
    val simpleSuper= savedSuper?.let{
        SimpleSuperUser(id=it.id,name=it.name,title=it.title,titleId=it.titleId,active=it.active,isSuper=it.isSuper)
    }
    val wardName = remember { mutableStateOf("") }

    CustomCheckbox(label = HAS_RENAL_DEVICES_LABEL,active=hasWard)
    if(hasWard.value){

        ColumnContainer {
            VerticalSpacer()
            CustomInput(value = wardName,label = NAME_LABEL)
            VerticalSpacer()
            ComboBox(title = DEVICE_TYPE_LABEL,
                loadedItems = types,
                selectedItem = selectedType,
                selectedContent = {
                    CustomInput(selectedType.value?.name?: SELECT_DEPARTMENT_TYPE_LABEL)
                }) {
                Label(it?.name?:"")
            }
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                IconButton(icon=R.drawable.ic_check_circle_green) {
                    val new = HospitalRenalDevice(
                        name=wardName.value,
                        typeId = selectedType.value?.id,
                        active=hasWard.value,
                        updatedBy = if(accountType== ViewType.HOSPITAL_USER) simpleUser else null,
                        updatedBySuper = if(accountType== ViewType.SUPER_USER) simpleSuper else null
                    )
                    val newList= mutableListOf<HospitalRenalDevice>()
                    if(devices.value.find { it==new }==null) newList.add(0,new)
                    newList.addAll(1,devices.value)
                    devices.value=newList
                    wardName.value=""
                }
                IconButton(icon=R.drawable.ic_cancel_red) {
                    wardName.value=""
                }
            }
            VerticalSpacer()
        }
        HorizontalDivider()
        VerticalSpacer(10)
    }
}

@Composable
fun InPatientWardsInput(hasWard: MutableState<Boolean>,wards:MutableState<List<HospitalWard>>){
    val accountType= Preferences.User().getType()
    val savedSuper= Preferences.User().getSuper()
    val user= Preferences.User().get()
    val simpleUser= user?.let{
        SimpleHospitalUser(id=it.id,hospital=it.hospital, hospitalId = it.hospitalId,name=it.name,title=it.title,active=it.active)
    }
    val simpleSuper= savedSuper?.let{
        SimpleSuperUser(id=it.id,name=it.name,title=it.title,titleId=it.titleId,active=it.active,isSuper=it.isSuper)
    }
    val wardName = remember { mutableStateOf("") }
    val allBeds = remember { mutableStateOf("0") }
    val active = remember { mutableStateOf(false) }

    CustomCheckbox(label = HAS_WARDS_LABEL,active=hasWard)
    if(hasWard.value){

        ColumnContainer {
            VerticalSpacer()
            CustomInput(value = wardName,label = NAME_LABEL)
            VerticalSpacer()
            CustomInput(value = allBeds,label = TOTAL_UNITS_LABEL,keyboardOptions = numericKeyBoard, enabled = true,
                onTextChange = {t->if(t.toIntOrNull()!=null) allBeds.value=t else allBeds.value=EMPTY_STRING})
            VerticalSpacer()
            CustomCheckbox(label= ACTIVE_LABEL,active)
            VerticalSpacer()

            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                IconButton(icon=R.drawable.ic_check_circle_green) {
                    val new = HospitalWard(
                        name=wardName.value,
                        allBeds = allBeds.value.toIntOrNull(),
                        active = active.value,
                        updatedBy = if(accountType==ViewType.HOSPITAL_USER) simpleUser else null,
                        updatedBySuper = if(accountType==ViewType.SUPER_USER) simpleSuper else null,
                    )
                    val newList= mutableListOf<HospitalWard>()
                    if(wards.value.find { it==new }==null) newList.add(0,new)
                    newList.addAll(1,wards.value)
                    wards.value=newList
                    wardName.value=""
                    allBeds.value="0"
                }
                IconButton(icon=R.drawable.ic_cancel_red) {
                    wardName.value="";allBeds.value=""
                }
            }
            VerticalSpacer()
        }
        VerticalSpacer()
        HorizontalDivider()
        VerticalSpacer(10)
    }
}


@Composable
fun HospitalDepartmentsInput(hasDepartments:MutableState<Boolean>,
                             basicDepartments:List<BasicDepartment>,
                             currentList:MutableState<List<HospitalDepartment>>,
                             ){

    val accountType= Preferences.User().getType()
    val savedSuper= Preferences.User().getSuper()
    val user= Preferences.User().get()
    val simpleUser= user?.let{
        SimpleHospitalUser(id=it.id,hospital=it.hospital, hospitalId = it.hospitalId,name=it.name,title=it.title,active=it.active)
    }
    val simpleSuper= savedSuper?.let{
        SimpleSuperUser(id=it.id,name=it.name,title=it.title,titleId=it.titleId,active=it.active,isSuper=it.isSuper)
    }
    val departmentName = remember { mutableStateOf("") }
    val basicDepartmentSelected = remember { mutableStateOf<BasicDepartment?>(null) }
    val isActive= remember { mutableStateOf(true) }

    CustomCheckbox(label = DEPARTMENTS_LABEL,active=hasDepartments)
    if(hasDepartments.value){
        ColumnContainer {
            VerticalSpacer()
            CustomInput(value=departmentName, label = SPECIAL_NAME_LABEL)
            VerticalSpacer()
            Row(modifier= Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)){
                    ComboBox(title = DEPARTMENT_LABEL, loadedItems = basicDepartments, selectedItem = basicDepartmentSelected, selectedContent = {
                        CustomInput(basicDepartmentSelected.value?.name?: SELECT_DEPARTMENT_LABEL)
                    }) {
                        Label(it?.name?:"")
                    }
                }
                Column {
                    VerticalSpacer(15)
                    IconButton(icon=R.drawable.ic_cancel_red) {
                        basicDepartmentSelected.value=null
                    }
                }
            }
            VerticalSpacer()
            CustomCheckbox(label=ACTIVE_LABEL,active=isActive)
            VerticalSpacer()
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround){
                IconButton(icon=R.drawable.ic_check_circle_green) {
                    val new = HospitalDepartment(
                        name=departmentName.value,
                        basicDepartmentId = basicDepartmentSelected.value?.id?:0,
                        basicDepartment = basicDepartmentSelected.value,
                        active=isActive.value,
                        updatedBy = if(accountType==ViewType.HOSPITAL_USER) simpleUser else null,
                        updatedBySuper = if(accountType==ViewType.SUPER_USER) simpleSuper else null,

                        )
                    val newList= mutableListOf<HospitalDepartment>()
                    if(currentList.value.find { it==new }==null) newList.add(0,new)
                    newList.addAll(1,currentList.value)
                    currentList.value=newList
                    departmentName.value=""
                    basicDepartmentSelected.value=null
                    isActive.value=true
                }
                IconButton(icon=R.drawable.ic_cancel_red) {
                    departmentName.value=""
                    basicDepartmentSelected.value=null
                    isActive.value=true
                }
            }
            VerticalSpacer()
        }
        VerticalSpacer()
        HorizontalDivider()
        VerticalSpacer(10)
    }
}

@Composable
fun SaveHospitalDialog(showDialog:MutableState<Boolean>,
                      model: Hospital?,navHostController: NavHostController,onSaveClick:()->Unit){
    val context= LocalContext.current
    val wards=model?.wards
    val renalDevices=model?.renalDevices
    val departments=model?.departments
    if(showDialog.value){
        if(model!=null){
            Dialog(onDismissRequest = {showDialog.value=false}) {
                Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    .shadow(elevation = 5.dp, shape = rcsB(20))
                    .background(color=Color.White, shape = rcsB(20))
                    .border(width = 1.dp, shape = rcsB(20),
                        color = colorResource(R.color.teal_700)
                    )
                    .padding(horizontal = 5.dp)
                ){
                    LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f)) {
                        item{
                            Label(icon=R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= NAME_LABEL,text=model.name)

                            VerticalSpacer()
                            Row{
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= CITY_LABEL,text=model.city?.name?:"")
                                HorizontalSpacer()
                                Label(icon=R.drawable.ic_info_white,
                                    iconBackground = colorResource(R.color.teal_700),
                                    label= AREA_LABEL,text=model.area?.name?:"")
                            }
                            VerticalSpacer()
                            Label(icon=R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= HOSPITAL_ADDRESS_LABEL,text=model.address?:"")
                            Span(if(model.isNBTS==true)"NBTS Blood Bank" else "Hospital", backgroundColor = BLUE, color = Color.White)
                            VerticalSpacer()
                            Label(icon=R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= HOSPITAL_TYPE_LABEL,text=model.type?.name?:"")

                            VerticalSpacer()
                            Label(icon=R.drawable.ic_info_white,
                                iconBackground = colorResource(R.color.teal_700),
                                label= SECTOR_LABEL,text=model.sector?.name?:"")
                            VerticalSpacer()
                            if(model.icu?.hasIcu == true){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_ICU_LABEL)
                                    HorizontalSpacer()
                                    Column {  }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allIcuBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeIcuBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.icu?.hasCcu == true){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_CCU_LABEL)
                                    HorizontalSpacer()
                                    Column {  }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allCcuBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeCcuBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.icu?.hasNicu == true){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_NICU_LABEL)
                                    HorizontalSpacer()
                                    Column {  }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allNicuBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeNicuBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.icu?.hasOncologyCareUnit == true){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_ONCOLOGY_CU_LABEL)
                                    HorizontalSpacer()
                                    Column {  }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allOncologyCareUnitBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeOncologyCareUnitBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.icu?.hasBurnCareUnit == true){
                                Box(modifier=Modifier.fillMaxWidth().padding(5.dp)
                                    .shadow(elevation = 5.dp, shape = rcs(20))
                                    .background(color=Color.White,shape= rcs(20))
                                    .border(width=1.dp,color=Color.White,shape= rcs(20))){}
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_BURNS_CU_LABEL)
                                    HorizontalSpacer()
                                    Column {  }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allBurnCareUnitBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeBurnCareUnitBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.icu?.hasNeuroCu == true){
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                    HorizontalSpacer()
                                    Label(text= HAS_NEUROLOGY_CU_LABEL)
                                    HorizontalSpacer()
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.icu.allNeurologyCareUnitBeds?:0}")
                                    HorizontalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.icu.freeNeurologyCareUnitBeds?:0}")
                                }
                                VerticalSpacer()
                            }
                            if(model.morgue != null){
                                val status=model.morgue.status
                                Column{
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(if((status?.id?:0)==1) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                                        HorizontalSpacer()
                                        Label(text= HAS_MORGUE_LABEL)
                                        HorizontalSpacer()
                                    }
                                    Label(label= TOTAL_UNITS_LABEL,text= "${model.morgue.allUnits?:0}")
                                    VerticalSpacer()
                                    Label(label= FREE_UNITS_LABEL,text= "${model.morgue.freeUnits?:0}")
                                }

                                VerticalSpacer()
                            }
                            renalDevices?.let{
                                if(renalDevices.isNotEmpty()){
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                        HorizontalSpacer()
                                        Label(HAS_RENAL_DEVICES_LABEL)
                                    }
                                    renalDevices.forEach { item->
                                        val renalDevicesController: RenalDevicesController = viewModel()
                                        RenalDeviceCard(item,renalDevicesController)
                                        VerticalSpacer()
                                    }
                                    VerticalSpacer()
                                }

                            }
                            wards?.let{
                                if(model.wards.isNotEmpty()){
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(R.drawable.ic_check_circle_green, containerSize = 26)
                                        HorizontalSpacer()
                                        Label(HAS_WARDS_LABEL)
                                    }
                                    model.wards.forEach { ward->
                                        WardCard(ward, navHostController =navHostController )
                                        VerticalSpacer()
                                    }
                                    VerticalSpacer()
                                }

                            }
                            departments?.let{
                                if(departments.isNotEmpty()){
                                    Label(icon=R.drawable.ic_check_circle_green,
                                        iconBackground = colorResource(R.color.white),
                                        label = DEPARTMENTS_LABEL, text = model.departments.size.toString())
                                    model.departments.forEach { department->
                                        Box(modifier=Modifier.padding(5.dp)){
                                            Label(text=department.name)
                                        }
                                    }
                                    VerticalSpacer()
                                }

                            }
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
                            shape = rcs(20)) {
                            Label(SAVE_CHANGES_LABEL, color = Color.White)
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.red),
                                contentColor = Color.White
                            ),
                            onClick = {showDialog.value=false},
                            shape = rcs(20)) {
                            Label(CANCEL_LABEL, color = Color.White)
                        }
                    }


                }
            }
        }
        else{
            Toast.makeText(context, TRY_AGAIN_LATER_LABEL,Toast.LENGTH_SHORT).show()
        }
    }
}

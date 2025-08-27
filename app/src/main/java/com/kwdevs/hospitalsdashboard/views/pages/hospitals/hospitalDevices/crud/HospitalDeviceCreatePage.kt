package com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices.crud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceBody
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalDeviceController
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceType
import com.kwdevs.hospitalsdashboard.models.settings.multipleReturns.CrudDeviceData
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDevicesRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_HOSPITAL_DEVICE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BACK_TO_MAIN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICE_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEPARTMENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEVICE_STATUS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DEVICE_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen

@Composable
fun HospitalDeviceCreatePage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()

    var deviceStatuses by remember { mutableStateOf<List<Status>>(emptyList()) }
    var deviceTypes by remember { mutableStateOf<List<DeviceType>>(emptyList()) }
    var departments by remember { mutableStateOf<List<HospitalDepartment>>(emptyList()) }
    val hospitalDeviceController : HospitalDeviceController= viewModel()
    val settingsController: SettingsController= viewModel()
    val crudDeviceOptionsState by settingsController.crudDeviceState.observeAsState()

    val state by hospitalDeviceController.singleState.observeAsState()
    var item by remember { mutableStateOf<HospitalDevice?>(null) }
    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var empty by remember { mutableStateOf(true) }
    when(crudDeviceOptionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = crudDeviceOptionsState as UiState.Success<CrudDeviceData>
            val r = s.data
            val data=r.data
            deviceStatuses=data.statuses
            deviceTypes=data.types
            departments=data.departments
        }
        else->{ hospital?.let { settingsController.crudDeviceOptions(it.id) } }
    }

    when(state){
        is UiState.Loading->{ loading=true;fail=false;success=false;empty=true }
        is UiState.Error->{loading=false;fail=true;success=false;empty=true}
        is UiState.Success->{
            loading=false;fail=false;success=true
            val s = state as UiState.Success<HospitalDeviceSingleResponse>
            val r = s.data
            val data = r.data
            item=data
            empty = data!=null
            LaunchedEffect(Unit) { navHostController.navigate(HospitalDevicesRoute.route) }

        }
        else->{
            loading=false;success=false;fail=false;empty=true
        }

    }
    val selectedDeviceStatus    = remember { mutableStateOf<Status?>(null) }
    val selectedDeviceType      = remember { mutableStateOf<DeviceType?>(null) }
    val deviceName              = remember { mutableStateOf("") }
    val deviceCode              = remember { mutableStateOf("") }
    val selectedDepartment      = remember { mutableStateOf<HospitalDepartment?>(null) }

    ColumnContainer {
        Header(ADD_NEW_HOSPITAL_DEVICE_LABEL, fontSize = 30, fontWeight = FontWeight.Bold, color = Color.White)
        if(item==null){
            Column(modifier=Modifier.padding(top = 10.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 5.dp)){
                    ComboBox(title = DEVICE_TYPE_LABEL, loadedItems = deviceTypes, selectedItem = selectedDeviceType, selectedContent = {
                        CustomInput(selectedDeviceType.value?.name?: SELECT_DEVICE_TYPE_LABEL)
                    }) {
                        Label(it?.name?:"")
                    }
                }
                //Status
                Box(modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 5.dp)
                ){
                    ComboBox(title = DEVICE_STATUS_LABEL, loadedItems = deviceStatuses, selectedItem = selectedDeviceStatus, selectedContent = {
                        CustomInput(selectedDeviceStatus.value?.name?: SELECT_DEVICE_STATUS_LABEL)
                    }) {
                        Label(it?.name?:"")
                    }
                }
                //Department
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)){
                    ComboBox(title = DEPARTMENT_LABEL, loadedItems = departments,
                        selectedItem = selectedDepartment, selectedContent = {
                            CustomInput(selectedDepartment.value?.name?: SELECT_DEPARTMENT_LABEL)
                        }) {
                        Label(it?.name?:"")
                    }
                }
                CustomInput(value = deviceName, label = NAME_LABEL)
                CustomInput(value = deviceCode, label = CODE_LABEL)
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround){
                    IconButton(icon= R.drawable.ic_cancel_red) {
                        val userType=Preferences.User().getType()
                        val route = if(userType==ViewType.SUPER_USER) HomeRoute.route else HospitalDevicesRoute.route
                        navHostController.navigate(route)
                    }
                    IconButton(icon= R.drawable.ic_check_circle_green) {
                        val dept=selectedDepartment.value
                        val type=selectedDeviceType.value
                        val status=selectedDeviceStatus.value
                        val name=deviceName.value
                        val code = deviceCode.value
                        val user = Preferences.User().get()
                        if(hospital!=null && dept!=null && type!=null && status!=null && name.trim()!= "" && code.trim() != ""){

                            val body= HospitalDeviceBody(
                                hospitalId = hospital.id,
                                statusId = status.id,
                                departmentId = dept.id,
                                typeId = type.id,
                                name = deviceName.value,
                                code = deviceCode.value,
                                createdById = user?.id
                            )

                            hospitalDeviceController.storeByNormalUser(body)
                        }
                    }
                }
                VerticalSpacer()
            }
        }else{
            SuccessScreen {
                Label(DATA_SAVED_LABEL)
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround){
                    CustomButton(label = BACK_TO_MAIN_LABEL
                    , enabledBackgroundColor = Color.Red) { }
                    CustomButton(label = ADD_NEW_LABEL) {
                        hospitalDeviceController.reload()
                        deviceCode.value=""
                        deviceName.value=""
                        selectedDepartment.value=null
                        selectedDeviceType.value=null
                        selectedDeviceStatus.value=null
                        item=null
                        loading=false;success=false;fail=false;empty=true
                    }
                }
            }
        }
        //Type

    }
    VerticalSpacer()
    HorizontalDivider()
    VerticalSpacer(10)
}
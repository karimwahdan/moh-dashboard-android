package com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceUsageBody
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalDeviceUsageController
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceDailyUsage
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsViewRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NO_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.USAGE_FREQUENCY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.devices.usage.DeviceUsageCard
import com.kwdevs.hospitalsdashboard.views.getFormattedDateJavaTime
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val formatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalDevicesUsagePage(navHostController: NavHostController){
    val userType=Preferences.User().getType()
    val saved           =  Preferences.HospitalDevices().get()
    val controller      :  HospitalDeviceUsageController = viewModel()
    val state           by controller.state.observeAsState()
    val singleState     by controller.singleState.observeAsState()
    val item            =  remember { mutableStateOf<HospitalDeviceDailyUsage?>(null) }
    val showStoreDialog =  remember { mutableStateOf(false) }
    val showEditDialog  =  remember { mutableStateOf(false) }
    var hospitalDevice  by remember { mutableStateOf<HospitalDevice?>(null) }
    var usages          by remember { mutableStateOf<List<HospitalDeviceDailyUsage>>(emptyList()) }
    var usagesByMonth   by remember { mutableStateOf<Map<Month,List<HospitalDeviceDailyUsage>>>(emptyMap()) }

    var loading         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    var empty           by remember { mutableStateOf(true) }

    when(singleState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = singleState as UiState.Success<HospitalDeviceUsageSingleResponse>
            val r = s.data
            val d = r.data
            LaunchedEffect(Unit) {item.value = d}
            if (d != null) {
                val n= mutableListOf<HospitalDeviceDailyUsage>()
                n.addAll(usages)
                n.add(d)
                usages=n
                showStoreDialog.value=false
            }
        }
        else->{}
    }
    when(state){
        is UiState.Loading->{
            loading = true
            fail    = false
            success = false
            empty   = true
        }
        is UiState.Error->{
            loading = false
            fail    = true
            success = false
            empty   = true
        }
        is UiState.Success->{
            loading=false;fail=false;success=true
            val s = state as UiState.Success<HospitalDeviceSingleResponse>
            val r = s.data
            val d = r.data
            empty = d == null
            hospitalDevice=d
            LaunchedEffect(Unit) {
                usages = d?.usages?: emptyList()
                if(usages.isEmpty()) Log.e("Empty U","Empty U")
                usagesByMonth = d?.usages?.groupBy { LocalDate.parse(it.day, formatter).month }?: emptyMap()
                if(usagesByMonth.isEmpty()) Log.e("Empty UBM","Empty UBM")
            }
        }
        else->{
            controller.indexByDevice(saved?.id?:0)
        }
    }

    hospitalDevice?.let{
        NewUsageDialog(showStoreDialog,it)
        UpdateUsageDialog(showEditDialog,it,item.value)
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = hospitalDevice?.name?:(saved?.name?:"-"),
        headerFontSize = 24,
        showSheet = showSheet,
        headerFontWeight = FontWeight.Bold,
        headerShowBackButton = true,
        headerOnClick = {
            if(userType==ViewType.SUPER_USER) navHostController.navigate(HospitalsViewRoute.route)
            else navHostController.navigate(HospitalHomeRoute.route) },
        headerIconButtonBackground = BLUE
    ) {
        Column(modifier=Modifier.fillMaxSize()){
            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(R.drawable.ic_add_circle_white, background = GREEN) {
                    showStoreDialog.value=true
                }
                IconButton(R.drawable.ic_autorenew_white, background = BLUE) {
                    controller.indexByDevice(saved?.id?:0)
                }

            }

            if(loading && !fail && !success) LoadingScreen(modifier = Modifier.fillMaxSize())
            else if(fail && !loading && !success) FailScreen(modifier=Modifier.fillMaxSize())
            if(success && !fail && !loading){
                if(empty || usages.isEmpty()){
                    FailScreen(modifier = Modifier.fillMaxSize(), label = NO_DATA_LABEL)
                }else{
                    LazyColumn{
                        usagesByMonth.entries.forEach {
                                (month, usageList) ->
                            // Month Header
                            item {
                                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                    Label(text = month.name.lowercase().replaceFirstChar { it.uppercase() })
                                    Label(text=" ( ${usageList.sumOf { it.usage?:0 }} Patient(s) )")
                                }
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween){ Label("Day"); Label("Patients")}

                            }

                            // Usage Items
                            items(usageList) { usage ->
                                DeviceUsageCard(usage,true){
                                    item.value = usage
                                    showEditDialog.value=true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewUsageDialog(showDialog: MutableState<Boolean>, device: HospitalDevice){
    val controller :HospitalDeviceUsageController= viewModel()
    val state by controller.singleState.observeAsState()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            showDialog.value=false
            controller.reload()
            controller.indexByDevice(device.id?:0)
        }
        else->{}
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val day = remember { mutableStateOf("") }
    val usageCount = remember { mutableStateOf("0") }
    LaunchedEffect(datePickerState.selectedDateMillis) { day.value = getFormattedDateJavaTime(datePickerState) }
    val showDatePicker = remember { mutableStateOf(false) }
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier= Modifier.fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.End){
                    IconButton(icon = R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                    enabledBackgroundColor = ORANGE,
                    onClick = { showDatePicker.value = !showDatePicker.value })
                Row(verticalAlignment = Alignment.CenterVertically){
                    Label(text=device.name)
                    Span(text="${device.code}", backgroundColor = BLUE, color = WHITE)
                }
                Label(day.value)
                DatePickerWidget(showDatePicker,datePickerState,day)

                CustomInput(value = usageCount,label = USAGE_FREQUENCY_LABEL,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                VerticalSpacer()
                CustomButton(label = SAVE_CHANGES_LABEL) {
                    val body = HospitalDeviceUsageBody(
                        id = null,
                        deviceId=device.id,
                        hospitalId = device.hospitalId,
                        deviceTypeId = device.typeId,
                        usage = usageCount.value.toInt(),
                        day = day.value
                    )
                    controller.storeByNormalUser(body)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateUsageDialog(showDialog: MutableState<Boolean>, device: HospitalDevice,item:HospitalDeviceDailyUsage?){
    val controller :HospitalDeviceUsageController= viewModel()
    val state by controller.singleState.observeAsState()

    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            showDialog.value=false
            controller.reload()
            controller.indexByDevice(device.id?:0)
        }
        else->{}
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val day = remember { mutableStateOf("") }
    val usageCount = remember { mutableStateOf("0") }

    LaunchedEffect(datePickerState.selectedDateMillis) { day.value = getFormattedDateJavaTime(datePickerState) }
    val showDatePicker = remember { mutableStateOf(false) }
    LaunchedEffect(item) {
        if(item!=null){
            try {
                item.day?.let{ day.value= LocalDate.parse(item.day, formatter).toString() }
                usageCount.value="${item.usage?:0}"
            }
            catch (e:Exception){ e.printStackTrace() }

        }
    }
    if(showDialog.value){
        LaunchedEffect(Unit) {
            if(item!=null){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                val selectedMillis = item.day?.let {
                    LocalDate.parse(it, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }
                datePickerState.selectedDateMillis=selectedMillis
            }
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier= Modifier.fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.End){
                    IconButton(icon = R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                    enabledBackgroundColor = ORANGE,
                    onClick = { showDatePicker.value = !showDatePicker.value })
                Row(verticalAlignment = Alignment.CenterVertically){
                    Label(text=device.name)
                    Span(text="${device.code}", backgroundColor = BLUE, color = WHITE)
                }
                Label(day.value)
                DatePickerWidget(showDatePicker,datePickerState,day)

                CustomInput(value = usageCount,label = USAGE_FREQUENCY_LABEL,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                VerticalSpacer()
                CustomButton(label = SAVE_CHANGES_LABEL) {
                    if(item!=null){
                        val body = HospitalDeviceUsageBody(
                            id = item.id,
                            usage = usageCount.value.toInt(),
                            day = day.value
                        )
                        controller.updateByNormalUser(body)

                    }
                }
            }
        }
    }
}



@Preview
@Composable
private fun Preview(){
    HospitalDevicesUsagePage(rememberNavController())
}
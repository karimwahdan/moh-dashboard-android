package com.kwdevs.hospitalsdashboard.views.pages.hospitals.receptionBeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionBedBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionFrequencyBody
import com.kwdevs.hospitalsdashboard.controller.hospital.ReceptionBedsController
import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionData
import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBed
import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBedSingleResponse
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BEDS_COUNT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEATHS_COUNT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NEW_USAGE_FREQUENCY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RECEPTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.ReceptionBedCard
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.ReceptionFrequencyCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceptionBedsIndexPage(navHostController: NavHostController){
    val controller: ReceptionBedsController = viewModel()
    val state by controller.singleState.observeAsState()
    var item by remember { mutableStateOf<ReceptionData?>(null) }
    var bed by remember { mutableStateOf<ReceptionBed?>(null) }
    var frequencies by remember { mutableStateOf<List<ReceptionFrequency>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val showCreateDialog = remember { mutableStateOf(false) }
    val showFrequencyCreateDialog = remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ReceptionBedSingleResponse>
            val r = s.data
            val data=r.data
            item=data
            bed=data?.receptionBedCount
            val pagination=data?.admissions
            lastPage=pagination?.lastPage?:1
            frequencies=pagination?.data?: emptyList()

        }
        else->{
            controller.indexByHospital()
        }
    }
    ReceptionBedCreateDialog(showCreateDialog,controller)
    ReceptionFrequencyCreateDialog(showFrequencyCreateDialog,controller)
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = RECEPTION_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                buttonShape = RectangleShape,
                onClick = { showCreateDialog.value=true })
        }
        VerticalSpacer()
        bed?.let { ReceptionBedCard(it) }
        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = NEW_USAGE_FREQUENCY_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                buttonShape = RectangleShape,
                onClick = { showFrequencyCreateDialog.value=true })
        }
        VerticalSpacer()
        PaginationContainer(
            currentPage=currentPage,
            lastPage = lastPage,
            totalItems = frequencies.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(frequencies){
                    ReceptionFrequencyCard(it)
                }
            }
        }

    }
}

@Composable
private fun ReceptionBedCreateDialog(showDialog:MutableState<Boolean>,controller: ReceptionBedsController){
    val hospital=Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val beds = remember { mutableStateOf("") }
    val state by controller.singleState.observeAsState()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                showDialog.value=false
                beds.value=""
                controller.indexByHospital()
            }
        }
        else->{ }
    }
    if(showDialog.value){
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                CustomInput(beds, BEDS_COUNT_LABEL)
                Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = SAVE_CHANGES_LABEL
                        , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                    ){
                        val body = ReceptionBedBody(
                            hospitalId = hospital?.id,
                            beds =beds.value.toInt(),
                            createdById = user?.id
                        )
                        controller.storeNormal(body)

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReceptionFrequencyCreateDialog(showDialog:MutableState<Boolean>,controller: ReceptionBedsController){
    val hospital=Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val patientsCount = remember { mutableStateOf("") }
    val deathsCount = remember { mutableStateOf("") }
    val dayState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val day = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val state by controller.singleState.observeAsState()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                showDialog.value=false
                day.value=""
                patientsCount.value=""
                deathsCount.value=""
                controller.indexByHospital()
            }
        }
        else->{

        }
    }
    DatePickerWidget(showDatePicker,dayState,day)

    if(showDialog.value){
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                    enabledBackgroundColor = ORANGE,
                    onClick = { showDatePicker.value = !showDatePicker.value })
                Label(label = DATE_LABEL,day.value)
                CustomInput(deathsCount, DEATHS_COUNT_LABEL)
                CustomInput(patientsCount, PATIENTS_LABEL)
                Row(modifier=Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = SAVE_CHANGES_LABEL
                        , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                    ){
                        val body = ReceptionFrequencyBody(
                            hospitalId = hospital?.id,
                            patients =patientsCount.value.toInt(),
                            deaths = deathsCount.value.toInt(),
                            day = day.value,
                            createdById = user?.id
                        )
                        controller.storeFrequencyNormal(body)

                    }
                }
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.views.cards.hospitals.clinics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.ClinicVisitBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalClinicController
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisit
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes.ClinicVisitType
import com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes.ClinicVisitTypeResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MEDICAL_EXAMINATIONS_FREQUENCY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_EXAMINATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@Composable
fun ClinicCard(item:HospitalClinic,controller: HospitalClinicController){
    val clinic=item.generalClinic
    val totalVisits=item.totalVisitsThisMonth?:0
    val showVisitsDialog = remember { mutableStateOf(false) }
    VisitsDialog(showVisitsDialog,item,controller)
    ColumnContainer {
        Row(modifier= Modifier.fillMaxWidth().padding(vertical = 5.dp,horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically){
            Label(clinic?.name?:"")
            HorizontalSpacer()
            Span(text = "$totalVisits", backgroundColor = BLUE, color = WHITE)
        }
        Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 10.dp)){

            CustomButtonWithImage(icon = R.drawable.ic_frequency,
                iconSize = 26, maxWidth = 52,
                label = MEDICAL_EXAMINATIONS_FREQUENCY_LABEL) {
                showVisitsDialog.value=true
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewVisitDialog(showDialog: MutableState<Boolean>,item:HospitalClinic,controller: HospitalClinicController){
    val state by controller.visitSingleState.observeAsState()
    val user=Preferences.User().get()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.clinicVisitTypesStatesState.observeAsState()
    var visitTypes by remember { mutableStateOf<List<ClinicVisitType>>(emptyList()) }
    val selectedVisitType = remember { mutableStateOf<ClinicVisitType?>(null) }
    val visits= remember { mutableStateOf("") }
    val enterTimeState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis())
    val enterTime = remember { mutableStateOf("") }
    val showEnterTimePicker = remember { mutableStateOf(false) }
    DatePickerWidget(showEnterTimePicker,enterTimeState,enterTime)
    if(showDialog.value){
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    showDialog.value=false
                    controller.indexClinicVisits(1,item.id?:0)
                }
            }
            else->{}
        }

        when(optionsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = optionsState as UiState.Success<ClinicVisitTypeResponse>
                val r = s.data
                val data = r.data
                visitTypes= data
            }
            else->{settingsController.clinicVisitTypesIndex()}
        }
        Dialog(onDismissRequest = {showDialog.value=false;controller.reload()}) {
            ColumnContainer {
                VerticalSpacer()
                Label(item.generalClinic?.name?:"N/A")
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                VerticalSpacer()
                if(enterTime.value.trim()!=""){
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Label(label = DATE_LABEL,text=enterTime.value)
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.Center){
                    CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                        enabledBackgroundColor = ORANGE,
                        onClick = { showEnterTimePicker.value = !showEnterTimePicker.value })
                }
                Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(selectedItem = selectedVisitType, loadedItems = visitTypes,
                        selectedContent = {
                            CustomInput(selectedVisitType.value?.name?: SELECT_EXAMINATION_TYPE_LABEL)
                        }) {
                        Label(it?.name?:"")
                    }
                }
                CustomInput(value = visits, label = MEDICAL_EXAMINATIONS_FREQUENCY_LABEL)

                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                    CustomButton(label = SAVE_CHANGES_LABEL) {
                        val body = ClinicVisitBody(
                            hospitalId = item.hospitalId,
                            clinicId = item.id,
                            day = enterTime.value,
                            visits = visits.value.toInt(),
                            visitTypeId = selectedVisitType.value?.id?:0,
                            createdById = user?.id
                        )
                        controller.storeVisit(body)
                    }
                }
            }
        }
    }
}

@Composable
private fun VisitsDialog(showDialog:MutableState<Boolean>,item: HospitalClinic,controller: HospitalClinicController){

    val showNewVisitDialog = remember { mutableStateOf(false) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val state by controller.visitsPaginationState.observeAsState()
    var items by remember { mutableStateOf<List<DailyClinicVisit>>(emptyList()) }

    NewVisitDialog(showNewVisitDialog,item,controller)
    if(showDialog.value){
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s =state as UiState.Success<ApiResponse<PaginationData<DailyClinicVisit>>>
                val r = s.data
                val pagination = r.pagination
                lastPage=pagination.lastPage
                val data = pagination.data
                items=data
            }
            else->{
                if(showDialog.value){ controller.indexClinicVisits(page=currentPage.intValue, clinicId =  item.id?:0)}
            }
        }
        Dialog(onDismissRequest = {
            showDialog.value=false;controller.reload()}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Center){
                    CustomButton(label = ADD_NEW_LABEL,
                        buttonShadowElevation = 6,
                        buttonShape = RectangleShape,
                        enabledBackgroundColor = BLUE) {
                        showNewVisitDialog.value=true
                    }
                }
                PaginationContainer(
                    currentPage=currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        items(items){
                            ClinicVisitCard(it)
                        }
                    }
                }
            }

        }
    }

}
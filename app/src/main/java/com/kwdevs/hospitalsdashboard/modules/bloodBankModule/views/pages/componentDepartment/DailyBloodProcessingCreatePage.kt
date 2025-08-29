package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodProcessingBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankComponentDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.BloodComponentOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.DailyBloodProcessingSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DailyBloodProcessingIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_PROCESSING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_BY_CAMPAIGN_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PROCESSING_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_PROCESSING_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_PROCESSED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBloodProcessingCreatePage(navHostController: NavHostController){

    val old=Preferences.BloodBanks.DailyProcesses().get()
    val crudType=Preferences.CrudTypes().get()
    val controller:BloodBankComponentDepartmentController= viewModel()
    val optionsState                by controller.optionsState.observeAsState()
    val state                       by controller.dailyBloodProcessingSingleState.observeAsState()
    var item                        by remember { mutableStateOf<DailyBloodProcessing?>(null) }
    var bloodTypes                  by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var collections                 by remember { mutableStateOf<List<DailyBloodCollection>>(emptyList()) }
    var filteredCollection          by remember { mutableStateOf<List<DailyBloodCollection>>(emptyList()) }
    var success                     by remember { mutableStateOf(false) }
    var fail                        by remember { mutableStateOf(false) }
    val user                        =  Preferences.User().get()
    val bloodBank                   =  Preferences.BloodBanks().get()
    val hospital                    =  Preferences.Hospitals().get()
    val body                        =  remember { mutableStateOf<DailyBloodProcessingBody?>(null) }
    var preSavedItem                by remember {mutableStateOf<DailyBloodProcessing?>(null)}
    val selectedBloodType           =  remember { mutableStateOf<BasicModel?>(null) }
    val total                       =  remember { mutableStateOf(EMPTY_STRING) }
    val campaignCode                =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedBloodCollection     =  remember { mutableStateOf<DailyBloodCollection?>(null) }
    val showSheet                   =  remember { mutableStateOf(false) }
    val showProcessingDatePicker    =  remember { mutableStateOf(false) }
    val processingDateState         =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val processingDate              =  remember { mutableStateOf(EMPTY_STRING) }
    val showDialog                  =  remember { mutableStateOf(false) }


    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = optionsState as UiState.Success<BloodComponentOptionsData>
                val r = s.data
                val data=r.data
                bloodTypes=data.bloodTypes
                collections=data.bloodCollections
                filteredCollection=data.bloodCollections

            }
        }
        else->{ controller.options() }
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{
            success=false;fail=true
        }
        is UiState.Success->{
            val s = state as UiState.Success<DailyBloodProcessingSingleResponse>
            val r = s.data
            val data=r.data
            item=data
            LaunchedEffect(Unit) {
                success=true;fail=false
                showSheet.value=true
            }
        }
        else->{
        }
    }
    LaunchedEffect(showSheet.value) {
        if(!showSheet.value){
            success=showSheet.value
        }
    }
    LaunchedEffect(Unit) {
        if(old!=null){
            selectedBloodType.value=old.unitType
            selectedBloodCollection.value=old.campaign
            total.value=(old.total?:0).toString()
            processingDate.value=old.processingDate?: EMPTY_STRING
        }
    }
    DatePickerWidget(showProcessingDatePicker,processingDateState,processingDate)
    SaveDialog(showDialog = showDialog,controller=controller,body=body,item=preSavedItem)
    Container(
        title = DAILY_PROCESSING_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(DailyBloodProcessingIndexRoute.route)},
        sheetColor = if(success) GREEN else Color.Red,
        sheetContent = {
            Label(text=if(success) DATA_SAVED_LABEL else ERROR_CREATING_DATA_LABEL, color = WHITE)
        },
        sheetOnClick = {if(success) navHostController.navigate(DailyBloodProcessingIndexRoute.route) else {showSheet.value=false;fail=false;success=false} }
    ) {
        Column(modifier= Modifier.fillMaxSize()){
            LazyColumn(modifier= Modifier.fillMaxSize().weight(1f).padding(5.dp)) {
                item{
                    Row(modifier= Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically){
                        Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                            CustomInput(campaignCode,FILTER_BY_CAMPAIGN_CODE_LABEL)
                        }
                        IconButton(R.drawable.ic_search_green) {
                            filteredCollection=collections.filter { it.code==campaignCode.value }
                            selectedBloodCollection.value=null
                        }
                    }
                    Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                            ComboBox(
                                title=CAMPAIGN_LABEL,
                                selectedItem = selectedBloodCollection, loadedItems = filteredCollection,
                                selectedContent = {
                                    val code=selectedBloodCollection.value?.code?:EMPTY_STRING
                                    val collectionDate= (selectedBloodCollection.value?.collectionDate?:EMPTY_STRING)
                                    val campaignType = selectedBloodCollection.value?.campaignType?.name?:EMPTY_STRING
                                    val dateOnly=collectionDate.replaceAfterLast(SPACE,EMPTY_STRING)
                                    val totalCollected=selectedBloodCollection.value?.total?:0
                                    CustomInput("$code - $dateOnly - $campaignType \n مجمع: $totalCollected", maxLines = 2)
                                }) {
                                val code=it?.code?:EMPTY_STRING
                                val collectionDate= (it?.collectionDate?:EMPTY_STRING)
                                val campaignType = it?.campaignType?.name
                                val totalCollected=it?.total?:EMPTY_STRING
                                val dateOnly=collectionDate.replaceAfterLast(SPACE,EMPTY_STRING)
                                Label(text="$code - $dateOnly - $campaignType \n مجمع: $totalCollected",textAlign = TextAlign.End, maximumLines = 2)
                            }
                        }
                        IconButton(R.drawable.ic_cancel_red) {
                            filteredCollection=collections
                            selectedBloodCollection.value=null
                            campaignCode.value=EMPTY_STRING

                        }
                    }
                    if(selectedBloodCollection.value!=null){
                        Row(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                            Box(modifier= Modifier.fillMaxWidth()){
                                ComboBox(
                                    title= UNIT_TYPE_LABEL,
                                    selectedItem = selectedBloodType, loadedItems = bloodTypes,
                                    selectedContent = {
                                        CustomInput(selectedBloodType.value?.name?: SELECT_UNIT_TYPE_LABEL)
                                    }) { Label(it?.name?: EMPTY_STRING) }
                            }
                        }
                        if(processingDate.value.trim()!=EMPTY_STRING){
                            Row(modifier= Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center){
                                Label(label = SELECT_PROCESSING_DATE_LABEL,processingDate.value)
                            }
                        }
                        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                            horizontalArrangement = Arrangement.Center){
                            CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                                enabledBackgroundColor = ORANGE,
                                onClick = { showProcessingDatePicker.value = !showProcessingDatePicker.value })
                        }
                        VerticalSpacer()
                        CustomInput(
                            value=total,label=TOTAL_PROCESSED_LABEL,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onTextChange = {
                                val number=it.toIntOrNull()
                                val collectionTotal=selectedBloodCollection.value?.total?: 0
                                val isValid=number !=null && number <=collectionTotal
                                if(isValid){total.value=it} else total.value= EMPTY_STRING})
                        VerticalSpacer(10)
                        Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween){
                            CustomButton(label= SAVE_CHANGES_LABEL,
                                buttonShape = RectangleShape,
                                enabledBackgroundColor = GREEN,
                                enabled =(processingDate.value.trim()!=EMPTY_STRING &&
                                        selectedBloodType.value!=null && selectedBloodCollection.value!=null &&
                                        total.value.trim()!= EMPTY_STRING && total.value.trim().toIntOrNull()!=null) ,
                                buttonShadowElevation = 6) {
                                if(processingDate.value.trim()!=EMPTY_STRING &&
                                    selectedBloodType.value!=null && selectedBloodCollection.value!=null &&
                                    total.value.trim()!= EMPTY_STRING && total.value.trim().toIntOrNull()!=null){
                                    preSavedItem=DailyBloodProcessing(
                                        unitType = selectedBloodType.value,
                                        campaign = selectedBloodCollection.value,
                                        processingDate = processingDate.value,
                                        total = total.value.trim().toIntOrNull(),
                                    )
                                    body.value = DailyBloodProcessingBody(
                                        id=if(old!=null && crudType==CrudType.UPDATE) old.id else null,
                                        hospitalId = hospital?.id,
                                        bloodBankId = bloodBank?.id,
                                        bloodUnitTypeId = selectedBloodType.value?.id,
                                        collectionId = selectedBloodCollection.value?.id,
                                        total = total.value.trim().toInt(),
                                        processingDate = processingDate.value.trim(),
                                        createdById =if(user!=null && old==null && crudType==CrudType.CREATE) user.id else null,
                                        updatedById =if(user!=null && old!=null && crudType==CrudType.UPDATE) user.id else null,
                                    )
                                    showDialog.value=true

                                }
                            }
                            CustomButton(label= CANCEL_LABEL, enabledBackgroundColor = Color.Red,
                                buttonShape = RectangleShape,
                                buttonShadowElevation = 6) {
                                Preferences.BloodBanks.DailyProcesses().delete()
                                navHostController.navigate(DailyBloodProcessingIndexRoute.route)
                            }
                        }
                    }

                }
            }
        }
    }
}
@Composable
private fun SaveDialog(
    showDialog:MutableState<Boolean>,
    controller: BloodBankComponentDepartmentController,
    body: MutableState<DailyBloodProcessingBody?>,
    item:DailyBloodProcessing?,

){
    val campaignDateOnly=if(item?.campaign?.collectionDate!=null) item.campaign.collectionDate.replaceAfterLast(SPACE, EMPTY_STRING) else EMPTY_STRING
    val processingDateOnly=if(item?.processingDate!=null) item.processingDate else EMPTY_STRING

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Label(SAVE_PROMPT)
                VerticalSpacer()
                item?.let {
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 3.dp)){
                        Label(label = CAMPAIGN_CODE_LABEL,text=it.campaign?.code?: EMPTY_STRING)
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Label(label = CAMPAIGN_DATE_LABEL,text=campaignDateOnly)
                        Label(label = COLLECTION_LABEL, text = "${it.campaign?.total}")
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 3.dp)){
                        Label(label= PROCESSING_DATE_LABEL,text=processingDateOnly)

                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Label(label = TOTAL_PROCESSED_LABEL,text="${it.total}")
                        Label(label= UNIT_TYPE_LABEL,text="${it.unitType?.name}")
                    }


                }
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                        enabledBackgroundColor = GREEN) {
                        body.value?.let{
                            controller.updateDailyProcessingNormal(it)
                            showDialog.value=false

                        }
                    }
                    CustomButton(label = CANCEL_LABEL,
                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                        enabledBackgroundColor = Color.Red) {
                        showDialog.value=false
                    }
                }
            }
        }
    }
}
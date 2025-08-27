package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.donationDepartment

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodCollectionBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.APHERESIS_DONORS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.APHERESIS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_LOCATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_BLOOD_COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DONATION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_APHERESIS_DONORS_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CAMPAIGN_CODE_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CAMPAIGN_TYPE_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_COLLECTION_DATE_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_DATE_ENTRY_MISSING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_DONATION_TYPE_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_RE_LOGIN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_TOTAL_COLLECTION_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_UNIT_TYPE_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.UNITS_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ONLY_STREET_OR_PLANNED_CAMPAIGNS
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.WhiteLabel
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

val centerArrangement=Arrangement.Center
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBloodCollectionCreatePage(navHostController: NavHostController){
    val controller:BloodBankController= viewModel()
    val body= remember { mutableStateOf<DailyBloodCollectionBody?>(null) }
    val state by controller.dailyBloodCollectionSingleState.observeAsState()
    val item = remember { mutableStateOf<DailyBloodCollection?>(null) }
    val hospital=Preferences.Hospitals().get()
    val bloodBank=Preferences.BloodBanks().get()
    val user=Preferences.User().get()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.bloodOptionsState.observeAsState()
    var campaign                    by remember { mutableStateOf<DailyBloodCollection?>(null) }
    var bloodTypes                  by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var donationTypes               by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    var campaignTypes               by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedBloodType           =  remember { mutableStateOf<BasicModel?>(null) }
    val selectedDonationType        =  remember { mutableStateOf<BasicModel?>(null) }
    val campaignCode                =  remember { mutableStateOf(EMPTY_STRING) }
    val selectedCampaignType        =  remember { mutableStateOf<BasicModel?>(null) }
    val numberOfDonations                      =  remember { mutableStateOf(EMPTY_STRING) }
    val apheresisDonors             =  remember { mutableStateOf(EMPTY_STRING) }
    val campaignLocation            =  remember { mutableStateOf(EMPTY_STRING) }
    val showSheet                   =  remember { mutableStateOf(false) }
    val collectionDateState         =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val collectionDate              =  remember { mutableStateOf(EMPTY_STRING) }
    val showCollectionDatePicker    =  remember { mutableStateOf(false) }
    val showDialog                  =  remember { mutableStateOf(false) }
    var loading                     by remember { mutableStateOf(false) }
    var fail                        by remember { mutableStateOf(false) }
    var success                     by remember { mutableStateOf(false) }
    var errors                      by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage                by remember { mutableStateOf(EMPTY_STRING) }
    LaunchedEffect(showSheet.value) {if(!showSheet.value){
            success=showSheet.value
        }
    }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{

        }
        is UiState.Success->{
            val s = optionsState as UiState.Success<BloodOptionsData>
            val r = s.data
            val data=r.data
            bloodTypes=data.bloodTypes
            donationTypes=data.donationTypes
            campaignTypes=data.campaignTypes
        }
        else->{
            settingsController.bloodOptions()
        }
    }
    when(state){
        is UiState.Loading->{LaunchedEffect(Unit){loading=true;fail=false;success=false}}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                errors=exception.errors?: emptyMap()
                showSheet.value=true
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = state as UiState.Success<DailyBloodCollectionSingleResponse>
                val r = s.data
                val data=r.data
                item.value=data
                loading=false;fail=false;success=true

                showSheet.value=true
            }
        }
        else->{}
    }

    DatePickerWidget(showCollectionDatePicker,collectionDateState,collectionDate)
    SaveDialog(showDialog,controller, body,campaign)
    Container(
        title = DAILY_BLOOD_COLLECTION_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(DailyBloodCollectionIndexRoute.route)},
        sheetColor = if(success) GREEN else Color.Red,
        sheetContent = {
            if(success){ Label(text= DATA_SAVED_LABEL, color = WHITE) }
            else{
             if(fail){
                 LazyColumn(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)) {
                    item{
                        Label(text=errorMessage, color = WHITE)
                        errors.forEach { (_, errorsList) ->
                            Column(horizontalAlignment = Alignment.Start){
                                errorsList.forEach { error->
                                    Label(error,color= WHITE)

                                }
                            }
                        }
                    }
                 }
             }
            }
        },
        sheetOnClick = {
            showSheet.value=false
            fail=false
            errorMessage=EMPTY_STRING
            errors= emptyMap()
            campaignCode.value= EMPTY_STRING
            collectionDate.value= EMPTY_STRING
            selectedBloodType.value=null
            selectedCampaignType.value=null
            selectedDonationType.value=null
            numberOfDonations.value= EMPTY_STRING
            apheresisDonors.value= EMPTY_STRING
        }
    ) {
        if(loading) LoadingScreen(modifier = Modifier.fillMaxSize())
        else{
            if(!success && !fail){
                Column(modifier=Modifier.fillMaxSize()){
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f).padding(5.dp)) {
                        item{
                            if(collectionDate.value.trim()!=EMPTY_STRING){
                                Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = centerArrangement){
                                    Label(label = SELECT_DATE_LABEL,collectionDate.value)
                                }
                            }
                            Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                                horizontalArrangement = centerArrangement){
                                CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                                    enabledBackgroundColor = ORANGE,
                                    onClick = { showCollectionDatePicker.value = !showCollectionDatePicker.value })
                            }
                            VerticalSpacer()
                            if(collectionDate.value.trim()!= EMPTY_STRING){
                                CustomInput(campaignCode,CAMPAIGN_CODE_LABEL)
                                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                                    Box(modifier=Modifier.fillMaxWidth().padding(horizontal=5.dp).weight(1f)){
                                        ComboBox(
                                            title=DONATION_TYPE_LABEL,
                                            selectedItem = selectedDonationType, loadedItems = donationTypes,
                                            selectedContent = {
                                                CustomInput(selectedDonationType.value?.name?: SELECT_LABEL)
                                            }) { Label(it?.name?:EMPTY_STRING)}
                                    }
                                    Box(modifier=Modifier.fillMaxWidth().padding(horizontal=3.dp).weight(1f)){
                                        ComboBox(
                                            title=CAMPAIGN_TYPE_LABEL,
                                            selectedItem = selectedCampaignType, loadedItems = campaignTypes,
                                            selectedContent = {
                                                CustomInput(selectedCampaignType.value?.name?: SELECT_LABEL)
                                            }) { Label(it?.name?:EMPTY_STRING)}
                                    }


                                }
                                Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                                    Box(modifier=Modifier.fillMaxWidth().padding(horizontal=5.dp)){
                                        ComboBox(
                                            title= UNIT_TYPE_LABEL,
                                            selectedItem = selectedBloodType, loadedItems = if(selectedDonationType.value?.id==2)bloodTypes.filter { it.id in listOf(7,9) }else bloodTypes,
                                            selectedContent = {
                                                CustomInput(selectedBloodType.value?.name?: SELECT_UNIT_TYPE_LABEL)
                                            }) { Label(it?.name?:EMPTY_STRING)}
                                    }

                                }
                                if((selectedCampaignType.value?.id?:0) in listOf(1,2) && selectedDonationType.value?.id != 2){
                                    CustomInput(campaignLocation,CAMPAIGN_LOCATION_LABEL)
                                    Box(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(Color.Red),
                                        contentAlignment = Alignment.CenterEnd){
                                        WhiteLabel(ONLY_STREET_OR_PLANNED_CAMPAIGNS, fontSize = 12, fontWeight = FontWeight.Bold,
                                            maximumLines = 2, textOverflow = TextOverflow.Ellipsis, softWrap = true,
                                            paddingEnd = 5)
                                    }
                                    VerticalSpacer()
                                }
                                CustomInput(value=numberOfDonations,label= UNITS_NUMBER_LABEL, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onTextChange = {if(it.toIntOrNull()!=null)numberOfDonations.value=it else numberOfDonations.value="0"})
                                if(selectedDonationType.value?.id==2) CustomInput(value=apheresisDonors,label=APHERESIS_DONORS_LABEL, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onTextChange = {if(it.toIntOrNull()!=null) apheresisDonors.value=it else apheresisDonors.value="0"})
                                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    val isApheresis=(selectedDonationType.value?.id == 2)
                                    val notApheresis=(selectedDonationType.value?.id != 2)
                                    val apheresisNotNull=(apheresisDonors.value.trim()!=EMPTY_STRING)
                                    val apheresisIsInt=apheresisDonors.value.trim().toIntOrNull()!=null
                                    val apheresisDonorsSet=(isApheresis && apheresisNotNull && apheresisIsInt) || notApheresis
                                    val hasUser=user!=null
                                    val campaignCodeIsSet=campaignCode.value!=EMPTY_STRING
                                    val hasHospital=hospital!=null
                                    val hasBloodType=selectedBloodType.value !=null
                                    val hasDonationType=selectedDonationType.value!=null
                                    val hasCampaignType=selectedCampaignType.value!=null
                                    val amountIsSet=numberOfDonations.value.trim()!=EMPTY_STRING
                                    val amountIsInt=amountIsSet && (numberOfDonations.value.trim().toIntOrNull()!=null)
                                    val locationSet=(selectedCampaignType.value?.id?:0) in listOf(1,2) && (campaignLocation.value.trim()!=EMPTY_STRING)
                                    val locationRequirementSet=locationSet || (selectedCampaignType.value?.id?:0)==3
                                    val hasCollectionDate=collectionDate.value.trim()!=EMPTY_STRING
                                    CustomButton(label= SAVE_CHANGES_LABEL, enabledBackgroundColor = GREEN,
                                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                                        enabled = (hasCollectionDate && hasBloodType && hasDonationType &&
                                                hasCampaignType  && amountIsInt && locationRequirementSet &&
                                                hasUser && hasHospital && apheresisDonorsSet && campaignCodeIsSet),
                                    ) {
                                        if(hasBloodType && hasDonationType && campaignCodeIsSet &&
                                            hasCampaignType  && amountIsInt  && hasCollectionDate &&
                                            locationRequirementSet && hasUser && hasHospital && apheresisDonorsSet){
                                            campaign= DailyBloodCollection(
                                                bloodType = selectedBloodType.value,
                                                code = campaignCode.value,
                                                donationType = selectedDonationType.value,
                                                campaignType = selectedCampaignType.value,
                                                total = numberOfDonations.value.trim().toIntOrNull(),
                                                apheresisDonors = apheresisDonors.value.trim().toIntOrNull(),
                                                collectionDate = collectionDate.value
                                            )
                                            body.value = DailyBloodCollectionBody(
                                                hospitalId = hospital?.id,
                                                bloodBankId = bloodBank?.id,
                                                bloodUnitTypeId = selectedBloodType.value?.id,
                                                code = campaignCode.value,
                                                donationTypeId = selectedDonationType.value?.id,
                                                campaignTypeId = selectedCampaignType.value?.id,
                                                total = numberOfDonations.value.trim().toIntOrNull(),
                                                collectionDate = collectionDate.value.trim(),
                                                campaignLocation = campaignLocation.value.trim(),
                                                apheresisDonors = if(selectedDonationType.value ==donationTypes[1]) apheresisDonors.value.trim().toIntOrNull() else null,
                                                createdById = user?.id?:0,
                                            )
                                            showDialog.value=true

                                        }
                                        else{
                                            val errorMap= mutableMapOf<String,List<String>>()
                                            if(!hasCollectionDate) errorMap["Date"]= listOf(ERROR_COLLECTION_DATE_REQUIRED_LABEL)
                                            if(!hasBloodType){
                                                errorMap["Unit Type"] = listOf(ERROR_UNIT_TYPE_FIELD_REQUIRED_LABEL)
                                            }
                                            if(!hasDonationType){
                                                errorMap["Donation Type"] = listOf(ERROR_DONATION_TYPE_REQUIRED_LABEL)
                                            }
                                            if(!hasCampaignType){
                                                errorMap["Campaign Type"] = listOf(ERROR_CAMPAIGN_TYPE_REQUIRED_LABEL)
                                            }
                                            if(!amountIsInt){
                                                errorMap["Collection"] = listOf(ERROR_TOTAL_COLLECTION_REQUIRED_LABEL)
                                            }
                                            if(!locationRequirementSet){
                                                errorMap["Location"] = listOf("نوع الحملة ${selectedCampaignType.value?.name?:"خارجى/منظم"} لكن لم يتم ادخال مكانها ")
                                            }
                                            if(!hasUser){ errorMap["Login"] = listOf(ERROR_RE_LOGIN_LABEL) }
                                            if(!campaignCodeIsSet){ errorMap["Code"] = listOf(ERROR_CAMPAIGN_CODE_REQUIRED_LABEL) }

                                            if(!hasHospital){ errorMap["Re-Login"] = listOf(ERROR_RE_LOGIN_LABEL) }
                                            if(!apheresisDonorsSet){
                                                val apheresisErrorsList= mutableListOf<String>()
                                                if(!apheresisNotNull){
                                                    apheresisErrorsList.add(ERROR_APHERESIS_DONORS_REQUIRED_LABEL)
                                                    errorMap["Apheresis"] = apheresisErrorsList
                                                }
                                                if(!apheresisIsInt){
                                                    apheresisErrorsList.add(ERROR_APHERESIS_DONORS_REQUIRED_LABEL)
                                                    errorMap["Number"] = apheresisErrorsList
                                                }
                                            }
                                            errors=errorMap
                                            errorMessage=ERROR_DATE_ENTRY_MISSING_LABEL
                                            showSheet.value=true
                                            fail=true
                                        }
                                    }
                                    CustomButton(label= CANCEL_LABEL,
                                        enabledBackgroundColor = Color.Red,
                                        buttonShadowElevation = 6,
                                        buttonShape = RectangleShape) {
                                        navHostController.navigate(DailyBloodCollectionIndexRoute.route)
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

@Composable
private fun SaveDialog(showDialog: MutableState<Boolean>,
                       controller: BloodBankController,
                       body: MutableState<DailyBloodCollectionBody?>,
                       item:DailyBloodCollection?){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Label(SAVE_PROMPT)
                VerticalSpacer()
                Column(modifier=Modifier.fillMaxWidth().padding(5.dp),horizontalAlignment = Alignment.Start){
                    Label(label= DATE_LABEL,text=item?.collectionDate?: EMPTY_STRING)
                    Label(label= CAMPAIGN_CODE_LABEL,text=item?.code?: EMPTY_STRING)
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Label(label= CAMPAIGN_TYPE_LABEL,text=item?.campaignType?.name?: EMPTY_STRING)
                        Label(label= DONATION_TYPE_LABEL,text=item?.donationType?.name?: EMPTY_STRING)
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Label(label = UNIT_TYPE_LABEL,text=item?.bloodType?.name?: EMPTY_STRING)
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        if(item?.donationType?.id==2) Label(label= APHERESIS_DONORS_LABEL,text="${item?.apheresisDonors?:0}")
                        Label(label= UNITS_NUMBER_LABEL,text="${item?.total?:0}")
                    }
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                        enabledBackgroundColor = GREEN) {
                        body.value?.let{
                            controller.storeDailyCollection(it)
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
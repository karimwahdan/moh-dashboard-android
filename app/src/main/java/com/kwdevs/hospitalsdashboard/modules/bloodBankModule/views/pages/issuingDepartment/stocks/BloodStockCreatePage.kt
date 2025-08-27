package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.stocks

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.EDIT_DAILY_BLOOD_STOCK_DATE
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.routes.BloodStockIndexRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.AT_12_PM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AT_6_AM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.AT_6_PM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_BLOOD_STOCK_UPDATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATA_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EMERGENCY_STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.INSERT_AT_LEAST_ONE_ITEM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MIDNIGHT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MISSING_BLOOD_GROUP_DATA
import com.kwdevs.hospitalsdashboard.views.assets.MISSING_PLASMA_DATA
import com.kwdevs.hospitalsdashboard.views.assets.OK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PALE_ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_PROMPT
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SHOW_DATE_TIME_PICKER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNDER_INSPECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.getFormattedDateJavaTime
import com.kwdevs.hospitalsdashboard.views.rcs
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodStockCreatePage(navHostController: NavHostController){
    //val item = remember { mutableStateOf<DailyBloodStock?>(null) }
    //val keepSelectedUnitType    =  remember { mutableStateOf(false) }
    //val selectedBloodGroup      =  remember { mutableStateOf<BasicModel?>(null) }
    //val keepSelectedBloodGroup  =  remember { mutableStateOf(false) }
    val hospital            = Preferences.Hospitals().get()
    val bloodBank           = Preferences.BloodBanks().get()
    val user                = Preferences.User().get()
    val permissions         = user?.roles?.flatMap { it.permissions }?.map{it.slug?:EMPTY_STRING}?: emptyList()
    var countdown by remember { mutableStateOf(Duration.ZERO) }
    var nextLabel by remember { mutableStateOf(EMPTY_STRING) }
    val cairoZone = ZoneId.of("Africa/Cairo")
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss") // 24-hour format
    var formattedTime by remember { mutableStateOf(EMPTY_STRING) }
    val timeBlocks  = listOf(
        Pair("00",MIDNIGHT_LABEL),
        Pair("06",AT_6_AM_LABEL),
        Pair("12",AT_12_PM_LABEL),
        Pair("18",AT_6_PM_LABEL))


    val controller: BloodBankController = viewModel()
    val settingsController:SettingsController = viewModel()
    
    val state               by controller.dailyBloodStocksPaginationState.observeAsState()
    val optionsState        by settingsController.bloodOptionsState.observeAsState()

    var bloodTypes          by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
    val selectedBloodType   =  remember { mutableStateOf<BasicModel?>(null) }

    var bloodGroups         by remember { mutableStateOf<List<BasicModel>>(emptyList()) }

    val entryTimeState      =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val entryDate           =  remember { mutableStateOf(EMPTY_STRING) }
    val showEntryDatePicker =  remember { mutableStateOf(false) }

    val selectedTimeBlock   =  remember { mutableStateOf<Pair<String,String>?>(null) }
    val amount              =  remember { mutableStateOf(EMPTY_STRING) }
    val underInspection     =  remember { mutableStateOf(EMPTY_STRING) }
    val isUnderInspection   =  remember { mutableStateOf(false) }
    val showSheet           =  remember { mutableStateOf(false) }
    var errors              by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage        by remember { mutableStateOf(EMPTY_STRING) }
    var loading             by remember { mutableStateOf(false) }
    var fail                by remember { mutableStateOf(false) }
    var success             by remember { mutableStateOf(false) }

    val items               =  remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }
    val bodies              =  remember { mutableStateOf<List<DailyBloodStockBody>>(emptyList()) }
    val showSaveDialog      =  remember { mutableStateOf(false) }


    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<BloodOptionsData>
            val r = s.data
            val data=r.data
            bloodGroups=data.bloodGroups
            bloodTypes=data.bloodTypes
            LaunchedEffect(Unit) {
                //if(!keepSelectedBloodGroup.value)selectedBloodGroup.value=null
                //if(!keepSelectedUnitType.value)selectedBloodType.value=null
                amount.value=EMPTY_STRING
                underInspection.value=EMPTY_STRING
                isUnderInspection.value=false
            }

        }
        else->{ settingsController.bloodOptions() }
    }
    when(state){
        is UiState.Loading->{ LaunchedEffect(Unit){loading=true;fail=false;success=false}}
        is UiState.Error->{
            LaunchedEffect(Unit){
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                val errorsResponse=exception.errors
                errors=errorsResponse?: emptyMap()
                errorMessage=exception.message?:EMPTY_STRING
                showSheet.value=true

            }

        }
        is UiState.Success->{

            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<ApiResponse<PaginationData<DailyBloodStock>>>
                val r = s.data
                val pagination=r.pagination
                pagination.data
                success=true
                showSheet.value=true
                //if(!keepSelectedBloodGroup.value) selectedBloodGroup.value=null
                //if(!keepSelectedUnitType.value) selectedBloodType.value=null
                amount.value= EMPTY_STRING
                underInspection.value=EMPTY_STRING
                isUnderInspection.value=false
                items.value= emptyList()
                bodies.value= emptyList()
            }
        }
        else->{ }
    }

    LaunchedEffect(Unit) {
        entryDate.value= getFormattedDateJavaTime(entryTimeState)

        while (true) {
            val now = ZonedDateTime.now(cairoZone) // ⬅️ لازم يبقى جوا اللوب
            formattedTime = now.format(formatter)

            val currentHour = now.hour

            // حوّل الساعات من String لـ Int
            val blockHours = timeBlocks.map { it.first.toInt() }

            // لاقي أول block أكبر من الساعة الحالية
            val nextBlockIndex = blockHours.indexOfFirst { it > currentHour }

            val currentBlock = if (nextBlockIndex != -1) timeBlocks[nextBlockIndex] else timeBlocks.first() // اليوم الجديد

            nextLabel = currentBlock.second
            val nextHour = currentBlock.first.toInt()

            // ابنِ target كامل (ساعة + دقيقة + ثانية)
            // 🕒 بناء targetDateTime كـ ZonedDateTime في نفس اليوم بتوقيت القاهرة
            var targetDateTime = now.toLocalDate()
                .atTime(LocalTime.of(nextHour, 0))
                .atZone(cairoZone)

            // ✅ هنا نستخدم isAfter على ZonedDateTime
            if (!targetDateTime.isAfter(now)) targetDateTime = targetDateTime.plusDays(1)

            countdown = Duration.between(now, targetDateTime)

            delay(1000) // يحدث كل ثانية
        }
    }
    LaunchedEffect(entryDate.value) {
        bodies.value=bodies.value.map { it.copy(entryDate=entryDate.value) }
    }
    LaunchedEffect(selectedTimeBlock.value) {
        selectedTimeBlock.value?.let{t-> bodies.value=bodies.value.map { it.copy(timeBlock =t.first) } }
    }

    DatePickerWidget(showEntryDatePicker,entryTimeState,entryDate)
    SaveDialog(showDialog=showSaveDialog,controller=controller,bodies= bodies)
    Container(
        title = DAILY_BLOOD_STOCK_UPDATE_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(BloodStockIndexRoute.route)},
        sheetColor = if(success) GREEN else Color.Red,
        sheetContent = {
            if(success){
                Label(text= DATA_SAVED_LABEL, color = WHITE,maximumLines = 3,
                    textOverflow = TextOverflow.Ellipsis,softWrap = true)
            }
            else{
                Column {
                    Label(text=errorMessage, color = WHITE,
                        maximumLines = 3,
                        textOverflow = TextOverflow.Ellipsis,
                        softWrap = true)
                    VerticalSpacer()
                    if(errors.isNotEmpty()){
                        errors.forEach { (_, errorsList) ->
                            errorsList.forEach { error->
                                Label(text=error, color = WHITE,
                                    maximumLines = 3,
                                    textOverflow = TextOverflow.Ellipsis,
                                    softWrap = true)
                            }
                        }
                    }
                }
            }
        },
        sheetOnClick = {
            if(fail){
                showSheet.value=false;fail=false;loading=false
                bodies.value= emptyList()
                items.value= emptyList()
                underInspection.value=EMPTY_STRING
                selectedTimeBlock.value=null
                isUnderInspection.value=false
                selectedBloodType.value=null
            }
            if(success){navHostController.navigate(BloodStockIndexRoute.route)}
        }
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else {
            if(success && !fail) {
                SuccessScreen(modifier=Modifier.fillMaxSize()){
                    ColumnContainer {
                        Image(painter = painterResource(R.drawable.logo),contentDescription = null)
                        Label(DATA_SAVED_LABEL)
                        CustomButton(label= OK_LABEL,
                            enabledBackgroundColor = GREEN,
                            buttonShadowElevation = 6,
                            buttonShape = RectangleShape) {
                            navHostController.navigate(IssuingDepartmentHomeRoute.route)
                        }
                    }
                }
            }
            if(!success && !fail){
                Column(modifier=Modifier.fillMaxSize()){
                    LazyColumn(modifier= Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(5.dp)) {
                        item{
                            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                                        //Label(formattedTime)
                                        //Label(nextLabel)
                                        Label("${countdown.toHours()}:${countdown.toMinutesPart()}:${countdown.toSecondsPart()}")
                                    }
                                }
                            }
                            if(entryDate.value.trim()!=EMPTY_STRING){
                                Row(modifier=Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Label(text=entryDate.value)
                                    if(permissions.contains(EDIT_DAILY_BLOOD_STOCK_DATE)){
                                        IconButton(R.drawable.ic_edit_blue) {
                                            showEntryDatePicker.value=true
                                        }

                                    }

                                }
                                Box(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)){
                                    ComboBox(
                                        title = TIME_LABEL,
                                        hasTitle = true,
                                        selectedItem = selectedTimeBlock,
                                        loadedItems = timeBlocks,
                                        selectedContent = { CustomInput(selectedTimeBlock.value?.second?:SELECT_TIME_LABEL)},
                                    ) {
                                        Label(it?.second?:EMPTY_STRING)
                                    }
                                }
                            }
                            else{
                                Row(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                    horizontalArrangement = Arrangement.Center){
                                    if(permissions.contains(EDIT_DAILY_BLOOD_STOCK_DATE)){
                                        CustomButton(label = SHOW_DATE_TIME_PICKER_LABEL ,
                                            enabledBackgroundColor = ORANGE,
                                            onClick = { showEntryDatePicker.value = !showEntryDatePicker.value })
                                    }

                                }
                            }
                            if(entryDate.value.trim()!=EMPTY_STRING && selectedTimeBlock.value!=null){
                                CustomCheckbox(UNDER_INSPECTION_LABEL,isUnderInspection)
                                if(!isUnderInspection.value){
                                    Box(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)){
                                        ComboBox(
                                            title=UNIT_TYPE_LABEL,
                                            selectedItem = selectedBloodType, loadedItems = bloodTypes,
                                            selectedContent = {
                                                CustomInput(selectedBloodType.value?.name?:SELECT_LABEL)
                                            }) { Label(it?.name?:EMPTY_STRING)}
                                    }
                                }
                                HorizontalDivider()
                                items.value.filter { it.underInspection!=null }.forEach {item->
                                    ColumnContainer {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(UNDER_INSPECTION_LABEL)
                                            HorizontalSpacer()
                                            Span(text = (item.amount?:0).toString(), backgroundColor = BLUE, color = WHITE)
                                            HorizontalSpacer()
                                            IconButton(R.drawable.ic_delete_red) {
                                                items.value=items.value.filter { it!=item }
                                                bodies.value.forEach { body->
                                                    if(
                                                        body.underInspection !=null
                                                    ){
                                                        bodies.value=bodies.value.filter { it!=body }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                                items.value.filter{it.bloodGroup!=null}.sortedBy { it.bloodGroupId?:0 }.chunked(2)
                                    .forEach { subList->
                                        Row(modifier=Modifier.fillMaxWidth()){
                                            subList.forEach { item->
                                                Box(modifier= Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f)){
                                                    ColumnContainer {
                                                        Row(verticalAlignment = Alignment.CenterVertically){
                                                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                                                Label(item.bloodUnitType?.name?:EMPTY_STRING)
                                                                Span(text = item.bloodGroup?.name?:EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
                                                            }
                                                            HorizontalSpacer()
                                                            Column(verticalArrangement = Arrangement.Center){
                                                                Label((item.amount?:0).toString())
                                                                Span((item.emergency?:0).toString(), backgroundColor = ORANGE)
                                                            }
                                                            HorizontalSpacer()
                                                            IconButton(R.drawable.ic_delete_red) {
                                                                items.value=items.value.filter { it!=item }
                                                                bodies.value.forEach { body->
                                                                    if(
                                                                        body.bloodGroupId==item.bloodGroupId &&
                                                                        body.bloodUnitTypeId==item.bloodUnitTypeId &&
                                                                        body.amount==item.amount
                                                                    ){
                                                                        bodies.value=bodies.value.filter { it!=body }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        VerticalSpacer()
                                    }
                                HorizontalDivider()
                                if(!isUnderInspection.value){
                                    if(selectedBloodType.value!=null){
                                        if(selectedBloodType.value?.id !in 3..6){
                                            bloodGroups.chunked(2).forEach{subBloodGroupList->
                                                Row(modifier=Modifier.fillMaxWidth()){
                                                    subBloodGroupList.forEach { bloodGroup->
                                                        val bloodGroupValue = remember { mutableStateOf(EMPTY_STRING) }
                                                        val emergencyValue= remember { mutableStateOf(EMPTY_STRING) }
                                                        Box(modifier= Modifier
                                                            .fillMaxWidth()
                                                            .weight(1f))
                                                        {
                                                            ColumnContainer(shape = RectangleShape,
                                                                background = PALE_ORANGE) {
                                                                Row(verticalAlignment = Alignment.CenterVertically){
                                                                    Column(modifier= Modifier
                                                                        .fillMaxWidth()
                                                                        .weight(1f),
                                                                        horizontalAlignment = Alignment.CenterHorizontally){
                                                                        Label(bloodGroup.name?:EMPTY_STRING)

                                                                        Box(modifier=Modifier.padding(5.dp)){
                                                                            TextField(
                                                                                modifier=Modifier.background(
                                                                                    color= WHITE, shape = rcs(20))
                                                                                    .clip(rcs(20))
                                                                                    .border(width=1.dp,shape= rcs(20),color= colorResource(R.color.orange3))
                                                                                    .padding(5.dp),
                                                                                value = bloodGroupValue.value,
                                                                                onValueChange = { input ->
                                                                                    if(input.length<7){
                                                                                        val bloodGroupIsInt=bloodGroupValue.value.trim().toIntOrNull()!=null
                                                                                        bloodGroupValue.value=input.filter { it.isDigit() }
                                                                                        if(bloodGroupValue.value.trim() !=EMPTY_STRING && bloodGroupIsInt){

                                                                                            val newDBS=DailyBloodStock(
                                                                                                id=null,
                                                                                                hospitalId = hospital?.id,
                                                                                                bloodBankId = bloodBank?.id,
                                                                                                bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                bloodGroupId = bloodGroup.id,
                                                                                                amount = bloodGroupValue.value.toInt(),
                                                                                                underInspection = null,
                                                                                                bloodGroup = bloodGroup,
                                                                                                bloodUnitType = selectedBloodType.value,
                                                                                                entryDate=entryDate.value,
                                                                                                timeBlock = selectedTimeBlock.value?.first,
                                                                                                hospital=hospital,
                                                                                                bloodBank = bloodBank,
                                                                                            )
                                                                                            val updatedList = items.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                        savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                            }
                                                                                                .toMutableList()

                                                                                            updatedList.add(newDBS) // add the new entry at the end
                                                                                            items.value = updatedList

                                                                                            val newDBSBody=DailyBloodStockBody(
                                                                                                hospitalId = hospital?.id,
                                                                                                bloodBankId = bloodBank?.id,
                                                                                                bloodGroupId = bloodGroup.id,
                                                                                                bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                amount = bloodGroupValue.value.toInt(),
                                                                                                underInspection = null,
                                                                                                entryDate=entryDate.value,
                                                                                                timeBlock = selectedTimeBlock.value?.first,
                                                                                                createdById = user?.id,
                                                                                            )
                                                                                            val updatedBodyList = bodies.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                        savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                            }
                                                                                                .toMutableList()

                                                                                            updatedBodyList.add(newDBSBody) // add the new entry at the end
                                                                                            bodies.value = updatedBodyList

                                                                                        }
                                                                                        else{
                                                                                            items.value=items.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                        savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                            }
                                                                                            bodies.value = bodies.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                        savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                },
                                                                                placeholder = {Label(STOCK_LABEL)},
                                                                                shape= rcs(20),
                                                                                colors=TextFieldDefaults.colors(focusedContainerColor = WHITE,
                                                                                    unfocusedContainerColor = WHITE,
                                                                                    focusedIndicatorColor = Color.Transparent,
                                                                                    unfocusedIndicatorColor = Color.Transparent),
                                                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                                                            )
                                                                        }
                                                                        Box(modifier=Modifier.padding(5.dp)){
                                                                            TextField(
                                                                                modifier=Modifier.background(
                                                                                    color= WHITE, shape = rcs(20))
                                                                                    .clip(rcs(20))
                                                                                    .border(width=1.dp,shape= rcs(20),color= colorResource(R.color.orange3))
                                                                                    .padding(5.dp),
                                                                                enabled = bloodGroupValue.value!=EMPTY_STRING,
                                                                                value = emergencyValue.value,
                                                                                onValueChange = { input ->
                                                                                    val isInt = input.trim().toIntOrNull() != null
                                                                                    val bloodGroupIsInt=bloodGroupValue.value.trim().toIntOrNull()!=null

                                                                                    if(bloodGroupIsInt && isInt && input.length<7){
                                                                                        val lessThanBloodGroupUnits=input.trim().toInt()<bloodGroupValue.value.trim().toInt()
                                                                                        if(lessThanBloodGroupUnits){
                                                                                            emergencyValue.value=input.filter { it.isDigit() }
                                                                                            if(emergencyValue.value.trim() !=EMPTY_STRING && emergencyValue.value.any { it.isDigit() }){

                                                                                                val newDBS=DailyBloodStock(
                                                                                                    id=null,
                                                                                                    hospitalId = hospital?.id,
                                                                                                    bloodBankId = bloodBank?.id,
                                                                                                    bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                    bloodGroupId = bloodGroup.id,
                                                                                                    amount = bloodGroupValue.value.toInt(),
                                                                                                    emergency = emergencyValue.value.toInt(),
                                                                                                    underInspection = null,
                                                                                                    bloodGroup = bloodGroup,
                                                                                                    bloodUnitType = selectedBloodType.value,
                                                                                                    entryDate=entryDate.value,
                                                                                                    timeBlock = selectedTimeBlock.value?.first,
                                                                                                    hospital=hospital,
                                                                                                    bloodBank = bloodBank,
                                                                                                )
                                                                                                val updatedList = items.value.filter { savedItem ->
                                                                                                    savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                            savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                                }
                                                                                                    .toMutableList()

                                                                                                updatedList.add(newDBS) // add the new entry at the end
                                                                                                items.value = updatedList

                                                                                                val newDBSBody=DailyBloodStockBody(
                                                                                                    hospitalId = hospital?.id,
                                                                                                    bloodBankId = bloodBank?.id,
                                                                                                    bloodGroupId = bloodGroup.id,
                                                                                                    bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                    amount = bloodGroupValue.value.toInt(),
                                                                                                    emergency = emergencyValue.value.toInt(),
                                                                                                    underInspection = null,
                                                                                                    entryDate=entryDate.value,
                                                                                                    timeBlock = selectedTimeBlock.value?.first,
                                                                                                    createdById = user?.id,
                                                                                                )
                                                                                                val updatedBodyList = bodies.value.filter { savedItem ->
                                                                                                    savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                            savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                                }
                                                                                                    .toMutableList()

                                                                                                updatedBodyList.add(newDBSBody) // add the new entry at the end
                                                                                                bodies.value = updatedBodyList

                                                                                            }
                                                                                            else{
                                                                                                items.value=items.value.filter { savedItem ->
                                                                                                    savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                            savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                                }
                                                                                                bodies.value = bodies.value.filter { savedItem ->
                                                                                                    savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                            savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                },
                                                                                shape= rcs(20),
                                                                                placeholder = {Label(EMERGENCY_STOCK_LABEL)},
                                                                                colors=TextFieldDefaults.colors(
                                                                                    focusedContainerColor = WHITE,
                                                                                    unfocusedContainerColor = WHITE,
                                                                                    focusedIndicatorColor = Color.Transparent,
                                                                                    unfocusedIndicatorColor = Color.Transparent),
                                                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                                                            )
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            bloodGroups.filter { it.id in listOf(1,3,5,7) }.chunked(2).forEach{subBloodGroupList->
                                                Row(modifier=Modifier.fillMaxWidth()){
                                                    subBloodGroupList.forEach { bloodGroup->
                                                        val bloodGroupValue = remember { mutableStateOf(EMPTY_STRING) }
                                                        Box(modifier= Modifier
                                                            .fillMaxWidth()
                                                            .weight(1f))
                                                        {
                                                            ColumnContainer(shape = RectangleShape,
                                                                background = PALE_ORANGE) {
                                                                Row(verticalAlignment = Alignment.CenterVertically){
                                                                    Column(modifier= Modifier.fillMaxWidth().weight(1f),
                                                                        horizontalAlignment = Alignment.CenterHorizontally){
                                                                        Label((bloodGroup.name?:EMPTY_STRING).replaceAfter(" ",EMPTY_STRING))
                                                                        Box(modifier=Modifier.padding(5.dp)){
                                                                            TextField(
                                                                                modifier=Modifier.background(
                                                                                    color= WHITE, shape = rcs(20))
                                                                                    .clip(rcs(20))
                                                                                    .border(width=1.dp,shape= rcs(20),color= colorResource(R.color.orange3))
                                                                                    .padding(5.dp),
                                                                                value = bloodGroupValue.value,
                                                                                onValueChange = { input ->
                                                                                    if(input.length<7){
                                                                                        bloodGroupValue.value=input.filter { it.isDigit() }
                                                                                        if(bloodGroupValue.value.trim() !=EMPTY_STRING && bloodGroupValue.value.any { it.isDigit() }){

                                                                                            val newDBS=DailyBloodStock(
                                                                                                id=null,
                                                                                                hospitalId = hospital?.id,
                                                                                                bloodBankId = bloodBank?.id,
                                                                                                bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                bloodGroupId = bloodGroup.id,
                                                                                                amount = bloodGroupValue.value.toInt(),
                                                                                                underInspection = null,
                                                                                                bloodGroup = bloodGroup,
                                                                                                bloodUnitType = selectedBloodType.value,
                                                                                                entryDate=entryDate.value,
                                                                                                timeBlock = selectedTimeBlock.value?.first,
                                                                                                hospital=hospital,
                                                                                                bloodBank = bloodBank,
                                                                                            )
                                                                                            val updatedList = items.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                        savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                            }
                                                                                                .toMutableList()

                                                                                            updatedList.add(newDBS) // add the new entry at the end
                                                                                            items.value = updatedList

                                                                                            val newDBSBody=DailyBloodStockBody(
                                                                                                hospitalId = hospital?.id,
                                                                                                bloodBankId = bloodBank?.id,
                                                                                                bloodGroupId = bloodGroup.id,
                                                                                                bloodUnitTypeId = selectedBloodType.value?.id,
                                                                                                amount = bloodGroupValue.value.toInt(),
                                                                                                underInspection = null,
                                                                                                entryDate=entryDate.value,
                                                                                                timeBlock = selectedTimeBlock.value?.first,
                                                                                                createdById = user?.id,
                                                                                            )
                                                                                            val updatedBodyList = bodies.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != newDBS.bloodGroupId ||
                                                                                                        savedItem.bloodUnitTypeId != newDBS.bloodUnitTypeId
                                                                                            }
                                                                                                .toMutableList()

                                                                                            updatedBodyList.add(newDBSBody) // add the new entry at the end
                                                                                            bodies.value = updatedBodyList

                                                                                        }
                                                                                        else{
                                                                                            items.value=items.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                        savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                            }
                                                                                            bodies.value = bodies.value.filter { savedItem ->
                                                                                                savedItem.bloodGroupId != bloodGroup.id ||
                                                                                                        savedItem.bloodUnitTypeId != selectedBloodType.value?.id
                                                                                            }

                                                                                        }

                                                                                    }
                                                                                },
                                                                                shape= rcs(20),
                                                                                colors=TextFieldDefaults.colors(
                                                                                    focusedContainerColor = WHITE,
                                                                                    unfocusedContainerColor = WHITE,
                                                                                    focusedIndicatorColor = Color.Transparent,
                                                                                    unfocusedIndicatorColor = Color.Transparent),
                                                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                                                            )
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
                                if( isUnderInspection.value){
                                    Box(modifier=Modifier.padding(5.dp)){
                                        TextField(
                                            modifier=Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color= WHITE, shape = rcs(20))
                                                .clip(rcs(20))
                                                .border(width=1.dp,shape= rcs(20),color= colorResource(R.color.orange3))
                                                .padding(5.dp),
                                            value = underInspection.value,
                                            label = {Label(UNDER_INSPECTION_LABEL)},
                                            onValueChange = { input ->
                                                if(input.length<7){
                                                    underInspection.value=input.filter { it.isDigit() }
                                                    if(underInspection.value.trim() !=EMPTY_STRING && underInspection.value.any { it.isDigit() }){

                                                        val newDBS=DailyBloodStock(
                                                            id=null,
                                                            hospitalId = hospital?.id,
                                                            bloodBankId = bloodBank?.id,
                                                            underInspection = isUnderInspection.value,
                                                            amount = underInspection.value.toInt(),
                                                            entryDate=entryDate.value,
                                                            timeBlock = selectedTimeBlock.value?.first,
                                                            hospital=hospital,
                                                            bloodBank = bloodBank,
                                                        )
                                                        val updatedList = items.value.filter { savedItem ->savedItem.underInspection ==null}.toMutableList()

                                                        updatedList.add(newDBS) // add the new entry at the end
                                                        items.value = updatedList

                                                        val newDBSBody=DailyBloodStockBody(
                                                            hospitalId = hospital?.id,
                                                            bloodBankId = bloodBank?.id,
                                                            underInspection = if(isUnderInspection.value) 1 else 0,
                                                            amount = underInspection.value.toInt(),
                                                            entryDate=entryDate.value,
                                                            timeBlock = selectedTimeBlock.value?.first,
                                                            createdById = user?.id,
                                                        )
                                                        val updatedBodyList = bodies.value.filter { savedItem ->savedItem.underInspection ==1}
                                                            .toMutableList()

                                                        updatedBodyList.add(newDBSBody) // add the new entry at the end
                                                        bodies.value = updatedBodyList

                                                    }
                                                    else{
                                                        items.value=items.value.filter { savedItem ->savedItem.underInspection ==null  }
                                                        bodies.value = bodies.value.filter {savedItem ->savedItem.underInspection ==null }

                                                    }
                                                }
                                            },
                                            shape= rcs(20),
                                            colors=TextFieldDefaults.colors(focusedContainerColor = WHITE,
                                                unfocusedContainerColor = WHITE,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                    }
                                }
                                VerticalSpacer()
                            }
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        CustomButton(label= SAVE_CHANGES_LABEL,
                            enabledBackgroundColor = GREEN,
                            buttonShape = RectangleShape,
                            enabled = bodies.value.isNotEmpty(),
                            buttonShadowElevation = 6) {
                            val plasmaGroupIds = listOf(1, 3, 5, 7)
                            val bloodGroupIds = listOf(1, 2, 3, 4, 5, 6, 7, 8)

                            val plasmaBodies=bodies.value.filter { it.bloodUnitTypeId in listOf(3,4,5,6) }

                            val bloodGroupBodies=bodies.value.filter { it !in plasmaBodies }
                            val allPlasmaGroupsPresent = if (plasmaBodies.isEmpty()) true // Ignore check if plasmaBodies is empty
                            else {
                                val validPlasmaMap = plasmaBodies
                                    .filter { (it.amount ?: 0) > 0 }
                                    .groupBy { it.bloodGroupId }

                                plasmaGroupIds.all { it in validPlasmaMap }
                            }

                            val allBloodGroupsPresent = if (bloodGroupBodies.isEmpty()) true
                            else {
                                val validBloodMap = bloodGroupBodies
                                    .filter{it.amount!=null}
                                    .groupBy { it.bloodGroupId }
                                bloodGroupIds.all { it in validBloodMap }
                            }
                            if((allBloodGroupsPresent && allPlasmaGroupsPresent && bodies.value.isNotEmpty()) ||
                                bodies.value.any { it.underInspection !=null }){
                                showSaveDialog.value=true
                            }
                            else{
                                showSheet.value=true
                                if(!allBloodGroupsPresent){
                                    var xx=" "
                                    val z=bloodGroupBodies
                                        .filter{it.amount!=null}
                                        .groupBy { it.bloodGroupId }
                                    z.forEach { (t, _) ->xx += t.toString()

                                    }
                                    errorMessage=MISSING_BLOOD_GROUP_DATA
                                }
                                if(!allPlasmaGroupsPresent) errorMessage=MISSING_PLASMA_DATA
                                if(bodies.value.isEmpty()){
                                    errorMessage=INSERT_AT_LEAST_ONE_ITEM_LABEL
                                }
                            }
                        }
                        CustomButton(label= CANCEL_LABEL,
                            enabledBackgroundColor = Color.Red,
                            buttonShape = RectangleShape,
                            buttonShadowElevation = 6) {
                            navHostController.navigate(BloodStockIndexRoute.route)
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun SaveDialog(showDialog:MutableState<Boolean>,controller: BloodBankController,bodies:MutableState<List<DailyBloodStockBody>>){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Label(SAVE_PROMPT)
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = RectangleShape,
                        enabledBackgroundColor = GREEN) {
                        controller.storeMultipleDailyStock(bodies.value)
                        showDialog.value=false
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
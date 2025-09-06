package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.moduleSelectorPages

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.HospitalModuleBody
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.modules.Module
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ModulesResponse
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalModuleSelectorPage(navHostController: NavHostController){

    val context= LocalContext.current
    val hospital=Preferences.Hospitals().get()
    val controller:HospitalController= viewModel()
    val state by controller.singleState.observeAsState()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.moduleOptionsState.observeAsState()
    var modules by remember { mutableStateOf<List<Module>>(emptyList()) }
    var selectedModules by remember { mutableStateOf<List<Module>>(emptyList()) }
    val showSheet = remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    val showNotes = remember { mutableStateOf(true) }
    //val showFields = remember { mutableStateOf(false) }
    val showConfirmatoryButtons= remember { mutableStateOf(true) }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = optionsState as UiState.Success<ModulesResponse>
            val r = s.data
            val data=r.data
            modules=data
            selectedModules=hospital?.modules?:emptyList()

        }
        else->{ settingsController.modulesOptions() }
    }
    LaunchedEffect(Unit) {
        val currentModules= mutableListOf<Module>()
        hospital?.let {
            val hospitalModules=it.modules
            Log.e("HMSP","${hospitalModules.size}")
            currentModules.addAll(hospitalModules)
            selectedModules=currentModules
        }
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                showSheet.value=true
                success=false;fail=true
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:""

            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                showSheet.value=true
                success=true;fail=false
                val s = state as UiState.Success<HospitalSingleResponse>
                val r = s.data
                val data=r.data


            }
        }
        else->{ }
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION) {
        Container(
            title = "Module Selector ${hospital?.id}",
            showSheet = showSheet,
            headerShowBackButton = true,
            headerIconButtonBackground = BLUE,
            headerOnClick = {navHostController.navigate(HomeRoute.route)},
            sheetColor = if(success) GREEN else if(fail) Color.Red else BLUE,
            sheetOnClick = { success=false;fail=false;showSheet.value=false },
            sheetContent = {
                if(success) Label("Data Saved Successfully", color = WHITE)
                if(fail) Label(errorMessage, color = WHITE)
            }
        ) {
            Box(modifier=Modifier.fillMaxWidth().background(BLUE), contentAlignment = Alignment.Center){
                Label("Welcome to Module Selector", color = WHITE, fontSize = 24, fontWeight = FontWeight.Bold)
            }
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                item{
                    VerticalSpacer()
                    if(hospital?.modules?.isEmpty() == true){
                        Box(modifier=Modifier.fillMaxWidth().background(WHITE), contentAlignment = Alignment.TopStart){
                            Label("This is the Second Step,",
                                textAlign = TextAlign.Start,
                                color = BLACK, fontSize = 16,
                                fontWeight = FontWeight.Bold,
                                paddingStart = 5)
                        }

                    }
                  NotesSection(hospital,visible=showNotes)

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
                                    //showFields.value=true
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

                    AnimatedVisibility(
                        visible = !showNotes.value,
                        enter= fadeIn() + expandVertically(),
                        exit = shrinkOut() + fadeOut()) {
                        Column(modifier=Modifier.fillMaxSize()){
                            Column(modifier=Modifier.fillMaxSize()) {
                                modules.forEach { module->
                                    var state by rememberSaveable { mutableStateOf(module in selectedModules) }
                                    CustomCheckbox(
                                        label={
                                        Row(modifier=Modifier.fillMaxWidth()){
                                            Label(module.name?: EMPTY_STRING)
                                        }
                                    },
                                        active=state,
                                        onCheckChange = {value->
                                        if(value){
                                            val newList=mutableListOf<Module>()
                                            newList.addAll(selectedModules.filter { it!=module })
                                            newList.add(module)
                                            selectedModules=newList
                                        }
                                        else {selectedModules=selectedModules.filter { it!=module }}

                                        state= module in selectedModules

                                    })
                                    VerticalSpacer()
                                }

                            }
                            VerticalSpacer()
                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center){
                                CustomButton(label=SAVE_CHANGES_LABEL,
                                    enabledBackgroundColor = Color.Transparent,
                                    disabledBackgroundColor = Color.Transparent,
                                    enabledFontColor = GREEN,
                                    borderColor = GREEN,
                                    hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                                    buttonShadowElevation = 6,
                                    buttonShape = rcs(5),
                                    horizontalPadding = 10,
                                )
                                {
                                    val bodies= mutableListOf<HospitalModuleBody>()
                                    selectedModules.forEach {
                                        val body=HospitalModuleBody(
                                            hospitalId = hospital?.id?:0,
                                            moduleId = it.id
                                        )
                                        bodies.add(body)
                                    }
                                    controller.addModulesToHospital(bodies)
                                }
                            }
                            VerticalSpacer(10)
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun NotesSection(hospital: SimpleHospital?, visible: MutableState<Boolean>) {
    Box(modifier=Modifier.fillMaxWidth().background(WHITE), contentAlignment = Alignment.TopStart){
        Label("Here, you can select what modules you want to add to the hospital,",
            textAlign = TextAlign.Start,
            color = BLACK, fontSize = 14,
            maximumLines = 2,
            fontWeight = FontWeight.Bold,
            paddingStart = 5)
    }
    Row(modifier=Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
        Label("Form Helper notes")
        HorizontalSpacer()
        Box(modifier=Modifier.clickable { visible.value=!visible.value }){
            Span(text= "Click To See/Hide", color=WHITE , backgroundColor = BLUE)
        }

    }
    Box(modifier=Modifier.fillMaxWidth().background(WHITE), contentAlignment = Alignment.TopStart){
        Label("Your Current Hospital is ${hospital?.name?:""}",
            textAlign = TextAlign.Start,
            color = BLACK, fontSize = 14,
            maximumLines = 2,
            fontWeight = FontWeight.Bold,
            paddingStart = 5)
    }

    AnimatedVisibility(visible = visible.value,
        enter= fadeIn() + expandVertically(),
        exit = shrinkOut() + fadeOut(),) {
        Column(){
            LazyRow(modifier=Modifier.fillMaxWidth()){
                item{
                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp).background(WHITE).padding(horizontal = 5.dp),
                        horizontalAlignment = Alignment.Start){
                        Label(label = "Module:",
                            "Is a Sub System to manage certain sector \nin the hospital",
                            textAlign = TextAlign.Start,
                            maximumLines = 2)
                        Label("Each module has a unique name,\n which is in the blue box",
                            maximumLines = 2,
                            textAlign = TextAlign.Start)
                        VerticalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label("The red trash button next to module \nwill remove it from hospital,",
                                maximumLines = 2,
                                textAlign = TextAlign.Start)
                            Icon(icon= R.drawable.ic_delete_red)
                        }
                        VerticalSpacer()
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Label("After selecting the module click on \n the  add button to add it to the hospital",
                                maximumLines = 2,
                                textAlign = TextAlign.Start)
                            Icon(icon= R.drawable.ic_add_circle_green)
                        }
                        Label("when the module is removed from the hospital,\n users will not be able to use it",
                            color = Color.Red,
                            maximumLines = 2,
                            textAlign = TextAlign.Start)
                        VerticalSpacer()
                        Label("You can scroll down/up to view other mandatory fields", color = BLUE, fontWeight = FontWeight.Bold)
                        VerticalSpacer()
                        Label("Once you complete the form,", color = BLUE, fontWeight = FontWeight.Bold)
                        Row(){
                            Label("press the", color = BLUE, fontWeight = FontWeight.Bold)
                            HorizontalSpacer(2)
                            Label("Green Save Changes Button", color = GREEN, fontWeight = FontWeight.Bold)
                        }
                        VerticalSpacer()
                        Label("If data is saved successfully, a small green bar will appear,\nat the bottom of screen confirming changes",
                            color = BLUE, fontWeight = FontWeight.Bold,
                            maximumLines = 2,
                            textAlign = TextAlign.Start)
                        VerticalSpacer()
                    }
                }
            }
        }
    }}
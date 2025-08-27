package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.pages.usersPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.kwdevs.hospitalsdashboard.models.settings.title.TitleResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.controller.AdminHospitalUserController
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.AdminHospitalUsersIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalUsersCreatePage(navHostController: NavHostController){
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.titlesState.observeAsState()
    val hospital=Preferences.Hospitals().get()
    val controller:AdminHospitalUserController= viewModel()
    val state by controller.fullHospitalUserSingleState.observeAsState()
    var item by remember { mutableStateOf<HospitalUser?>(null) }
    val showSheet = remember { mutableStateOf(false) }

    val name = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val nationalId = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(true) }
    var titles by remember { mutableStateOf<List<Title>>(emptyList()) }
    val selectedTitle= remember { mutableStateOf<Title?>(null) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    when(optionsState){
        is UiState.Loading->{}
        is UiState.Error->{

        }
        is UiState.Success->{
            val s = optionsState as UiState.Success<TitleResponse>
            val r = s.data
            val data=r.data
            titles=data
        }
        else->{ settingsController.titlesIndex() }
    }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                success=false;fail=true
                showSheet.value=true
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:""
                errors=exception.errors?: emptyMap()

            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                success=true;fail=false
                showSheet.value=true
                val s = state as UiState.Success<HospitalUserSingleResponse>
                val r = s.data
                val data=r.data
                item=data

            }

        }
        else->{
        }
    }
    Container(
        title = "Create User for Hospital ${hospital?.name?:""}",
        headerIconButtonBackground = BLUE,
        headerShowBackButton = true,
        headerOnClick = {navHostController.navigate(AdminHospitalUsersIndexRoute.route)},
        showSheet = showSheet,
        sheetColor = if(success) GREEN else if(fail) Color.Red else BLUE,
        sheetOnClick ={
            success=false;fail=false
            showSheet.value=false
        },
        sheetContent = {
            Column {
                Label(if(success) "Data Saved Successfully" else errorMessage,color= WHITE)
                if(errors.isNotEmpty()){
                    errors.forEach { (t, errorsList )->
                        errorsList.forEach {
                            Label(it, color = WHITE)
                        }
                    }
                }
            }
        }
    ) {
        CustomInput(name,"Name")
        CustomInput(nationalId,"National Id")
        CustomInput(username,"Username")
        CustomInput(value=password,label="Password", isPassword = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            ComboBox(
                selectedItem = selectedTitle,
                loadedItems = titles,
                selectedContent = { CustomInput(selectedTitle.value?.name?:"Select Title")}
            ) {
                Label(it?.name?:"")
            }
        }
        CustomCheckbox(label="Active",active)
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label= SAVE_CHANGES_LABEL) {
                val nameNotNull=name.value.trim()!=""
                val usernameNotNull=username.value.trim()!=""
                val passwordNotNull=password.value.trim()!=""
                val hospitalNotNull=hospital!=null
                val nationalIdNotNull=nationalId.value.trim()!=""
                val titleIdNotNull=selectedTitle.value!=null
                val allValid=nameNotNull && usernameNotNull && passwordNotNull && hospitalNotNull && nationalIdNotNull && titleIdNotNull
                if(allValid){
                    val body = HospitalUserBody(
                        hospitalId = hospital?.id?:0,
                        name=name.value,
                        username = username.value,
                        password = password.value,
                        nationalId = nationalId.value,
                        active = if(active.value) 1 else 0,
                        titleId = selectedTitle.value?.id?:0
                    )
                    controller.create(body)

                }
            }
        }
    }
}
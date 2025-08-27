package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createUser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserPage(navHostController: NavHostController){
    val controller:UsersController= viewModel()
    val state by controller.hospitalUserSingleState.observeAsState()
    val userType=Preferences.User().getType()
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val hospital=Preferences.Hospitals().get()
    val crudType=Preferences.CrudTypes().get()
    val name = remember { mutableStateOf("") }
    val username= remember { mutableStateOf("") }
    val password= remember { mutableStateOf("") }
    val nationalId = remember { mutableStateOf("") }
    val selectedTitle = remember { mutableStateOf<Title?>(null) }
    val titles by remember { mutableStateOf<List<Title>>(emptyList()) }
    val active = remember { mutableStateOf(true) }
    val showSheet = remember { mutableStateOf(false) }
    val item = remember { mutableStateOf<SimpleHospitalUser?>(null) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                val s = state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:""
                success=false;fail=true
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = state as UiState.Success<HospitalUserSSResponse>
                val r = s.data
                val data=r.data
                item.value=data
                success=true
                fail=false
            }

        }
        else->{}
    }
    Container(
        title = "${if(crudType==CrudType.CREATE) "Create" else "Update"} Hospital User",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            if(userType==ViewType.SUPER_USER){
                navHostController.navigate(HomeRoute.route)
            }else{
                navHostController.navigate(HospitalHomeRoute.route)
            }
        }
    ) {
        VerticalSpacer()
        Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            ComboBox(selectedItem = selectedTitle, loadedItems = titles,
                selectedContent = { CustomInput(selectedTitle.value?.name?:"Select Title")}) {
                Label(it?.name?:"")
            }
        }
        CustomInput(name,"Name")
        CustomInput(nationalId,"National Id")
        CustomInput(username,"Username")
        CustomInput(password,"Password")
        CustomCheckbox("Active",active)
        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            CustomButton(label= SAVE_CHANGES_LABEL) {
                val body = HospitalUserBody(
                    hospitalId = hospital?.id,
                    name = name.value,
                    username = username.value,
                    nationalId = nationalId.value,
                    password = password.value,
                    active = if(active.value) 1 else 0,
                    titleId = selectedTitle.value?.id,
                    createdById = if(crudType==CrudType.CREATE){if(userType==ViewType.HOSPITAL_USER) user?.id else null} else null,
                    updatedById = if(crudType==CrudType.UPDATE){if(userType==ViewType.HOSPITAL_USER) user?.id else null} else null,
                    createdBySuperId = if(crudType==CrudType.CREATE){if(userType==ViewType.SUPER_USER) superUser?.id else null} else null,
                    updatedBySuperId = if(crudType==CrudType.UPDATE){if(userType==ViewType.SUPER_USER) superUser?.id else null} else null,

                    )
                controller.create(body)
            }
        }
    }
}
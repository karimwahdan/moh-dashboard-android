package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.models.control.PermissionData
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.controller.AdminHospitalUserController
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.responses.settings.PermissionDataResponse
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun SimpleHospitalUserCard(item:SimpleHospitalUser,
                           navHostController: NavHostController,controller: AdminHospitalUserController){

    val showDialog = remember { mutableStateOf(false) }
    ColumnContainer(shape = RectangleShape) {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(item.name)
                HorizontalSpacer()
                Span(item.title?.name?:"", backgroundColor = BLUE, color = WHITE)
                HorizontalSpacer()
                Icon(if(item.active) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red)
            }
        }
        val rolesChucked=item.roles.chunked(2)
        rolesChucked.forEach { list->
                list.forEach { role->
                    Row(){ Span(role.name) }
            }
        }
        Row(modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButton(label = "Roles", enabledBackgroundColor = BLUE, buttonShape = RectangleShape) {
                showDialog.value=true
            }
            CustomButton(label = "Edit", enabledBackgroundColor = BLUE, buttonShape = RectangleShape) {

            }
        }
    }
    RolesDialog(showDialog, controller =controller,item )
}

@Composable
private fun RolesDialog(showDialog:MutableState<Boolean>,controller:AdminHospitalUserController,item:SimpleHospitalUser){
    val permissionsController: PermissionsController = viewModel()
    val state by permissionsController.singleUserState.observeAsState()
    val optionsState by permissionsController.state.observeAsState()
    var roles                           by remember { mutableStateOf<List<Role>>(emptyList()) }
    val selectedRole = remember { mutableStateOf<Role?>(null) }
    val selectedRoles = remember { mutableStateOf<List<Role>>(emptyList()) }

    if(showDialog.value){
        when(optionsState){
            is UiState.Loading->{ }
            is UiState.Error->{

            }
            is UiState.Success->{
                val s           = optionsState as UiState.Success<PermissionDataResponse>
                val r           = s.data
                val data        = r.data
                roles           = data.roles
            }
            else->{ permissionsController.index() }
        }
        when(state){
            is UiState.Loading->{ }
            is UiState.Error->{

            }
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    val s           = state as UiState.Success<HospitalUserSSResponse>
                    val r           = s.data
                    val data        = r.data
                    controller.indexByHospital(1)
                    showDialog.value=false

                }

            }
            else->{  }
        }
        LaunchedEffect(Unit) {
            selectedRoles.value=item.roles
        }
        LaunchedEffect(selectedRole.value) {
            val new= mutableListOf<Role>()
            val s=selectedRole.value
            s?.let {
                new.add(it)
                new.addAll(selectedRoles.value.filter { it!=s })
                selectedRole.value=null
                selectedRoles.value=new
            }
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                val chucked=selectedRoles.value.chunked(2)
                Label(item.name)
                chucked.forEach { list->
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        list.forEach {role->
                            Row(modifier=Modifier.padding(5.dp).border(width=1.dp, color = GRAY)){ Label(role.name)
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedRoles.value=selectedRoles.value.filter { it!=role }
                                }
                            }

                        }
                    }
                }
                ComboBox(
                    loadedItems = roles,
                    selectedItem = selectedRole,
                    selectedContent = { CustomInput(selectedRole.value?.name?:"Select Role")}
                ) {
                    Label(it?.name?:"")
                }
                CustomButton(label="Add") {
                    val body=UserRoleBody(
                        userId = item.id,
                        roleIds = selectedRoles.value.map { it.id }
                    )
                    controller.updateRoles(body)
                }
            }
        }
    }


}
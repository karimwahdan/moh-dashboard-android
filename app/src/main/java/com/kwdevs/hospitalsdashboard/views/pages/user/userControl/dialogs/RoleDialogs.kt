package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.RoleBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.models.settings.permissions.PermissionsResponse
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.responses.settings.RoleSingleResponse
import com.kwdevs.hospitalsdashboard.routes.UserControlRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PERMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_PERMISSION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsT

@Composable
fun AddNewRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController){
    val context= LocalContext.current
    val name = remember { mutableStateOf("") }
    val slug = remember { mutableStateOf("") }
    var role by remember { mutableStateOf<Role?>(null) }
    val state by controller.singleRoleState.observeAsState()
    if(showDialog.value){
        LaunchedEffect(state) {
            when (state) {
                is UiState.Success -> {
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Added Successfully")
                    Log.e(UserControlRoute.route,"Add New Dialog: Success")
                    controller.reload()
                    showDialog.value=false
                }
                is UiState.Error -> {
                    toast(context, "Failed to Add New Role")
                    Log.e(UserControlRoute.route,"Add New Dialog: Error")
                    controller.reload()
                    showDialog.value=false
                }
                is UiState.Loading->{
                    Log.e(UserControlRoute.route,"Add New Dialog: Loading")
                }
                else -> {
                    Log.e(UserControlRoute.route,"Add New Dialog: Else")
                }
            }
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label(ADD_NEW_ROLE_LABEL, color = WHITE) }
                }
                VerticalSpacer()
                CustomInput(name, NAME_LABEL)
                VerticalSpacer()
                CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")
                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        enabledFontColor = WHITE,
                        disabledFontColor = GRAY,
                        disabledBackgroundColor = Color.LightGray,
                        enabledBackgroundColor = GREEN,
                        buttonShadowElevation = 5,
                        buttonShape = rcs(5),
                        enabled = name.value!="" && slug.value!="") {
                        val input= RoleBody(
                            name=name.value.trim(),
                            slug = slug.value.trim().replace("\\s+".toRegex(), ".")
                        )
                        controller.storeNewRole(input)
                    }
                    CustomButton(label = CANCEL_LABEL,
                        enabledFontColor = WHITE,
                        buttonShadowElevation = 5,
                        buttonShape = rcs(5),
                        enabledBackgroundColor = Color.Red) {
                        showDialog.value=false
                    }
                }
                VerticalSpacer()
            }
        }

    }
}

@Composable
fun EditRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController,
                           item: Role?){
    val context= LocalContext.current
    val name = remember { mutableStateOf("") }
    val slug = remember { mutableStateOf("") }
    var role by remember { mutableStateOf<Role?>(null) }
    var permissions by remember { mutableStateOf<List<Permission>>(emptyList()) }
    val selectedPermission = remember { mutableStateOf<Permission?>(null) }
    val selectedPermissions = remember { mutableStateOf<List<Permission>>(emptyList()) }
    val permissionsListState by controller.permissionsState.observeAsState()
    val state by controller.singleRoleState.observeAsState()
    if(showDialog.value){
        when(permissionsListState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = permissionsListState as UiState.Success<PermissionsResponse>
                val r = s.data
                val data=r.data
                permissions=data
            }
            else->{controller.permissionsList()}
        }
        LaunchedEffect(Unit) {
            name.value=item?.name?:""
            slug.value=item?.slug?:""
            selectedPermissions.value=role?.permissions?: emptyList()

        }
        LaunchedEffect(state) {
            when (state) {
                is UiState.Success -> {
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Added Successfully")
                    controller.reload()
                    showDialog.value=false
                }
                is UiState.Error -> {
                    toast(context, "Failed to Add New Role")
                    showDialog.value=false
                }
                is UiState.Loading->{
                    Log.e(UserControlRoute.route,"Add New Dialog: Loading")
                }
                else -> {}
            }
        }
        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label(EDIT_LABEL, color = WHITE) }
                }
                VerticalSpacer()
                CustomInput(name, NAME_LABEL)
                VerticalSpacer()
                CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")
                VerticalSpacer()
                Row{
                    Box(modifier= Modifier.fillMaxWidth().padding(5.dp).weight(1f)){
                        ComboBox(title = PERMISSIONS_LABEL,
                            loadedItems = permissions, selectedItem = selectedPermission, selectedContent = {
                                CustomInput(selectedPermission.value?.name?: SELECT_PERMISSION_LABEL)
                            }) {
                            Label(it?.name?:"")
                        }
                    }
                    IconButton(R.drawable.ic_add_circle_green) {
                        val new= mutableListOf<Permission>()
                        selectedPermissions.value.forEach {
                            new.add(it)
                        }
                        selectedPermission.value?.let{new.add(it)}
                        selectedPermissions.value=new
                        selectedPermission.value=null
                    }
                }
                VerticalSpacer()
                LazyRow {
                    items(selectedPermissions.value){
                        Row(modifier= Modifier.padding(5.dp).border(1.dp, BLACK)){
                            Label(it.name?:"")
                            IconButton(R.drawable.ic_delete_red) {
                                selectedPermissions.value=selectedPermissions.value.filter { p->p!=it }
                            }
                        }
                    }
                }
                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        enabledFontColor = WHITE,
                        disabledFontColor = GRAY,
                        disabledBackgroundColor = Color.LightGray,
                        enabledBackgroundColor = GREEN,
                        buttonShadowElevation = 5,
                        buttonShape = rcs(5),
                        enabled = name.value!="" && slug.value!="") {
                        val input= RoleBody(
                            id=item?.id,
                            name=name.value.trim(),
                            slug = slug.value.trim().replace("\\s+".toRegex(), "."),
                            permissions = selectedPermissions.value
                        )
                        controller.updateRole(input)
                    }
                    CustomButton(label = CANCEL_LABEL,
                        enabledFontColor = WHITE,
                        buttonShadowElevation = 5,
                        buttonShape = rcs(5),
                        enabledBackgroundColor = Color.Red) {
                        showDialog.value=false
                    }
                }
                VerticalSpacer()
            }
        }

    }
}



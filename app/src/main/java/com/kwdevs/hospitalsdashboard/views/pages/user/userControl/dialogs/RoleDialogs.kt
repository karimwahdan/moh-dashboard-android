package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.RoleBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.modules.adminModule.controller.HospitalUserPermissionController
import com.kwdevs.hospitalsdashboard.modules.adminModule.controller.SuperUserPermissionController
import com.kwdevs.hospitalsdashboard.responses.settings.RoleSingleResponse
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonLeftIcon
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NORMAL_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SUPER_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsT

@Composable
fun AddNewHospitalUserRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController){
    val context= LocalContext.current
    val permissionsController:HospitalUserPermissionController= viewModel()
    val name = remember { mutableStateOf(EMPTY_STRING) }
    val slug = remember { mutableStateOf(EMPTY_STRING) }
    var role by remember { mutableStateOf<Role?>(null) }
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val state by controller.singleRoleState.observeAsState()
    if(showDialog.value){
        when (state) {
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Updated Successfully")
                    controller.reload()
                    showDialog.value=false
                    Preferences.Roles().delete()
                    permissionsController.reloadSingleRole()
                }

            }
            is UiState.Error -> {
                LaunchedEffect(Unit) {
                    loading=false;success=false;fail=true
                    val s=state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                    toast(context, "Failed to Add New Role")
                }

            }
            is UiState.Loading->{
                LaunchedEffect(Unit) { loading=true;success=false;fail=false}

            }
            else -> {}
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label("$ADD_NEW_ROLE_LABEL $NORMAL_USER_LABEL", color = WHITE) }
                }
                if(!loading && !success && !fail){
                    VerticalSpacer()
                    CustomInput(name, NAME_LABEL)
                    VerticalSpacer()
                    CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")
                    VerticalSpacer()
                    Row(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround){
                        CustomButtonLeftIcon(label = SAVE_CHANGES_LABEL,
                            enabledFontColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            disabledFontColor = GRAY,
                            disabledBackgroundColor = Color.Transparent,
                            enabledBackgroundColor = Color.Transparent,
                            buttonShadowElevation = 5,
                            borderColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            hasBorder = true,
                            icon = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                            buttonShape = rcs(5),
                            enabled = name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING) {
                            val input= RoleBody(
                                name=name.value.trim(),
                                slug = slug.value.trim().replace("\\s+".toRegex(), "."),
                                permissions = null,
                            )
                            controller.storeHospitalUserRole(input)
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
                else {
                    if(loading) LoadingScreen()
                    else if(fail) FailScreen(errors=errors, message = errorMessage)
                }

            }
        }

    }
}

@Composable
fun AddNewSuperRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController){
    val context= LocalContext.current
    val permissionsController:SuperUserPermissionController= viewModel()
    val name = remember { mutableStateOf(EMPTY_STRING) }
    val slug = remember { mutableStateOf(EMPTY_STRING) }
    var role by remember { mutableStateOf<Role?>(null) }
    val state by permissionsController.singleRoleState.observeAsState()
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    if(showDialog.value){
        when (state) {
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Updated Successfully")
                    controller.reload()
                    showDialog.value=false
                    Preferences.Roles().delete()
                    permissionsController.reloadSingleRole()
                }

            }
            is UiState.Error -> {
                LaunchedEffect(Unit) {
                    loading=false;success=false;fail=true
                    val s=state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                    toast(context, "Failed to Add New Role")
                }

            }
            is UiState.Loading->{
                LaunchedEffect(Unit) { loading=true;success=false;fail=false}

            }
            else -> {}
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label("$ADD_NEW_ROLE_LABEL $SUPER_USER_LABEL", color = WHITE) }
                }
                VerticalSpacer()
                CustomInput(name, NAME_LABEL)
                VerticalSpacer()
                CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")
                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    CustomButtonLeftIcon(label = SAVE_CHANGES_LABEL,
                        enabledFontColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                        disabledFontColor = GRAY,
                        disabledBackgroundColor = Color.Transparent,
                        enabledBackgroundColor = Color.Transparent,
                        buttonShadowElevation = 5,
                        borderColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                        hasBorder = true,
                        icon = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                        buttonShape = rcs(5),
                        enabled = name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING) {
                        val input= RoleBody(
                            name=name.value.trim(),
                            slug = slug.value.trim().replace("\\s+".toRegex(), "."),
                            permissions = null,
                        )
                        permissionsController.storeSuperUserRole(input)
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
fun EditHospitalUserRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController,
                               item: Role?){
    val permissionsController:HospitalUserPermissionController= viewModel()
    val context= LocalContext.current
    val name = remember { mutableStateOf(EMPTY_STRING) }
    val slug = remember { mutableStateOf(EMPTY_STRING) }
    var role by remember { mutableStateOf<Role?>(null) }
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    val state by permissionsController.singleRoleState.observeAsState()
    if(showDialog.value){

        LaunchedEffect(Unit) {
            name.value=item?.name?:EMPTY_STRING
            slug.value=item?.slug?:EMPTY_STRING
        }
        when (state) {
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    loading=false;fail=false;success=true
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Update Successfully")
                    controller.reload();permissionsController.reloadSingleRole()
                    showDialog.value=false
                    Preferences.Roles().delete()
                }
            }
            is UiState.Error -> {
                LaunchedEffect(Unit) {
                    val s=state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                    loading=false;success=false;fail=true
                    toast(context, "Failed to Edit Role")
                }
            }
            is UiState.Loading->{loading=true;fail=false;success=false}
            else -> {}
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label("$EDIT_LABEL $ROLE_LABEL $NORMAL_USER_LABEL", color = WHITE) }
                }
                if(!loading && !success && !fail){
                    VerticalSpacer()
                    CustomInput(name, NAME_LABEL)
                    VerticalSpacer()
                    CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")

                    VerticalSpacer()
                    Row(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround){
                        CustomButtonLeftIcon (label = SAVE_CHANGES_LABEL,
                            enabledFontColor = if(item!=null && name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            disabledFontColor = GRAY,
                            disabledBackgroundColor = Color.Transparent,
                            enabledBackgroundColor = Color.Transparent,
                            buttonShadowElevation = 5,
                            borderColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            hasBorder = true,
                            icon = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                            buttonShape = rcs(5),
                            enabled = name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING) {
                            val input= RoleBody(
                                id=item?.id,
                                name=name.value.trim(),
                                slug = slug.value.trim().replace("\\s+".toRegex(), "."),
                            )
                            controller.updateHospitalUserRole(input)
                        }
                        CustomButton(label = CANCEL_LABEL,
                            enabledFontColor = WHITE,
                            buttonShadowElevation = 5,
                            buttonShape = rcs(5),
                            enabledBackgroundColor = Color.Red) {
                            showDialog.value=false
                            Preferences.Roles().delete()
                        }
                    }
                    VerticalSpacer()
                }
                else {
                    if(loading) LoadingScreen()
                    else if(fail) FailScreen(errors=errors, message = errorMessage)
                }

            }
        }

    }
}

@Composable
fun EditSuperRoleDialog(showDialog: MutableState<Boolean>, controller: PermissionsController,
                        item: Role?){
    val context= LocalContext.current
    val permissionsController:SuperUserPermissionController= viewModel()
    val name = remember { mutableStateOf(EMPTY_STRING) }
    val slug = remember { mutableStateOf(EMPTY_STRING) }
    var role by remember { mutableStateOf<Role?>(null) }
    val state by controller.singleRoleState.observeAsState()
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    if(showDialog.value){
        LaunchedEffect(Unit) {
            name.value=item?.name?:EMPTY_STRING
            slug.value=item?.slug?:EMPTY_STRING

        }
        when (state) {
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false
                    val s = state as UiState.Success<RoleSingleResponse>
                    val response=s.data
                    val data = response.data
                    role = data
                    toast(context, "${data.name} Role Updated Successfully")
                    controller.reload()
                    showDialog.value=false
                    Preferences.Roles().delete()
                    permissionsController.reloadSingleRole()
                }

            }
            is UiState.Error -> {
                LaunchedEffect(Unit) {
                    loading=false;success=false;fail=true
                    val s=state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                    toast(context, "Failed to Add New Role")
                }

            }
            is UiState.Loading->{
                LaunchedEffect(Unit) { loading=true;success=false;fail=false}

            }
            else -> {}
        }

        Dialog(onDismissRequest = {
            showDialog.value=false
            Preferences.Roles().delete()
        }) {
            Column(modifier= Modifier.fillMaxWidth()
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))) {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label("$EDIT_LABEL $ROLE_LABEL $SUPER_USER_LABEL", color = WHITE) }
                }
                if(!loading && !success && !fail){
                    VerticalSpacer()
                    CustomInput(name, NAME_LABEL)
                    VerticalSpacer()
                    CustomInput(slug, SLUG_LABEL, hasWhiteSpaces = false, replacedWith = ".")
                    VerticalSpacer()
                    Row(modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround){
                        CustomButtonLeftIcon (label = SAVE_CHANGES_LABEL,
                            enabledFontColor = if(item!=null && name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            disabledFontColor = GRAY,
                            disabledBackgroundColor = Color.Transparent,
                            enabledBackgroundColor = Color.Transparent,
                            buttonShadowElevation = 5,
                            borderColor = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)GREEN else GRAY,
                            hasBorder = true,
                            icon = if(name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                            buttonShape = rcs(5),
                            enabled = name.value!=EMPTY_STRING && slug.value!=EMPTY_STRING) {
                            val input= RoleBody(
                                id=item?.id,
                                name=name.value.trim(),
                                slug = slug.value.trim().replace("\\s+".toRegex(), "."),
                            )
                            controller.updateHospitalUserRole(input)
                        }
                        CustomButton(label = CANCEL_LABEL,
                            enabledFontColor = WHITE,
                            buttonShadowElevation = 5,
                            buttonShape = rcs(5),
                            enabledBackgroundColor = Color.Red) {
                            showDialog.value=false
                            Preferences.Roles().delete()
                        }
                    }
                    VerticalSpacer()
                }
                else {
                    if(loading) LoadingScreen()
                    else if(fail) FailScreen(errors=errors, message = errorMessage)
                }

            }
        }

    }
}



package com.kwdevs.hospitalsdashboard.views.pages.user.userControl

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.CustomModelBody
import com.kwdevs.hospitalsdashboard.bodies.control.RolePermissionsBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.controller.control.TableController
import com.kwdevs.hospitalsdashboard.models.control.PermissionData
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.models.settings.permissions.PermissionsResponse
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.responses.settings.PermissionDataResponse
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACCOUNT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_PERMISSION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BLUE3
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.ROLES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TABLES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TEAL200
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.USER_CONTROL_HEADER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.whiteColor
import com.kwdevs.hospitalsdashboard.views.cards.control.ModelCard
import com.kwdevs.hospitalsdashboard.views.cards.control.RoleCard
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.AddNewPermissionDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.AddNewRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditTableDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewHospitalUserDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewSuperUserDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewTableDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.SuperUserDetailsDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.UserDetailsDialog
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB

@Composable
fun UserControlPage(navHostController: NavHostController){
    val controller      : PermissionsController = viewModel()
    val tableController : TableController = viewModel()

    val tableState                      by tableController.singleState.observeAsState()
    val state                           by controller.state.observeAsState()
    var permissionData                  by remember { mutableStateOf<PermissionData?>(null) }
    var users                           by remember { mutableStateOf<List<SimpleHospitalUser>>(emptyList()) }
    var superUsers                      by remember { mutableStateOf<List<SimpleSuperUser>>(emptyList()) }
    var models                          by remember { mutableStateOf<List<CustomModel>>(emptyList()) }
    var roles                           by remember { mutableStateOf<List<Role>>(emptyList()) }
    var actionTypes                     by remember { mutableStateOf<List<ActionType>>(emptyList()) }

    val context                         =  LocalContext.current
    val userTypes                       =  listOf(Pair(ViewType.SUPER_USER,"Super"),Pair(ViewType.HOSPITAL_USER,"Normal"))
    val selectedType                    =  remember { mutableStateOf(ViewType.HOSPITAL_USER) }
    val selectedRole                    =  remember { mutableStateOf<Role?>(null) }
    val selectedTable                   =  remember { mutableStateOf<CustomModel?>(null) }

    val showUsers                       =  remember { mutableStateOf(false) }
    val showNewRoleDialog               =  remember { mutableStateOf(false) }
    val showNewTableDialog              =  remember { mutableStateOf(false) }
    val showEditRoleDialog              =  remember { mutableStateOf(false) }
    val showEditTableDialog             =  remember { mutableStateOf(false) }
    val showNewPermissionDialog         =  remember { mutableStateOf(false) }
    val showNewHospitalUserDialog       =  remember { mutableStateOf(false) }
    val showNewSuperUserDialog       =  remember { mutableStateOf(false) }

    val showEditRolePermissionDialog    =  remember { mutableStateOf(false) }
    var loading                         by remember { mutableStateOf(true ) }
    var fail                            by remember { mutableStateOf(false) }
    var success                         by remember { mutableStateOf(false) }

    var errors                          by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage                    by remember { mutableStateOf(EMPTY_STRING) }

    when(tableState){
        is UiState.Error->{
            Toast.makeText(context,"Item Not Updated",Toast.LENGTH_SHORT).show()
            tableController.reload()
        }
        is UiState.Success->{
            Toast.makeText(context,"Item Updated Successfully",Toast.LENGTH_SHORT).show()
            tableController.reload()
        }
        else->{}
    }
    when(state){
        is UiState.Loading->{ loading=true;fail=false;success=false }
        is UiState.Error->{
            loading=false;fail=true;success=false
            val s           = state as UiState.Error
            val exception   = s.exception
            errors          = exception.errors?: emptyMap()
            errorMessage    = exception.message?:"Error..."
        }
        is UiState.Success->{
            loading=false;fail=false;success=true
            val s           = state as UiState.Success<PermissionDataResponse>
            val r           = s.data
            val data        = r.data
            permissionData  = data
            users           = data.users
            superUsers      = data.superUsers
            models          = data.models
            roles           = data.roles
            actionTypes     = data.actionTypes
        }
        else->{ controller.index() }
    }

    val tableName = remember { mutableStateOf(EMPTY_STRING) }
    val tableColumns = remember { mutableStateOf<List<String>>(emptyList()) }
    selectedRole.value?.let{
        EditRolePermissionsDialog(showEditRolePermissionDialog,controller,it)
        EditRoleDialog(showEditRoleDialog,controller,selectedRole.value)
    }
    NewTableDialog(showNewTableDialog,tableName,tableColumns) {
        if(tableName.value!=EMPTY_STRING && tableColumns.value.isNotEmpty()){
            val body= CustomModelBody(
                name=tableName.value,
                columns = tableColumns.value
            )
            tableController.store(body)
            showNewTableDialog.value=false
        }
    }
    EditTableDialog(showEditTableDialog,selectedTable,tableName,tableColumns) {
        if(tableName.value!=EMPTY_STRING && tableColumns.value.isNotEmpty()){
            val body= CustomModelBody(
                id=selectedTable.value?.id,
                name=tableName.value,
                columns = tableColumns.value
            )
            tableController.edit(body)
            showEditTableDialog.value=false
        }
    }

    AddNewRoleDialog(showNewRoleDialog,controller)
    AddNewPermissionDialog(showNewPermissionDialog,controller,models=models,actionTypes=actionTypes,roles=roles)
    NewHospitalUserDialog(showNewHospitalUserDialog,controller)
    NewSuperUserDialog(showDialog=showNewSuperUserDialog,permissionsController=controller)
    Column(modifier=Modifier.fillMaxWidth()){
        TopHeader(navHostController,controller)
        VerticalSpacer()
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        if(fail) FailScreen(modifier = Modifier.fillMaxSize(),label = errorMessage)
        if(success){
            PermissionsButton(showNewPermissionDialog)
            HorizontalDivider()
            LazyColumn{
                item{
                    Label(ACCOUNT_TYPE_LABEL)
                    UsersToggler(
                        showUsers=showUsers,
                        showNewHospitalUserDialog=showNewHospitalUserDialog,
                        showNewSuperUserDialog=showNewSuperUserDialog,
                        userTypes=userTypes,selectedType=selectedType)
                    AnimatedVisibility(visible = showUsers.value) {
                        Column {
                            Label(selectedType.value.name)
                            if(selectedType.value==ViewType.HOSPITAL_USER) HospitalUsersList(users=users,roles=roles,controller=controller)
                            else SuperUsersList(superUsers=superUsers,roles=roles,controller=controller)
                        }
                    }
                    VerticalSpacer()
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Label(TABLES_LABEL)
                        IconButton(R.drawable.ic_add_circle_green) {
                            showNewTableDialog.value=true
                        }
                    }
                    ModelsSection(showEditTableDialog,models,selectedTable)
                    VerticalSpacer()
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Label(ROLES_LABEL)
                        HorizontalSpacer()
                        IconButton(R.drawable.ic_add_circle_green, paddingTop = 5, paddingBottom = 5) {showNewRoleDialog.value=true }
                    }
                    RolesSection(roles,showEditRoleDialog,showEditRolePermissionDialog,selectedRole)
                }
            }
        }
    }
}

@Composable
private fun HospitalUsersList(users: List<SimpleHospitalUser>,roles: List<Role>,controller: PermissionsController) {
    val showDialog= remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<SimpleHospitalUser?>(null) }

    users.forEach { user->
        Row(modifier = Modifier.clickable { selected=user;showDialog.value=true },verticalAlignment = Alignment.CenterVertically){
            Label(user.name)
            HorizontalSpacer()
            Span(text=user.hospital.name?:EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
        }
        VerticalSpacer()
    }
    UserDetailsDialog(showDialog=showDialog, user = selected, loadedRoles = roles, controller = controller)
}

@Composable
private fun SuperUsersList(superUsers: List<SimpleSuperUser>,roles: List<Role>,controller: PermissionsController) {
    val showDialog= remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<SimpleSuperUser?>(null) }
    superUsers.forEach { user->
        Row(modifier = Modifier.clickable { selected=user;showDialog.value=true },verticalAlignment = Alignment.CenterVertically){
            Label(user.name)
            HorizontalSpacer()
            Span(user.title?.name?: EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
        }
        VerticalSpacer()
    }
    SuperUserDetailsDialog(showDialog,selected,roles, controller =controller )

}


@Composable
private fun ModelsSection(
    showEditTableDialog: MutableState<Boolean>,
    models: List<CustomModel>,
    selectedTable: MutableState<CustomModel?>
) {
    models.forEach { model-> ModelCard(model){
        selectedTable.value=model
        showEditTableDialog.value=true}
    }
}

@Composable
private fun RolesSection(
    roles: List<Role>,
    showEditRoleDialog: MutableState<Boolean>,
    showEditRolePermissionDialog: MutableState<Boolean>,
    selectedRole: MutableState<Role?>
) {
    roles.forEach { role-> RoleCard(role,
        onEditClick={
            selectedRole.value=role
            Preferences.Roles().set(role)
            showEditRoleDialog.value=true },
        onPermissionsClick = {
            Preferences.Roles().set(role)
            selectedRole.value=role
            showEditRolePermissionDialog.value=true

        })
    }
}

@Composable
fun UsersToggler(
    showUsers: MutableState<Boolean>,
    showNewHospitalUserDialog: MutableState<Boolean>,
    showNewSuperUserDialog:MutableState<Boolean>,
    userTypes: List<Pair<ViewType, String>>,
    selectedType: MutableState<ViewType>
) {
    Row(modifier=Modifier.fillMaxWidth()
        .background(color= BLUE3),
        horizontalArrangement = Arrangement.SpaceAround){
        userTypes.forEach { type->
            val fg= whiteColor()
            val bg=if(type.first==ViewType.SUPER_USER) TEAL700 else BLUE
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(modifier=Modifier.padding(vertical = 5.dp)
                    .clickable { selectedType.value=type.first;showUsers.value=!showUsers.value }){
                    Span(text = type.second, color = fg, backgroundColor =bg )
                }
                HorizontalSpacer()
                IconButton(R.drawable.ic_add_circle_green) {
                    if(type.first==ViewType.HOSPITAL_USER) showNewHospitalUserDialog.value=true
                    else showNewSuperUserDialog.value=true
                }
            }
        }
    }

}

@Composable
private fun PermissionsButton(showNewPermissionDialog: MutableState<Boolean>) {
    Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround){
        Box(modifier=Modifier.weight(1f).clip(rcs(20)).clickable { showNewPermissionDialog.value=true }){
            ColumnContainer {
                VerticalSpacer()
                Icon(R.drawable.ic_add_circle_white, containerSize = 26, background = GREEN)
                Label(ADD_NEW_PERMISSION_LABEL, softWrap = true, textOverflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun TopHeader(navHostController: NavHostController, controller: PermissionsController) {
    Column(modifier=Modifier.background(color = TEAL700, shape = rcsB(40))) {
        VerticalSpacer()
        Box(modifier=Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
            Header(USER_CONTROL_HEADER_LABEL, fontSize = 24, fontWeight = FontWeight.Bold, color = Color.White)
            Row (modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(icon = R.drawable.ic_arrow_back_white,
                    containerSize = 30,
                    paddingStart = 5,
                    paddingEnd = 5,
                    background = TEAL200) {
                    navHostController.navigate(HomeRoute.route)
                }
                IconButton(icon = R.drawable.ic_reload_white,
                    containerSize = 30,
                    paddingStart = 5,
                    paddingEnd = 5,
                    background = TEAL200) { controller.index() }
            }
        }
        VerticalSpacer()
    }
}

@Composable
fun EditRolePermissionsDialog(showDialog:MutableState<Boolean>,
                              controller: PermissionsController,
                              item:Role){
    if(showDialog.value){
        val state by controller.singleRoleState.observeAsState()
        val options by controller.permissionsState.observeAsState()
        var permissionsList by remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermissions = remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermission = remember { mutableStateOf<Permission?>(null) }
        LaunchedEffect(Unit) {
            selectedPermissions.value=item.permissions
        }
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {showDialog.value=false
                controller.index()
                }
            }
            else->{}
        }
        when(options){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = options as UiState.Success<PermissionsResponse>
                val r = s.data
                val data=r.data
                permissionsList=data
            }
            else->{ controller.permissionsList() }
        }
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Box(modifier= Modifier.fillMaxWidth().padding(5.dp).weight(1f)){
                        ComboBox(hasTitle = false, loadedItems = permissionsList,
                            selectedItem = selectedPermission,
                            selectedContent = {
                                CustomInput(value=selectedPermission.value?.name?:EMPTY_STRING,label="Permission", readOnly = true)
                            }) { Label(it?.name?:EMPTY_STRING) }
                    }
                    IconButton(R.drawable.ic_add_circle_green) {
                        val new= mutableListOf<Permission>()
                        selectedPermission.value?.let {new.add(it)}
                        selectedPermissions.value.forEach { p-> if(new.find { n->n==p }==null) new.add(p) }
                        selectedPermissions.value=new
                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 0..2){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }
                        }
                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 3..5){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }

                        }

                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 6..8){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }

                        }

                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 9..11){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }

                        }

                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 12..14){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }

                        }

                    }
                }
                LazyRow {
                    itemsIndexed(selectedPermissions.value){index,p->
                        if(index in 15..17){
                            Row(modifier=Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically){
                                Label(p.name?:EMPTY_STRING)
                                HorizontalSpacer()
                                IconButton(R.drawable.ic_delete_red) {
                                    selectedPermissions.value=selectedPermissions.value.filter { it!=p }
                                }
                            }
                        }
                    }
                }
                CustomButton(label = SAVE_CHANGES_LABEL) {
                    val permissionIds=selectedPermissions.value.map { it.id?:0 }
                    val body=RolePermissionsBody(
                        id=item.id,
                        permissions = permissionIds
                    )
                    controller.editRolePermissions(body)
                }
            }
        }
    }
}




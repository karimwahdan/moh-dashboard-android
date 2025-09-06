package com.kwdevs.hospitalsdashboard.views.pages.user.userControl

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.getCrudPrefix
import com.kwdevs.hospitalsdashboard.app.getModulePrefix
import com.kwdevs.hospitalsdashboard.app.removeCrudPrefix
import com.kwdevs.hospitalsdashboard.app.removeModulePrefix
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.CustomModelBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.controller.control.TableController
import com.kwdevs.hospitalsdashboard.models.control.PermissionData
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.responses.settings.PermissionDataResponse
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NORMAL_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PERMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SUPER_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TEAL200
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.USER_CONTROL_HEADER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.cards.control.RoleCard
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.AddNewHospitalUserRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.AddNewSuperRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditHospitalUserRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditHospitalUserRolePermissionsDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditSuperRoleDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditSuperUserRolePermissionsDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.EditTableDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.HospitalUserPermissionDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewHospitalUserDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewSuperUserDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.NewTableDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.SuperUserDetailsDialog
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs.SuperUserPermissionDialog
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
    var superRoles                      by remember { mutableStateOf<List<Role>>(emptyList()) }

    var permissions                     by remember { mutableStateOf<List<Permission>>(emptyList()) }
    var superPermissions                by remember { mutableStateOf<List<Permission>>(emptyList()) }
    var actionTypes                     by remember { mutableStateOf<List<ActionType>>(emptyList()) }

    val context                         =  LocalContext.current
   val selectedTable                   =  remember { mutableStateOf<CustomModel?>(null) }

    val showNewTableDialog              =  remember { mutableStateOf(false) }

    val showEditTableDialog             =  remember { mutableStateOf(false) }


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
            superRoles      = data.superRoles
            actionTypes     = data.actionTypes
            permissions     = data.permissions
            superPermissions= data.superPermissions
        }
        else->{ controller.index() }
    }

    val tableName = remember { mutableStateOf(EMPTY_STRING) }
    val tableColumns = remember { mutableStateOf<List<String>>(emptyList()) }


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
    Column(modifier=Modifier.fillMaxWidth()){
        TopHeader(navHostController,controller)
        VerticalSpacer()
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        if(fail) FailScreen(modifier = Modifier.fillMaxSize(),message = errorMessage,errors=errors)
        if(success){
            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){

                HorizontalSpacer()

            }
            HorizontalDivider()
            LazyColumn{
                item{
                    //VerticalSpacer();Row(verticalAlignment = Alignment.CenterVertically){Label(TABLES_LABEL);IconButton(R.drawable.ic_add_circle_green) {showNewTableDialog.value=true}};ModelsSection(showEditTableDialog,models,selectedTable)
                    HospitalUsersSection(users=users,roles=roles,controller=controller)
                    VerticalSpacer()
                    SuperUsersSection(users=superUsers,roles=superRoles,controller=controller)
                    VerticalSpacer()
                    HospitalUserPermissionsSection(permissions,controller)
                    VerticalSpacer()
                    SuperUserPermissionsSection(permissions=superPermissions,controller)
                    VerticalSpacer()
                    HospitalUserRolesSubSection(roles=roles,controller=controller)
                    VerticalSpacer()
                    SuperUserRolesSubSection(roles=superRoles,controller=controller)
                }
            }
        }
    }
}

@Composable
private fun HospitalUserRolesSubSection(roles: List<Role>, controller: PermissionsController) {
    var show                by remember { mutableStateOf(false) }

    val selectedRole                =  remember { mutableStateOf<Role?>(null) }
    val showNewDialog               =  remember { mutableStateOf(false) }
    val showEditDialog              =  remember { mutableStateOf(false) }
    val showEditPermissionDialog    = remember { mutableStateOf(false) }

    selectedRole.value?.let{
        EditHospitalUserRolePermissionsDialog(showEditPermissionDialog,controller,it)
        EditHospitalUserRoleDialog(showEditDialog,controller,selectedRole.value)
    }

    AddNewHospitalUserRoleDialog(showDialog=showNewDialog,controller)
    Row(modifier=Modifier.clickable { show = !show }.fillMaxWidth().background(ORANGE).padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label("$ROLES_LABEL $NORMAL_USER_LABEL", color = WHITE)
        Icon(if(show) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, background = ORANGE)
    }
    AnimatedVisibility(visible =show ,
        enter = fadeIn()+ expandVertically(),
        exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            VerticalSpacer()
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                buttonShape = RectangleShape,
                buttonShadowElevation = 6,
                enabledBackgroundColor = GREEN,
                label= ADD_NEW_LABEL) {showNewDialog.value=true}
            VerticalSpacer()
            HospitalUserRolesSection(roles,showEditDialog,showEditPermissionDialog,selectedRole)
        }
    }
}

@Composable
private fun SuperUserRolesSubSection(roles: List<Role>,controller: PermissionsController) {
    var show                by remember { mutableStateOf(false) }

    val selectedRole                =  remember { mutableStateOf<Role?>(null) }
    val showNewDialog               =  remember { mutableStateOf(false) }
    val showEditDialog              =  remember { mutableStateOf(false) }
    val showEditPermissionDialog    = remember { mutableStateOf(false) }

    selectedRole.value?.let{
        EditSuperRoleDialog(showEditDialog,controller,it)
        EditSuperUserRolePermissionsDialog(showEditPermissionDialog,controller,it)
    }

    AddNewSuperRoleDialog(showDialog=showNewDialog,controller)
    Row(modifier=Modifier.clickable { show = !show }.fillMaxWidth().background(ORANGE).padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label("$ROLES_LABEL $SUPER_USER_LABEL", color = WHITE)
        Icon(if(show) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, background = ORANGE)
    }
    AnimatedVisibility(visible =show ,
    enter = fadeIn()+ expandVertically(),
    exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            VerticalSpacer()
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                buttonShape = RectangleShape,
                buttonShadowElevation = 6,
                enabledBackgroundColor = GREEN,
                label= ADD_NEW_LABEL) {showNewDialog.value=true}
            VerticalSpacer()
            SuperUserRolesSection(roles,showEditDialog,showEditPermissionDialog,selectedRole)
        }
    }
}

@Composable
private fun SuperUserPermissionsSection(
    permissions: List<Permission>,
    controller: PermissionsController) {
    var show                        by remember { mutableStateOf(false) }
    var crudType                    by remember { mutableStateOf<CrudType?>(null) }
    var currentPermission           by remember { mutableStateOf<Permission?>(null) }
    val showPermissionDialog        =  remember { mutableStateOf(false) }
    crudType?.let {
        SuperUserPermissionDialog(showDialog=showPermissionDialog,oldItem = currentPermission,crudType=it,controller=controller)
    }
    Row(modifier=Modifier.fillMaxWidth().background(color= ORANGE)
        .clickable { show=!show }
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label(text="$PERMISSIONS_LABEL $SUPER_USER_LABEL", color = WHITE)
        Icon(icon=if(show)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white,
            background = ORANGE)
    }
    AnimatedVisibility(visible = show,
        enter = fadeIn()+ expandVertically(),
        exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth()){
            VerticalSpacer()
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                buttonShape = RectangleShape,
                buttonShadowElevation = 6,
                enabledBackgroundColor = BLUE,
                label= ADD_NEW_LABEL) {
                crudType=CrudType.CREATE
                currentPermission=null
                showPermissionDialog.value=true
            }
            VerticalSpacer()
            permissions.sortedBy { it.slug }.forEach {
                val slug=it.slug?: EMPTY_STRING
                val slugBackgroundColor=when{
                    slug.startsWith("browse:")-> BLUE
                    slug.startsWith("create:")-> GREEN
                    slug.startsWith("update:")-> ORANGE
                    slug.startsWith("view:")-> colorResource(R.color.teal_700)
                    slug.startsWith("delete:")-> Color.Red
                    slug.startsWith("read:")-> colorResource(R.color.pink)
                    else-> BLACK
                }
                val moduleSlugBackgroundColor=when(slug.removeCrudPrefix().getModulePrefix()){
                    "inner_module"-> ORANGE
                    "sub_module"-> GREEN
                    "main_module"-> colorResource(R.color.pink)
                    "scope"->colorResource(R.color.teal_700)
                    else-> BLACK
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp).background(color= WHITE)
                    .border(width=1.dp,color= Color.LightGray, shape = rcs(5))
                    .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Column(modifier=Modifier.fillMaxWidth().weight(1f)){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Span(slug.getCrudPrefix(), backgroundColor = slugBackgroundColor,
                                maximumLines = Int.MAX_VALUE,
                                color = WHITE)
                            HorizontalSpacer()
                            Box(modifier=Modifier.weight(1f), contentAlignment = Alignment.CenterStart){
                                Label(it.name?: EMPTY_STRING, maximumLines = Int.MAX_VALUE)
                            }
                            HorizontalSpacer()
                            Span(slug.removeCrudPrefix().getModulePrefix(), backgroundColor = moduleSlugBackgroundColor,
                                maximumLines = Int.MAX_VALUE,
                                color = WHITE)
                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){}
                        }
                        VerticalSpacer()
                        Span(slug.removeCrudPrefix().removeModulePrefix(), backgroundColor = slugBackgroundColor,
                            maximumLines = Int.MAX_VALUE,
                            color = WHITE)
                    }
                    IconButton(R.drawable.ic_edit_blue) {
                        crudType=CrudType.UPDATE
                        currentPermission=it
                        showPermissionDialog.value=true
                    }
                }
            }
        }
    }
}

@Composable
private fun HospitalUserPermissionsSection(
    permissions: List<Permission>,
    controller: PermissionsController){
    var show                        by remember { mutableStateOf(false) }
    var crudType                    by remember { mutableStateOf<CrudType?>(null) }
    var currentPermission           by remember { mutableStateOf<Permission?>(null) }
    val showPermissionDialog        =  remember { mutableStateOf(false) }
    crudType?.let {
        HospitalUserPermissionDialog(showDialog=showPermissionDialog, oldItem = currentPermission, crudType = it,controller=controller)
    }
    Row(modifier=Modifier.fillMaxWidth().background(color= ORANGE)
        .clickable { show=!show }
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label(text="$PERMISSIONS_LABEL $NORMAL_USER_LABEL", color = WHITE)
        Icon(icon=if(show)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white,
            background = ORANGE)
    }
    AnimatedVisibility(visible = show,
        enter = fadeIn()+ expandVertically(),
        exit = fadeOut()+ shrinkVertically()) {
        Column(modifier=Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            VerticalSpacer()
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                buttonShape = RectangleShape,
                buttonShadowElevation = 6,
                enabledBackgroundColor = BLUE,
                label= ADD_NEW_LABEL) {
                crudType=CrudType.CREATE
                currentPermission=null
                showPermissionDialog.value=true
            }

            permissions.sortedBy { it.slug }.forEach {
                val slug=it.slug?: EMPTY_STRING
                val slugBackgroundColor=when{
                    slug.startsWith("browse:")-> BLUE
                    slug.startsWith("create:")-> GREEN
                    slug.startsWith("update:")-> ORANGE
                    slug.startsWith("view:")-> colorResource(R.color.teal_700)
                    slug.startsWith("delete:")-> Color.Red
                    slug.startsWith("read:")-> colorResource(R.color.pink)
                    else-> BLACK
                }
                val moduleSlugBackgroundColor=when(slug.removeCrudPrefix().getModulePrefix()){
                    "inner_module"-> ORANGE
                    "sub_module"-> GREEN
                    "main_module"-> colorResource(R.color.pink)
                    "scope"->colorResource(R.color.teal_700)

                    else-> BLACK
                }
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp).background(color= WHITE)
                    .border(width=1.dp,color= Color.LightGray, shape = rcs(5))
                    .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Column(modifier=Modifier.fillMaxWidth().weight(1f)){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Span(slug.getCrudPrefix(), backgroundColor = slugBackgroundColor,
                                maximumLines = Int.MAX_VALUE,
                                color = WHITE)
                            HorizontalSpacer()
                            Box(modifier=Modifier.weight(1f), contentAlignment = Alignment.CenterStart){
                                Label(it.name?: EMPTY_STRING, maximumLines = Int.MAX_VALUE)
                            }
                            HorizontalSpacer()
                            Span(slug.removeCrudPrefix().getModulePrefix(), backgroundColor = moduleSlugBackgroundColor,
                                maximumLines = Int.MAX_VALUE,
                                color = WHITE)
                            Box(modifier=Modifier.fillMaxWidth().weight(1f)){}
                        }
                        VerticalSpacer()
                        Span(slug.removeCrudPrefix().removeModulePrefix(), backgroundColor = slugBackgroundColor,
                            maximumLines = Int.MAX_VALUE,
                            color = WHITE)
                    }
                    IconButton(R.drawable.ic_edit_blue) {
                        crudType=CrudType.UPDATE
                        currentPermission=it
                        showPermissionDialog.value=true
                    }
                }
            }
        }
    }
}

@Composable
private fun SuperUsersSection(
    users: List<SimpleSuperUser>,
    roles: List<Role>,
    controller: PermissionsController
) {
    var show                    by remember { mutableStateOf(false) }
    val showNewSuperUserDialog  =  remember { mutableStateOf(false) }

    NewSuperUserDialog(showDialog=showNewSuperUserDialog,permissionsController=controller)

    Row(modifier=Modifier
        .clickable { show=!show }
        .fillMaxWidth().background(color= ORANGE)
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label(SUPER_USER_LABEL, color = WHITE)
        Icon(if(show)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white,
            background = ORANGE)
    }
    AnimatedVisibility(visible = show) {
        Column {
            VerticalSpacer()
            CustomButton(
                label= ADD_NEW_LABEL,
                enabledBackgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent,
                enabledFontColor = GREEN,
                borderColor = GREEN,
                hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                buttonShadowElevation = 6,
                buttonShape = rcs(5),
                horizontalPadding = 10,
            ) {showNewSuperUserDialog.value=true}
            VerticalSpacer()
            SuperUsersList(superUsers=users,roles=roles,controller=controller)
        }
    }
}

@Composable
private fun HospitalUsersSection(
    users: List<SimpleHospitalUser>,
    roles: List<Role>,
    controller: PermissionsController
) {
    var show                        by remember { mutableStateOf(false) }
    val showNewDialog   =  remember { mutableStateOf(false) }
    Row(modifier=Modifier
        .clickable { show=!show }
        .fillMaxWidth().background(color= ORANGE)
        .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Label(NORMAL_USER_LABEL, color = WHITE)
        Icon(if(show)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white,
            background = ORANGE)
    }
    NewHospitalUserDialog(showNewDialog,controller)

    AnimatedVisibility(visible = show) {
        Column {
            VerticalSpacer()
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                buttonShape = RectangleShape,
                buttonShadowElevation = 6,
                enabledBackgroundColor = BLUE,
                label= ADD_NEW_LABEL) {showNewDialog.value=true}
            VerticalSpacer()
            HospitalUsersList(users=users,roles=roles,controller=controller)
        }
    }
}

@Composable
private fun HospitalUsersList(users: List<SimpleHospitalUser>,roles: List<Role>,controller: PermissionsController) {
    val showDialog= remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<SimpleHospitalUser?>(null) }

    users.forEach { user->
        Row(modifier = Modifier.fillMaxWidth().border(width = 1.dp, shape = rcs(5), color = GRAY)
            .background(Color.Transparent).padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(user.name)
                HorizontalSpacer()
                Span(text=user.hospital.name?:EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
            }
            IconButton(R.drawable.ic_eye_blue, background = WHITE) { selected=user;showDialog.value=true }
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
        Row(modifier = Modifier.fillMaxWidth().border(width = 1.dp, shape = rcs(5), color = GRAY)
            .background(Color.Transparent).padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(user.name)
                HorizontalSpacer()
                Span(user.title?.name?: EMPTY_STRING, backgroundColor = BLUE, color = WHITE)
            }
            IconButton(R.drawable.ic_eye_blue, background = WHITE) { selected=user;showDialog.value=true }
        }
        VerticalSpacer()
    }
    SuperUserDetailsDialog(showDialog,selected,roles, controller =controller )

}

@Composable
private fun HospitalUserRolesSection(
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
private fun SuperUserRolesSection(
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
private fun TopHeader(navHostController: NavHostController, controller: PermissionsController) {
    Column(modifier=Modifier.background(color = TEAL700, shape = rcsB(40))) {
        VerticalSpacer()
        Box(modifier=Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
            Header(USER_CONTROL_HEADER_LABEL, fontSize = 16, fontWeight = FontWeight.Bold, color = Color.White)
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


//@Composable
//private fun ModelsSection(showEditTableDialog: MutableState<Boolean>,models: List<CustomModel>,selectedTable: MutableState<CustomModel?>) {models.forEach { model-> ModelCard(model){selectedTable.value=model;showEditTableDialog.value=true}}}





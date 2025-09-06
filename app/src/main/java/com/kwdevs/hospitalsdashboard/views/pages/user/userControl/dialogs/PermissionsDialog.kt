package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.getCrudPrefix
import com.kwdevs.hospitalsdashboard.app.getModulePrefix
import com.kwdevs.hospitalsdashboard.app.removeCrudPrefix
import com.kwdevs.hospitalsdashboard.app.removeModulePrefix
import com.kwdevs.hospitalsdashboard.app.replaceSpaceWithDot
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.app.returnAction
import com.kwdevs.hospitalsdashboard.bodies.control.ConditionBody
import com.kwdevs.hospitalsdashboard.bodies.control.PermissionBody
import com.kwdevs.hospitalsdashboard.bodies.control.RolePermissionsBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.permissions.Permission
import com.kwdevs.hospitalsdashboard.models.settings.permissions.PermissionsResponse
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.modules.adminModule.controller.HospitalUserPermissionController
import com.kwdevs.hospitalsdashboard.modules.adminModule.controller.SuperUserPermissionController
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.Modular
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.actionList
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.scopeList
import com.kwdevs.hospitalsdashboard.views.assets.ACTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ACTION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BROWSE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CONDITIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CREATE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonLeftIcon
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DELETE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_ACTION_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_CREATING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_NAME_FILED_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SCOPE_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SLUG_FIELD_HAS_SPACE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SLUG_FIELD_IS_INVALID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SLUG_FIELD_REQUIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NORMAL_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.PERMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PINK
import com.kwdevs.hospitalsdashboard.views.assets.READ_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RESTORE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ACTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_FILED_IS_VALID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPACE
import com.kwdevs.hospitalsdashboard.views.assets.SUPER_USER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.UPDATE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VIEW_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun SuperUserPermissionDialog(showDialog: MutableState<Boolean>,
                              oldItem: Permission?,
                              crudType: CrudType,
                              controller: PermissionsController){

    val permissionController:SuperUserPermissionController= viewModel()

    val selectedAction  =  remember { mutableStateOf<Triple<CRUD,String, String>?>(null) }
    val selectedScope   =  remember { mutableStateOf<Triple<Modular,String,String>?>(null) }

    val name            =  remember { mutableStateOf(EMPTY_STRING) }
    val slug            =  remember { mutableStateOf(EMPTY_STRING) }
    val state           by permissionController.singlePermissionState.observeAsState()
    var loading         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    var errorMessage    by remember { mutableStateOf(EMPTY_STRING) }
    var errors          by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var validName       by remember { mutableStateOf(false) }
    var validSlug       by remember { mutableStateOf(false) }
    var validAction     by remember { mutableStateOf(false) }
    var slugHasScope    by remember { mutableStateOf(false) }
    var validScope      by remember { mutableStateOf(false) }
    if(showDialog.value){
        when(state){
            is UiState.Loading->{LaunchedEffect(Unit) {loading=true;success=false;fail=false }}
            is UiState.Error->{
                LaunchedEffect(Unit) {
                    loading=false;success=false;fail=true
                    val s= state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                }
            }
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false;showDialog.value=false;controller.reload()
                    permissionController.reloadSinglePermission()
                    name.value= EMPTY_STRING;slug.value= EMPTY_STRING
                }
            }
            else->{loading=false;success=false;fail=false}
        }
        LaunchedEffect(name.value) {validName=name.value!= EMPTY_STRING}
        LaunchedEffect(selectedAction.value) {validAction=selectedAction.value!=null}
        LaunchedEffect(selectedScope.value) {validScope=selectedScope.value!=null}
        LaunchedEffect(slug.value) {
            slugHasScope=scopeList.filter { slug.value.startsWith(it.third) }.maxByOrNull { it.third.length }!=null && selectedScope.value!=null
            validSlug=slug.value!= EMPTY_STRING && !slug.value.contains(SPACE) && !slugHasScope
        }

        LaunchedEffect(Unit) {
            if(crudType==CrudType.UPDATE && oldItem!=null){
                val oldName=oldItem.name
                val oldSlug=oldItem.slug

                oldName?.let {name.value= it}
                oldSlug?.let {
                    val matchedAction = actionList.firstOrNull { action-> oldSlug.startsWith(action.third, ignoreCase = true) }
                    val scope=scopeList.firstOrNull {scope-> oldSlug.removeCrudPrefix().startsWith(scope.third,ignoreCase = true) }
                    selectedAction.value = actionList.firstOrNull { it== matchedAction }
                    selectedScope.value= scope
                    slug.value=oldSlug.removeCrudPrefix().removeModulePrefix()
                }

            }
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            if(!loading && !fail && !success){
                ColumnContainer {
                    Row(modifier=Modifier.fillMaxWidth().background(BLUE), horizontalArrangement = Arrangement.Center){
                        Label("$PERMISSIONS_LABEL $SUPER_USER_LABEL",color=WHITE, paddingTop = 5, paddingBottom = 5)
                    }
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(title = ACTION_LABEL,hasTitle = true,selectedItem = selectedAction,loadedItems = actionList,
                            selectedContent = {CustomInput(selectedAction.value?.second?:SELECT_ACTION_LABEL)},
                            itemContent = { Label(it?.second?: EMPTY_STRING)})
                    }
                    if(selectedAction.value==null){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Label(text = "*$ERROR_ACTION_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(
                            title = "Scopes",
                            hasTitle = true,
                            selectedItem = selectedScope,
                            loadedItems = scopeList,
                            selectedContent = {
                                CustomInput(selectedScope.value?.second?:EMPTY_STRING)
                            }
                        ) { Label(it?.second?:EMPTY_STRING)}
                    }
                    if(selectedScope.value==null){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Label(text = "*$ERROR_SCOPE_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }
                    CustomInput(name, NAME_LABEL, maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onTextChange = {name.value=it})
                    if(name.value.trim()== EMPTY_STRING){
                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                            Label(text = "*$ERROR_NAME_FILED_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }

                    CustomInput(value=slug, SLUG_LABEL,maxLines = 2,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onTextChange = {t->slug.value=t.replaceSpaceWithDot().toLowerCase(Locale("en")).removeCrudPrefix().removeModulePrefix()})

                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                        when{
                            slug.value.trim()== EMPTY_STRING->{Label(text = "*$ERROR_SLUG_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)}
                            slug.value.contains(SPACE)->{Label(text = "*$ERROR_SLUG_FIELD_HAS_SPACE_LABEL", color = Color.Red, fontSize = 10)}
                            slugHasScope->{Label(text = "*$ERROR_SCOPE_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)}
                            else->{Label(text = SLUG_FILED_IS_VALID_LABEL, color = GREEN, fontSize = 10)}
                        }
                    }

                    VerticalSpacer()
                    selectedAction.value?.let{Label("${returnAction(it.first)}${ slug.value.removeCrudPrefix()}", maximumLines = 3)}
                    VerticalSpacer()
                    CustomButtonLeftIcon(label= SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = rcs(15),
                        enabled = (validName && validSlug  && validAction && validScope),
                        enabledBackgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        disabledFontColor = GRAY,
                        enabledFontColor = GREEN,
                        icon = if(validName && validSlug  && validAction && validScope)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                        horizontalPadding = 10,
                        hasBorder = true,
                        borderColor = if(validName && validSlug  && validAction && validScope) GREEN else GRAY) {
                        val currentName=name.value
                        val currentSlug=slug.value
                        val currentAction=selectedAction.value
                        val currentScope=selectedScope.value
                        val validData=(validName && validSlug  && validAction && validScope)
                        val id=when(crudType){CrudType.CREATE->null;CrudType.UPDATE->{oldItem?.id};else->null}

                        if(validData){
                            val action=returnAction(currentAction!!.first)
                            val scope=currentScope?.third
                            val cleanSlug=currentSlug.removeCrudPrefix().removeModulePrefix()
                            val fullSlug="$action$scope$cleanSlug"
                            val body=PermissionBody(id=id,name = currentName,slug = fullSlug)
                            when(crudType){
                                CrudType.CREATE->{permissionController.storeSingleSuperUserPermission(body)}
                                CrudType.UPDATE->{if(body.id!=null)permissionController.updateSingleSuperUserPermission(body)}
                                else->{}
                            }

                        }
                    }
                    VerticalSpacer()
                }
            }
            else{
                if(loading) LoadingScreen(modifier=Modifier.background(WHITE))
                else{
                    if(fail) FailScreen(errors = errors, message = errorMessage)
                }
            }
        }
    }
}

//Edit a permission that can be used in role
@Composable
fun HospitalUserPermissionDialog(showDialog: MutableState<Boolean>,
                                 oldItem: Permission?,
                                 crudType: CrudType,
                                 controller: PermissionsController){

    val permissionController:HospitalUserPermissionController= viewModel()

    val selectedAction  =  remember { mutableStateOf<Triple<CRUD,String,String>?>(null) }
    val selectedScope   =  remember { mutableStateOf<Triple<Modular,String,String>?>(null) }
    val name            =  remember { mutableStateOf(EMPTY_STRING) }
    val slug            =  remember { mutableStateOf(EMPTY_STRING) }
    val state           by permissionController.singlePermissionState.observeAsState()
    var loading         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    var errorMessage    by remember { mutableStateOf(EMPTY_STRING) }
    var errors          by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var validName       by remember { mutableStateOf(false) }
    var validSlug       by remember { mutableStateOf(false) }
    var validAction     by remember { mutableStateOf(false) }
    var slugHasScope    by remember { mutableStateOf(false) }
    var validScope      by remember { mutableStateOf(false) }


    if(showDialog.value){
        when(state){
            is UiState.Loading->{LaunchedEffect(Unit) {loading=true;success=false;fail=false }}
            is UiState.Error->{
                LaunchedEffect(Unit) {
                    loading=false;success=false;fail=true
                    val s= state as UiState.Error
                    val exception=s.exception
                    errors=exception.errors?: emptyMap()
                    errorMessage=exception.message?: ERROR_CREATING_DATA_LABEL
                }
            }
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false;showDialog.value=false;controller.reload()
                    permissionController.reloadSinglePermission()
                    name.value= EMPTY_STRING;slug.value= EMPTY_STRING
                }
            }
            else->{loading=false;success=false;fail=false}
        }
        LaunchedEffect(name.value) {validName=name.value!= EMPTY_STRING}
        LaunchedEffect(selectedAction.value) {validAction=selectedAction.value!=null}
        LaunchedEffect(selectedScope.value) {validScope=selectedScope.value!=null}
        LaunchedEffect(slug.value) {
            slugHasScope=scopeList.filter { slug.value.startsWith(it.third) }.maxByOrNull { it.third.length }!=null && selectedScope.value!=null
            validSlug=slug.value!= EMPTY_STRING && !slug.value.contains(SPACE) && !slugHasScope
        }

        LaunchedEffect(Unit) {
            if(crudType==CrudType.UPDATE && oldItem!=null){
                val oldName=oldItem.name
                val oldSlug=oldItem.slug

                oldName?.let {name.value= it}
                oldSlug?.let {
                    val matchedAction = actionList.firstOrNull { action-> oldSlug.startsWith(action.third, ignoreCase = true) }
                    val scope=scopeList.firstOrNull {scope-> oldSlug.removeCrudPrefix().startsWith(scope.third,ignoreCase = true) }
                    selectedAction.value = actionList.firstOrNull { it== matchedAction }
                    selectedScope.value= scope
                    slug.value=oldSlug.removeCrudPrefix().removeModulePrefix()
                }

            }
        }

        Dialog(onDismissRequest = {showDialog.value=false}) {
            if(!loading && !fail && !success){
                ColumnContainer {
                    Row(modifier=Modifier.fillMaxWidth().background(BLUE), horizontalArrangement = Arrangement.Center){
                        Label("$PERMISSIONS_LABEL $NORMAL_USER_LABEL",color=WHITE, paddingTop = 5, paddingBottom = 5)
                    }
                    VerticalSpacer()
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(title = ACTION_LABEL,hasTitle = true,selectedItem = selectedAction,loadedItems = actionList,
                            selectedContent = {CustomInput(selectedAction.value?.second?:SELECT_ACTION_LABEL)},
                            itemContent = { Label(it?.second?: EMPTY_STRING)})
                    }
                    if(selectedAction.value==null){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Label(text = "*$ERROR_ACTION_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }
                    Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(
                            title = "Scopes",
                            hasTitle = true,
                            selectedItem = selectedScope,
                            loadedItems = scopeList,
                            selectedContent = {
                                CustomInput(selectedScope.value?.second?:EMPTY_STRING)
                            }
                        ) { Label(it?.second?:EMPTY_STRING)}
                    }
                    if(selectedScope.value==null){
                        Row(modifier=Modifier.fillMaxWidth()){
                            Label(text = "*$ERROR_SCOPE_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }
                    CustomInput(name, NAME_LABEL, maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onTextChange = {name.value=it})
                    if(name.value.trim()== EMPTY_STRING){
                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                            Label(text = "*$ERROR_NAME_FILED_REQUIRED_LABEL", color = Color.Red, fontSize = 10)
                        }
                    }
                    CustomInput(value=slug, SLUG_LABEL,maxLines = 2,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onTextChange = {t->
                            val withNoDots=t.replaceSpaceWithDot().toLowerCase(Locale("en"))
                            slug.value=withNoDots.removeCrudPrefix().removeModulePrefix()})
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                        when{
                            slug.value.trim()== EMPTY_STRING->{Label(text = "*$ERROR_SLUG_FIELD_REQUIRED_LABEL", color = Color.Red, fontSize = 10)}
                            slug.value.contains(SPACE)->{Label(text = "*$ERROR_SLUG_FIELD_HAS_SPACE_LABEL", color = Color.Red, fontSize = 10)}
                            slugHasScope->{Label(text = "*$ERROR_SLUG_FIELD_IS_INVALID_LABEL", color = Color.Red, fontSize = 10)}
                            else->{Label(text = SLUG_FILED_IS_VALID_LABEL, color = GREEN, fontSize = 10)}
                        }
                    }

                    selectedAction.value?.let{
                        val currentAction=returnAction(it.first)
                        val selectedScope=selectedScope.value?.third
                        val cleanSlug=slug.value.removeCrudPrefix().removeModulePrefix()
                        val fullSlug="$currentAction$selectedScope$cleanSlug"
                        Label(text=fullSlug, maximumLines = 3)
                    }
                    VerticalSpacer()
                    CustomButtonLeftIcon(label= SAVE_CHANGES_LABEL,
                        buttonShadowElevation = 6, buttonShape = rcs(15),
                        enabled = (validName && validSlug  && validAction && validScope),
                        enabledBackgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        disabledFontColor = GRAY,
                        enabledFontColor = GREEN,
                        icon = if(validName && validSlug  && validAction && validScope)R.drawable.ic_send_green else R.drawable.ic_send_gray,
                        horizontalPadding = 10,
                        hasBorder = true,
                        borderColor = if(validName && validSlug  && validAction && validScope) GREEN else GRAY) {
                        val currentName=name.value
                        val currentSlug=slug.value
                        val currentAction=selectedAction.value
                        val currentScope=selectedScope.value
                        val validData=(validName && validSlug  && validAction && validScope)
                        val id=when(crudType){CrudType.CREATE->null;CrudType.UPDATE->{oldItem?.id};else->null}

                        if(validData){
                            val action=returnAction(currentAction!!.first)
                            val scope=currentScope?.third
                            val cleanSlug=currentSlug.removeCrudPrefix().removeModulePrefix()
                            val fullSlug="$action$scope$cleanSlug"
                            val body=PermissionBody(id=id,name = currentName,slug = fullSlug)
                            when(crudType){
                                CrudType.CREATE->{permissionController.storeSingleHospitalUserPermission(body)}
                                CrudType.UPDATE->{if(body.id!=null)permissionController.updateSingleHospitalUserPermission(body)}
                                else->{}
                            }

                        }
                    }
                    VerticalSpacer()
                }
            }
            else{
                if(loading) LoadingScreen(modifier=Modifier.background(WHITE))
                else{
                    if(fail) FailScreen(errors = errors, message = errorMessage)
                }
            }
        }
    }
}


//Edit Permissions within a role
@Composable
fun EditHospitalUserRolePermissionsDialog(showDialog:MutableState<Boolean>,
                                          controller: PermissionsController,
                                          item:Role){
    if(showDialog.value){
        val state by controller.singleRoleState.observeAsState()
        val options by controller.permissionsState.observeAsState()
        var permissionsList by remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermissions = remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermission = remember { mutableStateOf<Permission?>(null) }
        LaunchedEffect(Unit) {selectedPermissions.value=item.permissions}
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{LaunchedEffect(Unit) {showDialog.value=false;controller.index()}}
            else->{}
        }
        when(options){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = options as UiState.Success<PermissionsResponse>
                val r = s.data
                val data=r.data
                permissionsList=data.permissions
            }
            else->{ controller.permissionsList() }
        }
        LaunchedEffect(selectedPermission.value) {
            val new= mutableListOf<Permission>()
            selectedPermission.value?.let {new.add(it)}
            selectedPermissions.value.forEach { p-> if(new.find { n->n==p }==null) new.add(p) }
            selectedPermissions.value=new
        }
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                Column(modifier=Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Label(item.name)
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                        items(permissionsList.sortedBy { it.slug }){
                            originalPermission->
                            var present by rememberSaveable { mutableStateOf(originalPermission in selectedPermissions.value) }
                            CustomCheckbox(
                                label = {
                                    val slug=originalPermission.slug?: EMPTY_STRING
                                    val crud=slug.getCrudPrefix()
                                    val module=slug.removeCrudPrefix().getModulePrefix()
                                    val crudBackgroundColor= when (crud) {
                                        BROWSE_EN_LABEL.toLowerCase(Locale("en")) -> BLUE
                                        CREATE_EN_LABEL.toLowerCase(Locale("en")) -> GREEN
                                        DELETE_EN_LABEL.toLowerCase(Locale("en")) -> Color.Red
                                        READ_EN_LABEL.toLowerCase(Locale("en")) -> colorResource(R.color.pink)
                                        RESTORE_EN_LABEL.toLowerCase(Locale("en")) -> GREEN
                                        UPDATE_EN_LABEL.toLowerCase(Locale("en")) -> ORANGE
                                        VIEW_EN_LABEL.toLowerCase(Locale("en")) -> colorResource(R.color.teal_700)
                                        else -> BLACK
                                    }
                                    val moduleBackgroundColor= when (module) {
                                        "main_module@" -> PINK
                                        "inner_module@" -> ORANGE
                                        "sub_module@" -> BLUE
                                        else -> BLACK
                                    }
                                    Column(modifier=Modifier.fillMaxWidth().weight(1f)){
                                        Label(originalPermission.name?: EMPTY_STRING,maximumLines = Int.MAX_VALUE,
                                            paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5)
                                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),verticalAlignment = Alignment.CenterVertically){
                                            if(crud!= EMPTY_STRING){
                                                Span(crud,
                                                    backgroundColor = crudBackgroundColor,
                                                    maximumLines = Int.MAX_VALUE,
                                                    paddingStart = 5, paddingEnd = 5,
                                                    color = WHITE)
                                            }
                                            if(module!= EMPTY_STRING){
                                                Span(crud,
                                                    backgroundColor = moduleBackgroundColor,
                                                    maximumLines = Int.MAX_VALUE,
                                                    paddingStart = 5, paddingEnd = 5,
                                                    color = WHITE)
                                            }
                                        }
                                        Span(slug.removeCrudPrefix().removeModulePrefix(), backgroundColor = BLACK,
                                            maximumLines = Int.MAX_VALUE,
                                            paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5,
                                            color = WHITE)

                                    }

                                },
                                active = present,
                                onCheckChange = {checked->
                                    if(checked){
                                        val newP= mutableListOf<Permission>()
                                        newP.add(originalPermission)
                                        newP.addAll(selectedPermissions.value.filter { it!=originalPermission })
                                        selectedPermissions.value=newP
                                    }
                                    else selectedPermissions.value=selectedPermissions.value.filter { selected->selected!=originalPermission }
                                    present=selectedPermissions.value.contains(originalPermission)
                                })
                        }
                    }
                    VerticalSpacer()
                    CustomButtonLeftIcon(label = SAVE_CHANGES_LABEL,
                        enabledBackgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        enabledFontColor = GREEN,
                        borderColor = GREEN,
                        hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                        buttonShadowElevation = 6,
                        buttonShape = rcs(5),
                        horizontalPadding = 10,
                    ) {
                        val permissionIds=selectedPermissions.value.map { it.id?:0 }
                        val body= RolePermissionsBody(
                            id=item.id,
                            permissions = permissionIds
                        )
                        controller.updateHospitalUserRolePermissions(body)
                    }

                }

            }
        }
    }
}

@Composable
fun EditSuperUserRolePermissionsDialog(showDialog:MutableState<Boolean>,
                                       controller: PermissionsController,
                                       item:Role){
    if(showDialog.value){
        val state by controller.singleRoleState.observeAsState()
        val options by controller.permissionsState.observeAsState()
        var permissionsList by remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermissions = remember { mutableStateOf<List<Permission>>(emptyList()) }
        val selectedPermission = remember { mutableStateOf<Permission?>(null) }
        LaunchedEffect(Unit) {selectedPermissions.value=item.permissions}
        when(state){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{LaunchedEffect(Unit) {showDialog.value=false;controller.index()}}
            else->{}
        }
        when(options){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                val s = options as UiState.Success<PermissionsResponse>
                val r = s.data
                val data=r.data
                permissionsList=data.superPermissions
            }
            else->{ controller.permissionsList() }
        }
        LaunchedEffect(selectedPermission.value) {
            val new= mutableListOf<Permission>()
            selectedPermission.value?.let {new.add(it)}
            selectedPermissions.value.forEach { p-> if(new.find { n->n==p }==null) new.add(p) }
            selectedPermissions.value=new
        }
        Dialog(
            onDismissRequest = {showDialog.value=false}
        ) {
            ColumnContainer {
                Column(modifier=Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                        items(permissionsList.sortedBy { it.slug }){
                                originalPermission->
                            var present by rememberSaveable { mutableStateOf(originalPermission in selectedPermissions.value) }
                            CustomCheckbox(
                                label = {
                                    val slug=originalPermission.slug?: EMPTY_STRING
                                    val crud=slug.getCrudPrefix()
                                    val module=slug.removeCrudPrefix().getModulePrefix()
                                    val crudBackgroundColor= when (crud) {
                                        BROWSE_EN_LABEL.toLowerCase(Locale("en")) -> BLUE
                                        CREATE_EN_LABEL.toLowerCase(Locale("en")) -> GREEN
                                        DELETE_EN_LABEL.toLowerCase(Locale("en")) -> Color.Red
                                        READ_EN_LABEL.toLowerCase(Locale("en")) -> colorResource(R.color.pink)
                                        RESTORE_EN_LABEL.toLowerCase(Locale("en")) -> GREEN
                                        UPDATE_EN_LABEL.toLowerCase(Locale("en")) -> ORANGE
                                        VIEW_EN_LABEL.toLowerCase(Locale("en")) -> colorResource(R.color.teal_700)
                                        else -> BLACK
                                    }
                                    val moduleBackgroundColor= when (module) {
                                        "main_module@" -> PINK
                                        "inner_module@" -> ORANGE
                                        "sub_module@" -> BLUE
                                        else -> BLACK
                                    }
                                    Column(modifier=Modifier.fillMaxWidth().weight(1f)){
                                        Label(originalPermission.name?: EMPTY_STRING,maximumLines = Int.MAX_VALUE,
                                            paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5)
                                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),verticalAlignment = Alignment.CenterVertically){
                                            if(crud!= EMPTY_STRING){
                                                Span(crud,
                                                    backgroundColor = crudBackgroundColor,
                                                    maximumLines = Int.MAX_VALUE,
                                                    paddingStart = 5, paddingEnd = 5,
                                                    color = WHITE)
                                            }
                                            if(module!= EMPTY_STRING){
                                                Span(crud,
                                                    backgroundColor = moduleBackgroundColor,
                                                    maximumLines = Int.MAX_VALUE,
                                                    paddingStart = 5, paddingEnd = 5,
                                                    color = WHITE)
                                            }
                                        }
                                        Span(slug.removeCrudPrefix().removeModulePrefix(), backgroundColor = BLACK,
                                            maximumLines = Int.MAX_VALUE,
                                            paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5,
                                            color = WHITE)

                                    }

                                },
                                active = present,
                                onCheckChange = {checked->
                                    if(checked){
                                        val newP= mutableListOf<Permission>()
                                        newP.add(originalPermission)
                                        newP.addAll(selectedPermissions.value.filter { it!=originalPermission })
                                        selectedPermissions.value=newP
                                    }
                                    else selectedPermissions.value=selectedPermissions.value.filter { selected->selected!=originalPermission }
                                    present=selectedPermissions.value.contains(originalPermission)
                                })
                        }
                    }

                    VerticalSpacer()
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        enabledBackgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        enabledFontColor = GREEN,
                        borderColor = GREEN,
                        hasBorder = true, icon = R.drawable.ic_wand_stars_green,
                        buttonShadowElevation = 6,
                        buttonShape = rcs(5),
                        horizontalPadding = 10,
                    ) {
                        val permissionIds=selectedPermissions.value.map { it.id?:0 }
                        val body= RolePermissionsBody(
                            id=item.id,
                            permissions = permissionIds
                        )
                        controller.updateSuperUserRolePermissions(body)
                    }

                }

            }
        }
    }
}

@Composable
private fun PermissionsPreview(
    model: MutableState<CustomModel?>,
    actionType: MutableState<ActionType?>,
    role: MutableState<Role?>,
    columns: MutableState<List<String>>,
    conditions: MutableState<List<ConditionBody>>
) {
    val mapped=conditions.value.groupBy { it.clause }
    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp)
        .border(width = 1.dp, shape = rcs(20),color= TEAL700))
    {
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),horizontalArrangement = Arrangement.SpaceAround){
            if(model.value!=null){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(TABLE_LABEL, color = WHITE, backgroundColor = BLUE)
                    Label(model.value?.name?:EMPTY_STRING, paddingStart = 10, paddingTop = 5)
                }
            }
            if(actionType.value!=null){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(ACTION_TYPE_LABEL, color = WHITE, backgroundColor = BLUE)
                    Label(actionType.value?.name?:EMPTY_STRING, paddingStart = 10)
                }
            }
            if(role.value!=null){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(ROLE_LABEL, color = WHITE, backgroundColor = BLUE)
                    Label(role.value?.name?:EMPTY_STRING, paddingStart = 10)
                }
            }
        }
        if(mapped.isNotEmpty()){
            val columnsValue=columns.value
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                    Label("Columns Preview")
                }
                VerticalSpacer()
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 0..3){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 4..7){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5, paddingTop = 0, paddingBottom = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 8..11){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5, paddingTop = 0, paddingBottom = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 12..15){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5, paddingTop = 0, paddingBottom = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 16..19){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, paddingStart = 5, paddingEnd = 5, paddingTop = 0, paddingBottom = 5)
                        }
                    }
                }
            }
        }
        /*
        clause:"where",
        columnName:"id",
        operator:"=",
        value:"1",
        conditions=
        [

        ]
         */
        if(mapped.isNotEmpty()){
            Label(CONDITIONS_LABEL, paddingStart = 5, paddingEnd = 5)
            mapped.forEach { (clause, listOfInputs) ->
                Label(clause)
                Column(modifier=Modifier.padding(horizontal = 5.dp)){
                    listOfInputs.forEach { body->
                        Row{
                            Label(body.value, paddingStart = 5, paddingEnd = 5)
                            Label(body.operator?:" ")
                            Label(body.columnName)
                        }
                    }

                }
            }
            VerticalSpacer()
        }
        VerticalSpacer()
    }
}


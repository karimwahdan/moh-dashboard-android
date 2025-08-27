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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.ConditionBody
import com.kwdevs.hospitalsdashboard.bodies.control.PermissionBody
import com.kwdevs.hospitalsdashboard.controller.control.PermissionsController
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.routes.UserControlRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_CONDITION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_PERMISSION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CONDITIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ACTION_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ROLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SLUG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsT

@Composable
fun AddNewPermissionDialog(showDialog: MutableState<Boolean>,
                           controller: PermissionsController,
                           roles:List<Role>,
                           models:List<CustomModel>,
                           actionTypes:List<ActionType>){
    val state by controller.singlePermissionState.observeAsState()
    val name = remember { mutableStateOf("") }
    val slug = remember { mutableStateOf("") }
    val selectedRole = remember { mutableStateOf<Role?>(null) }
    val conditions = remember { mutableStateOf<List<ConditionBody>>(emptyList()) }
    val selectedColumns = remember { mutableStateOf<List<String>>(emptyList()) }
    val selectedModel = remember { mutableStateOf<CustomModel?>(null) }
    val selectedActionType = remember { mutableStateOf<ActionType?>(null) }
    var modelColumns by remember { mutableStateOf<List<String>>(emptyList()) }
    val showConditionsDialog = remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    LaunchedEffect(showDialog.value) {if(!showDialog.value) controller.reload()}
    ConditionsDialog(showConditionsDialog,
        columns = modelColumns,
        result = conditions)
    if(showDialog.value){
        when(state){
            is UiState.Loading->{
                loading=true;fail=false;success=false
                Log.e(UserControlRoute.route,"AddNewPermissionDialog,loading: $state")
            }
            is UiState.Error->{
                loading=false;fail=true;success=false

                Log.e(UserControlRoute.route,"AddNewPermissionDialog,error: $state")

                val s = state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage = exception.message?:""
                Log.e(UserControlRoute.route,"Add New Permission Dialog: Error: $errorMessage")
            }
            is UiState.Success->{
                loading=false;fail=false;success=true
                Log.e(UserControlRoute.route,"AddNewPermissionDialog,success: $state")

            }
            else->{
                loading=false;fail=false;success=false
                Log.e(UserControlRoute.route,"AddNewPermissionDialog,else: $state")

            }
        }

        LaunchedEffect(selectedModel.value) {
            if(selectedModel.value!=null){
                val model=selectedModel.value
                model?.let { modelColumns=it.columns }
            }
        }

        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {showDialog.value=false}) {
            Column(modifier= Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp)
                .shadow(elevation = 5.dp, shape = rcs(20))
                .background(color = WHITE, shape = rcs(20))){
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label(ADD_NEW_PERMISSION_LABEL, color = WHITE) }
                }
                VerticalSpacer()
                if(!loading && !success && !fail){
                    LazyColumn(modifier= Modifier.fillMaxWidth()){
                        item{

                            //TODO PREVIEW
                            if(selectedModel.value!=null || selectedActionType.value!=null || selectedRole.value!=null){
                                PermissionsPreview(model=selectedModel,actionType=selectedActionType,role=selectedRole,columns=selectedColumns,conditions=conditions)
                            }
                            //TODO END PREVIEW

                            VerticalSpacer()
                            CustomButton(
                                label = ADD_NEW_CONDITION_LABEL,
                                buttonShape = rcs(5),
                                enabled = selectedModel.value!=null) {
                                showConditionsDialog.value=true
                            }
                            VerticalSpacer()
                            //TODO NAME
                            Box(modifier= Modifier.padding(5.dp)){
                                CustomInput(value=name,label= NAME_LABEL)
                            }

                            //TODO SLUG
                            Box(modifier= Modifier.padding(5.dp)){
                                CustomInput(value=slug,label= SLUG_LABEL, hasWhiteSpaces = false,replacedWith=".")
                            }
                            //TODO ROLE SELECTOR
                            Box(modifier= Modifier.padding(5.dp)){
                                ComboBox(title = ROLES_LABEL,
                                    loadedItems = roles, selectedItem = selectedRole, selectedContent = {
                                        CustomInput(selectedRole.value?.name?: SELECT_ROLE_LABEL)
                                    }) {
                                    Label(it?.name?:"")
                                }
                            }

                            VerticalSpacer()

                            //TODO ACTION TYPE SELECTOR
                            Box(modifier= Modifier.padding(5.dp)){
                                ComboBox(title = ACTION_TYPE_LABEL,
                                    loadedItems = actionTypes, selectedItem = selectedActionType, selectedContent = {
                                        CustomInput(selectedActionType.value?.name?: SELECT_ACTION_TYPE_LABEL)
                                    }) {
                                    Label(it?.name?:"")
                                }
                            }

                            VerticalSpacer()

                            //TODO MODEL SELECTOR
                            Box(modifier= Modifier.padding(5.dp)){
                                ComboBox(title = TABLE_LABEL,
                                    loadedItems = models, selectedItem = selectedModel, selectedContent = {
                                        CustomInput(selectedModel.value?.name?: SELECT_TABLE_LABEL)
                                    }) {
                                    Label(it?.name?:"")
                                }

                            }

                            VerticalSpacer()

                            //TODO COLUMNS SELECTOR
                            if(modelColumns.isNotEmpty()){
                                Column(modifier = Modifier.fillMaxWidth().padding(10.dp)
                                    .border(width = 1.dp, shape = rcs(20), color = GRAY)){
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 0..2) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 3..5) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 6..8) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 9..11) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 12..14) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    LazyRow(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(modelColumns){index,s->
                                            if (index in 15..17) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                                        .padding(5.dp)
                                                        .border(width = 1.dp,
                                                            color = Color.Gray,
                                                            shape = rcs(5)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Label(s, paddingStart = 5, paddingTop = 5, paddingBottom = 5)
                                                    HorizontalSpacer()
                                                    if (s in selectedColumns.value) {
                                                        IconButton(R.drawable.ic_delete_red) {
                                                            selectedColumns.value =
                                                                selectedColumns.value.filter { it != s }
                                                        }
                                                    }
                                                    else {
                                                        IconButton(
                                                            R.drawable.ic_add_circle_white,
                                                            background = GREEN
                                                        ) {
                                                            val n = mutableListOf<String>()
                                                            n.addAll(selectedColumns.value)
                                                            if (s !in n) {
                                                                n.add(s)
                                                            }
                                                            selectedColumns.value = n
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            VerticalSpacer()

                            //TODO SAVE BUTTONS
                            Row(modifier= Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround){
                                CustomButton(label = SAVE_CHANGES_LABEL,
                                    enabledFontColor = WHITE,
                                    disabledFontColor = GRAY,
                                    disabledBackgroundColor = Color.LightGray,
                                    enabledBackgroundColor = GREEN,
                                    buttonShadowElevation = 5,
                                    buttonShape = rcs(5),
                                    enabled = selectedModel.value!=null && selectedActionType.value!=null && selectedRole.value!=null) {
                                    val m=selectedModel.value
                                    val at=selectedActionType.value
                                    val role=selectedRole.value
                                    if(m!=null && at !=null && role!=null){
                                        val input= PermissionBody(
                                            name=name.value,
                                            slug=slug.value,
                                           // roleId = role.id,
                                            modelId =m.id,
                                            actionTypeId = at.id,
                                            columns = selectedColumns.value,
                                            conditions =conditions.value
                                        )
                                        controller.storeNewPermission(input)
                                    }
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
                if(fail){
                    FailScreen(label=errorMessage)
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
                    Label(model.value?.name?:"", paddingStart = 10, paddingTop = 5)
                }
            }
            if(actionType.value!=null){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(ACTION_TYPE_LABEL, color = WHITE, backgroundColor = BLUE)
                    Label(actionType.value?.name?:"", paddingStart = 10)
                }
            }
            if(role.value!=null){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Span(ROLE_LABEL, color = WHITE, backgroundColor = BLUE)
                    Label(role.value?.name?:"", paddingStart = 10)
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
                            Span(text=s, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5, topPadding = 5, bottomPadding = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 4..7){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5, topPadding = 0, bottomPadding = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 8..11){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5, topPadding = 0, bottomPadding = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 12..15){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5, topPadding = 0, bottomPadding = 5)
                        }
                    }
                }
                Row(modifier=Modifier.padding(5.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    columnsValue.forEachIndexed { index, s ->
                        if(index in 16..19){
                            Span(text=s, color = WHITE, backgroundColor = BLUE, startPadding = 5, endPadding = 5, topPadding = 0, bottomPadding = 5)
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


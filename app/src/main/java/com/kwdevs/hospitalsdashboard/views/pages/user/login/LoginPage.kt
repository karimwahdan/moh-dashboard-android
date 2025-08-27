package com.kwdevs.hospitalsdashboard.views.pages.user.login

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.RIGHT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.ACCOUNT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.LOGIN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_ACCOUNT_TYPE
import com.kwdevs.hospitalsdashboard.views.assets.USERNAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen
import com.kwdevs.hospitalsdashboard.views.rcs

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginPage(navHostController: NavHostController){
    val controller          :  UsersController= viewModel()
    val username            =  remember { mutableStateOf("") }
    val password            =  remember { mutableStateOf("") }
    val selectedAccountType =  remember { mutableStateOf(SELECT_ACCOUNT_TYPE) }
    var showPassword        by remember { mutableStateOf(false) }
    var superUser           by remember { mutableStateOf<SimpleSuperUser?>(null) }
    var hospitalUser        by remember { mutableStateOf<SimpleHospitalUser?>(null) }
    val hospitalUserState   by controller.hospitalUserSingleState.observeAsState()
    val superUserState      by controller.superUserSingleState.observeAsState()
    var loading             by remember{ mutableStateOf(false)}
    var success             by remember{ mutableStateOf(false)}
    var fail                by remember{ mutableStateOf(false)}
    val ministryAccount="حساب متابعة"
    val normalAccount="حساب عادى"
    val accountTypes = listOf(ministryAccount,normalAccount)
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf("") }
    when(hospitalUserState){
        is UiState.Loading->{ loading=true;fail=false;success=false }
        is UiState.Error->{
            LaunchedEffect(Unit){
                loading=false;fail=true;success=false
                val s = hospitalUserState as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
                errors=exception.errors?: emptyMap()
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = hospitalUserState as UiState.Success<HospitalUserSSResponse>
                val response = s.data
                val data = response.data
                hospitalUser=data
                Preferences.User().set(HospitalUser(
                    id=data.id, title = data.title,
                    titleId = data.titleId,
                    name = data.name,
                    username = username.value,
                    password = password.value,
                    active = data.active,
                    hospital = data.hospital,
                    hospitalId = data.hospitalId,
                    roles = data.roles
                ))
                val hospital=data.hospital
                Preferences.Hospitals().set(hospital)
                Preferences.User().setType(ViewType.HOSPITAL_USER)
                navHostController.navigate(HospitalHomeRoute.route)
            }
        }
        else->{}
    }
    when(superUserState){
        is UiState.Loading->{
            loading=true;fail=false;success=false
        }
        is UiState.Error->{
            LaunchedEffect(Unit){
                loading=false;fail=true;success=false
                val s = superUserState as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
                errors=exception.errors?: emptyMap()
            }
        }
        is UiState.Success->{

            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = superUserState as UiState.Success<SimpleSuperUserSingleResponse>
                val response = s.data
                val data = response.data
                superUser=data
                Preferences.User().setSuper(
                    SuperUser(
                        id=data.id, title = data.title,
                        titleId = data.titleId, name = data.name,
                        username = username.value,
                        password = password.value,
                        active = data.active,
                        isSuper = data.isSuper,
                        roles = data.roles))
                Preferences.User().setType(ViewType.SUPER_USER)
                if(data.isSuper) navHostController.navigate(AdminHomeRoute.route)
                else navHostController.navigate(HomeRoute.route)
            }
        }
        else->{}
    }
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(!success && !fail){
                Box(modifier=Modifier.fillMaxSize().background(color= colorResource(R.color.blue3))){
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        Icon(background = WHITE,icon=R.drawable.logo, size = 144, containerSize = 145, shape = CircleShape)
                        VerticalSpacer(20)

                        VerticalSpacer(25)

                        ColumnContainer {
                            VerticalSpacer(10)
                            Box(modifier=Modifier.padding(horizontal = 5.dp)){
                                ComboBox(
                                    default = false,
                                    hasTitle = false,
                                    loadedItems = accountTypes,
                                    selectedItem = selectedAccountType,
                                    selectedContent = {

                                        TextField(
                                            modifier=Modifier.fillMaxWidth().padding(5.dp)
                                                .border(width = 1.dp, shape = rcs(3),color= colorResource(R.color.lightGray2))
                                                .background(color= colorResource(R.color.lightGray), shape = rcs(3))
                                                .clip(rcs(3)),
                                            maxLines = 1,
                                            minLines = 1,
                                            singleLine = true,
                                            readOnly = true,
                                            leadingIcon = {
                                                Box(modifier=Modifier.size(52.dp).background(color= colorResource(R.color.lightGray)),
                                                    contentAlignment = Alignment.Center){
                                                    Image(
                                                        modifier=Modifier.size(26.dp),
                                                        contentScale = ContentScale.FillBounds,
                                                        painter = painterResource(R.drawable.ic_account_tree_white),
                                                        contentDescription = null)

                                                }
                                            },
                                            value=selectedAccountType.value,
                                            label = { Label(ACCOUNT_TYPE_LABEL)},
                                            onValueChange = {},
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = WHITE,
                                                unfocusedContainerColor = colorResource(R.color.lightGray2),
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent
                                            )
                                        )

                                        //CustomInput(selectedAccountType.value)
                                    }) {
                                    Label(it)
                                }
                            }
                            VerticalSpacer(10)
                            TextField(
                                modifier=Modifier.fillMaxWidth().padding(10.dp)
                                    .border(width = 1.dp, shape = rcs(3),color= colorResource(R.color.lightGray2))
                                    .background(color= colorResource(R.color.lightGray), shape = rcs(3))
                                    .clip(rcs(3)),
                                maxLines = 1,
                                minLines = 1,
                                singleLine = true,
                                leadingIcon = {
                                    Box(modifier=Modifier.size(52.dp).background(color= colorResource(R.color.lightGray)),
                                        contentAlignment = Alignment.Center){
                                        Image(
                                            modifier=Modifier.size(26.dp),
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(R.drawable.ic_person_white),
                                            contentDescription = null)

                                    }
                                },
                                value=username.value,
                                label = { Label(USERNAME_LABEL)},
                                onValueChange = {username.value=it},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = WHITE,
                                    unfocusedContainerColor = colorResource(R.color.lightGray2),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                            VerticalSpacer(5)
                            TextField(
                                modifier=Modifier.fillMaxWidth().padding(10.dp)
                                    .border(width = 1.dp, shape = rcs(3),color= colorResource(R.color.lightGray2))
                                    .background(color= colorResource(R.color.lightGray), shape = rcs(3))
                                    .clip(rcs(3)),
                                maxLines = 1,
                                minLines = 1,
                                singleLine = true,
                                leadingIcon = {
                                    Box(modifier=Modifier.size(52.dp).background(color= colorResource(R.color.lightGray)),
                                        contentAlignment = Alignment.Center){
                                        Image(
                                            modifier=Modifier.size(26.dp),
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(R.drawable.ic_lock_white),
                                            contentDescription = null)
                                    }
                                },
                                trailingIcon = {
                                    IconButton(background = Color.Transparent,icon=if(showPassword) R.drawable.ic_visibility_off_dark_blue  else R.drawable.ic_eye_blue ){showPassword=!showPassword}
                                },
                                value=password.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                label = { Label(PASSWORD_LABEL)},
                                onValueChange = {password.value=it},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = WHITE,
                                    unfocusedContainerColor = colorResource(R.color.lightGray2),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        VerticalSpacer(20)
                        Button(
                            modifier=Modifier.width(200.dp),
                            shape = rcs(5),
                            onClick = {
                                if(selectedAccountType.value==ministryAccount) controller.loginSuper(username.value,password.value)
                                else if(selectedAccountType.value==normalAccount) controller.loginNormal(username.value,password.value)
                            },
                            enabled = username.value!= "" &&
                                    password.value!="" &&
                                    selectedAccountType.value!=SELECT_ACCOUNT_TYPE,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.orange),
                                disabledContainerColor = colorResource(R.color.lightGray)
                            )) {
                            Label(LOGIN_LABEL,
                                color = colorResource(if(username.value!="" &&
                                    password.value!="" &&
                                    selectedAccountType.value!=SELECT_ACCOUNT_TYPE) R.color.white else R.color.gray),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16)
                        }
                    }

                }
            }
            if(success){
                SuccessScreen(modifier=Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.ic_check_circle_green),contentDescription = null)
                }
            }
            if(fail){
                FailScreen(modifier = Modifier.fillMaxSize(),
                    message=errorMessage,errors = errors,)
            }
        }

    }
    BackHandler {
       fail=false;success=false;loading=false;
        controller.reload()
    }
}
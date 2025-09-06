package com.kwdevs.hospitalsdashboard.views.pages.user

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.PasswordBody
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CHANGE_PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CURRENT_PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.NEW_PASSWORD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PASSWORD_CONFIRMATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.USERNAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.toast
import com.kwdevs.hospitalsdashboard.views.deleteData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordPage(navHostController: NavHostController){
    val controller:UsersController= viewModel()
    val superState by controller.superUserSingleState.observeAsState()
    val hospitalState by controller.fullHospitalUserSingleState.observeAsState()
    val userType=Preferences.User().getType()
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val showSheet = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf(EMPTY_STRING) }
    val oldPassword = remember { mutableStateOf(EMPTY_STRING) }
    val newPassword= remember { mutableStateOf(EMPTY_STRING) }
    val passwordConfirmation= remember { mutableStateOf(EMPTY_STRING) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    var loadingSuper by remember { mutableStateOf(false) }
    var failSuper by remember { mutableStateOf(false) }
    var successSuper by remember { mutableStateOf(false) }

    val context= LocalContext.current
    when(userType){
        ViewType.SUPER_USER->{
            when(superState){
                is UiState.Loading->{
                    LaunchedEffect(Unit){loadingSuper=true;failSuper=false;successSuper=false}}
                is UiState.Error->{LaunchedEffect(Unit){loadingSuper=false;failSuper=true;successSuper=false}}
                is UiState.Success->{
                    LaunchedEffect(Unit) {
                        loadingSuper=false;failSuper=false;successSuper=true
                        toast(context,"تم تغيير كلمة المرور بنجاح, من فضلك اعد تسجيل الدخول")
                        navHostController.navigate(LoginRoute.route)
                        deleteData()
                    }
                }
                else->{}
            }

        }
        ViewType.HOSPITAL_USER->{
            when(hospitalState){
                is UiState.Loading->{
                    LaunchedEffect(Unit){loading=true;fail=false;success=false}}
                is UiState.Error->{LaunchedEffect(Unit){loading=false;fail=true;success=false}}
                is UiState.Success->{
                    LaunchedEffect(Unit) {
                        loading=false;fail=false;success=true
                        toast(context,"تم تغيير كلمة المرور بنجاح, من فضلك اعد تسجيل الدخول")
                        navHostController.navigate(LoginRoute.route)
                        deleteData()
                    }
                }
                else->{}
            }

        }
        else->{}
    }
    Container(
        title = CHANGE_PASSWORD_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = { navHostController.navigate(when(userType){
            ViewType.HOSPITAL_USER->HospitalHomeRoute.route
            ViewType.SUPER_USER->HomeRoute.route
            else->LoginRoute.route
        }) },
        ) {
        if((!loading && !success && !fail) && (!loadingSuper && !successSuper && !failSuper)){
            VerticalSpacer()
            CustomInput(username,USERNAME_LABEL)
            CustomInput(oldPassword,CURRENT_PASSWORD_LABEL)
            CustomInput(newPassword,NEW_PASSWORD_LABEL)
            CustomInput(passwordConfirmation,PASSWORD_CONFIRMATION_LABEL)
            Row(modifier=Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center){
                CustomButton(label= SAVE_CHANGES_LABEL,
                    enabled = (username.value!= EMPTY_STRING && oldPassword.value!= EMPTY_STRING &&
                            newPassword.value!= EMPTY_STRING && passwordConfirmation.value!= EMPTY_STRING &&
                            newPassword.value==passwordConfirmation.value),
                    buttonShape = RectangleShape,
                    buttonShadowElevation = 6,) {
                    Log.e("ChangePass",userType?.name?: EMPTY_STRING)
                    Log.e("username",username.value)
                    Log.e("old",oldPassword.value)

                    if(newPassword.value==passwordConfirmation.value){
                        val id=if(userType==ViewType.SUPER_USER && superUser!=null) superUser.id
                        else if(userType==ViewType.HOSPITAL_USER && user!=null) user.id
                        else null
                        val body=PasswordBody(
                            userId = id,
                            username = username.value,
                            oldPassword = oldPassword.value,
                            newPassword = newPassword.value
                        )
                        if(userType==ViewType.SUPER_USER && superUser!=null){
                            controller.changeSuperPassword(body)
                        }
                        else if(userType==ViewType.HOSPITAL_USER && user!=null){
                            controller.changeHospitalUserPassword(body)
                        }
                    }
                    else{
                        Toast.makeText(context,"Password and Confirmation do not match",Toast.LENGTH_SHORT).show()
                    }


                }
            }
        }
        else{
            if(loadingSuper ||loading) LoadingScreen(modifier = Modifier.fillMaxSize())
            else{ if(failSuper || fail) FailScreen(modifier=Modifier.fillMaxSize()) }

        }

    }
    BackHandler {  }
}
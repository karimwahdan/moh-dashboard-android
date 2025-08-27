package com.kwdevs.hospitalsdashboard.views.pages.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordPage(navHostController: NavHostController){
    val controller:UsersController= viewModel()
    val superState by controller.superUserSingleState.observeAsState()
    val hospitalState by controller.hospitalUserSingleState.observeAsState()
    val userType=Preferences.User().getType()
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val showSheet = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    val oldPassword = remember { mutableStateOf("") }
    val newPassword= remember { mutableStateOf("") }
    val passwordConfirmation= remember { mutableStateOf("") }
    val context= LocalContext.current
    if(userType==ViewType.SUPER_USER){
        when(superState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    Toast.makeText(context,"Password Changed successfully, please re-login",Toast.LENGTH_SHORT).show()
                    navHostController.navigate(LoginRoute.route)
                }
            }
            else->{}
        }
    }
    if(userType==ViewType.HOSPITAL_USER){
        when(hospitalState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    Toast.makeText(context,"Password Changed successfully, please re-login",Toast.LENGTH_SHORT).show()
                    navHostController.navigate(LoginRoute.route)
                }
            }
            else->{}
        }
    }
    Container(
        title = "Change Password",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            if(userType==ViewType.SUPER_USER) navHostController.navigate(HomeRoute.route)
            if(userType==ViewType.HOSPITAL_USER) navHostController.navigate(HospitalHomeRoute.route)
        }

    ) {
        CustomInput(username,"Username")
        CustomInput(oldPassword,"Current Password")
        CustomInput(newPassword,"New Password")
        CustomInput(passwordConfirmation,"Password Confirmation")

        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            CustomButton(label= SAVE_CHANGES_LABEL) {
                Log.e("ChangePass",userType?.name?:"")
                Log.e("username",username.value?:"")
                Log.e("old",oldPassword.value?:"")

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
}
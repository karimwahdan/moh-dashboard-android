package com.kwdevs.hospitalsdashboard.views.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LandingPage(navHostController: NavHostController){
    val accountType = Preferences.User().getType()

    val controller: UsersController= viewModel()
    val hospitalUserState by controller.hospitalUserSingleState.observeAsState()
    val superUserState by controller.superUserSingleState.observeAsState()
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    LaunchedEffect(accountType) {
        if(accountType!=null){
            if(accountType==ViewType.HOSPITAL_USER){
                val saved = Preferences.User().get()
                saved?.let{controller.loginNormal(it.username,it.password)}
            }
            else if(accountType==ViewType.SUPER_USER){
                val saved = Preferences.User().getSuper()
                saved?.let{
                    controller.loginSuper(it.username,it.password)
                }
            }
        }
        else navHostController.navigate(LoginRoute.route)
    }
    when(hospitalUserState){
        is UiState.Loading->{loading=true;success=false;fail=false}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;success=true;fail=false
                val s = hospitalUserState as UiState.Success<HospitalUserSSResponse>
                val response = s.data
                val data = response.data
                navHostController.navigate(HospitalHomeRoute.route)
            }

        }
        is UiState.Error->{
            loading=false;success=false;fail=true
            navHostController.navigate(LoginRoute.route)
        }
        else->{}
    }
    when(superUserState){
        is UiState.Loading->{loading=true;success=false;fail=false}
        is UiState.Success->{
            loading=false;success=true;fail=false
            val s = superUserState as UiState.Success<SimpleSuperUserSingleResponse>
            val response = s.data
            val data = response.data
            LaunchedEffect(Unit) {
                if(data.isSuper) navHostController.navigate(AdminHomeRoute.route)
                else navHostController.navigate(HomeRoute.route)
            }
        }
        is UiState.Error->{
            loading=false;success=false;fail=true
            navHostController.navigate(LoginRoute.route)
        }

        else->{}
    }
    if(loading){
        LoadingScreen(modifier= Modifier.fillMaxSize())
    }
}
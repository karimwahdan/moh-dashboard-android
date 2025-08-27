package com.kwdevs.hospitalsdashboard.views.pages.user

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalUserPage(navHostController: NavHostController){
    val controller:UsersController= viewModel()
    val state by controller.fullHospitalUserSingleState.observeAsState()
    var user by remember { mutableStateOf<HospitalUser?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    var roles by remember { mutableStateOf<List<Role>>(emptyList()) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<HospitalUserSingleResponse>
            val r = s.data
            val data=r.data
            user=data
            roles=data.roles
        }
        else->{ controller.viewNormal() }
    }
    Container(
        title = "Account",
        showSheet = showSheet,
    ) {
        roles.forEach {
            Label(it.name)
            Row(modifier= Modifier.fillMaxWidth()){
                it.permissions.forEach { p->
                    Span(p.name?:"")
                    HorizontalSpacer()
                }
            }
        }
    }
}
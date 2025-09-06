package com.kwdevs.hospitalsdashboard.views.pages.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.UsersController
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MY_ACCOUNT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PINK
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalUserPage(navHostController: NavHostController){
    val createSlugColor= GREEN
    val browseSlugColor= BLUE
    val viewSlugColor= PINK
    val controller:UsersController= viewModel()
    val state by controller.fullHospitalUserSingleState.observeAsState()
    var user by remember { mutableStateOf<HospitalUser?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    var roles by remember { mutableStateOf<List<Role>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    when(state){
        is UiState.Loading->{LaunchedEffect(Unit){loading=true;fail=false;success=false}}
        is UiState.Error->{
            LaunchedEffect(Unit){
                loading=false;fail=true;success=false
                val s =state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: EMPTY_STRING
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit){
                loading=false;fail=false;success=true
                val s = state as UiState.Success<HospitalUserSingleResponse>
                val r = s.data
                val data=r.data
                user=data
                roles=data.roles

            }
        }
        else->{ controller.viewNormal() }
    }

    Container(
        title = MY_ACCOUNT_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                LazyRow {
                    items(roles){
                        ColumnContainer{
                            Label(it.name)
                            VerticalSpacer()
                            it.permissions.sortedBy { it.slug }.forEach { p->
                                Span(p.name?:EMPTY_STRING,
                                    backgroundColor = when{
                                        (p.slug?: EMPTY_STRING).startsWith("browse")->browseSlugColor
                                        (p.slug?: EMPTY_STRING).startsWith("view")->viewSlugColor
                                        (p.slug?: EMPTY_STRING).startsWith("create")->createSlugColor
                                        else->BLACK
                                    }, color = WHITE, paddingStart = 5, paddingEnd = 5, paddingTop = 5, paddingBottom = 5)
                                VerticalSpacer()
                            }
                        }
                    }
                }
            }
            if(fail){
                FailScreen(modifier=Modifier.fillMaxSize(),
                    errors = errors,
                    message = errorMessage)
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.pages.usersPage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.controller.AdminHospitalUserController
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.AdminHospitalUsersCreateRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.cards.SimpleHospitalUserCard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalUsersIndexPage(navHostController: NavHostController){
    val hospital=Preferences.Hospitals().get()
    val controller:AdminHospitalUserController= viewModel()
    val state by controller.paginatedState.observeAsState()
    var items by remember { mutableStateOf<List<SimpleHospitalUser>>(emptyList()) }
    val showSheet = remember { mutableStateOf(false) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }

    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<SimpleHospitalUser>>>
            val r = s.data
            val pagination=r.pagination
            lastPage=pagination.lastPage
            val data=pagination.data
            items=data
        }
        else->{
            controller.indexByHospital(currentPage.intValue)
        }
    }
    Container(
        title = "Hospital ${hospital?.name?:""} Users",
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(AdminHomeRoute.route)}
    ) {
        Row(modifier=Modifier.fillMaxWidth()){
            CustomButton(label= ADD_NEW_LABEL) {
                navHostController.navigate(AdminHospitalUsersCreateRoute.route)
            }
        }
        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = items.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(items){
                    SimpleHospitalUserCard(it,navHostController,controller)
                }
            }
        }
    }
}
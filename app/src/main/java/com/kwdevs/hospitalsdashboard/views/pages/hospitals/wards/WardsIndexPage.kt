package com.kwdevs.hospitalsdashboard.views.pages.hospitals.wards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalWardController
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.WardCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.WARDS_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.WardCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer
import com.kwdevs.hospitalsdashboard.views.rcs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalUserWardsIndexPage(navHostController: NavHostController){

    val controller:HospitalWardController= viewModel()
    val state by controller.paginationState.observeAsState()
    var wards by remember { mutableStateOf<List<HospitalWard>>(emptyList()) }
    val page = remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<HospitalWard>>>
            val r = s.data
            val pagination = r.pagination
            totalPages=pagination.lastPage
            wards= pagination.data
        }
        else->{
            LaunchedEffect(Unit) {
                controller.indexByHospital(page.intValue)

            }
        }
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = WARDS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                CustomButton(label = ADD_NEW_LABEL) { navHostController.navigate(WardCreateRoute.route) }
            }
            PaginationContainer(
                shape = rcs(5),
                currentPage = page,
                lastPage = totalPages,
                totalItems = wards.size
            ) {
                LazyColumn {
                    items(wards){
                        WardCard(it,navHostController)
                    }
                }
            }
        }
    }
}
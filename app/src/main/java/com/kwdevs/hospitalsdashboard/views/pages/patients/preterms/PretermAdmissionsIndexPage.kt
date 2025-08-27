package com.kwdevs.hospitalsdashboard.views.pages.patients.preterms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.PretermAdmissionsController
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmission
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.PRETERM_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.patients.PretermAdmissionCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PretermAdmissionsIndexPage(navHostController: NavHostController){

    val controller:PretermAdmissionsController= viewModel()
    val state by controller.admissionsState.observeAsState()
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    var items by remember { mutableStateOf<List<PretermAdmission>>(emptyList()) }

    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<PretermAdmission>>>
            val r = s.data
            val pagination=r.pagination
            val data=pagination.data
            lastPage=pagination.lastPage
            items=data
        }
        else->{controller.indexByHospital(currentPage.intValue)}
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = PRETERM_LABEL,
        headerShowBackButton = true,
        showSheet = showSheet,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                onClick = { navHostController.navigate(PretermAdmissionsCreateRoute.route) })
        }
        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = items.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(items){
                    PretermAdmissionCard(it)
                }
            }
        }
    }
}

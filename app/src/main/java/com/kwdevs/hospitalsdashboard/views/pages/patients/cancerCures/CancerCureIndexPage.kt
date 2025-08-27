package com.kwdevs.hospitalsdashboard.views.pages.patients.cancerCures

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
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.patients.CancerCureController
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCure
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.CancerCureStoreRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCER_CURE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.cards.patients.CancerCureCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancerCureIndexPage(navHostController: NavHostController){
    val controller:CancerCureController= viewModel()
    val state by controller.paginationState.observeAsState()
    var items by remember { mutableStateOf<List<CancerCure>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val viewType = Preferences.ViewTypes().get()
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<CancerCure>>>
            val r = s.data
            val paginationData=r.pagination
            lastPage=paginationData.lastPage
            items=paginationData.data
        }
        else->{
            when(viewType){
                ViewType.BY_HOSPITAL->{controller.indexByHospital(currentPage.intValue)}
                ViewType.BY_PATIENT->{controller.indexByPatient(currentPage.intValue)}
                else->{}
            }

        }
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(
        title = CANCER_CURE_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            when(viewType){
                ViewType.BY_PATIENT->{navHostController.navigate(PatientsIndexRoute.route)}
                else->{navHostController.navigate(HospitalHomeRoute.route)}
            }
        }
    ) {
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center){
            CustomButton(label = ADD_NEW_LABEL ,
                enabledBackgroundColor = BLUE,
                buttonShadowElevation = 6,
                onClick = { navHostController.navigate(CancerCureStoreRoute.route) })
        }
        PaginationContainer(
            currentPage = currentPage,
            lastPage = lastPage,
            totalItems = items.size
        ) {
            LazyColumn(modifier=Modifier.fillMaxSize()) {
                items(items){
                    CancerCureCard(it,navHostController)
                }
            }
        }
    }
}
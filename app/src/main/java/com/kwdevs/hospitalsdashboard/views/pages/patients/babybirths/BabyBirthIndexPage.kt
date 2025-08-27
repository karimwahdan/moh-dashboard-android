package com.kwdevs.hospitalsdashboard.views.pages.patients.babybirths

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.kwdevs.hospitalsdashboard.controller.patients.BabyBirthController
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirth
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.BabyBirthCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.PatientViewRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.cards.patients.BabyBirthCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBirthIndexPage(navHostController: NavHostController){
    var items by remember { mutableStateOf<List<BabyBirth>>(emptyList()) }
    val controller: BabyBirthController = viewModel()
    val state by controller.paginationState.observeAsState()
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<BabyBirth>>>
            val r = s.data
            val pagination=r.pagination
            lastPage=pagination.lastPage
            items=pagination.data
        }
        else->{ controller.indexByHospital(currentPage.intValue) }
    }
    val showSheet = remember { mutableStateOf(false) }
    val viewType=Preferences.ViewTypes().get()
    Container(
        title = BIRTHS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {
            navHostController.navigate(if(viewType==ViewType.BY_PATIENT)
                PatientViewRoute.route else HospitalHomeRoute.route)
        }
    ) {
        Column(modifier= Modifier.fillMaxSize()) {
            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label = ADD_NEW_LABEL ,
                    enabledBackgroundColor = BLUE,
                    buttonShadowElevation = 6,
                    onClick = {
                        navHostController.navigate(BabyBirthCreateRoute.route) })
            }
            PaginationContainer(
                currentPage=currentPage,
                lastPage = lastPage,
                totalItems = items.size
            ) {
                LazyColumn(modifier=Modifier.fillMaxSize()) {
                    items(items){
                        BabyBirthCard(it,navHostController)
                    }
                }

            }
        }
    }
}
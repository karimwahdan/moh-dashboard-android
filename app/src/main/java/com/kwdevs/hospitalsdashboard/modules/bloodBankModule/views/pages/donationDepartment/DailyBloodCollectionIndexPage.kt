package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.donationDepartment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.donationDepartment.BloodDonationStatisticsCard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_BLOOD_COLLECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBloodCollectionIndexPage(navHostController: NavHostController){
    val controller:BloodBankController= viewModel()
    val state           by controller.dailyBloodCollectionsPaginationState.observeAsState()
    var items           by remember { mutableStateOf<List<DailyBloodCollection>>(emptyList()) }
    var lastPage        by remember { mutableIntStateOf(1) }
    var loading         by remember { mutableStateOf(false) }
    var success         by remember { mutableStateOf(false) }
    var fail            by remember { mutableStateOf(false) }
    var errors          by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage    by remember { mutableStateOf(EMPTY_STRING) }
    val currentPage     =  remember { mutableIntStateOf(1) }
    val showSheet       =  remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{ LaunchedEffect(Unit) { loading=true;fail=false;success=false } }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;fail=true;success=false
                val s = state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL

            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true

                val s = state as UiState.Success<ApiResponse<PaginationData<DailyBloodCollection>>>
                val r = s.data
                val paginationData=r.pagination
                lastPage=paginationData.lastPage
                items = paginationData.data
            }

        }
        else->{ controller.indexBloodCollectionsByHospital(currentPage.intValue) }
    }
    LaunchedEffect(currentPage.intValue) {controller.indexBloodCollectionsByHospital(currentPage.intValue)}

    Container(
        title = DAILY_BLOOD_COLLECTION_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(DonationDepartmentHomeRoute.route)}
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                VerticalSpacer()
                CustomButton(label= ADD_NEW_LABEL, buttonShadowElevation = 6, buttonShape = RectangleShape,
                    enabledBackgroundColor = BLUE) {
                    navHostController.navigate(DailyBloodCollectionCreateRoute.route)
                }
                HorizontalSpacer()
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        item{
                            BloodDonationStatisticsCard(items)
                            VerticalSpacer()

                        }
                    }

                }

            }
            if(fail) FailScreen(modifier = Modifier.fillMaxSize(), errors = errors, message = errorMessage)
        }
    }
    BackHandler {
        navHostController.navigate(BloodBankHomeRoute.route)
    }
}
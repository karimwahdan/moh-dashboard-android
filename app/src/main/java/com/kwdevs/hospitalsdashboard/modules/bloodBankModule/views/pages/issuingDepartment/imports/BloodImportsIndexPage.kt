package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.imports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.bloodImport.BloodImportHorizontalCard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THE_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IMPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.QUANTITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodImportsIndexPage(navHostController: NavHostController){

    val controller:BloodBankIssuingDepartmentController= viewModel()
    val state by controller.bloodImportsPaginationState.observeAsState()
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var items by remember { mutableStateOf<List<BloodImport>>(emptyList()) }
    val showSheet = remember { mutableStateOf(false) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {loading=true;success=false;fail=false }
        }
        is UiState.Error->{
            LaunchedEffect(Unit) {
                loading=false;success=false;fail=true
                val s = state as UiState.Error
                val exception=s.exception
                errors=exception.errors?: emptyMap()
                errorMessage=exception.message?: ERROR_LOADING_DATA_LABEL
            }
        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;success=true;fail=false
                val s = state as UiState.Success<ApiResponse<PaginationData<BloodImport>>>
                val response =s.data
                val pagination=response.pagination
                lastPage=pagination.lastPage
                items=pagination.data
            }
        }
        else->{
            LaunchedEffect(Unit) {
                controller.indexBloodImports(currentPage.intValue)
            }
        }
    }
    LaunchedEffect(Unit) {  }
    Container(
        title= IMPORTS_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(IssuingDepartmentHomeRoute.route)}) {
        VerticalSpacer()
        CustomButton(label=ADD_NEW_LABEL, enabledBackgroundColor = BLUE,
            buttonShadowElevation = 6, buttonShape = RectangleShape) {navHostController.navigate(BloodImportCreateRoute.route)}
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else{
            if(success){
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = items.size
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        item{
                            Row(modifier=Modifier.fillMaxWidth()){
                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                    contentAlignment = Alignment.Center){
                                    Label(THE_HOSPITAL_LABEL)
                                }
                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                    contentAlignment = Alignment.Center){
                                    Label(UNIT_LABEL)

                                }
                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                    contentAlignment = Alignment.Center){
                                    Label(DATE_LABEL)
                                }
                                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                                    contentAlignment = Alignment.Center){
                                    Label(QUANTITY_LABEL)
                                }
                            }
                            items.forEach {
                                BloodImportHorizontalCard(it)
                            }
                        }
                    }
                }
            }
        }
    }
}
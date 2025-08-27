package com.kwdevs.hospitalsdashboard.views.pages.hospitals.operationRooms

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.hospital.OperationRoomController
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.OPERATION_ROOMS_LABEL
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.OperationRoomCard
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationRoomsIndexPage(navHostController: NavHostController){
    var items by remember { mutableStateOf<List<OperationRoom>>(emptyList()) }
    val controller:OperationRoomController= viewModel()
    val state by controller.paginationState.observeAsState()
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{}
        is UiState.Success->{
            val s = state as UiState.Success<ApiResponse<PaginationData<OperationRoom>>>
            val r = s.data
            val pagination=r.pagination
            lastPage=pagination.lastPage
            val data=pagination.data
            items=data
        }
        else->{controller.indexByHospital(currentPage.intValue)}
    }
    val showSheet = remember { mutableStateOf(false) }

    Container(title = OPERATION_ROOMS_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        showSheet = showSheet,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}) {
        Column(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CustomButton(label= ADD_NEW_LABEL, enabledBackgroundColor = BLUE, buttonShape = RectangleShape, buttonShadowElevation = 6) {
                    navHostController.navigate(OperationRoomCreateRoute.route)
                }
            }
            PaginationContainer(
                currentPage=currentPage,
                lastPage = lastPage,
                totalItems = items.size
            ) {
                LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)) {
                    items(items){ OperationRoomCard(it,navHostController) }
                }
            }
        }
    }

}
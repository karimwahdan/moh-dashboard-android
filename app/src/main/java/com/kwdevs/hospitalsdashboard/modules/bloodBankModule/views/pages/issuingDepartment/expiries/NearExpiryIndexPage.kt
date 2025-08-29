package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.expiries

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodNearExpiredSourceFilter
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodNearExpiredController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.MyBloodNearExpiryCard
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment.OtherBloodNearExpiryCard
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BoxContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.LOAD_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NEAR_EXPIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OTHER_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THIS_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_SOURCE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.assets.container.PaginationContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearExpiryIndexPage(navHostController: NavHostController){
    val controller:BloodNearExpiredController= viewModel()
    val state by controller.paginationState.observeAsState()
    val searchList = listOf(Pair(BloodNearExpiredSourceFilter.MY_BLOOD_BANK, THIS_HOSPITAL_LABEL),
        Pair(BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS, OTHER_HOSPITAL_LABEL))
    val selectedSource = remember { mutableStateOf<Pair<BloodNearExpiredSourceFilter,String>?>(searchList[0]) }
    var otherItems  by remember { mutableStateOf<List<BloodNearExpiredItem>>(emptyList()) }
    var myItems     by remember { mutableStateOf<List<BloodNearExpiredItem>>(emptyList()) }
    val currentPage = remember { mutableIntStateOf(1) }
    var lastPage by remember { mutableIntStateOf(1) }
    val showSheet = remember { mutableStateOf(false ) }
    var loading by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf<Map<String,List<String>>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf(EMPTY_STRING) }
    when(state){
        is UiState.Loading->{
            LaunchedEffect(Unit) {
                loading=true;fail=false;success=false
            }
        }
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
                val s = state as UiState.Success<ApiResponse<PaginationData<BloodNearExpiredItem>>>
                val r = s.data
                val pagination=r.pagination
                lastPage= pagination.lastPage
                when(selectedSource.value?.first){
                    BloodNearExpiredSourceFilter.MY_BLOOD_BANK->{myItems=pagination.data}
                    BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS->{otherItems=pagination.data}
                    else->{}
                }
            }
        }
        else->{}
    }
    Container(
        title = NEAR_EXPIRED_LABEL,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(IssuingDepartmentHomeRoute.route)},
        showSheet=showSheet,
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        else {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)){
                Column(modifier=Modifier.fillMaxWidth().padding( 5.dp)){
                    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.Center) {
                        CustomButtonWithImage(
                            icon = R.drawable.ic_view_timeline_blue,
                            label = LOAD_DATA_LABEL,
                            enabled = selectedSource.value!=null,
                            maxWidth = 82, maxLines = 2) {
                            when(selectedSource.value?.first){
                                BloodNearExpiredSourceFilter.MY_BLOOD_BANK->{controller.indexMine()}
                                BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS->{controller.indexOther()}
                                else->{}
                            }
                        }
                        HorizontalSpacer(10)
                        CustomButtonWithImage(icon = R.drawable.ic_add_circle_green,
                            label = ADD_NEW_LABEL,
                            maxWidth = 82, maxLines = 2) {
                            Preferences.BloodBanks.NearExpiredBloodUnits().delete()
                            navHostController.navigate(NearExpiredCreateRoute.route)
                        }
                    }
                    VerticalSpacer()
                    if(selectedSource.value==null) {Label(SELECT_UNIT_SOURCE_LABEL, color = Color.Red)}
                    BoxContainer(hasBorder = false) {
                        ComboBox(
                            title = UNIT_SOURCE_LABEL,
                            hasTitle = true,
                            loadedItems = searchList,
                            selectedItem = selectedSource,
                            selectedContent = { CustomInput(selectedSource.value?.second?: SELECT_UNIT_SOURCE_LABEL)}
                        ) {
                            Label(it?.second?: EMPTY_STRING)
                        }
                    }
                    VerticalSpacer()
                }
            }
            if(success){
                PaginationContainer(
                    currentPage = currentPage,
                    lastPage = lastPage,
                    totalItems = if(selectedSource.value?.first==BloodNearExpiredSourceFilter.MY_BLOOD_BANK)myItems.size else if(selectedSource.value?.first==BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS) otherItems.size else 0
                ) {
                    LazyColumn(modifier=Modifier.fillMaxSize()) {
                        items(if(selectedSource.value?.first==BloodNearExpiredSourceFilter.MY_BLOOD_BANK)myItems else if(selectedSource.value?.first==BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS) otherItems else emptyList()){
                            if(selectedSource.value?.first==BloodNearExpiredSourceFilter.MY_BLOOD_BANK) MyBloodNearExpiryCard(it,navHostController)
                            else if(selectedSource.value?.first==BloodNearExpiredSourceFilter.OTHER_BLOOD_BANKS) OtherBloodNearExpiryCard(it)
                        }
                    }
                }
            }
            if(fail){
                FailScreen(modifier=Modifier.fillMaxSize())
            }
        }
    }
}
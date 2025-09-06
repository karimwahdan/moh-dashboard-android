package com.kwdevs.hospitalsdashboard.views.pages.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.controller.HomeController
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.Directorate
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PermissionSector
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasDirectoratePermission
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasPermission
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.CertainDirectorateCollectiveBBKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.CurativeBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.DirectoratesBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.EducationalBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.InsuranceBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.NBTSBloodBankKpiSection
import com.kwdevs.hospitalsdashboard.views.pages.home.homeCharts.bloodBankModule.general.SpecializedBloodBankKpiSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperBloodKpiPage(navHostController: NavHostController) {
    val context= LocalContext.current
    val superUser= Preferences.User().getSuper()

    val canViewDirectorate =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.DIRECTORATE_SECTOR)?:false
    val canViewSpecialized =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.SPECIALIZED_SECTOR)?:false
    val canViewEducational =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.EDUCATIONAL_SECTOR)?:false
    val canViewNBTS =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.NBTS_SECTOR)?:false
    val canViewInsurance =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.INSURANCE_SECTOR)?:false
    val canViewCurative =superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.CURATIVE_SECTOR)?:false
    val canViewCertainDirectorate=superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.CERTAIN_DIRECTORATE)?:false
    val canViewAllDirectorates=superUser?.hasDirectoratePermission(CRUD.VIEW, Directorate.ALL)?:false

    val controller : HomeController = viewModel()
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = KPI_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerOnClick = {navHostController.navigate(HomeRoute.route)},
        headerIconButtonBackground = BLUE
    ) {
        VerticalSpacer(10)
        LazyColumn(modifier=Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            item{
                VerticalSpacer(10)
                if(canViewAllDirectorates){
                    HorizontalDivider()
                    VerticalSpacer()
                    DirectoratesBloodBankKpiSection(controller)
                }
                else if(canViewCertainDirectorate) CertainDirectorateCollectiveBBKpiSection(controller)
                if(canViewSpecialized) {HorizontalDivider();VerticalSpacer();SpecializedBloodBankKpiSection(controller)}
                if(canViewEducational) {HorizontalDivider();VerticalSpacer();EducationalBloodBankKpiSection(controller)}
                if(canViewNBTS) {HorizontalDivider();VerticalSpacer();NBTSBloodBankKpiSection(controller)}
                if(canViewInsurance) {HorizontalDivider();VerticalSpacer();InsuranceBloodBankKpiSection(controller)}
                if(canViewCurative) {HorizontalDivider();VerticalSpacer();CurativeBloodBankKpiSection(controller)}

            }
        }

    }


    ColumnContainer {

    }

}
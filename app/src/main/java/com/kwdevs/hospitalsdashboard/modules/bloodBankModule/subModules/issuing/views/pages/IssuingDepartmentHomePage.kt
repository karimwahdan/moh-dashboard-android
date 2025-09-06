package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.issuing.views.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredIndexRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodExports
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodImports
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodIssuingQualityKpis
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodNearExpired
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodStocks
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseIssuingIncineration
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodExports
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodImports
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodNearExpired
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodStocks
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewIssuingIncineration
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewIssuingQualityKpis
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasIssuingIncineration
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodStockIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalViewRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EXPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IMPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEAR_EXPIRED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuingDepartmentHomePage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val userType=Preferences.User().getType()
    val hospital=Preferences.Hospitals().get()
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = DEPARTMENT_ISSUING_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {
            when(userType){
                ViewType.HOSPITAL_USER->{navHostController.navigate(BloodBankHomeRoute.route)}
                ViewType.SUPER_USER->{navHostController.navigate(HospitalViewRoute.route)}
                else->{navHostController.navigate(LoginRoute.route)}
            }
        }
    ) {

        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = EXPORTS_LABEL,
                enabled = user.canBrowseBloodExports() || superUser.canViewBloodExports() ,
                icon = R.drawable.ic_report, maxWidth = 82,
                maxLines = 2) {
                navHostController.navigate(MonthlyIssuingReportsIndexRoute.route)
            }

            CustomButtonWithImage(label = KPI_LABEL,
                enabled = user.canBrowseBloodIssuingQualityKpis() || superUser.canViewIssuingQualityKpis(),
                icon = R.drawable.ic_chart, maxWidth = 122,
                maxLines = 3) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(BloodBankKpiIndexRoute.route)
            }
            CustomButtonWithImage(label = INCINERATION_LABEL,
                enabled=hospital.hasIssuingIncineration() &&
                        ( user.canBrowseIssuingIncineration() || superUser.canViewIssuingIncineration()),
                maxLines = 2,
                icon = R.drawable.ic_incineration, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(IncinerationIndexRoute.route)
            }
        }
        Row(modifier= Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = IMPORTS_LABEL,
                enabled =user.canBrowseBloodImports() || superUser.canViewBloodImports(),
                icon = R.drawable.ic_ambulance, maxWidth = 82) {
                navHostController.navigate(BloodImportIndexRoute.route)
            }
            CustomButtonWithImage(label = NEAR_EXPIRED_LABEL,
                enabled = user.canBrowseBloodNearExpired() || superUser.canViewBloodNearExpired() ,
                maxLines = 2,icon = R.drawable.ic_near_expiry, maxWidth = 82) {
                navHostController.navigate(NearExpiredIndexRoute.route)
            }
            CustomButtonWithImage(label = STOCK_LABEL,
                enabled = user.canBrowseBloodStocks() || superUser.canViewBloodStocks() ,
                icon = R.drawable.ic_blood_drop, maxWidth = 82) {
                navHostController.navigate(BloodStockIndexRoute.route)
            }

        }
    }
}
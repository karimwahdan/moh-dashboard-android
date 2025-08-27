package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_INCINERATION_MODULE
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredIndexRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_ISSUING_DAILY_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_ISSUING_EXPORTS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_ISSUING_IMPORTS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_ISSUING_INCINERATION
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_ISSUING_KPIS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_NEAR_EXPIRY
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodStockIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
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

    var moduleSlugs by remember { mutableStateOf<List<String>>(emptyList()) }
    val hasIncineration=moduleSlugs.any{it==BLOOD_BANK_INCINERATION_MODULE}
    val hospital = Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val roles=user?.roles?: emptyList()
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }
    val canBrowseStocks=permissions.any { it==BROWSE_BB_ISSUING_DAILY_STOCKS }
    val canBrowseExports=permissions.any { it==BROWSE_BB_ISSUING_EXPORTS }
    val canBrowseKpis=permissions.any { it== BROWSE_BB_ISSUING_KPIS }
    val canBrowseIncineration=permissions.any { it==BROWSE_BB_ISSUING_INCINERATION }
    val canBrowseImports=permissions.any { it==BROWSE_BB_ISSUING_IMPORTS }
    val canBrowseNearExpiry=permissions.any { it==BROWSE_BB_NEAR_EXPIRY }
    LaunchedEffect(Unit) {
        hospital?.let { it ->
            moduleSlugs=it.modules.map { it.slug?: EMPTY_STRING }
        }
    }
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = DEPARTMENT_ISSUING_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(BloodBankHomeRoute.route)}
    ) {

        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = EXPORTS_LABEL,
                enabled = canBrowseExports,
                icon = R.drawable.ic_report, maxWidth = 82,
                maxLines = 2) {
                navHostController.navigate(MonthlyIssuingReportsIndexRoute.route)
            }

            CustomButtonWithImage(label = KPI_LABEL,
                enabled = canBrowseKpis,
                icon = R.drawable.ic_chart, maxWidth = 122,
                maxLines = 3) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(BloodBankKpiIndexRoute.route)
            }
            CustomButtonWithImage(label = INCINERATION_LABEL,
                enabled = hasIncineration && canBrowseIncineration,
                maxLines = 2,
                icon = R.drawable.ic_incineration, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(IncinerationIndexRoute.route)
            }

        }
        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = IMPORTS_LABEL,
                enabled = canBrowseImports,
                icon = R.drawable.ic_ambulance, maxWidth = 82) {
                navHostController.navigate(BloodImportIndexRoute.route)
            }
            CustomButtonWithImage(label = NEAR_EXPIRED_LABEL,
                enabled = canBrowseNearExpiry,
                maxLines = 2,
                icon = R.drawable.ic_near_expiry, maxWidth = 82) {
                navHostController.navigate(NearExpiredIndexRoute.route)
            }
            CustomButtonWithImage(label = STOCK_LABEL,
                enabled = canBrowseStocks,
                icon = R.drawable.ic_blood_drop, maxWidth = 82) {
                navHostController.navigate(BloodStockIndexRoute.route)
            }

        }
    }
}
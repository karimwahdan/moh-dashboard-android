package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment

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
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DailyBloodProcessingIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationIndexRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_COMPONENT_DAILY_PROCESSING
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_COMPONENT_INCINERATION
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_COMPONENT_KPIS
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DAILY_PROCESSING_REPORTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_COMPONENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.INCINERATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentDepartmentHomePage(navHostController: NavHostController){
    var moduleSlugs by remember { mutableStateOf<List<String>>(emptyList()) }
    val hasIncineration=moduleSlugs.any{it==BLOOD_BANK_INCINERATION_MODULE}

    val hospital = Preferences.Hospitals().get()
    val user=Preferences.User().get()
    val roles=user?.roles?: emptyList()
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }

    val canBrowseDailyProcessing=permissions.any{it==BROWSE_BB_COMPONENT_DAILY_PROCESSING}
    val canBrowseIncineration=permissions.any { it==BROWSE_BB_COMPONENT_INCINERATION }
    val canBrowseKpis=permissions.any { it== BROWSE_BB_COMPONENT_KPIS }

    LaunchedEffect(Unit) { hospital?.let { it -> moduleSlugs=it.modules.map { it.slug?: EMPTY_STRING } } }
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = DEPARTMENT_COMPONENT_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(BloodBankHomeRoute.route)}
    ) {
        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){

            CustomButtonWithImage(label = DAILY_PROCESSING_REPORTS_LABEL,
                icon = R.drawable.ic_blood_drop,
                enabled = canBrowseDailyProcessing,
                maxWidth = 122,
                maxLines = 3) {
                navHostController.navigate(DailyBloodProcessingIndexRoute.route)
            }
            CustomButtonWithImage(label = KPI_LABEL,
                enabled = canBrowseKpis,
                icon = R.drawable.ic_chart, maxWidth = 122,
                maxLines = 3) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.COMPONENT_DEPARTMENT)
                navHostController.navigate(BloodBankKpiIndexRoute.route)
            }

            CustomButtonWithImage(label = INCINERATION_LABEL,
                enabled = hasIncineration && canBrowseIncineration,
                icon = R.drawable.ic_incineration, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.COMPONENT_DEPARTMENT)
                navHostController.navigate(IncinerationIndexRoute.route)
            }

        }
    }
}
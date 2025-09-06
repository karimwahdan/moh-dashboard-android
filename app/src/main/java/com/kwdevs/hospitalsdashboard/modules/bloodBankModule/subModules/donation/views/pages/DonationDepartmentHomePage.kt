package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.donation.views.pages

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
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiIndexRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_DONATION_CAMPAIGNS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BROWSE_BB_DONATION_KPIS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseBloodCollection
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canBrowseDonationQualityKpis
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewBloodCollection
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.canViewDonationQualityKpis
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGNS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DONATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.KPI_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationDepartmentHomePage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val superUser= Preferences.User().getSuper()
    val roles=user?.roles?: emptyList()
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }

    val canBrowseKpis=permissions.any { it== BROWSE_BB_DONATION_KPIS }
    val canBrowseCampaigns=permissions.any { it==BROWSE_BB_DONATION_CAMPAIGNS }

    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = DEPARTMENT_DONATION_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(BloodBankHomeRoute.route)}
    ) {

        VerticalSpacer()
        Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = CAMPAIGNS_LABEL,
                enabled = user.canBrowseBloodCollection() || superUser.canViewBloodCollection(),
                icon = R.drawable.ic_report, maxWidth = 122,
                maxLines = 2) {
                navHostController.navigate(DailyBloodCollectionIndexRoute.route)
            }

            CustomButtonWithImage(label = KPI_LABEL,
                enabled = user.canBrowseDonationQualityKpis() || superUser.canViewDonationQualityKpis(),
                icon = R.drawable.ic_chart, maxWidth = 122,
                maxLines = 3) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.DONATION_DEPARTMENT)
                navHostController.navigate(BloodBankKpiIndexRoute.route)
            }

        }
        VerticalSpacer()

    }
}
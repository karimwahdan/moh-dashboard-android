package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages

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
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_COMPONENT_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_DONATION_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_ISSUING_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_SEROLOGY_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.BLOOD_BANK_THERAPEUTIC_DEPARTMENT_MODULE
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_COMPONENT
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_DONATION
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_ISSUING
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_SEROLOGY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_THERAPEUTIC
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_COMPONENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DONATION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_ISSUING_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_SEROLOGY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_THERAPEUTIC_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankHomePage(navHostController: NavHostController){
    var moduleSlugs by remember { mutableStateOf<List<String>>(emptyList()) }
    val user=Preferences.User().get()
    val roles=user?.roles?: emptyList()
    val activeComponentModule=moduleSlugs.contains(BLOOD_BANK_COMPONENT_DEPARTMENT_MODULE)
    val activeDonationModule=moduleSlugs.contains(BLOOD_BANK_DONATION_DEPARTMENT_MODULE)
    val activeSerologyModule=moduleSlugs.contains(BLOOD_BANK_SEROLOGY_DEPARTMENT_MODULE)
    val activeIssuingModule=moduleSlugs.contains(BLOOD_BANK_ISSUING_DEPARTMENT_MODULE)
    val activeTherapeuticModule=moduleSlugs.contains(BLOOD_BANK_THERAPEUTIC_DEPARTMENT_MODULE)
    val permissions=roles.flatMap { it -> it.permissions.map { it.slug } }
    val canViewIssuing=permissions.find{it==VIEW_ISSUING}!=null
    val canViewComponent=permissions.find{it== VIEW_COMPONENT}!=null
    val canViewDonation=permissions.find{it== VIEW_DONATION}!=null
    val canViewSerology=permissions.find{it== VIEW_SEROLOGY}!=null
    val canViewTherapeutic=permissions.find{it== VIEW_THERAPEUTIC}!=null
    val hospital = Preferences.Hospitals().get()
    LaunchedEffect(Unit) { hospital?.let { it -> moduleSlugs=it.modules.map { it.slug?: EMPTY_STRING } } }
    val showSheet = remember { mutableStateOf(false) }
    Container(
        title = BLOOD_BANK_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HospitalHomeRoute.route)}
    ) {
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){

            CustomButtonWithImage(label =DEPARTMENT_DONATION_LABEL,
                enabled = activeDonationModule && canViewDonation,
                icon = R.drawable.ic_blood_bag2, maxWidth = 82) {
                navHostController.navigate(DonationDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_COMPONENT_LABEL,
                enabled = activeComponentModule && canViewComponent,
                icon = R.drawable.ic_blood_drop, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.COMPONENT_DEPARTMENT)
                navHostController.navigate(ComponentDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_SEROLOGY_LABEL,
                enabled = activeSerologyModule && canViewSerology,
                icon = R.drawable.ic_serology, maxWidth = 82) {
                navHostController.navigate(DailyBloodCollectionIndexRoute.route)
            }
        }
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = DEPARTMENT_ISSUING_LABEL,
                enabled = activeIssuingModule && canViewIssuing,
                icon = R.drawable.ic_heart, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(IssuingDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_THERAPEUTIC_LABEL,
                enabled = activeTherapeuticModule && canViewTherapeutic,
                icon = R.drawable.ic_hand_drop, maxWidth = 82) {
                navHostController.navigate(DailyBloodCollectionIndexRoute.route)
            }


        }

    }
}
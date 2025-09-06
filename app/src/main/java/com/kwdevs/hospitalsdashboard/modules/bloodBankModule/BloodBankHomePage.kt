package com.kwdevs.hospitalsdashboard.modules.bloodBankModule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CRUD
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SubModule
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasSubModule
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.hasSubModulePermission
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
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankHomePage(navHostController: NavHostController){
    val user=Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    val hospital = Preferences.Hospitals().get()

    val drawerState         =  rememberDrawerState(initialValue = DrawerValue.Closed)

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
                enabled = hospital?.hasSubModule(SubModule.BLOOD_DONATION)?:false &&
                (user?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_DONATION)?:false ||
                        superUser?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_DONATION)?:false),
                icon = R.drawable.ic_blood_bag2, maxWidth = 82) {
                navHostController.navigate(DonationDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_COMPONENT_LABEL,
                enabled = hospital?.hasSubModule(SubModule.BLOOD_COMPONENT)?:false &&
                        (user?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_COMPONENT)?:false ||
                                superUser?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_COMPONENT)?:false),
                icon = R.drawable.ic_blood_drop, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.COMPONENT_DEPARTMENT)
                navHostController.navigate(ComponentDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_SEROLOGY_LABEL,
                enabled = hospital?.hasSubModule(SubModule.BLOOD_SEROLOGY)?:false &&
                        (user?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_SEROLOGY)?:false ||
                                superUser?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_SEROLOGY)?:false),
                icon = R.drawable.ic_serology, maxWidth = 82) {
                navHostController.navigate(DailyBloodCollectionIndexRoute.route)
            }
        }
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(label = DEPARTMENT_ISSUING_LABEL,
                enabled = hospital?.hasSubModule(SubModule.BLOOD_ISSUING)?:false &&
                        (user?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_ISSUING)?:false ||
                                superUser?.hasSubModulePermission(CRUD.VIEW,SubModule.BLOOD_ISSUING)?:false),
                icon = R.drawable.ic_heart, maxWidth = 82) {
                Preferences.BloodBanks.Departments().set(BloodBankDepartment.ISSUING_DEPARTMENT)
                navHostController.navigate(IssuingDepartmentHomeRoute.route)
            }
            CustomButtonWithImage(label = DEPARTMENT_THERAPEUTIC_LABEL,
                enabled = hospital?.hasSubModule(SubModule.THERAPEUTIC_UNIT)?:false &&
                        (user?.hasSubModulePermission(CRUD.VIEW,SubModule.THERAPEUTIC_UNIT)?:false ||
                                superUser?.hasSubModulePermission(CRUD.VIEW,SubModule.THERAPEUTIC_UNIT)?:false),
                icon = R.drawable.ic_hand_drop, maxWidth = 82) {
                navHostController.navigate(DailyBloodCollectionIndexRoute.route)
            }


        }

    }
}
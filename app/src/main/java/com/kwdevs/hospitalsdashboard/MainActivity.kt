package com.kwdevs.hospitalsdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kwdevs.hospitalsdashboard.modules.adminModule.views.pages.AdminHomePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankKpiIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodImportIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ComponentDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DailyBloodProcessingCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DailyBloodProcessingIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DonationDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.IncinerationIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MonthlyIssuingReportsIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredCreateRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredIndexRoute
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.BloodBankHomePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment.ComponentDepartmentHomePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment.DailyBloodProcessingCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.componentDepartment.DailyBloodProcessingIndexPage
import com.kwdevs.hospitalsdashboard.routes.AdmissionCreateRoute
import com.kwdevs.hospitalsdashboard.routes.AreaViewRoute
import com.kwdevs.hospitalsdashboard.routes.BabyBirthCreateRoute
import com.kwdevs.hospitalsdashboard.routes.BabyBirthIndexRoute
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.CancerCureStoreRoute
import com.kwdevs.hospitalsdashboard.routes.CitiesRoute
import com.kwdevs.hospitalsdashboard.routes.CityViewRoute
import com.kwdevs.hospitalsdashboard.routes.ClinicsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceUsagesRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalDevicesRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalHomeRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalUserViewRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsStoreRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsViewRoute
import com.kwdevs.hospitalsdashboard.routes.IncubatorIndexRoute
import com.kwdevs.hospitalsdashboard.routes.LabTestIndexRoute
import com.kwdevs.hospitalsdashboard.routes.LandingRoute
import com.kwdevs.hospitalsdashboard.routes.LoginRoute
import com.kwdevs.hospitalsdashboard.routes.MorgueCreateRoute
import com.kwdevs.hospitalsdashboard.routes.MorguesIndexRoute
import com.kwdevs.hospitalsdashboard.routes.NationalIdScannerRoute
import com.kwdevs.hospitalsdashboard.routes.NormalUserWardsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationCreateRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomCreateRoute
import com.kwdevs.hospitalsdashboard.routes.OperationRoomIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientViewRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PhysicalTherapyIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.ReceptionBedsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.RenalDevicesIndexRoute
import com.kwdevs.hospitalsdashboard.routes.UserControlRoute
import com.kwdevs.hospitalsdashboard.routes.WardCreateRoute
import com.kwdevs.hospitalsdashboard.ui.theme.HospitalsDashboardTheme
import com.kwdevs.hospitalsdashboard.views.RIGHT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.pages.LandingPage
import com.kwdevs.hospitalsdashboard.views.pages.basics.areas.AreaViewPage
import com.kwdevs.hospitalsdashboard.views.pages.basics.cities.CitiesPage
import com.kwdevs.hospitalsdashboard.views.pages.basics.cities.CityViewPage
import com.kwdevs.hospitalsdashboard.views.pages.home.HomeScreen
import com.kwdevs.hospitalsdashboard.views.pages.hospitalHome.HospitalHomePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.HospitalViewPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.HospitalsIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.donationDepartment.DailyBloodCollectionCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.donationDepartment.DailyBloodCollectionIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.donationDepartment.DonationDepartmentHomePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.stocks.BloodStockCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.stocks.BloodStockIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.IssuingDepartmentHomePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.incineration.IncinerationCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.incineration.IncinerationIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.expiries.NearExpiryCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.expiries.NearExpiryIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.imports.BloodImportCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.imports.BloodImportsIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.reports.monthlyIssuingReports.MonthlyIssuingReportsCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.issuingDepartment.reports.monthlyIssuingReports.MonthlyIssuingReportsIndexPage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.kpis.KpiCreatePage
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.pages.kpis.KpiIndexPage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createBloodBank.AdminCreateBloodBankPage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createPage.HospitalGeneralCreatePage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createUser.CreateUserPage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.moduleSelectorPages.HospitalModuleSelectorPage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.AdminCreateBloodBankRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.CreateUserRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalGeneralCreateRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.AdminHospitalUsersCreateRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.AdminHospitalUsersIndexRoute
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.pages.usersPage.HospitalUsersCreatePage
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.views.pages.usersPage.HospitalUsersIndexPage
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodBankHomeRoute
import com.kwdevs.hospitalsdashboard.routes.BloodStockCreateRoute
import com.kwdevs.hospitalsdashboard.routes.BloodStockIndexRoute
import com.kwdevs.hospitalsdashboard.routes.ChangePasswordRoute
import com.kwdevs.hospitalsdashboard.routes.DailyBloodCollectionIndexRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalModuleSelectorRoute
import com.kwdevs.hospitalsdashboard.routes.IssuingDepartmentHomeRoute
import com.kwdevs.hospitalsdashboard.routes.TestRoute
import com.kwdevs.hospitalsdashboard.views.assets.container.Test
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.clinics.ClinicsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.crud.HospitalCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices.HospitalDevicesPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices.HospitalDevicesUsagePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.hospitalDevices.crud.HospitalDeviceCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.incubators.IncubatorsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.labTests.LabTestsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.morgues.MorgueCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.morgues.MorgueIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.operationRooms.OperationRoomCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.operationRooms.OperationRoomsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.preterms.PretermAdmissionsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.receptionBeds.ReceptionBedsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.renalDevices.RenalDevicesIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.wards.NormalUserWardsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.hospitals.wards.WardCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.nationalIdScanner.NationalIdScannerPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.HospitalPatientsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.PatientCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.patients.PatientViewPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.admissions.PatientWardAdmissionCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.patients.admissions.PatientWardAdmissionsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.babybirths.BabyBirthCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.patients.babybirths.BabyBirthIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.cancerCures.CancerCureCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.patients.cancerCures.CancerCureIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.operations.OperationCreatePage
import com.kwdevs.hospitalsdashboard.views.pages.patients.operations.OperationsIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.patients.preterms.PretermAdmissionCreatePage
import com.kwdevs.hospitalsdashboard.modules.physicalTherapyModule.PhysicalTherapyIndexPage
import com.kwdevs.hospitalsdashboard.views.pages.user.ChangePasswordPage
import com.kwdevs.hospitalsdashboard.views.pages.user.HospitalUserPage
import com.kwdevs.hospitalsdashboard.views.pages.user.login.LoginPage
import com.kwdevs.hospitalsdashboard.views.pages.user.userControl.UserControlPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navHostController= rememberNavController()
            val context = LocalContext.current
            HospitalsDashboardTheme {
                CompositionLocalProvider(RIGHT_LAYOUT_DIRECTION){
                    Surface(
                        modifier = Modifier.fillMaxSize().statusBarsPadding()
                            .navigationBarsPadding().padding(5.dp).imePadding(),
                        color = Color.White,
                    ){
                        Column(modifier=Modifier.fillMaxSize()){
                            Box(modifier=Modifier.fillMaxSize().weight(1f)){
                                View(navHostController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun View(navHostController: NavHostController) {
    NavHost(navController = navHostController,
        startDestination =LandingRoute.route //TestRoute.route
    ) {
        //Total (36 pages)
        composable(TestRoute.route){ Test() }

        //A (2)
        composable(AreaViewRoute.route){ AreaViewPage(navHostController) }
        composable(AdmissionCreateRoute.route) { PatientWardAdmissionCreatePage(navHostController) }
        composable(AdminHomeRoute.route) { AdminHomePage(navHostController) }
        composable(AdminHospitalUsersIndexRoute.route) { HospitalUsersIndexPage(navHostController) }
        composable(AdminHospitalUsersCreateRoute.route) { HospitalUsersCreatePage(navHostController) }
        composable(AdminCreateBloodBankRoute.route) { AdminCreateBloodBankPage(navHostController) }
        //B (2)
        composable(BabyBirthIndexRoute.route){ BabyBirthIndexPage(navHostController) }
        composable(BabyBirthCreateRoute.route){ BabyBirthCreatePage(navHostController) }
        composable(BloodBankHomeRoute.route){BloodBankHomePage(navHostController)}
        composable(BloodStockIndexRoute.route){ BloodStockIndexPage(navHostController) }
        composable(BloodStockCreateRoute.route){ BloodStockCreatePage(navHostController) }
        composable(BloodImportIndexRoute.route){ BloodImportsIndexPage(navHostController) }
        composable(BloodImportCreateRoute.route){ BloodImportCreatePage(navHostController) }
        composable(BloodBankKpiIndexRoute.route){ KpiIndexPage(navHostController) }
        composable(BloodBankKpiCreateRoute.route){ KpiCreatePage(navHostController) }

        //BloodBankKpiIndexRoute
        //C (5)
        composable(CitiesRoute.route){ CitiesPage(navHostController) }
        composable(CityViewRoute.route){ CityViewPage(navHostController) }

        composable(CancerCureStoreRoute.route){ CancerCureCreatePage(navHostController) }
        composable(CancerCureIndexRoute.route){ CancerCureIndexPage(navHostController) }

        composable(ClinicsIndexRoute.route) { ClinicsIndexPage(navHostController) }
        composable(CreateUserRoute.route) { CreateUserPage(navHostController) }
        composable(ComponentDepartmentHomeRoute.route) { ComponentDepartmentHomePage(navHostController) }

        //ComponentDepartmentIndexRoute
        //D
        composable(DailyBloodCollectionCreateRoute.route){ DailyBloodCollectionCreatePage(navHostController) }
        composable(DailyBloodCollectionIndexRoute.route){ DailyBloodCollectionIndexPage(navHostController) }
        composable(DailyBloodProcessingCreateRoute.route){ DailyBloodProcessingCreatePage(navHostController) }
        composable(DailyBloodProcessingIndexRoute.route){ DailyBloodProcessingIndexPage(navHostController) }
        composable(DonationDepartmentHomeRoute.route){ DonationDepartmentHomePage(navHostController) }

        //DonationDepartmentHomeRoute
        //DailyBloodProcessingCreateRoute
        //H (8)
        composable(HomeRoute.route){ HomeScreen(navHostController) }
        composable(HospitalsIndexRoute.route){ HospitalsIndexPage(navHostController) }
        composable(HospitalsStoreRoute.route){ HospitalCreatePage(navHostController) }
        composable(HospitalDeviceCreateRoute.route){ HospitalDeviceCreatePage(navHostController)}
        composable(HospitalsViewRoute.route){HospitalViewPage(navHostController)}
        composable(HospitalDeviceUsagesRoute.route){ HospitalDevicesUsagePage(navHostController)}
        composable(HospitalHomeRoute.route){ HospitalHomePage(navHostController) }
        composable(HospitalDevicesRoute.route){HospitalDevicesPage(navHostController)}
        composable(HospitalUserViewRoute.route){ HospitalUserPage(navHostController) }
        composable(HospitalGeneralCreateRoute.route){ HospitalGeneralCreatePage(navHostController) }
        composable(HospitalModuleSelectorRoute.route){ HospitalModuleSelectorPage(navHostController) }

        //I
        composable(IncubatorIndexRoute.route){ IncubatorsIndexPage(navHostController) }
        composable(IssuingDepartmentHomeRoute.route){ IssuingDepartmentHomePage(navHostController) }
        composable(IncinerationIndexRoute.route){ IncinerationIndexPage(navHostController) }
        composable(IncinerationCreateRoute.route){ IncinerationCreatePage(navHostController) }
        composable(ChangePasswordRoute.route){ ChangePasswordPage(navHostController) }

        //ChangePasswordPage
        //J

        //K

        //L (3)
        composable(LoginRoute.route){ LoginPage(navHostController) }
        composable(LandingRoute.route){ LandingPage(navHostController) }
        composable(LabTestIndexRoute.route){ LabTestsIndexPage(navHostController)}

        //M (2)
        composable(MorguesIndexRoute.route){ MorgueIndexPage(navHostController) }
        composable(MorgueCreateRoute.route){ MorgueCreatePage(navHostController) }
        composable(MonthlyIssuingReportsIndexRoute.route){ MonthlyIssuingReportsIndexPage(navHostController) }
        composable(MonthlyIssuingReportsCreateRoute.route){ MonthlyIssuingReportsCreatePage(navHostController) }

        //N (2)
        composable(NormalUserWardsIndexRoute.route){ NormalUserWardsIndexPage(navHostController)}
        composable(NationalIdScannerRoute.route){ NationalIdScannerPage(navHostController)}
        composable(NearExpiredIndexRoute.route){ NearExpiryIndexPage(navHostController) }
        composable(NearExpiredCreateRoute.route){ NearExpiryCreatePage(navHostController)}

        //O (4)
        composable(OperationsIndexRoute.route){ OperationsIndexPage(navHostController) }
        composable(OperationCreateRoute.route){ OperationCreatePage(navHostController) }
        composable(OperationRoomIndexRoute.route){ OperationRoomsIndexPage(navHostController) }
        composable(OperationRoomCreateRoute.route){ OperationRoomCreatePage(navHostController) }

        //P (5)
        composable(PatientCreateRoute.route){ PatientCreatePage(navHostController)}
        composable(PatientsIndexRoute.route){ HospitalPatientsIndexPage(navHostController) }
        composable(PatientViewRoute.route){ PatientViewPage(navHostController)}
        composable(PatientWardAdmissionsCreateRoute.route){PatientWardAdmissionCreatePage(navHostController)}
        composable(PatientWardAdmissionsIndexRoute.route){PatientWardAdmissionsIndexPage(navHostController)}
        composable(PhysicalTherapyIndexRoute.route){ PhysicalTherapyIndexPage(navHostController) }


        composable(PretermAdmissionsIndexRoute.route){ PretermAdmissionsIndexPage(navHostController) }
        composable(PretermAdmissionsCreateRoute.route){ PretermAdmissionCreatePage(navHostController) }

        //R (2)
        composable(RenalDevicesIndexRoute.route){ RenalDevicesIndexPage(navHostController) }
        composable(ReceptionBedsIndexRoute.route){ ReceptionBedsIndexPage(navHostController) }

        //U (1)
        composable(UserControlRoute.route){ UserControlPage(navHostController) }

        //W (1)
        composable(WardCreateRoute.route) { WardCreatePage(navHostController)}
    }
}

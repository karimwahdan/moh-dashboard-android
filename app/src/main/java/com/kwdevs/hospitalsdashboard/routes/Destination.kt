package com.kwdevs.hospitalsdashboard.routes

interface Destination {val route:String}

object TestRoute: Destination {override val route="TestRoute"}

//A
object AreaViewRoute: Destination {override val route="AreaViewRoute"}
object AdmissionCreateRoute: Destination { override val route= "AdmissionCreateRoute"}
object AdminHomeRoute:Destination{ override val route="AdminHomeRoute" }
//B
object BabyBirthIndexRoute:Destination{override val route="BabyBirthIndexRoute"}
object BabyBirthCreateRoute:Destination{override val route="BabyBirthCreateRoute"}
object BloodBankHomeRoute:Destination{override val route="BloodBankHomeRoute"}
object BloodStockIndexRoute:Destination{override val route="BloodStockIndexRoute"}
object BloodStockCreateRoute:Destination{override val route="BloodStockCreateRoute"}

//C
object CitiesRoute: Destination {override val route="CitiesRoute"}
object CityViewRoute: Destination {override val route="CityViewRoute"}
object CancerCureIndexRoute: Destination {override val route="CancerCureIndexRoute"}
object CancerCureStoreRoute: Destination {override val route="CancerCureStoreRoute"}
object ClinicsIndexRoute: Destination {override val route="ClinicsIndexRoute"}
object ChangePasswordRoute: Destination {override val route="ChangePasswordRoute"}

//ChangePasswordPage
//D
object DailyBloodCollectionCreateRoute:Destination{override val route="DailyBloodCollectionCreateRoute"}
object DailyBloodCollectionIndexRoute:Destination{override val route="DailyBloodCollectionIndexRoute"}

//E

//F

//H
object HomeRoute: Destination {override val route="HomeRoute"}
object HospitalsIndexRoute: Destination {override val route="HospitalsIndexRoute"}
object HospitalsStoreRoute: Destination {override val route="HospitalsStoreRoute"}
object HospitalsViewRoute: Destination {override val route="HospitalViewRoute"}
object HospitalDepartmentCreateRoute:Destination{override val route="HospitalDepartmentCreateRoute"}
object HospitalDeviceCreateRoute: Destination {override val route="HospitalDeviceCreateRoute"}
object HospitalHomeRoute:Destination {override val route="HospitalHomeRoute"}
object HospitalDevicesRoute:Destination{override val route="HospitalDevicesRoute"}
object HospitalUserViewRoute:Destination{override val route="HospitalUserViewRoute"}
object HospitalDeviceUsagesRoute:Destination{override val route="HospitalDeviceUsagesRoute"}
object HospitalModuleSelectorRoute:Destination{override val route="HospitalModuleSelectorRoute"}

//I
object IncubatorIndexRoute:Destination{override val route="IncubatorIndexRoute"}
object IssuingDepartmentHomeRoute:Destination{override val route="IssuingDepartmentHomeRoute"}
//L
object LoginRoute: Destination {override val route="LoginRoute"}
object LandingRoute: Destination {override val route="LandingRoute"}
object LabTestIndexRoute:Destination{override val route="LabTestIndexRoute"}

//M
object MorguesIndexRoute: Destination {override val route="MorguesIndexRoute"}
object MorgueCreateRoute: Destination {override val route="MorgueCreateRoute"}

//N
object NormalUserWardsIndexRoute: Destination {override val route="NormalUserWardsIndexRoute"}
object NationalIdScannerRoute: Destination {override val route="NationalIdScannerRoute"}

//O
object OperationsIndexRoute: Destination {override val route="OperationsIndexRoute"}
object OperationCreateRoute: Destination {override val route="OperationCreateRoute"}
object OperationRoomIndexRoute: Destination {override val route="OperationRoomIndexRoute"}
object OperationRoomCreateRoute: Destination {override val route="OperationRoomCreateRoute"}

//P
object PatientsIndexRoute: Destination {override val route="PatientsIndexRoute"}
object PatientCreateRoute: Destination {override val route="PatientCreateRoute"}
object PatientViewRoute:Destination{override val route="PatientViewRoute"}
object PatientWardAdmissionsCreateRoute: Destination {override val route="PatientWardAdmissionsCreateRoute"}
object PatientWardAdmissionsIndexRoute: Destination {override val route="PatientWardAdmissionsIndexRoute"}
object PhysicalTherapyIndexRoute: Destination{override val route="PhysicalTherapyIndexRoute"}
object PretermAdmissionsIndexRoute: Destination{override val route="PretermAdmissionsIndexRoute"}
object PretermAdmissionsCreateRoute: Destination{override val route="PretermAdmissionsCreateRoute"}

//R
object ReceptionBedsIndexRoute: Destination {override val route="ReceptionBedsIndexRoute"}
object RenalDevicesIndexRoute: Destination {override val route="RenalDevicesIndexRoute"}

//S

//T

//U
object UserControlRoute:Destination {override val route="UserControlRoute"}

//W
object WardCreateRoute: Destination {override val route="WardCreateRoute"}

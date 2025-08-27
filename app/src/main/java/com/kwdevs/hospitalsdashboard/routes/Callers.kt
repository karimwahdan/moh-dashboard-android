package com.kwdevs.hospitalsdashboard.routes

import com.kwdevs.hospitalsdashboard.app.retrofit.RetrofitBuilder
import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.USERS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.interfaces.AreaApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.BabyBirthApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.CancerCureApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.CityApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalClinicApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalDeviceApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalDeviceUsageApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalWardApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.IncubatorApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.LabTestsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.MorgueApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.OperationRoomApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.PatientOperationApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.PatientsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.PhysicalTherapyApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.PretermAdmissionsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.ReceptionBedsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.ReceptionFrequenciesApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.RenalDevicesApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.ReportsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.SettingsApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.UsersApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.WardAdmissionsApi
import retrofit2.http.Body
import retrofit2.http.POST


class Callers {

    //A
    fun areaApi():AreaApi{return RetrofitBuilder.createService(AreaApi::class.java)}

    //B
    fun babyBirthApi():BabyBirthApi{return RetrofitBuilder.createService(BabyBirthApi::class.java)}

    //C
    fun cityApi():CityApi{return RetrofitBuilder.createService(CityApi::class.java)}
    fun cancerCureApi():CancerCureApi{return RetrofitBuilder.createService(CancerCureApi::class.java)}

    //H
    fun hospitalApi():HospitalApi{return RetrofitBuilder.createService(HospitalApi::class.java)}
    fun hospitalDevicesApi():HospitalDeviceApi{return RetrofitBuilder.createService(HospitalDeviceApi::class.java)}
    fun hospitalDeviceUsagesApi():HospitalDeviceUsageApi{return RetrofitBuilder.createService(HospitalDeviceUsageApi::class.java)}
    fun hospitalClinicsApi():HospitalClinicApi{return RetrofitBuilder.createService(HospitalClinicApi::class.java)}

    //I
    fun incubatorApi():IncubatorApi{return RetrofitBuilder.createService(IncubatorApi::class.java)}

    //L
    fun labTestsApi():LabTestsApi{return RetrofitBuilder.createService(LabTestsApi::class.java)}

    //M
    fun morgueApi():MorgueApi{return RetrofitBuilder.createService(MorgueApi::class.java)}

    //O
    fun operationRoomApi():OperationRoomApi {return RetrofitBuilder.createService(OperationRoomApi::class.java)}

    //P
    fun patientsApi():PatientsApi {return RetrofitBuilder.createService(PatientsApi::class.java)}
    fun patientOperationsApi():PatientOperationApi {return RetrofitBuilder.createService(PatientOperationApi::class.java)}
    fun physicalTherapyApi(): PhysicalTherapyApi {return RetrofitBuilder.createService(PhysicalTherapyApi::class.java)}
    fun pretermAdmissionsApi():PretermAdmissionsApi {return RetrofitBuilder.createService(PretermAdmissionsApi::class.java)}

    //R
    fun renalDevicesApi():RenalDevicesApi{return RetrofitBuilder.createService(RenalDevicesApi::class.java)}
    fun receptionBedsApi():ReceptionBedsApi{return RetrofitBuilder.createService(ReceptionBedsApi::class.java)}
    fun receptionFrequenciesApi():ReceptionFrequenciesApi {return RetrofitBuilder.createService(ReceptionFrequenciesApi::class.java)}
    fun reportsApi():ReportsApi {return RetrofitBuilder.createService(ReportsApi::class.java)}

    //S
    fun settingsApi():SettingsApi{return RetrofitBuilder.createService(SettingsApi::class.java)}

    //U
    fun usersApi():UsersApi{return RetrofitBuilder.createService(UsersApi::class.java)}

    //W
    fun wardsApi(): HospitalWardApi{return RetrofitBuilder.createService(HospitalWardApi::class.java)}
    fun wardAdmissionsApi(): WardAdmissionsApi{return RetrofitBuilder.createService(WardAdmissionsApi::class.java)}



}
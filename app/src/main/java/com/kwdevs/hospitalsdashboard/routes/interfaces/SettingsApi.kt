package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.control.CustomModelBody
import com.kwdevs.hospitalsdashboard.bodies.control.PermissionBody
import com.kwdevs.hospitalsdashboard.bodies.control.RoleBody
import com.kwdevs.hospitalsdashboard.bodies.control.RolePermissionsBody
import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartmentResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes.ClinicVisitTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.cureTypes.CureTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.generalClinics.GeneralClinicResponse
import com.kwdevs.hospitalsdashboard.models.settings.statuses.StatusResponse
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.multipleReturns.CrudDeviceData
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.permissions.PermissionsResponse
import com.kwdevs.hospitalsdashboard.models.settings.sector.SectorResponse
import com.kwdevs.hospitalsdashboard.models.settings.title.TitleResponse
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardTypeResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BLOOD_BANKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.USERS_PREFIX
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.ModulesResponse
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.MorgueOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.TitlesTypesSectorsCitiesOptionsData
import com.kwdevs.hospitalsdashboard.responses.settings.CustomModelSingleResponse
import com.kwdevs.hospitalsdashboard.responses.settings.PatientMedicalStatesResponse
import com.kwdevs.hospitalsdashboard.responses.settings.PermissionDataResponse
import com.kwdevs.hospitalsdashboard.responses.settings.SinglePermissionResponse
import com.kwdevs.hospitalsdashboard.responses.settings.RoleSingleResponse
import com.kwdevs.hospitalsdashboard.routes.WARD_ADMISSION_OPTIONS_API
import com.kwdevs.hospitalsdashboard.routes.BASIC_DEPARTMENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.CANCER_CURE_TYPES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.CITY_DETAILS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.CLINIC_TYPES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.CLINIC_VISIT_TYPES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.CONTROL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.DEVICES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.DEVICE_STATUSES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOME_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITAL_TYPES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.MODELS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.MORGUE_OPTIONS_API
import com.kwdevs.hospitalsdashboard.routes.NATIONALITIES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPERATION_STATUSES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.RENAL_DEVICE_TYPES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.SECTORS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.SETTINGS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.TITLES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.USER_PREFIX
import com.kwdevs.hospitalsdashboard.routes.WARD_TYPES_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SettingsApi {

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/city-chart")
    suspend fun cityCharts(@Body body: KpiFilterBody): CityBloodBankKpiResponse

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/insurance-chart")
    suspend fun insuranceCharts(@Body body: KpiFilterBody): HospitalBloodBankKpiResponse

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/educational-chart")
    suspend fun educationalCharts(@Body body: KpiFilterBody): HospitalBloodBankKpiResponse

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/curative-chart")
    suspend fun curativeCharts(@Body body: KpiFilterBody): HospitalBloodBankKpiResponse

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/specialized-chart")
    suspend fun specializedCharts(@Body body: KpiFilterBody): HospitalBloodBankKpiResponse

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/nbts-chart")
    suspend fun nBTSCharts(@Body body: KpiFilterBody): HospitalBloodBankKpiResponse

    @GET("$HOME_PREFIX/$HOME_PREFIX")
    suspend fun home(
        @Query("userId")userId:Int,
    ):HomeResponse

    @GET("$CITY_DETAILS_PREFIX/$INDEX_PREFIX")
    suspend fun citiesWithCount():CityWithCountResponse

    @GET("$SETTINGS_PREFIX/$BASIC_DEPARTMENTS_PREFIX")
    suspend fun basicDepartments():BasicDepartmentResponse

    @GET("$SETTINGS_PREFIX/$DEVICE_STATUSES_PREFIX")
    suspend fun deviceStatuses():StatusResponse

    @GET("$SETTINGS_PREFIX/$OPERATION_STATUSES_PREFIX")
    suspend fun operationStatuses():StatusResponse

    @GET("$SETTINGS_PREFIX/$DEVICES_PREFIX/crud-options")
    suspend fun crudDeviceOptions(@Query("hospital_id") hospitalId:Int):CrudDeviceData

    @GET("$SETTINGS_PREFIX/$SECTORS_PREFIX")
    suspend fun sectors():SectorResponse

    @GET("$SETTINGS_PREFIX/$NATIONALITIES_PREFIX")
    suspend fun nationalities():NationalitiesResponse
    @GET("$SETTINGS_PREFIX/medical-states")
    suspend fun medicalStates():PatientMedicalStatesResponse

    @GET("$SETTINGS_PREFIX/$TITLES_PREFIX")
    suspend fun titles():TitleResponse

    @GET("$SETTINGS_PREFIX/$HOSPITAL_TYPES_PREFIX")
    suspend fun hospitalTypes():HospitalTypeResponse

    @GET("$SETTINGS_PREFIX/permissions-data")
    suspend fun permissionData():PermissionDataResponse
    @POST("$USERS_PREFIX/update-user-roles")
    suspend fun updateHospitalUserRoles(@Body body: UserRoleBody): HospitalUserSSResponse

    @GET("$CONTROL_PREFIX/options")
    suspend fun permissionsList(): PermissionsResponse

    @POST("$CONTROL_PREFIX/store-role")
    suspend fun storeRole(@Body roleBody: RoleBody):RoleSingleResponse
    @POST("$CONTROL_PREFIX/update-role")
    suspend fun updateRole(@Body roleBody: RoleBody):RoleSingleResponse

    @POST("$CONTROL_PREFIX/store-permission")
    suspend fun storePermission(@Body body: PermissionBody):SinglePermissionResponse

    @POST("$CONTROL_PREFIX/update-role-permissions")
    suspend fun updateRolePermissions(@Body body: RolePermissionsBody):RoleSingleResponse

    @POST("$CONTROL_PREFIX/$MODELS_PREFIX/$STORE_PREFIX")
    suspend fun storeTable(@Body body:CustomModelBody):CustomModelSingleResponse

    @POST("$CONTROL_PREFIX/$MODELS_PREFIX/$UPDATE_PREFIX")
    suspend fun updateTable(@Body body:CustomModelBody):CustomModelSingleResponse

    @GET(MORGUE_OPTIONS_API)
    suspend fun morgueOptions(): MorgueOptionsData

    @GET(WARD_ADMISSION_OPTIONS_API)
    suspend fun wardAdmissionOptions():HospitalWardAdmissionOptionsData

    @GET(WARD_TYPES_PREFIX)
    suspend fun wardTypes():WardTypeResponse

    @GET("$SETTINGS_PREFIX/$CANCER_CURE_TYPES_PREFIX")
    suspend fun cancerCureTypes(): CureTypeResponse


    @GET("$SETTINGS_PREFIX/$CLINIC_TYPES_PREFIX")
    suspend fun clinicTypes(): GeneralClinicResponse


    @GET("$SETTINGS_PREFIX/$CLINIC_VISIT_TYPES_PREFIX")
    suspend fun clinicVisitTypes(): ClinicVisitTypeResponse

    @GET("$SETTINGS_PREFIX/$RENAL_DEVICE_TYPES_PREFIX")
    suspend fun renalDeviceTypes(): DeviceTypeResponse

    @GET("$SETTINGS_PREFIX/blood-options")
    suspend fun bloodOptions(): BloodOptionsData

    @GET("$SETTINGS_PREFIX/ttsc")
    suspend fun titlesTypesSectorsCitiesOptions():TitlesTypesSectorsCitiesOptionsData

    @GET("$SETTINGS_PREFIX/hospitals")
    suspend fun hospitalOptions():HospitalsResponse
    @GET("$SETTINGS_PREFIX/hospitals-by-sector")
    suspend fun sectorHospitalOptions(@Query("id")sectorId:Int):HospitalsResponse
    @GET("$SETTINGS_PREFIX/hospitals-by-type")
    suspend fun typeHospitalOptions(@Query("id")typeId:Int):HospitalsResponse
    @GET("$SETTINGS_PREFIX/hospitals-by-area")
    suspend fun areaHospitalOptions(@Query("id")areaId:Int):HospitalsResponse

    @GET("$SETTINGS_PREFIX/modules")
    suspend fun modulesOptions():ModulesResponse

    @POST("$USER_PREFIX/add-super-user-role")
    suspend fun addSuperUserRole(@Query("userId")userId: Int,@Query("roleId")roleId: Int):PermissionDataResponse

    @POST("$USER_PREFIX/remove-super-user-role")
    suspend fun removeSuperUserRole(@Query("userId")userId: Int,@Query("roleId")roleId: Int):PermissionDataResponse

    @POST("$USER_PREFIX/add-user-role")
    suspend fun addUserRole(@Query("userId")userId: Int,@Query("roleId")roleId: Int):PermissionDataResponse

    @POST("$USER_PREFIX/remove-user-role")
    suspend fun removeUserRole(@Query("userId")userId: Int,@Query("roleId")roleId: Int):PermissionDataResponse

    @POST("$USER_PREFIX/add-sector-head")
    suspend fun addSectorHead(@Query("userId")userId: Int,@Query("sectorId")sectorId: Int):PermissionDataResponse

    @POST("$USER_PREFIX/add-city-head")
    suspend fun addCityHead(@Query("userId")userId: Int,@Query("cityId")cityId: Int):PermissionDataResponse

}
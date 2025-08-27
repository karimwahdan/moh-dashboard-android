package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.ClinicVisitBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.HospitalClinicBody
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisit
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisitSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.CLINICS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.VISITS_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HospitalClinicApi {
    @GET("$HOSPITALS_PREFIX/$CLINICS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId") hospitalId:Int):ApiResponse<PaginationData<HospitalClinic>>

    @POST("$HOSPITALS_PREFIX/$CLINICS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeByNormalUser(@Body body: HospitalClinicBody):HospitalClinicSingleResponse

    @POST("$HOSPITALS_PREFIX/$CLINICS_PREFIX/$VISITS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeVisits(@Body body: ClinicVisitBody):DailyClinicVisitSingleResponse

    @GET("$HOSPITALS_PREFIX/$CLINICS_PREFIX/$VISITS_PREFIX/$INDEX_PREFIX/by-clinic")
    suspend fun indexVisitsByClinic(@Query("page")page:Int,@Query("clinicId") clinicId:Int):ApiResponse<PaginationData<DailyClinicVisit>>

}
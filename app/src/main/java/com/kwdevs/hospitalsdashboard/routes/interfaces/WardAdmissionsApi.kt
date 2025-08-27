package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.PatientAdmissionBody
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmissionSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.ADMISSIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.TRANSFER_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.WARDS_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WardAdmissionsApi {

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun store(@Body body: PatientAdmissionBody): PatientAdmissionSingleResponse

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$TRANSFER_PREFIX/$NORMAL_PREFIX")
    suspend fun transfer(@Body body: PatientAdmissionBody): PatientAdmissionSingleResponse

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX")
    suspend fun update(@Body body: PatientAdmissionBody): PatientAdmissionSingleResponse

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$TRANSFER_PREFIX/$NORMAL_PREFIX")
    suspend fun quit(@Body body: PatientAdmissionBody): PatientAdmissionSingleResponse

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$TRANSFER_PREFIX/$NORMAL_PREFIX")
    suspend fun die(@Body body: PatientAdmissionBody): PatientAdmissionSingleResponse

    @GET("$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/$INDEX_PREFIX/by-hospital-and-patient")
    suspend fun indexByHospitalAndPatient(@Query("page")page:Int,
                                          @Query("hospitalId")hospitalId:Int,
                                          @Query("patientId") patientId:Int):ApiResponse<PaginationData<PatientAdmission>>


}
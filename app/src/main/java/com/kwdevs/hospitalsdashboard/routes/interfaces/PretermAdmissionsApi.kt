package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.PretermAdmissionBody
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmission
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmissionSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PATIENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PRETERMS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.TRANSFER_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PretermAdmissionsApi {
    @POST("$PATIENTS_PREFIX/$PRETERMS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun store(@Body body: PretermAdmissionBody): PretermAdmissionSingleResponse

    @POST("$PATIENTS_PREFIX/$PRETERMS_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX")
    suspend fun update(@Body body: PretermAdmissionBody): PretermAdmissionSingleResponse

    @POST("$PATIENTS_PREFIX/$PRETERMS_PREFIX/$TRANSFER_PREFIX/$NORMAL_PREFIX")
    suspend fun quit(@Body body: PretermAdmissionBody): PretermAdmissionSingleResponse
    @POST("$PATIENTS_PREFIX/$PRETERMS_PREFIX/$NORMAL_PREFIX")
    suspend fun die(@Body body: PretermAdmissionBody): PretermAdmissionSingleResponse

    @GET("$PATIENTS_PREFIX/$PRETERMS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(
        @Query("page")page:Int,
        @Query("hospitalId") hospitalId:Int):ApiResponse<PaginationData<PretermAdmission>>


}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.CancerCureBody
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCure
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCureSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.CANCER_CURE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PATIENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CancerCureApi {
    @GET("$PATIENTS_PREFIX/$CANCER_CURE_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<CancerCure>>

    @GET("$PATIENTS_PREFIX/$CANCER_CURE_PREFIX/$INDEX_PREFIX/by-patient")
    suspend fun indexByPatient(@Query("page")page:Int,@Query("patientId")patientId:Int
                               ,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<CancerCure>>

    @POST("$PATIENTS_PREFIX/$CANCER_CURE_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeNormal(@Body body: CancerCureBody):CancerCureSingleResponse
}
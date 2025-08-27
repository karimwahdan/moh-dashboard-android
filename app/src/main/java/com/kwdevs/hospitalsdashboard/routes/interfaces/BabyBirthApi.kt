package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.BabyBirthBody
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirth
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirthSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.BABY_BIRTHS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PATIENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BabyBirthApi {
    @GET("$PATIENTS_PREFIX/$BABY_BIRTHS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<BabyBirth>>

    @POST("$PATIENTS_PREFIX/$BABY_BIRTHS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeNormal(@Body body: BabyBirthBody):BabyBirthSingleResponse
}
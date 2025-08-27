package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.MorgueBody
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.MorgueSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.options.MorgueOptionsData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.MORGUES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPTIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MorgueApi {

    @GET("$HOSPITALS_PREFIX/$MORGUES_PREFIX/$OPTIONS_PREFIX")
    suspend fun options():MorgueOptionsData

    @GET("$HOSPITALS_PREFIX/$MORGUES_PREFIX/$INDEX_PREFIX/all")
    suspend fun indexAll():ApiResponse<PaginationData<Morgue>>

    @GET("$HOSPITALS_PREFIX/$MORGUES_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospital_id")hospitalId:Int):ApiResponse<PaginationData<Morgue>>

    @POST("$HOSPITALS_PREFIX/$MORGUES_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeNormal(@Body body: MorgueBody):MorgueSingleResponse


}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.PhysicalTherapyFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.physicalTherapy.PhysicalTherapyFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.physicalTherapy.PhysicalTherapyFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PHYSICAL_THERAPY_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PhysicalTherapyApi {

    @GET("$HOSPITALS_PREFIX/$PHYSICAL_THERAPY_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<PhysicalTherapyFrequency>>

    @POST("$HOSPITALS_PREFIX/$PHYSICAL_THERAPY_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeNormal(@Body body: PhysicalTherapyFrequencyBody):PhysicalTherapyFrequencySingleResponse


}
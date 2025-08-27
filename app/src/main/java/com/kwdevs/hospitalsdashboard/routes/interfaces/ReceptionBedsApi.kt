package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionBedBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBedSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.ADMISSIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.RECEPTION_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReceptionBedsApi {

    @GET("$HOSPITALS_PREFIX/$RECEPTION_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("hospitalId")hospitalId:Int):ReceptionBedSingleResponse

    @POST("$HOSPITALS_PREFIX/$RECEPTION_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeNormal(@Body body: ReceptionBedBody):ReceptionBedSingleResponse

    @GET("$HOSPITALS_PREFIX/$RECEPTION_PREFIX/$ADMISSIONS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexFrequenciesByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int): ApiResponse<PaginationData<ReceptionFrequency>>

    @POST("$HOSPITALS_PREFIX/$RECEPTION_PREFIX/$ADMISSIONS_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeFrequencyNormal(@Body body: ReceptionFrequencyBody): ReceptionBedSingleResponse


}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.IncubatorBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.LabTestFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.incubators.Incubator
import com.kwdevs.hospitalsdashboard.models.hospital.incubators.IncubatorSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INCUBATORS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.LAB_TESTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IncubatorApi {

    @GET("$HOSPITALS_PREFIX/$INCUBATORS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<Incubator>>

    @POST("$HOSPITALS_PREFIX/$INCUBATORS_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeNormal(@Body body: IncubatorBody):IncubatorSingleResponse


}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalDeviceBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalWashFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.RENAL_DEVICES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.WASH_FREQUENCIES
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RenalDevicesApi {
    @GET("$HOSPITALS_PREFIX/$RENAL_DEVICES_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<HospitalRenalDevice>>

    @POST("$HOSPITALS_PREFIX/$RENAL_DEVICES_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeNormal(@Body body:RenalDeviceBody):RenalDeviceSingleResponse

    @GET("$HOSPITALS_PREFIX/$RENAL_DEVICES_PREFIX/$WASH_FREQUENCIES/$INDEX_PREFIX/by-device")
    suspend fun indexFrequencyByDevice(@Query("page")page:Int,@Query("deviceId")deviceId:Int):ApiResponse<PaginationData<RenalWashFrequency>>

    @POST("$HOSPITALS_PREFIX/$RENAL_DEVICES_PREFIX/$WASH_FREQUENCIES/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeFrequencyNormal(@Body body:RenalWashFrequencyBody):RenalWashFrequencySingleResponse
}
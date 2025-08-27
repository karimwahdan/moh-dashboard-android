package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceBody
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.DEVICES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.DEVICE_USAGE_BY_DEVICE_ID_API
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HospitalDeviceApi {
    @GET(DEVICE_USAGE_BY_DEVICE_ID_API)
    suspend fun indexByDevice(@Query("deviceId") deviceId:Int):HospitalDeviceSingleResponse

    @GET("$HOSPITALS_PREFIX/$DEVICES_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int): ApiResponse<PaginationData<HospitalDevice>>

    @POST("$HOSPITALS_PREFIX/$DEVICES_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeByNormalUser(@Body body: HospitalDeviceBody):HospitalDeviceUsageSingleResponse

}
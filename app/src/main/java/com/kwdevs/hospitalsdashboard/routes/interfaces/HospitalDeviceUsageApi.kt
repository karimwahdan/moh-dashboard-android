package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceUsageBody
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.routes.DEVICE_USAGE_BY_DEVICE_ID_API
import com.kwdevs.hospitalsdashboard.routes.STORE_DEVICE_USAGE_BY_NORMAL_USER_API
import com.kwdevs.hospitalsdashboard.routes.UPDATE_DEVICE_USAGE_BY_NORMAL_USER_API
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HospitalDeviceUsageApi {
    @GET(DEVICE_USAGE_BY_DEVICE_ID_API)
    suspend fun indexByDevice(@Query ("deviceId") deviceId:Int):HospitalDeviceSingleResponse

    @POST(STORE_DEVICE_USAGE_BY_NORMAL_USER_API)
    suspend fun storeByNormalUser(@Body body: HospitalDeviceUsageBody):HospitalDeviceUsageSingleResponse

    @POST(UPDATE_DEVICE_USAGE_BY_NORMAL_USER_API)
    suspend fun updateByNormalUser(@Body body: HospitalDeviceUsageBody):HospitalDeviceUsageSingleResponse

}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.OperationRoomBody
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoomSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPERATION_ROOMS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_BY_USER
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationRoomApi {
    @GET("$HOSPITALS_PREFIX/$OPERATION_ROOMS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<OperationRoom>>

    @POST("$HOSPITALS_PREFIX/$OPERATION_ROOMS_PREFIX/$STORE_BY_USER")
    suspend fun store(@Body body: OperationRoomBody):OperationRoomSingleResponse
}
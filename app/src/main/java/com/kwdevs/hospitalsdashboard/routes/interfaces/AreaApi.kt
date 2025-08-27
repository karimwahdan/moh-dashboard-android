package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.routes.AREA_DETAILS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.VIEW_PREFIX
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AreaApi {
    @GET("$AREA_DETAILS_PREFIX/$INDEX_PREFIX/{id}")
    suspend fun areasWithCountIndex(@Path("id")id:Int):AreaWithCountResponse

    @GET("$AREA_DETAILS_PREFIX/$VIEW_PREFIX/{id}")
    suspend fun view(@Path("id")id:Int):AreaWithHospitalDetailSingleResponse
}
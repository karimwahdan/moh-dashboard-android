package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreaSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.routes.CITY_DETAILS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.VIEW_PREFIX
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CityApi {
    @GET("$CITY_DETAILS_PREFIX/$INDEX_PREFIX")
    suspend fun citiesWithCountIndex():CityWithCountResponse

    @GET("$CITY_DETAILS_PREFIX/$VIEW_PREFIX/{id}")
    suspend fun view(@Path("id")id:Int):CityWithAreaSingleResponse
}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalWardBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.wards.HospitalWardsResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreaSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalWardSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.CITY_DETAILS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.FILTER_HOSPITALS
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_BY_SECTOR_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_BY_TYPE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITAL_STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.VIEW_PREFIX
import com.kwdevs.hospitalsdashboard.routes.WARDS_PREFIX
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HospitalWardApi {

    @POST(FILTER_HOSPITALS)
    suspend fun filterHospitals(@Body body: HospitalFilterBody):HospitalsResponse

    @GET("$HOSPITALS_PREFIX/$WARDS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("hospitalId") hospitalId: Int,
                                @Query("page")page:Int):ApiResponse<PaginationData<HospitalWard>>
    @GET("$HOSPITALS_PREFIX/$WARDS_PREFIX/$INDEX_PREFIX/by-type-and-hospital")
    suspend fun indexByTypeAndHospital(@Query("hospitalId") hospitalId: Int,
                                @Query("wardTypeId")typeId:Int):HospitalWardsResponse

    @GET(HOSPITALS_BY_SECTOR_PREFIX)
    suspend fun indexBySector(
        @Path("id")sectorId:Int,
        @Query("page")page:Int,
    ):ApiResponse<PaginationData<Hospital>>

    @GET(HOSPITALS_BY_TYPE_PREFIX)
    suspend fun indexByType(
        @Path("id")typeId:Int,
        @Query("page")page:Int,
    ):ApiResponse<PaginationData<Hospital>>

    @POST("$HOSPITALS_PREFIX/$WARDS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun store(@Body body: HospitalWardBody):HospitalWardSingleResponse

}
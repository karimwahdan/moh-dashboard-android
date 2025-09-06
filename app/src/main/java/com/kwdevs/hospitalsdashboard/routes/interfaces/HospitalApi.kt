package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.control.HospitalModuleBody
import com.kwdevs.hospitalsdashboard.bodies.control.SlugListBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.FILTER_HOSPITALS
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PAGINATE_BY_SECTOR_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_BY_TYPE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITAL_STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITAL_UPDATE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.MODULES_PREFIX
import com.kwdevs.hospitalsdashboard.routes.SETTINGS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.VIEW_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HospitalApi {

    @POST(FILTER_HOSPITALS)
    suspend fun filterHospitals(@Body body: HospitalFilterBody):HospitalsResponse

    @GET("$SETTINGS_PREFIX/hospitals")
    suspend fun indexAllHospitals():HospitalsResponse

    @GET("$SETTINGS_PREFIX/directorate-hospitals")
    suspend fun indexDirectorateHospitals():HospitalsResponse

    @GET("$SETTINGS_PREFIX/hospitals-by-city-slug")
    suspend fun indexByCitySlug(@Query("slug")slug:String):HospitalsResponse

    @POST("$SETTINGS_PREFIX/hospitals-by-city-slug-list")
    suspend fun indexByCitySlugList(@Body slugs:SlugListBody):HospitalsResponse

    @GET("$HOSPITALS_PREFIX/$VIEW_PREFIX/{id}")
    suspend fun view(@Path("id") id: Int):HospitalSingleResponse

    @GET(HOSPITALS_PAGINATE_BY_SECTOR_PREFIX)
    suspend fun paginateBySector(
        @Path("id")sectorId:Int,
        @Query("page")page:Int,
    ):ApiResponse<PaginationData<Hospital>>

    @GET("$SETTINGS_PREFIX/hospitals-by-sector")
    suspend fun indexBySector(@Path("id")sectorId:Int):HospitalsResponse

    @GET(HOSPITALS_BY_TYPE_PREFIX)
    suspend fun indexByType(
        @Path("id")typeId:Int,
        @Query("page")page:Int,
    ):ApiResponse<PaginationData<Hospital>>

    @POST(HOSPITAL_STORE_PREFIX)
    suspend fun store(@Body body: HospitalBody):HospitalSingleResponse

    @POST(HOSPITAL_UPDATE_PREFIX)
    suspend fun update(@Body body: HospitalBody):HospitalSingleResponse

    @POST("$HOSPITALS_PREFIX/$MODULES_PREFIX/$STORE_PREFIX")
    suspend fun addModulesToHospital(@Body bodies:List<HospitalModuleBody>):HospitalSingleResponse

}
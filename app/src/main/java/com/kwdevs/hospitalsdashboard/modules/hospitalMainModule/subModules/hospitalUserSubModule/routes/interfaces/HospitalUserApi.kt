package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.USERS_PREFIX
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.settings.RolesResponse
import com.kwdevs.hospitalsdashboard.routes.AREA_DETAILS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HospitalUserApi {
    @POST("$USERS_PREFIX/$STORE_PREFIX")
    suspend fun storeByAdmin(@Body hospitalUserBody: HospitalUserBody): HospitalUserSingleResponse

    @GET("$USERS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int,@Query("hospitalId") hospitalId: Int): ApiResponse<PaginationData<SimpleHospitalUser>>

    @POST("$USERS_PREFIX/update-user-roles")
    suspend fun updateRoles(@Body body:UserRoleBody): HospitalUserSSResponse


}
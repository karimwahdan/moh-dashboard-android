package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.control.PasswordBody
import com.kwdevs.hospitalsdashboard.bodies.control.SuperUserBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.routes.LOGIN_NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.LOGIN_SUPER_PREFIX
import com.kwdevs.hospitalsdashboard.routes.USER_PREFIX
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {
    @POST("$USER_PREFIX/$LOGIN_SUPER_PREFIX")
    suspend fun loginSuper(
        @Query("username") username:String,
        @Query("password") password:String
    ): SimpleSuperUserSingleResponse

    @POST("$USER_PREFIX/$LOGIN_NORMAL_PREFIX")
    suspend fun loginNormal(
        @Query("username") username:String,
        @Query("password") password:String
    ):HospitalUserSSResponse

    @POST("$USER_PREFIX/view-normal")
    suspend fun viewNormal(
        @Query("username") username:String,
        @Query("password") password:String
    ):HospitalUserSingleResponse

    @POST("$USER_PREFIX/store")
    suspend fun create(
       @Body body: HospitalUserBody
    ):HospitalUserSingleResponse

    @POST("$USER_PREFIX/store-super")
    suspend fun createSuper(
        @Body body: SuperUserBody
    ):SimpleSuperUserSingleResponse

    @POST("$USER_PREFIX/change-hospital-user-password")
    suspend fun changeHospitalUserPassword(
        @Body body: PasswordBody
    ):HospitalUserSingleResponse

    @POST("$USER_PREFIX/change-super-user-password")
    suspend fun changeSuperUserPassword(
        @Body body: PasswordBody
    ):SimpleSuperUserSingleResponse
}
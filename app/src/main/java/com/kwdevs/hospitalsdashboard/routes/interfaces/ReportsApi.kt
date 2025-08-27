package com.kwdevs.hospitalsdashboard.routes.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming

interface ReportsApi {
    @GET("api/export-blood-stock")
    @Streaming // prevent loading entire file into memory
    suspend fun getTodayBloodStock(): Response<ResponseBody>
}
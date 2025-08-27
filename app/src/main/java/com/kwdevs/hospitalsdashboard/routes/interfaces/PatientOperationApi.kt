package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.PatientOperationBody
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperation
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperationSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.options.OperationOptionsData
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPERATIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPTIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PATIENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PatientOperationApi {

    @GET("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$OPTIONS_PREFIX")
    suspend fun options(
        @Query("hospitalId")hospitalId:Int): OperationOptionsData

    @GET("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(
        @Query("page")page:Int,
        @Query("hospitalId")hospitalId:Int,
        @Query("userId")userId:Int):ApiResponse<PaginationData<PatientOperation>>


    @GET("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$INDEX_PREFIX/by-patient")
    suspend fun indexByPatient(
        @Query("page")page:Int,
        @Query("patientId")patientId:Int,
        @Query("userId")userId:Int):ApiResponse<PaginationData<PatientOperation>>

    @GET("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$INDEX_PREFIX/by-room")
    suspend fun indexByRoom(
        @Query("page")page:Int,
        @Query("roomId")roomId:Int,
        @Query("userId")userId:Int):ApiResponse<PaginationData<PatientOperation>>

    @POST("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun store(@Body body: PatientOperationBody): PatientOperationSingleResponse


    @POST("$PATIENTS_PREFIX/$OPERATIONS_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX")
    suspend fun update(@Body body: PatientOperationBody): PatientOperationSingleResponse
}
package com.kwdevs.hospitalsdashboard.routes.interfaces

import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.PatientSingleResponse
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.DELETE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.FILTER_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.PATIENTS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.RESTORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PatientsApi {
    @GET("$PATIENTS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexForHospital(@Query("page")page:Int,@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<Patient>>

    @GET("$PATIENTS_PREFIX/$INDEX_PREFIX/by-ward")
    suspend fun indexForWard(@Query("page")page:Int,@Query("wardId")wardId:Int):ApiResponse<PaginationData<Patient>>

    @GET("$PATIENTS_PREFIX/$INDEX_PREFIX/by-operation-room")
    suspend fun indexForOperationRoom(@Query("page")page:Int,@Query("operationRoomId")operationRoomId:Int):ApiResponse<PaginationData<Patient>>

    @GET("$PATIENTS_PREFIX/$INDEX_PREFIX/female")
    suspend fun indexFemale(@Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<Patient>>

    @GET("$PATIENTS_PREFIX/$FILTER_PREFIX/by-national-id")
    suspend fun filter(@Query("nationalId")nationalId:String):PatientsResponse

    @GET("$PATIENTS_PREFIX/$FILTER_PREFIX/by-code")
    suspend fun filterByCode(@Query("patientCode")patientCode:String,
                             @Query("hospitalId")hospitalId: Int):PatientsResponse

    @GET("$PATIENTS_PREFIX/$FILTER_PREFIX/by-female-national-id")
    suspend fun filterFemale(@Query("nationalId")nationalId:String):PatientsResponse

    @POST("$PATIENTS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeNormal(@Body body: PatientBody):PatientSingleResponse

    @POST("$PATIENTS_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX")
    suspend fun updateNormal(@Body body: PatientBody):PatientSingleResponse

    @POST("$PATIENTS_PREFIX/$DELETE_PREFIX/$NORMAL_PREFIX")
    suspend fun deleteNormal(@Body body: PatientBody):PatientSingleResponse

    @POST("$PATIENTS_PREFIX/$RESTORE_PREFIX/$NORMAL_PREFIX")
    suspend fun restoreNormal(@Body body: PatientBody):PatientSingleResponse
}
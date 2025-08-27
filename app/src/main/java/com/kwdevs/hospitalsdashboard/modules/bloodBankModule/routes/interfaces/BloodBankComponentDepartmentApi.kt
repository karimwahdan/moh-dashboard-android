package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodProcessingBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.BloodComponentOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.DailyBloodProcessingSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BB_COMPONENT_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BB_ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BLOOD_BANKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DAILY_BLOOD_STOCKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DAILY_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DONATION_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ISSUING_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MONTHLY_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.PROCESSING_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPTIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import com.kwdevs.hospitalsdashboard.routes.UPDATE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BloodBankComponentDepartmentApi {

    @GET("$BB_COMPONENT_DEPARTMENT_PREFIX/$REPORTS_PREFIX/$PROCESSING_REPORTS_PREFIX/$DAILY_REPORTS_PREFIX/$INDEX_PREFIX/by-blood-bank")
    suspend fun indexByBloodBank(@Query("page")page:Int, @Query("bloodBankId")bloodBankId:Int):ApiResponse<PaginationData<DailyBloodProcessing>>

    @POST("$BB_COMPONENT_DEPARTMENT_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeDailyProcessing(@Body body:DailyBloodProcessingBody): DailyBloodProcessingSingleResponse

    @POST("$BB_COMPONENT_DEPARTMENT_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX")
    suspend fun updateDailyProcessing(@Body body:DailyBloodProcessingBody): DailyBloodProcessingSingleResponse

    @GET("$BB_COMPONENT_DEPARTMENT_PREFIX/$OPTIONS_PREFIX")
    suspend fun options(@Query("hospitalId")hospitalId:Int): BloodComponentOptionsData

    @GET("$BB_COMPONENT_DEPARTMENT_PREFIX/$REPORTS_PREFIX/$PROCESSING_REPORTS_PREFIX/$INDEX_PREFIX/by-hospital-and-date")
    suspend fun indexStockByHospitalAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodProcessing>>

    @GET("$BB_COMPONENT_DEPARTMENT_PREFIX/$REPORTS_PREFIX/$PROCESSING_REPORTS_PREFIX/$DAILY_REPORTS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexDailyProcessingByHospital(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodProcessing>>

}
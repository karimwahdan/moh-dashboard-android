package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodCollectionBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIncinerationBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationReasonsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.MonthlyIncinerationSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BB_ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BLOOD_BANKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DAILY_BLOOD_COLLECTION_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DAILY_BLOOD_STOCKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DONATION_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.INCINERATION_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPTIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BloodBankIssuingDepartmentIncinerationApi {

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$INCINERATION_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<MonthlyIncineration>>

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/$INCINERATION_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeMonthlyIncinerationNormal(@Body body:List<MonthlyIncinerationBody>): ApiResponse<PaginationData<MonthlyIncineration>>

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$INCINERATION_PREFIX/$OPTIONS_PREFIX")
    suspend fun options(@Query("hospitalId")hospitalId: Int): IncinerationOptionsData

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByHospitalAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital-today")
    suspend fun indexStockByHospitalToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$DONATION_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndSectorToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndSectorAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndCityToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndCityAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndHospitalTypeToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupAndHospitalTypeAndDate(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<DailyBloodStock>>

}
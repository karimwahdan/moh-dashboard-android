package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces

import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodImportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodNearExpiredItemBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIssuingReportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BBKpiOptionsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.BloodBankKpisResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.BloodImportSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.BloodNearExpiredItemSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStocksResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.MonthlyIssuingReportSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BB_ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BLOOD_BANKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BLOOD_IMPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DAILY_BLOOD_STOCKS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.DONATION_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.INCINERATION_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ISSUING_DEPARTMENT_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.ISSUING_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.MONTHLY_REPORTS_PREFIX
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.HOME_PREFIX
import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.INDEX_PREFIX
import com.kwdevs.hospitalsdashboard.routes.NORMAL_PREFIX
import com.kwdevs.hospitalsdashboard.routes.OPTIONS_PREFIX
import com.kwdevs.hospitalsdashboard.routes.STORE_PREFIX
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BloodBankIssuingDepartmentApi {

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$ISSUING_REPORTS_PREFIX/$MONTHLY_REPORTS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByBloodBank(@Query("page")page:Int, @Query("bloodBankId")bloodBankId:Int):ApiResponse<PaginationData<MonthlyIssuingReport>>

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/$ISSUING_REPORTS_PREFIX/$MONTHLY_REPORTS_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX")
    suspend fun storeMonthlyIssuingReportNormal(@Body body:MonthlyIssuingReportBody): MonthlyIssuingReportSingleResponse

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$INCINERATION_PREFIX/$OPTIONS_PREFIX")
    suspend fun options(@Query("hospitalId")hospitalId: Int): IncinerationOptionsData

    @POST("$HOME_PREFIX/export-blood-stock")
    suspend fun exportStock(@Body body:DailyBloodStockFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-city-kpi")
    suspend fun exportCityKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-insurance-kpi")
    suspend fun exportInsuranceKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-curative-kpi")
    suspend fun exportCurativeKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-specialized-kpi")
    suspend fun exportSpecializedKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-nbts-kpi")
    suspend fun exportNBTSKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/export-educational-kpi")
    suspend fun exportEducationalKpi(@Body body: KpiFilterBody): Response<ResponseBody>

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/filter")
    suspend fun filter(@Body body:DailyBloodStockFilterBody):DailyBloodStocksResponse

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$ISSUING_REPORTS_PREFIX/$MONTHLY_REPORTS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexByHospital(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<MonthlyIssuingReport>>

    @GET("$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$DONATION_DEPARTMENT_PREFIX/$DAILY_BLOOD_STOCKS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexStockByBloodGroupToday(@Query("page")page:Int, @Query("hospitalId")hospitalId:Int):ApiResponse<PaginationData<MonthlyIssuingReport>>

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

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/$BLOOD_IMPORTS_PREFIX/$INDEX_PREFIX/by-hospital")
    suspend fun indexImportsByHospital(@Query("page")page:Int, @Query("bloodBankId")bloodBankId:Int):ApiResponse<PaginationData<BloodImport>>

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/$BLOOD_IMPORTS_PREFIX/$STORE_PREFIX/normal")
    suspend fun storeBloodImport(@Body body:BloodImportBody):BloodImportSingleResponse

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/kpi/options")
    suspend fun kpiOptions(@Query("hospitalId")hospitalId: Int,
                           @Query("year")year:String,
                           @Query("month")month:String,
                           @Query("departmentId")departmentId:Int,
                           ):BBKpiOptionsResponse

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/kpi/index")
    suspend fun indexKpi(@Body body:KpiBody):BloodBankKpisResponse
    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/kpi/store/multiple")
    suspend fun storeMultipleKpiItems(@Body bodies:List<KpiBody>):BloodBankKpisResponse

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/near-expiry/index/by-hospital")
    suspend fun indexMyNearExpiryBloodUnits(@Query("hospitalId")hospitalId: Int):ApiResponse<PaginationData<BloodNearExpiredItem>>

    @GET("$BB_ISSUING_DEPARTMENT_PREFIX/near-expiry/index/all")
    suspend fun indexOtherNearExpiryBloodUnits(@Query("hospitalId")hospitalId: Int):ApiResponse<PaginationData<BloodNearExpiredItem>>

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/near-expiry/store")
    suspend fun storeNearExpiryBloodUnits(@Body body: BloodNearExpiredItemBody):BloodNearExpiredItemSingleResponse

    @POST("$BB_ISSUING_DEPARTMENT_PREFIX/near-expiry/update")
    suspend fun updateNearExpiryBloodUnits(@Body body: BloodNearExpiredItemBody):BloodNearExpiredItemSingleResponse

}
package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.KpiType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodImportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIssuingReportBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodImport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIssuingReport
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.BloodImportSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStocksResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.MonthlyIssuingReportSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class BloodBankIssuingDepartmentController : ViewModel() {

    private val api = BloodBankCaller().bloodBankIssuingDepartmentApi()

    val hospital=Preferences.Hospitals().get()

    val user= Preferences.User().get()

    private val data = MutableLiveData<UiState<DailyBloodStocksResponse>>()
    val state: LiveData<UiState<DailyBloodStocksResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalClinicSingleResponse>>()
    val singleState: LiveData<UiState<HospitalClinicSingleResponse>> get() = datum

    private val monthlyIssuingReportsDatum = MutableLiveData<UiState<MonthlyIssuingReportSingleResponse>>()
    val monthlyIssuingReportsSingleState: LiveData<UiState<MonthlyIssuingReportSingleResponse>> get() = monthlyIssuingReportsDatum

    private val monthlyIssuingReportsPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<MonthlyIssuingReport>>>>()
    val monthlyIssuingReportsPaginationState: LiveData<UiState<ApiResponse<PaginationData<MonthlyIssuingReport>>>> get() = monthlyIssuingReportsPaginatedData

    private val dailyBloodStockDatum = MutableLiveData<UiState<DailyBloodStockSingleResponse>>()
    val dailyBloodStockSingleState: LiveData<UiState<DailyBloodStockSingleResponse>> get() = dailyBloodStockDatum

    private val dailyBloodStocksPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>>()
    val dailyBloodStocksPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>> get() = dailyBloodStocksPaginatedData

    private val bloodImportsPaginatedData=MutableLiveData<UiState<ApiResponse<PaginationData<BloodImport>>>>()
    val bloodImportsPaginationState:LiveData<UiState<ApiResponse<PaginationData<BloodImport>>>> get() = bloodImportsPaginatedData

    private val bloodImportsSingleData=MutableLiveData<UiState<BloodImportSingleResponse>>()
    val bloodImportsSingleState:LiveData<UiState<BloodImportSingleResponse>> get() = bloodImportsSingleData

    private val directorateKpiExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val directorateKpiExcelState: LiveData<UiState<Response<ResponseBody>>> get() = directorateKpiExcel

    private val insuranceExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val insuranceExcelState: LiveData<UiState<Response<ResponseBody>>> get() = insuranceExcel

    private val educationalExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val educationalExcelState: LiveData<UiState<Response<ResponseBody>>> get() = educationalExcel

    private val curativeExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val curativeExcelState: LiveData<UiState<Response<ResponseBody>>> get() = curativeExcel

    private val specializedExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val specializedExcelState: LiveData<UiState<Response<ResponseBody>>> get() = specializedExcel

    private val nbtsExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val nbtsExcelState: LiveData<UiState<Response<ResponseBody>>> get() = nbtsExcel

    private val stockExcel = MutableLiveData<UiState<Response<ResponseBody>>>()
    val stockExcelState: LiveData<UiState<Response<ResponseBody>>> get() = stockExcel

    private val hospitalsData = MutableLiveData<UiState<HospitalsResponse>>()
    val hospitalsStateState: LiveData<UiState<HospitalsResponse>> get() = hospitalsData

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                data.value=UiState.Reload
                bloodImportsSingleData.value=UiState.Reload
                paginatedData.value=UiState.Loading
                monthlyIssuingReportsPaginatedData.value=UiState.Loading
            }
        }
    }

    fun filter(filterBody: DailyBloodStockFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value = UiState.Loading}
                val response = api.filter(filterBody)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun filterHospitalsBloodStock(filterBody: DailyBloodStockFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalsData.value = UiState.Loading}
                val response = api.filterHospitalBloodStocks(filterBody)
                withContext(Dispatchers.Main) {hospitalsData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun saveStockExcelFile(filterBody: DailyBloodStockFilterBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){stockExcel.value = UiState.Loading}
                val response = api.exportStock(filterBody)
                withContext(Dispatchers.Main) {stockExcel.value = UiState.Success(response) }

            }
            catch (e: Exception) {withContext(Dispatchers.Main) {stockExcel.value = UiState.Error(error(e))}}
        }
    }
    fun saveKpiExcelFile(kpiType: KpiType,kpiBody: KpiFilterBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val liveData=when(kpiType){
                    KpiType.NBTS->{nbtsExcel}
                    KpiType.CURATIVE->{curativeExcel}
                    KpiType.INSURANCE->{insuranceExcel}
                    KpiType.DIRECTORATE->{directorateKpiExcel}
                    KpiType.SPECIALIZED->{specializedExcel}
                    KpiType.EDUCATIONAL->{educationalExcel}
                }
                withContext(Dispatchers.Main){liveData.value = UiState.Loading}
                val response = when (kpiType) {
                    KpiType.NBTS -> api.exportNBTSKpi(kpiBody)
                    KpiType.INSURANCE -> api.exportInsuranceKpi(kpiBody)
                    KpiType.EDUCATIONAL->api.exportEducationalKpi(kpiBody)
                    KpiType.DIRECTORATE->api.exportCityKpi(kpiBody)
                    KpiType.CURATIVE->api.exportCurativeKpi(kpiBody)
                    KpiType.SPECIALIZED->api.exportSpecializedKpi(kpiBody)
                }
                withContext(Dispatchers.Main) {liveData.value = UiState.Success(response) }
            }
            catch (e: Exception) {
                val liveData=when(kpiType){
                    KpiType.NBTS->{nbtsExcel}
                    KpiType.CURATIVE->{curativeExcel}
                    KpiType.INSURANCE->{insuranceExcel}
                    KpiType.DIRECTORATE->{directorateKpiExcel}
                    KpiType.SPECIALIZED->{specializedExcel}
                    KpiType.EDUCATIONAL->{educationalExcel}
                }
                withContext(Dispatchers.Main) { liveData.value = UiState.Error(error(e)) } }
        }
    }

    fun indexMonthlyIssuingReportsByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {monthlyIssuingReportsPaginatedData.value = UiState.Loading}
                val response = api.indexByHospital(page = page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {monthlyIssuingReportsPaginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    monthlyIssuingReportsPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun storeMonthlyIssuingReportNormal(reportBody: MonthlyIssuingReportBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {monthlyIssuingReportsDatum.value = UiState.Loading}
                val response = api.storeMonthlyIssuingReportNormal(body=reportBody)
                withContext(Dispatchers.Main) {monthlyIssuingReportsDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    monthlyIssuingReportsDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun indexBloodImports(page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {bloodImportsPaginatedData.value = UiState.Loading}
                val response = api.indexImportsByHospital(page=page, bloodBankId = hospital?.id?:0)
                withContext(Dispatchers.Main) {bloodImportsPaginatedData.value = UiState.Success(response)}
            }
            catch (e: Exception) { withContext(Dispatchers.Main) { bloodImportsPaginatedData.value = UiState.Error(error(e)) }}
        }
    }
    fun storeBloodImports(importBody: BloodImportBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {bloodImportsSingleData.value = UiState.Loading}
                val response = api.storeBloodImport(body = importBody)
                withContext(Dispatchers.Main) {bloodImportsSingleData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    bloodImportsSingleData.value = UiState.Error(error(e))
                }
            }
        }
    }

}
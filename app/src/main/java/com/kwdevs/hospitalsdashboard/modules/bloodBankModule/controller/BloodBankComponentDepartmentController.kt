package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicResponse
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodProcessingBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.BloodComponentOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment.DailyBloodProcessingSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.MonthlyIssuingReportSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BloodBankComponentDepartmentController : ViewModel() {

    private val api = BloodBankCaller().bloodBankComponentDepartmentApi()

    val hospital=Preferences.Hospitals().get()

    val user= Preferences.User().get()

    private val data = MutableLiveData<UiState<HospitalClinicResponse>>()
    val state: LiveData<UiState<HospitalClinicResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalClinicSingleResponse>>()
    val singleState: LiveData<UiState<HospitalClinicSingleResponse>> get() = datum

    private val dailyBloodProcessingDatum = MutableLiveData<UiState<DailyBloodProcessingSingleResponse>>()
    val dailyBloodProcessingSingleState: LiveData<UiState<DailyBloodProcessingSingleResponse>> get() = dailyBloodProcessingDatum

    private val monthlyIssuingReportsDatum = MutableLiveData<UiState<MonthlyIssuingReportSingleResponse>>()
    val monthlyIssuingReportsSingleState: LiveData<UiState<MonthlyIssuingReportSingleResponse>> get() = monthlyIssuingReportsDatum

    private val dailyBloodProcessingPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyBloodProcessing>>>>()
    val dailyBloodProcessingPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyBloodProcessing>>>> get() = dailyBloodProcessingPaginatedData

    private val optionsData = MutableLiveData<UiState<BloodComponentOptionsData>>()
    val optionsState: LiveData<UiState<BloodComponentOptionsData>> get() = optionsData

    private val dailyBloodStockDatum = MutableLiveData<UiState<DailyBloodStockSingleResponse>>()
    val dailyBloodStockSingleState: LiveData<UiState<DailyBloodStockSingleResponse>> get() = dailyBloodStockDatum

    private val dailyBloodStocksPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>>()
    val dailyBloodStocksPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>> get() = dailyBloodStocksPaginatedData

    fun options(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {optionsData.value = UiState.Loading}
                val response = api.options(hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {optionsData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    optionsData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun indexDailyProcessingByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodProcessingPaginatedData.value = UiState.Loading}
                val response = api.indexDailyProcessingByHospital(page = page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {dailyBloodProcessingPaginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodProcessingPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeDailyProcessingNormal(bloodProcessingBody: DailyBloodProcessingBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodProcessingDatum.value = UiState.Loading}
                val response = api.storeDailyProcessing(body=bloodProcessingBody)
                withContext(Dispatchers.Main) {dailyBloodProcessingDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodProcessingDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun updateDailyProcessingNormal(bloodProcessingBody: DailyBloodProcessingBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodProcessingDatum.value = UiState.Loading}
                val response = api.updateDailyProcessing(body=bloodProcessingBody)
                withContext(Dispatchers.Main) {dailyBloodProcessingDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodProcessingDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

}
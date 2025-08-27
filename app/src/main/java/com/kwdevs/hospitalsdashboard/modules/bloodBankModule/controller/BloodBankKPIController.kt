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
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodCollectionBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BBKpiOptionsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.BloodBankKpisResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BloodBankKPIController : ViewModel() {

    private val api = BloodBankCaller().bloodBankIssuingDepartmentApi()

    val hospital=Preferences.Hospitals().get()

    val user= Preferences.User().get()

    private val options = MutableLiveData<UiState<BBKpiOptionsResponse>>()
    val optionsState: LiveData<UiState<BBKpiOptionsResponse>> get() = options

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalClinicSingleResponse>>()
    val singleState: LiveData<UiState<HospitalClinicSingleResponse>> get() = datum

    private val dailyBloodCollectionDatum = MutableLiveData<UiState<DailyBloodCollectionSingleResponse>>()
    val dailyBloodCollectionSingleState: LiveData<UiState<DailyBloodCollectionSingleResponse>> get() = dailyBloodCollectionDatum

    private val dailyBloodCollectionsPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyBloodCollection>>>>()
    val dailyBloodCollectionsPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyBloodCollection>>>> get() = dailyBloodCollectionsPaginatedData

    private val dailyBloodStockDatum = MutableLiveData<UiState<DailyBloodStockSingleResponse>>()
    val dailyBloodStockSingleState: LiveData<UiState<DailyBloodStockSingleResponse>> get() = dailyBloodStockDatum

    private val kpisData = MutableLiveData<UiState<BloodBankKpisResponse>>()
    val kpisState: LiveData<UiState<BloodBankKpisResponse>> get() = kpisData

    fun options(year:String,month:String,departmentId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {options.value = UiState.Loading}
                val response = api.kpiOptions(year = year,month=month, departmentId = departmentId, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {options.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    options.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun indexKpis(kpiBody: KpiBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {kpisData.value = UiState.Loading}
                val response = api.indexKpi(body = kpiBody)
                withContext(Dispatchers.Main) {kpisData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    kpisData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun storeMultipleKpis(bodies: List<KpiBody>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {kpisData.value = UiState.Loading}
                val response = api.storeMultipleKpiItems(bodies = bodies)
                withContext(Dispatchers.Main) {kpisData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    kpisData.value = UiState.Error(error(e))
                }
            }
        }
    }

}
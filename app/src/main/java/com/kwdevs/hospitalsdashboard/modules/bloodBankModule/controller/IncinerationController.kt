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
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.MonthlyIncinerationBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationOptionsData
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationOptionsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.IncinerationReasonsResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.MonthlyIncinerationSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncinerationController : ViewModel() {

    private val api = BloodBankCaller().incinerationApi()

    val hospital=Preferences.Hospitals().get()
    val user= Preferences.User().get()

    private val optionsData = MutableLiveData<UiState<IncinerationOptionsData>>()
    val optionsState: LiveData<UiState<IncinerationOptionsData>> get() = optionsData

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<MonthlyIncineration>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<MonthlyIncineration>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<MonthlyIncinerationSingleResponse>>()
    val singleState: LiveData<UiState<MonthlyIncinerationSingleResponse>> get() = datum



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

    fun indexByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Loading}
                val response = api.indexByHospital(page=page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun storeMonthlyIncineration(bodies: List<MonthlyIncinerationBody>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Loading}
                val response = api.storeMonthlyIncinerationNormal(body = bodies)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

}
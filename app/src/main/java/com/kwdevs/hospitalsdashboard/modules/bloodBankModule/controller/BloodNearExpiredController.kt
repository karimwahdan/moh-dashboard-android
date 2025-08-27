package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodNearExpiredItemBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.BloodNearExpiredItemSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BloodNearExpiredController: ViewModel() {
    private val api = BloodBankCaller().bloodBankIssuingDepartmentApi()

    val hospital= Preferences.Hospitals().get()

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<BloodNearExpiredItem>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<BloodNearExpiredItem>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<BloodNearExpiredItemSingleResponse>>()
    val singleState: LiveData<UiState<BloodNearExpiredItemSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            try {withContext(Dispatchers.Main) {datum.value = UiState.Loading}}
            catch (e: Exception) {withContext(Dispatchers.Main) {datum.value = UiState.Error(error(e))}}
        }
    }
    fun indexMine(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Loading}
                val response = api.indexMyNearExpiryBloodUnits(hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun indexOther(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Loading}
                val response = api.indexOtherNearExpiryBloodUnits(hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun store(nearExpiredItemBody: BloodNearExpiredItemBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Loading}
                val response = api.storeNearExpiryBloodUnits(body=nearExpiredItemBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun update(nearExpiredItemBody: BloodNearExpiredItemBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Loading}
                val response = api.updateNearExpiryBloodUnits(body=nearExpiredItemBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }


}
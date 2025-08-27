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
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.donationDepartment.DailyBloodCollectionSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStockSingleResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.BloodBankCaller
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BloodBankController : ViewModel() {

    private val api = BloodBankCaller().bloodBankApi()

    val hospital=Preferences.Hospitals().get()

    val user= Preferences.User().get()

    private val data = MutableLiveData<UiState<HospitalClinicResponse>>()
    val state: LiveData<UiState<HospitalClinicResponse>> get() = data

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

    private val dailyBloodStocksPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>>()
    val dailyBloodStocksPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyBloodStock>>>> get() = dailyBloodStocksPaginatedData

    fun indexBloodCollectionsByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodCollectionsPaginatedData.value = UiState.Loading}
                val response = api.indexBloodCollectionByHospital(page = page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {dailyBloodCollectionsPaginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodCollectionsPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeDailyCollection(dailyBloodCollectionBody: DailyBloodCollectionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodCollectionDatum.value = UiState.Loading}
                val response = api.storeDailyBloodCollectionsNormal(body = dailyBloodCollectionBody)
                withContext(Dispatchers.Main) {dailyBloodCollectionDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodCollectionDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun indexBloodStocksByHospitalToday(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodStocksPaginatedData.value = UiState.Loading}
                val response = api.indexStockByHospitalToday(page = page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {dailyBloodStocksPaginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodCollectionsPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeDailyStock(dailyBloodStockBody: DailyBloodStockBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodStockDatum.value = UiState.Loading}
                val response = api.storeDailyBloodStockNormal(body = dailyBloodStockBody)
                withContext(Dispatchers.Main) {dailyBloodStockDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodStockDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun storeMultipleDailyStock(bodies: List<DailyBloodStockBody>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {dailyBloodStocksPaginatedData.value = UiState.Loading}
                val response = api.storeMultipleDailyBloodStockNormal(bodies = bodies)
                withContext(Dispatchers.Main) {dailyBloodStocksPaginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dailyBloodStocksPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

}
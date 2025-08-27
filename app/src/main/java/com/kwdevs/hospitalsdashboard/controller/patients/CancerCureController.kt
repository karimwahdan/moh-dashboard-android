package com.kwdevs.hospitalsdashboard.controller.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.CancerCureBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCure
import com.kwdevs.hospitalsdashboard.models.patients.cancerCures.CancerCureSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CancerCureController : ViewModel() {

    private val api = Callers().cancerCureApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    val patient=Preferences.Patients().get()
    private val data = MutableLiveData<UiState<ApiResponse<PaginationData<CancerCure>>>>()
    val state: LiveData<UiState<ApiResponse<PaginationData<CancerCure>>>> get() = data

    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<CancerCure>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<CancerCure>>>> get() = paginationData

    private val datum = MutableLiveData<UiState<CancerCureSingleResponse>>()
    val singleState: LiveData<UiState<CancerCureSingleResponse>> get() = datum

    fun indexByHospital(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByHospital(page=page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginationData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun indexByPatient(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByPatient(page=page,
                    patientId = patient?.id?:0,
                    hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginationData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeNormal(cacnerCureBody: CancerCureBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(body = cacnerCureBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }

        }
    }

}
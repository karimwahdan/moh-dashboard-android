package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionBedBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.ReceptionFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.reception.ReceptionFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.receptionBeds.ReceptionBedSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReceptionBedsController : ViewModel() {

    private val api = Callers().receptionBedsApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()

    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<ReceptionFrequency>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<ReceptionFrequency>>>> get() = paginationData

    private val datum = MutableLiveData<UiState<ReceptionBedSingleResponse>>()
    val singleState: LiveData<UiState<ReceptionBedSingleResponse>> get() = datum

    fun indexByHospital() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.indexByHospital(hospital?.id?:0)
                withContext(Dispatchers.Main) {datum.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }
        }
    }
    fun indexFrequencyByHospital(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexFrequenciesByHospital(page,hospital?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { paginationData.value = UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                ) }
            }
        }
    }
    fun storeFrequencyNormal(receptionFrequencyBody: ReceptionFrequencyBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeFrequencyNormal(receptionFrequencyBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }

    fun storeNormal(receptionBedBody: ReceptionBedBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(receptionBedBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }

}
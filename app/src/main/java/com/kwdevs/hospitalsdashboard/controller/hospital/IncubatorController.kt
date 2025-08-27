package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.IncubatorBody
import com.kwdevs.hospitalsdashboard.models.hospital.incubators.Incubator
import com.kwdevs.hospitalsdashboard.models.hospital.incubators.IncubatorSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncubatorController : ViewModel() {

    private val api = Callers().incubatorApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()

    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<Incubator>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<Incubator>>>> get() = paginationData

    private val datum = MutableLiveData<UiState<IncubatorSingleResponse>>()
    val singleState: LiveData<UiState<IncubatorSingleResponse>> get() = datum

    fun index(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByHospital(page,hospital?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginationData.value = UiState.Error(
                        com.kwdevs.hospitalsdashboard.controller.error(
                            e
                        )
                    )
                }
            }
        }
    }

    fun storeNormal(incubatorBody: IncubatorBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(incubatorBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }

}
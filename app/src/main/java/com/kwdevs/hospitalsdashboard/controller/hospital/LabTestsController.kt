package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.LabTestFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.labTests.LabTestFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LabTestsController : ViewModel() {

    private val api = Callers().labTestsApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()

    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<LabTestFrequency>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<LabTestFrequency>>>> get() = paginationData

    private val datum = MutableLiveData<UiState<LabTestFrequencySingleResponse>>()
    val singleState: LiveData<UiState<LabTestFrequencySingleResponse>> get() = datum

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

    fun storeNormal(labTestFrequencyBody: LabTestFrequencyBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(labTestFrequencyBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }

}
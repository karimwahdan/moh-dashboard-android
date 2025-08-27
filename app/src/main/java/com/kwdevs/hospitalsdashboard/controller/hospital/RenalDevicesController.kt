package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalDeviceBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.RenalWashFrequencyBody
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalDevicesResponse
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequency
import com.kwdevs.hospitalsdashboard.models.hospital.renal.RenalWashFrequencySingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RenalDevicesController : ViewModel() {

    private val api = Callers().renalDevicesApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<RenalDevicesResponse>>()
    val state: LiveData<UiState<RenalDevicesResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalRenalDevice>>>>()
    val paginatedState: LiveData<UiState<ApiResponse<PaginationData<HospitalRenalDevice>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<RenalDeviceSingleResponse>>()
    val singleState: LiveData<UiState<RenalDeviceSingleResponse>> get() = datum

    private val frequencyPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<RenalWashFrequency>>>>()
    val frequencyPaginatedState: LiveData<UiState<ApiResponse<PaginationData<RenalWashFrequency>>>> get() = frequencyPaginatedData

    private val frequencyDatum = MutableLiveData<UiState<RenalWashFrequencySingleResponse>>()
    val frequencySingleState: LiveData<UiState<RenalWashFrequencySingleResponse>> get() = frequencyDatum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                paginatedData.value=UiState.Reload
                frequencyPaginatedData.value=UiState.Reload
            }
        }
    }
    fun indexByHospital(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value=UiState.Reload}
                val response = api.indexByHospital(page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(
                        com.kwdevs.hospitalsdashboard.controller.error(
                            e
                        )
                    )
                }
            }
        }
    }
    fun storeNormal(deviceBody: RenalDeviceBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(deviceBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }
    fun indexFrequencyByDevice(page:Int,id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {frequencyPaginatedData.value=UiState.Reload}
                val response = api.indexFrequencyByDevice(page, deviceId = id)
                withContext(Dispatchers.Main) {frequencyPaginatedData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    frequencyPaginatedData.value = UiState.Error(
                        com.kwdevs.hospitalsdashboard.controller.error(
                            e
                        )
                    )
                }
            }
        }
    }

    fun storeFrequencyNormal(frequencyBody: RenalWashFrequencyBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {frequencyDatum.value = UiState.Reload}
                val response = api.storeFrequencyNormal(frequencyBody)
                withContext(Dispatchers.Main) {frequencyDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    frequencyDatum.value = UiState.Error(
                        com.kwdevs.hospitalsdashboard.controller.error(
                            e
                        )
                    )
                }
            }

        }
    }

}
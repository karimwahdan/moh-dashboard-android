package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalDeviceController : ViewModel() {

    private val api = Callers().hospitalDevicesApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()

    private val data = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalDevice>>>>()
    val state: LiveData<UiState<ApiResponse<PaginationData<HospitalDevice>>>> get() = data

    private val datum = MutableLiveData<UiState<HospitalDeviceSingleResponse>>()
    val singleState: LiveData<UiState<HospitalDeviceSingleResponse>> get() = datum

    private val usageDatum = MutableLiveData<UiState<HospitalDeviceUsageSingleResponse>>()
    val usageSingleState: LiveData<UiState<HospitalDeviceUsageSingleResponse>> get() = usageDatum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {usageDatum.value=UiState.Reload}
        }
    }
    fun index(page:Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){data.value=UiState.Reload}
                val response = api.indexByHospital(page,hospital?.id?:0)
                withContext(Dispatchers.Main){data.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){data.value=UiState.Error(error(e))}}
        }

    }

    fun view(id:Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response = api.indexByDevice(id)
                withContext(Dispatchers.Main){datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(error(e))}}
        }
    }

    fun storeByNormalUser(itemBody: HospitalDeviceBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {usageDatum.value=UiState.Reload}
                val response = api.storeByNormalUser(itemBody)
                withContext(Dispatchers.Main) {usageDatum.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    usageDatum.value = UiState.Error(error(e))
                }
            }
        }

    }

}
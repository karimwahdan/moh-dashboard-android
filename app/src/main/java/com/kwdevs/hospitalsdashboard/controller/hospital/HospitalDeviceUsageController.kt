package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceUsageBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalDeviceUsageController : ViewModel() {

    private val api = Callers().hospitalDeviceUsagesApi()
    val user= Preferences.User().get()

    private val data = MutableLiveData<UiState<HospitalDeviceSingleResponse>>()
    val state: LiveData<UiState<HospitalDeviceSingleResponse>> get() = data

    private val datum = MutableLiveData<UiState<HospitalDeviceUsageSingleResponse>>()
    val singleState: LiveData<UiState<HospitalDeviceUsageSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {datum.value=UiState.Reload}
        }
    }

    fun indexByDevice(id:Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){data.value=UiState.Reload}
                val response = api.indexByDevice(id)
                withContext(Dispatchers.Main){data.value=UiState.Success(response)}
            }
            catch (e:Exception){ withContext(Dispatchers.Main){data.value=UiState.Error(error(e))} }
        }
    }

    //fun view(id:Int) {}

    fun storeByNormalUser(itemBody: HospitalDeviceUsageBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.storeByNormalUser(itemBody)
                withContext(Dispatchers.Main) {datum.value=UiState.Success(response)}
            }
            catch (e: Exception) {withContext(Dispatchers.Main) {datum.value = UiState.Error(error(e))}}
        }
    }

    fun updateByNormalUser(itemBody: HospitalDeviceUsageBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.updateByNormalUser(itemBody)
                withContext(Dispatchers.Main) {datum.value=UiState.Success(response)}
            }
            catch (e: Exception) {withContext(Dispatchers.Main) {datum.value = UiState.Error(error(e))}}
        }
    }

}
package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.MorgueBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.devices.HospitalDeviceBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDeviceUsageSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.MorgueSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.options.MorgueOptionsData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MorgueController : ViewModel() {

    private val api = Callers().morgueApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()

    private val data = MutableLiveData<UiState<ApiResponse<PaginationData<Morgue>>>>()
    val state: LiveData<UiState<ApiResponse<PaginationData<Morgue>>>> get() = data

    private val optionsData = MutableLiveData<UiState<MorgueOptionsData>>()
    val optionsState: LiveData<UiState<MorgueOptionsData>> get() = optionsData

    private val datum = MutableLiveData<UiState<MorgueSingleResponse>>()
    val singleState: LiveData<UiState<MorgueSingleResponse>> get() = datum

    fun options() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){optionsData.value=UiState.Reload}
                val response = api.options()
                withContext(Dispatchers.Main){optionsData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){optionsData.value=UiState.Error(error(e))}}
        }
    }

    fun indexByHospital(page:Int) {
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

    fun storeByNormalUser(itemBody: MorgueBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.storeNormal(itemBody)
                withContext(Dispatchers.Main) {datum.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }

    }

}
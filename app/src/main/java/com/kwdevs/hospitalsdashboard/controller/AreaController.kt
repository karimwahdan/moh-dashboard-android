package com.kwdevs.hospitalsdashboard.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AreaController : ViewModel() {

    private val api = Callers().areaApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val datum = MutableLiveData<UiState<AreaWithHospitalDetailSingleResponse>>()
    val singleState: LiveData<UiState<AreaWithHospitalDetailSingleResponse>> get() = datum

    fun index(areaId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value=UiState.Loading}
                val response = api.areasWithCountIndex(areaId)
                withContext(Dispatchers.Main) {data.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun view(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Loading}
                val response = api.view(id)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }

        }
    }

}
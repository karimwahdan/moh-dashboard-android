package com.kwdevs.hospitalsdashboard.controller.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.CustomModelBody
import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse
import com.kwdevs.hospitalsdashboard.responses.settings.CustomModelSingleResponse
import com.kwdevs.hospitalsdashboard.responses.settings.CustomModelsResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TableController : ViewModel() {

    private val api = Callers().settingsApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<CustomModelsResponse>>()
    val state: LiveData<UiState<CustomModelsResponse>> get() = data


    private val datum = MutableLiveData<UiState<CustomModelSingleResponse>>()
    val singleState: LiveData<UiState<CustomModelSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {datum.value=UiState.Reload}
        }
    }
    fun store(tableBody:CustomModelBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.storeTable(tableBody)
                withContext(Dispatchers.Main) {datum.value= UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun edit(tableBody:CustomModelBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value=UiState.Reload}
                val response = api.updateTable(tableBody)
                withContext(Dispatchers.Main) {datum.value= UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }
    private fun error(e:Exception): ErrorResponse {
        return ErrorParser().errorResponse(e)
    }
}
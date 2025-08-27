package com.kwdevs.hospitalsdashboard.controller.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithHospitalDetailSingleResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class ReportsController : ViewModel() {

    private val api = Callers().reportsApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val datum = MutableLiveData<UiState<Response<ResponseBody>>>()
    val singleState: LiveData<UiState<Response<ResponseBody>>> get() = datum


    fun downLoadTodayBloodStock() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.getTodayBloodStock()
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }

        }
    }

}
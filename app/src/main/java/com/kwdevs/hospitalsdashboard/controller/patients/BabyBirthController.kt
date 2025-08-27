package com.kwdevs.hospitalsdashboard.controller.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.BabyBirthBody
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirth
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirthSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BabyBirthController : ViewModel() {

    private val api = Callers().babyBirthApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<ApiResponse<PaginationData<BabyBirth>>>>()
    val state: LiveData<UiState<ApiResponse<PaginationData<BabyBirth>>>> get() = data

    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<BabyBirth>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<BabyBirth>>>> get() = paginationData

    private val datum = MutableLiveData<UiState<BabyBirthSingleResponse>>()
    val singleState: LiveData<UiState<BabyBirthSingleResponse>> get() = datum

    fun indexByHospital(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByHospital(page=page, hospitalId = hospital?.id?:0)
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

    fun storeNormal(babyBirthBody: BabyBirthBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.storeNormal(body = babyBirthBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }

}
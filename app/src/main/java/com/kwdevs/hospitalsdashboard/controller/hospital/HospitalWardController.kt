package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalWardBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.controller.serverError
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.wards.HospitalWardsResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalWardSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import com.kwdevs.hospitalsdashboard.routes.interfaces.HospitalWardApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HospitalWardController : ViewModel() {

    private val api = Callers().wardsApi()
    val user= Preferences.User().get()
    val hospital = Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<HospitalWardsResponse>>()
    val state: LiveData<UiState<HospitalWardsResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalWard>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<HospitalWard>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalWardSingleResponse>>()
    val singleState: LiveData<UiState<HospitalWardSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                datum.value=UiState.Loading
                paginatedData.value=UiState.Loading
            }
        }
    }
    fun view(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun indexByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){paginatedData.value=UiState.Reload}
                val response = api.indexByHospital(page=page, hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val error = when (e) {
                        is HttpException -> {
                            val errorBody = e.response()?.errorBody()?.string()?:""
                            val parsedError = try {ErrorParser().parseError(errorBody)}
                            catch (parseEx: Exception) { null }
                            parsedError ?: serverError()
                        }
                        else -> ErrorResponse(code = 505, message = e.message ?: "Unknown error", errors = null)
                    }
                    paginatedData.value = UiState.Error(error)
                }
            }
        }
    }

    fun indexByTypeAndHospital(typeId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value = UiState.Reload}
                val response = api.indexByTypeAndHospital(hospital?.id?:0, typeId = typeId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
            }
        }
    }

    fun storeNormal(storeBody: HospitalWardBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Loading}
                val response = api.store(storeBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }

}
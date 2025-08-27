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
import com.kwdevs.hospitalsdashboard.bodies.hospital.OperationRoomBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.controller.serverError
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoomResponse
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoomSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class OperationRoomController : ViewModel() {

    private val api = Callers().operationRoomApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<OperationRoomResponse>>()
    val state: LiveData<UiState<OperationRoomResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<OperationRoom>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<OperationRoom>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<OperationRoomSingleResponse>>()
    val singleState: LiveData<UiState<OperationRoomSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                datum.value=UiState.Loading
                paginatedData.value=UiState.Loading
            }
        }
    }

    fun indexByHospital(page:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){paginatedData.value=UiState.Reload}
                val response = api.indexByHospital(page=page,hospital?.id?:0)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun store(operationRoomBody: OperationRoomBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response = api.store(body=operationRoomBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

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
                    datum.value = UiState.Error(error)
                }
            }
        }
    }

}
package com.kwdevs.hospitalsdashboard.controller.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientOperationBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperation
import com.kwdevs.hospitalsdashboard.models.patients.operations.PatientOperationSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.responses.options.OperationOptionsData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientOperationController : ViewModel() {

    private val api = Callers().patientOperationsApi()
    val user= Preferences.User().get()
    val hospital = Preferences.Hospitals().get()
    val patient = Preferences.Patients().get()
    val room = Preferences.OperationRooms().get()
    private val paginationData = MutableLiveData<UiState<ApiResponse<PaginationData<PatientOperation>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<PatientOperation>>>> get() = paginationData

    private val optionsData = MutableLiveData<UiState<OperationOptionsData>>()
    val optionsState: LiveData<UiState<OperationOptionsData>> get() = optionsData

    private val datum = MutableLiveData<UiState<PatientOperationSingleResponse>>()
    val singleState: LiveData<UiState<PatientOperationSingleResponse>> get() = datum

    fun options() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {optionsData.value=UiState.Reload}
                val response = api.options(hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {optionsData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { optionsData.value = UiState.Error(error(e))}
            }
        }
    }

    fun indexByHospital(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByHospital(page=page, hospitalId = hospital?.id?:0, userId = user?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { paginationData.value = UiState.Error(error(e))}
            }
        }
    }

    fun indexByPatient(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByPatient(page=page, patientId = patient?.id?:0, userId = user?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { paginationData.value = UiState.Error(error(e))}
            }
        }
    }

    fun indexByRoom(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginationData.value=UiState.Reload}
                val response = api.indexByRoom(page=page, roomId = room?.id?:0, userId = user?.id?:0)
                withContext(Dispatchers.Main) {paginationData.value=UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { paginationData.value = UiState.Error(error(e))}
            }
        }
    }

    fun store(patientOperationBody: PatientOperationBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.store(patientOperationBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }

        }
    }
    fun update(patientOperationBody: PatientOperationBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {datum.value = UiState.Reload}
                val response = api.update(patientOperationBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }

        }
    }

}
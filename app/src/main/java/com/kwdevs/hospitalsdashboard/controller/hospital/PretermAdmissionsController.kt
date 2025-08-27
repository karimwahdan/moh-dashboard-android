package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PretermAdmissionBody
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmission
import com.kwdevs.hospitalsdashboard.models.patients.preterms.PretermAdmissionSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PretermAdmissionsController : ViewModel() {
    private val admissionsApi = Callers().pretermAdmissionsApi()
    val user= Preferences.User().get()
    val hospital = Preferences.Hospitals().get()
    private val datum = MutableLiveData<UiState<PretermAdmissionSingleResponse>>()
    val singleState: LiveData<UiState<PretermAdmissionSingleResponse>> get() = datum

    private val admissionsData = MutableLiveData<UiState<ApiResponse<PaginationData<PretermAdmission>>>>()
    val admissionsState:LiveData<UiState<ApiResponse<PaginationData<PretermAdmission>>>> get() = admissionsData

    fun indexByHospital(page:Int=1){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionsData.value=UiState.Reload}
                val response=admissionsApi.indexByHospital(page=page,hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main){admissionsData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionsData.value=UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                )}
            }
        }

    }

    fun storeAdmission(admissionBody: PretermAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response=admissionsApi.store(admissionBody)
                withContext(Dispatchers.Main){datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                )}
            }
        }
    }
    fun updateAdmission(admissionBody: PretermAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response=admissionsApi.update(admissionBody)
                withContext(Dispatchers.Main){datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                )}
            }
        }
    }

    fun quit(admissionBody: PretermAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response=admissionsApi.quit(admissionBody)
                withContext(Dispatchers.Main){datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                )}
            }
        }
    }
    fun die(admissionBody: PretermAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response=admissionsApi.die(admissionBody)
                withContext(Dispatchers.Main){datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                )}
            }
        }
    }
}
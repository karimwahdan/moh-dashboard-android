package com.kwdevs.hospitalsdashboard.controller.patients

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientAdmissionBody
import com.kwdevs.hospitalsdashboard.bodies.patients.PatientBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmission
import com.kwdevs.hospitalsdashboard.models.patients.admissions.PatientAdmissionSingleResponse
import com.kwdevs.hospitalsdashboard.models.patients.PatientSingleResponse
import com.kwdevs.hospitalsdashboard.models.patients.PatientsResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientsController : ViewModel() {

    private val api = Callers().patientsApi()
    private val admissionsApi = Callers().wardAdmissionsApi()
    val user= Preferences.User().get()
    val hospital = Preferences.Hospitals().get()
    val ward = Preferences.Wards().get()
    val operationRoom=Preferences.OperationRooms().get()
    val patient = Preferences.Patients().get()
    private val data = MutableLiveData<UiState<ApiResponse<PaginationData<Patient>>>>()
    val state: LiveData<UiState<ApiResponse<PaginationData<Patient>>>> get() = data

    private val admissionData = MutableLiveData<UiState<PatientAdmissionSingleResponse>>()
    val admissionState:LiveData<UiState<PatientAdmissionSingleResponse>> get() = admissionData

    private val allData = MutableLiveData<UiState<PatientsResponse>>()
    val allState:LiveData<UiState<PatientsResponse>> get() = allData

    private val datum = MutableLiveData<UiState<PatientSingleResponse>>()
    val singleState: LiveData<UiState<PatientSingleResponse>> get() = datum

    private val admissionsData = MutableLiveData<UiState<ApiResponse<PaginationData<PatientAdmission>>>>()
    val admissionsState:LiveData<UiState<ApiResponse<PaginationData<PatientAdmission>>>> get() = admissionsData

    fun filter(nationalId:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {allData.value = UiState.Reload}
                val response = api.filter(nationalId = nationalId)
                withContext(Dispatchers.Main) {allData.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { allData.value = UiState.Error(
                    error(
                        e
                    )
                ) }
            }
        }
    }
    fun filterByCode(patientCode:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {allData.value = UiState.Reload}
                val response = api.filterByCode(hospitalId =hospital?.id?:0, patientCode = patientCode)
                withContext(Dispatchers.Main) {allData.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { allData.value = UiState.Error(
                    error(
                        e
                    )
                ) }
            }
        }
    }

    fun indexByHospital(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("PatientsController","${hospital?.id}")
                withContext(Dispatchers.Main) {data.value = UiState.Reload}
                val response = api.indexForHospital(page=page,hospitalId = hospital?.id?:0)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }


            }
        }
    }
    fun indexByWard(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value = UiState.Reload}
                val response = api.indexForWard(page=page,wardId = ward?.id?:0)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }


            }
        }
    }
    fun indexByOperationRoom(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value = UiState.Reload}
                val response = api.indexForOperationRoom(page=page, operationRoomId = operationRoom?.id?:0)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }


            }
        }
    }

    fun filterFemale(nationalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {allData.value = UiState.Reload}
                val response = api.filterFemale(nationalId = nationalId)
                withContext(Dispatchers.Main) {allData.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    allData.value = UiState.Error(error(e))
                }


            }
        }
    }

    fun store(patientBody: PatientBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value = UiState.Reload}
                val response = api.storeNormal(patientBody)
                withContext(Dispatchers.Main){datum.value = UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value = UiState.Error(error(e))}
            }
        }
    }

    fun indexAdmissionsByHospitalAndPatient(page:Int=1){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionsData.value=UiState.Reload}
                val response=admissionsApi.indexByHospitalAndPatient(page=page,hospitalId = hospital?.id?:0, patientId = patient?.id?:0)
                withContext(Dispatchers.Main){admissionsData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionsData.value=UiState.Error(error(e))}
            }
        }
    }

    fun storeAdmission(admissionBody: PatientAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionData.value=UiState.Reload}
                val response=admissionsApi.store(admissionBody)
                withContext(Dispatchers.Main){admissionData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionData.value=UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }
    fun transferAdmission(admissionBody: PatientAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionData.value=UiState.Reload}
                val response=admissionsApi.transfer(admissionBody)
                withContext(Dispatchers.Main){admissionData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionData.value=UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }
    fun updateAdmission(admissionBody: PatientAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionData.value=UiState.Reload}
                val response=admissionsApi.update(admissionBody)
                withContext(Dispatchers.Main){admissionData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionData.value=UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }

    fun quit(admissionBody: PatientAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionData.value=UiState.Reload}
                val response=admissionsApi.quit(admissionBody)
                withContext(Dispatchers.Main){admissionData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionData.value=UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }
    fun die(admissionBody: PatientAdmissionBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){admissionData.value=UiState.Reload}
                val response=admissionsApi.die(admissionBody)
                withContext(Dispatchers.Main){admissionData.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){admissionData.value=UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }

    fun update(patientBody: PatientBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value = UiState.Reload}
                val response = api.updateNormal(patientBody)
                withContext(Dispatchers.Main){datum.value = UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value = UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }
    fun delete(patientBody: PatientBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value = UiState.Reload}
                val response = api.deleteNormal(patientBody)
                withContext(Dispatchers.Main){datum.value = UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value = UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }
    fun restore(patientBody: PatientBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value = UiState.Reload}
                val response = api.restoreNormal(patientBody)
                withContext(Dispatchers.Main){datum.value = UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value = UiState.Error(
                    error(
                        e
                    )
                )}
            }
        }
    }


}
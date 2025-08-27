package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.ClinicVisitBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.clinics.HospitalClinicBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisit
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.DailyClinicVisitSingleResponse
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinic
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicResponse
import com.kwdevs.hospitalsdashboard.models.hospital.clinics.HospitalClinicSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalClinicController : ViewModel() {

    private val api = Callers().hospitalClinicsApi()

    val hospital=Preferences.Hospitals().get()

    val user= Preferences.User().get()

    private val data = MutableLiveData<UiState<HospitalClinicResponse>>()
    val state: LiveData<UiState<HospitalClinicResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<HospitalClinic>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalClinicSingleResponse>>()
    val singleState: LiveData<UiState<HospitalClinicSingleResponse>> get() = datum

    private val visitDatum = MutableLiveData<UiState<DailyClinicVisitSingleResponse>>()
    val visitSingleState: LiveData<UiState<DailyClinicVisitSingleResponse>> get() = visitDatum

    private val visitsPaginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<DailyClinicVisit>>>>()
    val visitsPaginationState: LiveData<UiState<ApiResponse<PaginationData<DailyClinicVisit>>>> get() = visitsPaginatedData

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                paginatedData.value=UiState.Reload
                visitsPaginatedData.value=UiState.Reload
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
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun store(clinicBody: HospitalClinicBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response = api.storeByNormalUser(body =clinicBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeVisit(visitBody: ClinicVisitBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {visitDatum.value = UiState.Reload}
                val response = api.storeVisits(body = visitBody)
                withContext(Dispatchers.Main) {visitDatum.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    visitDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun indexClinicVisits(page:Int,clinicId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){visitsPaginatedData.value=UiState.Reload}
                val response = api.indexVisitsByClinic(page=page, clinicId = clinicId)
                withContext(Dispatchers.Main) {visitsPaginatedData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    visitsPaginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

}
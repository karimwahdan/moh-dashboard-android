package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.ErrorParser
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.HospitalModuleBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.controller.serverError
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodBankBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.BloodBankSingleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.HospitalMainModuleCallers
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

class HospitalMainModuleController : ViewModel() {

    private val api = HospitalMainModuleCallers().hospitalMainModuleApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<HospitalsResponse>>()
    val state: LiveData<UiState<HospitalsResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<Hospital>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<Hospital>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalSingleResponse>>()
    val singleState: LiveData<UiState<HospitalSingleResponse>> get() = datum

    private val bloodBankDatum = MutableLiveData<UiState<BloodBankSingleResponse>>()
    val bloodBankSingleState: LiveData<UiState<BloodBankSingleResponse>> get() = bloodBankDatum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                datum.value=UiState.Loading
                paginatedData.value=UiState.Loading
            }
        }
    }
    fun storeBloodBankBySuperAdmin(bloodBankBody: BloodBankBody){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){bloodBankDatum.value=UiState.Reload}
                val response=api.storeBloodBankBySuperUser(bloodBankBody)
                withContext(Dispatchers.Main){ bloodBankDatum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){bloodBankDatum.value=UiState.Error(error(e))}
            }
        }
    }
    fun view(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response = api.view(id=id)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun bySector(page:Int, sectorId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){paginatedData.value=UiState.Reload}
                val response = api.indexBySector(page=page, sectorId = sectorId)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun byType(page:Int, typeId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Reload}
                val response = api.indexByType(page=page, typeId = typeId)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Error(error(e))}
            }
        }
    }

    fun filter(filterBody: HospitalFilterBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //withContext(Dispatchers.Main){data.value=UiState.Reload}
                val response = api.filterHospitals(filterBody)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
            }
        }
    }
    fun update(storeBody: HospitalBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Loading}
                val response = api.update(storeBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {datum.value = UiState.Error(error(e))}
            }
        }
    }


}
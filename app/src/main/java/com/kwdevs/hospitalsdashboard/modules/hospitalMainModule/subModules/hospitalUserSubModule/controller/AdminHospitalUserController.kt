package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.HospitalUserCallers
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminHospitalUserController : ViewModel() {

    private val api = HospitalUserCallers().adminHospitalUserApi()
    val user= Preferences.User().get()
    val hospital=Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<HospitalUsersSimpleResponse>>()
    val state: LiveData<UiState<HospitalUsersSimpleResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<SimpleHospitalUser>>>>()
    val paginatedState: LiveData<UiState<ApiResponse<PaginationData<SimpleHospitalUser>>>> get() = paginatedData

    private val fullHospitalUserDatum = MutableLiveData<UiState<HospitalUserSingleResponse>>()
    val fullHospitalUserSingleState: LiveData<UiState<HospitalUserSingleResponse>> get() = fullHospitalUserDatum

    private val simpleHospitalUserDatum = MutableLiveData<UiState<HospitalUserSSResponse>>()
    val simpleHospitalUserSingleState: LiveData<UiState<HospitalUserSSResponse>> get() = simpleHospitalUserDatum

    fun create(userBody: HospitalUserBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //withContext(Dispatchers.Main) { superUserDatum.value = UiState.Reload }
                val response = api.storeByAdmin(userBody)
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {fullHospitalUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }
    //updateRoles
    fun indexByHospital(page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { paginatedData.value = UiState.Reload }
                val response = api.indexByHospital(page,hospital?.id?:0)
                withContext(Dispatchers.Main) { paginatedData.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Error(error(e)) }
            }
        }
    }
    fun updateRoles(roleBody: UserRoleBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { simpleHospitalUserDatum.value = UiState.Reload }
                val response = api.updateRoles(roleBody)
                withContext(Dispatchers.Main) { simpleHospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {simpleHospitalUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }

}
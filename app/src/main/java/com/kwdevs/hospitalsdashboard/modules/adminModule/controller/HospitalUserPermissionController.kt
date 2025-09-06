package com.kwdevs.hospitalsdashboard.modules.adminModule.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.PermissionBody
import com.kwdevs.hospitalsdashboard.bodies.control.RoleBody
import com.kwdevs.hospitalsdashboard.modules.adminModule.routes.AdminModuleCallers
import com.kwdevs.hospitalsdashboard.responses.settings.RoleSingleResponse
import com.kwdevs.hospitalsdashboard.responses.settings.SinglePermissionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalUserPermissionController: ViewModel() {
    private val api = AdminModuleCallers().hospitalUserPermissionsApi()
    private val user= Preferences.User().getSuper()

    private val singlePermission = MutableLiveData<UiState<SinglePermissionResponse>>()
    val singlePermissionState: LiveData<UiState<SinglePermissionResponse>> get() = singlePermission

    private val singleRole = MutableLiveData<UiState<RoleSingleResponse>>()
    val singleRoleState: LiveData<UiState<RoleSingleResponse>> get() = singleRole

    fun reloadSinglePermission(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Reload }

            }
            catch (e:Exception){
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Reload }
            }
        }

    }
    fun reloadSingleRole(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value=UiState.Reload }

            }
            catch (e:Exception){
                withContext(Dispatchers.Main) { singleRole.value=UiState.Reload }
            }
        }

    }
    fun storeSingleHospitalUserPermission(input: PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Loading }
                val response = api.storePermission(input)
                withContext(Dispatchers.Main) {singlePermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singlePermission.value = UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                ) }
            }
        }
    }
    fun updateSingleHospitalUserPermission(input: PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Loading }
                val response = api.updatePermission(input)
                withContext(Dispatchers.Main) {singlePermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singlePermission.value = UiState.Error(
                    com.kwdevs.hospitalsdashboard.controller.error(
                        e
                    )
                ) }
            }
        }
    }


    fun storeHospitalUserRole(input: RoleBody){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value= UiState.Loading }
                val response = api.storeHospitalUserRole(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    singleRole.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }
    fun updateHospitalUserRole(input: RoleBody){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value= UiState.Loading }
                val response = api.updateHospitalUserRole(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    singleRole.value = UiState.Error(com.kwdevs.hospitalsdashboard.controller.error(e))
                }
            }

        }
    }


}
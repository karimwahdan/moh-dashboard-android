package com.kwdevs.hospitalsdashboard.controller.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.PermissionBody
import com.kwdevs.hospitalsdashboard.bodies.control.RoleBody
import com.kwdevs.hospitalsdashboard.bodies.control.RolePermissionsBody
import com.kwdevs.hospitalsdashboard.bodies.control.UserRoleBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.settings.permissions.PermissionsResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.responses.settings.PermissionDataResponse
import com.kwdevs.hospitalsdashboard.responses.settings.SinglePermissionResponse
import com.kwdevs.hospitalsdashboard.responses.settings.RoleSingleResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionsController : ViewModel() {

    private val api = Callers().settingsApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<PermissionDataResponse>>()
    val state: LiveData<UiState<PermissionDataResponse>> get() = data

    private val permissionsData = MutableLiveData<UiState<PermissionsResponse>>()
    val permissionsState: LiveData<UiState<PermissionsResponse>> get() = permissionsData

    private val singleUserDatum = MutableLiveData<UiState<HospitalUserSSResponse>>()
    val singleUserState: LiveData<UiState<HospitalUserSSResponse>> get() = singleUserDatum

    private val singleRole = MutableLiveData<UiState<RoleSingleResponse>>()
    val singleRoleState: LiveData<UiState<RoleSingleResponse>> get() = singleRole

    private val singlePermission = MutableLiveData<UiState<SinglePermissionResponse>>()
    val singlePermissionState: LiveData<UiState<SinglePermissionResponse>> get() = singlePermission

    private val singleSuperPermission = MutableLiveData<UiState<SinglePermissionResponse>>()
    val singleSuperPermissionState: LiveData<UiState<SinglePermissionResponse>> get() = singleSuperPermission

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                data.value=UiState.Reload
                singleRole.value=UiState.Loading
                singlePermission.value=UiState.Reload
                singleSuperPermission.value=UiState.Reload
            }
        }
    }
    fun reloadSingleRole(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                singleRole.value=UiState.Loading
            }
        }
    }
    fun updateHospitalUserRoles(roleBody: UserRoleBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleUserDatum.value = UiState.Reload }
                val response = api.updateHospitalUserRoles(roleBody)
                withContext(Dispatchers.Main) { singleUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {singleUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }


    fun index() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.permissionData()
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

    //Super

    fun addRoleToSuperUser(userId: Int,roleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.addSuperUserRole(userId=userId,roleId=roleId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun removeRoleFromSuperUser(userId: Int,roleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.removeSuperUserRole(userId=userId,roleId=roleId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeSingleSuperPermission(input:PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleSuperPermission.value=UiState.Loading }
                val response = api.storeSuperPermission(input)
                withContext(Dispatchers.Main) {singleSuperPermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singleSuperPermission.value = UiState.Error(error(e)) }
            }
        }
    }
    fun updateSingleSuperPermission(input:PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleSuperPermission.value=UiState.Loading }
                val response = api.updateSuperPermission(input)
                withContext(Dispatchers.Main) {singleSuperPermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singleSuperPermission.value = UiState.Error(error(e)) }
            }
        }
    }
    fun updateSuperUserRolePermissions(input: RolePermissionsBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value=UiState.Loading }
                val response = api.updateSuperRolePermissions(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singleRole.value = UiState.Error(error(e)) }
            }
        }
    }



    fun permissionsList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { permissionsData.value=UiState.Loading }
                val response = api.permissionsList()
                withContext(Dispatchers.Main) {permissionsData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    permissionsData.value = UiState.Error(error(e))
                }
            }

        }
    }

    //Hospital Users
    fun storeHospitalUserRole(input:RoleBody){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value=UiState.Loading }
                val response = api.storeHospitalUserRole(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    singleRole.value = UiState.Error(error(e))
                }
            }

        }
    }
    fun updateHospitalUserRole(input:RoleBody){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                //withContext(Dispatchers.Main) { singleRole.value=UiState.Loading }
                val response = api.updateRole(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    singleRole.value = UiState.Error(error(e))
                }
            }

        }
    }

    fun addRoleToHospitalUser(userId:Int, roleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.addUserRole(userId=userId,roleId=roleId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun removeRoleFromHospitalUser(userId:Int, roleId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.removeUserRole(userId=userId,roleId=roleId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun storeSingleHospitalUserPermission(input:PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Loading }
                val response = api.storePermission(input)
                withContext(Dispatchers.Main) {singlePermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singlePermission.value = UiState.Error(error(e)) }
            }
        }
    }
    fun updateSingleHospitalUserPermission(input:PermissionBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singlePermission.value=UiState.Loading }
                val response = api.updatePermission(input)
                withContext(Dispatchers.Main) {singlePermission.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singlePermission.value = UiState.Error(error(e)) }
            }
        }
    }

    fun updateHospitalUserRolePermissions(input: RolePermissionsBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { singleRole.value=UiState.Loading }
                val response = api.updateRolePermissions(input)
                withContext(Dispatchers.Main) {singleRole.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { singleRole.value = UiState.Error(error(e)) }
            }
        }
    }

    fun addSectorHead(userId:Int,sectorId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.addSectorHead(userId=userId,sectorId=sectorId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun addCityHead(userId:Int,cityId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { data.value=UiState.Loading }
                val response = api.addCityHead(userId=userId,cityId=cityId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }
            }
        }
    }

}
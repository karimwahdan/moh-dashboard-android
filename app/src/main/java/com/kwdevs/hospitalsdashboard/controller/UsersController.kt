package com.kwdevs.hospitalsdashboard.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.PasswordBody
import com.kwdevs.hospitalsdashboard.bodies.control.SuperUserBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.bodies.HospitalUserBody
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCountResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSSResponse
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUserSingleResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUserSingleResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersController : ViewModel() {

    private val api = Callers().usersApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<AreaWithCountResponse>>()
    val state: LiveData<UiState<AreaWithCountResponse>> get() = data

    private val hospitalUserDatum = MutableLiveData<UiState<HospitalUserSSResponse>>()
    val hospitalUserSingleState: LiveData<UiState<HospitalUserSSResponse>> get() = hospitalUserDatum

    private val fullHospitalUserDatum = MutableLiveData<UiState<HospitalUserSingleResponse>>()
    val fullHospitalUserSingleState: LiveData<UiState<HospitalUserSingleResponse>> get() = fullHospitalUserDatum

    private val superUserDatum = MutableLiveData<UiState<SimpleSuperUserSingleResponse>>()
    val superUserSingleState: LiveData<UiState<SimpleSuperUserSingleResponse>> get() = superUserDatum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main){
                    hospitalUserDatum.value=UiState.Reload
                    fullHospitalUserDatum.value=UiState.Reload
                    superUserDatum.value=UiState.Reload

                }
            }
            catch (e:Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
                    hospitalUserDatum.value=UiState.Reload
                    fullHospitalUserDatum.value=UiState.Reload
                    superUserDatum.value=UiState.Reload

                }
            }
        }
    }
    fun loginNormal(username:String,password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){hospitalUserDatum.value=UiState.Loading}
                val response = api.loginNormal(username,password)
                withContext(Dispatchers.Main) { hospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { hospitalUserDatum.value = UiState.Error(error(e)) }

            }
        }
    }

    fun loginSuper(username:String,password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Loading }
                val response = api.loginSuper(username,password)
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {superUserDatum.value = UiState.Error(error(e)) }

            }

        }
    }

    fun viewNormal(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("ViewNormal","UserName: ${user?.username} Password:${user?.password}")
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Loading }
                val response = api.viewNormal(user?.username,user?.password)
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {fullHospitalUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }
    fun create(userBody: HospitalUserBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Loading }
                val response = api.create(userBody)
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {fullHospitalUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }
    fun createSuper(userBody: SuperUserBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Loading }
                val response = api.createSuper(userBody)
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {superUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }

    fun changeSuperPassword(passwordBody: PasswordBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Loading }
                val response = api.changeSuperUserPassword(passwordBody)
                withContext(Dispatchers.Main) { superUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {superUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }
    fun changeHospitalUserPassword(passwordBody: PasswordBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Loading }
                val response = api.changeHospitalUserPassword(passwordBody)
                withContext(Dispatchers.Main) { fullHospitalUserDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {fullHospitalUserDatum.value = UiState.Error(error(e)) }
            }
        }
    }

}
package com.kwdevs.hospitalsdashboard.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreaSingleResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityController : ViewModel() {

    private val api = Callers().cityApi()
    val user= Preferences.User().get()
    private val data = MutableLiveData<UiState<CityWithCountResponse>>()
    val state: LiveData<UiState<CityWithCountResponse>> get() = data


    private val cityWithAreaDatum = MutableLiveData<UiState<CityWithAreaSingleResponse>>()
    val singleCityWithAreaState: LiveData<UiState<CityWithAreaSingleResponse>> get() = cityWithAreaDatum

    fun index() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {data.value = UiState.Reload}
                val response = api.citiesWithCountIndex()
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    data.value = UiState.Error(error(e))
                }


            }
        }
    }

    fun view(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //withContext(Dispatchers.Main) {cityWithAreaDatum.value = UiState.Reload}
                val response = api.view(id)
                withContext(Dispatchers.Main) {cityWithAreaDatum.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { cityWithAreaDatum.value = UiState.Error(error(e)) }
            }

        }
    }


}
package com.kwdevs.hospitalsdashboard.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city.CityBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.ComparativeBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.hospital.HospitalBloodBankKpiResponse
import com.kwdevs.hospitalsdashboard.responses.home.HomeResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeController : ViewModel() {

    private val api = Callers().settingsApi()
    val user= Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    private val datum = MutableLiveData<UiState<HomeResponse>>()
    val singleState: LiveData<UiState<HomeResponse>> get() = datum

    private val certainDirectorateKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val certainDirectorateState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = certainDirectorateKpiDatum

    private val citiesKpiDatum=MutableLiveData<UiState<CityBloodBankKpiResponse>>()
    val citiesKpiState: LiveData<UiState<CityBloodBankKpiResponse>> get() = citiesKpiDatum

    private val specializedKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val specializedKpiState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = specializedKpiDatum

    private val comparativeKpiDatum=MutableLiveData<UiState<ComparativeBloodBankKpiResponse>>()
    val comparativeKpiState: LiveData<UiState<ComparativeBloodBankKpiResponse>> get() = comparativeKpiDatum

    private val insuranceKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val insuranceKpiState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = insuranceKpiDatum

    private val curativeKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val curativeKpiState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = curativeKpiDatum

    private val educationalKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val educationalKpiState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = educationalKpiDatum

    private val nBTSKpiDatum=MutableLiveData<UiState<HospitalBloodBankKpiResponse>>()
    val nBTSKpiState: LiveData<UiState<HospitalBloodBankKpiResponse>> get() = nBTSKpiDatum
    fun getCertainDirectorateCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){certainDirectorateKpiDatum.value = UiState.Loading}
                val response = api.certainDirectorateCharts(kpiBody)
                withContext(Dispatchers.Main) {certainDirectorateKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    certainDirectorateKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun getDirectoratesCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){citiesKpiDatum.value = UiState.Loading}
                val response = api.cityCharts(kpiBody)
                withContext(Dispatchers.Main) {citiesKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    citiesKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun getInsuranceCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){insuranceKpiDatum.value = UiState.Loading}
                val response = api.insuranceCharts(kpiBody)
                withContext(Dispatchers.Main) {insuranceKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    insuranceKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun getEducationalCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){educationalKpiDatum.value = UiState.Loading}
                val response = api.educationalCharts(kpiBody)
                withContext(Dispatchers.Main) {educationalKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    educationalKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun getCurativeCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){curativeKpiDatum.value = UiState.Loading}
                val response = api.curativeCharts(kpiBody)
                withContext(Dispatchers.Main) {curativeKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    curativeKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun getNBTSCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){nBTSKpiDatum.value = UiState.Loading}
                val response = api.nBTSCharts(kpiBody)
                withContext(Dispatchers.Main) {nBTSKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    nBTSKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun getSpecializedCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){specializedKpiDatum.value = UiState.Loading}
                val response = api.specializedCharts(kpiBody)
                withContext(Dispatchers.Main) {specializedKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    specializedKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun getComparativeCharts(kpiBody: KpiFilterBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){comparativeKpiDatum.value = UiState.Loading}
                val response = api.comparativeCharts(kpiBody)
                withContext(Dispatchers.Main) {comparativeKpiDatum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    comparativeKpiDatum.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value = UiState.Loading}
                val response = api.home(userId=superUser?.id?:0)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }
}
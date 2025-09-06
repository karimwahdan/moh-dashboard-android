package com.kwdevs.hospitalsdashboard.controller.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.bodies.control.HospitalModuleBody
import com.kwdevs.hospitalsdashboard.bodies.control.SlugListBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalBody
import com.kwdevs.hospitalsdashboard.bodies.hospital.HospitalFilterBody
import com.kwdevs.hospitalsdashboard.controller.error
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.responses.ApiResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalSingleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.PaginationData
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HospitalController : ViewModel() {

    private val api = Callers().hospitalApi()
    val user= Preferences.User().get()
    val saved=Preferences.Hospitals().get()
    private val data = MutableLiveData<UiState<HospitalsResponse>>()
    val state: LiveData<UiState<HospitalsResponse>> get() = data

    private val paginatedData = MutableLiveData<UiState<ApiResponse<PaginationData<Hospital>>>>()
    val paginationState: LiveData<UiState<ApiResponse<PaginationData<Hospital>>>> get() = paginatedData

    private val datum = MutableLiveData<UiState<HospitalSingleResponse>>()
    val singleState: LiveData<UiState<HospitalSingleResponse>> get() = datum

    fun reload(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                datum.value=UiState.Loading
                paginatedData.value=UiState.Loading
            }
        }
    }
    fun addModulesToHospital(bodies:List<HospitalModuleBody>){
        viewModelScope.launch(Dispatchers.IO){
            try{
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response=api.addModulesToHospital(bodies)
                withContext(Dispatchers.Main){ datum.value=UiState.Success(response)}
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){datum.value=UiState.Error(error(e))}
            }
        }
    }
    fun indexAllHospitals(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){data.value=UiState.Loading}
                val response = api.indexAllHospitals()
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
            }
        }
    }
    fun indexDirectorateHospitals(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){data.value=UiState.Loading}
                val response = api.indexDirectorateHospitals()
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
            }
        }
    }
    fun view(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Reload}
                val response = api.view(id=saved?.id?:0)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    datum.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun indexBySector(sectorId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){data.value=UiState.Loading}
                val response = api.indexBySector(sectorId = sectorId)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun paginateBySector(page:Int, sectorId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){paginatedData.value=UiState.Loading}
                val response = api.paginateBySector(page=page, sectorId = sectorId)
                withContext(Dispatchers.Main) {paginatedData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    paginatedData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun paginateByType(page:Int, typeId:Int){
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
    fun byCity(citySlug:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){data.value=UiState.Loading}
                val response = api.indexByCitySlug(citySlug)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
            }
        }
    }
    fun indexByCitySlugList(slugs:SlugListBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){data.value=UiState.Loading}
                val response = api.indexByCitySlugList(slugs)
                withContext(Dispatchers.Main) {data.value = UiState.Success(response)}
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {data.value = UiState.Error(error(e))}
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
    fun store(storeBody: HospitalBody){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){datum.value=UiState.Loading}
                val response = api.store(storeBody)
                withContext(Dispatchers.Main) {datum.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {datum.value = UiState.Error(error(e))}
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
package com.kwdevs.hospitalsdashboard.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.PatientState
import com.kwdevs.hospitalsdashboard.models.settings.basicDepartments.BasicDepartmentResponse
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.models.settings.clinicVisitTypes.ClinicVisitTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.cureTypes.CureTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.statuses.StatusResponse
import com.kwdevs.hospitalsdashboard.models.settings.deviceTypes.DeviceTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.generalClinics.GeneralClinicResponse
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalTypeResponse
import com.kwdevs.hospitalsdashboard.models.settings.modules.Module
import com.kwdevs.hospitalsdashboard.models.settings.multipleReturns.CrudDeviceData
import com.kwdevs.hospitalsdashboard.models.settings.nationality.NationalitiesResponse
import com.kwdevs.hospitalsdashboard.models.settings.sector.SectorResponse
import com.kwdevs.hospitalsdashboard.models.settings.title.TitleResponse
import com.kwdevs.hospitalsdashboard.models.settings.wardTypes.WardTypeResponse
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.KpiFilterBody
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses.HospitalUsersSimpleResponse
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.ModulesResponse
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.HospitalWardAdmissionOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.MorgueOptionsData
import com.kwdevs.hospitalsdashboard.responses.options.TitlesTypesSectorsCitiesOptionsData
import com.kwdevs.hospitalsdashboard.responses.settings.PatientMedicalStatesResponse
import com.kwdevs.hospitalsdashboard.routes.Callers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class SettingsController : ViewModel() {

    private val api = Callers().settingsApi()
    val user= Preferences.User().get()
    val superUser=Preferences.User().getSuper()
    private val deviceStatusData = MutableLiveData<UiState<StatusResponse>>()
    val deviceStatusState: LiveData<UiState<StatusResponse>> get() = deviceStatusData

    private val citiesOptionsData=MutableLiveData<UiState<CityWithCountResponse>>()
    val citiesState:LiveData<UiState<CityWithCountResponse>> get() = citiesOptionsData
    private val deviceTypesData = MutableLiveData<UiState<DeviceTypeResponse>>()
    val deviceTypeState: LiveData<UiState<DeviceTypeResponse>> get() = deviceTypesData
    private val wardTypesData = MutableLiveData<UiState<WardTypeResponse>>()
    val wardTypeState: LiveData<UiState<WardTypeResponse>> get() = wardTypesData

    private val crudDeviceData = MutableLiveData<UiState<CrudDeviceData>>()
    val crudDeviceState: LiveData<UiState<CrudDeviceData>> get() = crudDeviceData

    private val basicDepartmentData = MutableLiveData<UiState<BasicDepartmentResponse>>()
    val basicDepartmentState: LiveData<UiState<BasicDepartmentResponse>> get() = basicDepartmentData

    private val sectorsData = MutableLiveData<UiState<SectorResponse>>()
    val sectorsState: LiveData<UiState<SectorResponse>> get() = sectorsData

    private val typesData = MutableLiveData<UiState<HospitalTypeResponse>>()
    val typesState: LiveData<UiState<HospitalTypeResponse>> get() = typesData

    private val titlesData = MutableLiveData<UiState<TitleResponse>>()
    val titlesState: LiveData<UiState<TitleResponse>> get() = titlesData

    private val morgueOptionsData = MutableLiveData<UiState<MorgueOptionsData>>()
    val morgueOptionsState:LiveData<UiState<MorgueOptionsData>> get() = morgueOptionsData

    private val nationalitiesData = MutableLiveData<UiState<NationalitiesResponse>>()
    val nationalitiesState:LiveData<UiState<NationalitiesResponse>> get() = nationalitiesData

    private val hospitalWardOptionsData = MutableLiveData<UiState<HospitalWardAdmissionOptionsData>>()
    val hospitalWardOptionsState:LiveData<UiState<HospitalWardAdmissionOptionsData>> get() = hospitalWardOptionsData

    private val medicalStatesData = MutableLiveData<UiState<PatientMedicalStatesResponse>>()
    val medicalStatesState:LiveData<UiState<PatientMedicalStatesResponse>> get() = medicalStatesData

    private val cureTypesStatesData = MutableLiveData<UiState<CureTypeResponse>>()
    val cureTypesStatesState:LiveData<UiState<CureTypeResponse>> get() = cureTypesStatesData

    private val generalClinicsStatesData = MutableLiveData<UiState<GeneralClinicResponse>>()
    val generalClinicsStatesState:LiveData<UiState<GeneralClinicResponse>> get() = generalClinicsStatesData

    private val clinicVisitTypesStatesData = MutableLiveData<UiState<ClinicVisitTypeResponse>>()
    val clinicVisitTypesStatesState:LiveData<UiState<ClinicVisitTypeResponse>> get() = clinicVisitTypesStatesData

    private val bloodOptionsData = MutableLiveData<UiState<BloodOptionsData>>()
    val bloodOptionsState:LiveData<UiState<BloodOptionsData>> get() = bloodOptionsData

    private val moduleOptionsData = MutableLiveData<UiState<ModulesResponse>>()
    val moduleOptionsState:LiveData<UiState<ModulesResponse>> get() = moduleOptionsData

    private val hospitalOptionsData = MutableLiveData<UiState<HospitalsResponse>>()
    val hospitalOptionsState:LiveData<UiState<HospitalsResponse>> get() = hospitalOptionsData


    private val titlesTypesSectorsCitiesOptionsData = MutableLiveData<UiState<TitlesTypesSectorsCitiesOptionsData>>()
    val titlesTypesSectorsCitiesOptionsState:LiveData<UiState<TitlesTypesSectorsCitiesOptionsData>> get() = titlesTypesSectorsCitiesOptionsData

    private val certainDirectorateBloodKpiExcelData = MutableLiveData<UiState<Response<ResponseBody>>>()
    val certainDirectorateBloodExcelState: LiveData<UiState<Response<ResponseBody>>> get() = certainDirectorateBloodKpiExcelData

    private val allowedReceiversData = MutableLiveData<UiState<HospitalUsersSimpleResponse>>()
    val allowedReceiversState: LiveData<UiState<HospitalUsersSimpleResponse>> get() = allowedReceiversData

    fun saveCertainDirectorateBloodKpiToExcel(filterBody: KpiFilterBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){certainDirectorateBloodKpiExcelData.value = UiState.Loading}
                val response = api.exportCertainDirectorateKpi(filterBody)
                withContext(Dispatchers.Main) {certainDirectorateBloodKpiExcelData.value = UiState.Success(response) }

            }
            catch (e: Exception) {withContext(Dispatchers.Main) {certainDirectorateBloodKpiExcelData.value = UiState.Error(error(e))}}
        }
    }
    fun sectorHospitalOptions(sectorId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Reload}
                val response = api.sectorHospitalOptions(sectorId)
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun typeHospitalOptions(typeId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Reload}
                val response = api.typeHospitalOptions(typeId)
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun allowedNotificationReceiversList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {allowedReceiversData.value = UiState.Reload}
                val response = api.getAllowedNotificationReceivers(superUser?.id?:0)
                withContext(Dispatchers.Main) {allowedReceiversData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    allowedReceiversData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun areaHospitalOptions(areaId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Reload}
                val response = api.areaHospitalOptions(areaId)
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun cityHospitalOptions(cityId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Reload}
                val response = api.cityHospitalOptions(cityId)
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun hospitalOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Reload}
                val response = api.hospitalOptions()
                withContext(Dispatchers.Main) {hospitalOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun modulesOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {moduleOptionsData.value = UiState.Reload}
                val response = api.modulesOptions()
                withContext(Dispatchers.Main) {moduleOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    moduleOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun titlesTypesSectorsCitiesOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {titlesTypesSectorsCitiesOptionsData.value = UiState.Reload}
                val response = api.titlesTypesSectorsCitiesOptions()
                withContext(Dispatchers.Main) {titlesTypesSectorsCitiesOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    titlesTypesSectorsCitiesOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun bloodOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {bloodOptionsData.value = UiState.Reload}
                val response = api.bloodOptions()
                withContext(Dispatchers.Main) {bloodOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    bloodOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun renalDeviceTypesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {deviceTypesData.value = UiState.Reload}
                val response = api.renalDeviceTypes()
                withContext(Dispatchers.Main) {deviceTypesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    deviceTypesData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun generalClinicsIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {generalClinicsStatesData.value = UiState.Reload}
                val response = api.clinicTypes()
                withContext(Dispatchers.Main) {generalClinicsStatesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    generalClinicsStatesData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun clinicVisitTypesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {clinicVisitTypesStatesData.value = UiState.Reload}
                val response = api.clinicVisitTypes()
                withContext(Dispatchers.Main) {clinicVisitTypesStatesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    clinicVisitTypesStatesData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun cureTypesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {cureTypesStatesData.value = UiState.Reload}
                val response = api.cancerCureTypes()
                withContext(Dispatchers.Main) {cureTypesStatesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    cureTypesStatesData.value = UiState.Error(error(e))
                }
            }
        }
    }

    fun nationalitiesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {nationalitiesData.value = UiState.Reload}
                val response = api.nationalities()
                withContext(Dispatchers.Main) {nationalitiesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    titlesData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun wardTypesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {wardTypesData.value = UiState.Reload}
                val response = api.wardTypes()
                withContext(Dispatchers.Main) {wardTypesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    wardTypesData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun wardAdmissionOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {hospitalWardOptionsData.value = UiState.Reload}
                val response = api.wardAdmissionOptions()
                withContext(Dispatchers.Main) {hospitalWardOptionsData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hospitalWardOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun titlesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {titlesData.value = UiState.Reload}
                val response = api.titles()
                withContext(Dispatchers.Main) {titlesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    titlesData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun morgueOptions(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){morgueOptionsData.value = UiState.Reload}
                val response = api.morgueOptions()
                withContext(Dispatchers.Main){morgueOptionsData.value = UiState.Success(response)}
            }
            catch (e:Exception){withContext(Dispatchers.Main){morgueOptionsData.value=UiState.Error(error(e))}}
        }
    }
    fun sectorsIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {sectorsData.value = UiState.Reload}
                val response = api.sectors()
                withContext(Dispatchers.Main) {sectorsData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    sectorsData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun typesIndex(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){typesData.value = UiState.Reload}
                val response = api.hospitalTypes()
                withContext(Dispatchers.Main) {typesData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    typesData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun crudDeviceOptions(hospitalId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {crudDeviceData.value = UiState.Reload}
                val response = api.crudDeviceOptions(hospitalId)
                withContext(Dispatchers.Main) {crudDeviceData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    crudDeviceData.value = UiState.Error(error(e))
                }
            }
        }

    }
    fun deviceStatusesIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {deviceStatusData.value = UiState.Reload}
                val response = api.deviceStatuses()
                withContext(Dispatchers.Main) {deviceStatusData.value = UiState.Success(response)}
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    deviceStatusData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun basicDepartmentsIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {basicDepartmentData.value = UiState.Reload}
                val response = api.basicDepartments()
                withContext(Dispatchers.Main) {basicDepartmentData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    basicDepartmentData.value = UiState.Error(error(e))
                }
            }
        }
    }
    fun citiesOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {citiesOptionsData.value = UiState.Loading}
                val response = api.citiesWithCount()
                withContext(Dispatchers.Main) {citiesOptionsData.value = UiState.Success(response)}

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    citiesOptionsData.value = UiState.Error(error(e))
                }
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.modules.Module
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.squareup.moshi.Json

data class SimpleHospital(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String?=null,

    @Json(name = "city_id")
    val cityId: Int?=null,

    @Json(name = "area_id")
    val areaId: Int?=null,

    @Json(name = "address")
    val address: String?=null,

    @Json(name = "sector_id")
    val sectorId: Int?=null,

    @Json(name = "type_id")
    val typeId: Int?=null,

    @Json(name = "longitude")
    val longitude: Float? = null,

    @Json(name = "latitude")
    val latitude: Float? = null,

    @Json(name = "active")
    val active: Boolean?=null,

    @Json(name = "is_nbts")
    val isNbts: Boolean?=null,


    @Json(name = "blood_bank")
    val bloodBank: BloodBank? = null,

    // BelongsTo relationships
    @Json(name = "city")
    val city: City?=null,  // Assuming you have a City data class

    @Json(name = "area")
    val area: Area?=null,  // Assuming you have an Area data class

    @Json(name = "sector")
    val sector: Sector?=null,  // Define this class similarly

    @Json(name = "type")
    val type: HospitalType?=null,  // Define this class too

    // Optional counts if API includes them
    @Json(name = "employees_count")
    val usersCount: Int = 0,

    @Json(name = "departments_count")
    val departmentsCount: Int = 0,

    @Json(name = "devices_count")
    val devicesCount: Int = 0,

    @Json(name = "renal_wards_count")
    val renalWardsCount: Int = 0,

    @Json(name = "wards_count")
    val wardsCount: Int = 0,

    @Json(name = "modules")
    val modules:List<Module> = emptyList(),
)

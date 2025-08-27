package com.kwdevs.hospitalsdashboard.models.hospital

import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.modules.Module
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankKpi
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class Hospital(
    @Json(name = "id")
    val id: Int?=null,

    @Json(name = "name")
    val name: String,

    @Json(name = "city_id")
    val cityId: Int?=null,

    @Json(name = "is_nbts")
    val isNBTS: Boolean?=null,

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

    // BelongsTo relationships
    @Json(name = "city")
    val city: CityWithCount?=null,  // Assuming you have a City data class

    @Json(name = "area")
    val area: AreaWithCount?=null,  // Assuming you have an Area data class

    @Json(name = "sector")
    val sector: Sector?=null,  // Define this class similarly

    @Json(name = "type")
    val type: HospitalType?=null,  // Define this class too

    // HasOne relationships
    @Json(name = "icu")
    val icu: IntensiveCare?=null,

    @Json(name = "morgue")
    val morgue: Morgue?=null,

    // HasMany relationships
    @Json(name = "employees")
    val users: List<SimpleHospitalUser>? = null,

    @Json(name = "departments")
    val departments: List<HospitalDepartment>? = null,

    @Json(name = "devices")
    val devices: List<HospitalDevice>? = null,

    @Json(name = "renal_devices")
    val renalDevices: List<HospitalRenalDevice>? = null,

    @Json(name = "wards")
    val wards: List<HospitalWard>? = null,

    @Json(name = "blood_bank")
    val bloodBank: BloodBank? = null,

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

    @Json(name = "updated_by_id")
    val updatedById: Int? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_user")
    val deletedAtUser: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null,

    @Json(name = "modules")
    val modules:List<Module> = emptyList(),

    @Json(name = "blood_kpis")
    val kpis:List<BloodBankKpi> = emptyList(),

)

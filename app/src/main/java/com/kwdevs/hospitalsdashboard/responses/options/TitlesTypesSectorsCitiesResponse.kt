package com.kwdevs.hospitalsdashboard.responses.options

import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationDepartmentType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.squareup.moshi.Json

data class TitlesTypesSectorsCitiesResponse(
    @Json(name = "titles")
    val titles: List<Title> = emptyList(),
    @Json(name = "types")
    val types:List<HospitalType> = emptyList(),
    @Json(name = "sectors")
    val sectors:List<Sector> = emptyList(),
    @Json(name = "cities")
    val cities:List<CityWithCount> = emptyList(),
)

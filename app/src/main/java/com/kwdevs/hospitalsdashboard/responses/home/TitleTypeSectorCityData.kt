package com.kwdevs.hospitalsdashboard.responses.home

import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.squareup.moshi.Json

data class TitleTypeSectorCityData(
    @Json(name = "sectors")
    val sectors:List<Sector>,

    @Json(name = "types")
    val types:List<HospitalType>,

    @Json(name = "cities")
    val cities:List<CityWithCount>,

    )

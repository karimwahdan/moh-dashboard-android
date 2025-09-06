package com.kwdevs.hospitalsdashboard.responses.home

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.title.Title
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.MonthlyIncineration
import com.squareup.moshi.Json

data class HomeData(
    @Json(name = "titles")
    val titles:List<Title> = emptyList(),

    @Json(name = "super_titles")
    val superTitles:List<Title> = emptyList(),
    @Json(name = "sectors")
    val sectors:List<Sector> = emptyList(),

    @Json(name = "types")
    val types:List<HospitalType> = emptyList(),

    @Json(name = "cities")
    val cities:List<CityWithCount> = emptyList(),


    @Json(name = "areas")
    val areas:List<AreaWithCount> = emptyList(),

    @Json(name = "hospitals")
    val hospitals:List<Hospital> = emptyList(),

    @Json(name = "blood_stocks")
    val bloodStocksToday:List<DailyBloodStock> = emptyList(),

    @Json(name = "incinerations")
    val incinerations:List<MonthlyIncineration> = emptyList()
    )

package com.kwdevs.hospitalsdashboard.responses.options

import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.squareup.moshi.Json

data class BloodOptionsResponse(
    @Json(name="blood_types")
    val bloodTypes:List<BasicModel> = emptyList(),
    @Json(name="blood_groups")
    val bloodGroups:List<BasicModel> = emptyList(),
    @Json(name="donation_types")
    val donationTypes:List<BasicModel> = emptyList(),

    @Json(name="statuses")
    val statuses:List<BasicModel> = emptyList(),
    @Json(name="campaign_types")
    val campaignTypes:List<BasicModel> = emptyList(),
    @Json(name = "hospitals")
    val hospitals:List<SimpleHospital> = emptyList(),
    @Json(name = "blood_banks")
    val bloodBanks:List<SimpleHospital> = emptyList(),
    @Json(name = "sectors")
    val sectors:List<Sector> = emptyList(),
    @Json(name = "hospital_types")
    val hospitalTypes:List<HospitalType> = emptyList(),

    )

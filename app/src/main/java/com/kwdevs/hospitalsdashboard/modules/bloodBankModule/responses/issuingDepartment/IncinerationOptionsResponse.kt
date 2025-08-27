package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.MorgueType
import com.kwdevs.hospitalsdashboard.models.settings.statuses.Status
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBankDepartment
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.IncinerationReason
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class IncinerationOptionsResponse(
    @Json(name = "bloodGroups")
    val bloodGroups:List<BasicModel> = emptyList(),
    @Json(name = "unitTypes")
    val types:List<BasicModel> = emptyList(),
    @Json(name = "reasons")
    val reasons:List<IncinerationReason> = emptyList(),
    @Json(name = "campaigns")
    val campaigns:List<DailyBloodCollection> = emptyList(),
    @Json(name = "departments")
    val departments:List<BloodBankDepartment> = emptyList()
)

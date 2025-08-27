package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.componentDepartment

import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.donationDepartment.DailyBloodCollection
import com.squareup.moshi.Json

data class BloodComponentOptionsResponse(
    @Json(name="blood_types")
    val bloodTypes:List<BasicModel> = emptyList(),
    @Json(name="collections")
    val bloodCollections:List<DailyBloodCollection> = emptyList(),


    )

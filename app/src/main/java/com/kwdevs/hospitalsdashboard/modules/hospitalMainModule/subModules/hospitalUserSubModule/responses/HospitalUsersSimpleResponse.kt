package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.responses

import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.squareup.moshi.Json

data class HospitalUsersSimpleResponse(
    @Json(name = "data")
    val data:List<SimpleHospitalUser>
)

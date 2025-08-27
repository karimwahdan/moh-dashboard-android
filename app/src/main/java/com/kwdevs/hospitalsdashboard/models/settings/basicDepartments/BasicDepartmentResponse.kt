package com.kwdevs.hospitalsdashboard.models.settings.basicDepartments

import com.squareup.moshi.Json

data class BasicDepartmentResponse(
    @Json(name = "data")
    var data:List<BasicDepartment>
)

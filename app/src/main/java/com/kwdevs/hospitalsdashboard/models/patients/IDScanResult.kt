package com.kwdevs.hospitalsdashboard.models.patients

data class IDScanResult(
    val firstName: String?,
    val fullNameLine: String?,
    val nationalId: String?,
    val parsedFields: Map<String, String>
)

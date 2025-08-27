package com.kwdevs.hospitalsdashboard.responses.options

import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationDepartmentType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationType
import com.squareup.moshi.Json

data class OperationOptionsResponse(
    @Json(name = "department_types")
    val departmentTypes: List<OperationDepartmentType> = emptyList(),
    @Json(name = "operation_types")
    val operationTypes:List<OperationType> = emptyList(),
    @Json(name = "operation_rooms")
    val operationRooms:List<OperationRoom> = emptyList(),
    @Json(name = "statuses")
    val statuses:List<OperationStatus> = emptyList(),
)

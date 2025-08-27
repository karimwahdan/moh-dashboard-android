package com.kwdevs.hospitalsdashboard.models.patients.operations

import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationStatus
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationDepartmentType
import com.kwdevs.hospitalsdashboard.models.settings.operations.OperationType
import com.kwdevs.hospitalsdashboard.models.users.normal.SimpleHospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SimpleSuperUser
import com.squareup.moshi.Json

data class PatientOperation(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "hospital_id")
    val hospitalId:Int?=null,

    @Json(name = "patient_id")
    val patientId:Int?=null,

    @Json(name = "patient")
    val patient: Patient?=null,

    @Json(name = "operation_room_id")
    val operationRoomId:String?=null,

    @Json(name = "room")
    val operationRoom:OperationRoom?=null,

    @Json(name = "operation_type_id")
    val operationTypeId:String?=null,

    @Json(name = "type")
    val operationType: OperationType?=null,

    @Json(name = "operation_department_type_id")
    val operationDepartmentTypeId:Int?=null,

    @Json(name = "department_type")
    val departmentType: OperationDepartmentType?=null,

    @Json(name = "status")
    val status: OperationStatus?=null,

    @Json(name = "patient_died")
    val patientDied: Boolean?=null,

    @Json(name = "operation_time")
    val operationTime:String?=null,

    @Json(name = "exit_time")
    val exitTime:String?=null,

    @Json(name = "created_by_id")
    val createdById: Int? = null,

    @Json(name = "updated_by_id")
    val updatedById: Int? = null,

    @Json(name = "deleted_by_id")
    val deletedById: Int? = null,

    @Json(name = "created_by_super_id")
    val createdBySuperId: Int? = null,

    @Json(name = "updated_by_super_id")
    val updatedBySuperId: Int? = null,

    @Json(name = "updated_at_super")
    val updatedAtSuper: String? = null,

    @Json(name = "deleted_by_super_id")
    val deletedBySuperId: Int? = null,

    @Json(name = "deleted_at_user")
    val deletedAtUser: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "deleted_at")
    val deletedAt: String? = null,

    @Json(name = "created_by")
    val createdBy: SimpleHospitalUser? = null,

    @Json(name = "updated_by")
    val updatedBy: SimpleHospitalUser? = null,

    @Json(name = "deleted_by")
    val deletedBy: SimpleHospitalUser? = null,

    @Json(name = "created_by_super")
    val createdBySuper: SimpleSuperUser? = null,

    @Json(name = "updated_by_super")
    val updatedBySuper: SimpleSuperUser? = null,

    @Json(name = "deleted_by_super")
    val deletedBySuper: SimpleSuperUser? = null
)

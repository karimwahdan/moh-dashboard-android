package com.kwdevs.hospitalsdashboard.models.settings.permissions

import com.kwdevs.hospitalsdashboard.bodies.control.ConditionBody
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.models.settings.actionTypes.ActionType
import com.squareup.moshi.Json

data class Permission(
    @Json(name = "id")
    val id:Int?=null,

    @Json(name = "name")
    val name:String?=null,

    @Json(name = "slug")
    val slug:String?=null,

    @Json(name = "model_id")
    val modeId:Int?=null,

    @Json(name = "action_type_id")
    val actionTypeId:Int?=null,

    @Json(name = "columns")
    val columns:List<String>? = emptyList(),

    @Json(name = "conditions")
    val conditions:List<ConditionBody>? = emptyList(),

    @Json(name = "custom_model")
    val model:CustomModel?=null,

    @Json(name = "action_type")
    val actionType:ActionType?=null
)

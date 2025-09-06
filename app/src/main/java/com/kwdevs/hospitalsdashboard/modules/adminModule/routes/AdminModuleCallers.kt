package com.kwdevs.hospitalsdashboard.modules.adminModule.routes

import com.kwdevs.hospitalsdashboard.app.retrofit.RetrofitBuilder
import com.kwdevs.hospitalsdashboard.routes.interfaces.AreaApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.BabyBirthApi

class AdminModuleCallers {
    fun hospitalUserPermissionsApi(): HospitalUserPermissionsApi {return RetrofitBuilder.createService(HospitalUserPermissionsApi::class.java)}

    //B
    fun superUserPermissionsApi(): SuperUserPermissionsApi {return RetrofitBuilder.createService(SuperUserPermissionsApi::class.java)}

}
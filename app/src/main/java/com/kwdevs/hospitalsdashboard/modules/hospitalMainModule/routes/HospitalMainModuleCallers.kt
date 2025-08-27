package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes

import com.kwdevs.hospitalsdashboard.app.retrofit.RetrofitBuilder
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.routes.interfaces.HospitalMainModuleApi
import com.kwdevs.hospitalsdashboard.routes.interfaces.AreaApi

class HospitalMainModuleCallers {
    fun hospitalMainModuleApi(): HospitalMainModuleApi {return RetrofitBuilder.createService(HospitalMainModuleApi::class.java)}

}
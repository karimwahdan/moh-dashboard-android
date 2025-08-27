package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes

import com.kwdevs.hospitalsdashboard.app.retrofit.RetrofitBuilder
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.subModules.hospitalUserSubModule.routes.interfaces.HospitalUserApi

class HospitalUserCallers {

    fun adminHospitalUserApi():HospitalUserApi{return RetrofitBuilder.createService(HospitalUserApi::class.java)}
}
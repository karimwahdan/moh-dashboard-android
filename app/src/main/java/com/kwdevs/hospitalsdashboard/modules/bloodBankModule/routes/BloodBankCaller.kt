package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes

import com.kwdevs.hospitalsdashboard.app.retrofit.RetrofitBuilder
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces.BloodBankApi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces.BloodBankComponentDepartmentApi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces.BloodBankIssuingDepartmentApi
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.interfaces.BloodBankIssuingDepartmentIncinerationApi

class BloodBankCaller {
    fun bloodBankApi(): BloodBankApi {return RetrofitBuilder.createService(BloodBankApi::class.java)}

    fun incinerationApi(): BloodBankIssuingDepartmentIncinerationApi {return RetrofitBuilder.createService(BloodBankIssuingDepartmentIncinerationApi::class.java)}
    fun bloodBankIssuingDepartmentApi(): BloodBankIssuingDepartmentApi {return RetrofitBuilder.createService(BloodBankIssuingDepartmentApi::class.java)}
    fun bloodBankComponentDepartmentApi(): BloodBankComponentDepartmentApi{return RetrofitBuilder.createService(BloodBankComponentDepartmentApi::class.java)}

}
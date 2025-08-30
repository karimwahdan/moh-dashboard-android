package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.chartModels.city

import com.squareup.moshi.Json

data class CityComparativeBloodBankKpi(
    @Json(name = "city_id") val cityId:Int?=null,
    @Json(name = "city_name") val cityName:String?=null,
    @Json(name = "total_collected_curative"       ) val totalCollectedCurative:Int?=null,
    @Json(name = "total_collected_insurance"      ) val totalCollectedInsurance:Int?=null,
    @Json(name = "total_collected_educational"    ) val totalCollectedEducational:Int?=null,
    @Json(name = "total_collected_specialized"    ) val totalCollectedSpecialized:Int?=null,
    @Json(name = "total_collected_directorate"    ) val totalCollectedDirectorate:Int?=null,
    @Json(name = "total_collected_nbts"           ) val totalCollectedNBTS:Int?=null,

    @Json(name = "total_issued_curative"          ) val totalIssuedCurative:Int?=null,
    @Json(name = "total_issued_insurance"         ) val totalIssuedInsurance:Int?=null,
    @Json(name = "total_issued_educational"       ) val totalIssuedEducational:Int?=null,
    @Json(name = "total_issued_specialized"       ) val totalIssuedSpecialized:Int?=null,
    @Json(name = "total_issued_directorate"       ) val totalIssuedDirectorate:Int?=null,
    @Json(name = "total_issued_nbts"              ) val totalIssuedNBTS:Int?=null,

    @Json(name = "total_expired_blood_curative"   ) val totalExpiredBloodCurative:Int?=null,
    @Json(name = "total_expired_blood_insurance"  ) val totalExpiredBloodInsurance:Int?=null,
    @Json(name = "total_expired_blood_educational") val totalExpiredBloodEducational:Int?=null,
    @Json(name = "total_expired_blood_specialized") val totalExpiredBloodSpecialized:Int?=null,
    @Json(name = "total_expired_blood_directorate") val totalExpiredBloodDirectorate:Int?=null,
    @Json(name = "total_expired_blood_nbts"       ) val totalExpiredBloodNBTS:Int?=null,

    @Json(name = "total_hcv_curative"             ) val totalHcvCurative:Int?=null,
    @Json(name = "total_hcv_insurance"            ) val totalHcvInsurance:Int?=null,
    @Json(name = "total_hcv_educational"          ) val totalHcvEducational:Int?=null,
    @Json(name = "total_hcv_specialized"          ) val totalHcvSpecialized:Int?=null,
    @Json(name = "total_hcv_directorate"          ) val totalHcvDirectorate:Int?=null,
    @Json(name = "total_hcv_nbts"                 ) val totalHcvNBTS:Int?=null,

    @Json(name = "total_hbv_curative"             ) val totalHbvCurative:Int?=null,
    @Json(name = "total_hbv_insurance"            ) val totalHbvInsurance:Int?=null,
    @Json(name = "total_hbv_educational"          ) val totalHbvEducational:Int?=null,
    @Json(name = "total_hbv_specialized"          ) val totalHbvSpecialized:Int?=null,
    @Json(name = "total_hbv_directorate"          ) val totalHbvDirectorate:Int?=null,
    @Json(name = "total_hbv_nbts"                 ) val totalHbvNBTS:Int?=null,

    @Json(name = "total_hiv_curative"             ) val totalHivCurative:Int?=null,
    @Json(name = "total_hiv_insurance"            ) val totalHivInsurance:Int?=null,
    @Json(name = "total_hiv_educational"          ) val totalHivEducational:Int?=null,
    @Json(name = "total_hiv_specialized"          ) val totalHivSpecialized:Int?=null,
    @Json(name = "total_hiv_directorate"          ) val totalHivDirectorate:Int?=null,
    @Json(name = "total_hiv_nbts"                 ) val totalHivNBTS:Int?=null,

    @Json(name = "total_syphilis_curative"        ) val totalSyphilisCurative:Int?=null,
    @Json(name = "total_syphilis_insurance"       ) val totalSyphilisInsurance:Int?=null,
    @Json(name = "total_syphilis_educational"     ) val totalSyphilisEducational:Int?=null,
    @Json(name = "total_syphilis_specialized"     ) val totalSyphilisSpecialized:Int?=null,
    @Json(name = "total_syphilis_directorate"     ) val totalSyphilisDirectorate:Int?=null,
    @Json(name = "total_syphilis_nbts"            ) val totalSyphilisNBTS:Int?=null,

    @Json(name = "total_incomplete_curative"      ) val totalIncompleteCurative:Int?=null,
    @Json(name = "total_incomplete_insurance"     ) val totalIncompleteInsurance:Int?=null,
    @Json(name = "total_incomplete_educational"   ) val totalIncompleteEducational:Int?=null,
    @Json(name = "total_incomplete_specialized"   ) val totalIncompleteSpecialized:Int?=null,
    @Json(name = "total_incomplete_directorate"   ) val totalIncompleteDirectorate:Int?=null,
    @Json(name = "total_incomplete_nbts"          ) val totalIncompleteNBTS:Int?=null,

    @Json(name = "total_incomplete") val totalIncomplete:Int?=null,
)

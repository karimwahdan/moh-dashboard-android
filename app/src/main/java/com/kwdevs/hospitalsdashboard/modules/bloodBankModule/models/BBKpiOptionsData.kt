package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models

import com.squareup.moshi.Json

data class BBKpiOptionsData(
    @Json(name = "items") val items:List<BloodBankKpiItem>?= emptyList(),
    @Json(name = "blood_issuing") val bloodIssuing:Int?=null,
    @Json(name = "plasma_issuing") val plasmaIssuing:Int?=null,
    @Json(name = "rdp_issuing") val rdpIssuing:Int?=null,
    @Json(name = "pooled_issuing") val pooledIssuing:Int?=null,
    @Json(name = "expired_plasma") val expiredPlasma:Int?=null,
    @Json(name = "expired_rbc") val expiredRbc:Int?=null,
    @Json(name = "expired_platelets") val expiredPlatelets:Int?=null,
    @Json(name = "broken_plasma") val brokenPlasma:Int?=null,
    @Json(name = "returned") val returned:Int?=null,
    @Json(name = "dat_pos") val datPos:Int?=null,

    )

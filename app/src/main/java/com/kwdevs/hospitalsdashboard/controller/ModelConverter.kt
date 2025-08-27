package com.kwdevs.hospitalsdashboard.controller

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City

class ModelConverter {

    fun convertHospitalToSimple(item:Hospital): SimpleHospital {
        val simple= SimpleHospital(
            id=item.id?:0,
            name= item.name,
            active=item.active?:true,
            address = item.address,
            area= Area(id=item.areaId?:1, name = item.area?.name?:"", cityId = item.area?.cityId?:0),
            areaId = item.areaId,
            city= City(id=item.cityId?:0,name=item.city?.name?:""),
            cityId = item.cityId,
            bloodBank = item.bloodBank,
            sectorId = item.sectorId,
            isNbts = item.isNBTS,
            sector = item.sector,
            type=item.type,
            typeId = item.typeId,
            longitude = item.longitude,
            latitude = item.latitude,
            modules = item.modules,
            )
        return simple
    }
}
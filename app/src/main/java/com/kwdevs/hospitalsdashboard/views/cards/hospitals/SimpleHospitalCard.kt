package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.EMPLOYEE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_MULTIPLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_ONE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MEDICAL_DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MEDICAL_DEVICE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_WASH_DEVICE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun SimpleHospitalCard(item:SimpleHospital,navHostController: NavHostController){
    val name=item.name
    val address=item.address
    val city=item.city
    val area=item.area
    val type=item.type
    val sector=item.sector
    val active=item.active
    val usersCount=item.usersCount
    val wardsCount=item.wardsCount
    val renalWardsCount=item.renalWardsCount
    val devicesCount=item.devicesCount
    val departmentsCount=item.departmentsCount
    val wardLabel=if(wardsCount==1 || wardsCount > 10 ) IN_PATIENT_ONE_LABEL else IN_PATIENT_MULTIPLE_LABEL
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .shadow(elevation = 5.dp, shape = rcs(10))
        .background(color= Color.White, shape = rcs(10))
        .border(width = 1.dp, shape = rcs(10), color = TEAL700)){
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_info_white, containerSize = 26, background = TEAL700)
                HorizontalSpacer()
                Label(text=name?:"", fontWeight = FontWeight.Bold)
                HorizontalSpacer()
                sector?.let{Span(sector.name, color = Color.White, backgroundColor = TEAL700)}
                HorizontalSpacer()
                type?.let{Span(type.name, color = Color.White, backgroundColor = BLUE)}
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(if(active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
                Icon(R.drawable.ic_eye_blue, containerSize = 26)
            }

        }

        VerticalSpacer()
        Row(verticalAlignment = Alignment.CenterVertically){
            HorizontalSpacer(40)
            city?.let{ Span(city.name?:"", color = WHITE, backgroundColor = GRAY) }
            HorizontalSpacer()
            area?.let{ Span(area.name?:"", color = WHITE, backgroundColor = BLACK) }
            HorizontalSpacer()
            Label(address?:"")
            HorizontalSpacer()
        }
        VerticalSpacer()

        HorizontalDivider()
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Icon(R.drawable.ic_info_white, containerSize = 26, background = BLUE)
                HorizontalSpacer()
                Label(label = "$usersCount", text= EMPLOYEE_LABEL)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_info_white, containerSize = 26, background = BLUE)
                HorizontalSpacer()
                Label(label = " $departmentsCount ", text= MEDICAL_DEPARTMENTS_LABEL)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_info_white, containerSize = 26, background = BLUE)
                HorizontalSpacer()
                Label(label = "$wardsCount", text=wardLabel)
                HorizontalSpacer()
            }
        }


        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Icon(R.drawable.ic_info_white, containerSize = 26, background = BLUE)
                HorizontalSpacer()
                Label(label = "$renalWardsCount", text= RENAL_WASH_DEVICE_LABEL)
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(R.drawable.ic_info_white, containerSize = 26, background = BLUE)
                HorizontalSpacer()
                Label(label = "$devicesCount", text= MEDICAL_DEVICE_LABEL)
                HorizontalSpacer()
            }

        }

        VerticalSpacer()
    }
}

@Preview
@Composable
private fun Preview(){
    val item=SimpleHospital(
        id=1,name="Cairo Specialized Hospital",
        cityId = 1, areaId = 1, sectorId = 1,
        typeId = 1,
        address = "In front of Cairo Police Department",
        city = City(id=1, name = "Cairo"),
        area = Area(id=1, cityId = 1, name = "Old Cairo"),
        type = HospitalType(id=1, name = "SMC", icon = null, hospitalsCount = 0),
        sector = Sector(id=1, name = "Specialized",icon=null, hospitalsCount = 0),
        active = true,
        latitude = null, longitude = null,
        departmentsCount = 10,
        devicesCount = 800,
        usersCount = 1000,
        wardsCount = 10,
        renalWardsCount = 1,
    )
    SimpleHospitalCard(item=item, navHostController = rememberNavController())
}
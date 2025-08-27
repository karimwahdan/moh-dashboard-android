package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.hospital.RenalDevicesController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.models.hospital.IntensiveCare
import com.kwdevs.hospitalsdashboard.models.hospital.morgues.Morgue
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceCreateRoute
import com.kwdevs.hospitalsdashboard.routes.HospitalsViewRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.BLUE2
import com.kwdevs.hospitalsdashboard.views.assets.BURNS_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CCU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DEPUTY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DETAILED_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEVICES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FREE_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GENERAL_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_HEAD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.ICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IN_PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MORGUE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NEUROLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NICU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ONCOLOGY_CU_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RENAL_DEVICE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TEAL200
import com.kwdevs.hospitalsdashboard.views.assets.TEAL700
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.renalDevices.RenalDeviceCard
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsB

@Composable
fun HospitalCard(item:Hospital,navHostController: NavHostController){
    val name=item.name
    val area=item.area
    val city=item.city
    val active=item.active
    val address=item.address
    val sector=item.sector
    val type=item.type
    val departments=item.departments
    val renalDevices=item.renalDevices
    val devices=item.devices
    val morgue=item.morgue
    val icu=item.icu
    val wards=item.wards
    var showIcu by remember { mutableStateOf(false) }
    var showWards by remember { mutableStateOf(false) }
    var showRenalWards by remember { mutableStateOf(false) }
    var showMorgue by remember { mutableStateOf(false) }
    var showDevices by remember { mutableStateOf(false) }
    var showDepartments by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()
        .shadow(elevation = 5.dp, shape = rcs(20))
        .background(color= Color.White, shape = rcs(20))
        .border(width = 1.dp, shape = rcs(20), color = TEAL700)) {
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(R.drawable.ic_location_city_white, size = 26,
                    containerSize = 26,
                    background = TEAL700)
                HorizontalSpacer()
                Box(modifier = Modifier.clickable {
                    val simple=ModelConverter().convertHospitalToSimple(item)
                    Preferences.Hospitals().set(simple)
                    navHostController.navigate(HospitalsViewRoute.route)
                }){ Label(name) }
                HorizontalSpacer()
                Span(sector?.name?:"", color = WHITE, backgroundColor = BLUE)
                HorizontalSpacer()
                Span(type?.name?:"", color = WHITE, backgroundColor = BLACK)
            }
            Icon(if (active==true) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
        }
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically){
            Span(city?.name?:"", fontSize = 18, color = WHITE, backgroundColor = BLUE)
            HorizontalSpacer()
            Span(area?.name?:"", fontSize = 18, color = WHITE, backgroundColor = BLACK)

        }
        Row(modifier=Modifier.padding(horizontal = 5.dp)){
            Label(text=address?:"")
        }
        VerticalSpacer()
        Row(modifier=Modifier.fillMaxWidth().background(color= BLUE)
            .clickable { showDetails =!showDetails },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row{
                HorizontalSpacer()
                Span(DETAILED_DATA_LABEL, color = BLACK, backgroundColor = BLUE2)
            }
            Row{

                Icon(if (showDetails) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = BLUE2)
                HorizontalSpacer()
            }
        }
        VerticalSpacer()
        HorizontalDivider()

        VerticalSpacer()
        if(showDetails){
            icu?.let{
                Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                    .clickable { showIcu =!showIcu },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Row{
                        HorizontalSpacer()
                        Span(GENERAL_CU_LABEL, color = Color.White, backgroundColor = TEAL700)
                    }
                    Row{

                        Icon(if (showIcu) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                        HorizontalSpacer()
                    }
                }
                AnimatedVisibility(visible = showIcu) {
                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp).border(width = 1.dp, shape = rcsB(20), color = Color.LightGray).padding(horizontal = 5.dp)) {
                        ICUCard(it)
                    }
                    VerticalSpacer()

                }
                VerticalSpacer()
            }
            renalDevices?.let{
                if(renalDevices.isNotEmpty()){
                    Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                        .clickable { showRenalWards =!showRenalWards },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row{
                            HorizontalSpacer()
                            Span(RENAL_DEVICE_LABEL, color = Color.White, backgroundColor = TEAL700)
                        }
                        Row{

                            Icon(if (showRenalWards) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                            HorizontalSpacer()
                        }
                    }
                    AnimatedVisibility(visible = showRenalWards) {
                        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp).border(width = 1.dp, shape = rcsB(20), color = Color.LightGray).padding(horizontal = 5.dp)) {
                            VerticalSpacer(10)
                            renalDevices.forEach { item->
                                val renalDevicesController: RenalDevicesController = viewModel()

                                RenalDeviceCard(item,renalDevicesController)
                                VerticalSpacer()
                            }
                            VerticalSpacer()
                        }
                    }
                    VerticalSpacer()
                }

            }
            wards?.let{
                if(wards.isNotEmpty()){
                    Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                        .clickable { showWards =!showWards },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row{
                            HorizontalSpacer()
                            Span(IN_PATIENT_LABEL, color = Color.White, backgroundColor = TEAL700)
                        }
                        Row{
                            Icon(if (showWards) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                            HorizontalSpacer()
                        }
                    }
                    AnimatedVisibility(visible = showWards) {
                        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp).border(width = 1.dp, shape = rcsB(20), color = Color.LightGray).padding(horizontal = 5.dp)) {
                            VerticalSpacer(10)
                            wards.forEach { ward->
                                WardCard(ward,navHostController)
                                VerticalSpacer()
                            }
                            VerticalSpacer()
                        }
                    }
                    VerticalSpacer()

                }

            }
            morgue?.let {
                Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                    .clickable { showMorgue =!showMorgue },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Row{
                        HorizontalSpacer()
                        Span(MORGUE_LABEL, color = Color.White, backgroundColor = TEAL700)
                    }
                    Row{
                        Icon(if (showMorgue) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                        HorizontalSpacer()
                    }
                }
                AnimatedVisibility(visible = showMorgue) {
                    Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp).border(width = 1.dp, shape = rcsB(20), color = Color.LightGray).padding(horizontal = 5.dp)) {
                        VerticalSpacer(10)
                        MorgueCard(it)
                        VerticalSpacer(10)
                    }
                }
                VerticalSpacer()
            }

                    Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                        .clickable { showDevices =!showDevices },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            HorizontalSpacer()
                            Span(DEVICES_LABEL, color = Color.White, backgroundColor = TEAL700)
                            HorizontalSpacer()
                            Span(" ${devices?.size?:0} ", color = Color.White, backgroundColor = TEAL700)
                        }
                        Row{
                            Icon(if (showDevices) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                            HorizontalSpacer()
                        }
                    }
                    AnimatedVisibility(visible = showDevices) {
                        Column(modifier=Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp).border(width = 1.dp,
                                shape = rcsB(20), color = Color.LightGray)
                            .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                            VerticalSpacer(10)
                            CustomButton(
                                label=ADD_NEW_LABEL) {
                                val simple=ModelConverter().convertHospitalToSimple(item)
                                Preferences.Hospitals().set(simple)
                                navHostController.navigate(HospitalDeviceCreateRoute.route)
                            }
                            VerticalSpacer(10)

                            if(!devices.isNullOrEmpty()){
                                devices.forEach {
                                        device->
                                    DeviceCard(device)
                                    VerticalSpacer()
                                }
                            }

                            VerticalSpacer()
                        }
                    }
                    VerticalSpacer()

            departments?.let{
                if(departments.isNotEmpty()){
                    Row(modifier=Modifier.fillMaxWidth().background(color= TEAL200)
                        .clickable { showDepartments =!showDepartments },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            HorizontalSpacer()
                            Span(DEPARTMENTS_LABEL, color = Color.White, backgroundColor = TEAL700)
                            HorizontalSpacer()
                            Span(" ${departments.size} ", color = Color.White, backgroundColor = TEAL700)

                        }
                        Row{
                            Icon(if (showDepartments) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = TEAL700)
                            HorizontalSpacer()
                        }
                    }
                    AnimatedVisibility(visible = showDepartments) {
                        Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp).border(width = 1.dp, shape = rcsB(20), color = Color.LightGray).padding(horizontal = 5.dp)) {
                            VerticalSpacer(10)
                            departments.forEach { department->
                                DepartmentCard(department)
                                VerticalSpacer()
                            }
                            VerticalSpacer()
                        }
                    }
                    VerticalSpacer()

                }

            }
        }

        VerticalSpacer()

    }
}

@Composable
private fun DepartmentCard(item:HospitalDepartment){
    val members=item.members
    val name=item.name
    val head=item.head
    val deputy=item.deputy
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp).border(width = 1.dp, shape = RectangleShape, color = TEAL700)){
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                HorizontalSpacer()
                Label(name, fontSize = 16)
                HorizontalSpacer()
            }
            HorizontalSpacer()

        }
        VerticalSpacer()
        head?.let{
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Label(DEPARTMENT_HEAD_LABEL)
                HorizontalSpacer()
                Label(it.name, fontSize = 18)
                HorizontalSpacer()
                Span(text=it.title?.name?:"", fontSize = 16, color = Color.White, backgroundColor = Color.Black)
            }
        }
        VerticalSpacer()
        deputy?.let{
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Label(DEPARTMENT_DEPUTY_LABEL)
                HorizontalSpacer()
                Label(it.name, fontSize = 16)
                HorizontalSpacer()
                Span(text=it.title?.name?:"", fontSize = 14, color = Color.White, backgroundColor = Color.Black)
            }
        }
        VerticalSpacer()
        HorizontalDivider()
        VerticalSpacer()
        members.forEach {member->
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    HorizontalSpacer()
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    HorizontalSpacer()
                    Label(member.user.name)
                    HorizontalSpacer()
                    Span(text=member.user.title?.name?:"", color = Color.White, backgroundColor = TEAL700)
                }

            }
        }
        VerticalSpacer()
    }
}
@Composable
private fun HospitalMorgueCard(item: Morgue){
    val allBeds=item.allUnits
    val freeBeds=item.freeUnits
    val status=item.status
    val space=40
    if((status?.id?:0)==1){
        Row{
            HorizontalSpacer()
            Label(label = TOTAL_UNITS_LABEL,text="$allBeds")
            HorizontalSpacer(space)
            Label(label = FREE_UNITS_LABEL,text="$freeBeds")
        }
    }
}

@Composable
private fun ICUCard(item:IntensiveCare){

    val hasIcu=item.hasIcu
    val totalICuBeds=item.allIcuBeds
    val freeICuBeds=item.freeIcuBeds

    val hasNicu=item.hasNicu
    val totalNicuBeds=item.allNicuBeds
    val freeNicuBeds=item.freeNicuBeds

    val hasCCu=item.hasCcu
    val totalCCuBeds=item.allCcuBeds
    val freeCCuBeds=item.freeCcuBeds

    val hasOncology=item.hasOncologyCareUnit
    val totalOncologyCuBeds=item.allOncologyCareUnitBeds
    val freeOncologyCuBeds=item.freeOncologyCareUnitBeds

    val hasBurn=item.hasBurnCareUnit
    val totalBurnCuBeds=item.allBurnCareUnitBeds
    val freeBurnCuBeds=item.freeBurnCareUnitBeds

    val hasNeurologyBeds=item.hasNeuroCu
    val totalNeurologyCuBeds=item.allNeurologyCareUnitBeds
    val freeNeurologyCuBeds=item.freeNeurologyCareUnitBeds
    val space=80
    Column{
        if(hasIcu == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(ICU_LABEL)
                }
                Label(label = TOTAL_UNITS_LABEL,text="$totalICuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeICuBeds")
            }
        }
        if(hasNicu == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(NICU_LABEL)
                }
                Label(label = TOTAL_UNITS_LABEL,text="$totalNicuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeNicuBeds")
            }
        }
        if(hasCCu == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(CCU_LABEL)
                }
                Label(label = TOTAL_UNITS_LABEL,text="$totalCCuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeCCuBeds")
            }
        }
        if(hasOncology == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(ONCOLOGY_CU_LABEL)
                }

                Label(label = TOTAL_UNITS_LABEL,text="$totalOncologyCuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeOncologyCuBeds")
            }
        }
        if(hasBurn == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(BURNS_CU_LABEL)
                }
                Label(label = TOTAL_UNITS_LABEL,text="$totalBurnCuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeBurnCuBeds")
            }
        }
        if(hasNeurologyBeds == true){
            VerticalSpacer(10)
            Row{
                Row(modifier = Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                    Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                    Label(NEUROLOGY_CU_LABEL)
                }
                Label(label = TOTAL_UNITS_LABEL,text="$totalNeurologyCuBeds")
                HorizontalSpacer(space)
                Label(label = FREE_UNITS_LABEL,text="$freeNeurologyCuBeds")
            }
        }
        VerticalSpacer()
    }
}


@Composable
private fun DeviceCard(item: HospitalDevice){
    val name=item.name
    val code=item.code
    val department=item.department
    val status=item.status

    Column(modifier=Modifier.fillMaxWidth().padding(5.dp).border(width = 1.dp, shape = RectangleShape, color = TEAL700)){
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Icon(R.drawable.ic_info_white, background = TEAL700, containerSize = 26)
                HorizontalSpacer()
                Label(name)
                HorizontalSpacer()
            }
            status?.let {
                Span(text=status.name, fontSize = 16, color = Color.White, backgroundColor = hexToComposeColor(status.color))
            }
        }
        VerticalSpacer()
        Row(verticalAlignment = Alignment.CenterVertically){
            HorizontalSpacer()
            Label("كود")
            HorizontalSpacer()
            Span(text="$code", fontSize = 16, color = Color.White, backgroundColor = Color.Black)
        }
        VerticalSpacer()
        department?.let{
            Row(verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Label("قسم")
                Span(text=it.name, fontSize = 16, color = Color.White, backgroundColor = Color.Black)
            }
        }
        VerticalSpacer()
    }
}

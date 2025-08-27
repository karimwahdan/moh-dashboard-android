@file:OptIn(ExperimentalMaterial3Api::class)

package com.kwdevs.hospitalsdashboard.views.cards.hospitals.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.routes.HospitalDeviceUsagesRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor

@Composable
fun HospitalDeviceCard(item:HospitalDevice,navHostController: NavHostController){
    val id = item.id
    val hospitalId=item.hospitalId
    val hospital=item.hospital
    val type=item.type
    val name=item.name
    val code=item.code
    val department=item.department
    val status=item.status
    val basicDepartment=department?.basicDepartment

    ColumnContainer {
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(name, fontWeight = FontWeight.Bold)
                Label("($code)", fontWeight = FontWeight.Normal)
            }
            status?.let {
                Span(status.name, color = WHITE, backgroundColor = hexToComposeColor(status.color))
            }
        }
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            department?.let{Span(it.name, color = WHITE, backgroundColor = BLUE)}
            HorizontalSpacer()
            type?.let{Span(it.name, color = WHITE, backgroundColor = BLUE)}
            HorizontalSpacer()
            //department?.let{Span(it.name, color = WHITE, backgroundColor = BLUE)}
        }
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
            IconButton(R.drawable.ic_edit_blue, elevation = 6) { }
            HorizontalSpacer()
            IconButton(R.drawable.ic_delete_red, elevation = 6) { }
            HorizontalSpacer()
            IconButton(R.drawable.ic_frequency, elevation = 6) {
                Preferences.HospitalDevices().set(item)
                navHostController.navigate(HospitalDeviceUsagesRoute.route)
            }
            HorizontalSpacer()
        }
    }
}

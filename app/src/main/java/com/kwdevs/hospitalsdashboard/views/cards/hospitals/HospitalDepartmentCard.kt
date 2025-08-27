package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalDepartment
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_DEPUTY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DEPARTMENT_HEAD_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun HospitalDepartmentCard(item:HospitalDepartment,navHostController: NavHostController){
    val id = item.id
    val hospitalId=item.hospitalId
    val hospital=item.hospital
    val head=item.head
    val name=item.name
    val deputy=item.deputy
    val department=item.basicDepartment
    val active=item.active
    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(name, fontWeight = FontWeight.Bold)
                Icon(if(active) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)

            }
            department?.let{Span(it.name, color = WHITE, backgroundColor = BLUE)}

        }
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            head?.let{Label(label = DEPARTMENT_HEAD_LABEL,text=it.name, color = BLACK)}
            HorizontalSpacer()
            deputy?.let{Label(label= DEPARTMENT_DEPUTY_LABEL,text=it.name, color = BLACK)}
            HorizontalSpacer()
        }

    }
}
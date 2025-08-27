package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.PatientViewType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FREE_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_UNITS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun WardCard(item: HospitalWard,navHostController: NavHostController){
    val name=item.name
    val allBeds=item.allBeds
    val type=item.type
    val freeBeds=(allBeds?:0) - (item.admissions.size)
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .border(width = 1.dp, shape = rcs(20), color = Color.Gray)
        .padding(vertical = 5.dp)){
        VerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
            Icon(R.drawable.ic_info_white, background = colorResource(R.color.teal_700), containerSize = 26)
            HorizontalSpacer()
            Label(name)
            Icon(if (item.active) R.drawable.ic_check_circle_green else R.drawable.ic_cancel_red, containerSize = 26)
            HorizontalSpacer()
            type?.let{
                Span(it.name, backgroundColor = BLUE, color = WHITE)

            }
        }
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Box(modifier=Modifier.padding(5.dp)){Label(label = TOTAL_UNITS_LABEL,text="$allBeds")}
            Box(modifier=Modifier.padding(5.dp)){Label(label = FREE_UNITS_LABEL,text="$freeBeds")}
        }
        Row(modifier=Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            CustomButtonWithImage(icon = R.drawable.ic_patient, iconSize = 26, maxWidth = 52, label = PATIENTS_LABEL) {
                Preferences.ViewTypes().setPatientViewType(PatientViewType.BY_WARD)
                Preferences.Wards().set(item)
                navHostController.navigate(PatientsIndexRoute.route)
            }
            CustomButtonWithImage(icon=R.drawable.ic_edit_blue, iconSize = 26, maxWidth = 52, label = EDIT_LABEL) {

            }
        }


        VerticalSpacer()
    }
}
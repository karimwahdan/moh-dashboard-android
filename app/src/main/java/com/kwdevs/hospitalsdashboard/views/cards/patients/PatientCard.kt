package com.kwdevs.hospitalsdashboard.views.cards.patients

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientViewRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCER_CURE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DELETE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_PATIENT_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.LOG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_HAS_QUIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_IS_PRESENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PatientFullName
import com.kwdevs.hospitalsdashboard.views.assets.RESTORE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun PatientCard(item:Patient,navHostController: NavHostController){
    val nationality = item.nationality
    val admissions = item.admissions
    val gender=item.gender
    val hasActiveAdmission = admissions.any {
        it.admissionTime != null && it.exitTime == null
    }
    val hasAdmissions = admissions.isNotEmpty()
    ColumnContainer {
        Row(verticalAlignment = Alignment.CenterVertically){
            Column(modifier= Modifier.fillMaxWidth().weight(1f).padding(5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    HorizontalSpacer()
                    PatientFullName(patient = item)
                }
                Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Label(item.nationalId?:"")
                        nationality?.let{
                            HorizontalSpacer()
                            Span(it.name, backgroundColor = BLUE, color = WHITE)
                            HorizontalSpacer()
                            if(gender==1) Icon(R.drawable.ic_male) else if(gender==2) Icon(R.drawable.ic_female)

                        }
                    }
                }
            }
            if(hasAdmissions){
                Span(text = if(hasActiveAdmission) PATIENT_IS_PRESENT_LABEL else PATIENT_HAS_QUIT_LABEL, backgroundColor = if(hasActiveAdmission) GREEN else BLUE, color = WHITE,
                    startPadding = 5, endPadding = 5)
            }
            Row(modifier=Modifier.padding(horizontal = 5.dp)){
                IconButton(R.drawable.ic_eye_blue) {
                    Preferences.Patients().set(item)
                    navHostController.navigate(PatientViewRoute.route)
                }
            }
        }

    }



}
package com.kwdevs.hospitalsdashboard.views.pages.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.routes.BabyBirthIndexRoute
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ADMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CANCER_CURE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.DELETE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_PATIENT_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.LOG_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MOBILE_NUMBER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONALITY_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.NATIONAL_ID_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.RESTORE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.patientFullName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientViewPage(navHostController: NavHostController){
    val patient = Preferences.Patients().get()
    val showSheet = remember { mutableStateOf(false) }
    val nationality = patient?.nationality
    val admissions = patient?.admissions
    val gender=patient?.gender
    val hasActiveAdmission = admissions?.any {
        it.admissionTime != null && it.exitTime == null
    }
    val hasAdmissions = admissions?.isNotEmpty()?:false
    if(patient!=null){
        Container(
            title = PATIENT_DATA_LABEL,
            showSheet = showSheet,
            headerShowBackButton = true,
            headerIconButtonBackground = BLUE,
            headerOnClick = {
                Preferences.Patients().delete()
                navHostController.navigate(PatientsIndexRoute.route)
            }
        ) {
            Row (modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically){
                Label(label = PATIENT_LABEL,text= patientFullName(patient))
                if(gender==1) Icon(R.drawable.ic_male) else if(gender==2) Icon(R.drawable.ic_female)


            }
            Row (modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically){
                Label(label = MOBILE_NUMBER_LABEL, text = patient.mobileNumber?:"")

            }
            Row (modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                Label(label = NATIONAL_ID_LABEL, text = patient.nationalId?:"")
            }
            Row (modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)){
                Label(label = NATIONALITY_LABEL, text = patient.nationality?.name?:"")
            }
            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween){
                CustomButtonWithImage(icon = R.drawable.ic_patient_transfer,
                    label = ADMISSIONS_LABEL,
                    iconSize = 26,
                    minWidth = 72,
                    maxWidth = 72) {
                    navHostController.navigate(
                        if(hasAdmissions) PatientWardAdmissionsIndexRoute.route
                        else PatientWardAdmissionsCreateRoute.route)
                }

                CustomButtonWithImage(icon = R.drawable.ic_operation_room_grayscale,
                    label = OPERATIONS_LABEL,
                    iconSize = 26) {
                    //Preferences.Patients().set(item)
                    Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                    navHostController.navigate(OperationsIndexRoute.route)
                }
                CustomButtonWithImage(icon = R.drawable.ic_cancer_grayscale,
                    label = CANCER_CURE_LABEL,
                    iconSize = 26) {
                    //Preferences.Patients().set(item)
                    Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                    navHostController.navigate(CancerCureIndexRoute.route)
                }
                if(gender==2){
                    CustomButtonWithImage(icon = R.drawable.ic_baby,
                        label = BIRTHS_LABEL,
                        iconSize = 26) {
                        //Preferences.Patients().set(item)
                        Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                        navHostController.navigate(BabyBirthIndexRoute.route)
                    }

                }
            }


            Row(modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween){
                CustomButtonWithImage(icon = R.drawable.ic_edit_blue,
                    label = EDIT_PATIENT_DATA_LABEL,
                    iconSize = 26,
                    maxWidth = 72) {
                    //Preferences.Patients().set(item)
                    Preferences.CrudTypes().set(CrudType.UPDATE)
                    navHostController.navigate(PatientCreateRoute.route)
                }
                if(patient.deletedAt==null){
                    CustomButtonWithImage(icon = R.drawable.ic_delete_red,
                        label = DELETE_LABEL,
                        iconSize = 26) {
                        //Preferences.Patients().set(item)
                        Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                        navHostController.navigate(CancerCureIndexRoute.route)
                    }
                }
                else{
                    CustomButtonWithImage(icon = R.drawable.ic_restore_green,
                        label = RESTORE_LABEL,
                        iconSize = 26) {
                        //Preferences.Patients().set(item)
                        Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                        navHostController.navigate(CancerCureIndexRoute.route)
                    }
                }

                CustomButtonWithImage(icon = R.drawable.ic_log,
                    label = LOG_LABEL,
                    iconSize = 26) {
                    //Preferences.Patients().set(item)
                    Preferences.ViewTypes().set(ViewType.BY_PATIENT)
                    navHostController.navigate(CancerCureIndexRoute.route)
                }
            }
        }
    }else{
        LaunchedEffect(Unit) {
            Preferences.ViewTypes().set(ViewType.BY_HOSPITAL)
            navHostController.navigate(PatientsIndexRoute.route)
        }
    }
}

package com.kwdevs.hospitalsdashboard.views.cards.hospitals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.PatientViewType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.routes.OperationsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomButtonWithImage
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.IN_ACTIVE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MAJOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MINOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.PATIENTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.THIS_MONTH_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TOTAL_OPERATIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun OperationRoomCard(item:OperationRoom,navHostController: NavHostController){
    val name=item.name
    val totalOperations=item.totalOperations
    val totalOperationsThisMonth=item.totalOperationsThisMonth
    val isMajor=item.major
    val majorLabel=if(isMajor==true) MAJOR_LABEL else MINOR_LABEL
    val isActive=item.active
    val activeColor=if(isActive==true) GREEN else Color.Red
    val activeLabel=if(isActive==true) ACTIVE_LABEL else IN_ACTIVE_LABEL

    ColumnContainer {
        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Label(name?:"")
            HorizontalSpacer()
            Span(majorLabel, backgroundColor = BLUE, color = WHITE)
            HorizontalSpacer()
            Span(activeLabel, backgroundColor = activeColor, color = WHITE)
        }
        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text="${totalOperations ?: 0}", label = TOTAL_OPERATIONS_LABEL)
            Label(text="${totalOperationsThisMonth ?: 0}", label = THIS_MONTH_LABEL)
        }
        VerticalSpacer()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)){
            CustomButtonWithImage(R.drawable.ic_edit_blue, maxWidth = 48, iconSize = 26, label = EDIT_LABEL) {

            }
            CustomButtonWithImage(R.drawable.ic_frequency, maxWidth = 48, iconSize = 26, label = OPERATIONS_LABEL) {
                Preferences.OperationRooms().set(item)
                Preferences.ViewTypes().set(ViewType.BY_OPERATION_ROOM)
                navHostController.navigate(OperationsIndexRoute.route)
            }
            CustomButtonWithImage(R.drawable.ic_patient, maxWidth = 48, iconSize = 26, label = PATIENTS_LABEL) {
                Preferences.OperationRooms().set(item)
                Preferences.ViewTypes().setPatientViewType(PatientViewType.BY_OPERATION_ROOM)
                navHostController.navigate(PatientsIndexRoute.route)
            }
        }
        VerticalSpacer()


    }
}


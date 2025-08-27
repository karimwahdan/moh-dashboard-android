package com.kwdevs.hospitalsdashboard.views.pages.home.sections

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.routes.HospitalsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BY_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BY_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.type.TypeWithCountCard
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun HomeHospitalTypesSection(hasPermission:Boolean,types: List<HospitalType>, navHostController: NavHostController) {
    if(hasPermission){
        if(types.isNotEmpty()){
            VerticalSpacer(20)
            Row(modifier=Modifier.fillMaxWidth()){
                Span(BY_TYPE_LABEL, color = Color.White, backgroundColor = colorResource(R.color.teal_700))
            }
            VerticalSpacer()
            ColumnContainer(background = colorResource(R.color.pale_orange),
                borderColor = colorResource(R.color.orange3)) {
                LazyRow(modifier=Modifier.fillMaxWidth()) {
                    items(types){
                        TypeWithCountCard(it,modifier= Modifier
                            .width(120.dp).padding(horizontal = 3.dp, vertical = 10.dp)) {
                            Preferences.Hospitals().setTypeOption(true)
                            Preferences.Hospitals().setSectorOption(false)
                            Preferences.HospitalTypes().setItem(it)
                            navHostController.navigate(HospitalsIndexRoute.route)
                        }
                    }
                }
            }
        }
        else{
            Label("No HospitalTypes to display")
            HorizontalDivider()
        }

    }
}

package com.kwdevs.hospitalsdashboard.views.pages.home.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.routes.HospitalsIndexRoute
import com.kwdevs.hospitalsdashboard.views.assets.BY_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.sector.SectorWithCountCard

@Composable
fun HomeSectorsSection(hasPermission:Boolean,sectors: List<Sector>, navHostController: NavHostController) {
    if(hasPermission){
        if(sectors.isNotEmpty()) {
            Row(modifier=Modifier.fillMaxWidth()){
                Span(BY_SECTOR_LABEL, color = Color.White, backgroundColor = colorResource(R.color.teal_700))
            }
            VerticalSpacer()

            ColumnContainer(background = colorResource(R.color.pale_orange),
                borderColor = colorResource(R.color.orange3)) {
                LazyRow(){
                    items(sectors){
                        SectorWithCountCard(
                            it, modifier= Modifier
                                .width(120.dp).padding(vertical=10.dp, horizontal = 3.dp)) {
                            Preferences.Hospitals().setSectorOption(true)
                            Preferences.Hospitals().setTypeOption(false)
                            Preferences.Sectors().setItem(it)
                            navHostController.navigate(HospitalsIndexRoute.route)
                        }
                        //HorizontalSpacer()
                    }
                }
            }
        }
    }
}



package com.kwdevs.hospitalsdashboard.views.pages.home.sections

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.kwdevs.hospitalsdashboard.routes.CitiesRoute
import com.kwdevs.hospitalsdashboard.routes.CityViewRoute
import com.kwdevs.hospitalsdashboard.views.assets.BY_DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.CityCard
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun HomeCitiesSection(hasPermission:Boolean,superUser: SuperUser?, items: List<CityWithCount>, navHostController: NavHostController) {
    if(hasPermission){
        Log.e("HCS","$hasPermission")
        if(items.isNotEmpty()){
            VerticalSpacer(20)
            Row(modifier= Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                Span(BY_DIRECTORATE_LABEL, color = Color.White, backgroundColor = colorResource(R.color.teal_700))
                Box(modifier= Modifier.clickable { navHostController.navigate(CitiesRoute.route) }){
                    Label(text="مشاهدة الكل..", color = colorResource(R.color.teal_700)) }
            }
            VerticalSpacer()
            ColumnContainer(background = colorResource(R.color.pale_orange),
                borderColor = colorResource(R.color.orange3)){
                LazyRow {
                    items(items.subList(0,6)){
                        CityCard(it, modifier = Modifier.width(120.dp).padding(horizontal = 3.dp, vertical = 10.dp)){
                            Preferences.Cities().set(City(id=it.id,name=it.name))
                            navHostController.navigate(CityViewRoute.route)
                        }
                    }
                }
            }
            VerticalSpacer()
        }
        else{ Label("No Cities to Display")}
    } else{
        val cityHeads=items.filter { (it.headId?:0)==(superUser?.id?:1) }
        if(cityHeads.isNotEmpty()){
            Column(modifier= Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = rcs(10)),)
            {
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically){
                    HorizontalSpacer()
                    items.forEach {
                        if(it.headId==(superUser?.id?:0)){
                            CityCard(it, modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)){
                                Preferences.Cities().set(City(id=it.id,name=it.name))
                                navHostController.navigate(CityViewRoute.route)
                            }
                            HorizontalSpacer()
                        }
                    }
                }
                VerticalSpacer()
            }
        }
    }
}

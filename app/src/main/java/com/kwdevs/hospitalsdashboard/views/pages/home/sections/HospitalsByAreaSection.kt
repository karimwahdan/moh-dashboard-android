package com.kwdevs.hospitalsdashboard.views.pages.home.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.area.AreaWithCount
import com.kwdevs.hospitalsdashboard.routes.AreaViewRoute
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.cards.hospitals.HospitalCardNameOnly


@Composable
fun HospitalsByAreaSection(hospitals: MutableState<List<Hospital>>,
                           areas:List<AreaWithCount> = emptyList(),
                           canBrowseHospitals:Boolean,
                           navHostController: NavHostController,
){
    if (hospitals.value.isNotEmpty() && canBrowseHospitals){
        areas.forEach{area->
            Row(modifier= Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                LabelSpan(label = "حسب الحى", value = area.name?: EMPTY_STRING)
                Box(modifier= Modifier.clickable {
                    Preferences.Areas().set(Area(id=area.id,name=area.name, cityId = area.cityId, headId = area.headId))
                    navHostController.navigate(AreaViewRoute.route)
                }){
                    Label(text="مشاهدة الكل..", color = colorResource(R.color.teal_700))
                }
            }
            Row(modifier= Modifier.fillMaxWidth()){
                hospitals.value.forEachIndexed {index, hospital->
                    if(index<4 && hospital.areaId==area.id){
                        Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                            HospitalCardNameOnly (item=hospital,modifier= Modifier.fillMaxWidth()){}
                        }
                    }
                }
                VerticalSpacer()
            }

        }
    }
}

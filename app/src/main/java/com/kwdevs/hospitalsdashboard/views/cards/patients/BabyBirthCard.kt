package com.kwdevs.hospitalsdashboard.views.cards.patients

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.models.patients.babyBirth.BabyBirth
import com.kwdevs.hospitalsdashboard.views.assets.BABY_DIED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BIRTHDATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.MOTHER_DIED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.MOTHER_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE


@Composable
fun BabyBirthCard(item: BabyBirth, navHostController: NavHostController){
    val mother=item.mother
    val birthdate=item.birthdate
    val babyDied=item.babyDied

    val motherDied=item.motherDied
    val birthType=item.birthType
    ColumnContainer{
        VerticalSpacer()
        Label(label = MOTHER_NAME_LABEL,text="${mother?.firstName?:""} ${mother?.secondName?:""} ${mother?.thirdName?:""} ${mother?.fourthName?:""}", fontSize = 18)
        VerticalSpacer()
        LazyRow(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            item {
                Label(label = BIRTHDATE_LABEL,birthdate?:"")
                HorizontalSpacer()
                birthType?.let{Span(it.name, backgroundColor = BLUE, color = WHITE)}
                HorizontalSpacer()
                if(babyDied==true || motherDied==true){
                    Row(modifier=Modifier.padding(5.dp)){
                        babyDied?.let{
                            if (babyDied){
                                Span(BABY_DIED_LABEL, backgroundColor = Color.Red, color = WHITE)}
                        }
                        HorizontalSpacer()
                        motherDied?.let{
                            if (motherDied){
                                Span(MOTHER_DIED_LABEL, backgroundColor = Color.Red, color = WHITE)}
                        }
                    }

                }
            }

        }
    }
}
package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.issuingDepartment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes.NearExpiredCreateRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CODE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.Span

@Composable
fun MyBloodNearExpiryCard(item:BloodNearExpiredItem,navHostController: NavHostController){
    val bgId=item.bloodGroupId
    val unitTypeId=item.unitTypeId
    val bloodGroup=item.bloodGroup
    val status=item.status
    val quantity=item.quantity
    val code=item.code
    val bloodGroupLabel=when(bgId){
        in listOf(1,2)->if(unitTypeId in listOf(3,4,5,6)) "A" else bloodGroup?.name?: EMPTY_STRING
        in listOf(3,4)->if(unitTypeId in listOf(3,4,5,6)) "B" else bloodGroup?.name?: EMPTY_STRING
        in listOf(5,6)->if(unitTypeId in listOf(3,4,5,6)) "O" else bloodGroup?.name?: EMPTY_STRING
        in listOf(7,8)->if(unitTypeId in listOf(3,4,5,6)) "AB" else bloodGroup?.name?: EMPTY_STRING
        else-> EMPTY_STRING
    }
    
    ColumnContainer {
        Row(modifier=Modifier.fillMaxWidth().padding(5.dp)){
            status?.let {Span(text=it.name?: EMPTY_STRING)}

            IconButton(R.drawable.ic_edit_blue) {
                Preferences.BloodBanks.NearExpiredBloodUnits().set(item)
                navHostController.navigate(NearExpiredCreateRoute.route) }
        }
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            LabelSpan(label = bloodGroup?.name?: EMPTY_STRING, value = bloodGroupLabel, spanColor = BLUE)
            quantity?.let { if(it>1) Label(it.toString())}
        }
        code?.let { Label(CODE_LABEL,it) }
    }
}


@Composable
fun OtherBloodNearExpiryCard(item:BloodNearExpiredItem){
    val hospital=item.hospital
    val bgId=item.bloodGroupId
    val unitTypeId=item.unitTypeId
    val bloodGroup=item.bloodGroup
    val status=item.status
    val quantity=item.quantity
    val bloodGroupLabel=when(bgId){
        in listOf(1,2)->if(unitTypeId in listOf(3,4,5,6)) "A" else bloodGroup?.name?: EMPTY_STRING
        in listOf(3,4)->if(unitTypeId in listOf(3,4,5,6)) "B" else bloodGroup?.name?: EMPTY_STRING
        in listOf(5,6)->if(unitTypeId in listOf(3,4,5,6)) "O" else bloodGroup?.name?: EMPTY_STRING
        in listOf(7,8)->if(unitTypeId in listOf(3,4,5,6)) "AB" else bloodGroup?.name?: EMPTY_STRING
        else-> EMPTY_STRING
    }

    ColumnContainer {
        status?.let {Span(text=it.name?: EMPTY_STRING)}
        Label(hospital?.name?: EMPTY_STRING)
        Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            LabelSpan(label = bloodGroup?.name?: EMPTY_STRING, value = bloodGroupLabel, spanColor = BLUE)
            quantity?.let { if(it>1) Label(it.toString())}
        }
    }
}
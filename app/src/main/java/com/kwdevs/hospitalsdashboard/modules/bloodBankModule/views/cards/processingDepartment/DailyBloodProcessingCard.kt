package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.views.cards.processingDepartment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun DailyBloodProcessingCard(item:DailyBloodProcessing){
    val code=item.campaign?.code?:""
    val totalCollected=item.total?:0
    val campaignType = item.campaign?.campaignType?.name
    val collectionDate= (item.campaign?.collectionDate?:"")
    val dateOnly=collectionDate.replaceAfterLast(" ","")
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION){
        ColumnContainer {
            Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Label(label = "Processing Date:",text=item.processingDate?:"N/A")
                    HorizontalSpacer()
                    Label("${item.bloodType?.name}")
                    HorizontalSpacer()
                    Span(text="${item.total}", backgroundColor = if((item.total?:0)<(item.campaign?.total?:0)) Color.Red else GREEN, color = WHITE)

                }
                Label(label="Campaign:",text="$code - $dateOnly")

            }
        }

    }
}
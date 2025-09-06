package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.subModules.component.views.cards

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
import com.kwdevs.hospitalsdashboard.views.assets.CAMPAIGN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PROCESSING_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.WHITE

@Composable
fun DailyBloodProcessingCard(item:DailyBloodProcessing){
    val code=item.campaign?.code?: EMPTY_STRING
    val processingDate=item.processingDate
    val unitType=item.unitType
    val total=item.total?:0
    val campaignTotal=item.campaign?.total?:0
    val notAllProcessed=total<campaignTotal
    val totalSpanColor=if(notAllProcessed) Color.Red else GREEN
    val collectionDate= (item.campaign?.collectionDate?:EMPTY_STRING)
    val dateOnly=collectionDate.replaceAfterLast(" ",EMPTY_STRING)
    CompositionLocalProvider(LEFT_LAYOUT_DIRECTION){
        ColumnContainer {
            Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    processingDate?.let{Label(label = "$PROCESSING_DATE_LABEL:",text=it)}
                    HorizontalSpacer()
                    unitType?.let{Label("${it.name}")}
                    HorizontalSpacer()
                    Span(text="$total", backgroundColor = totalSpanColor,color = WHITE)

                }
                Label(label="$CAMPAIGN_LABEL:",text="$code - $dateOnly")

            }
        }

    }
}
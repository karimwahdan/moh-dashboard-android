package com.kwdevs.hospitalsdashboard.views.pages.home

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.BloodFilterBy
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.ModelConverter
import com.kwdevs.hospitalsdashboard.controller.SettingsController
import com.kwdevs.hospitalsdashboard.controller.hospital.HospitalController
import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.DailyBloodStockFilterBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.controller.BloodBankIssuingDepartmentController
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.DailyBloodStock
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.issuingDepartment.DailyBloodStocksResponse
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ALEXANDRIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASUIT
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ASWAN
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BANI_SUIF
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.BUHIRA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.CAIRO
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAKAHLIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.DAMIATTA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.FAYUM
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GHARBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.GIZA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.ISMAILIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.KAFR_EL_SHEIKH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.LUXOR
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MATROUH
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MENUFIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.MINIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NEW_VALLEY
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.NORTH_SINAI
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.PORT_SAID
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QALUBIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.QENA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.RED_SEA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SHARQIA
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SOHAG
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.SUEZ
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_ALL_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_CERTAIN_DIRECTORATE_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_CURATIVE_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_DIRECTORATE_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_EDUCATIONAL_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_HOSPITAL_TYPE_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_INSURANCE_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_NBTS_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_SECTOR_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles.VIEW_SPECIALIZED_BLOOD_STOCKS
import com.kwdevs.hospitalsdashboard.responses.HospitalsResponse
import com.kwdevs.hospitalsdashboard.responses.options.BloodOptionsData
import com.kwdevs.hospitalsdashboard.views.assets.AB_NEG
import com.kwdevs.hospitalsdashboard.views.assets.AB_POS
import com.kwdevs.hospitalsdashboard.views.assets.A_NEG
import com.kwdevs.hospitalsdashboard.views.assets.A_POS
import com.kwdevs.hospitalsdashboard.views.assets.BLACK
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLOOD_STOCK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.B_NEG
import com.kwdevs.hospitalsdashboard.views.assets.B_POS
import com.kwdevs.hospitalsdashboard.views.assets.CANCEL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.CURATIVE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.DATA_LOADED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DATE_TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DatePickerWidget
import com.kwdevs.hospitalsdashboard.views.assets.EDUCATIONAL_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_LOADING_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ERROR_SAVE_EXCEL_FILE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILE_SAVED_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FILTER_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.FROM_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITALS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.INSURANCE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.LabelSpan
import com.kwdevs.hospitalsdashboard.views.assets.NEW_LINE
import com.kwdevs.hospitalsdashboard.views.assets.NO_DATA_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.OK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ORANGE
import com.kwdevs.hospitalsdashboard.views.assets.O_NEG
import com.kwdevs.hospitalsdashboard.views.assets.O_POS
import com.kwdevs.hospitalsdashboard.views.assets.SECTORS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_BANK_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_BLOOD_GROUP_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_DATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_HOSPITAL_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SELECT_UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPECIALIZED_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.THE_HOSPITAL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THIS_DIRECTORATE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TIME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.TO_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNDER_INSPECTION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.UNIT_TYPE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.FailScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.SuccessScreen
import com.kwdevs.hospitalsdashboard.views.assets.montaserFont
import com.kwdevs.hospitalsdashboard.views.assets.timeBlocks
import com.kwdevs.hospitalsdashboard.views.rcs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

data class HospitalBloodSummary(
    val hospital: SimpleHospital?,
    val bloodType: BasicModel?, // from blood_unit_type
    val date: String,
    val timeBlock: String,
    val aPos: Int = 0,
    val aPosStrategic: Int = 0,
    val aNeg: Int = 0,
    val aNegStrategic: Int = 0,
    val bPos: Int = 0,
    val bPosStrategic: Int = 0,
    val bNeg: Int = 0,
    val bNegStrategic: Int = 0,
    val abPos: Int = 0,
    val abPosStrategic: Int = 0,
    val abNeg: Int = 0,
    val abNegStrategic: Int = 0,
    val oPos: Int = 0,
    val oPosStrategic: Int = 0,
    val oNeg: Int = 0,
    val oNegStrategic: Int = 0,
    val underInspection: Int = 0
)
fun List<Hospital>.toBloodStockSummaries():List<HospitalBloodSummary>{
    val summaries= mutableListOf<HospitalBloodSummary>()
    this.forEach {hospital->
        val stocks=hospital.stocks
            .sortedWith(compareByDescending<DailyBloodStock> { it.entryDate }
            .thenByDescending { it.hospital?.cityId ?: Int.MIN_VALUE}
            .thenByDescending { it.hospital?.areaId ?: Int.MIN_VALUE }
            .thenByDescending { it.hospitalId ?: Int.MIN_VALUE }
            .thenBy { it.timeBlock})
        if (stocks.isEmpty()) {
            summaries.add(
                HospitalBloodSummary(
                    hospital = ModelConverter().convertHospitalToSimple(hospital),
                    bloodType = null,
                    date = NO_DATA_LABEL,
                    timeBlock = NO_DATA_LABEL,
                )
            )
            return@forEach
        }
        else{
            val grouped=stocks.groupBy { Triple(it.hospitalId, it.entryDate, it.timeBlock) }
            grouped.map { (_, entries) ->

                val bloodType = entries.firstOrNull()?.bloodUnitType
                val date = entries.firstOrNull()?.entryDate.orEmpty()
                val timeBlock = entries.firstOrNull()?.timeBlock.orEmpty()

                // Create mutable holders
                var aPos = 0; var aPosStrategic = 0
                var aNeg = 0; var aNegStrategic = 0
                var bPos = 0; var bPosStrategic = 0
                var bNeg = 0; var bNegStrategic = 0
                var abPos = 0; var abPosStrategic = 0
                var abNeg = 0; var abNegStrategic = 0
                var oPos = 0; var oPosStrategic = 0
                var oNeg = 0; var oNegStrategic = 0
                var underInspectionAmount = 0

                for (e in entries) {
                    when (e.bloodGroupId) {
                        1 -> { aPos = e.amount ?: 0; aPosStrategic = e.emergency ?: 0 }
                        2 -> { aNeg = e.amount ?: 0; aNegStrategic = e.emergency ?: 0 }
                        3 -> { bPos = e.amount ?: 0; bPosStrategic = e.emergency ?: 0 }
                        4 -> { bNeg = e.amount ?: 0; bNegStrategic = e.emergency ?: 0 }
                        5 -> { oPos = e.amount ?: 0; oPosStrategic = e.emergency ?: 0 }
                        6 -> { oNeg = e.amount ?: 0; oNegStrategic = e.emergency ?: 0 }
                        7 -> { abPos = e.amount ?: 0; abPosStrategic = e.emergency ?: 0 }
                        8 -> { abNeg = e.amount ?: 0; abNegStrategic = e.emergency ?: 0 }
                        else -> {/* if (e.underInspection == true) underInspectionAmount = e.amount ?:0 */}
                    }
                    when(e.underInspection?:false){
                        true->{underInspectionAmount = e.amount ?: 0}
                        false->{}
                    }
                }
                val s=HospitalBloodSummary(
                    hospital = ModelConverter().convertHospitalToSimple(hospital),
                    bloodType = bloodType,
                    date = date.replaceRange(5,date.length,EMPTY_STRING),
                    timeBlock = timeBlock,
                    aPos = aPos,
                    aPosStrategic = aPosStrategic,
                    aNeg = aNeg,
                    aNegStrategic = aNegStrategic,
                    bPos = bPos,
                    bPosStrategic = bPosStrategic,
                    bNeg = bNeg,
                    bNegStrategic = bNegStrategic,
                    abPos = abPos,
                    abPosStrategic = abPosStrategic,
                    abNeg = abNeg,
                    abNegStrategic = abNegStrategic,
                    oPos = oPos,
                    oPosStrategic = oPosStrategic,
                    oNeg = oNeg,
                    oNegStrategic = oNegStrategic,
                    underInspection = underInspectionAmount
                )
                summaries.add(s)
            }
        }
    }
    return summaries
}


fun List<DailyBloodStock>.toHospitalSummaries(): List<HospitalBloodSummary> {
    return this
        .groupBy { Triple(it.hospitalId, it.entryDate, it.timeBlock) }
        .map { (_, entries) ->

            val hospital = entries.firstOrNull()?.hospital
            val bloodType = entries.firstOrNull()?.bloodUnitType
            val date = entries.firstOrNull()?.entryDate.orEmpty()
            val timeBlock = entries.firstOrNull()?.timeBlock.orEmpty()

            // Create mutable holders
            var aPos = 0; var aPosStrategic = 0
            var aNeg = 0; var aNegStrategic = 0
            var bPos = 0; var bPosStrategic = 0
            var bNeg = 0; var bNegStrategic = 0
            var abPos = 0; var abPosStrategic = 0
            var abNeg = 0; var abNegStrategic = 0
            var oPos = 0; var oPosStrategic = 0
            var oNeg = 0; var oNegStrategic = 0
            var underInspectionAmount = 0

            for (e in entries) {
                when (e.bloodGroupId) {
                    1 -> { aPos = e.amount ?: 0; aPosStrategic = e.emergency ?: 0 }
                    2 -> { aNeg = e.amount ?: 0; aNegStrategic = e.emergency ?: 0 }
                    3 -> { bPos = e.amount ?: 0; bPosStrategic = e.emergency ?: 0 }
                    4 -> { bNeg = e.amount ?: 0; bNegStrategic = e.emergency ?: 0 }
                    5 -> { oPos = e.amount ?: 0; oPosStrategic = e.emergency ?: 0 }
                    6 -> { oNeg = e.amount ?: 0; oNegStrategic = e.emergency ?: 0 }
                    7 -> { abPos = e.amount ?: 0; abPosStrategic = e.emergency ?: 0 }
                    8 -> { abNeg = e.amount ?: 0; abNegStrategic = e.emergency ?: 0 }
                    else -> {/* if (e.underInspection == true) underInspectionAmount = e.amount ?:0 */}
                }
                when(e.underInspection?:false){
                    true->{underInspectionAmount = e.amount ?: 0}
                    false->{}
                }
            }

            HospitalBloodSummary(
                hospital = hospital,
                bloodType = bloodType,
                date = date,
                timeBlock = timeBlock,
                aPos = aPos,
                aPosStrategic = aPosStrategic,
                aNeg = aNeg,
                aNegStrategic = aNegStrategic,
                bPos = bPos,
                bPosStrategic = bPosStrategic,
                bNeg = bNeg,
                bNegStrategic = bNegStrategic,
                abPos = abPos,
                abPosStrategic = abPosStrategic,
                abNeg = abNeg,
                abNegStrategic = abNegStrategic,
                oPos = oPos,
                oPosStrategic = oPosStrategic,
                oNeg = oNeg,
                oNegStrategic = oNegStrategic,
                underInspection = underInspectionAmount
            )
        }
}

@Composable
fun BloodStockTable(data: List<HospitalBloodSummary>) {
    if(data.isNotEmpty()){
        Row(modifier = Modifier.fillMaxWidth()) {

            // ======= Constant Part =======
            Column(modifier = Modifier.width(150.dp).background(Color.LightGray)) {
                // Header
                Row(modifier = Modifier.background(BLUE)) {
                    Text(DIRECTORATE_LABEL, fontSize = 12.sp,
                        fontFamily = montaserFont,
                        color = WHITE,
                        modifier = Modifier.weight(1f).padding(8.dp))
                    Text(THE_HOSPITAL_LABEL,color = WHITE,fontFamily = montaserFont,fontSize = 12.sp,modifier = Modifier.weight(1.2f).padding(8.dp))
                }

                // Data
                data.forEach { summary ->
                    Row(modifier=Modifier.height(70.dp),verticalAlignment = Alignment.CenterVertically) {
                        Text(summary.hospital?.city?.name ?: EMPTY_STRING,
                            fontFamily = montaserFont,
                            fontSize = 10.sp,
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(0.9f).padding(8.dp), textAlign = TextAlign.Center)
                        Text((summary.hospital?.name ?: EMPTY_STRING)+NEW_LINE,
                            fontFamily = montaserFont,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(1.2f).padding(8.dp),
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center)

                    }
                }
            }

            // ======= (Scrollable) =======
            LazyRow(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BloodStockSummaryHeader()
                        BloodStockSummaryBody(data)

                    }
                }
            }
        }

    }
}

@Composable
fun BloodStockSummaryHeader(){
    Row(verticalAlignment = Alignment.CenterVertically) {
        BloodGroupHeader(DATE_LABEL,width=55, fontSize = 12)
        BloodGroupHeader(TIME_LABEL,width=45, fontSize = 12)
        BloodGroupHeader(UNDER_INSPECTION_LABEL,width=65, fontSize = 10)

        BloodGroupHeader(A_POS );BloodGroupHeader(A_NEG )
        BloodGroupHeader(B_POS );BloodGroupHeader(B_NEG )
        BloodGroupHeader(O_POS );BloodGroupHeader(O_NEG )
        BloodGroupHeader(AB_POS);BloodGroupHeader(AB_NEG)
    }
}
@Composable
fun BloodStockSummaryRow(summary:HospitalBloodSummary){
    val noDateData=summary.date== NO_DATA_LABEL
    val noTimeData=summary.timeBlock== NO_DATA_LABEL
    val date=summary.date
    val timeBlock=summary.timeBlock
    val underInspection=summary.underInspection.toString()
    if(!noDateData){
        Row(modifier=Modifier.height(70.dp),verticalAlignment = Alignment.CenterVertically) {
            BloodGroupCell(amount=date, null, width = 55, amountColor = BLACK)
            BloodGroupCell(amount=timeBlock, null, width = 55, amountColor = if(noTimeData) Color.Red else BLACK)
            BloodGroupCell(underInspection,null, width = 60)

            BloodGroupCell(summary.aPos.toString(), summary.aPosStrategic.toString())
            BloodGroupCell(summary.aNeg.toString(), summary.aNegStrategic.toString())
            BloodGroupCell(summary.bPos.toString(), summary.bPosStrategic.toString())
            BloodGroupCell(summary.bNeg.toString(), summary.bNegStrategic.toString())
            BloodGroupCell(summary.oPos.toString(), summary.oPosStrategic.toString())
            BloodGroupCell(summary.oNeg.toString(), summary.oNegStrategic.toString())
            BloodGroupCell(summary.abPos.toString(), summary.abPosStrategic.toString())
            BloodGroupCell(summary.abNeg.toString(), summary.abNegStrategic.toString())
        }
    }
    else{
        Row(modifier=Modifier.height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) { Label(NO_DATA_LABEL, color = Color.Red, fontWeight = FontWeight.Bold) }
    }

}
@Composable
fun BloodStockSummaryBody(data:List<HospitalBloodSummary>){
    data.forEach { summary -> BloodStockSummaryRow(summary) }
}
@Composable
fun HospitalBloodStockTable(data: List<HospitalBloodSummary>) {
    if(data.isNotEmpty()){
        Row(modifier = Modifier.fillMaxWidth()) {

            // ======= Constant Part =======
            Column(modifier = Modifier.width(150.dp).background(Color.LightGray)) {
                // Header
                Row(modifier = Modifier.background(BLUE)) {
                    Text(DIRECTORATE_LABEL, fontSize = 12.sp,
                        fontFamily = montaserFont,
                        color = WHITE,
                        modifier = Modifier.weight(1f).padding(8.dp))
                    Text(THE_HOSPITAL_LABEL,color = WHITE,fontFamily = montaserFont,fontSize = 12.sp,modifier = Modifier.weight(1.2f).padding(8.dp))
                }

                // Data
                data.forEach { summary ->
                    Row(modifier=Modifier.height(70.dp),verticalAlignment = Alignment.CenterVertically) {
                        Text(summary.hospital?.city?.name ?: EMPTY_STRING,
                            fontFamily = montaserFont,
                            fontSize = 10.sp,
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(0.9f).padding(8.dp), textAlign = TextAlign.Center)
                        Text((summary.hospital?.name ?: EMPTY_STRING)+NEW_LINE,
                            fontFamily = montaserFont,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(1.2f).padding(8.dp),
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center)

                    }
                }
            }

            // ======= (Scrollable) =======
            LazyRow(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                item {
                    Column {
                        // Header Blood Groups
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BloodGroupHeader(DATE_LABEL,width=55, fontSize = 12)
                            BloodGroupHeader(TIME_LABEL,width=45, fontSize = 12)
                            BloodGroupHeader(UNDER_INSPECTION_LABEL,width=65, fontSize = 10)

                            BloodGroupHeader(A_POS )
                            BloodGroupHeader(A_NEG )
                            BloodGroupHeader(B_POS )
                            BloodGroupHeader(B_NEG )
                            BloodGroupHeader(O_POS )
                            BloodGroupHeader(O_NEG )
                            BloodGroupHeader(AB_POS)
                            BloodGroupHeader(AB_NEG)
                        }
                        HorizontalDivider(color = WHITE)
                        // Data
                        data.forEach { summary ->
                            Row(modifier=Modifier.height(70.dp),verticalAlignment = Alignment.CenterVertically) {
                                BloodGroupCell(summary.date.replaceRange(5,summary.date.length,EMPTY_STRING), null, width = 55)
                                BloodGroupCell(summary.timeBlock, null, width = 45)
                                BloodGroupCell(summary.underInspection.toString(),null, width = 60)

                                BloodGroupCell(summary.aPos.toString(), summary.aPosStrategic.toString())
                                BloodGroupCell(summary.aNeg.toString(), summary.aNegStrategic.toString())
                                BloodGroupCell(summary.bPos.toString(), summary.bPosStrategic.toString())
                                BloodGroupCell(summary.bNeg.toString(), summary.bNegStrategic.toString())
                                BloodGroupCell(summary.oPos.toString(), summary.oPosStrategic.toString())
                                BloodGroupCell(summary.oNeg.toString(), summary.oNegStrategic.toString())
                                BloodGroupCell(summary.abPos.toString(), summary.abPosStrategic.toString())
                                BloodGroupCell(summary.abNeg.toString(), summary.abNegStrategic.toString())
                            }
                        }
                        HorizontalDivider(color = Color.Black)

                    }
                }
            }
        }

    }
}


@Composable
fun BloodGroupHeader(name: String,width:Int=40,fontSize: Int=14) {
    Column(
        modifier = Modifier.width(width.dp).background(Color.Gray).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name,fontSize=fontSize.sp,
            fontWeight = FontWeight.Bold,modifier=Modifier.padding(vertical = 4.dp))
    }
}

// (amount و strategic)
@Composable
fun BloodGroupCell(amount: String,
                   strategic: String?,
                   width:Int=40,amountColor: Color= BLACK,strategicColor: Color= BLACK) {
    Column(
        modifier = Modifier.width(width.dp).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Label(text=amount, fontWeight = FontWeight.Bold, color = amountColor)
        strategic?.let{Label(text=strategic, color = strategicColor)}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodStockSection(records:MutableState<List<DailyBloodStock>>){
    val controller:BloodBankIssuingDepartmentController= viewModel()
    val exportState         by controller.stockExcelState.observeAsState()
    val state               by controller.state.observeAsState()
    val context             =  LocalContext.current
    val showFilterDialog    =  remember { mutableStateOf(false) }
    val body                =  remember { mutableStateOf<DailyBloodStockFilterBody?>(null) }
    var sortedBloodRecords  by remember { mutableStateOf<List<DailyBloodStock>>(emptyList()) }

    var loadingExport       by remember { mutableStateOf(false) }
    var failExport          by remember { mutableStateOf(false) }
    var successExport       by remember { mutableStateOf(false) }
    var loading             by remember { mutableStateOf(false) }
    var fail                by remember { mutableStateOf(false) }
    var success             by remember { mutableStateOf(false) }

    val drawerState         =  rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(state) {
        when(state){
            is UiState.Loading->{loading=true;fail=false;success=false}
            is UiState.Error->{loading=false;fail=true;success=false}
            is UiState.Success->{loading=false;fail=false;success=true
                sortedBloodRecords=records.value.sortedByDescending { it.entryDate }
                .filter { it.bloodUnitTypeId in listOf(null,1,2) }
                .sortedWith(compareByDescending<DailyBloodStock> { it.entryDate }
                    .thenByDescending { it.hospital?.cityId ?: Int.MIN_VALUE}
                    .thenByDescending { it.hospital?.areaId ?: Int.MIN_VALUE }
                    .thenByDescending { it.hospitalId ?: Int.MIN_VALUE }
                    .thenBy { it.timeBlock})
            }
            else->{}
        }

    }
    LaunchedEffect(exportState) {
        when (exportState) {
            is UiState.Loading->{loadingExport=true;failExport=false;successExport=false}
            is UiState.Error -> {
                loadingExport=false;failExport=true;successExport=false
                val message = (exportState as UiState.Error).exception.message ?: ERROR_LOADING_DATA_LABEL
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            is UiState.Success<*> -> {
                loadingExport=false;failExport=false
                val response = (exportState as UiState.Success<Response<ResponseBody>>).data
                if (response.isSuccessful) {
                    successExport=true
                    val saved = saveExcelFile(
                        //context = context,
                        responseBody = response.body()!!,
                        fileName = "daily_blood_stock.xlsx"
                    )
                    Toast.makeText(
                        context,
                        if (saved) FILE_SAVED_LABEL else ERROR_SAVE_EXCEL_FILE_LABEL,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(context, "Download failed: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
            else -> {}
        }
    }

    val startDateState      =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val startDate           =  remember { mutableStateOf(EMPTY_STRING) }
    val showStartDatePicker =  remember { mutableStateOf(false) }

    val endDateState        =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val endDate             =  remember { mutableStateOf(EMPTY_STRING) }
    val showEndDatePicker   =  remember { mutableStateOf(false) }
    val hasDateTo           =  remember { mutableStateOf(false) }

    DatePickerWidget(showEndDatePicker,endDateState,endDate)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier=Modifier.width(250.dp).fillMaxHeight().background(Color.LightGray)){
                    VerticalSpacer(10)
                    CustomCheckbox(label = DATE_TO_LABEL,hasDateTo)
                    if(hasDateTo.value){
                        CustomButton(label = SELECT_DATE_LABEL, enabledBackgroundColor = if(endDate.value!= EMPTY_STRING) ORANGE else GREEN) {
                            showEndDatePicker.value=true
                        }
                        if(endDate.value!= EMPTY_STRING){
                            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Label(label= DATE_TO_LABEL,endDate.value,)
                                IconButton(R.drawable.ic_cancel_red) {endDate.value= EMPTY_STRING }
                            }

                        }
                    }
                }

        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .scrollable(state = rememberScrollState(),
                orientation = Orientation.Horizontal))
        {
            // Header Row
            Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                IconButton(R.drawable.ic_filter_blue) {controller.reload();showFilterDialog.value=true}
                Box(modifier=Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center){
                    Label(text=BLOOD_STOCK_LABEL,fontSize = 12, fontWeight = FontWeight.Bold,color = BLUE)
                }
                body.value?.let { if(sortedBloodRecords.isNotEmpty())IconButton(R.drawable.ic_save_blue) { controller.saveStockExcelFile(it) } }
            }
            if(loading) LoadingScreen()
            else{
                if(success)if(sortedBloodRecords.isNotEmpty()) BloodStockTable(sortedBloodRecords.toHospitalSummaries())
                if(fail) FailScreen()
            }

        }
    }

    FilterDialog(showDialog=showFilterDialog,records=records,controller,body)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDialog(showDialog: MutableState<Boolean>,
                         records: MutableState<List<DailyBloodStock>>,
                         controller:BloodBankIssuingDepartmentController,
                         body: MutableState<DailyBloodStockFilterBody?>) {
    val user=Preferences.User().getSuper()
    val roles=user?.roles?: emptyList()
    val isSuper=user?.isSuper?:false
    val permissions=roles.flatMap { r->r.permissions.map { p->p.slug?: EMPTY_STRING } }
    val state by controller.state.observeAsState()
    val settingsController:SettingsController= viewModel()
    val optionsState by settingsController.bloodOptionsState.observeAsState()
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val viewBySector=if(isSuper || permissions.any{it in listOf(VIEW_ALL_BLOOD_STOCKS,VIEW_SECTOR_BLOOD_STOCKS)}){
        Pair(BloodFilterBy.SECTOR, SECTOR_LABEL)}else null
    val viewByDirectorate=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,VIEW_DIRECTORATE_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.DIRECTORATE_SECTOR, DIRECTORATE_LABEL)
    }else null
    val viewByHospitalType=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,VIEW_HOSPITAL_TYPE_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.HOSPITAL_TYPE, HOSPITAL_TYPE_LABEL)
    }else null
    val viewByHospital=if(isSuper || permissions.any { it ==VIEW_ALL_BLOOD_STOCKS }){
        Pair(BloodFilterBy.HOSPITAL, THE_HOSPITAL_LABEL)
    }else null
    val viewByNbts=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,VIEW_NBTS_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.NBTS_SECTOR, IS_NBTS_LABEL)
    }else null
    val viewBySpecialized=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,
            VIEW_SPECIALIZED_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.NBTS_SECTOR, SPECIALIZED_SECTOR_LABEL)
    }else null
    val viewByInsurance=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,
            VIEW_INSURANCE_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.NBTS_SECTOR, INSURANCE_SECTOR_LABEL)
    }else null
    val viewByCurative=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,
            VIEW_CURATIVE_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.NBTS_SECTOR, CURATIVE_SECTOR_LABEL)
    }else null
    val viewByEducational=if(isSuper || permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,
            VIEW_EDUCATIONAL_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.NBTS_SECTOR, EDUCATIONAL_SECTOR_LABEL)
    }else null
    val viewCertainDirectorate=if(permissions.any { it in listOf(VIEW_ALL_BLOOD_STOCKS,
            VIEW_CERTAIN_DIRECTORATE_BLOOD_STOCKS) }){
        Pair(BloodFilterBy.CERTAIN_DIRECTORATE, THIS_DIRECTORATE_LABEL)
    }else null
    val filterByList = listOf(
        viewByHospital,viewBySector,
        viewByHospitalType,viewByDirectorate,
        viewByNbts,viewBySpecialized,viewByEducational,viewByInsurance,
        viewByCurative,
        viewCertainDirectorate
        ).mapNotNull { it }
    if(showDialog.value){
        val selectByBloodGroups by remember { mutableStateOf(false) }

        val selectedTimeBlock = remember { mutableStateOf<Pair<String,String>?>(null) }

        var hospitals           by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
        var filteredHospitals   by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
        val selectedHospital    = remember { mutableStateOf<SimpleHospital?>(null) }
        val selectedHospitals   = remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }

        var sectors             by remember { mutableStateOf<List<Sector>>(emptyList()) }
        val selectedSector      = remember { mutableStateOf<Sector?>(null) }
        val selectedSectors     = remember { mutableStateOf<List<Sector>>(emptyList()) }

        var hospitalTypes       by remember { mutableStateOf<List<HospitalType>>(emptyList()) }
        val selectedHospitalType  = remember { mutableStateOf<HospitalType?>(null) }
        val selectedHospitalTypes = remember { mutableStateOf<List<HospitalType>>(emptyList()) }

        var bloodBanks          by remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }
        val selectedBloodBank   =  remember { mutableStateOf<SimpleHospital?>(null) }
        val selectedBloodBanks  =  remember { mutableStateOf<List<SimpleHospital>>(emptyList()) }

        var bloodGroups         by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
        val selectedBloodGroup  =  remember { mutableStateOf<BasicModel?>(null) }
        val selectedBloodGroups =  remember { mutableStateOf<List<BasicModel>>(emptyList()) }

        var unitTypes           by remember { mutableStateOf<List<BasicModel>>(emptyList()) }
        val selectedUnitType    =  remember { mutableStateOf<BasicModel?>(null) }
        val selectedUnitTypes   =  remember { mutableStateOf<List<BasicModel>>(emptyList()) }

        val startDateState      =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        val startDate           =  remember { mutableStateOf(EMPTY_STRING) }
        val showStartDatePicker =  remember { mutableStateOf(false) }

        val endDateState        =  rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        val endDate             =  remember { mutableStateOf(EMPTY_STRING) }
        val showEndDatePicker   =  remember { mutableStateOf(false) }

        val selectedFilterBy    =  remember { mutableStateOf<Pair<BloodFilterBy,String>?>(null) }
        when(state){
            is UiState.Loading->{ LaunchedEffect(Unit) {loading=true;success=false;fail=false} }
            is UiState.Error->{LaunchedEffect(Unit) {loading=false;success=false;fail=true} }
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    loading=false;success=true;fail=false
                    try{
                        val s =state as UiState.Success<DailyBloodStocksResponse>
                        val r = s.data
                        val data=r.data
                        records.value=data.sortedWith(
                            compareByDescending<DailyBloodStock> { it.hospital?.cityId ?: Int.MIN_VALUE }
                                .thenByDescending { it.hospital?.areaId ?: Int.MIN_VALUE }
                                .thenByDescending { it.hospitalId ?: Int.MIN_VALUE }
                                .thenByDescending { it.entryDate}
                                .thenBy { it.timeBlock}
                        )
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }


                }
            }
            else->{}
        }
        val hController:HospitalController=viewModel()
        val hospState by hController.state.observeAsState()
        when(hospState){
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    val s =hospState as UiState.Success<HospitalsResponse>
                    val r = s.data
                    val data=r.data
                    hospitals=data.map{ ModelConverter().convertHospitalToSimple(it)}
                }


            }
            else->{}
        }
        when(optionsState){
            is UiState.Loading->{}
            is UiState.Error->{}
            is UiState.Success->{
                LaunchedEffect(Unit) {
                    val s =optionsState as UiState.Success<BloodOptionsData>
                    val r = s.data
                    val data=r.data
                    unitTypes=data.bloodTypes
                    bloodBanks=data.bloodBanks
                    bloodGroups=data.bloodGroups
                    sectors=data.sectors
                    hospitalTypes=data.hospitalTypes
                    hospitals=data.hospitals
                    Log.e("Option Hospitals","${hospitals.size}")
                    //data.hospitals.map { it.city }
                }

            }

            else->{ LaunchedEffect(Unit) { settingsController.bloodOptions() } }
        }
        LaunchedEffect(Unit) {
            loading=false;success=false;fail=false
            controller.reload()
            selectedSectors.value= emptyList()
            selectedHospitals.value= emptyList()
            selectedBloodBanks.value= emptyList()
            selectedUnitTypes.value= emptyList()
            selectedHospitalTypes.value= emptyList()
            selectedBloodGroups.value= emptyList()
            startDate.value=EMPTY_STRING
            endDate.value=EMPTY_STRING
            selectedTimeBlock.value=null
        }
        LaunchedEffect(filterByList) {
            if(filterByList.isNotEmpty()){
                val oneItem=filterByList.size==1
                val onlyCertainDirectorate=filterByList[0].first==BloodFilterBy.CERTAIN_DIRECTORATE
                if(oneItem && onlyCertainDirectorate) selectedFilterBy.value=filterByList[0]
            }
        }

        LaunchedEffect(selectedFilterBy.value) {
            val mainFilter=selectedFilterBy.value
            if(mainFilter!=null && permissions.isNotEmpty()){
                when(mainFilter.first){
                    BloodFilterBy.CURATIVE_SECTOR    ->{filteredHospitals=hospitals.filter { it.isNbts==false && it.sectorId==1};bloodBanks=emptyList()}
                    BloodFilterBy.INSURANCE_SECTOR   ->{filteredHospitals=hospitals.filter { it.isNbts==false && it.sectorId==2};bloodBanks=emptyList()}
                    BloodFilterBy.EDUCATIONAL_SECTOR ->{filteredHospitals=hospitals.filter { it.isNbts==false && it.sectorId==3};bloodBanks=emptyList()}
                    BloodFilterBy.SPECIALIZED_SECTOR ->{filteredHospitals=hospitals.filter { it.isNbts==false && it.sectorId==4 };bloodBanks=emptyList()}
                    BloodFilterBy.DIRECTORATE_SECTOR ->{filteredHospitals=hospitals.filter { it.isNbts==false };bloodBanks=emptyList()}
                    BloodFilterBy.NBTS_SECTOR        ->{bloodBanks=hospitals.filter { it.isNbts==true && it.sectorId==6 };filteredHospitals= emptyList()}
                    BloodFilterBy.HOSPITAL           ->{filteredHospitals=hospitals.filter { it.isNbts==false };bloodBanks=emptyList()}
                    BloodFilterBy.CERTAIN_DIRECTORATE->{
                        if(isSuper || permissions.contains(VIEW_ALL_BLOOD_STOCKS)){filteredHospitals=hospitals}
                        if(isSuper || permissions.contains(VIEW_NBTS_BLOOD_STOCKS)){bloodBanks=hospitals.filter { it.isNbts==true && it.sectorId==6 }}
                        if(permissions.contains(VIEW_CERTAIN_DIRECTORATE_BLOOD_STOCKS)){
                            val directorateHospitals=hospitals.filter{it.sectorId==5}
                            val grouped=hospitals.groupBy { it.sectorId }
                            if(hospitals.isNotEmpty()){
                                grouped.forEach { groupName, list -> Log.e("Grouped","$groupName ${list.size}")}
                            }else{Log.e("Grouped","Hospitals is Empty")}
                            if(permissions.contains(CAIRO)){
                                hController.byCity(CAIRO.replaceBefore(".","").replace(".",""))
                                Log.e("containsCairo", CAIRO)
                                if(directorateHospitals.isNotEmpty()){
                                    directorateHospitals.forEach{
                                        if((it.city?.slug?: EMPTY_STRING)==CAIRO.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING)){
                                            Log.e("Cairo HospitalSlug","Sector Id: ${it.sectorId} ${it.name?:EMPTY_STRING} ${it.city?.slug?: EMPTY_STRING}")
                                        }
                                        else{
                                            Log.e("HospitalSlug","Sector Id: ${it.sectorId} ${it.name?:EMPTY_STRING} ${it.city?.slug?: EMPTY_STRING}")
                                        }
                                    }

                                }
                                filteredHospitals=directorateHospitals.filter {
                                    val citySlug=it.city?.slug?: EMPTY_STRING
                                    val cairoOnly=CAIRO.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING)
                                    citySlug.contains(cairoOnly)}
                                filteredHospitals.forEach { it->
                                    Log.e("SlugOnly", it.city?.slug?:EMPTY_STRING)

                                }
                            }

                            if(permissions.contains(GIZA)){             filteredHospitals=hospitals.filter {
                                (it.city?.slug?: EMPTY_STRING).contains(GIZA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))             }}
                            if(permissions.contains(QALUBIA)){          filteredHospitals=hospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(QALUBIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(ALEXANDRIA)){       filteredHospitals=hospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(ALEXANDRIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))       }}
                            if(permissions.contains(ISMAILIA)){         filteredHospitals=hospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(ISMAILIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))         }}
                            if(permissions.contains(ASWAN)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(ASWAN.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(ASUIT)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(ASUIT.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(LUXOR)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(LUXOR.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(RED_SEA)){          filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(RED_SEA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(BUHIRA)){           filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(BUHIRA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))           }}
                            if(permissions.contains(BANI_SUIF)){        filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(BANI_SUIF.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))        }}
                            if(permissions.contains(PORT_SAID)){        filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(PORT_SAID.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))        }}
                            if(permissions.contains(DAKAHLIA)){         filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(DAKAHLIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))         }}
                            if(permissions.contains(DAMIATTA)){         filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(DAMIATTA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))         }}
                            if(permissions.contains(SOHAG)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(SOHAG.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(SUEZ)){             filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(SUEZ.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))             }}
                            if(permissions.contains(SHARQIA)){          filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(SHARQIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(NORTH_SINAI)){      filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(NORTH_SINAI.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))      }}
                            if(permissions.contains(GHARBIA)){          filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(GHARBIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(FAYUM)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(FAYUM.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(QENA)){             filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(QENA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))             }}
                            if(permissions.contains(KAFR_EL_SHEIKH)){   filteredHospitals=directorateHospitals.filter { (it.city?.slug?: EMPTY_STRING).contains(KAFR_EL_SHEIKH.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))   }}
                            if(permissions.contains(MATROUH)){          filteredHospitals=directorateHospitals.filter { (it.city?.slug?:EMPTY_STRING) .contains(MATROUH.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(MENUFIA)){          filteredHospitals=directorateHospitals.filter { (it.city?.slug?:EMPTY_STRING) .contains(MENUFIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))          }}
                            if(permissions.contains(MINIA)){            filteredHospitals=directorateHospitals.filter { (it.city?.slug?:EMPTY_STRING) .contains(MINIA.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))            }}
                            if(permissions.contains(NEW_VALLEY)){       filteredHospitals=directorateHospitals.filter { (it.city?.slug?:EMPTY_STRING) .contains(NEW_VALLEY.replaceBefore(".",EMPTY_STRING).replace(".",EMPTY_STRING))       }}
                            if(startDate.value!= EMPTY_STRING && selectedTimeBlock.value!=null){selectedHospitals.value=filteredHospitals} else {selectedHospitals.value=emptyList()}

                        }
                        else{
                            Log.e("cannot","cannot browse certain directorate")
                        }
                    }
                    else->{
                        Log.e("BloodFilterBy","No Permissions")
                        filteredHospitals= emptyList();bloodBanks=emptyList()
                    }
                }

            }
        }
        LaunchedEffect(selectedHospital.value) {
            if(selectedSectors.value.isEmpty() && selectedHospitalTypes.value.isEmpty()){
                val new= mutableListOf<SimpleHospital>()
                new.addAll(selectedHospitals.value)
                val hasNone= new.none { it==selectedHospital.value }
                if(hasNone) selectedHospital.value?.let{new.add(it)}
                selectedHospital.value=null
                selectedHospitals.value=new
            }
        }
        LaunchedEffect(selectedBloodBank.value) {
            if(selectedSectors.value.isEmpty() && selectedHospitalTypes.value.isEmpty()){
                val new= mutableListOf<SimpleHospital>()
                new.addAll(selectedBloodBanks.value)
                val hasNone= new.none { it==selectedBloodBank.value }
                if(hasNone) selectedBloodBank.value?.let{new.add(it)}
                selectedBloodBank.value=null
                selectedBloodBanks.value=new
            }
        }
        LaunchedEffect(selectedSector.value) {
            val new= mutableListOf<Sector>()
            new.addAll(selectedSectors.value)
            val hasNone= new.none { it==selectedSector.value }
            if(hasNone) selectedSector.value?.let{new.add(it)}
            selectedSector.value=null
            selectedSectors.value=new
            selectedBloodBanks.value= emptyList()
            selectedBloodBanks.value= emptyList()
            selectedHospitals.value= emptyList()
        }
        LaunchedEffect(selectedHospitalType.value) {
            val new= mutableListOf<HospitalType>()
            new.addAll(selectedHospitalTypes.value)
            val hasNone= new.none { it==selectedHospitalType.value }
            if(hasNone) selectedHospitalType.value?.let{new.add(it)}
            selectedHospitalType.value=null
            selectedHospitalTypes.value=new
            selectedBloodBanks.value= emptyList()
            selectedHospitals.value= emptyList()
        }
        LaunchedEffect(selectedBloodGroup.value) {
            val new= mutableListOf<BasicModel>()
            new.addAll(selectedBloodGroups.value)
            val hasNone= new.none { it==selectedBloodGroup.value }
            if(hasNone) selectedBloodGroup.value?.let{new.add(it)}
            selectedBloodGroup.value=null
            selectedBloodGroups.value=new
        }
        LaunchedEffect(selectedUnitType.value) {
            val new= mutableListOf<BasicModel>()
            new.addAll(selectedUnitTypes.value)
            val hasNone= new.none { it==selectedUnitType.value }
            if(hasNone) selectedUnitType.value?.let{new.add(it)}
            selectedUnitType.value=null
            selectedUnitTypes.value=new

        }
        DatePickerWidget(showStartDatePicker,startDateState,startDate)
        DatePickerWidget(showEndDatePicker,endDateState,endDate)
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier= Modifier.fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 10.dp)){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false }
                }
                if(loading) LoadingScreen(modifier=Modifier.fillMaxWidth())
                else{
                    if(!success && !fail){
                        LazyColumn(modifier=Modifier.fillMaxWidth()) {
                            item{
                                Row(modifier= Modifier.fillMaxWidth()
                                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){
                                    Row(modifier=Modifier.fillMaxWidth().weight(1f),
                                        verticalAlignment = Alignment.CenterVertically){
                                        LabelSpan(value=startDate.value, label = FROM_LABEL)
                                        if(startDate.value.trim()!=EMPTY_STRING){IconButton(R.drawable.ic_cancel_red) { startDate.value=EMPTY_STRING }}
                                    }
                                    Row(modifier=Modifier.fillMaxWidth().weight(1f),
                                        verticalAlignment = Alignment.CenterVertically){
                                        LabelSpan(value=endDate.value, label = TO_LABEL)
                                        if(endDate.value.trim()!=EMPTY_STRING){IconButton(R.drawable.ic_cancel_red) { endDate.value=EMPTY_STRING }}

                                    }

                                }

                                if(selectedSectors.value.isNotEmpty()){
                                    Span(SECTORS_LABEL)
                                    selectedSectors.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedSectors.value=selectedSectors.value.filter { f->f!=it }
                                            }
                                        }

                                    }
                                    VerticalSpacer()
                                }
                                if(selectedHospitalTypes.value.isNotEmpty()){
                                    Span(HOSPITAL_TYPE_LABEL, backgroundColor = BLUE, color = WHITE)
                                    selectedHospitalTypes.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedHospitalTypes.value=selectedHospitalTypes.value.filter { f->f!=it }
                                            }
                                        }

                                    }
                                    VerticalSpacer()
                                }
                                if(selectedBloodBanks.value.isNotEmpty()){
                                    Span(BLOOD_BANK_LABEL)
                                    selectedBloodBanks.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name?:EMPTY_STRING)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedBloodBanks.value=selectedBloodBanks.value.filter { f->f!=it }
                                            }
                                        }

                                    }
                                    VerticalSpacer()
                                }
                                if(selectedHospitals.value.isNotEmpty()){
                                    Span(HOSPITALS_LABEL)
                                    selectedHospitals.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name?:EMPTY_STRING)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedHospitals.value=selectedHospitals.value.filter { f->f!=it }
                                            }
                                        }
                                    }
                                    VerticalSpacer()
                                }
                                if(selectedBloodGroups.value.isNotEmpty()){
                                    Span(BLOOD_GROUP_LABEL)
                                    selectedBloodGroups.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name?:EMPTY_STRING)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedBloodGroups.value=selectedBloodGroups.value.filter { f->f!=it }
                                            }
                                        }
                                    }
                                    VerticalSpacer()
                                }
                                if(selectedUnitTypes.value.isNotEmpty()){
                                    Span(UNIT_TYPE_LABEL)
                                    selectedUnitTypes.value.forEach {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Label(it.name?:EMPTY_STRING)
                                            IconButton(R.drawable.ic_delete_red) {
                                                selectedUnitTypes.value=selectedUnitTypes.value.filter { f->f!=it }
                                            }
                                        }
                                    }
                                    VerticalSpacer()
                                }

                                Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    CustomButton(label = DATE_FROM_LABEL ,
                                        enabledBackgroundColor = ORANGE,
                                        onClick = { showStartDatePicker.value = !showStartDatePicker.value })

                                    CustomButton(label = DATE_TO_LABEL ,
                                        enabledBackgroundColor = ORANGE,
                                        onClick = { showEndDatePicker.value = !showEndDatePicker.value })
                                }
                                if(startDate.value!= EMPTY_STRING){
                                    Row(modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier= Modifier.padding(5.dp).fillMaxWidth()){
                                            ComboBox(
                                                hasTitle = false,
                                                loadedItems = timeBlocks,
                                                selectedItem = selectedTimeBlock,
                                                selectedContent = { CustomInput(selectedTimeBlock.value?.second?: SELECT_TIME_LABEL)},
                                                itemContent = {Label(it?.second?:EMPTY_STRING)}
                                            )
                                        }
                                    }
                                    if(selectedTimeBlock.value!=null){
                                        if(filterByList.isNotEmpty() ){

                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                verticalAlignment = Alignment.CenterVertically){
                                                Box(modifier= Modifier.fillMaxWidth()){
                                                    ComboBox(
                                                        hasTitle = false,
                                                        loadedItems = filterByList,
                                                        selectedItem = selectedFilterBy,
                                                        selectedContent = { CustomInput(selectedFilterBy.value?.second?: FILTER_LABEL)}
                                                    ) {
                                                        Label(it?.second?:EMPTY_STRING)
                                                    }
                                                }
                                            }
                                            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                                Label(selectedFilterBy.value?.second?: EMPTY_STRING)
                                            }

                                            if(selectedFilterBy.value!=null){
                                                selectedFilterBy.value?.let{
                                                    when(it.first){
                                                        in listOf(
                                                            BloodFilterBy.HOSPITAL,
                                                            BloodFilterBy.DIRECTORATE_SECTOR,
                                                            BloodFilterBy.SPECIALIZED_SECTOR,
                                                            BloodFilterBy.EDUCATIONAL_SECTOR,
                                                            BloodFilterBy.INSURANCE_SECTOR,
                                                            BloodFilterBy.CURATIVE_SECTOR
                                                        )->
                                                            {
                                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                                verticalAlignment = Alignment.CenterVertically){
                                                                Box(modifier= Modifier
                                                                    .fillMaxWidth()){
                                                                    ComboBox(
                                                                        hasTitle = false,
                                                                        loadedItems = filteredHospitals,
                                                                        selectedItem = selectedHospital,
                                                                        selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                                                    ) {l->Label(l?.name?:EMPTY_STRING)}
                                                                }

                                                            }
                                                        }
                                                        BloodFilterBy.SECTOR->{
                                                            Row(modifier= Modifier
                                                                .fillMaxWidth()
                                                                .padding(5.dp),
                                                                verticalAlignment = Alignment.CenterVertically){
                                                                Box(modifier= Modifier
                                                                    .fillMaxWidth()
                                                                    .weight(1f)){
                                                                    ComboBox(
                                                                        hasTitle = false,
                                                                        loadedItems = sectors,
                                                                        selectedItem = selectedSector,
                                                                        selectedContent = { CustomInput(selectedSector.value?.name?: SELECT_SECTOR_LABEL)}
                                                                    ){l->Label(l?.name?:EMPTY_STRING)}
                                                                }

                                                            }
                                                        }
                                                        BloodFilterBy.HOSPITAL_TYPE->{
                                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                                verticalAlignment = Alignment.CenterVertically){
                                                                Box(modifier= Modifier
                                                                    .fillMaxWidth()
                                                                    .weight(1f)){
                                                                    ComboBox(
                                                                        hasTitle = false,
                                                                        loadedItems = hospitalTypes,
                                                                        selectedItem = selectedHospitalType,
                                                                        selectedContent = { CustomInput(selectedHospitalType.value?.name?:SELECT_HOSPITAL_TYPE_LABEL)}
                                                                    ){l->Label(l?.name?:EMPTY_STRING)}
                                                                }

                                                            }
                                                        }
                                                        BloodFilterBy.NBTS_SECTOR->{
                                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                                verticalAlignment = Alignment.CenterVertically){
                                                                Box(modifier= Modifier
                                                                    .fillMaxWidth()
                                                                    .weight(1f)){
                                                                    ComboBox(
                                                                        hasTitle = false,
                                                                        loadedItems = bloodBanks,
                                                                        selectedItem = selectedBloodBank,
                                                                        selectedContent = { CustomInput(selectedBloodBank.value?.name?: SELECT_BLOOD_BANK_LABEL)}
                                                                    ){l->Label(l?.name?:EMPTY_STRING)}
                                                                }

                                                            }
                                                        }
                                                        BloodFilterBy.CERTAIN_DIRECTORATE->{
                                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                                verticalAlignment = Alignment.CenterVertically){
                                                                Box(modifier= Modifier.fillMaxWidth().weight(1f)){
                                                                    ComboBox(
                                                                        hasTitle = false,
                                                                        loadedItems = filteredHospitals,
                                                                        selectedItem = selectedHospital,
                                                                        selectedContent = { CustomInput(selectedHospital.value?.name?: SELECT_HOSPITAL_LABEL)}
                                                                    ) {l->Label(l?.name?:EMPTY_STRING)}
                                                                }
                                                                IconButton(R.drawable.ic_view_timeline_blue) {selectedHospitals.value=filteredHospitals }

                                                            }
                                                        }
                                                        else->{ Label("NA") }
                                                    }
                                                }
                                            }
                                            if(selectByBloodGroups){
                                                Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            hasTitle = false,
                                                            loadedItems = bloodGroups,
                                                            selectedItem = selectedBloodGroup,
                                                            selectedContent = { CustomInput(selectedBloodGroup.value?.name?: SELECT_BLOOD_GROUP_LABEL)}
                                                        ) {
                                                            Label(it?.name?:EMPTY_STRING)
                                                        }
                                                    }
                                                }
                                                Row(modifier= Modifier.fillMaxWidth().padding(5.dp),
                                                    verticalAlignment = Alignment.CenterVertically){
                                                    Box(modifier= Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)){
                                                        ComboBox(
                                                            hasTitle = false,
                                                            loadedItems = unitTypes,
                                                            selectedItem = selectedUnitType,
                                                            selectedContent = { CustomInput(selectedUnitType.value?.name?: SELECT_UNIT_TYPE_LABEL)}
                                                        ) {
                                                            Label(it?.name?:EMPTY_STRING)
                                                        }
                                                    }
                                                }

                                            }
                                            Row(modifier= Modifier.fillMaxWidth().padding(5.dp).padding(horizontal = 15.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween){
                                                CustomButton(label= FILTER_LABEL,
                                                    buttonShape = rcs(5),
                                                    enabled = (permissions.contains(VIEW_ALL_BLOOD_STOCKS) ||
                                                            (permissions.contains(VIEW_CERTAIN_DIRECTORATE_BLOOD_STOCKS) && selectedHospitals.value.isNotEmpty())
                                                            )
                                                ) {
                                                    val created=DailyBloodStockFilterBody(
                                                        hospitalIds =  if(selectedHospitals.value.isNotEmpty())selectedHospitals.value.map { it.id } else null,
                                                        bloodGroupIds = if(selectedBloodGroups.value.isNotEmpty())selectedBloodGroups.value.map { it.id?:0 } else null,
                                                        sectorIds =  if(selectedSectors.value.isNotEmpty())selectedSectors.value.map { it.id } else null,
                                                        typeIds =  if(selectedHospitalTypes.value.isNotEmpty())selectedHospitalTypes.value.map { it.id } else null,
                                                        bloodBankIds =  if(selectedBloodBanks.value.isNotEmpty())selectedBloodBanks.value.map { it.id } else null,
                                                        bloodUnitTypeId = selectedUnitType.value?.id,
                                                        startDate = if(startDate.value.trim()!=EMPTY_STRING)startDate.value else null,
                                                        endDate = if(endDate.value.trim()!=EMPTY_STRING)endDate.value else null,
                                                        timeBlock = selectedTimeBlock.value?.first,
                                                    )
                                                    body.value=created
                                                    controller.filterHospitalsBloodStock(created)

                                                }
                                                CustomButton(label= CANCEL_LABEL, buttonShape = rcs(5), enabledBackgroundColor = Color.Red) { showDialog.value=false }
                                                VerticalSpacer()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(fail){
                            FailScreen(modifier=Modifier.fillMaxSize(),
                                errors= emptyMap(),
                                message = ERROR_LOADING_DATA_LABEL)}
                        if(success) SuccessScreen{
                            ColumnContainer {
                                Column(horizontalAlignment = Alignment.CenterHorizontally){
                                    Image(painter = painterResource(R.drawable.logo),contentDescription = null)
                                    Label(DATA_LOADED_LABEL)
                                    CustomButton(label = OK_LABEL) { showDialog.value=false }
                                }
                            }
                        }
                    }
                }

            }

        }
    }

}

@Composable
private fun Cell(modifier:Modifier=Modifier,text:String,fontSize:Int,spacerHeight:Int=0,padding:Int=0){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label(text, fontSize = fontSize, textOverflow = TextOverflow.Ellipsis, softWrap = true, paddingEnd = padding, paddingStart = padding)
        VerticalSpacer(spacerHeight)
        HorizontalDivider(thickness = 2.dp)
    }
}

@Composable
private fun SpanCell(modifier:Modifier=Modifier, text:String, fontSize:Int, backgroundColor:Color= BLUE, spacerHeight:Int=0,padding: Int=0){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Span(
            text = text,
            color = Color.White,
            fontSize = fontSize,
            backgroundColor = backgroundColor,
            paddingStart = padding,
            paddingEnd = padding
        )
        VerticalSpacer(spacerHeight)
        HorizontalDivider()
    }
}

suspend fun saveExcelFile(responseBody: ResponseBody, fileName: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsFolder.exists()) downloadsFolder.mkdirs()

            val file = File(downloadsFolder, fileName)
            val inputStream = responseBody.byteStream()
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

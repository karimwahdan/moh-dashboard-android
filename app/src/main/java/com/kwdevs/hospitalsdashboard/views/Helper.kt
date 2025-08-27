package com.kwdevs.hospitalsdashboard.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

//Icons
val ARROW_UP= R.drawable.ic_arrow_up_white
val ARROW_DOWN=R.drawable.ic_arrow_down_white
//-------------------
val RIGHT_DIRECTION= LayoutDirection.Rtl
val LEFT_DIRECTION = LayoutDirection.Ltr
val RIGHT_LAYOUT_DIRECTION = LocalLayoutDirection provides RIGHT_DIRECTION
val LEFT_LAYOUT_DIRECTION = LocalLayoutDirection provides LEFT_DIRECTION


fun rcs(corners:Int=5): RoundedCornerShape {
    return RoundedCornerShape(corners.dp)
}
fun rcsB(corners: Int=5): RoundedCornerShape {
    return RoundedCornerShape(bottomStart = corners.dp,bottomEnd = corners.dp)
}
fun rcsT(corners: Int=5): RoundedCornerShape {
    return RoundedCornerShape(topStart = corners.dp, topEnd = corners.dp)
}

fun hexToComposeColor(hex: String): Color {
    val formattedHex = if (hex.startsWith("#")) hex else "#$hex"
    return Color(android.graphics.Color.parseColor(formattedHex))
}

fun shortToast(context: Context,text:String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

val numericKeyBoard= KeyboardOptions(keyboardType = KeyboardType.Number)

@OptIn(ExperimentalMaterial3Api::class)
fun getFormattedDateJavaTime(datePickerState: DatePickerState): String {
    val selectedMillis = datePickerState.selectedDateMillis ?: return "No date selected" // Handle null case
    val instant = Instant.ofEpochMilli(selectedMillis)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

    // Option 1: Using a predefined style (recommended for localization)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
    val customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
    return localDate.format(customFormatter)
}

@OptIn(ExperimentalMaterial3Api::class)
fun getFormattedDateTimeJavaTime(datePickerState: DatePickerState): String {
    val selectedMillis = datePickerState.selectedDateMillis ?: return "No date selected" // Handle null case
    val instant = Instant.ofEpochMilli(selectedMillis)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

    // Option 1: Using a predefined style (recommended for localization)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
    val customFormatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss", Locale.ENGLISH)
    return localDate.format(customFormatter)
}

fun patientFullName(item:Patient?): String {
    return "${item?.firstName?:""} ${item?.secondName?:""} ${item?.thirdName?:""} ${item?.fourthName?:""}"
}



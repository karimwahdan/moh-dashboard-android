package com.kwdevs.hospitalsdashboard.views.assets

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.Instance
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor


val GRAY= grayColor()
fun grayColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.gray)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val GREEN= greenColor()
fun greenColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.green)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val PALE_ORANGE=paleOrangeColor()
fun paleOrangeColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.pale_orange)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val WHITE= whiteColor()
fun whiteColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.white)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val BLACK= blackColor()
fun blackColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.black)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val BLUE= blueColor()
fun blueColor(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.blue)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val PINK = PinkColor()
fun PinkColor():Color{
    val context = Instance.context
    val colorInt=ContextCompat.getColor(context,R.color.pink)
    val colorString=String.format("#%06X",0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val BLUE2= blue2Color()
fun blue2Color(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.blue2)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}
val BLUE3= blue3Color()
fun blue3Color(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.blue3)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}
val TEAL700= teal700Color()
fun teal700Color(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.teal_700)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val TEAL200= teal200Color()
fun teal200Color(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.teal_200)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val YELLOW= yellow()
fun yellow(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.yellow)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}

val ORANGE= orange()
fun orange(): Color {
    val context = Instance.context
    val colorInt = ContextCompat.getColor(context, R.color.orange)
    val colorString = String.format("#%06X", 0xFFFFFF and colorInt)
    return hexToComposeColor(colorString)
}
package com.kwdevs.hospitalsdashboard.views.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun CustomButton(
    modifier: Modifier=Modifier,
    label:String,
    labelFontWeight: FontWeight=FontWeight.Bold,
    labelFontSize:Int=14,
    labelSoftWrap:Boolean=true,
    labelTextOverflow: TextOverflow=TextOverflow.Clip,
    labelTextAlign: TextAlign=TextAlign.Center,
    labelMaximumLines:Int=2,
    buttonShape: Shape= rcs(20),
    enabledBackgroundColor:Color= GREEN,
    disabledBackgroundColor:Color= GRAY,
    enabledFontColor:Color= WHITE,
    disabledFontColor:Color= WHITE,
    enabled:Boolean=true,
    buttonShadowElevation:Int=0,
    onClick:()->Unit){
    Button(
        modifier = modifier
            .shadow(elevation = buttonShadowElevation.dp, shape = buttonShape)
            .background(color=if(enabled)enabledBackgroundColor else disabledBackgroundColor,
                shape=buttonShape)
        ,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = enabledFontColor,
            disabledContentColor = disabledFontColor,),
        enabled = enabled,
        onClick = onClick) {
        Label(
            text=label,
            color = if(enabled) enabledFontColor else disabledFontColor,
            fontWeight = labelFontWeight,
            fontSize = labelFontSize,
            softWrap = labelSoftWrap,
            textOverflow = labelTextOverflow,
            textAlign = labelTextAlign,
            maximumLines = labelMaximumLines
        )
    }
}

@Composable
fun CustomButtonWithImage(icon:Int = R.drawable.moh_logo,
                          enabled: Boolean=true,
                          background: Color= WHITE,
                          iconSize:Int=52,
                          label:String="",
                          maxLines:Int=2,
                          maxWidth:Int=52,
                          minWidth:Int=52,
                          onClick: () -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Button(modifier=Modifier.width(iconSize.dp)
            .background(color=Color.Transparent), onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = if(enabled)Color.Transparent else GRAY,
                contentColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = rcs(5)) {
            Column(modifier=Modifier.padding(0.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                Column(modifier=Modifier
                    .shadow(elevation = 5.dp)
                    .background(shape = rcs(5), color = if(enabled)background else WHITE)
                    .border(width = 1.dp, shape = rcs(5), color = GRAY),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Box(modifier=Modifier, contentAlignment = Alignment.Center){
                        Icon(icon=if(enabled) icon else R.drawable.ic_lock_blue, size = iconSize, containerSize = iconSize,
                            background = if(enabled)background else WHITE)

                    }
                }
            }

        }
        if(label.trim()!=""){
            Row(modifier=Modifier.widthIn(min=minWidth.dp,max = maxWidth.dp).padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Center){
                Label(label, fontSize = 12, textOverflow = TextOverflow.Ellipsis, maximumLines = maxLines)
            }
        }
    }
}

@Composable
fun AddNewItemButton(background:Color= BLUE,elevation:Int=5,onClick: () -> Unit){
    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        CustomButton(label = ADD_NEW_LABEL, enabledBackgroundColor = background, buttonShadowElevation = elevation, onClick = onClick)
    }
}
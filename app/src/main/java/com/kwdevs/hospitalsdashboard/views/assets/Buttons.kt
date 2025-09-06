package com.kwdevs.hospitalsdashboard.views.assets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.routes.Destination
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun DrawerMenuButton(label: String, icon: Int, buttonColor: Color, fontColor: Color=WHITE,
                     contentColor: Color= GRAY,
                     content:@Composable ()->Unit){
    var open by remember { mutableStateOf(false) }
    Column(modifier=Modifier.fillMaxWidth()){
        Row(modifier=Modifier.fillMaxWidth().clickable { open=!open }
            .background(color=buttonColor).padding(vertical = 5.dp),verticalAlignment = Alignment.CenterVertically){
            Row(modifier=Modifier.fillMaxWidth().weight(1f),verticalAlignment = Alignment.CenterVertically){
                HorizontalSpacer()
                Icon(icon)
                HorizontalSpacer()
                Label(label, color = fontColor, paddingTop = 5, paddingBottom = 5)
                HorizontalSpacer()
            }
            Icon(size = 18, containerSize = 18,background = BLUE,icon=if(open)R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white)
            HorizontalSpacer(3)
        }
        AnimatedVisibility(visible = open,
            enter = fadeIn()+ expandVertically(),
            exit = fadeOut()+ shrinkVertically()) {
            Column(modifier=Modifier.fillMaxWidth().padding(start = 30.dp)) {
                content()
            }
        }
    }
}
@Composable
fun DrawerButton(label: String,icon: Int,buttonColor: Color=GRAY,fontColor: Color=WHITE,onClick: () -> Unit){
    Row(modifier=Modifier.fillMaxWidth().clickable { onClick.invoke() }
        .background(color=buttonColor).padding(vertical = 5.dp),verticalAlignment = Alignment.CenterVertically){
        HorizontalSpacer()
        Icon(icon)
        HorizontalSpacer()
        Label(label, color = fontColor, paddingTop = 5, paddingBottom = 5)
        HorizontalSpacer()
    }
}
@Composable
fun DrawerButton(navHostController: NavHostController,route:Destination,label: String,icon: Int,
                 buttonColor: Color=GRAY,fontColor: Color=WHITE){
    Row(modifier=Modifier.fillMaxWidth().clickable { navHostController.navigate(route.route) }
        .background(color=buttonColor).padding(vertical = 5.dp),verticalAlignment = Alignment.CenterVertically){
        HorizontalSpacer()
        Icon(icon)
        HorizontalSpacer()
        Label(label, color = fontColor, paddingTop = 5, paddingBottom = 5)
        HorizontalSpacer()
    }

}

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
                shape=buttonShape),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = enabledFontColor,
            disabledContentColor = disabledFontColor,),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal=3.dp, vertical = 2.dp),
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
    hasBorder:Boolean,
    borderColor: Color=Color.LightGray,
    onClick:()->Unit){
    Button(
        modifier = modifier
            .shadow(elevation = if(hasBorder)0.dp else buttonShadowElevation.dp, shape = buttonShape)
            .background(color=if(enabled)enabledBackgroundColor else disabledBackgroundColor,
                shape=buttonShape)
            .border(width = if(hasBorder)1.dp else 0.dp, color = borderColor, shape = buttonShape),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = enabledFontColor,
            disabledContentColor = disabledFontColor,),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal=3.dp, vertical = 2.dp),
        onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically){
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
}


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
    horizontalPadding:Int=10,
    hasBorder:Boolean,
    borderColor: Color=Color.LightGray,
    icon: Int,
    maxHeight:Int=45,
    onClick:()->Unit){
    Button(
        modifier = modifier
            .shadow(elevation = if(hasBorder)0.dp else buttonShadowElevation.dp, shape = buttonShape)
            .background(color=if(enabled)enabledBackgroundColor else disabledBackgroundColor,
                shape=buttonShape)
            .border(width = if(hasBorder)1.dp else 0.dp, color = borderColor, shape = buttonShape),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = enabledFontColor,
            disabledContentColor = disabledFontColor,),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal=3.dp, vertical = 2.dp),
        onClick = onClick) {
        Row(modifier=Modifier.heightIn(min=45.dp,max=maxHeight.dp),verticalAlignment = Alignment.CenterVertically){
            HorizontalSpacer()
            Icon(icon=icon, background = if(enabled)enabledBackgroundColor else disabledBackgroundColor)
            HorizontalSpacer()
            VerticalDivider(thickness = 2.dp,color = borderColor)
            Label(
                text=label,
                color = if(enabled) enabledFontColor else disabledFontColor,
                fontWeight = labelFontWeight,
                fontSize = labelFontSize,
                paddingStart = horizontalPadding,
                paddingEnd = horizontalPadding,
                softWrap = labelSoftWrap,
                textOverflow = labelTextOverflow,
                textAlign = labelTextAlign,
                maximumLines = labelMaximumLines
            )
        }
    }
}

@Composable
fun CustomButtonLeftIcon(
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
    horizontalPadding:Int=10,
    hasBorder:Boolean,
    borderColor: Color=Color.LightGray,
    icon: Int,
    maxHeight:Int=45,
    onClick:()->Unit){
    Button(
        modifier = modifier
            .shadow(elevation = if(hasBorder)0.dp else buttonShadowElevation.dp, shape = buttonShape)
            .background(color=if(enabled)enabledBackgroundColor else disabledBackgroundColor,
                shape=buttonShape)
            .border(width = if(hasBorder)1.dp else 0.dp, color = borderColor, shape = buttonShape),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = enabledFontColor,
            disabledContentColor = disabledFontColor,),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal=3.dp, vertical = 2.dp),
        onClick = onClick) {
        Row(modifier=Modifier.heightIn(min=45.dp,max=maxHeight.dp),verticalAlignment = Alignment.CenterVertically){
            Label(
                text=label,
                color = if(enabled) enabledFontColor else disabledFontColor,
                fontWeight = labelFontWeight,
                fontSize = labelFontSize,
                paddingStart = horizontalPadding,
                paddingEnd = horizontalPadding,
                softWrap = labelSoftWrap,
                textOverflow = labelTextOverflow,
                textAlign = labelTextAlign,
                maximumLines = labelMaximumLines
            )
            VerticalDivider(thickness = 2.dp,color = borderColor)
            HorizontalSpacer()
            Icon(icon=icon, background = if(enabled)enabledBackgroundColor else disabledBackgroundColor)
            HorizontalSpacer()

        }
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
package com.kwdevs.hospitalsdashboard.views.assets.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.Header
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(title:String,
              headerAlignment:TextAlign=TextAlign.Center,
              headerColor: Color = Color. Black,
              headerFontSize: Int = 14,
              headerShape: Shape = RectangleShape,
              headerFontWeight: FontWeight = FontWeight. Normal,
              headerShowBackButton:Boolean=false,
              headerIconButtonBackground: Color = Color.White,
              headerArrangement:Arrangement.Horizontal=Arrangement.Center,
              icon:Int= R.drawable.ic_arrow_back_white,
              showSheet:MutableState<Boolean>,
              sheetState: SheetState=rememberModalBottomSheetState(skipPartiallyExpanded = true),
              sheetContent:@Composable ()->Unit={},
              sheetColor: Color=Color.Red,
              shape: Shape= rcs(20),
              headerOnClick: () -> Unit={},
              sheetOnClick:()->Unit={},
              content:@Composable ()->Unit){
    if (showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = sheetColor,
            contentColor = WHITE,
            dragHandle = {}
        ) {
           Column(modifier=Modifier.fillMaxWidth()){
               VerticalSpacer()
               Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                   verticalAlignment = Alignment.CenterVertically){
                   IconButton(R.drawable.ic_close_white,
                       background = Color.Transparent) {
                       try { sheetOnClick.invoke() }catch (e:Exception){e.printStackTrace()}
                       showSheet.value=false
                   }
                   Box(modifier=Modifier.fillMaxWidth().weight(1f),
                       contentAlignment = Alignment.CenterEnd){
                       sheetContent() }
                   HorizontalSpacer()
                   Icon(R.drawable.ic_cancel_white, background = Color.Transparent)
               }
               VerticalSpacer()
           }
        }
    }
    Column(modifier= Modifier.background(Color.White)) {
        Header(text = title, textAlign = headerAlignment,
            color=headerColor,
            fontSize = headerFontSize,
            shape = headerShape,
            fontWeight=headerFontWeight,
            showBackButton =  headerShowBackButton,
            iconButtonBackground=headerIconButtonBackground,
            headerArrangement = headerArrangement,
            icon = icon,
            onClick = headerOnClick
        )
        ColumnContainer(shape =shape ) {
            content()
        }
    }

}
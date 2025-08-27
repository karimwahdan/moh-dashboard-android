package com.kwdevs.hospitalsdashboard.views.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> ComboBox(
    modifier: Modifier=Modifier,
    title: String= EMPTY_STRING,
    hasTitle:Boolean=true,
    default:Boolean=true,
    selectedItem: MutableState<T>,
    loadedItems: List<T>?,
    selectedContent: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
        val x=Modifier.fillMaxWidth()
        .border(width=1.dp, shape = RoundedCornerShape(5.dp),color= Color.Gray)
        .background(color= Color.White,shape= RoundedCornerShape(5.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        if(hasTitle) Text(modifier = Modifier.fillMaxWidth(), text = title)
        ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth(), expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value })
        {
            Box(modifier= if(default) x else modifier){
                selectedContent()
            }


            ExposedDropdownMenu(
                scrollState = rememberScrollState(),
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false })
            {
                loadedItems?.forEach {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedItem.value = it;expanded.value = false },
                        content = { itemContent(it) }

                    )
                }
            }
        }
    }
}
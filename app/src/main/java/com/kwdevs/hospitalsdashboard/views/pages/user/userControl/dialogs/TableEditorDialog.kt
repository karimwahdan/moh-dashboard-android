package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.settings.CustomModel
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.COLUMN_NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.EDIT_TABLE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.NAME_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.Span
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer

@Composable
fun NewTableDialog(showDialog:MutableState<Boolean>,name:MutableState<String>,columns:MutableState<List<String>>,onClick:()->Unit){
    val columnName = remember { mutableStateOf("") }
    if(showDialog.value){
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Label(ADD_NEW_TABLE_LABEL)
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        IconButton(R.drawable.ic_cancel_red, paddingStart = 10, paddingEnd = 10) {showDialog.value=false }
                        IconButton(R.drawable.ic_check_circle_green, paddingStart = 10, paddingEnd = 10, onClick = onClick)
                    }
                }
                VerticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 0..3) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 4..8) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 9..12) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 13..16) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                VerticalSpacer()
                CustomInput(value = name, label = NAME_LABEL)
                VerticalSpacer()
                CustomInput(value = columnName, label = COLUMN_NAME_LABEL)
                IconButton(R.drawable.ic_add_circle_green) {
                    val nl= mutableListOf<String>()
                    nl.addAll(columns.value)
                    if(columnName.value !in columns.value) nl.add(columnName.value)

                    columns.value=nl
                }
            }
        }
    }
}

@Composable
fun EditTableDialog(showDialog:MutableState<Boolean>,model:MutableState<CustomModel?>,name:MutableState<String>,columns:MutableState<List<String>>,onClick:()->Unit){
    val columnName = remember { mutableStateOf("") }

    if(showDialog.value){
        LaunchedEffect(Unit) {
            if(model.value!=null){
                name.value=model.value?.name?:""
                columns.value=model.value?.columns?: emptyList()
            }
        }
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Label(EDIT_TABLE_LABEL)
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        IconButton(R.drawable.ic_cancel_red, paddingStart = 10, paddingEnd = 10) {showDialog.value=false }
                        IconButton(R.drawable.ic_check_circle_green, paddingStart = 10, paddingEnd = 10, onClick = onClick)
                    }
                }
                VerticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 0..3) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 4..8) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 9..12) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    columns.value.forEachIndexed { index, s ->
                        if (index in 13..16) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Span(text = s)
                                IconButton(R.drawable.ic_delete_red) {columns.value=columns.value.filter { it!=s } }
                            }
                        }
                    }
                }
                VerticalSpacer()
                CustomInput(value = name, label = NAME_LABEL)
                VerticalSpacer()
                CustomInput(value = columnName, label = COLUMN_NAME_LABEL)
                IconButton(R.drawable.ic_add_circle_green) {
                    val nl= mutableListOf<String>()
                    nl.addAll(columns.value)
                    if(columnName.value !in columns.value) nl.add(columnName.value)

                    columns.value=nl
                }
            }
        }
    }
}
package com.kwdevs.hospitalsdashboard.views.assets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.ColumnDefinition

@Composable
fun <T> AdvancedDataTable(
    items: List<T>,
    columns: List<ColumnDefinition<T>>,
    rowKey: (T) -> String,
    modifier: Modifier = Modifier
) {

    val context=LocalContext.current

    var sortColumn by remember { mutableStateOf<ColumnDefinition<T>?>(null) }

    var sortAscending by remember { mutableStateOf(true) }

    var filterText by remember { mutableStateOf("") }

    var selectedRows by remember { mutableStateOf(setOf<String>()) }
    var editingRowKey by remember { mutableStateOf<String?>(null) }
    var editBuffer by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val filteredItems = remember(filterText, items) {
        if (filterText.isBlank()) items else
            items.filter { item ->
                columns.any { col ->
                    col.cellText(item).contains(filterText, ignoreCase = true)
                }
            }
    }

    val sortedItems = remember(sortColumn, sortAscending, filteredItems) {
        if (sortColumn == null) filteredItems else {
            val comparator = compareBy<T> { sortColumn!!.cellText(it) }
            if (sortAscending) filteredItems.sortedWith(comparator)
            else filteredItems.sortedWith(comparator.reversed())
        }
    }

    Column(modifier = modifier.padding(8.dp)) {
        // Filter input
        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Filter") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Sticky Header
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selectedRows.size == sortedItems.size && sortedItems.isNotEmpty(),
                onCheckedChange = { checked ->
                    selectedRows = if (checked) sortedItems.map(rowKey).toSet() else emptySet()
                }
            )
            columns.forEach { col ->
                Text(
                    text = col.header,modifier = Modifier.weight(col.weight)
                        .clickable {
                            if (sortColumn == col) sortAscending = !sortAscending
                            else sortColumn = col;sortAscending = true
                        },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // Table Body
        LazyColumn {
            items(sortedItems) { item ->
                val isEditing = editingRowKey == rowKey(item)
                val key = rowKey(item)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedRows.contains(key),
                        onCheckedChange = {
                            selectedRows = if (it) selectedRows + key else selectedRows - key
                        }
                    )

                    columns.forEach { col ->
                        Box(modifier = Modifier.weight(1f)) {
                            if (isEditing && col.editable) {
                                val currentValue = editBuffer[col.key] ?: col.cellText(item)
                                OutlinedTextField(
                                    value = currentValue,
                                    onValueChange = { new ->
                                        editBuffer = editBuffer + (col.key to new)
                                    },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            else {
                                Text(
                                    text = col.cellText(item),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        if (isEditing && col.editable) {
                            IconButton(
                                icon=R.drawable.ic_save_blue,
                                onClick = {
                                    if (columns.all { !it.editable || !it.required || (editBuffer[it.key]?.isNotBlank() == true) }) {
                                        col.onSave?.invoke(key, editBuffer)
                                        editingRowKey = null
                                        editBuffer = emptyMap()
                                    } else {
                                        Toast.makeText(context,"Validation failed",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )

                            IconButton(
                                icon=R.drawable.ic_cancel_red,
                                onClick = {
                                editingRowKey = null
                                editBuffer = emptyMap()
                            })
                        }
                        else {
                            if(col.editable){
                                IconButton(
                                    icon=R.drawable.ic_edit_blue,
                                    onClick = {
                                    editingRowKey = key
                                    editBuffer = columns.associate { it.key to it.cellText(item) }
                                })

                            }
                        }
                    }
                }

                HorizontalDivider()
            }
        }
    }

}

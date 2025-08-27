package com.kwdevs.hospitalsdashboard.views.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.ColumnDefinition

@Composable
fun<T> CustomTable(
    modifier: Modifier=Modifier,
    data:List<T>,
    columns:List<ColumnDefinition<T>>,

){
    var sortColumn by remember { mutableStateOf<ColumnDefinition<T>?>(null) }
    var sortAscending by remember { mutableStateOf(true) }
    var filterText by remember { mutableStateOf("") }
    val filteredItems = remember(filterText, data) {
        if (filterText.isBlank()) data else
            data.filter { item ->
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
    Column(modifier = modifier) {
        // Filter input
        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Filter") },
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(5)

        // Sticky Header
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns.forEach { col ->
                Box(modifier = Modifier.weight(col.weight)
                    .clickable {
                        if (sortColumn == col) sortAscending = !sortAscending
                        else sortColumn = col;sortAscending = true
                    },
                    contentAlignment = Alignment.Center){
                    Label(col.header)
                }
            }
        }

        HorizontalDivider()

        // Table Body
        sortedItems.forEach { item->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                columns.forEach { col ->
                    Box(modifier = Modifier.border(width=1.dp,color= Color.Gray).weight(1f),
                        contentAlignment = Alignment.Center) {
                        Label(col.cellText(item), fontSize = 12, paddingTop = 3, paddingBottom = 3)

                    }
                }
            }
            //HorizontalDivider()
        }
    }

}

@Composable
fun TableColumn(
    modifier: Modifier=Modifier,
    header:String,
    headFontSize:Int=12,
    itemFontSize:Int=12,
    itemPaddingTop:Int=5,
    itemPaddingBottom:Int=5,
    itemPaddingStart:Int=5,
    headerPaddingStart:Int=5,
    headerPaddingEnd:Int=5,
    itemPaddingEnd:Int=5,
    items:List<String>,){
    Column(modifier=modifier.border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally){
        Box(modifier=Modifier.fillMaxWidth().background(color= colorResource(R.color.pale_orange)),
            contentAlignment = Alignment.Center){
            Label(text=header, fontSize = headFontSize, maximumLines = 3, paddingTop = 5, paddingBottom = 5, paddingStart =headerPaddingStart, paddingEnd = headerPaddingEnd)
        }
        HorizontalDivider()
        items.forEach {Label(it, fontSize = itemFontSize, paddingTop = itemPaddingTop,
            paddingBottom = itemPaddingBottom,
            paddingStart = itemPaddingStart,
            paddingEnd = itemPaddingEnd)
            HorizontalDivider()}
    }
}

@Composable
fun DualTableColumn(
    modifier: Modifier=Modifier,
    header:String,
    firstSubHeader:String,
    secondSubHeader:String,
    headFontSize:Int=12,
    itemFontSize:Int=12,
    itemPaddingTop:Int=5,
    itemPaddingBottom:Int=5,
    itemPaddingStart:Int=5,
    headerPaddingStart:Int=5,
    headerPaddingEnd:Int=5,
    itemPaddingEnd:Int=5,
    firstList:List<String>,
    secondList: List<String>,
    ){
    Column(modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally){
        Column(modifier=Modifier.fillMaxWidth().background(color= colorResource(R.color.pale_orange)), horizontalAlignment = Alignment.CenterHorizontally){
            Label(text=header, fontSize = headFontSize, maximumLines = 1, textOverflow = TextOverflow.Ellipsis, softWrap = true, paddingStart =headerPaddingStart, paddingEnd = headerPaddingEnd)
            Row(modifier=Modifier.fillMaxWidth()){
                Box(modifier=Modifier.fillMaxWidth().background(color= colorResource(R.color.yellow2)),
                    contentAlignment = Alignment.Center){
                    Label(text=firstSubHeader, fontSize = headFontSize, maximumLines = 3, paddingStart =2, paddingEnd = 2, paddingBottom = 6)

                }
                Box(modifier=Modifier.fillMaxWidth().background(color= colorResource(R.color.light_green)),
                    contentAlignment = Alignment.Center){
                    Label(text=secondSubHeader, fontSize = headFontSize, maximumLines = 3, paddingStart =2, paddingEnd = 2, paddingBottom = 6)

                }

            }
        }
        HorizontalDivider()
        Row(modifier=Modifier.widthIn(min=100.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier=Modifier.width(35.dp).border(width = 1.dp, color = Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {
                firstList.forEach {Label(it, fontSize = itemFontSize, paddingTop = itemPaddingTop,
                    paddingBottom = itemPaddingBottom,
                    paddingStart = itemPaddingStart,
                    paddingEnd = itemPaddingEnd)
                    HorizontalDivider()
                }
            }
            Column(modifier=Modifier.width(65.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                secondList.forEach {
                    Label(it, fontSize = itemFontSize, paddingTop = itemPaddingTop,
                    paddingBottom = itemPaddingBottom,
                    paddingStart = itemPaddingStart,
                    paddingEnd = itemPaddingEnd)
                    HorizontalDivider()
                }
            }

        }

    }
}
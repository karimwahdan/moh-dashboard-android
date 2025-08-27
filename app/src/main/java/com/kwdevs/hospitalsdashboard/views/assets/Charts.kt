package com.kwdevs.hospitalsdashboard.views.assets

import android.view.animation.Animation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.ColorTemplate
import com.kwdevs.hospitalsdashboard.views.hexToComposeColor

@Composable
fun DraggableBar(sliderValue: MutableState<Float>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val colors= SliderColors(
            thumbColor = Color.Black,
            activeTrackColor = Color.White,
            activeTickColor = Color.Blue,
            inactiveTickColor = Color.Gray,
            inactiveTrackColor = Color.Gray,
            disabledInactiveTickColor = Color.Gray,
            disabledThumbColor = Color.Gray,
            disabledActiveTrackColor = Color.Gray,
            disabledActiveTickColor = Color.LightGray,
            disabledInactiveTrackColor = Color.LightGray)
        // Slider component
        Slider(
            colors = colors,
            value = sliderValue.value,
            onValueChange = { value ->
                sliderValue.value = value  // Update the value when slider is dragged
            },
            valueRange = 40f..100f,  // Set the range of the slider
            modifier = Modifier
                .fillMaxWidth()
                .background(color= hexToComposeColor("#182424"))
            ,
            steps = 20  // Optional: Divides the range into steps for a more discrete slider
        )

        // Display the value of the slider
        Text(
            text = "Width: ${sliderValue.value.toInt()}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
val BOTTOM_INSIDE=XAxis.XAxisPosition.BOTTOM_INSIDE
val BOTTOM=XAxis.XAxisPosition.BOTTOM
val BOTH_SIDED=XAxis.XAxisPosition.BOTH_SIDED
val TOP=XAxis.XAxisPosition.TOP
val TOP_INSIDE=XAxis.XAxisPosition.TOP_INSIDE

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    xData: List<String>,
    yData: List<Int>,
    label: String,
    lineColor: Color = Color.Red,
    fillColors: Color = Color.Red,
    fillAlphas: Int = 50,
    axisTextColor: Color = Color.White,
    drawValues: Boolean = false,
    drawMarkers: Boolean = false,
    drawFilled: Boolean = true,
    descriptionEnabled: Boolean = false,
    description:String?,
    legendEnabled: Boolean = true,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            val chart = LineChart(context)  // Initialise the chart
            val values = yData.map {it.toFloat() }
            val entries: List<Entry> = values.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value)
            }
            val dataSet = LineDataSet(entries, label).apply {
                // Here we apply styling to the dataset
                color = lineColor.toArgb()
                setDrawValues(drawValues)
                setDrawCircles(drawMarkers)
                setDrawFilled(drawFilled)
                fillColor = fillColors.toArgb()
                fillAlpha = fillAlphas
                mode=LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(dataSet)  // Pass the dataset to the chart
            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = true
            chart.moveViewToX(0f)
            chart.xAxis.apply {
                textColor=axisTextColor.toArgb()
                position=xAxisPosition
                isGranularityEnabled = true
                granularity = 1f // Ensure one label per data point
                setDrawLabels(true)
                valueFormatter= IndexAxisValueFormatter(xData)
                setAvoidFirstLastClipping(false)
            }
            chart.axisLeft.apply {
                axisMinimum = 0f
                textColor = axisTextColor.toArgb()
            }
            chart.description.text=if(descriptionEnabled && description!=null)description else EMPTY_STRING
            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}


@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    xData: List<String>,
    yData: List<Int>,
    barsColors:List<Color> = emptyList(),
    horizontalAxisLabelRotationAngle:Int=45,
    horizontalAxisLabelSize:Int=12,
    horizontalAxisLabelColor: Color = Color.Black,
    horizontalAxisLabelPosition: XAxis.XAxisPosition = BOTTOM,
    showHorizontalAxisLabels:Boolean=true,

    verticalAxisColor: Color = Color.DarkGray,
    verticalAxisFontSize:Int=16,
    valueColor:Color=Color.Black,
    borderColor:Color=Color.Black,
    backgroundColor:Color=Color.White,
    showBarValues: Boolean = false,
    drawMarkers: Boolean = false,
    descriptionEnabled: Boolean = false,
    clipBarValues:Boolean=false,
    legendTextColor:Color=Color.Black,
    legendEnabled: Boolean = true,
    legendTextSize:Int=12,

    barValueTextSize:Int=16,
    barBackgroundColor:Color=Color.LightGray,
    chartDescriptionText:String?,
    chartDescriptionColor: Color=Color.Black,
    chartDescriptionSize:Int=12,
    showVerticalRightAxis:Boolean=false,
){
    AndroidView(
        modifier = modifier.fillMaxSize().background(Color.White),
        factory = { context ->
            BarChart(context)  // Initialise the chart
        },
        update = {chart->
            val values = yData.map {it.toFloat() }
            val entries = values.mapIndexed { index, value ->BarEntry(index.toFloat(), value)}
            val gradientColors=barsColors.map {GradientColor(Color.White.toArgb(), it.toArgb())}.toList()
            //TODO Bar
            val barDataSet = BarDataSet(entries, EMPTY_STRING)
                .apply {
                    //color = datasetColor.toArgb()
                    if(barsColors.isNotEmpty()) setGradientColors(gradientColors)
                    else setColor(barBackgroundColor.toArgb())
                    valueTextColor = valueColor.toArgb()
                    valueTextSize=barValueTextSize.toFloat()
                    formSize=12f
                    barBorderWidth=1f
                    barBorderColor=borderColor.toArgb()
                    setDrawValues(showBarValues)


                }

            val data = BarData(barDataSet)  // Pass the dataset to the chart
            data.barWidth = 0.4f  // Set the bar width
            chart.data = data  // Pass the dataset to the chart

            //TODO vertical axis
            chart.axisLeft.apply {
                textColor=verticalAxisColor.toArgb()
                textSize=verticalAxisFontSize.toFloat()
                axisMinimum=0f
                xOffset=20f
            }
            //Horizontal Axis
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xData)
                isGranularityEnabled = true
                granularity = 1f // Ensure one label per data point
                position = horizontalAxisLabelPosition
                textColor = horizontalAxisLabelColor.toArgb()
                textSize=horizontalAxisLabelSize.toFloat()
                labelRotationAngle = horizontalAxisLabelRotationAngle.toFloat()
                axisMinimum = 0f
                setAvoidFirstLastClipping(true)
                setDrawLabels(showHorizontalAxisLabels)
                setLabelCount(xData.size)
            }

            chart.apply {
                setDrawMarkers(drawMarkers)
                setDrawValueAboveBar(showBarValues)
                setClipValuesToContent(clipBarValues)
                setBackgroundColor(backgroundColor.toArgb())
            }

            //TODO Description which is at the bottom of the chart
            chart.description.apply{
                isEnabled=false
                textSize=chartDescriptionSize.toFloat()
                textColor=chartDescriptionColor.toArgb()
                text=if(descriptionEnabled && chartDescriptionText!=null) chartDescriptionText else EMPTY_STRING
            }

            //TODO: If bar-colors list not empty then gradient is applied
            if(barsColors.isNotEmpty() && legendEnabled){
                val legendEntries=barsColors.mapIndexed {index,color->
                    LegendEntry(xData[index], Legend.LegendForm.CIRCLE,10f,
                        10f,null, color.toArgb())
                }
                chart.legend.setCustom(legendEntries)
                chart.legend.apply {
                    isEnabled = true
                    form= Legend.LegendForm.CIRCLE
                    //setEntries(legendEntries)
                    textSize=legendTextSize.toFloat()
                    textColor=legendTextColor.toArgb()
                }
            }
            //Show Right Vertical Axis
            chart.axisRight.isEnabled = showVerticalRightAxis


            // Refresh and return the chart
            chart.invalidate()
        }
    )
}

@Composable
fun MultiBarChart(
    modifier: Modifier = Modifier,
    xLabels: List<String>, // e.g., ["Cairo", "Giza"]
    groupedYData: Map<String, List<Int>>, // e.g., "A+" -> [34, 50], "A-" -> [3, 5]
    groupColors: Map<String, Color>, // e.g., "A+" -> Red, "A-" -> Blue
    barWidth: Float = 0.2f,
    groupSpacing: Float = 0.1f,
    horizontalAxisLabelRotationAngle: Int = 45,
    legendEnabled: Boolean = true,
    chartDescriptionText: String? = null
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            BarChart(context).apply {
                setNoDataText("No data available")
            }
        },
        update = { chart ->
            val groupLabels = groupedYData.keys.sorted()
            val groupCount = groupLabels.size
            val cityCount = xLabels.size

            // Build one BarDataSet per blood group
            val barDataSets = groupLabels.mapIndexed { groupIndex, groupLabel ->
                val entries = groupedYData[groupLabel]?.mapIndexed { cityIndex, value ->
                    // shift x to align with grouped bars
                    BarEntry(cityIndex.toFloat(), value.toFloat())
                } ?: emptyList()

                BarDataSet(entries, groupLabel).apply {
                    color = groupColors[groupLabel]?.toArgb() ?: Color.Gray.toArgb()
                    valueTextSize = 12f
                    valueTextColor = Color.Black.toArgb()
                    barBorderWidth = 1f
                    barBorderColor = Color.Black.toArgb()
                    setDrawValues(true)
                }
            }

            val barData = BarData(barDataSets)
            barData.barWidth = barWidth

            // Group the bars properly
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                granularity = 1f
                isGranularityEnabled = true
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = horizontalAxisLabelRotationAngle.toFloat()
                textSize = 12f
                textColor = Color.Black.toArgb()
                setDrawLabels(true)
                setAvoidFirstLastClipping(true)
                axisMinimum = 0f
                axisMaximum = cityCount.toFloat()
            }

            // Grouping logic
            val groupSpace = groupSpacing
            val barSpace = (1f - groupSpace) / groupCount * 0.1f
            val groupWidth = barData.getGroupWidth(groupSpace, barSpace)

            chart.data = barData
            chart.groupBars(0f, groupSpace, barSpace)

            chart.axisLeft.apply {
                axisMinimum = 0f
                textSize = 12f
                textColor = Color.Black.toArgb()
            }

            chart.axisRight.isEnabled = false

            chart.description.apply {
                text = chartDescriptionText ?: EMPTY_STRING
                isEnabled = !chartDescriptionText.isNullOrEmpty()
                textColor = Color.Black.toArgb()
                textSize = 12f
            }

            chart.legend.apply {
                isEnabled = legendEnabled
                textColor = Color.Black.toArgb()
                textSize = 12f
            }

            chart.invalidate()
        }
    )
}

@Composable
fun PieGraph(xData: List<Pair<String,Int>>,
             entryValueFontSize:Int=26,
             entryValueFontColor:Color=Color.White,
             entryLabelFontSize:Int=14,
             entryLabelFontColor:Color=Color.Black,
             spaceBetweenSlices:Int=4,
             pieCenterText:String= EMPTY_STRING,
             pieCenterTextSize:Int=18,
             pieColors:List<Int> = ColorTemplate.MATERIAL_COLORS.toList(),
             usePercentage:Boolean=false){
    val p = mutableListOf<PieEntry>()
    xData.forEach{ p.add(PieEntry(it.second.toFloat(),it.first)) }
    val entries=p.toList()
    val dataset = PieDataSet(entries,EMPTY_STRING).apply {
        colors = pieColors.toList()
        valueTextSize = entryValueFontSize.toFloat()
        valueTextColor = entryValueFontColor.toArgb()
        sliceSpace = spaceBetweenSlices.toFloat()
    }
    val pieData = PieData(dataset)

    AndroidView(modifier=Modifier.height(height = 350.dp).fillMaxWidth(),
        factory = {context->
            PieChart(context).apply {
                data = pieData
                description.isEnabled = false
                isRotationEnabled = true
                centerText = pieCenterText
                setCenterTextSize(pieCenterTextSize.toFloat())
                setUsePercentValues(usePercentage)
                setEntryLabelColor(entryLabelFontColor.toArgb())
                setEntryLabelTextSize(entryLabelFontSize.toFloat())
                legend.isEnabled = true
                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }
        }
    )
}

package com.kwdevs.hospitalsdashboard.views.pages.user.userControl.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.bodies.control.ConditionBody
import com.kwdevs.hospitalsdashboard.views.assets.ADD_NEW_CONDITION_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.CLOSE_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GRAY
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PREVIEW_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SAVE_CHANGES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHERE_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHERE_IN_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHERE_OR_EN_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs
import com.kwdevs.hospitalsdashboard.views.rcsT

@Composable
fun ConditionsDialog(showDialog: MutableState<Boolean>,
                     columns: List<String>,
                     result: MutableState<List<ConditionBody>>
){
    val operators = listOf(
        Pair("="  , "Equal To"),
        Pair("!=" , "Not Equal To"),
        Pair(">"  , "Greater Than"),
        Pair("<"  , "Less Than"),
        Pair(">=" , "Greater than or Equal To"),
        Pair("<=" , "Less than or Equal To")
    )
    val mainWheres= listOf(WHERE_EN_LABEL, WHERE_OR_EN_LABEL, WHERE_IN_EN_LABEL)
    val selectedColumn = remember { mutableStateOf("") }
    val selectedOperator = remember { mutableStateOf<Pair<String,String>?>(null) }
    val selectedCondition = remember { mutableStateOf("") }
    val condVal = remember { mutableStateOf("") }

    val operatorNeeded=selectedCondition.value in listOf(WHERE_EN_LABEL, WHERE_OR_EN_LABEL) && selectedOperator.value!=null
    val noOperatorNeeded = selectedCondition.value== WHERE_OR_EN_LABEL && selectedOperator.value==null

    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier= Modifier.fillMaxWidth()
                    .background(color= BLUE, shape = rcsT(20)),
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier= Modifier.padding(vertical = 5.dp)){ Label(ADD_NEW_CONDITION_LABEL, color = WHITE) }
                }
                if(result.value.isNotEmpty()){
                    ConditionsPreview(result)
                }
                Label("${selectedCondition.value} ${selectedColumn.value} ${selectedOperator.value?.second?:""} ${condVal.value}")
                Box(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(hasTitle = false, loadedItems = mainWheres,
                        selectedItem = selectedCondition,
                        selectedContent = {
                            CustomInput(value=selectedCondition.value,
                                label="Condition", readOnly = true)
                        }) { Label(it) }
                }
                Box(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                    ComboBox(hasTitle = false, loadedItems = columns, selectedItem = selectedColumn,
                        selectedContent = {
                            CustomInput(readOnly = true,value=selectedColumn.value,
                                label = "Column",)
                        }) { Label(it) }
                }
                if(selectedCondition.value != WHERE_IN_EN_LABEL){
                    Box(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                        ComboBox(hasTitle = false, loadedItems = operators,
                            selectedItem = selectedOperator,
                            selectedContent = {
                                CustomInput(readOnly = true,value=selectedOperator.value?.second?:"",label="Operator")
                            })
                        { Label(it?.second?:"Select Operator") }
                    }
                    if(selectedCondition.value in listOf(WHERE_EN_LABEL, WHERE_OR_EN_LABEL)){
                        Label(text = "required ", color = Color.Red)
                    }
                }
                else selectedOperator.value=null
                Box(modifier= Modifier.fillMaxWidth().padding(5.dp)){
                    CustomInput(value=condVal,label="Value")
                }
                if(selectedCondition.value== WHERE_IN_EN_LABEL){
                    Row(modifier= Modifier.fillMaxWidth()){
                        Label(text = "Separate between values with ',' for example 1,2,3 ", maximumLines = Int.MAX_VALUE)

                    }
                }
                VerticalSpacer()
                Row(modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    CustomButton(label = SAVE_CHANGES_LABEL,
                        enabled = (operatorNeeded || noOperatorNeeded) && condVal.value!="",
                        buttonShape = rcs(5),) {
                        if(selectedCondition.value!="" && selectedColumn.value!="" && condVal.value!="" ){
                            if(operatorNeeded || noOperatorNeeded && condVal.value!=""){
                                val b= ConditionBody(
                                    clause = selectedCondition.value,
                                    columnName = selectedColumn.value,
                                    operator = selectedOperator.value?.first,
                                    value = condVal.value
                                )
                                val newConditions= mutableListOf(b)
                                newConditions.addAll(result.value.filter { it!=b })
                                result.value=newConditions
                                selectedCondition.value=""
                                selectedOperator.value=null
                                selectedColumn.value=""
                                condVal.value=""
                            }

                        }

                    }
                    CustomButton(label= CLOSE_LABEL,
                        buttonShape = rcs(5),
                        enabledBackgroundColor = Color.Red,
                        enabledFontColor = WHITE
                    ) {
                        showDialog.value=false
                    }
                }
            }
        }
    }
}

@Composable
private fun ConditionItemCard(
    item:ConditionBody,
    result:MutableState<List<ConditionBody>>){
    Row(modifier=Modifier.border(width = 1.dp,
        shape = rcs(5), color = GRAY
    )
        .padding(5.dp),verticalAlignment = Alignment.CenterVertically){
        Label(item.columnName)
        Label(item.operator?:"")
        Label(item.value)
        IconButton(R.drawable.ic_delete_red) {
            val newMainList= mutableListOf<ConditionBody>()
            val filtered=result.value.filter { it.clause==item.clause && it.value==item.value && it.operator==item.operator && it.columnName==item.columnName }
            newMainList.addAll(filtered)
            result.value=filtered
        }
    }}

@Composable
private fun ConditionsPreview(result:MutableState<List<ConditionBody>>){
    val new = result.value.groupBy { it.clause }
    ColumnContainer {
        Label(PREVIEW_LABEL)
        Column(modifier=Modifier.padding(5.dp)){
            new.forEach { (key, values) ->
                Label(key)
                ColumnContainer { LazyRow{items(values){ body-> ConditionItemCard(body,result) }} }
            }
        }
    }
}



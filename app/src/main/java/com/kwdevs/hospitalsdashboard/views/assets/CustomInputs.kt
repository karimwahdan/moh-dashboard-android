package com.kwdevs.hospitalsdashboard.views.assets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun CustomInput(
    value: MutableState<String>,
    label: String,
    placeholder: String=label,
    keyboardOptions: KeyboardOptions,
    icon: Int?=null,
    isPassword:Boolean=false,
    readOnly:Boolean=false
) {
    var hidePassword by remember { mutableStateOf(false) }
    VerticalSpacer(height = 16)
    OutlinedTextField(
        keyboardOptions = keyboardOptions,
        value = value.value,
        visualTransformation = if(isPassword) {
            if(hidePassword) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        onValueChange = { value.value = it },
        readOnly = readOnly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        leadingIcon = {
            if (icon != null) {
                Image(
                    painter = painterResource(
                        id = icon
                    ),
                    contentDescription = null
                )
            }

        },
        trailingIcon = {if(isPassword)
            IconButton(icon=if(hidePassword) R.drawable.ic_visibility_off_dark_blue  else R.drawable.ic_eye_blue ){hidePassword=!hidePassword}
        }
    )
}

@Composable
fun CustomInput(value: MutableState<String>,
                label:String,
                placeHolder:String=label,
                hasSuffix:Boolean=false,
                suffix:String?=EMPTY_STRING,
                hasPrefix:Boolean=false,
                prefix:String?=EMPTY_STRING,
                hasLeadingIcon:Boolean=false,
                containerSize:Int=26,
                @DrawableRes leadingIcon: Int?=null,
                shape: Shape =rcs(5)){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value.value, onValueChange = {value.value=it},
        label = { Label(label) },
        placeholder = { Label(placeHolder) },
        leadingIcon = { if(hasLeadingIcon){leadingIcon?.let{Icon(icon=leadingIcon, containerSize = containerSize)}} },
        suffix = {if(hasSuffix){Label(suffix?:EMPTY_STRING)} },
        prefix = {if(hasPrefix){Label(prefix?:EMPTY_STRING)} },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value: MutableState<String>,
                label:String,
                placeHolder:String=label,
                hasSuffix:Boolean=false,
                suffix:String?=EMPTY_STRING,
                hasPrefix:Boolean=false,
                prefix:String?=EMPTY_STRING,
                shape: Shape =rcs(5)){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value.value, onValueChange = {value.value=it},
        label = { Label(label) },
        placeholder = { Label(placeHolder) },
        suffix = {if(hasSuffix){Label(suffix?:EMPTY_STRING)} },
        prefix = {if(hasPrefix){Label(prefix?:EMPTY_STRING)} },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value: MutableState<String>,
                label:String,
                placeHolder:String=label,
                shape: Shape =rcs(5)){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value.value, onValueChange = {value.value=it},
        label = { Label(label, maximumLines = 3, textOverflow = TextOverflow.Ellipsis, softWrap = true) },
        placeholder = { Label(text=placeHolder, fontSize = 14,
            maximumLines = 2) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}
@Composable
fun CustomInput(value: MutableState<String>,
                label:String,
                enabled:Boolean=true,
                placeHolder:String=label,
                shape: Shape =rcs(5),
                keyboardOptions: KeyboardOptions,
                onTextChange:(String)->Unit={}){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value.value, onValueChange = onTextChange,
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        label = { Label(label) },
        placeholder = { Label(text=placeHolder, fontSize = 14,
            maximumLines = 2) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value: MutableState<String>,
                label:String,
                placeHolder:String=label,
                hasWhiteSpaces:Boolean=true,
                replacedWith:String= EMPTY_STRING,
                shape: Shape =rcs(5)){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value.value, onValueChange = {if(hasWhiteSpaces) value.value=it else value.value=it.trim().replace("\\s+".toRegex(), replacedWith)},
        label = { Label(label) },
        placeholder = { Label(placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value:String, readOnly:Boolean=false,
                shape: Shape =rcs(5), label: String= EMPTY_STRING){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = shape),
        value = value, onValueChange = {},
        readOnly = readOnly,
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value:String,maxLines: Int=2, readOnly:Boolean=false){
    TextField(
        modifier= Modifier.fillMaxWidth(),
        maxLines = maxLines,
        value = value, onValueChange = {},
        readOnly = readOnly,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomInput(value:String, readOnly:Boolean=false,@DrawableRes icon: Int){
    Row(modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically){
        TextField(
            modifier= Modifier.fillMaxWidth().weight(1f),
            value = value, onValueChange = {},
            readOnly = readOnly,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ))
        Icon(icon)
    }

}

@Composable
fun CustomInput(value: MutableState<String>, label: String,
                lines:Int, maxLines:Int=Int.MAX_VALUE,
                readOnly:Boolean=false){
    TextField(
        modifier= Modifier.fillMaxWidth().padding(5.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = rcs(5)),
        value = value.value, onValueChange = {value.value=it},
        maxLines = maxLines,
        minLines = lines,
        label = { Text(label) },
        readOnly = readOnly,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ))
}

@Composable
fun CustomCheckbox(label:String,active: MutableState<Boolean>){
    Row(modifier= Modifier.padding(horizontal = 5.dp),verticalAlignment = Alignment.CenterVertically){
        Label(label)
        Checkbox(checked = active.value, onCheckedChange = { active.value = it })
    }
}

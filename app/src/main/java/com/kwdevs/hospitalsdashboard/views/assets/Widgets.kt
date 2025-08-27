package com.kwdevs.hospitalsdashboard.views.assets

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.app.PatientViewType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.ViewType
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.routes.BabyBirthIndexRoute
import com.kwdevs.hospitalsdashboard.routes.CancerCureIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PatientWardAdmissionsCreateRoute
import com.kwdevs.hospitalsdashboard.routes.PatientsIndexRoute
import com.kwdevs.hospitalsdashboard.routes.PretermAdmissionsIndexRoute
import com.kwdevs.hospitalsdashboard.views.LEFT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.RIGHT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.RIGHT_LAYOUT_DIRECTION
import com.kwdevs.hospitalsdashboard.views.getFormattedDateJavaTime
import com.kwdevs.hospitalsdashboard.views.patientFullName
import com.kwdevs.hospitalsdashboard.views.rcs
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HorizontalSpacer(width:Int=5){Spacer(modifier= Modifier.width(width.dp))}

@Composable
fun VerticalSpacer(height:Int=5){Spacer(modifier= Modifier.height(height.dp))}

@Composable
fun PatientFullName(patient: Patient?){
    Label(patientFullName(patient), textOverflow = TextOverflow.Ellipsis, softWrap = true,
        textAlign = TextAlign.Start,
        maximumLines = 2)
}

@Composable
fun Icon(@DrawableRes icon: Int, background:Color= Color.White,
         size:Int=26,
         containerSize:Int=26,
         contentDescription:String?=null,
         shape: Shape= CircleShape){
    Box(modifier=Modifier.size((containerSize).dp)
        .background(color = background, shape = shape)
        .clip(shape),
        contentAlignment = Alignment.Center){
        Image(modifier=Modifier.size(size.dp),painter= painterResource(icon), contentDescription = contentDescription )
    }
}

fun toast(context:Context,text: String){
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
}

@Composable
fun IconButton(@DrawableRes icon:Int,background: Color=Color.White,
               size: Int=26,
               paddingTop:Int=0,
               paddingBottom:Int=0,
               paddingStart:Int=0,
               paddingEnd:Int=0,
               containerSize: Int=26,onClick:()->Unit){
    Box(modifier=Modifier
        .padding(top=paddingTop.dp, bottom = paddingBottom.dp, start = paddingStart.dp,end=paddingEnd.dp)
        .background(color = background, shape = CircleShape)
        .clip(CircleShape)
        .clickable {
            try{ onClick.invoke() } catch (e:Exception){e.printStackTrace()} },
        contentAlignment = Alignment.Center){
        Icon(icon, size = size,
            containerSize = containerSize,
            background = background)
    }
}

@Composable
fun IconButton(@DrawableRes icon:Int,background: Color=Color.White,
               size: Int=26,
               paddingTop:Int=0,
               paddingBottom:Int=0,
               paddingStart:Int=0,
               paddingEnd:Int=0,
               elevation:Int=5,
               containerSize: Int=26,onClick:()->Unit){
    Box(modifier=Modifier
        .padding(top=paddingTop.dp, bottom = paddingBottom.dp, start = paddingStart.dp,end=paddingEnd.dp)
        .shadow(elevation=elevation.dp, shape = CircleShape)
        .background(color = background, shape = CircleShape)
        .clip(CircleShape)
        .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center){
        Icon(icon, size = size,
            containerSize = containerSize,
            background = background)
    }
}

@Composable
fun Header(text:String,
           textAlign: TextAlign = TextAlign. Center,
           color: Color = Color. Black,
           fontSize: Int = 14,
           hasHeader:Boolean=true,
           shape: Shape= RectangleShape,
           fontWeight: FontWeight = FontWeight. Normal,
           showBackButton:Boolean=false,
           iconButtonBackground:Color=Color.White,
           icon: Int=R.drawable.ic_arrow_back_white,
           headerArrangement:Arrangement.Horizontal=Arrangement.Center,
           onClick: () -> Unit={}){
    if(hasHeader){
        Box(modifier=Modifier.fillMaxWidth().clip(shape),
            contentAlignment = Alignment.CenterEnd){
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement =  Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Label(text=text,textAlign=textAlign,color=color,fontSize=fontSize,fontWeight=fontWeight)
            }
            if(showBackButton){
                IconButton(background = iconButtonBackground,icon=icon, onClick = onClick, paddingStart = 5, paddingEnd = 5)
            }
        }

    }
}

@Composable
fun BoxContainer(hasBorder:Boolean=true,content:@Composable ()->Unit){
    val x=if(hasBorder)Modifier.fillMaxWidth().padding(5.dp)
        .shadow(elevation = 5.dp, shape = rcs(20))
        .background(color=Color.White,shape= rcs(20))
        .border(width=1.dp,color=Color.White,shape= rcs(20))
    else Modifier
    Box(modifier=x,contentAlignment = Alignment.Center){content()}
}

@Composable
fun ColumnContainer(shape: Shape= rcs(20),
                    background: Color= WHITE,
                    borderColor: Color= WHITE,
                    content:@Composable ()->Unit){
    Column(modifier=Modifier.fillMaxWidth().padding(5.dp)
        .shadow(elevation = 5.dp, shape = shape)
        .background(color=background,shape= shape)
        .border(width=1.dp,color=borderColor,shape= shape),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        content()
    }
}

@Composable
fun LabelSpan(value:String,label:String,labelColor:Color= BLACK,
              labelWeight: FontWeight=FontWeight.Bold,
              spanColor: Color= BLUE,maximumLines:Int=1,
              layoutDirection: ProvidedValue<LayoutDirection> = RIGHT_LAYOUT_DIRECTION){
    CompositionLocalProvider(layoutDirection) {
        Row(verticalAlignment = Alignment.CenterVertically){
            HorizontalSpacer()
            if(value.trim()!=""){
                Span(text=value, backgroundColor = spanColor, color = Color.White, maximumLines = maximumLines)
                HorizontalSpacer()
            }
            Label(text=label, color = labelColor, fontWeight = labelWeight , textOverflow = TextOverflow.Ellipsis, softWrap = true)
            HorizontalSpacer()
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWidget(showDialog:MutableState<Boolean>, datePickerState: DatePickerState, timeString:MutableState<String>){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.End){
                    IconButton(icon = R.drawable.ic_cancel_red) {showDialog.value=false }
                }
                LazyColumn {
                    item{
                        Row(modifier=Modifier.fillMaxWidth().padding(5.dp),
                            horizontalArrangement = Arrangement.Center){
                            CustomButton(label = SAVE_CHANGES_LABEL
                                , buttonShape = RectangleShape, enabledBackgroundColor = GREEN,
                            ){
                                timeString.value = getFormattedDateJavaTime(datePickerState)
                                showDialog.value=false
                            }
                        }
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerWidget(
    showDialog: MutableState<Boolean>,
    datePickerState: DatePickerState,
    timeString: MutableState<String>
) {
    val context = LocalContext.current
    val selectedHour = remember { mutableStateOf(LocalTime.now().hour) }
    val selectedMinute = remember { mutableStateOf(LocalTime.now().minute) }
    val showTimePicker = remember { mutableStateOf(false) }

    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ColumnContainer {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(icon = R.drawable.ic_cancel_red) {
                        showDialog.value = false
                        showTimePicker.value=false
                    }
                }

                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                label = "Pick Time",
                                buttonShape = RectangleShape,
                                enabledBackgroundColor = Color.Gray
                            ) {
                                showTimePicker.value = true
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomButton(
                                label = SAVE_CHANGES_LABEL,
                                buttonShape = RectangleShape,
                                enabledBackgroundColor = GREEN
                            ) {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val date = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()

                                    val time = LocalTime.of(
                                        selectedHour.value,
                                        selectedMinute.value
                                    )

                                    val dateTime = LocalDateTime.of(date, time)
                                    timeString.value = dateTime.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                    )
                                } else {
                                    timeString.value = "No date selected"
                                }
                                showDialog.value = false
                            }
                        }

                        DatePicker(state = datePickerState)
                    }
                }
            }
        }
    }

    if (showTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                selectedHour.value = hour
                selectedMinute.value = minute
                showTimePicker.value = false
            },
            selectedHour.value,
            selectedMinute.value,
            true
        ).show()
    }
}

@Composable
fun Toast(context: Context,text:String){
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
}

@Composable
fun CustomDialog(showDialog: MutableState<Boolean>, content:@Composable () -> Unit){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false}) {
            ColumnContainer {
                Row(modifier=Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.End){
                    IconButton(R.drawable.ic_cancel_red) { showDialog.value=false}
                }
                content()
            }
        }
    }
}

@Composable
fun PatientCreatedDialog(showDialog: MutableState<Boolean>, navHostController: NavHostController){
    if(showDialog.value){
        Dialog(onDismissRequest = {showDialog.value=false},
            properties = DialogProperties(dismissOnBackPress = false,
                dismissOnClickOutside = false)
        ) {
            ColumnContainer {
                VerticalSpacer()
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Label(DATA_SAVED_LABEL, color = GREEN, fontSize = 16, maximumLines = 2)
                }
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Label(NEXT_DESTINATION_LABEL, color = BLACK, maximumLines = 2)
                }
                Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp,vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Box(modifier=Modifier.padding(horizontal = 10.dp)){
                        CustomButtonWithImage(
                            icon=R.drawable.ic_patient,
                            label = PATIENTS_LABEL,
                            maxWidth = 70) {
                            Preferences.ViewTypes().setPatientViewType(PatientViewType.BY_HOSPITAL)
                            navHostController.navigate(PatientsIndexRoute.route)
                            showDialog.value=false
                        }
                    }
                    Box(modifier=Modifier.padding(horizontal = 10.dp)){
                        CustomButtonWithImage(
                            icon=R.drawable.ic_premature_baby,
                            label = PRETERM_LABEL,
                            maxWidth = 70) {
                            navHostController.navigate(PretermAdmissionsIndexRoute.route)
                            showDialog.value=false
                        }
                    }
                    Box(modifier=Modifier.padding(horizontal = 10.dp)){
                        CustomButtonWithImage(
                            icon=R.drawable.ic_baby,
                            label = BIRTHS_LABEL,
                            maxWidth = 70) {
                            navHostController.navigate(BabyBirthIndexRoute.route)
                            showDialog.value=false
                        }
                    }
                }
                Row(modifier= Modifier.fillMaxWidth().padding(horizontal = 5.dp,vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Box(modifier=Modifier.padding(horizontal = 10.dp)){
                        CustomButtonWithImage(icon=R.drawable.ic_medical_ward,
                            label = NEW_ADMISSION_LABEL,
                            maxWidth = 70) {
                            navHostController.navigate(PatientWardAdmissionsCreateRoute.route)
                            showDialog.value=false
                        }
                    }
                    Box(modifier=Modifier.padding(horizontal = 10.dp)){
                        CustomButtonWithImage(icon=R.drawable.ic_cancer,
                            label = TUMORS_LABEL,
                            maxWidth = 70) {
                            Preferences.Patients().delete()
                            Preferences.ViewTypes().set(ViewType.BY_HOSPITAL)
                            navHostController.navigate(CancerCureIndexRoute.route)
                            showDialog.value=false
                        }
                    }
                }
            }

        }

    }
}


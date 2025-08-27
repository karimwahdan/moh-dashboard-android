package com.kwdevs.hospitalsdashboard.views.assets.container

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.ColumnDefinition
import com.kwdevs.hospitalsdashboard.views.assets.AdvancedDataTable
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun Corridor(
    modifier: Modifier=Modifier,
    height:Int=80,
    background:Color=Color.White){
    Box(modifier=modifier.fillMaxWidth()
        .height(height.dp)){
        Box(modifier=Modifier.fillMaxSize()
            .background(color=background)){
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(R.drawable.img_corridor),contentDescription = null)
        }
    }
}
@Composable
fun Room(
    modifier: Modifier=Modifier,
    height:Int=80,
    background:Color=Color.White,
    wallColor:Color=Color.DarkGray,
    content:@Composable ()->Unit={}){
    Box(modifier=modifier.fillMaxWidth().height(height.dp).border(width = 3.dp, color = wallColor)){
        Box(modifier=Modifier.fillMaxSize()
            .background(color=background),
            contentAlignment = Alignment.Center){
            content()
        }
    }
}
data class Person(val id: String, val name: String, val email: String)

@Composable
fun Test(){
    val list= listOf(
        Person("1", "Ali", "ali@mail.com"),
        Person("2", "Nada", "nada@mail.com"),
        Person("3", "Hassan", "hassanhassanhassan@mail.com"),

        Person("4", "Ali", "ali@mail.com"),
        Person("5", "Nada", "nada@mail.com"),
        Person("6", "Hassan", "hassan@mail.com"),

        Person("7", "Ali", "ali@mail.com"),
        Person("8", "Nada", "nada@mail.com"),
        Person("9", "Hassan", "hassan@mail.com"),

        Person("10", "Ali", "ali@mail.com"),
        Person("11", "Nada", "nada@mail.com"),
        Person("12", "Hassan", "hassan@mail.com"),

        Person("13", "Ali", "ali@mail.com"),
        Person("14", "Nada", "nada@mail.com"),
        Person("15", "Hassan", "hassan@mail.com"),

        Person("16", "Ali", "ali@mail.com"),
        Person("17", "Nada", "nada@mail.com"),
        Person("18", "Hassan", "hassan@mail.com"),
        Person("19", "Ali", "ali@mail.com"),
        Person("20", "Nada", "nada@mail.com"),
        Person("21", "Hassan", "hassan@mail.com"),

    )
    var people by  remember { mutableStateOf(list) }
    AdvancedDataTable(
        items = people,
        rowKey = { it.id },
        columns = listOf(
            ColumnDefinition(
                key = "id",
                header = "Id",
                cellText = { it.id },
                weight = 0.5f,
                editable = false,
                required = true,
                onSave = { id, fields ->
                    people = people.map {
                        if (it.id == id) it.copy(name = fields["name"] ?: it.name) else it
                    }
                }
            ),
            ColumnDefinition(
                key = "name",
                header = "Name",
                cellText = { it.name },
                weight = 2f,
                editable = true,
                required = true,
                onSave = { id, fields ->
                    people = people.map {
                        if (it.id == id) it.copy(name = fields["name"] ?: it.name) else it
                    }
                }
            ),
            ColumnDefinition(
                key = "email",
                header = "Email",
                cellText = { it.email },
                editable = false,
                required = true,
                onSave = { id, fields ->
                    people = people.map {
                        if (it.id == id) it.copy(email = fields["email"] ?: it.email) else it
                    }
                }
            )
        )
    )
}
@Composable
fun HospitalGameScreen(){

    val height=80
    Column(modifier=Modifier.fillMaxSize()){
        Box(modifier=Modifier.fillMaxWidth()
            .height(height.dp)
            .border(width = 3.dp, color = Color.DarkGray),
            contentAlignment = Alignment.Center){
            Box(modifier=Modifier.fillMaxSize()
                .background(color=Color.LightGray),
                contentAlignment = Alignment.Center){
                Text("Admission")

            }
        }

        Row(){
            Room(modifier=Modifier.weight(2f),
                height = 110,
                background = Color.White){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(
                        R.drawable.img_clinic_top_view_door_right),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth)
                Column(modifier=Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    //Text("Clinics")
                    Row(modifier=Modifier
                        .padding(
                            start=0.dp,
                            end=0.dp,
                            top=0.dp,
                            bottom =0.dp )
                        .background(color= colorResource(R.color.blue3),
                            shape = rcs(20)),
                        horizontalArrangement = Arrangement.Center){
                        HorizontalSpacer(2)
                        Text(
                            modifier=Modifier.padding(horizontal = 5.dp),
                            text="Clinic",textAlign= TextAlign.Center,
                            color= colorResource(R.color.white),
                            fontSize=16.sp,
                            fontWeight= FontWeight.Bold,
                            maxLines = 2)
                        HorizontalSpacer(2)
                    }

                }

            }

            Corridor(modifier=Modifier.weight(1f))
            Room(modifier=Modifier.weight(2f),
                height = 110,
                background = Color.White){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(
                        R.drawable.img_lab_door_left),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth)
                Column(modifier=Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Row(modifier=Modifier
                        .padding(
                            start=0.dp,
                            end=0.dp,
                            top=0.dp,
                            bottom =0.dp )
                        .background(color= colorResource(R.color.orange),
                            shape = rcs(20)),
                        horizontalArrangement = Arrangement.Center){
                        HorizontalSpacer(2)
                        Text(
                            modifier=Modifier.padding(horizontal = 5.dp),
                            text="Laboratory",textAlign= TextAlign.Center,
                            color= colorResource(R.color.white),
                            fontSize=16.sp,
                            fontWeight= FontWeight.Bold,
                            maxLines = 2)
                        HorizontalSpacer(2)
                    }

                }

            }
        }
        Row(){
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("Laboratory")
                        VerticalSpacer(40)
                    }
                }


            }

            Box(modifier=Modifier.fillMaxWidth().weight(1f)){


            }
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("X-Ray")
                        VerticalSpacer(40)
                    }
                }


            }

        }
        Corridor()
        Row(){
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("Preterms")
                        VerticalSpacer(40)
                    }
                }


            }

            Box(modifier=Modifier.fillMaxWidth().weight(1f)){


            }
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("Births")
                        VerticalSpacer(40)
                    }
                }


            }

        }
        Row(){
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("Preterms")
                        VerticalSpacer(40)
                    }
                }


            }

            Box(modifier=Modifier.fillMaxWidth().weight(1f)){


            }
            Box(modifier=Modifier.fillMaxWidth().weight(2f)){
                Box(modifier=Modifier.fillMaxWidth()
                    .border(width = 3.dp, color = Color.DarkGray)){
                    Column(modifier=Modifier
                        .background(color=Color.Gray)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        VerticalSpacer(40)
                        Text("Births")
                        VerticalSpacer(40)
                    }
                }


            }

        }

    }
}

@Preview
@Composable
private fun Preview(){
    Test()
}
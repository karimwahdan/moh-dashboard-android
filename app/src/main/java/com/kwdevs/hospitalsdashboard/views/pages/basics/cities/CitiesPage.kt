package com.kwdevs.hospitalsdashboard.views.pages.basics.cities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.controller.CityController
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCountResponse
import com.kwdevs.hospitalsdashboard.routes.CityViewRoute
import com.kwdevs.hospitalsdashboard.routes.HomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.basicSceens.LoadingScreen
import com.kwdevs.hospitalsdashboard.views.assets.container.Container
import com.kwdevs.hospitalsdashboard.views.cards.CityCard

const val space=15

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesPage(navHostController: NavHostController){
    val controller : CityController = viewModel()
    val state by controller.state.observeAsState()
    var cities by remember { mutableStateOf<List<CityWithCount>>(emptyList()) }

    var loading by remember { mutableStateOf(true) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }
    when(state){
        is UiState.Loading->{
            loading=true;fail=false;success=false
        }
        is UiState.Error->{loading=false;fail=true;success=false}
        is UiState.Success->{
            LaunchedEffect(Unit) {
                loading=false;fail=false;success=true
                val s = state as UiState.Success<CityWithCountResponse>
                val response = s.data
                val data = response.data
                cities=data
            }

        }
        else->{ LaunchedEffect(Unit){controller.index()}}
    }
    Container(
        title = DIRECTORATES_LABEL,
        showSheet = showSheet,
        headerShowBackButton = true,
        headerIconButtonBackground = BLUE,
        headerOnClick = {navHostController.navigate(HomeRoute.route)}
    ) {
        if(loading) LoadingScreen(modifier=Modifier.fillMaxSize())
        if(success){
            if(cities.isNotEmpty()){
                Column(modifier=Modifier.fillMaxSize()){
                    LazyColumn(modifier=Modifier.fillMaxSize().weight(1f)){
                        item{
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it-> if(index<3){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }
                                }
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 3..5){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 6..8){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 9..11){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 12..14){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }
                                }
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 15..17){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 18..20){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 21..23){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 24..26){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)
                                }}
                            }
                            VerticalSpacer(space)

                            Row(modifier=Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically){
                                cities.forEachIndexed {index,it->if(index in 27..29){
                                    HorizontalSpacer(10)
                                    CityCard(it,Modifier.fillMaxWidth().weight(1f)){
                                        Preferences.Cities().set(City(id=it.id, name = it.name))
                                        navHostController.navigate(CityViewRoute.route)
                                    }
                                    HorizontalSpacer(10)

                                }}
                            }
                            VerticalSpacer(space)

                        }
                    }
                }
            }
        }
    }

}

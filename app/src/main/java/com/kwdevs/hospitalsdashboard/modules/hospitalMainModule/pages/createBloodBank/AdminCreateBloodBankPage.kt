package com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.pages.createBloodBank

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwdevs.hospitalsdashboard.app.CrudType
import com.kwdevs.hospitalsdashboard.app.Preferences
import com.kwdevs.hospitalsdashboard.app.retrofit.UiState
import com.kwdevs.hospitalsdashboard.models.settings.BasicModel
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.bodies.BloodBankBody
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.responses.BloodBankSingleResponse
import com.kwdevs.hospitalsdashboard.modules.hospitalMainModule.controller.HospitalMainModuleController
import com.kwdevs.hospitalsdashboard.routes.AdminHomeRoute
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.ComboBox
import com.kwdevs.hospitalsdashboard.views.assets.CustomButton
import com.kwdevs.hospitalsdashboard.views.assets.CustomCheckbox
import com.kwdevs.hospitalsdashboard.views.assets.CustomInput
import com.kwdevs.hospitalsdashboard.views.assets.GREEN
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.VerticalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.assets.container.Container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateBloodBankPage(navHostController: NavHostController){
    val controller:HospitalMainModuleController= viewModel()
    val state by controller.bloodBankSingleState.observeAsState()
    val saved = Preferences.BloodBanks().get()
    var item by remember { mutableStateOf<BloodBank?>(null) }
    val crudType=Preferences.CrudTypes().get()
    val superUser=Preferences.User().getSuper()
    val hospital=Preferences.Hospitals().get()
    val types= listOf(BasicModel(1,"تخزينى"),BasicModel(2,"تجميعى"))
    val selectedType = remember { mutableStateOf<BasicModel?>(null) }
    val name = remember { mutableStateOf("") }
    val isNbts = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var fail by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    when(state){
        is UiState.Loading->{}
        is UiState.Error->{
            LaunchedEffect(Unit) {
                success=false;fail=true
                val s =state as UiState.Error
                val exception=s.exception
                errorMessage=exception.message?:""
                showSheet.value=true
            }

        }
        is UiState.Success->{
            LaunchedEffect(Unit) {
                val s = state as UiState.Success<BloodBankSingleResponse>
                val r = s.data
                val data=r.data
                item=data
                success=data!=null
                fail=data==null
                showSheet.value=true
            }



        }
        else->{}
    }
    LaunchedEffect(Unit) {
        if(saved!=null && crudType==CrudType.UPDATE){
            name.value=hospital?.bloodBank?.name?:"N/A"
            selectedType.value=hospital?.bloodBank?.type
            isNbts.value=saved.isNationalBloodBank?:false
        }
    }
    Container(
        title = if(crudType==CrudType.CREATE)"Add Blood Bank to ${hospital?.name}"
        else "Update Blood Bank of ${hospital?.name}",
        headerIconButtonBackground = BLUE,
        headerShowBackButton = true,
        headerOnClick = { navHostController.navigate(AdminHomeRoute.route) },
        showSheet = showSheet,
        sheetColor = if(success) GREEN else Color.Red,
        sheetOnClick = {success=false;fail=false;showSheet.value=false},
        sheetContent = {
            if(success) Label("Data Saved Successfully", color = WHITE)
            if(fail) Label(errorMessage, color = WHITE)
        }
    ) {
        ColumnContainer {
            VerticalSpacer()
            Box(modifier=Modifier.fillMaxWidth().padding(5.dp)){
                ComboBox(
                    selectedItem = selectedType,
                    loadedItems = types,
                    selectedContent = { CustomInput(selectedType.value?.name?:"Select Type")}
                ) {
                    Label(it?.name?:"")
                }
            }
            CustomInput(name,"Name")
            CustomCheckbox("Is N.B.T.S.",isNbts)
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                CustomButton(
                    modifier=Modifier,
                    label="Save Changes",
                    buttonShape = RectangleShape,
                    buttonShadowElevation = 6,
                    enabledBackgroundColor = BLUE) {
                    if(name.value!="" && selectedType.value!=null && hospital!=null && superUser!=null){

                        val body = BloodBankBody(
                            id=if(crudType==CrudType.UPDATE && saved!=null) saved.id?:0 else null,
                            hospitalId = hospital.id,
                            name=name.value,
                            bloodBankTypeId = selectedType.value?.id,
                            createdBySuperId = superUser.id
                        )
                        if(crudType==CrudType.CREATE){controller.storeBloodBankBySuperAdmin(body)}
                    }
                }
            }
        }
    }


}


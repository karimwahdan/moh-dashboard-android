package com.kwdevs.hospitalsdashboard.views.cards.control

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kwdevs.hospitalsdashboard.R
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.views.assets.BLUE
import com.kwdevs.hospitalsdashboard.views.assets.ColumnContainer
import com.kwdevs.hospitalsdashboard.views.assets.HorizontalSpacer
import com.kwdevs.hospitalsdashboard.views.assets.Icon
import com.kwdevs.hospitalsdashboard.views.assets.IconButton
import com.kwdevs.hospitalsdashboard.views.assets.Label
import com.kwdevs.hospitalsdashboard.views.assets.PERMISSIONS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.WHITE
import com.kwdevs.hospitalsdashboard.views.rcs

@Composable
fun RoleCard(item: Role, onEditClick: () -> Unit={}, onPermissionsClick:()->Unit={}){
    val name=item.name
    val permissions=item.permissions
    var open by remember { mutableStateOf(false) }

    ColumnContainer {
        Row(modifier = Modifier.fillMaxWidth().clip(rcs(20))
            .background(color = BLUE, shape = rcs(20))
            .clickable { if(permissions.isNotEmpty())open=!open },
            horizontalArrangement = if(permissions.isNotEmpty()) Arrangement.SpaceBetween else Arrangement.Start ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Label(name, paddingStart = 5, paddingEnd = 5, color = WHITE)
                HorizontalSpacer()
                IconButton(R.drawable.ic_edit_blue, paddingTop = 5, paddingBottom = 5, onClick = onEditClick)
                HorizontalSpacer()
                IconButton(R.drawable.ic_lock_person_blue, paddingTop = 5, paddingBottom = 5, onClick = onPermissionsClick)

            }
            if(permissions.isNotEmpty()){
                Box(modifier= Modifier.padding(vertical = 5.dp,horizontal = 10.dp), contentAlignment = Alignment.Center){
                    Icon(if (open) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_down_white, containerSize = 26, background = BLUE)
                }

            }
        }
        AnimatedVisibility(visible = open) {
            Column{
                if (permissions.isNotEmpty()){
                    HorizontalDivider()
                    Label(PERMISSIONS_LABEL)
                    Column(modifier= Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center){
                        permissions.forEach { permission-> PermissionCard(permission) }
                    }

                }
            }
        }

    }
}

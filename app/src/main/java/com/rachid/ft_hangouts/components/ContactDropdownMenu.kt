package com.rachid.ft_hangouts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.dataClasses.Contact

@Composable
fun ContactDropdownMenu(
    navController: NavHostController,
    contact: Contact,
    contactToDelete: MutableState<Contact?>,
    openAlertDialog: MutableState<Boolean>,

    ) {
    var isOptionsExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(enabled = true, onClick = {
                isOptionsExpanded.value = !isOptionsExpanded.value
            }),
    ) {
        Icon(
            painter = painterResource(R.drawable.ellipsis_vertical_icon),
            contentDescription = stringResource(R.string.more_options),
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        DropdownMenu(
            expanded = isOptionsExpanded.value,
            onDismissRequest = { isOptionsExpanded.value = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit_contact)) },
                onClick = {
                    navController.navigate("Edit Contact/${contact.id}")
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_contact)) },
                onClick = {
                    // set the contact to delete
                    contactToDelete.value = contact
                    // close the dropdown menu
                    isOptionsExpanded.value = false
                    // show the alert dialog
                    openAlertDialog.value = true
                }
            )
        }
    }
}
package com.rachid.ft_hangouts.screens

import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.components.AddContactFloatingActionButton
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.db.ContactDatabaseHelper
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.types.Contact

@Composable
fun HomeScreen(navController: NavHostController) {

    // get all contacts from the database
    val db = ContactDatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase
    var contacts = db.getAllContacts(dbHelper)

    Scaffold(
        topBar = {
            TopBar(
                title = "Contacts",
            )
        },
        floatingActionButton = {
            AddContactFloatingActionButton(
                navController = navController
            )
        },
    ) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // Display the list of contacts
            if (contacts.isNotEmpty()) {
                // Display the list of contacts
                ContactsList(contacts = contacts, db = db, navController = navController)
            }
            // If there are no contacts, display an empty screen
            else {
                EmptyContactsScreen()
            }
        }
    }
}

@Composable
fun EmptyContactsScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No contacts found",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ContactsList(contacts: MutableList<Contact>, db: ContactDatabaseHelper, navController: NavHostController) {
    val dbHelper = db.readableDatabase
    val openAlertDialog = remember { mutableStateOf(false) }
    var contactToDelete = remember { mutableStateOf<Contact?>(null) }

    when {
        // ...
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = {
                    openAlertDialog.value = false
                    contactToDelete.value = null
                },
                onConfirmation = {
                    openAlertDialog.value = false
                    if (contactToDelete.value != null) {
                        // Delete the contact from the database
                        db.deleteContact(dbHelper, contactToDelete.value?.id.toString())
                    }

                    // delete from the list
                    contacts.remove(contactToDelete.value)

                    // notify the user
                    Toast.makeText(
                        navController.context,
                        "Contact deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                dialogTitle = "Delete Contact",
                dialogText = "Are you sure you want to delete this contact?",
                icon = Icons.Default.Delete
            )
        }
    }

    LazyColumn {
        items(contacts.size) { index ->
            val contact = contacts[index]
            var isOptionsExpanded = remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(enabled = true, onClick = {})
            ) {

                // avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Contact Icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                }

                // contact details
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "${contact.firstName} ${contact.lastName}",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = contact.phoneNumber.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // more options
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(enabled = true, onClick = {
                            isOptionsExpanded.value = !isOptionsExpanded.value
                        }),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ellipsis_vertical),
                        contentDescription = "More Options",
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
                            text = { Text("Delete") },
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
        }
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
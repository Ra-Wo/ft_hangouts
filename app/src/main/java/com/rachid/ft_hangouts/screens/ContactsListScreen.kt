package com.rachid.ft_hangouts.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.components.AddContactFAB
import com.rachid.ft_hangouts.components.ContactItem
import com.rachid.ft_hangouts.components.EmptyScreen
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.db.DatabaseHelper

@Composable
fun ContactsListScreen(navController: NavHostController) {
    // get all contacts from the database
    val db = DatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase
    var contacts = db.getAllContacts(dbHelper)

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.my_contacts),
                endContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_icon),
                        contentDescription = stringResource(id = R.string.settings),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("settings")
                            },
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        },
        floatingActionButton = {
            AddContactFAB(
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
                ContactsList(contacts = contacts, navController = navController)
            }
            // If there are no contacts, display an empty screen
            else {
                EmptyScreen(text = stringResource(R.string.no_contacts_found))
            }
        }
    }
}

@Composable
fun ContactsList(
    contacts: MutableList<Contact>,
    navController: NavHostController
) {
    LazyColumn {
        items(contacts.size) { index ->
            val contact = contacts[index]

            ContactItem(
                navController = navController,
                contact = contact,
            )
        }
    }
}


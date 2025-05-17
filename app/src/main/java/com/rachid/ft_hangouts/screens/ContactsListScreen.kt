package com.rachid.ft_hangouts.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
    val db = DatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase
    var contacts = remember {
        mutableStateOf<List<Contact>>(db.getAllContacts(dbHelper))
    }

    // register the receiver to listen for new messages
    DisposableEffect(Unit) {
        // register the receiver
        val smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val contactId = intent.getStringExtra("contactId")
                contacts.value = db.getAllContacts(dbHelper)
                Log.d("ContactsListScreen01", "New message received for contact: $contactId")
            }
        }

        // register the receiver
        val filter = IntentFilter("com.rachid.ft_hangouts.NEW_MESSAGE")
        ContextCompat.registerReceiver(
            navController.context,
            smsReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        // unregister the receiver when the composable is disposed
        onDispose {
            navController.context.unregisterReceiver(smsReceiver)
        }
    }

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
            if (contacts.value.isNotEmpty()) {
                // Display the list of contacts
                ContactsList(contacts = contacts.value, navController = navController)
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
    contacts: List<Contact>,
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


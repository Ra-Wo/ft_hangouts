package com.rachid.ft_hangouts.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.components.AddContactFAB
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.db.DatabaseHelper
import com.rachid.ft_hangouts.components.ContactItem
import com.rachid.ft_hangouts.components.EmptyScreen
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.dataClasses.Message
import com.rachid.ft_hangouts.R

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
                ContactsList(contacts = contacts, db = db, navController = navController)
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
    db: DatabaseHelper,
    navController: NavHostController
) {
    val dbHelper = db.readableDatabase
    var unreadMessages = remember {
        mutableStateOf<List<Message>>(
            db.getUnreadMessages(dbHelper)
        )
    }

    // register the receiver to listen for new messages
    DisposableEffect(Unit) {
        // register the receiver
        val smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                unreadMessages.value = db.getUnreadMessages(dbHelper)
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

        onDispose {
            navController.context.unregisterReceiver(smsReceiver)
        }
    }

    LazyColumn {
        items(contacts.size) { index ->
            val contact = contacts[index]
            val unreadMessagesForContact = unreadMessages.value.filter { message ->
                message.contactId == contact.id
            }

            ContactItem(
                navController = navController,
                contact = contact,
                unreadMessages = unreadMessagesForContact,
            )
        }
    }
}


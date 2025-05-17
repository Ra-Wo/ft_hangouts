package com.rachid.ft_hangouts.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.components.ContactDropdownMenu
import com.rachid.ft_hangouts.components.CustomAlertDialog
import com.rachid.ft_hangouts.components.EmptyScreen
import com.rachid.ft_hangouts.components.MessageItem
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.db.DatabaseHelper
import com.rachid.ft_hangouts.utils.SmsMessage
import com.rachid.ft_hangouts.utils.SmsType
import com.rachid.ft_hangouts.utils.getConversationWithNumber


@Composable
fun MessagesScreen(navController: NavHostController, contactId: String) {
    // get all contacts from the database
    val db = DatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase

    // get the contact by id
    val contact: Contact? = db.getContactById(dbHelper, contactId)

    // get all messages for the contact
    var messages = remember {
        mutableStateOf<List<SmsMessage>>(
            getConversationWithNumber(navController.context, contact?.phoneNumber ?: "Unknown")
        )
    }

    // new message state
    val newMessage = remember { mutableStateOf("") }

    // handle send message
    fun handleSendMessage() {
        if (newMessage.value.isNotEmpty()) {

            val message = SmsMessage(
                sender = "me",
                body = newMessage.value,
                date = System.currentTimeMillis(),
                type = SmsType.SENT
            )

            // send sms
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                contact?.phoneNumber, // phone number
                null, // scAddress
                newMessage.value, // message body
                null, // sentIntent
                null // deliveryIntent
            )
            newMessage.value = ""

            // add the message to the list
            messages.value = listOf(message) + messages.value
        }
    }

    // make the new messages counter in the contact 0
    db.editContact(
        db = dbHelper,
        contact = contact?.copy(newMessages = 0) ?: Contact(
            firstName = "",
            lastName = "",
            phoneNumber = "",
            email = "",
            address = "",
            newMessages = 0
        )
    )

    // register the receiver to listen for new messages
    DisposableEffect(Unit) {
        // register the receiver
        val smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val contactId = intent.getStringExtra("contactId")
                val message = intent.getStringExtra("message")

                if (contactId != null && message != null && contactId == contact?.id) {
                    // add the message to the list
                    messages.value = listOf(
                        SmsMessage(
                            sender = contact.phoneNumber,
                            body = message,
                            date = System.currentTimeMillis(),
                            type = SmsType.RECEIVED
                        )
                    ) + messages.value
                }
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

    // delete contact functionality
    val openAlertDialog = remember { mutableStateOf(false) }
    var contactToDelete = remember { mutableStateOf<Contact?>(null) }

    when {
        openAlertDialog.value -> {
            CustomAlertDialog(
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

                    navController.navigate("home")

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

    Scaffold(
        topBar = {
            TopBar(
                title = contact?.firstName + " " + contact?.lastName,
                startContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                navController.popBackStack()
                            }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier
                                .size(35.dp)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                endContent = {
                    if (contact != null)
                        ContactDropdownMenu(
                            navController = navController,
                            contact = contact,
                            openAlertDialog = openAlertDialog,
                            contactToDelete = contactToDelete,
                        )
                }
            )
        },
        bottomBar = {
            OutlinedTextField(
                value = newMessage.value,
                onValueChange = { value -> newMessage.value = value },
                placeholder = { Text(stringResource(R.string.type_a_message)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                shape = MaterialTheme.shapes.large,
                trailingIcon = {
                    IconButton(
                        onClick = { handleSendMessage() },
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_icon),
                            contentDescription = stringResource(
                                id = R.string.send
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (messages.value.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    reverseLayout = true,
                ) {
                    items(messages.value.size) { index ->
                        MessageItem(
                            message = messages.value[index]
                        )
                    }
                }
            } else {
                EmptyScreen(text = stringResource(R.string.no_messages_found))
            }
        }
    }
}
package com.rachid.ft_hangouts.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.types.Contact
import java.util.UUID
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.db.DatabaseHelper

@Composable
fun ContactFormScreen(navController: NavHostController, contactId: String? = null) {

    val db = DatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase

    // state for the contact
    val contact = remember {
        mutableStateOf(
            db.getContactById(dbHelper, contactId.toString()) ?: Contact(
                id = UUID.randomUUID().toString(),
                firstName = "",
                lastName = "",
                phoneNumber = "",
                email = "",
                address = ""
            )
        )
    }

    fun handleSaveContact() {
        val dbHelper = DatabaseHelper(navController.context)
        val db = dbHelper.writableDatabase

        if (contactId != null) {
            // Update the contact in the database
            dbHelper.editContact(db, contact.value)
        } else {
            // Insert a new contact into the database
            dbHelper.insertContact(db, contact.value)
        }

        // Show a toast message
        Toast.makeText(
            navController.context,
            "Contact saved!",
            Toast.LENGTH_SHORT
        ).show()

        // Navigate back to the home screen
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = when (contactId) {
                    null -> "Add Contact"
                    else -> "Edit Contact"
                },
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
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(35.dp)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                },
                endContent = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    ) {
                        Button(
                            onClick = { handleSaveContact() },
                            modifier = Modifier
                        ) {
                            Text("Save", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // state for the scrollable content
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Input fields
                OutlinedTextField(
                    value = contact.value.firstName,
                    onValueChange = { value ->
                        contact.value = contact.value.copy(firstName = value)
                    },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                )

                OutlinedTextField(
                    value = contact.value.lastName,
                    onValueChange = { value ->
                        contact.value = contact.value.copy(lastName = value)
                    },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                )

                OutlinedTextField(
                    value = contact.value.phoneNumber,
                    onValueChange = { value ->
                        contact.value = contact.value.copy(phoneNumber = value)
                    },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                )

                OutlinedTextField(
                    value = contact.value.email,
                    onValueChange = { value -> contact.value = contact.value.copy(email = value) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(18.dp),
                )

                OutlinedTextField(
                    value = contact.value.address,
                    onValueChange = { value ->
                        contact.value = contact.value.copy(address = value)
                    },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                )

                Text(
                    text = contact.value.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = contactId.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

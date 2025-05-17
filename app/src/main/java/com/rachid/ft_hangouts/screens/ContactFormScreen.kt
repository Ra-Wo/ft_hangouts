package com.rachid.ft_hangouts.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.components.TopBar
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.db.DatabaseHelper

@Composable
fun ContactFormScreen(navController: NavHostController, contactId: String? = null) {

    val db = DatabaseHelper(context = navController.context)
    val dbHelper = db.readableDatabase

    // state for the contact
    val contact = remember {
        mutableStateOf(
            db.getContactById(dbHelper, contactId.toString()) ?: Contact(
                firstName = "",
                lastName = "",
                phoneNumber = "",
                email = "",
                address = "",
                newMessages = 0
            )
        )
    }

    fun handleSaveContact() {
        val dbHelper = DatabaseHelper(navController.context)
        val db = dbHelper.writableDatabase

        if (
            contact.value.firstName.isEmpty()
            || contact.value.lastName.isEmpty()
            || contact.value.phoneNumber.isEmpty()
        ) {
            Toast.makeText(
                navController.context,
                "Please fill in all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

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

    fun formatText(text: String): String {
        return text.trim()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = when (contactId) {
                    null -> stringResource(id = R.string.add_contact)
                    else -> stringResource(id = R.string.edit_contact)
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
                            contentDescription = stringResource(
                                id = R.string.back
                            ),
                            modifier = Modifier
                                .size(35.dp)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                endContent = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    ) {
                        Button(
                            onClick = { handleSaveContact() },
                        ) {
                            Text(
                                stringResource(id = R.string.save),
                                style = MaterialTheme.typography.labelLarge
                            )
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
                        contact.value = contact.value.copy(firstName = formatText(value))
                    },
                    label = { Text(stringResource(R.string.first_name)) },
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
                    label = { Text(stringResource(R.string.last_name)) },
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
                    label = { Text(stringResource(R.string.phone_number)) },
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
                    label = { Text(stringResource(R.string.Email)) },
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
                    label = { Text(stringResource(R.string.Address)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                )
            }
        }
    }
}

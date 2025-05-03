package com.rachid.ft_hangouts.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.types.Contact
import java.util.UUID

@Composable
fun AddContactScreen(navController: NavHostController) {

    val contact = remember {
        mutableStateOf(
            Contact(
                id = UUID.randomUUID().toString(),
                firstName = "",
                lastName = "",
                phoneNumber = "",
                email = "",
                address = ""
            )
        )
    }

    Scaffold() { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column() {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Input fields
                    OutlinedTextField(
                        value = contact.value.firstName,
                        onValueChange = { contact.value = contact.value.copy(firstName = it) },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = contact.value.lastName,
                        onValueChange = { contact.value = contact.value.copy(lastName = it) },
                        label = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = contact.value.phoneNumber,
                        onValueChange = { contact.value = contact.value.copy(phoneNumber = it) },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = contact.value.email,
                        onValueChange = { contact.value = contact.value.copy(email = it) },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = contact.value.address,
                        onValueChange = { contact.value = contact.value.copy(address = it) },
                        label = { Text("Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    // Save button
                    Button(
                        onClick = {
                            Log.d("AddContact", "Contact saved: ${contact.value}")
                            Toast.makeText(
                                navController.context,
                                "Contact saved!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Save Contact", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

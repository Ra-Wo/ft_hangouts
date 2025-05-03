package com.rachid.ft_hangouts.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R

@Composable
fun AddContactFloatingActionButton(navController: NavHostController) {
    FloatingActionButton(
        onClick = { navController.navigate("Add Contact") },
    ) {
        Icon(
            painter = painterResource(R.drawable.add_contact),
            contentDescription = "Add Contact",
        )
    }
}
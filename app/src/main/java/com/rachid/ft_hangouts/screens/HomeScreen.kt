package com.rachid.ft_hangouts.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.components.AddContactFloatingActionButton
import com.rachid.ft_hangouts.components.TopBar

@Composable
fun HomeScreen(navController: NavHostController) {
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

        }
    }
}

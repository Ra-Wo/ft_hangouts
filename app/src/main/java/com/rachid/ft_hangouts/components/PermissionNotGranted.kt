package com.rachid.ft_hangouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PermissionNotGrantedScreen(
    onRequestPermission: () -> Unit
) {
    onRequestPermission()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Permission not granted, waiting...",
            modifier = Modifier
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}
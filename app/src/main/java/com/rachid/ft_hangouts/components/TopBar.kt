package com.rachid.ft_hangouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rachid.ft_hangouts.R

@Composable
fun TopBar(
    startContent: @Composable (() -> Unit)? = null,
    endContent: @Composable (() -> Unit)? = null,
    title: String = stringResource(R.string.home),
) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) { innerPadding: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column {
                // add space to the top of the screen to avoid the status bar
                Spacer(
                    modifier = Modifier.height(innerPadding.calculateTopPadding())
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxHeight()
                ) {
                    // add the start content
                    if (startContent != null) {
                        startContent()
                    }
                    // add the title
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = if (startContent != null) 8.dp else 16.dp)
                            .weight(1f)
                    )
                    // add the end content
                    if (endContent != null) {
                        endContent()
                    }
                }
            }
        }
    }
}
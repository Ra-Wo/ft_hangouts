package com.rachid.ft_hangouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.dataClasses.Contact

@Composable
fun ContactItem(
    navController: NavHostController, contact: Contact
) {
    Row(
        modifier = Modifier
            .clickable(enabled = true, onClick = {
                navController.navigate("Edit Contact/${contact.id}")
            })
            .padding(16.dp)
    ) {
        // avatar
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                painter = painterResource(R.drawable.user_icon),
                contentDescription = stringResource(R.string.contact_icon),
                modifier = Modifier
                    .size(45.dp)
                    .padding(8.dp)
            )
        }

        // contact details
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = "${
                    contact.firstName.replaceFirstChar { firstChar ->
                        firstChar.uppercase()
                    }
                } ${
                    contact.lastName.replaceFirstChar { firstChar ->
                        firstChar.uppercase()
                    }
                }",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 19.sp,
            )
            Text(
                text = contact.phoneNumber,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(45.dp)
                .clip(CircleShape)
                .background(if (contact.newMessages >= 1) MaterialTheme.colorScheme.primary else Color.Transparent)
                .padding(2.dp)
                .clickable(true, onClick = {
                    navController.navigate("Messages/${contact.id}")
                }),
            contentAlignment = androidx.compose.ui.Alignment.Center

        ) {
            if (contact.newMessages >= 1) {
                Text(
                    text = if (contact.newMessages > 9) "9+" else contact.newMessages.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Icon(
                painter = painterResource(R.drawable.chat_bubble),
                contentDescription = "",
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp),
                tint = if (contact.newMessages >= 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

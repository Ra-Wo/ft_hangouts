package com.rachid.ft_hangouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rachid.ft_hangouts.R
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.dataClasses.Message

@Composable
fun ContactItem(
    navController: NavHostController,
    contact: Contact,
    unreadMessages: List<Message>
) {
    val unreadCount = unreadMessages.size
    val lastMessage = if (unreadCount > 0) {
        unreadMessages[0]
    } else {
        null
    }
    var lastMessageContent = if (lastMessage != null) {
        if (lastMessage.content.length > 80) {
            "${lastMessage.content.substring(0, 80)}..."
        } else {
            lastMessage.content
        }
    } else {
        contact.phoneNumber
    }

    Row(
        modifier = Modifier
            .clickable(enabled = true, onClick = {
                navController.navigate("Messages/${contact.id}")
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
                text = lastMessageContent,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // unread messages count
        if (unreadCount > 0) {
            Text(
                text = unreadCount.toString(),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 6.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp
            )
        }
    }
}

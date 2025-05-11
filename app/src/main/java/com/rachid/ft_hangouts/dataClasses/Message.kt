package com.rachid.ft_hangouts.dataClasses

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val contactId: String?, // Can be null if from unknown number
    val phoneNumber: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOutgoing: Boolean, // true if sent by user, false if received
    val isRead: Boolean = false
) {
    override fun toString(): String {
        return " id='$id'\n" +
                ",contactId=$contactId\n" +
                ",phoneNumber='$phoneNumber'\n" +
                ",content='$content'\n" +
                ",timestamp=$timestamp\n" +
                ",isOutgoing=$isOutgoing\n" +
                ",isRead=$isRead)"
    }
}
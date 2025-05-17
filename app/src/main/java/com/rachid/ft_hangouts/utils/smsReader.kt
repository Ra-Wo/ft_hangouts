package com.rachid.ft_hangouts.utils

import android.content.Context
import androidx.core.net.toUri

data class SmsMessage(
    val sender: String, val body: String, val date: Long, val type: SmsType
) {
    override fun toString(): String {
        return "SmsMessage(sender='$sender', body='$body', date=$date, type=$type)"
    }
}

enum class SmsType {
    RECEIVED, SENT, FAILED, DRAFT, OUTBOX, QUEUED, UNKNOWN
}

fun getConversationWithNumber(context: Context, phoneNumber: String): List<SmsMessage> {
    val allMessages = mutableListOf<SmsMessage>()

    val uri = "content://sms".toUri()
    val projection = arrayOf("address", "body", "date", "type")
    val selection = "address LIKE ?"
    val selectionArgs = arrayOf("%$phoneNumber%")

    val cursor = context.contentResolver.query(
        uri, projection, selection, selectionArgs, "date ASC"
    )

    while (cursor?.moveToNext() == true) {
        val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
        val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
        val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
        val typeCode = cursor.getInt(cursor.getColumnIndexOrThrow("type"))

        val type = when (typeCode) {
            1 -> SmsType.RECEIVED
            2 -> SmsType.SENT
            3 -> SmsType.DRAFT
            4 -> SmsType.OUTBOX
            5 -> SmsType.FAILED
            6 -> SmsType.QUEUED
            else -> SmsType.UNKNOWN
        }

        allMessages.add(
            SmsMessage(
                sender = if (type == SmsType.SENT) "me" else address,
                body = body,
                date = date,
                type = type
            )
        )
    }
    cursor?.close()
    return allMessages.sortedBy {value -> value.date }.reversed()
}

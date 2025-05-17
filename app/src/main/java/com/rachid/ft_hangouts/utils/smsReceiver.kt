package com.rachid.ft_hangouts.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.db.DatabaseHelper

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isEmpty()) return

            val messageBuilder = StringBuilder()
            var sender = ""

            for (message in messages) {
                sender = message.displayOriginatingAddress
                messageBuilder.append(message.messageBody)
            }


            val db = DatabaseHelper(context = context)
            val dbHelper = db.readableDatabase

            // Check if the sender is already in the database
            var contact = db.getContactByPhoneNumber(dbHelper, sender)
            if (contact == null) {
                return
            }

            // increment the unread message count
            contact.newMessages = contact.newMessages + 1
            db.editContact(
                db = dbHelper,
                contact = contact
            )

            // Save the message to the database
            val fullMessage = messageBuilder.toString()

            // send local broadcast to update UI
            val localIntent = Intent("com.rachid.ft_hangouts.NEW_MESSAGE")
            localIntent.putExtra("contactId", contact.id)
            localIntent.putExtra("message", fullMessage)
            context.sendBroadcast(localIntent)
        }
    }
}

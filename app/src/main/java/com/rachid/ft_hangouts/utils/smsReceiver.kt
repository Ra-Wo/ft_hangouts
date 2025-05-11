package com.rachid.ft_hangouts.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.dataClasses.Message
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
                // If the sender is not in the database, add it
                contact = Contact(
                    firstName = sender,
                    lastName = "",
                    phoneNumber = sender,
                    email = "",
                    address = ""
                )
                db.insertContact(
                    db = dbHelper,
                    contact = contact
                )
            }

            // Save the message to the database
            val fullMessage = messageBuilder.toString()
            db.insertMessage(
                db = dbHelper,
                message = Message(
                    contactId = contact.id,
                    phoneNumber = sender,
                    content = fullMessage,
                    isOutgoing = false,
                )
            )

            // send local broadcast to update UI
            val localIntent = Intent("com.rachid.ft_hangouts.NEW_MESSAGE")
            localIntent.putExtra("contactId", contact.id)
            localIntent.putExtra("message", fullMessage)
            context.sendBroadcast(localIntent)
        }
    }
}

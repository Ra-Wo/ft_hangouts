package com.rachid.ft_hangouts.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

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

            val fullMessage = messageBuilder.toString()
            Log.d("SMS11", "Message from $sender: $fullMessage")


        }
    }
}

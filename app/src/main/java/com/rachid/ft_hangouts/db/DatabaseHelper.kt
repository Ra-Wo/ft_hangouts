package com.rachid.ft_hangouts.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rachid.ft_hangouts.dataClasses.Contact
import com.rachid.ft_hangouts.dataClasses.Message

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "ft_hangout.db"

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Contacts table
        const val TABLE_NAME = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"

        // Messages table
        const val TABLE_MESSAGES = "messages"
        const val COLUMN_MESSAGE_ID = "id"
        const val COLUMN_CONTACT_ID = "contact_id"
        const val COLUMN_MESSAGE_PHONE_NUMBER = "phone_number"
        const val COLUMN_MESSAGE_CONTENT = "content"
        const val COLUMN_MESSAGE_TIMESTAMP = "timestamp"
        const val COLUMN_MESSAGE_IS_OUTGOING = "is_outgoing"
        const val COLUMN_MESSAGE_IS_READ = "is_read"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_PHONE_NUMBER TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_ADDRESS TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE $TABLE_MESSAGES (
                $COLUMN_MESSAGE_ID TEXT PRIMARY KEY,
                $COLUMN_CONTACT_ID TEXT,
                $COLUMN_MESSAGE_PHONE_NUMBER TEXT,
                $COLUMN_MESSAGE_CONTENT TEXT,
                $COLUMN_MESSAGE_TIMESTAMP INTEGER,
                $COLUMN_MESSAGE_IS_OUTGOING INTEGER,
                $COLUMN_MESSAGE_IS_READ INTEGER,
                FOREIGN KEY ($COLUMN_CONTACT_ID) REFERENCES $TABLE_NAME($COLUMN_ID)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Contact operations
    fun insertContact(db: SQLiteDatabase, contact: Contact) {
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.id)
            put(COLUMN_FIRST_NAME, contact.firstName)
            put(COLUMN_LAST_NAME, contact.lastName)
            put(COLUMN_PHONE_NUMBER, contact.phoneNumber)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_ADDRESS, contact.address)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun getAllContacts(db: SQLiteDatabase): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            // Get the values from the cursor
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))

            // Create a new Contact object and add it to the list
            contacts.add(
                Contact(
                    id,
                    firstName,
                    lastName,
                    phoneNumber,
                    email,
                    address
                )
            )
        }

        cursor.close()
        return contacts
    }

    fun deleteContact(db: SQLiteDatabase, contactId: String) {
        db.delete(
            TABLE_MESSAGES,
            "$COLUMN_CONTACT_ID = ?",
            arrayOf(contactId)
        )
        db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(contactId)
        )
    }

    fun editContact(db: SQLiteDatabase, contact: Contact) {
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, contact.firstName)
            put(COLUMN_LAST_NAME, contact.lastName)
            put(COLUMN_PHONE_NUMBER, contact.phoneNumber)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_ADDRESS, contact.address)
        }
        db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(contact.id)
        )
    }

    fun getContactById(db: SQLiteDatabase, contactId: String): Contact? {
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(contactId),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))

            Contact(id, firstName, lastName, phoneNumber, email, address)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun getContactByPhoneNumber(db: SQLiteDatabase, phoneNumber: String): Contact? {
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_PHONE_NUMBER = ?",
            arrayOf(phoneNumber),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))

            Contact(id, firstName, lastName, phoneNumber, email, address)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    // Message operations
    fun insertMessage(db: SQLiteDatabase, message: Message) {
        val values = ContentValues().apply {
            put(COLUMN_MESSAGE_ID, message.id)
            put(COLUMN_CONTACT_ID, message.contactId)
            put(COLUMN_MESSAGE_PHONE_NUMBER, message.phoneNumber)
            put(COLUMN_MESSAGE_CONTENT, message.content)
            put(COLUMN_MESSAGE_TIMESTAMP, message.timestamp)
            put(COLUMN_MESSAGE_IS_OUTGOING, if (message.isOutgoing) 1 else 0)
            put(COLUMN_MESSAGE_IS_READ, if (message.isRead) 1 else 0)
        }
        db.insert(TABLE_MESSAGES, null, values)
    }

    fun getMessagesForContactId(db: SQLiteDatabase, contactId: String): MutableList<Message> {
        val messages = mutableListOf<Message>()
        val cursor = db.query(
            TABLE_MESSAGES,
            null,
            "$COLUMN_CONTACT_ID = ?",
            arrayOf(contactId),
            null,
            null,
            "$COLUMN_MESSAGE_TIMESTAMP DESC"
        )

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_ID))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_PHONE_NUMBER))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_CONTENT))
            val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP))
            val isOutgoing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_IS_OUTGOING)) == 1
            val isRead = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_IS_READ)) == 1

            messages.add(
                Message(
                    id,
                    contactId,
                    phoneNumber,
                    content,
                    timestamp,
                    isOutgoing,
                    isRead
                )
            )
        }

        cursor.close()
        return messages
    }

    fun getUnreadMessages(db: SQLiteDatabase): MutableList<Message> {
        val messages = mutableListOf<Message>()
        val cursor = db.query(
            TABLE_MESSAGES,
            null,
            "$COLUMN_MESSAGE_IS_READ = ?",
            arrayOf("0"),
            null,
            null,
            "$COLUMN_MESSAGE_TIMESTAMP DESC"
        )

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_ID))
            val contactId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_ID))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_PHONE_NUMBER))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_CONTENT))
            val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TIMESTAMP))
            val isOutgoing = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_IS_OUTGOING)) == 1
            val isRead = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_IS_READ)) == 1

            messages.add(
                Message(
                    id,
                    contactId,
                    phoneNumber,
                    content,
                    timestamp,
                    isOutgoing,
                    isRead
                )
            )
        }

        cursor.close()
        return messages
    }

    fun markMessagesAsReadForContact(db: SQLiteDatabase, contactId: String) {
        val values = ContentValues().apply {
            put(COLUMN_MESSAGE_IS_READ, 1)
        }
        db.update(
            TABLE_MESSAGES,
            values,
            "$COLUMN_CONTACT_ID = ? AND $COLUMN_MESSAGE_IS_READ = ?",
            arrayOf(contactId, "0")
        )
    }
}

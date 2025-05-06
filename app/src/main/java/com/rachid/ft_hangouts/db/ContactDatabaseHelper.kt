package com.rachid.ft_hangouts.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rachid.ft_hangouts.types.Contact

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "contacts.db"

class ContactDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val TABLE_NAME = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

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
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(contactId)
        )
    }
}

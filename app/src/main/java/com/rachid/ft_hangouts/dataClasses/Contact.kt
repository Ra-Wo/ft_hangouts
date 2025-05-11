package com.rachid.ft_hangouts.dataClasses

import java.util.UUID

data class Contact(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
) {
    override fun toString(): String {
        return "Id: $id\n" +
                "First Name: $firstName\n" +
                "Last Name: $lastName\n" +
                "Phone Number: $phoneNumber\n" +
                "Email: $email\n" +
                "Address: $address"
    }
}
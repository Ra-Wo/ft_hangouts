package com.rachid.ft_hangouts.dataClasses

import java.util.UUID

data class Contact(
    val id: String = UUID.randomUUID().toString(),
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var email: String,
    var address: String,
    var newMessages: Int,
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
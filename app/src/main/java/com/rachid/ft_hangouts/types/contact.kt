package com.rachid.ft_hangouts.types

data class Contact(
    val id: String,
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
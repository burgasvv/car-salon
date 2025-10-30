package org.burgas.carsalon.message

enum class IdentityMessages {

    MATCHED_FLAG("Identity matched flag"),
    IDENTITY_NOT_FOUND("Identity not found"),
    PASSWORD_MATCHED("Password matched");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
package org.burgas.carrental.message

enum class RentMessages {

    RENT_NOT_FOUND("Rent not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
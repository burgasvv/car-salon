package org.burgas.carsalon.message

enum class CarMessages {

    CAR_NOT_FOUND("Car not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
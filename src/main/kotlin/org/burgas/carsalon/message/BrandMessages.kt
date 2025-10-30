package org.burgas.carsalon.message

enum class BrandMessages {

    BRAND_NOT_FOUND("Brand not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
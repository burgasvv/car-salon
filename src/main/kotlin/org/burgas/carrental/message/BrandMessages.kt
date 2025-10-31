package org.burgas.carrental.message

enum class BrandMessages {

    BRAND_NOT_FOUND("Brand not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
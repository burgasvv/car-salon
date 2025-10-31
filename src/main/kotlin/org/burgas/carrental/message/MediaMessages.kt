package org.burgas.carrental.message

enum class MediaMessages {

    MEDIA_NOT_FOUND("Media not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
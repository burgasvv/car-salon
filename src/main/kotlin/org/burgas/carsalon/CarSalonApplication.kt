package org.burgas.carsalon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class CarSalonApplication

fun main(args: Array<String>) {
    runApplication<CarSalonApplication>(*args)
}

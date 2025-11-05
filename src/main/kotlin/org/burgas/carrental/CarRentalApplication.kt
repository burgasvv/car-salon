package org.burgas.carrental

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@EnableScheduling
@SpringBootApplication
class CarSalonApplication

fun main(args: Array<String>) {
    runApplication<CarSalonApplication>(*args)
}

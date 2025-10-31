package org.burgas.carrental.router

import org.burgas.carrental.dto.car.CarRequest
import org.burgas.carrental.service.CarService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.util.UUID

@Configuration
class CarRouter : Router<CarService> {

    final override val service : CarService

    constructor(service: CarService) {
        this.service = service
    }

    @Bean
    fun carRoutes() = router {

        GET("/api/v1/cars") {
            ServerResponse.ok().body(service.findAll())
        }

        GET("/api/v1/cars/by-id") {
            ServerResponse.ok().body(
                service.findById(UUID.fromString(it.param("carId").orElseThrow()))
            )
        }

        POST("/api/v1/cars/create") {
            ServerResponse.ok().body(
                service.create(it.body<CarRequest>())
            )
        }

        PUT("/api/v1/cars/update") {
            ServerResponse.ok().body(
                service.update(it.body<CarRequest>())
            )
        }

        DELETE("/api/v1/cars/delete") {
            ServerResponse.ok().body(
                service.delete(UUID.fromString(it.param("carId").orElseThrow()))
            )
        }

        onError({ true }) { throwable, _ ->
            ServerResponse.badRequest().body(throwable.localizedMessage)
        }
    }
}
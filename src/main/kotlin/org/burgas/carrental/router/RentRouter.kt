package org.burgas.carrental.router

import org.burgas.carrental.dto.rent.RentRequest
import org.burgas.carrental.router.contract.Router
import org.burgas.carrental.service.RentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import java.util.*

@Configuration
class RentRouter : Router<RentService> {

    final override val service: RentService

    constructor(service: RentService) {
        this.service = service
    }

    @Bean
    fun rentRoutes() = router {

        GET("/api/v1/rents/by-identity") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findByIdentityId(
                        UUID.fromString(it.param("identityId").orElseThrow())
                    )
                )
        }

        GET("/api/v1/rents/by-car") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findByCarId(
                        UUID.fromString(it.param("carId").orElseThrow())
                    )
                )
        }

        GET("/api/v1/rents/by-id") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(
                        UUID.fromString(it.param("rentId").orElseThrow())
                    )
                )
        }

        POST("/api/v1/rents/create") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.create(it.body(RentRequest::class.java)))
        }

        PUT("/api/v1/rents/update") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.update(it.body(RentRequest::class.java)))
        }

        DELETE("/api/v1/rents/delete") {
            service.delete(UUID.fromString(it.param("rentId").orElseThrow()))
            ServerResponse.noContent().build()
        }

        onError({true}) {throwable, _ -> ServerResponse.badRequest().body(throwable.localizedMessage) }
    }
}
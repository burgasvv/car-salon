package org.burgas.carrental.router

import org.burgas.carrental.dto.brand.BrandRequest
import org.burgas.carrental.service.BrandService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import java.net.URI
import java.util.*

@Configuration
class BrandRouter : Router<BrandService> {

    final override val service: BrandService

    constructor(service: BrandService) {
        this.service = service
    }

    @Bean
    fun brandRoutes() = router {

        GET("/api/v1/brands") {
            ServerResponse.ok().body(service.findAll())
        }

        GET("/api/v1/brands/by-id") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(
                        UUID.fromString(it.param("brandId").orElseThrow())
                    )
                )
        }

        POST("/api/v1/brands/create") {
            val brandFullResponse = service.create(it.body<BrandRequest>())
            ServerResponse
                .status(HttpStatus.FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/api/v1/brands/by-id?brandId=${brandFullResponse.id}"))
                .body(brandFullResponse)
        }

        PUT("api/v1/brands/update") {
            val brandFullResponse = service.update(it.body<BrandRequest>())
            ServerResponse
                .status(HttpStatus.FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/api/v1/brands/by-id?brandId=${brandFullResponse.id}"))
                .body(brandFullResponse)
        }

        DELETE("/api/v1/brands/delete") {
            service.delete(UUID.fromString(it.param("brandId").orElseThrow()))
            ServerResponse.noContent().build()
        }

        onError({ true }) { throwable, _ ->
            ServerResponse.status(HttpStatus.BAD_REQUEST).body(throwable.localizedMessage)
        }
    }
}
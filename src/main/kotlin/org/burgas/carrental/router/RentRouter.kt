package org.burgas.carrental.router

import org.burgas.carrental.dto.rent.RentRequest
import org.burgas.carrental.entity.identity.IdentityDetails
import org.burgas.carrental.exception.IdentityNotAuthenticatedException
import org.burgas.carrental.exception.IdentityNotAuthorizedException
import org.burgas.carrental.router.contract.Router
import org.burgas.carrental.service.RentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
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

        filter { request, function ->
            if (
                request.path().equals("/api/v1/rents/by-id", false) ||
                request.path().equals("/api/v1/rents/by-identity", false) ||
                request.path().equals("/api/v1/rents/delete", false)
            ) {
                val authentication = request.principal().orElseThrow() as Authentication

                if (authentication.isAuthenticated) {
                    val identityDetails = authentication.principal as IdentityDetails
                    val identityIdParam = request.param("identityId").orElseThrow()
                    val identityId = UUID.fromString(identityIdParam)

                    if (identityDetails.identity.id == identityId) {
                        return@filter function(request)

                    } else {
                        throw IdentityNotAuthorizedException("Identity Not Authorized")
                    }

                } else {
                    throw IdentityNotAuthenticatedException("Identity not authenticated")
                }

            } else if (
                request.path().equals("/api/v1/rents/create", false) ||
                request.path().equals("/api/v1/rents/update", false)
            ) {
                val authentication = request.principal().orElseThrow() as Authentication

                if (authentication.isAuthenticated) {
                    val identityDetails = authentication.principal as IdentityDetails
                    val rentRequest = request.body<RentRequest>()
                    val identityId = rentRequest.identityId ?: throw IdentityNotAuthorizedException("Identity Not Authorized")

                    if (identityDetails.identity.id == identityId) {
                        request.attributes()["rentRequest"] = rentRequest
                        return@filter function(request)

                    } else {
                        throw IdentityNotAuthorizedException("Identity Not Authorized")
                    }

                } else {
                    throw IdentityNotAuthenticatedException("Identity not authenticated")
                }
            }
            return@filter function(request)
        }

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
                .body(service.create(it.attribute("rentRequest").orElseThrow() as RentRequest))
        }

        PUT("/api/v1/rents/update") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.update(it.attribute("rentRequest").orElseThrow() as RentRequest))
        }

        DELETE("/api/v1/rents/delete") {
            service.delete(UUID.fromString(it.param("rentId").orElseThrow()))
            ServerResponse.noContent().build()
        }

        onError({true}) {throwable, _ -> ServerResponse.badRequest().body(throwable.localizedMessage) }
    }
}
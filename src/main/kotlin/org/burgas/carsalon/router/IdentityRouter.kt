package org.burgas.carsalon.router

import jakarta.servlet.http.Part
import org.burgas.carsalon.dto.identity.IdentityRequest
import org.burgas.carsalon.entity.identity.Identity
import org.burgas.carsalon.exception.IdentityNotAuthenticatedException
import org.burgas.carsalon.exception.IdentityNotAuthorizedException
import org.burgas.carsalon.service.IdentityService
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
class IdentityRouter : Router<IdentityService> {

    final override val service: IdentityService

    constructor(service: IdentityService) {
        this.service = service
    }

    @Bean
    fun identityRoutes() = router {

        filter { request, function ->
            if (
                request.path().equals("/api/v1/identities/by-id", false) ||
                request.path().equals("/api/v1/identities/delete", false) ||
                request.path().equals("/api/v1/identities/change-password", false) ||
                request.path().equals("/api/v1/identities/add-images", false) ||
                request.path().equals("/api/v1/identities/remove-images", false)
            ) {
                val authentication = request.principal().orElseThrow() as Authentication
                if (authentication.isAuthenticated) {
                    val identity = authentication.principal as Identity
                    val identityIdParam = request.param("identityId").orElseThrow()
                    val identityId = UUID.fromString(identityIdParam)

                    if (identity.id == identityId) {
                        return@filter function(request)

                    } else {
                        throw IdentityNotAuthorizedException("Identity Not Authorized")
                    }

                } else {
                    throw IdentityNotAuthenticatedException("User is not authenticated")
                }

            } else if (
                request.path().equals("/api/v1/identities/update", false)
            ) {
                val authentication = request.principal().orElseThrow() as Authentication

                if (authentication.isAuthenticated) {
                    val identity = authentication.principal as Identity
                    val identityRequest = request.body<IdentityRequest>()
                    val identityId = identityRequest.id ?: throw IdentityNotAuthorizedException("Identity Not Authorized")

                    if (identity.id == identityId) {
                        request.attributes()["identityRequest"] = identityRequest
                        return@filter function(request)

                    } else {
                        throw IdentityNotAuthorizedException("Identity Not Authorized")
                    }

                } else {
                    throw IdentityNotAuthenticatedException("User is not authenticated")
                }
            }
            return@filter function(request)
        }

        GET("/api/v1/identities") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll())
        }

        GET("/api/v1/identities/by-id") { request ->
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(
                        UUID.fromString(request.param("identityId").orElseThrow())
                    )
                )
        }

        POST("/api/v1/identities/create") { request ->
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(request.body<IdentityRequest>())
                )
        }

        PUT("/api/v1/identities/update") { request ->
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.update(request.attribute("identityRequest").orElseThrow() as IdentityRequest)
                )
        }

        DELETE("/api/v1/identities/delete") { request ->
            service.delete(UUID.fromString(request.param("identityId").orElseThrow()))
            ServerResponse.noContent().build()
        }

        PUT("/api/v1/identities/change-password") {
            service.changePassword(
                UUID.fromString(it.param("identityId").orElseThrow()),
                it.param("newPassword").orElseThrow()
            )
            ServerResponse.noContent().build()
        }

        POST("/api/v1/identities/add-images") {
            service.addImages(
                UUID.fromString(it.param("identityId").orElseThrow()),
                it.multipartData()["media"] as List<Part>
            )
            ServerResponse.noContent().build()
        }

        DELETE("/api/v1/identities/remove-images") {
            val identityId = UUID.fromString(it.param("identityId").orElseThrow())
            val mediaIds = it.servletRequest().getParameterValues("mediaId")
                .map { string -> UUID.fromString(string) }
            service.removeImages(identityId, mediaIds)
            ServerResponse.noContent().build()
        }

        onError({ true }) { throwable, _ ->
            ServerResponse.status(HttpStatus.BAD_REQUEST).body(throwable.localizedMessage)
        }
    }
}
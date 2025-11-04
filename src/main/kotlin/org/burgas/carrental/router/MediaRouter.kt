package org.burgas.carrental.router

import org.burgas.carrental.router.contract.Router
import org.burgas.carrental.service.MediaService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import java.io.ByteArrayInputStream
import java.util.*

@Configuration
class MediaRouter : Router<MediaService> {

    final override val service: MediaService

    constructor(service: MediaService) {
        this.service = service
    }

    @Bean
    fun mediaRoutes() = router {

        GET("/api/v1/media/by-id") { request ->
            val media = service.findEntity(
                UUID.fromString(request.param("mediaId").orElseThrow())
            )
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(media.contentType))
                .body(
                    InputStreamResource(
                        ByteArrayInputStream(media.data)
                    )
                )
        }

        POST("/api/v1/media/create") { request ->
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(request.multipartData().asSingleValueMap()["media"]
                        ?: throw RuntimeException("Invalid media"))
                )
        }

        PUT("/api/v1/media/change") { request ->
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.change(
                        UUID.fromString(request.param("mediaId").orElseThrow()),
                        request.multipartData().asSingleValueMap()["media"]
                            ?: throw RuntimeException("Invalid media")
                    )
                )
        }

        DELETE("/api/v1/media/delete") { request ->
            service.delete(UUID.fromString(request.param("mediaId").orElseThrow()))
            ServerResponse.noContent().build()
        }

        onError({ true }) { throwable, _ ->
            ServerResponse.status(HttpStatus.BAD_REQUEST).body(throwable.localizedMessage)
        }
    }
}
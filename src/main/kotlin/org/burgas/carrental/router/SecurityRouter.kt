package org.burgas.carrental.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
class SecurityRouter {

    @Bean
    fun securityRoutes() = router {
        GET("/api/v1/security/csrf-token") {
            ServerResponse.ok().body(it.attribute("_csrf"))
        }
    }
}
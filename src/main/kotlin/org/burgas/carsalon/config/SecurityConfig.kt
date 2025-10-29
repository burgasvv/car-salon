package org.burgas.carsalon.config

import org.burgas.carsalon.entity.identity.Authority.ADMIN
import org.burgas.carsalon.entity.identity.Authority.USER
import org.burgas.carsalon.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final val customUserDetailsService: CustomUserDetailsService
    private final val passwordEncoder: PasswordEncoder

    constructor(customUserDetailsService: CustomUserDetailsService, passwordEncoder: PasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService
        this.passwordEncoder = passwordEncoder
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider(this.customUserDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { it.configurationSource(UrlBasedCorsConfigurationSource()) }
            .csrf { it.csrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler()) }
            .httpBasic { it.securityContextRepository(RequestAttributeSecurityContextRepository()) }
            .authenticationManager(this.authenticationManager())
            .authorizeHttpRequests {

                it
                    .requestMatchers(
                        "/api/v1/security/csrf-token",

                        "/api/v1/media/by-id", "/api/v1/media/create", "/api/v1/media/change", "/api/v1/media/delete",

                        "/api/v1/identities/create"
                    )
                    .permitAll()

                    .requestMatchers(
                        "/api/v1/identities/by-id", "/api/v1/identities/update", "/api/v1/identities/delete",
                        "/api/v1/identities/change-password", "/api/v1/identities/add-images", "/api/v1/identities/remove-images"
                    )
                    .hasAnyAuthority(ADMIN.authority, USER.authority)

                    .requestMatchers(
                        "/api/v1/identities"
                    )
                    .hasAnyAuthority(ADMIN.authority)
            }
            .build()
    }
}
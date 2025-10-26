package org.burgas.carsalon.service

import org.burgas.carsalon.exception.IdentityNotFoundException
import org.burgas.carsalon.message.IdentityMessages
import org.burgas.carsalon.repository.IdentityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    private final val identityRepository: IdentityRepository

    constructor(identityRepository: IdentityRepository) {
        this.identityRepository = identityRepository
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return this.identityRepository.findByEmail(username)
            .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }
    }
}
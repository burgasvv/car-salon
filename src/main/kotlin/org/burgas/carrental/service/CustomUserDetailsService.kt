package org.burgas.carrental.service

import org.burgas.carrental.entity.identity.IdentityDetails
import org.burgas.carrental.exception.IdentityNotFoundException
import org.burgas.carrental.message.IdentityMessages
import org.burgas.carrental.repository.IdentityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CustomUserDetailsService : UserDetailsService {

    private final val identityRepository: IdentityRepository

    constructor(identityRepository: IdentityRepository) {
        this.identityRepository = identityRepository
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val identity = this.identityRepository.findByEmail(username)
            .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }
        return IdentityDetails(identity)
    }
}
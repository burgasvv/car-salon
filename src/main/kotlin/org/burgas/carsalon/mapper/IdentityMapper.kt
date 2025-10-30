package org.burgas.carsalon.mapper

import org.burgas.carsalon.dto.identity.IdentityFullResponse
import org.burgas.carsalon.dto.identity.IdentityRequest
import org.burgas.carsalon.dto.identity.IdentityShortResponse
import org.burgas.carsalon.entity.identity.Identity
import org.burgas.carsalon.repository.IdentityRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdentityMapper : EntityMapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    final val identityRepository: IdentityRepository
    private final val passwordEncoder: PasswordEncoder

    constructor(identityRepository: IdentityRepository, passwordEncoder: PasswordEncoder) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
    }

    override fun toEntity(request: IdentityRequest): Identity {
        val identityId = request.id ?: UUID.randomUUID()
        return this.identityRepository.findById(identityId)
            .map { identity ->
                Identity().apply {
                    this.id = identity.id
                    this.authority = request.authority ?: identity.authority
                    this.email = request.email ?: identity.email
                    this.password = identity.password
                    this.enabled = identity.enabled
                    this.firstname = request.firstname ?: identity.firstname
                    this.lastname = request.lastname ?: identity.lastname
                    this.patronymic = request.patronymic ?: identity.patronymic
                }
            }
            .orElseGet {
                val password = request.password ?: throw IllegalArgumentException("No password specified.")
                Identity().apply {
                    this.authority = request.authority ?: throw IllegalArgumentException("No authority specified.")
                    this.email = request.email ?: throw IllegalArgumentException("No email specified.")
                    this.password = passwordEncoder.encode(password)
                    this.enabled = true
                    this.firstname = request.firstname ?: throw IllegalArgumentException("No first name specified.")
                    this.lastname = request.lastname ?: throw IllegalArgumentException("No last name specified.")
                    this.patronymic = request.patronymic ?: throw IllegalArgumentException("No patronymic specified.")
                }
            }
    }

    override fun toShortResponse(entity: Identity): IdentityShortResponse {
        return IdentityShortResponse(
            id = entity.id,
            authority = entity.authority,
            email = entity.email,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic
        )
    }

    override fun toFullResponse(entity: Identity): IdentityFullResponse {
        return IdentityFullResponse(
            id = entity.id,
            authority = entity.authority,
            email = entity.email,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic,
            media = entity.media
        )
    }
}
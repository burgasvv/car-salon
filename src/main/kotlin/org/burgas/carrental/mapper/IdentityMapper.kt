package org.burgas.carrental.mapper

import org.burgas.carrental.dto.identity.IdentityFullResponse
import org.burgas.carrental.dto.identity.IdentityRequest
import org.burgas.carrental.dto.identity.IdentityShortResponse
import org.burgas.carrental.entity.identity.Identity
import org.burgas.carrental.mapper.contract.EntityMapper
import org.burgas.carrental.repository.IdentityRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdentityMapper : EntityMapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    final val identityRepository: IdentityRepository
    private final val passwordEncoder: PasswordEncoder
    private final val rentMapperObjectFactory: ObjectFactory<RentMapper>

    constructor(
        identityRepository: IdentityRepository,
        passwordEncoder: PasswordEncoder,
        rentMapperObjectFactory: ObjectFactory<RentMapper>
    ) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
        this.rentMapperObjectFactory = rentMapperObjectFactory
    }

    private fun getRentMapper(): RentMapper = this.rentMapperObjectFactory.`object`

    override fun toEntity(request: IdentityRequest): Identity = this.identityRepository.findById(request.id ?: UUID.randomUUID())
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

    override fun toShortResponse(entity: Identity): IdentityShortResponse = IdentityShortResponse(
        id = entity.id,
        authority = entity.authority,
        email = entity.email,
        firstname = entity.firstname,
        lastname = entity.lastname,
        patronymic = entity.patronymic
    )

    override fun toFullResponse(entity: Identity): IdentityFullResponse = IdentityFullResponse(
        id = entity.id,
        authority = entity.authority,
        email = entity.email,
        firstname = entity.firstname,
        lastname = entity.lastname,
        patronymic = entity.patronymic,
        rents = entity.rents.map { this.getRentMapper().toRentWithCarResponse(it) },
        media = entity.media
    )
}
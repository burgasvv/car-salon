package org.burgas.carsalon.service

import org.burgas.carsalon.dto.identity.IdentityFullResponse
import org.burgas.carsalon.dto.identity.IdentityRequest
import org.burgas.carsalon.dto.identity.IdentityShortResponse
import org.burgas.carsalon.entity.identity.Identity
import org.burgas.carsalon.exception.IdentityNotFoundException
import org.burgas.carsalon.mapper.IdentityMapper
import org.burgas.carsalon.message.IdentityMessages
import org.burgas.carsalon.repository.IdentityRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class IdentityService : BaseService, CrudService<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityRepository: IdentityRepository
    private final val identityMapper: IdentityMapper

    constructor(identityRepository: IdentityRepository, identityMapper: IdentityMapper) {
        this.identityRepository = identityRepository
        this.identityMapper = identityMapper
    }

    override fun findEntity(id: UUID): Identity {
        return this.identityRepository.findById(id)
            .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }
    }

    override fun findAll(): List<IdentityShortResponse> {
        return this.identityRepository.findAll()
            .map { identity -> this.identityMapper.toShortResponse(identity) }
    }

    @Cacheable(value = ["identityFullResponse"], key = "#id")
    override fun findById(id: UUID): IdentityFullResponse {
        return this.identityMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun create(request: IdentityRequest): IdentityFullResponse {
        return this.identityMapper.toFullResponse(
            this.identityRepository.save(this.identityMapper.toEntity(request))
        )
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#request.id")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun update(request: IdentityRequest): IdentityFullResponse {
        return this.identityMapper.toFullResponse(
            this.identityRepository.save(this.identityMapper.toEntity(request))
        )
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#id")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun delete(id: UUID) {
        val entity = this.findEntity(id)
        this.identityRepository.delete(entity)
    }
}
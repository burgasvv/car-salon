package org.burgas.carrental.service

import jakarta.servlet.http.Part
import org.burgas.carrental.dto.identity.IdentityFullResponse
import org.burgas.carrental.dto.identity.IdentityRequest
import org.burgas.carrental.dto.identity.IdentityShortResponse
import org.burgas.carrental.entity.identity.Identity
import org.burgas.carrental.exception.IdentityMatchedFlagException
import org.burgas.carrental.exception.IdentityNotFoundException
import org.burgas.carrental.exception.PasswordMatchedException
import org.burgas.carrental.mapper.IdentityMapper
import org.burgas.carrental.message.IdentityMessages
import org.burgas.carrental.service.contract.BaseService
import org.burgas.carrental.service.contract.CrudService
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
@CacheConfig(cacheManager = "identityCacheManager")
class IdentityService : BaseService, CrudService<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityMapper: IdentityMapper
    private final val passwordEncoder: PasswordEncoder
    private final val mediaService: MediaService

    constructor(identityMapper: IdentityMapper, passwordEncoder: PasswordEncoder, mediaService: MediaService) {
        this.identityMapper = identityMapper
        this.passwordEncoder = passwordEncoder
        this.mediaService = mediaService
    }

    override fun findEntity(id: UUID): Identity = this.identityMapper.identityRepository.findById(id)
        .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }

    override fun findAll(): List<IdentityShortResponse> = this.identityMapper.identityRepository.findAll()
        .map { identity -> this.identityMapper.toShortResponse(identity) }

    @Cacheable(value = ["identityFullResponse"], key = "#id")
    override fun findById(id: UUID): IdentityFullResponse = this.identityMapper.toFullResponse(this.findEntity(id))

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun create(request: IdentityRequest): IdentityFullResponse = this.identityMapper.toFullResponse(
        this.identityMapper.identityRepository.save(this.identityMapper.toEntity(request))
    )

    @CacheEvict(value = ["identityFullResponse"], key = "#request.id")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun update(request: IdentityRequest): IdentityFullResponse = this.identityMapper.toFullResponse(
        this.identityMapper.identityRepository.save(this.identityMapper.toEntity(request))
    )

    @CacheEvict(value = ["identityFullResponse"], key = "#id")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    override fun delete(id: UUID) {
        val entity = this.findEntity(id)
        this.identityMapper.identityRepository.delete(entity)
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#identityId")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun changePassword(identityId: UUID, newPassword: String) {
        val identity = this.findEntity(identityId)
        if (this.passwordEncoder.matches(newPassword, identity.password)) {
            throw PasswordMatchedException(IdentityMessages.PASSWORD_MATCHED.message)
        }
        identity.apply {
            this.password = passwordEncoder.encode(newPassword)
        }
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#identityId")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun addImages(identityId: UUID, parts: List<Part>) {
        val identity = this.findEntity(identityId)
        parts.forEach {
            val media = this.mediaService.create(it)
            identity.media.add(media)
        }
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#identityId")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun removeImages(identityId: UUID, mediaIds: List<UUID>) {
        val identity = this.findEntity(identityId)
        mediaIds.forEach { mediaId ->
            identity.media.removeIf { media -> mediaId == media.id }
            mediaService.delete(mediaId)
        }
    }

    @CacheEvict(value = ["identityFullResponse"], key = "#identityId")
    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [RuntimeException::class, Throwable::class]
    )
    fun enableOrDisable(identityId: UUID, flag: Boolean) {
        val identity = this.findEntity(identityId)
        if (identity.enabled != flag) {
            identity.apply {
                this.enabled = flag
            }
        } else {
            throw IdentityMatchedFlagException(IdentityMessages.MATCHED_FLAG.message)
        }
    }
}
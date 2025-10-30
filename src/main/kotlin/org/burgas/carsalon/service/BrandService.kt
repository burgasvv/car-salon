package org.burgas.carsalon.service

import org.burgas.carsalon.dto.brand.BrandFullResponse
import org.burgas.carsalon.dto.brand.BrandRequest
import org.burgas.carsalon.dto.brand.BrandShortResponse
import org.burgas.carsalon.entity.brand.Brand
import org.burgas.carsalon.exception.BrandNotFoundException
import org.burgas.carsalon.mapper.BrandMapper
import org.burgas.carsalon.message.BrandMessages
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
@CacheConfig(cacheManager = "brandCacheManager")
class BrandService : BaseService, CrudService<BrandRequest, Brand, BrandShortResponse, BrandFullResponse> {

    private final val brandMapper: BrandMapper

    constructor(brandMapper: BrandMapper) {
        this.brandMapper = brandMapper
    }

    override fun findEntity(id: UUID): Brand {
        return this.brandMapper.brandRepository.findById(id)
            .orElseThrow { throw BrandNotFoundException(BrandMessages.BRAND_NOT_FOUND.message) }
    }

    override fun findAll(): List<BrandShortResponse> {
        return this.brandMapper.brandRepository.findAll()
            .map { this.brandMapper.toShortResponse(it) }
    }

    @Cacheable(value = ["brandFullResponse"], key = "#id")
    override fun findById(id: UUID): BrandFullResponse {
        return this.brandMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(request: BrandRequest): BrandFullResponse {
        return this.brandMapper.toFullResponse(
            this.brandMapper.brandRepository.save(this.brandMapper.toEntity(request))
        )
    }

    @CacheEvict(value = ["brandFullResponse"], key = "#request.id")
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(request: BrandRequest): BrandFullResponse {
        return this.brandMapper.toFullResponse(
            this.brandMapper.brandRepository.save(this.brandMapper.toEntity(request))
        )
    }

    @CacheEvict(value = ["brandFullResponse"], key = "#id")
    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID) {
        val brand = this.findEntity(id)
        this.brandMapper.brandRepository.delete(brand)
    }
}
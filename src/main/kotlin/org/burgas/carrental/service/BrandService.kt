package org.burgas.carrental.service

import org.burgas.carrental.dto.brand.BrandFullResponse
import org.burgas.carrental.dto.brand.BrandRequest
import org.burgas.carrental.dto.brand.BrandShortResponse
import org.burgas.carrental.entity.brand.Brand
import org.burgas.carrental.exception.BrandNotFoundException
import org.burgas.carrental.mapper.BrandMapper
import org.burgas.carrental.message.BrandMessages
import org.burgas.carrental.service.contract.BaseService
import org.burgas.carrental.service.contract.CrudService
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
class BrandService : BaseService, CrudService<BrandRequest, Brand, BrandShortResponse, BrandFullResponse> {

    private final val brandMapper: BrandMapper

    constructor(brandMapper: BrandMapper) {
        this.brandMapper = brandMapper
    }

    override fun findEntity(id: UUID): Brand = this.brandMapper.brandRepository.findById(id)
        .orElseThrow { throw BrandNotFoundException(BrandMessages.BRAND_NOT_FOUND.message) }

    override fun findAll(): List<BrandShortResponse> = this.brandMapper.brandRepository.findAll()
        .map { this.brandMapper.toShortResponse(it) }

    override fun findById(id: UUID): BrandFullResponse = this.brandMapper.toFullResponse(this.findEntity(id))

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(request: BrandRequest): BrandFullResponse = this.brandMapper.toFullResponse(
        this.brandMapper.brandRepository.save(this.brandMapper.toEntity(request))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(request: BrandRequest): BrandFullResponse = this.brandMapper.toFullResponse(
        this.brandMapper.brandRepository.save(this.brandMapper.toEntity(request))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID) {
        val brand = this.findEntity(id)
        this.brandMapper.brandRepository.delete(brand)
    }
}
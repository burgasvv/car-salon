package org.burgas.carrental.mapper

import org.burgas.carrental.dto.brand.BrandFullResponse
import org.burgas.carrental.dto.brand.BrandRequest
import org.burgas.carrental.dto.brand.BrandShortResponse
import org.burgas.carrental.entity.brand.Brand
import org.burgas.carrental.mapper.contract.EntityMapper
import org.burgas.carrental.repository.BrandRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BrandMapper : EntityMapper<BrandRequest, Brand, BrandShortResponse, BrandFullResponse> {

    final val brandRepository: BrandRepository
    private final val carMapperObjectFactory: ObjectFactory<CarMapper>

    private fun getCarMapper(): CarMapper = this.carMapperObjectFactory.`object`

    constructor(brandRepository: BrandRepository, carMapperObjectFactory: ObjectFactory<CarMapper>) {
        this.brandRepository = brandRepository
        this.carMapperObjectFactory = carMapperObjectFactory
    }

    override fun toEntity(request: BrandRequest): Brand = this.brandRepository.findById(request.id ?: UUID.randomUUID())
        .map {
            Brand().apply {
                this.id = it.id
                this.name = request.name ?: it.name
                this.description = request.description ?: it.description
            }
        }
        .orElseGet {
            Brand().apply {
                this.name = request.name ?: throw NullPointerException("Name is null")
                this.description = request.description ?: throw NullPointerException("Description is null")
            }
        }

    override fun toShortResponse(entity: Brand): BrandShortResponse = BrandShortResponse(
        id = entity.id,
        name = entity.name,
        description = entity.description
    )

    override fun toFullResponse(entity: Brand): BrandFullResponse = BrandFullResponse(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        cars = entity.cars.map { this.getCarMapper().toShortResponse(it) }
    )
}
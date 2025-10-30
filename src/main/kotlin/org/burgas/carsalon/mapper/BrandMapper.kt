package org.burgas.carsalon.mapper

import org.burgas.carsalon.dto.brand.BrandFullResponse
import org.burgas.carsalon.dto.brand.BrandRequest
import org.burgas.carsalon.dto.brand.BrandShortResponse
import org.burgas.carsalon.entity.brand.Brand
import org.burgas.carsalon.repository.BrandRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BrandMapper : EntityMapper<BrandRequest, Brand, BrandShortResponse, BrandFullResponse> {

    final val brandRepository: BrandRepository
    private final val carMapperObjectFactory: ObjectFactory<CarMapper>

    private fun getCarMapper(): CarMapper {
        return this.carMapperObjectFactory.`object`
    }

    constructor(brandRepository: BrandRepository, carMapperObjectFactory: ObjectFactory<CarMapper>) {
        this.brandRepository = brandRepository
        this.carMapperObjectFactory = carMapperObjectFactory
    }

    override fun toEntity(request: BrandRequest): Brand {
        val brandId = request.id ?: UUID.randomUUID()
        return this.brandRepository.findById(brandId)
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
    }

    override fun toShortResponse(entity: Brand): BrandShortResponse {
        return BrandShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }

    override fun toFullResponse(entity: Brand): BrandFullResponse {
        return BrandFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            cars = entity.cars.map { this.getCarMapper().toShortResponse(it) }
        )
    }
}
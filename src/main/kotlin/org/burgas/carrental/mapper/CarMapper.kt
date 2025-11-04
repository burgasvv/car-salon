package org.burgas.carrental.mapper

import org.burgas.carrental.dto.car.CarFullResponse
import org.burgas.carrental.dto.car.CarRequest
import org.burgas.carrental.dto.car.CarShortResponse
import org.burgas.carrental.entity.car.Car
import org.burgas.carrental.mapper.contract.EntityMapper
import org.burgas.carrental.repository.BrandRepository
import org.burgas.carrental.repository.CarRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class CarMapper : EntityMapper<CarRequest, Car, CarShortResponse, CarFullResponse> {

    final val carRepository: CarRepository
    private final val brandMapperObjectFactory: ObjectFactory<BrandMapper>
    private final val brandRepository: BrandRepository
    private final val rentMapperObjectFactory: ObjectFactory<RentMapper>

    constructor(
        carRepository: CarRepository,
        brandMapperObjectFactory: ObjectFactory<BrandMapper>,
        brandRepository: BrandRepository,
        rentMapperObjectFactory: ObjectFactory<RentMapper>
    ) {
        this.carRepository = carRepository
        this.brandMapperObjectFactory = brandMapperObjectFactory
        this.brandRepository = brandRepository
        this.rentMapperObjectFactory = rentMapperObjectFactory
    }

    private fun getBrandMapper(): BrandMapper = this.brandMapperObjectFactory.`object`

    private fun getRentMapper(): RentMapper = this.rentMapperObjectFactory.`object`

    override fun toEntity(request: CarRequest): Car = this.carRepository.findById(request.id ?: UUID.randomUUID())
            .map {
                Car().apply {
                    this.id = it.id
                    this.brand = brandRepository.findById(request.brandId ?: UUID.randomUUID()).orElse(null) ?: it.brand
                    this.model = request.model ?: it.model
                    this.characteristics = request.characteristics ?: it.characteristics
                    this.rentPrice = request.rentPrice ?: it.rentPrice
                }
            }
            .orElseGet {
                Car().apply {
                    this.brand = brandRepository.findById(request.brandId ?: UUID.randomUUID()).orElse(null)
                        ?: throw NullPointerException("Brand not found")
                    this.model = request.model ?: throw NullPointerException("Model is null")
                    this.characteristics = request.characteristics ?: throw NullPointerException("Characteristics is null")
                    this.rentPrice = request.rentPrice ?: throw NullPointerException("Rent Price is null")
                }
            }

    override fun toShortResponse(entity: Car): CarShortResponse = CarShortResponse(
        id = entity.id,
        brand = Optional.ofNullable(entity.brand)
            .map { this.getBrandMapper().toShortResponse(it) }
            .orElse(null),
        model = entity.model,
        characteristics = entity.characteristics,
        rentPrice = entity.rentPrice
    )

    override fun toFullResponse(entity: Car): CarFullResponse = CarFullResponse(
        id = entity.id,
        brand = Optional.ofNullable(entity.brand)
            .map { this.getBrandMapper().toShortResponse(it) }
            .orElse(null),
        model = entity.model,
        characteristics = entity.characteristics,
        rentPrice = entity.rentPrice,
        rents = entity.rents.map { this.getRentMapper().toRentWithIdentityResponse(it) },
        media = entity.media
    )
}
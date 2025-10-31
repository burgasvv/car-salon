package org.burgas.carrental.mapper

import org.burgas.carrental.dto.car.CarFullResponse
import org.burgas.carrental.dto.car.CarRequest
import org.burgas.carrental.dto.car.CarShortResponse
import org.burgas.carrental.entity.car.Car
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

    private fun getBrandMapper(): BrandMapper {
        return this.brandMapperObjectFactory.`object`
    }

    constructor(
        carRepository: CarRepository,
        brandMapperObjectFactory: ObjectFactory<BrandMapper>,
        brandRepository1: BrandRepository
    ) {
        this.carRepository = carRepository
        this.brandMapperObjectFactory = brandMapperObjectFactory
        this.brandRepository = brandRepository1
    }

    override fun toEntity(request: CarRequest): Car {
        val carId = request.id ?: UUID.randomUUID()
        return this.carRepository.findById(carId)
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
    }

    override fun toShortResponse(entity: Car): CarShortResponse {
        return CarShortResponse(
            id = entity.id,
            brand = Optional.ofNullable(entity.brand)
                .map { this.getBrandMapper().toShortResponse(it) }
                .orElse(null),
            model = entity.model,
            characteristics = entity.characteristics,
            rentPrice = entity.rentPrice
        )
    }

    override fun toFullResponse(entity: Car): CarFullResponse {
        return CarFullResponse(
            id = entity.id,
            brand = Optional.ofNullable(entity.brand)
                .map { this.getBrandMapper().toShortResponse(it) }
                .orElse(null),
            model = entity.model,
            characteristics = entity.characteristics,
            rentPrice = entity.rentPrice
        )
    }
}
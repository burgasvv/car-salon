package org.burgas.carsalon.service

import org.burgas.carsalon.dto.car.CarFullResponse
import org.burgas.carsalon.dto.car.CarRequest
import org.burgas.carsalon.dto.car.CarShortResponse
import org.burgas.carsalon.entity.car.Car
import org.burgas.carsalon.exception.CarNotFoundException
import org.burgas.carsalon.mapper.CarMapper
import org.burgas.carsalon.message.CarMessages
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
@CacheConfig(cacheManager = "carCacheManager")
class CarService : BaseService, CrudService<CarRequest, Car, CarShortResponse, CarFullResponse> {

    private final val carMapper: CarMapper

    constructor(carMapper: CarMapper) {
        this.carMapper = carMapper
    }

    override fun findEntity(id: UUID): Car {
        return this.carMapper.carRepository.findById(id)
            .orElseThrow { throw CarNotFoundException(CarMessages.CAR_NOT_FOUND.message) }
    }

    override fun findAll(): List<CarShortResponse> {
        return this.carMapper.carRepository.findAll()
            .map { this.carMapper.toShortResponse(it) }
    }

    @Cacheable(value = ["carFullResponse"], key = "#id")
    override fun findById(id: UUID): CarFullResponse {
        return this.carMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(request: CarRequest): CarFullResponse {
        return this.carMapper.toFullResponse(
            this.carMapper.carRepository.save(this.carMapper.toEntity(request))
        )
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#request.id")
    override fun update(request: CarRequest): CarFullResponse {
        return this.carMapper.toFullResponse(
            this.carMapper.carRepository.save(this.carMapper.toEntity(request))
        )
    }

    @Transactional(
        isolation = Isolation.REPEATABLE_READ,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#id")
    override fun delete(id: UUID) {
        val car = this.findEntity(id)
        this.carMapper.carRepository.delete(car)
    }
}
package org.burgas.carrental.service

import jakarta.servlet.http.Part
import org.burgas.carrental.dto.car.CarFullResponse
import org.burgas.carrental.dto.car.CarRequest
import org.burgas.carrental.dto.car.CarShortResponse
import org.burgas.carrental.entity.car.Car
import org.burgas.carrental.exception.CarNotFoundException
import org.burgas.carrental.mapper.CarMapper
import org.burgas.carrental.message.CarMessages
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
@CacheConfig(cacheManager = "carCacheManager")
class CarService : BaseService, CrudService<CarRequest, Car, CarShortResponse, CarFullResponse> {

    private final val carMapper: CarMapper
    private final val mediaService: MediaService

    constructor(carMapper: CarMapper, mediaService1: MediaService) {
        this.carMapper = carMapper
        this.mediaService = mediaService1
    }

    override fun findEntity(id: UUID): Car = this.carMapper.carRepository.findById(id)
        .orElseThrow { throw CarNotFoundException(CarMessages.CAR_NOT_FOUND.message) }

    override fun findAll(): List<CarShortResponse> = this.carMapper.carRepository.findAll()
        .map { this.carMapper.toShortResponse(it) }

    @Cacheable(value = ["carFullResponse"], key = "#id")
    override fun findById(id: UUID): CarFullResponse = this.carMapper.toFullResponse(this.findEntity(id))

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(request: CarRequest): CarFullResponse = this.carMapper.toFullResponse(
        this.carMapper.carRepository.save(this.carMapper.toEntity(request))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#request.id")
    override fun update(request: CarRequest): CarFullResponse = this.carMapper.toFullResponse(
        this.carMapper.carRepository.save(this.carMapper.toEntity(request))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#id")
    override fun delete(id: UUID) {
        val car = this.findEntity(id)
        this.carMapper.carRepository.delete(car)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#carId")
    fun addImages(carId: UUID, parts: List<Part>) {
        val car = this.findEntity(carId)
        parts.forEach {
            val media = this.mediaService.create(it)
            car.media.add(media)
        }
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    @CacheEvict(value = ["carFullResponse"], key = "#carId")
    fun removeImages(carId: UUID, mediaIds: List<UUID>) {
        val car = this.findEntity(carId)
        mediaIds.forEach { mediaId ->
            car.media.removeIf { media -> media.id == mediaId }
            this.mediaService.delete(mediaId)
        }
    }
}
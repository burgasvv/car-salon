package org.burgas.carrental.service

import org.burgas.carrental.dto.rent.RentFullResponse
import org.burgas.carrental.dto.rent.RentRequest
import org.burgas.carrental.dto.rent.RentWithCarResponse
import org.burgas.carrental.dto.rent.RentWithIdentityResponse
import org.burgas.carrental.entity.rent.Rent
import org.burgas.carrental.exception.RentNotFoundException
import org.burgas.carrental.mapper.RentMapper
import org.burgas.carrental.message.RentMessages
import org.burgas.carrental.service.contract.BaseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class RentService : BaseService {

    private final val rentMapper: RentMapper
    private final val carService: CarService
    private final val identityService: IdentityService

    constructor(rentMapper: RentMapper, carService: CarService, identityService: IdentityService) {
        this.rentMapper = rentMapper
        this.carService = carService
        this.identityService = identityService
    }

    fun findEntity(rentId: UUID): Rent = this.rentMapper.rentRepository.findById(rentId)
        .orElseThrow { throw RentNotFoundException(RentMessages.RENT_NOT_FOUND.message) }

    fun findById(rentId: UUID): RentFullResponse = this.rentMapper.toFullResponse(this.findEntity(rentId))

    fun findByIdentityId(identityId: UUID): List<RentWithCarResponse> {
        val identity = this.identityService.findEntity(identityId)
        return this.rentMapper.rentRepository.findRentsByIdentity(identity)
            .map { this.rentMapper.toRentWithCarResponse(it) }
    }

    fun findByCarId(carId: UUID): List<RentWithIdentityResponse> {
        val car = this.carService.findEntity(carId)
        return this.rentMapper.rentRepository.findRentsByCar(car)
            .map { this.rentMapper.toRentWithIdentityResponse(it) }
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun create(rentRequest: RentRequest): RentFullResponse = this.rentMapper.toFullResponse(
        this.rentMapper.rentRepository.save(this.rentMapper.toEntity(rentRequest))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun update(rentRequest: RentRequest): RentFullResponse = this.rentMapper.toFullResponse(
        this.rentMapper.rentRepository.save(this.rentMapper.toEntity(rentRequest))
    )

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun delete(rentId: UUID) {
        val rent = this.findEntity(rentId)
        this.rentMapper.rentRepository.delete(rent)
    }
}
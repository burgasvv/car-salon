package org.burgas.carrental.mapper

import org.burgas.carrental.dto.rent.RentFullResponse
import org.burgas.carrental.dto.rent.RentRequest
import org.burgas.carrental.dto.rent.RentWithCarResponse
import org.burgas.carrental.dto.rent.RentWithIdentityResponse
import org.burgas.carrental.entity.rent.Rent
import org.burgas.carrental.mapper.contract.BasicMapper
import org.burgas.carrental.repository.RentRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class RentMapper : BasicMapper<RentRequest, Rent> {

    final val rentRepository: RentRepository
    private final val carMapperObjectFactory: ObjectFactory<CarMapper>
    private final val identityMapperObjectFactory: ObjectFactory<IdentityMapper>

    constructor(
        rentRepository: RentRepository,
        carMapperObjectFactory: ObjectFactory<CarMapper>,
        identityMapperObjectFactory: ObjectFactory<IdentityMapper>
    ) {
        this.rentRepository = rentRepository
        this.carMapperObjectFactory = carMapperObjectFactory
        this.identityMapperObjectFactory = identityMapperObjectFactory
    }

    private fun getCarMapper(): CarMapper = this.carMapperObjectFactory.`object`

    private fun getIdentityMapper(): IdentityMapper = this.identityMapperObjectFactory.`object`

    override fun toEntity(request: RentRequest): Rent = this.rentRepository.findById(request.id ?: UUID.randomUUID())
        .map {
            Rent().apply {
                this.id = it.id

                this.identity = getIdentityMapper().identityRepository.findById(request.identityId ?: UUID.randomUUID())
                    .orElse(null) ?: it.identity

                this.car = getCarMapper().carRepository.findById(request.carId ?: UUID.randomUUID())
                    .orElse(null) ?: it.car

                if (request.startTime == null)
                    this.startTime = it.startTime

                if (request.endTime == null)
                    this.endTime = it.endTime

                if (request.startTime != null && request.endTime != null) {
                    if (startTime.isAfter(endTime))
                        throw IllegalArgumentException("Illegal time input")

                    this.startTime = request.startTime
                    this.endTime = request.endTime

                    val hours = TimeUnit.HOURS.toChronoUnit()
                        .between(this.startTime, this.endTime)

                    this.price = this.car.rentPrice * hours

                } else {
                    this.price = it.price
                }
            }
        }
        .orElseGet {
            Rent().apply {
                this.identity = getIdentityMapper().identityRepository.findById(request.identityId ?: UUID.randomUUID())
                    .orElse(null) ?: throw NullPointerException("Identity is null")

                this.car = getCarMapper().carRepository.findById(request.carId ?: UUID.randomUUID())
                    .orElse(null) ?: throw NullPointerException("Car is null")

                val startTime = request.startTime ?: throw NullPointerException("Start time is null")
                val endTime = request.endTime ?: throw NullPointerException("Ent tim is null")

                if (startTime.isAfter(endTime))
                    throw IllegalArgumentException("Illegal time input")

                this.startTime = startTime
                this.endTime = endTime

                val hours = TimeUnit.HOURS.toChronoUnit()
                    .between(this.startTime, this.endTime)

                this.price = this.car.rentPrice * hours
            }
        }

    fun toRentWithIdentityResponse(rent: Rent): RentWithIdentityResponse = RentWithIdentityResponse(
            id = rent.id,
            identity = Optional.ofNullable(rent.identity)
                .map { this.getIdentityMapper().toShortResponse(it) }
                .orElse(null),
            startTime = this.dateFormat(rent.startTime),
            endTime = this.dateFormat(rent.endTime),
            price = rent.price,
            closed = rent.closed
        )

    fun toRentWithCarResponse(rent: Rent): RentWithCarResponse = RentWithCarResponse(
            id = rent.id,
            car = Optional.ofNullable(rent.car)
                .map { this.getCarMapper().toShortResponse(it) }
                .orElse(null),
            startTime = this.dateFormat(rent.startTime),
            endTime = this.dateFormat(rent.endTime),
            price = rent.price,
            closed = rent.closed
        )

    fun toFullResponse(rent: Rent): RentFullResponse = RentFullResponse(
            id = rent.id,
            identity = Optional.ofNullable(rent.identity)
                .map { this.getIdentityMapper().toShortResponse(it) }
                .orElse(null),
            car = Optional.ofNullable(rent.car)
                .map { this.getCarMapper().toShortResponse(it) }
                .orElse(null),
            startTime = this.dateFormat(rent.startTime),
            endTime = this.dateFormat(rent.endTime),
            price = rent.price,
            closed = rent.closed
        )

    private fun dateFormat(localDateTime: LocalDateTime): String? = localDateTime.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm")
    )
}
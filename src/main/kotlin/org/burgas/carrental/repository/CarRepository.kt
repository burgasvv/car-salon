package org.burgas.carrental.repository

import jakarta.persistence.LockModeType
import org.burgas.carrental.entity.car.Car
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CarRepository : JpaRepository<Car, UUID> {

    @EntityGraph(value = "car-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Car>

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    fun findCarById(id: UUID): Optional<Car>
}
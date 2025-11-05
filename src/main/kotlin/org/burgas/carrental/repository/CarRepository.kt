package org.burgas.carrental.repository

import jakarta.persistence.LockModeType
import org.burgas.carrental.entity.car.Car
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CarRepository : JpaRepository<Car, UUID> {

    @Query(value = "select c from org.burgas.carrental.entity.car.Car c left join fetch c.brand left join fetch c.media")
    override fun findAll(): List<Car>

    @Query(value = "select c from org.burgas.carrental.entity.car.Car c left join fetch c.rents")
    fun findAllScheduled(): List<Car>

    @EntityGraph(value = "car-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Car>

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    fun findCarById(id: UUID): Optional<Car>
}
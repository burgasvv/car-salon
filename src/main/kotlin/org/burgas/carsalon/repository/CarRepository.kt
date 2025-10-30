package org.burgas.carsalon.repository

import org.burgas.carsalon.entity.car.Car
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CarRepository : JpaRepository<Car, UUID> {

    @EntityGraph(value = "car-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Car>
}
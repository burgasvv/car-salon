package org.burgas.carrental.repository

import org.burgas.carrental.entity.car.Car
import org.burgas.carrental.entity.identity.Identity
import org.burgas.carrental.entity.rent.Rent
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface RentRepository : JpaRepository<Rent, UUID> {

    @EntityGraph(value = "rent-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Rent>

    @EntityGraph(value = "rent-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    fun findRentsByIdentity(identity: Identity): MutableList<Rent>

    @EntityGraph(value = "rent-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    fun findRentsByCar(car: Car): MutableList<Rent>
}
package org.burgas.carsalon.repository

import org.burgas.carsalon.entity.identity.Identity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface IdentityRepository : JpaRepository<Identity, UUID> {

    @EntityGraph(value = "identity-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Identity>

    fun findByEmail(email: String): Optional<Identity>
}
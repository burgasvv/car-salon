package org.burgas.carrental.repository

import org.burgas.carrental.entity.identity.Identity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface IdentityRepository : JpaRepository<Identity, UUID> {

    @Query(value = "select i from org.burgas.carrental.entity.identity.Identity i left join fetch i.media")
    override fun findAll(): List<Identity>

    @EntityGraph(value = "identity-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Identity>

    fun findByEmail(email: String): Optional<Identity>
}
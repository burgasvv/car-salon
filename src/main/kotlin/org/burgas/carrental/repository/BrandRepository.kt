package org.burgas.carrental.repository

import org.burgas.carrental.entity.brand.Brand
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface BrandRepository : JpaRepository<Brand, UUID> {

    @EntityGraph(value = "brand-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Brand>
}
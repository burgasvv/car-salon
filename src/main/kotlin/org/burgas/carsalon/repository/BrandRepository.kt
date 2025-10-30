package org.burgas.carsalon.repository

import org.burgas.carsalon.entity.brand.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BrandRepository : JpaRepository<Brand, UUID>
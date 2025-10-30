package org.burgas.carsalon.dto.brand

import org.burgas.carsalon.dto.Request
import java.util.UUID

data class BrandRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Request

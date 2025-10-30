package org.burgas.carsalon.dto.car

import org.burgas.carsalon.dto.Response
import org.burgas.carsalon.dto.brand.BrandShortResponse
import java.util.UUID

data class CarFullResponse(
    override val id: UUID?,
    val brand: BrandShortResponse?,
    val model: String?,
    val characteristics: String?,
    val rentPrice: Double?
) : Response

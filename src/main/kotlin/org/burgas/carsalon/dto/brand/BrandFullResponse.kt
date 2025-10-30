package org.burgas.carsalon.dto.brand

import org.burgas.carsalon.dto.Response
import org.burgas.carsalon.dto.car.CarShortResponse
import java.util.UUID

data class BrandFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val cars: List<CarShortResponse>?
) : Response

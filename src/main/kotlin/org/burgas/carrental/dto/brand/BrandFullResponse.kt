package org.burgas.carrental.dto.brand

import org.burgas.carrental.dto.Response
import org.burgas.carrental.dto.car.CarShortResponse
import java.util.UUID

data class BrandFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val cars: List<CarShortResponse>?
) : Response

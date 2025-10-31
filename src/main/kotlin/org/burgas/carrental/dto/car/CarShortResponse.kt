package org.burgas.carrental.dto.car

import org.burgas.carrental.dto.Response
import org.burgas.carrental.dto.brand.BrandShortResponse
import java.util.UUID

data class CarShortResponse(
    override val id: UUID?,
    val brand: BrandShortResponse?,
    val model: String?,
    val characteristics: String?,
    val rentPrice: Double?
) : Response

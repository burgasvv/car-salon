package org.burgas.carsalon.dto.car

import org.burgas.carsalon.dto.Request
import java.util.UUID

data class CarRequest(
    override val id: UUID?,
    val brandId: UUID?,
    val model: String?,
    val characteristics: String?,
    val rentPrice: Double?
) : Request

package org.burgas.carrental.dto.car

import org.burgas.carrental.dto.Request
import java.util.UUID

data class CarRequest(
    override val id: UUID?,
    val brandId: UUID?,
    val model: String?,
    val characteristics: String?,
    val rentPrice: Double?,
    val free: Boolean?
) : Request

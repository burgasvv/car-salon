package org.burgas.carsalon.dto.brand

import org.burgas.carsalon.dto.Response
import java.util.*

data class BrandShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Response

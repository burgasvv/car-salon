package org.burgas.carrental.dto.brand

import org.burgas.carrental.dto.Response
import java.util.*

data class BrandShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Response

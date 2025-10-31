package org.burgas.carrental.dto.brand

import org.burgas.carrental.dto.Request
import java.util.UUID

data class BrandRequest(
    override val id: UUID?,
    val name: String?,
    val description: String?
) : Request

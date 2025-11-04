package org.burgas.carrental.dto.rent

import org.burgas.carrental.dto.Response
import org.burgas.carrental.dto.identity.IdentityShortResponse
import java.util.*

data class RentWithIdentityResponse(
    override val id: UUID?,
    val identity: IdentityShortResponse?,
    val startTime: String?,
    val endTime: String?,
    val price: Double?,
    val closed: Boolean?
) : Response

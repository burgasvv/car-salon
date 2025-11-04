package org.burgas.carrental.dto.rent

import org.burgas.carrental.dto.Response
import org.burgas.carrental.dto.car.CarShortResponse
import org.burgas.carrental.dto.identity.IdentityShortResponse
import java.util.UUID

data class RentFullResponse(
    override val id: UUID?,
    val identity: IdentityShortResponse?,
    val car: CarShortResponse?,
    val startTime: String?,
    val endTime: String?,
    val price: Double?,
    val closed: Boolean?
) : Response

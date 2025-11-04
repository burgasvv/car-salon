package org.burgas.carrental.dto.rent

import org.burgas.carrental.dto.Request
import java.time.LocalDateTime
import java.util.UUID

data class RentRequest(
    override val id: UUID?,
    val identityId: UUID?,
    val carId: UUID?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val closed: Boolean?
) : Request

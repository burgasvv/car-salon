package org.burgas.carrental.dto.rent

import org.burgas.carrental.dto.Response
import org.burgas.carrental.dto.car.CarShortResponse
import java.util.*

data class RentWithCarResponse(
    override val id: UUID?,
    val car: CarShortResponse?,
    val startTime: String?,
    val endTime: String?,
    val price: Double?,
    val closed: Boolean?
) : Response

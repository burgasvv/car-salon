package org.burgas.carsalon.dto.identity

import org.burgas.carsalon.dto.Response
import org.burgas.carsalon.entity.identity.Authority
import java.util.UUID

data class IdentityShortResponse(
    override val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val pass: String?,
    val enabled: Boolean?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?
) : Response

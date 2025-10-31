package org.burgas.carrental.dto.identity

import org.burgas.carrental.dto.Response
import org.burgas.carrental.entity.identity.Authority
import java.util.UUID

data class IdentityShortResponse(
    override val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?
) : Response

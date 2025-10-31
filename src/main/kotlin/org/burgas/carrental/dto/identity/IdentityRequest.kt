package org.burgas.carrental.dto.identity

import org.burgas.carrental.dto.Request
import org.burgas.carrental.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    override val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val password: String?,
    val enabled: Boolean?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?
) : Request

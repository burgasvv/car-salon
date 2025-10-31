package org.burgas.carrental.dto.identity

import org.burgas.carrental.dto.Response
import org.burgas.carrental.entity.identity.Authority
import org.burgas.carrental.entity.media.Media
import java.util.UUID

data class IdentityFullResponse(
    override val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val media: MutableList<Media>?
) : Response

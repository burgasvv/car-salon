package org.burgas.carsalon.mapper

import org.burgas.carsalon.dto.Request
import org.burgas.carsalon.dto.Response
import org.burgas.carsalon.entity.BaseEntity
import org.springframework.stereotype.Component

@Component
interface EntityMapper<in R : Request, E : BaseEntity, out S : Response, out F : Response> {

    fun toEntity(request: R): E

    fun toShortResponse(entity: E): S

    fun toFullResponse(entity: E): F
}
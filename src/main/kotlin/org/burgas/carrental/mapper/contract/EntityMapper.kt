package org.burgas.carrental.mapper.contract

import org.burgas.carrental.dto.Request
import org.burgas.carrental.dto.Response
import org.burgas.carrental.entity.BaseEntity
import org.springframework.stereotype.Component

@Component
interface EntityMapper<in R : Request, E : BaseEntity, out S : Response, out F : Response> : BasicMapper<R, E> {

    override fun toEntity(request: R): E

    fun toShortResponse(entity: E): S

    fun toFullResponse(entity: E): F
}
package org.burgas.carrental.mapper.contract

import org.burgas.carrental.dto.Request
import org.burgas.carrental.entity.BaseEntity
import org.springframework.stereotype.Component

@Component
interface BasicMapper<in R : Request, out E : BaseEntity> {

    fun toEntity(request: R): E
}